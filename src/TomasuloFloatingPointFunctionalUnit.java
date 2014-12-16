import java.io.PrintWriter;
import java.util.Queue;

/**
 * Created by sina on 12/3/14.
 */
public class TomasuloFloatingPointFunctionalUnit extends TomasuloFunctionalUnit {

    public int computeResult(TomasuloReservationStation reservationStation,TomasuloRegisterFileManager rf,PrintWriter writer, TomasuloMemory memory,
                             Queue<TomasuloReservationStation> trapBuffer,Queue<TomasuloReservationStation> loadBuffer) {

        if (reservationStation.currentInstruction == null) return 0;
        float result;
        switch (reservationStation.currentInstruction.opcodeName) {
            case "addf":
                result = Float.intBitsToFloat(reservationStation.Vj) + Float.intBitsToFloat(reservationStation.Vk);
                return Float.floatToIntBits(result);
            case "subf":
                result = Float.intBitsToFloat(reservationStation.Vj) - Float.intBitsToFloat(reservationStation.Vk);
                return Float.floatToIntBits(result);
            case "multf":
                result = Float.intBitsToFloat(reservationStation.Vj) * Float.intBitsToFloat(reservationStation.Vk);
                return Float.floatToIntBits(result);
            case "divf":
                result = Float.intBitsToFloat(reservationStation.Vj) / Float.intBitsToFloat(reservationStation.Vk);
                return Float.floatToIntBits(result);
            case "mult":
                return (reservationStation.Vj * reservationStation.Vk);
            case "div":
                return (reservationStation.Vj / reservationStation.Vk);
            case "cvtf2i":
                return (int)Float.intBitsToFloat(reservationStation.Vj);
            case "cvti2f":
                return Float.floatToIntBits((float)reservationStation.Vj);
        }
        return 0;
    }
}
