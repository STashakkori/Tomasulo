/**
 * Created by rt on 12/1/14.
 */
public class TomasuloInstruction {

    String opcodeName;
    int functionCode;
    String firstSourceRegister;
    String secondSourceRegister;
    String destinationRegister;
    int immediateValue;
    boolean isFirstSourceRegisterBit;
    boolean isSecondSourceRegisterBit;
    boolean destinationRegisterBit;
    boolean immediateField;
    boolean immediateFieldStartingBit;
    boolean immediateFieldEndingBit;
    String registerDesign;

    public TomasuloInstruction(){
    }

    public void setOpcodeName(String opcodeName) {
        this.opcodeName = opcodeName;
    }

    public void setFunctionCode(int functionCode) {
        this.functionCode = functionCode;
    }

    public void setDestinationRegister(String destinationRegister) {
        this.destinationRegister = destinationRegister;
    }

    public void setFirstSourceRegisterBit(boolean isFirstSourceRegisterBit) {
        this.isFirstSourceRegisterBit = isFirstSourceRegisterBit;
    }

    public void setSecondSourceRegisterBit(boolean isSecondSourceRegisterBit) {
        this.isSecondSourceRegisterBit = isSecondSourceRegisterBit;
    }

    public void setDestinationRegisterBit(boolean destinationRegisterBit) {
        this.destinationRegisterBit = destinationRegisterBit;
    }

    public void setImmediateField(boolean immediateField) {
        this.immediateField = immediateField;
    }

    public void setImmediateFieldStartingBit(boolean immediateFieldStartingBit) {
        this.immediateFieldStartingBit = immediateFieldStartingBit;
    }

    public void setImmediateFieldEndingBit(boolean immediateFieldEndingBit) {
        this.immediateFieldEndingBit = immediateFieldEndingBit;
    }

    public String getOpcodeName() {

        return opcodeName;
    }

    public void setRegisterDesign(String registerDesign) {
        this.registerDesign = registerDesign;
    }

    public String getRegisterDesign() {

        return registerDesign;
    }

    public void setImmediateValue(int immediateValue) {
        this.immediateValue = immediateValue;
    }

    public int getImmediateValue() {

        return immediateValue;
    }


    public int getFunctionCode() {
        return functionCode;
    }

    public String getDestinationRegister() {
        return destinationRegister;
    }

    public boolean isFirstSourceRegisterBit() {
        return isFirstSourceRegisterBit;
    }

    public boolean isSecondSourceRegisterBit() {
        return isSecondSourceRegisterBit;
    }

    public boolean isDestinationRegisterBit() {
        return destinationRegisterBit;
    }

    public boolean isImmediateField() {
        return immediateField;
    }

    public boolean isImmediateFieldStartingBit() {
        return immediateFieldStartingBit;
    }

    public boolean isImmediateFieldEndingBit() {
        return immediateFieldEndingBit;
    }

    public void setFirstSourceRegister(String firstSourceRegister) {
        this.firstSourceRegister = firstSourceRegister;
    }

    public void setSecondSourceRegister(String secondSourceRegister) {
        this.secondSourceRegister = secondSourceRegister;
    }

    public String getFirstSourceRegister() {

        return firstSourceRegister;
    }

    public String getSecondSourceRegister() {
        return secondSourceRegister;
    }
}
