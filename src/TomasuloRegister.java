/**
 * Created by rt on 11/29/14.
 */
public class TomasuloRegister {
    public String Qi;
    public String registerName;
    public int value;
    public boolean hasOperand;

    public TomasuloRegister(String name){
        Qi = null;
        registerName = name;
        value = 0;
    }

    public boolean isAvailable(int registerNumber){
        return ((Qi == null) ? true : false);
    }

    public void setHasOperand(boolean hasOperand) {
        this.hasOperand = hasOperand;
    }

    public boolean isHasOperand() {

        return hasOperand;
    }
}
