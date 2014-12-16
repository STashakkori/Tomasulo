/**
 * Created by sina on 12/3/14.
 */
public class TomasuloTools {

    public int getBitsInRange(int start, int end, int bits) {
        bits = bits << start;
        bits = bits >>> start + (31 - end);
        return bits;
    }

    public int takeTwosCompliment(int number, int bits){
        if((number & (1 << (bits - 1))) != 0)
            number -= 1 << bits;
        return number;
    }
}
