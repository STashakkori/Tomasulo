/**
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
    boolean branch;
    boolean halt;
    boolean stall;

    int testcounter = 0;

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

    public void run(){
        int[] fetched;
        int word;

        while(!halt || !functionalUnitsCompleted()){
            System.out.println("Clock cycle: " + clock.cycle);
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            //cdb = writeResult(fuManager);
            execute(rsManager,fuManager);
            if(!halt && !branch){
                word = memory.fetchWord(pc.address);
                instruction = decodeInstruction(word,decoder,instructionBuilder);
                stall = issue(instruction,rsManager,fuManager,regManager);
                printDumps(memory,rsManager,fuManager,regManager);
                if(!halt && !stall) pc.increment();
                clock.increment();
                updateReservationStations(cdb);
                clearReservationStations();
                if(testcounter== 5) break;
                testcounter++;
            }
        }
    }

    // Singleton design pattern without double checked locking
    static TomasuloCentralProcessingUnit createInstance(){
        if (cpuInstance == null)
            cpuInstance = new TomasuloCentralProcessingUnit();
        return cpuInstance;
    }

    static TomasuloInstruction decodeInstruction(int word, TomasuloDecoder decoder, TomasuloInstructionFactory instructionBuilder){
        String operation = getInstructionOpcodeOperationFromWord(word, decoder);
        System.out.println("operation: " +  operation);
        String operationType = getInstructionUnitType(operation,decoder);
        System.out.println("operationType: " + operationType);
        TomasuloInstruction instruction = createInstruction(operationType,instructionBuilder);
        String operationRegisterDesign = getInstructionRegisterDesign(operation,decoder);
        System.out.println("register design: " + operationRegisterDesign);
        instruction.setOpcodeName(operation);
        instruction.setRegisterDesign(operationRegisterDesign);
        System.out.println("Instruction type: " + instruction.getClass().getName());
        String[] operands = decodeOperands(operationRegisterDesign,word,decoder);
        for(String s : operands){
            System.out.println("operand: " + s);
        }
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

    public boolean functionalUnitsCompleted(){
       return fuManager.allUnitsCompleted();
    }

    static String[] decodeOperands(String registerDesign, int encoding, TomasuloDecoder decoder){
        return decoder.decodeOperands(registerDesign,encoding);
    }

    static void execute(TomasuloReservationStationManager rsManager, TomasuloFunctionalUnitManager fuManager){
        fuManager.execute(rsManager);
    }

    static TomasuloCommonDataBus writeResult(TomasuloFunctionalUnitManager fuManager){
        for(TomasuloFunctionalUnit f : fuManager.functionalUnits){
            TomasuloCommonDataBus result = f.write();
            if(result != null){
                return result;
            }
        }
        return null;
    }

    static boolean issue(TomasuloInstruction instruction,TomasuloReservationStationManager rsManager, TomasuloFunctionalUnitManager fuManager,
                         TomasuloRegisterFileManager rfManager){
        if(instruction.getOpcodeName().equals("trap") && instruction.getImmediateValue() == 0){
            return true;
        }
        switch(instruction.getClass().getName()) {
            case "TomasuloFloatingPointInstruction":
                if(!rsManager.hasAvailableRStation("float")) return true;
            case "TomasuloIntegerInstruction":
                if(!rsManager.hasAvailableRStation("int")) return true;
            case "TomasuloMemoryInstruction":
                if(!rsManager.hasAvailableRStation("mem")) return true;
            case "TomasuloBranchInstruction":
                if(!rsManager.hasAvailableRStation("branch")) return true;
            case "TomasuloTrapInstruction":
                if(!rsManager.hasAvailableRStation("trap")) return true;

        }
        return rsManager.issue(instruction,fuManager,rfManager);
    }

    static void updateReservationStations(TomasuloCommonDataBus cdb){

    }

    static void clearReservationStations(){

    }

    static void printDumps(TomasuloMemory memory, TomasuloReservationStationManager rsManager,
                           TomasuloFunctionalUnitManager fuManager, TomasuloRegisterFileManager regManager){
        memory.printDataContents();
        rsManager.printStationContents();
        fuManager.printFunctionalUnitContents();
        regManager.printRegisterContents();
        System.out.println("\n==========================================================================================");
    }
}
