import java.util.HashMap;

/**
 * Created by rt on 11/30/14.
 */
public class TomasuloFunctionalUnitManager {

    HashMap<String,String> instructionRegisterMap;

    public TomasuloFunctionalUnitManager(){

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
}
