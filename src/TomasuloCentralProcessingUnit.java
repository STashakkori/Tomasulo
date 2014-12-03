/**
 * Created by rt on 11/30/14.
 */
public class TomasuloCentralProcessingUnit {

    static TomasuloCentralProcessingUnit cpuInstance = null;
    TomasuloInstructionFactory instructionBuilder;
    TomasuloDecoder decoder;
    TomasuloMemory memory = TomasuloMemory.getInstance();

    public TomasuloCentralProcessingUnit(){
        instructionBuilder = new TomasuloInstructionFactory();
        decoder = new TomasuloDecoder();
    }

    public void run(){
        int[] fetched;
        TomasuloProgramCounter pc = TomasuloProgramCounter.getInstance();
        fetched = memory.fetchFourBytes(pc.address);
        for(int i:fetched) System.out.println("\nfetched: " + i);
        //cpu.instructionBuilder.makeInstruction();
    }

    // Singleton design pattern without double checked locking
    static TomasuloCentralProcessingUnit createInstance(){
        if (cpuInstance == null)
            cpuInstance = new TomasuloCentralProcessingUnit();
        return cpuInstance;
    }

    public TomasuloInstruction createInstruction(String name){
        TomasuloInstruction instruction = instructionBuilder.makeInstruction(name);
        return  instruction;
    }

    public String getInstructionOpcodeOperation(int opcodeNumber){
        return decoder.getInstructionOpcodeOperation(opcodeNumber);
    }

    public String getInstructionUnitType(String operationName) {
        return decoder.getInstructionUnitType(operationName);
    }

    public String getInstructionType(String operationName){
        return decoder.getInstructionType(operationName);
    }

    public int getFunctionCode(String operationName){
        return decoder.getFunctionCode(operationName);
    }

    public String getTrapFunction(int immediateValue){
        return getTrapFunction(immediateValue);
    }
}
