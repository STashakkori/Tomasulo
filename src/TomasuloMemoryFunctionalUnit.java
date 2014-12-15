import java.io.PrintWriter;
import java.util.Queue;

/**
 * Created by rt on 12/3/14.
 */
public class TomasuloMemoryFunctionalUnit extends TomasuloFunctionalUnit{

    public int computeResult(TomasuloReservationStation reservationStation,TomasuloRegisterFileManager rf,PrintWriter writer,TomasuloMemory memory,
                             Queue<TomasuloReservationStation> trapBuffer,Queue<TomasuloReservationStation> loadBuffer){
        if(reservationStation.currentInstruction == null) return 0;
        switch(reservationStation.currentInstruction.opcodeName){
            case "lw":
                int word = memory.fetchWord(reservationStation.Vj + reservationStation.A);
                return word;
            case "lf":
                word = memory.fetchWord(reservationStation.Vj + reservationStation.A);
                return word;
            case "sw":
                memory.storeWord(reservationStation.Vj + reservationStation.A,rf.getRegister(reservationStation.currentInstruction.destinationRegister).value);
                return rf.getRegister(reservationStation.currentInstruction.destinationRegister).value;
            case "sf":
                memory.storeWord(reservationStation.Vj + reservationStation.A,rf.getRegister(reservationStation.currentInstruction.destinationRegister).value);
                return rf.getRegister(reservationStation.currentInstruction.destinationRegister).value;
        }
        return 0;
    }
}
