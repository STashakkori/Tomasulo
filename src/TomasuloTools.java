/**
 * Created by rt on 12/3/14.
 */
public class TomasuloTools {

    public static int getBitsInRange(int start, int end, int bits) {
        bits = bits << start;
        bits = bits >>> start + (31 - end);
        return bits;
    }



}
