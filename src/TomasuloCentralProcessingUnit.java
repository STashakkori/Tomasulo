import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

/**
 * TomasuloCentralProcessingUnit :: Class that orchestratics the stages and cycles of the processor.
 * Created by rt on 11/30/14.
 */
public class TomasuloCentralProcessingUnit {

    static TomasuloCentralProcessingUnit cpuInstance = null;
    TomasuloInstructionFactory instructionBuilder;
    TomasuloDecoder decoder;
    TomasuloMemory memory = TomasuloMemory.getInstance();
    TomasuloFunctionalUnitManager fuManager;
    TomasuloReservationStationManager rsManager;
    TomasuloCommonDataBus cdb;
    TomasuloProgramCounter pc;
    TomasuloRegisterFileManager regManager;
    TomasuloClock clock;
    TomasuloInstruction instruction;
    PrintWriter writer;
    boolean branch;
    boolean halt;
    boolean stall;

    public TomasuloCentralProcessingUnit(){
        instructionBuilder = new TomasuloInstructionFactory();
        decoder = new TomasuloDecoder();
        fuManager = new TomasuloFunctionalUnitManager();
        rsManager = new TomasuloReservationStationManager();
        regManager = new TomasuloRegisterFileManager();
        pc = TomasuloProgramCounter.getInstance();
        cdb = TomasuloCommonDataBus.getInstance();
        clock = TomasuloClock.getInstance();
    }

    public void run(Boolean isVerbose){
        int[] fetched;
        int word;
        int counter = 10;

        while(!halt || !pipelineCompleted()){
            //System.out.println("**************************Clock cycle: " + clock.cycle);
            //System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            cdb = writeResult(rsManager,cdb);
            execute(rsManager,regManager,fuManager,writer,memory);
            //System.out.println("STALL"+stall+"\nHALT"+halt+"\nBRANCH"+branch);
            if(!halt && !rsManager.hasBranch()){
                //System.out.println("HALT: "+halt);
                word = memory.fetchWord(pc.address);
                instruction = decodeInstruction(word, decoder, instructionBuilder);
                stall = issue(instruction, rsManager, fuManager, regManager);
                if(!halt && !stall) pc.increment();
            }
            //if(isVerbose) printDumps(memory, rsManager, fuManager, regManager);
            updateReservationStations(regManager,rsManager,cdb);
            clearReservationStations(rsManager);
            clock.increment();
            /*if(counter == 0) break;
            counter--;*/
        }
        //writer.close();
    }

    // Singleton design pattern without double checked locking
    static TomasuloCentralProcessingUnit createInstance(){
        if (cpuInstance == null)
            cpuInstance = new TomasuloCentralProcessingUnit();
        return cpuInstance;
    }

    static TomasuloInstruction decodeInstruction(int word, TomasuloDecoder decoder, TomasuloInstructionFactory instructionBuilder){
        String operation = getInstructionOpcodeOperationFromWord(word, decoder);
        String operationType = getInstructionUnitType(operation,decoder);
        TomasuloInstruction instruction = createInstruction(operationType,instructionBuilder);
        String operationRegisterDesign = getInstructionRegisterDesign(operation,decoder);
        instruction.setOpcodeName(operation);
        instruction.setRegisterDesign(operationRegisterDesign);
        String[] operands = decodeOperands(operationRegisterDesign,word,decoder);
        instruction.setFirstSourceRegister(operands[0]);
        instruction.setSecondSourceRegister(operands[1]);
        instruction.setDestinationRegister(operands[2]);
        instruction.instructionType = operationType;
        if(!operands[3].equals("none")) instruction.setImmediateValue(Integer.parseInt(operands[3]));
        else instruction.setImmediateValue(0);
        return instruction;
    }

    static TomasuloInstruction createInstruction(String name, TomasuloInstructionFactory instructionBuilder){
        TomasuloInstruction instruction = instructionBuilder.makeInstruction(name);
        return  instruction;
    }

    static String getInstructionOpcodeOperation(int[] opcodeEncoding,TomasuloDecoder decoder){
        return decoder.getInstructionOpcodeOperation(opcodeEncoding);
    }

    static String getInstructionOpcodeOperationFromWord(int word, TomasuloDecoder decoder){
        return decoder.getInstructionOpcodeOperationFromWord(word);
    }

    static String getInstructionUnitType(String operationName,TomasuloDecoder decoder) {
        return decoder.getInstructionUnitType(operationName);
    }

    static String getInstructionRegisterDesign(String operation, TomasuloDecoder decoder){
        return decoder.getInstructionRegisterDesign(operation);
    }

    static String getInstructionType(String operationName,TomasuloDecoder decoder){
        return decoder.getInstructionType(operationName);
    }

    static int getFunctionCode(String operationName,TomasuloDecoder decoder){
        return decoder.getFunctionCode(operationName);
    }

    public String getTrapFunction(int immediateValue){
        return getTrapFunction(immediateValue);
    }

    public boolean pipelineCompleted(){
       return rsManager.allUnitsCompleted();
    }

    static String[] decodeOperands(String registerDesign, int encoding, TomasuloDecoder decoder){
        return decoder.decodeOperands(registerDesign,encoding);
    }

    static void execute(TomasuloReservationStationManager rsManager, TomasuloRegisterFileManager rfManager,TomasuloFunctionalUnitManager fuManager, PrintWriter writer, TomasuloMemory memory){
        fuManager.execute(rsManager,rfManager,writer,memory);
    }

    static TomasuloCommonDataBus writeResult(TomasuloReservationStationManager rsManager,TomasuloCommonDataBus cdb){
        TomasuloReservationStation reservationStation = null;
        for(TomasuloReservationStation rs : rsManager.reservationStations){
            if(rs.resultReady == true && !rs.resultWritten){
                reservationStation = rs;
                break;
            }
        }
        if(reservationStation != null) {
            TomasuloProgramCounter pc = TomasuloProgramCounter.getInstance();
            if(reservationStation.currentInstruction.instructionType.equals("branch")){
                pc.address = reservationStation.branchValue;
            }
            cdb.setResult(reservationStation.result);
            cdb.setReservationStation(reservationStation.name);
            cdb.setNameOfSourceFunctionalUnit(reservationStation.executingUnit.name);
            reservationStation.resultWritten = true;
        }
        return cdb;
    }

    static PrintWriter setupWriter(String fileName,PrintWriter writer){
        String outputFile = fileName.substring(0,fileName.lastIndexOf('.'));
        try {
            writer = new PrintWriter("intUnit1.out", "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return writer;
    }

    boolean issue(TomasuloInstruction instruction,TomasuloReservationStationManager rsManager, TomasuloFunctionalUnitManager fuManager,
                         TomasuloRegisterFileManager rfManager){
        if(instruction.getOpcodeName().equals("trap") && instruction.getImmediateValue() == 0){
            halt = true;
            return true;
        }
        if(instruction.instructionType.equals("branch")){
            branch = true;
        }
        switch(instruction.getClass().getName()) {
            case "TomasuloFloatingPointInstruction":
                if(!rsManager.hasAvailableRStation("float")) return true;
                break;
            case "TomasuloIntegerInstruction":
                if(!rsManager.hasAvailableRStation("int")) return true;
                break;
            case "TomasuloMemoryInstruction":
                if(!rsManager.hasAvailableRStation("mem")) return true;
                break;
            case "TomasuloBranchInstruction":
                if(!rsManager.hasAvailableRStation("branch")) {
                    return true;
                }
                break;
            case "TomasuloTrapInstruction":
                if(!rsManager.hasAvailableRStation("trap")) return true;
                break;
        }
        return rsManager.issue(instruction,fuManager,rfManager);
    }

    static void updateReservationStations(TomasuloRegisterFileManager rfManager, TomasuloReservationStationManager rsManager,
                                          TomasuloCommonDataBus cdb){
        if(cdb.nameOfWritingReservationStation == null) return;
        for(TomasuloRegister rf : rfManager.registers){
            if(rf.Qi != null && rf.Qi.equals(cdb.nameOfWritingReservationStation)){
                //System.out.println("Test register to be writtenn: " + rf.registerName);
                rf.setValue(cdb.result);
                rf.Qi = null;
            }
        }
        for(TomasuloReservationStation rs : rsManager.reservationStations){
            if(rs.Qj != null && rs.Qj.equals(cdb.nameOfWritingReservationStation))
            {
                rs.Vj = cdb.result;
                rs.Qj = null;

            }
            if(rs.Qk != null && rs.Qk.equals(cdb.nameOfWritingReservationStation))
            {
                rs.Vk = cdb.result;
                rs.Qk = null;
            }
        }
        cdb.nameOfWritingReservationStation = null;
        /*for(TomasuloReservationStation rs : rsManager.reservationStations){
            if(rs.name.equals(cdb.nameOfWritingReservationStation))
                rs.setBusy(false);
        }*/
    }

    static void clearReservationStations(TomasuloReservationStationManager rsManager){
        for(TomasuloReservationStation rs : rsManager.reservationStations){
            if(rs.resultWritten == true) rs.resetContents();
        }
    }

    static void printDumps(TomasuloMemory memory, TomasuloReservationStationManager rsManager,
                           TomasuloFunctionalUnitManager fuManager, TomasuloRegisterFileManager regManager){
        TomasuloClock clock = TomasuloClock.getInstance();
        System.out.println("==========================================================================================");
        System.out.println("Clock cycle:" + clock.getCurrentCycle());
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        memory.printDataContents();
        rsManager.printIssuedInstruction();
        rsManager.printStationContents();
        fuManager.printFunctionalUnitContents();
        regManager.printRegisterContents();
        printCDB();
    }

    static void printCDB(){
        System.out.println("...........................................");
        TomasuloCommonDataBus cdb = TomasuloCommonDataBus.getInstance();
        System.out.println("cdb:" + cdb.result);
        System.out.println("writtenby:" + cdb.nameOfWritingReservationStation);
        System.out.println("...........................................");
    }
}
