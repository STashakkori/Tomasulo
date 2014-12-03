import java.util.HashMap;

/**
 * Created by rt on 12/2/14.
 */
public class TomasuloDecoder {

    HashMap<Integer,String> instructionOpcodeMap;

    /*
        TomasuloDecoder constructor method
    */
    public TomasuloDecoder(){

        instructionOpcodeMap= new HashMap(); // opcodenumber -> instruction operation

        // Integer Unit Instructions
        instructionOpcodeMap.put(8,"addi");
        instructionOpcodeMap.put(0,"nop");
        instructionOpcodeMap.put(32,"add");
        instructionOpcodeMap.put(34,"sub");
        instructionOpcodeMap.put(36,"and");
        instructionOpcodeMap.put(37,"or");
        instructionOpcodeMap.put(38,"zor");
        instructionOpcodeMap.put(50,"movf");
        instructionOpcodeMap.put(52,"movfp2i");
        instructionOpcodeMap.put(53,"movi2fp");

        // Trap Unit Instructions
        instructionOpcodeMap.put(17,"trap");

        // Branch Instructions
        instructionOpcodeMap.put(4,"beqz");
        instructionOpcodeMap.put(2,"j");
        instructionOpcodeMap.put(18,"jr");
        instructionOpcodeMap.put(3,"jal");
        instructionOpcodeMap.put(19,"jalr");

        // Memory Instructions
        instructionOpcodeMap.put(35,"lw");
        instructionOpcodeMap.put(38,"lf");
        instructionOpcodeMap.put(43,"sw");
        instructionOpcodeMap.put(46,"sf");

        // Floating Point Instructions
        instructionOpcodeMap.put(0,"addf");
        instructionOpcodeMap.put(1,"subf");
        instructionOpcodeMap.put(2,"multf");
        instructionOpcodeMap.put(3,"divf");
        instructionOpcodeMap.put(14,"mult");
        instructionOpcodeMap.put(15,"div");
        instructionOpcodeMap.put(9,"ctf2i");
        instructionOpcodeMap.put(12,"cvti2f");
    }

    /*
        getInstructionOpcodeOperation :: Method that checks an opcode number
            and returns a string for the operation name.
     */
    public String getInstructionOpcodeOperation(int opcodeNumber){
        return instructionOpcodeMap.get(opcodeNumber);
    }

    /*
        getInstructionUnitType :: Method that checks an operation name and returns
            the string name of the functional unit that should eventually execute the
            instruction. Returns error if the operation name is not valid.
     */
    public String getInstructionUnitType(String operationName){
        switch(operationName) {
            // Integer Unit Instructions
            case "addi":
            case "nop":
            case "add":
            case "sub":
            case "and":
            case "or":
            case "zor":
            case "movf":
            case "movfp2i":
            case "movi2fp":
                return "int";

            // Trap Unit Instructions
            case "trap":
                return "trap";

            // Branch Instructions
            case "beqz":
            case "j":
            case "jr":
            case "jal":
            case "jalr":
                return "branch";

            // Memory Instructions
            case "lw":
            case "lf":
            case "sw":
            case "sf":
                return "mem";

            // Floating Point Instructions
            case "addf":
            case "subf":
            case "multf":
            case "divf":
            case "mult":
            case "div":
            case "ctf2i":
            case "cvti2f":
                return "fp";
        }
        return "error";
    }

    /*
        getInstructionType :: Method that checks whether an instruction is an
            Itype, Jtype, or Rtype instruction. This is needed to know whether to
            then look for a function code or not in the instruction. Returns a string
            name unless the operationName is not valid, in which case it returns error.
     */
    public String getInstructionType(String operationName){
        switch(operationName) {
            // Rtype instructions
            case "nop":
            case "add":
            case "sub":
            case "and":
            case "or":
            case "xor":
            case "movf":
            case "movfp2i":
            case "movi2fp":
            case "addf":
            case "subf":
            case "multf":
            case "divf":
            case "mult":
            case "div":
            case "ctf2i":
            case "cvti2f":
                return "Rtype";

            // Jtype Instructions
            case "j":
            case "jal":
                return "Jtype";

            // Itype Instructions
            case "addi":
            case "trap":
            case "beqz":
            case "jr":
            case "jalr":
            case "lw":
            case "lf":
            case "sw":
            case "sf":
                return "Itype";
        }
        return "error";
    }

    /*
        getFunctionCode :: Method that returns a function code 0 or 1 for rtype instructions.
            In the case that an instruction is incorrectly input to the
            method, the method returns -1 for error handling.
    */
    public int getFunctionCode(String operationName){
        switch(operationName) {
            case "nop":
            case "add":
            case "sub":
            case "and":
            case "or":
            case "xor":
            case "movf":
            case "movfp2i":
            case "movi2fp":
                return 0;
            case "addf":
            case "subf":
            case "multf":
            case "divf":
            case "mult":
            case "div":
            case "ctf2i":
            case "cvti2f":
                return 1;
        }
        return -1;
    }

    /*
        getTrapFunction :: Method that returns a string trap function
            based on the immediate value of the trap instruction.
            The method returns error for handling in the case that an
            incorrect instruction type is passed.
     */
    public String getTrapFunction(int immediateValue){
        switch(immediateValue) {
            case 0:
                return "halt";
            case 1:
                return "outgpr";
            case 2:
                return "outfpr";
            case 3:
                return "outstr";
        }
        return "error";
    }
}
