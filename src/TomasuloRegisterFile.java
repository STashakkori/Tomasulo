/**
 * Created by rt on 11/29/14.
 */
public class TomasuloRegisterFile {
    protected long[] registers;
    protected String[] Qi;
    static final int REGISTERFILESIZE = 32;

    public TomasuloRegisterFile(){
        registers = new long[REGISTERFILESIZE];
        Qi = new String[REGISTERFILESIZE];
    }

    public boolean isAvailable(int registerNumber){
        return ((Qi[registerNumber] == null) ? true : false);
    }

    public long getRegister(int registerNumber)
    {
        return registers[registerNumber];
    }

}
