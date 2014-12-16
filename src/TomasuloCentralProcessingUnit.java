import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

/**
 * TomasuloCentralProcessingUnit :: Class that orchestratics the stages and cycles of the processor.
 *
 * Created by sina on 11/30/14.
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

    /**
     *  Constructor that instantiates all of the main pieces of the processor.
     */
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

    /**
     * run :: Method that contains the loop that interates through all the stages of the pipeline
     * over every instruction until all instructions have completed writing their result.
     * @param isVerbose
     */
    public void run(Boolean isVerbose){
        int[] fetched;
        int word;

        while(!halt || !pipelineCompleted()){
            cdb = writeResult(rsManager,cdb);
            execute(rsManager,regManager,fuManager,writer,memory);
            if(!halt && !rsManager.hasBranch()){
                word = memory.fetchWord(pc.address);
                instruction = decodeInstruction(word, decoder, instructionBuilder);
                stall = issue(instruction, rsManager, fuManager, regManager);
                if(!halt && !stall) pc.increment();
            }
            if(isVerbose) printDumps(memory, rsManager, fuManager, regManager);
            updateReservationStations(regManager,rsManager,cdb);
            clearReservationStations(rsManager);
            clock.increment();
        }
    }

    // Singleton design pattern without double checked locking
    static TomasuloCentralProcessingUnit createInstance(){
        if (cpuInstance == null)
            cpuInstance = new TomasuloCentralProcessingUnit();
        return cpuInstance;
    }

    /**
     *  decodeInstruction :: Method that calls several methods of the TomasuloDecoder call to take a 32 bit integer
     *  representing an instruction encoding and extract all the information from that encoding needed for getting
     *  through the pipeline.
     * @param word
     * @param decoder
     * @param instructionBuilder
     * @return
     */
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

    /**
     * printDumps :: Method that calls printing methods on all of the major components of the pipeline.
     * this method is called when the isVerboseMode flag is set.
     *
     * @param memory
     * @param rsManager
     * @param fuManager
     * @param regManager
     */
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

    /**
     *
     * @param rsManager
     * @param cdb
     * @return
     */
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

    /**
     *
     * @param instruction
     * @param rsManager
     * @param fuManager
     * @param rfManager
     * @return
     */
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

    /**
     *
     * @param rfManager
     * @param rsManager
     * @param cdb
     */
    static void updateReservationStations(TomasuloRegisterFileManager rfManager, TomasuloReservationStationManager rsManager,
                                          TomasuloCommonDataBus cdb){
        if(cdb.nameOfWritingReservationStation == null) return;
        for(TomasuloRegister rf : rfManager.registers){
            if(rf.Qi != null && rf.Qi.equals(cdb.nameOfWritingReservationStation)){
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
    }

    /**
     *
     * @param rsManager
     */
    static void clearReservationStations(TomasuloReservationStationManager rsManager){
        for(TomasuloReservationStation rs : rsManager.reservationStations){
            if(rs.resultWritten == true) rs.resetContents();
        }
    }

    /**
     *
     */
    static void printCDB(){
        System.out.println("...........................................");
        TomasuloCommonDataBus cdb = TomasuloCommonDataBus.getInstance();
        System.out.println("cdb:" + cdb.result);
        System.out.println("writtenby:" + cdb.nameOfWritingReservationStation);
        System.out.println("...........................................");
    }

    /**
     *
     * @param name
     * @param instructionBuilder
     * @return
     */
    static TomasuloInstruction createInstruction(String name, TomasuloInstructionFactory instructionBuilder){
        TomasuloInstruction instruction = instructionBuilder.makeInstruction(name);
        return  instruction;
    }

    /**
     *
     * @param opcodeEncoding
     * @param decoder
     * @return
     */
    static String getInstructionOpcodeOperation(int[] opcodeEncoding,TomasuloDecoder decoder){
        return decoder.getInstructionOpcodeOperation(opcodeEncoding);
    }

    /**
     *
     * @param word
     * @param decoder
     * @return
     */
    static String getInstructionOpcodeOperationFromWord(int word, TomasuloDecoder decoder){
        return decoder.getInstructionOpcodeOperationFromWord(word);
    }

    /**
     *
     * @param operationName
     * @param decoder
     * @return
     */
    static String getInstructionUnitType(String operationName,TomasuloDecoder decoder) {
        return decoder.getInstructionUnitType(operationName);
    }

    /**
     *
     * @param operation
     * @param decoder
     * @return
     */
    static String getInstructionRegisterDesign(String operation, TomasuloDecoder decoder){
        return decoder.getInstructionRegisterDesign(operation);
    }

    /**
     *
     * @param operationName
     * @param decoder
     * @return
     */
    static String getInstructionType(String operationName,TomasuloDecoder decoder){
        return decoder.getInstructionType(operationName);
    }

    /**
     *
     * @param operationName
     * @param decoder
     * @return
     */
    static int getFunctionCode(String operationName,TomasuloDecoder decoder){
        return decoder.getFunctionCode(operationName);
    }

    /**
     *
     * @param immediateValue
     * @return
     */
    public String getTrapFunction(int immediateValue){
        return getTrapFunction(immediateValue);
    }

    /**
     *
     * @return
     */
    public boolean pipelineCompleted(){
       return rsManager.allUnitsCompleted();
    }

    /**
     *
     * @param registerDesign
     * @param encoding
     * @param decoder
     * @return
     */
    static String[] decodeOperands(String registerDesign, int encoding, TomasuloDecoder decoder){
        return decoder.decodeOperands(registerDesign,encoding);
    }

    /**
     *
     * @param rsManager
     * @param rfManager
     * @param fuManager
     * @param writer
     * @param memory
     */
    static void execute(TomasuloReservationStationManager rsManager, TomasuloRegisterFileManager rfManager,TomasuloFunctionalUnitManager fuManager, PrintWriter writer, TomasuloMemory memory){
        fuManager.execute(rsManager,rfManager,writer,memory);
    }

    /**
     *
     * @param fileName
     * @param writer
     * @return
     */
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
}
