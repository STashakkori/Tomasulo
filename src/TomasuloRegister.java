/**
 * Created by sina on 11/29/14.
 */
public class TomasuloRegister {
    public String Qi;
    public String registerName;
    public int value;
    public boolean hasOperand;

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue(){
        return value;
    }

    public TomasuloRegister(String name){
        Qi = null;
        registerName = name;
        value = 0;

    }

    public boolean isAvailable(){
        return ((Qi == null && hasOperand == false) ? true : false);
    }

    public void setHasOperand(boolean hasOperand) {
        this.hasOperand = hasOperand;
    }

    public boolean isHasOperand() {
        return hasOperand;
    }
}
