import java.util.HashMap;

/**
 * Created by rt on 12/2/14.
 */
public class TomasuloDecoder {

    HashMap<Integer,String> instructionOpcodeMap;
    HashMap<Integer, String> instructionFunctionCodeMap;
    HashMap<String,String> instructionRegisterMap;

    /*
        TomasuloDecoder constructor method
    */
    public TomasuloDecoder(){

        instructionOpcodeMap= new HashMap(); // opcodenumber -> instruction operation
        instructionFunctionCodeMap = new HashMap(); // functioncode -> instruction operation

        // Integer Unit Instructions
        instructionOpcodeMap.put(8,"addi");
        instructionOpcodeMap.put(0,"rtypei");
        instructionOpcodeMap.put(1,"rtypef");

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

        // Floating Point Rtype Instructions
        instructionFunctionCodeMap.put(0,"addf");
        instructionFunctionCodeMap.put(1,"subf");
        instructionFunctionCodeMap.put(2,"multf");
        instructionFunctionCodeMap.put(3,"divf");
        instructionFunctionCodeMap.put(9,"ctf2i");
        instructionFunctionCodeMap.put(12,"cvti2f");
        instructionFunctionCodeMap.put(14,"mult");
        instructionFunctionCodeMap.put(15,"div");

        //Integer Unit Rtype Instructions
        instructionFunctionCodeMap.put(32,"add");
        instructionFunctionCodeMap.put(34,"sub");
        instructionFunctionCodeMap.put(36,"and");
        instructionFunctionCodeMap.put(37,"or");
        instructionFunctionCodeMap.put(38,"xor");
        instructionFunctionCodeMap.put(50,"movf");
        instructionFunctionCodeMap.put(52,"movfp2i");
        instructionFunctionCodeMap.put(53,"movi2fp");

        // Map that stores the register structure of each instruction operation
        instructionRegisterMap = new HashMap();
        instructionRegisterMap.put("nop","none");
        instructionRegisterMap.put("trap","number");
        instructionRegisterMap.put("j","name");
        instructionRegisterMap.put("jal","name");
        instructionRegisterMap.put("jr","gpr");
        instructionRegisterMap.put("jalr","gpr");
        instructionRegisterMap.put("beqz","gprname");
        instructionRegisterMap.put("benz","gprname");
        instructionRegisterMap.put("movfp2i","gprfpr");
        instructionRegisterMap.put("movd","dprdpr");
        instructionRegisterMap.put("cvtf2i","fprfpr");
        instructionRegisterMap.put("cvti2f","fprfpr");
        instructionRegisterMap.put("movf","fprfpr");
        instructionRegisterMap.put("lhi","gprnum");
        instructionRegisterMap.put("movi2fp","fprgpr");
        instructionRegisterMap.put("cvtd2f","fprdpr");
        instructionRegisterMap.put("cvtd2i","fprdpr");
        instructionRegisterMap.put("cvtf2d","dprfpr");
        instructionRegisterMap.put("cvti2d","dprfpr");
        instructionRegisterMap.put("addi","gprgprint");
        instructionRegisterMap.put("seqi","gprgprint");
        instructionRegisterMap.put("sgei","gprgprint");
        instructionRegisterMap.put("sgti","gprgprint");
        instructionRegisterMap.put("slei","gprgprint");
        instructionRegisterMap.put("slti","gprgprint");
        instructionRegisterMap.put("snei","gprgprint");
        instructionRegisterMap.put("subi","gprgprint");
        instructionRegisterMap.put("addui","gprgpruint");
        instructionRegisterMap.put("andi","gprgpruint");
        instructionRegisterMap.put("ori","gprgpruint");
        instructionRegisterMap.put("slli","gprgpruint");
        instructionRegisterMap.put("srai","gprgpruint");
        instructionRegisterMap.put("srli","gprgpruint");
        instructionRegisterMap.put("subui","gprgpruint");
        instructionRegisterMap.put("xori","gprgpruint");
        instructionRegisterMap.put("add","gprgprgpr");
        instructionRegisterMap.put("addu","gprgprgpr");
        instructionRegisterMap.put("and","gprgprgpr");
        instructionRegisterMap.put("or","gprgprgpr");
        instructionRegisterMap.put("seq","gprgprgpr");
        instructionRegisterMap.put("sge","gprgprgpr");
        instructionRegisterMap.put("sgt","gprgprgpr");
        instructionRegisterMap.put("sle","gprgprgpr");
        instructionRegisterMap.put("sll","gprgprgpr");
        instructionRegisterMap.put("slt","gprgprgpr");
        instructionRegisterMap.put("sne","gprgprgpr");
        instructionRegisterMap.put("sra","gprgprgpr");
        instructionRegisterMap.put("srl","gprgprgpr");
        instructionRegisterMap.put("sub","gprgprgpr");
        instructionRegisterMap.put("subu","gprgprgpr");
        instructionRegisterMap.put("xor","gprgprgpr");
        instructionRegisterMap.put("addd","dprdprdpr");
        instructionRegisterMap.put("divd","dprdprdpr");
        instructionRegisterMap.put("multd","dprdprdpr");
        instructionRegisterMap.put("subd","dprdprdpr");
        instructionRegisterMap.put("addf","fprfprfpr");
        instructionRegisterMap.put("div","fprfprfpr");
        instructionRegisterMap.put("divf","fprfprfpr");
        instructionRegisterMap.put("divu","fprfprfpr");
        instructionRegisterMap.put("mult","fprfprfpr");
        instructionRegisterMap.put("multf","fprfprfpr");
        instructionRegisterMap.put("multu","fprfprfpr");
        instructionRegisterMap.put("subf","fprfprfpr");
        instructionRegisterMap.put("lb","gproff");
        instructionRegisterMap.put("lbu","gproff");
        instructionRegisterMap.put("lh","gproff");
        instructionRegisterMap.put("lhu","gproff");
        instructionRegisterMap.put("lw","gproff");
        instructionRegisterMap.put("ld","dproff");
        instructionRegisterMap.put("lf","fproff");
        instructionRegisterMap.put("sb","offgpr");
        instructionRegisterMap.put("sh","offgpr");
        instructionRegisterMap.put("sw","offgpr");
        instructionRegisterMap.put("sd","offdpr");
        instructionRegisterMap.put("sf","offfpr");
    }

    /*
        getInstructionOpcodeOperation :: Method that checks an opcodeName number
            and returns a string for the operation name.
     */
    public String getInstructionOpcodeOperation(int[] encoding){
        int opcodeNumber = extractSixBitOpcode(encoding);
        System.out.println("decoder test: " + opcodeNumber);
        if (instructionOpcodeMap.get(opcodeNumber) == "rtypei"){
            int functionCode = extractSixBitFunctionCode(encoding);
            System.out.println("functionCode:  " + functionCode);
            return instructionFunctionCodeMap.get(functionCode);
        }
        if (instructionOpcodeMap.get(opcodeNumber) == "rtypef"){
            int functionCode = extractFiveBitFunctionCode(encoding);
            System.out.println("functionCode:  " + functionCode);
            return instructionFunctionCodeMap.get(functionCode);
        }
        return instructionOpcodeMap.get(opcodeNumber);
    }

    public String getInstructionOpcodeOperationFromWord(int encoding){
        int opcodeNumber = extractSixBitOpcodeFromWord(encoding);
        System.out.println("decoder test: " + opcodeNumber);
        if (instructionOpcodeMap.get(opcodeNumber) == "rtypei"){
            int functionCode = extractSixBitFunctionCodeFromWord(encoding);
            System.out.println("functionCode:  " + functionCode);
            return instructionFunctionCodeMap.get(functionCode);
        }
        if (instructionOpcodeMap.get(opcodeNumber) == "rtypef"){
            int functionCode = extractFiveBitFunctionCodeFromWord(encoding);
            System.out.println("functionCode:  " + functionCode);
            return instructionFunctionCodeMap.get(functionCode);
        }
        return instructionOpcodeMap.get(opcodeNumber);
    }

    public String[] decodeOperands(String registerDesign, int encoding){
    /*     registers[0] = first source register
           registers[1] = second source register
           registers[2] = destination register
           registers[3] = immediate value
    */
        String[] registers = new String[]{"none","none","none","none"};
        int fiveBitMask = 0x0000001F;
        int sixBitMask = 0x0000003F;
        int sixteenBitMask = 0x0000FFFF;
        int twentySixBitMask = 0x03FFFFFF;
        int register = 0;
        switch(registerDesign){
            case "none":
                return registers;

            case "number":
                register |= encoding;
                register &= fiveBitMask;
                registers[3] = "" + register;
                register |= encoding;
                register >>= (32 - (6 + 5));
                register &= fiveBitMask;
                registers[0] = "r" + register;
                return registers;

            case "name":
                register |= encoding;
                register &= twentySixBitMask;
                registers[2] = "" + register;
                return registers;

            case "gpr":
                register |= encoding;
                register >>= (5 + 16);
                register &= fiveBitMask;
                registers[0] = "r" + register;
                return registers;

            case "gprname":
                register |= encoding;
                register >>= (5 + 16);
                register &= fiveBitMask;
                registers[0] = "r" + register;
                register |= encoding;
                register &= sixteenBitMask;
                registers[3] = "" + register;
                return registers;

            case "gprfpr":
                register |= encoding;
                register >>= (5 + 6);
                register &= fiveBitMask;
                registers[2] = "r" + register;
                register |= encoding;
                register >>= (5 + 5 + 5 + 6);
                register &= fiveBitMask;
                registers[0] = "f" + register;
                return registers;

            case "dprdpr":
                register |= encoding;
                register >>= (5 + 6);
                register &= fiveBitMask;
                registers[2] = "f" + register;
                register |= encoding;
                register >>= (5 + 5 + 5 + 6);
                register &= fiveBitMask;
                registers[0] = "f" + register;
                return registers;

            case "fprfpr":
                register |= encoding;
                register >>= (5 + 6);
                register &= fiveBitMask;
                registers[2] = "f" + register;
                register |= encoding;
                register >>= (5 + 5 + 5 + 6);
                register &= fiveBitMask;
                registers[0] = "f" + register;
                return registers;

            case "gprnum":
                register |= encoding;
                register &= sixteenBitMask;
                registers[3] = "" + register;
                register |= encoding;
                register >>= 16;
                register &= fiveBitMask;
                registers[2] = "r" + register;
                return registers;

            case "fprgpr":
                register |= encoding;
                register >>= 5 + 6;
                register >>= fiveBitMask;
                registers[2] = "f" + register;
                register |= encoding;
                register >>= (6 + 5 + 5 + 5);
                register &= fiveBitMask;
                registers[0] = "r" + register;
                return registers;

            case "fprdpr":
                register |= encoding;
                register >>= 5 + 6;
                register >>= fiveBitMask;
                registers[2] = "f" + register;
                register |= encoding;
                register >>= (6 + 5 + 5 + 5);
                register &= fiveBitMask;
                registers[0] = "r" + register;
                return registers;

            case "dprfpr":
                register |= encoding;
                register >>= 5 + 6;
                register >>= fiveBitMask;
                registers[2] = "f" + register;
                register |= encoding;
                register >>= (6 + 5 + 5 + 5);
                register &= fiveBitMask;
                registers[0] = "f" + register;
                return registers;

            case "gprgprint":
                register |= encoding;
                register &= sixBitMask;
                registers[3] = "" + register;
                register |= encoding;
                register >>= 16;
                register &= fiveBitMask;
                registers[2] = "r" + register;
                register |= encoding;
                register >>= 16 + 5;
                register &= fiveBitMask;
                registers[0] = "r" + register;
                return registers;

            case "gprgpruint":
                register |= encoding;
                register &= sixBitMask;
                registers[3] = "" + register;
                register |= encoding;
                register >>= 16;
                register &= fiveBitMask;
                registers[2] = "r" + register;
                register |= encoding;
                register >>= 16 + 5;
                register &= fiveBitMask;
                registers[1] = "r" + register;
                return registers;

            case "gprgprgpr":
                register |= encoding;
                register >>= (5 + 6);
                register &= fiveBitMask;
                registers[2] = "r" + register;
                register |= encoding;
                register >>= (5 + 5 + 6);
                register &= fiveBitMask;
                registers[1] = "r" + register;
                register |= encoding;
                register >>= (5 + 5 + 5 + 6);
                register &= fiveBitMask;
                registers[0] = "r" + register;
                return registers;

            case "dprdprdpr":
                register |= encoding;
                register >>= (5 + 6);
                register &= fiveBitMask;
                registers[2] = "f" + register;
                register |= encoding;
                register >>= (5 + 5 + 6);
                register &= fiveBitMask;
                registers[1] = "f" + register;
                register |= encoding;
                register >>= (5 + 5 + 5 + 6);
                register &= fiveBitMask;
                registers[0] = "f" + register;
                return registers;

            case "fprfprfpr":
                register |= encoding;
                register >>= (5 + 6);
                register &= fiveBitMask;
                registers[2] = "f" + register;
                register |= encoding;
                register >>= (5 + 5 + 6);
                register &= fiveBitMask;
                registers[1] = "f" + register;
                register |= encoding;
                register >>= (5 + 5 + 5 + 6);
                register &= fiveBitMask;
                registers[0] = "f" + register;
                return registers;

            case "gproff":
                register |= encoding;
                register &= sixteenBitMask;
                registers[3] = "" + register;
                register |= encoding;
                register >>= (16 + 5);
                register &= fiveBitMask;
                registers[2] = "r" + register;
                register |= encoding;
                register |= fiveBitMask;
                registers[0] = "r" + register;
                return registers;

            case "dproff":
                register |= encoding;
                register &= sixteenBitMask;
                registers[3] = "" + register;
                register |= encoding;
                register >>= (16 + 5);
                register &= fiveBitMask;
                registers[2] = "f" + register;
                register |= encoding;
                register |= fiveBitMask;
                registers[0] = "r" + register;
                return registers;

            case "fproff":
                register |= encoding;
                register &= sixteenBitMask;
                registers[3] = "" + register;
                register |= encoding;
                register >>= (16 + 5);
                register &= fiveBitMask;
                registers[2] = "f" + register;
                register |= encoding;
                register |= fiveBitMask;
                registers[0] = "r" + register;
                return registers;

            case "offgpr":
                register |= encoding;
                register &= sixteenBitMask;
                registers[3] = "" + register;
                register |= encoding;
                register >>= (16 + 5);
                register &= fiveBitMask;
                registers[2] = "r" + register;
                register |= encoding;
                register |= fiveBitMask;
                registers[0] = "r" + register;
                return registers;

            case "offdpr":
                register |= encoding;
                register &= sixteenBitMask;
                registers[3] = "" + register;
                register |= encoding;
                register >>= (16 + 5);
                register &= fiveBitMask;
                registers[2] = "f" + register;
                register |= encoding;
                register |= fiveBitMask;
                registers[0] = "r" + register;
                return registers;

            case "offfpr":
                register |= encoding;
                register &= sixteenBitMask;
                registers[3] = "" + register;
                register |= encoding;
                register >>= (16 + 5);
                register &= fiveBitMask;
                registers[2] = "f" + register;
                register |= encoding;
                register |= fiveBitMask;
                registers[0] = "r" + register;
                return registers;
        }
        return registers;
    }

    public String getInstructionRegisterDesign(String operation){
        return instructionRegisterMap.get(operation);
    }

    static int extractSixBitOpcode(int[] encoding){
        int extraction = encoding[0];
        extraction >>= 2;
        return extraction;
    }

    static int extractSixBitOpcodeFromWord(int encoding){
        encoding >>>= (32 - 6);
        encoding &= 0x0000003F;
        return encoding;
    }

    static int extractSixBitFunctionCodeFromWord(int encoding){
        encoding &= 0x0000003F;
        return encoding;
    }

    static int extractFiveBitFunctionCodeFromWord(int encoding){
        encoding &= 0x0000001F;
        return encoding;
    }

    static int extractSixBitFunctionCode(int[] encoding){
        int extraction = encoding[3];
        extraction >>= 2;
        return extraction;
    }

    static int extractFiveBitFunctionCode(int[] encoding){
        int extraction = encoding[3];
        extraction >>= 3;
        return extraction;
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
