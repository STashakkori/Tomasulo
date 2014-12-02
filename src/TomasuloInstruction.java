/**
 * Created by rt on 12/1/14.
 */
public class TomasuloInstruction {

    String opcode;
    int opcodeNo;
    int funCode;
    String dstReg;
    boolean dstRegBit;
    boolean firstSourceReg;
    boolean isFirstSourceRegBit;
    boolean secondSource;
    boolean isSecondSourceRegBit;
    boolean immedField;
    boolean immedFieldStartingBit;
    boolean immedFieldEndingBit;

    public TomasuloInstruction(){

    }

}
