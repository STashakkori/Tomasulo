/**
 * Created by rt on 12/1/14.
 */
public class TomasuloInstruction {

    String opcode;
    int opcodeNumberValue;
    int functionCode;
    String destinationRegister;
    boolean firstSourceRegister;
    boolean secondSourceRegister;
    boolean isFirstSourceRegisterBit;
    boolean isSecondSourceRegisterBit;
    boolean destinationRegisterBit;
    boolean immediateField;
    boolean immediateFieldStartingBit;
    boolean immediateFieldEndingBit;

    public TomasuloInstruction(){

    }

}
