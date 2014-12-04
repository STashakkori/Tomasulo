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
        //for(int i:fetched) System.out.println("\nfetched: " + i);
        //cpu.instructionBuilder.makeInstruction();

        while(!halt || !functionalUnitsCompleted()){
            cdb = writeResult(fuManager);
            //execute(fuManager);
            if(!halt && !branch){
                fetched = memory.fetchFourBytes(pc.address);
                String operation = getInstructionOpcodeOperation(fetched[0],decoder);
                TomasuloInstruction instruction = createInstruction(operation,instructionBuilder);
                stall = issue(instruction);
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

    static TomasuloInstruction createInstruction(String name, TomasuloInstructionFactory instructionBuilder){
        TomasuloInstruction instruction = instructionBuilder.makeInstruction(name);
        return  instruction;
    }

    static String getInstructionOpcodeOperation(int opcodeNumber,TomasuloDecoder decoder){
        return decoder.getInstructionOpcodeOperation(opcodeNumber);
    }

    static String getInstructionUnitType(String operationName,TomasuloDecoder decoder) {
        return decoder.getInstructionUnitType(operationName);
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

    static boolean issue(TomasuloInstruction instruction){
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
