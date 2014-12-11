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
        //cpu.instructionBuilder.makeInstruction();

        while(!halt || !functionalUnitsCompleted()){
            cdb = writeResult(fuManager);
            //execute(fuManager);
            if(!halt && !branch){
                word = memory.fetchWord(pc.address);
                instruction = decodeInstruction(word,decoder,instructionBuilder);
                stall = issue(instruction,rsManager,fuManager);
                if(!halt && !stall) pc.increment();
                clock.increment();
                updateReservationStations(cdb);
                clearReservationStations();
                //printDumps(memory,rsManager,fuManager,regManager);
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

    static boolean execute(TomasuloFunctionalUnitManager fuManager){
        for(TomasuloFunctionalUnit f : fuManager.functionalUnits){
            f.execute();
            if(f.getClass().getName().equals("TomasuloBranchFunctionalUnit")) return f.execute();
        }
        return false;
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

    static boolean issue(TomasuloInstruction instruction,TomasuloReservationStationManager rsManager, TomasuloFunctionalUnitManager fuManager){
        if(instruction.getOpcodeName().equals("trap") && instruction.getImmediateValue() == 0){
            return true;
        }
        if(!rsManager.hasAvailableRStation()) return true;
        switch(instruction.getClass().getName()) {
            case "TomasuloFloatingPointInstruction":
                return rsManager.issue("fp",fuManager);
            case "TomasuloIntegerInstruction":
                return rsManager.issue("int",fuManager);
            case "TomasuloMemoryInstruction":
                return rsManager.issue("mem",fuManager);
            case "TomasuloBranchInstruction":
                return rsManager.issue("branch",fuManager);
            case "TomasuloTrapInstruction":
                return rsManager.issue("trap",fuManager);
        }
        return false;
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
    }
}
