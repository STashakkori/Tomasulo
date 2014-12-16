import java.io.PrintWriter;
import java.util.Queue;

/**
 * Created by sina on 12/3/14.
 */
public class TomasuloTrapFunctionalUnit extends TomasuloFunctionalUnit {

    public int computeResult(TomasuloReservationStation rs,TomasuloRegisterFileManager rfm, PrintWriter writer,TomasuloMemory memory,
                             Queue<TomasuloReservationStation> trapBuffer,Queue<TomasuloReservationStation> loadBuffer) {

            int value = rs.Vj;

            switch (rs.A){
                case 1:
                    System.out.print(value);
                    return 0;
                case 2:
                    System.out.print(Float.intBitsToFloat(value));
                    return 0;
                case 3:
                    //System.out.println("TRAP");
                    String data = memory.getDataString(value);
                    System.out.print(data);
                    return 0;
        }
        return 0;
    }
}
