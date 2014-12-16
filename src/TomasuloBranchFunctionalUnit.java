import java.io.PrintWriter;
import java.util.Queue;

/**
 * TomasuloFunctionalUnit :: Class that represents a single functional unit. Computes a result based on operands.
 *
 * Created by sina on 12/3/14.
 */
public class TomasuloBranchFunctionalUnit extends TomasuloFunctionalUnit {

    /**
     * computeResult :: Method that calculates how the PC should be updated based on the branch instruction.
     *
     * @param reservationStation
     * @param rf
     * @param writer
     * @param memory
     * @param trapBuffer
     * @param loadBuffer
     * @return
     */
    public int computeResult(TomasuloReservationStation reservationStation,TomasuloRegisterFileManager rf,PrintWriter writer,TomasuloMemory memory,
                             Queue<TomasuloReservationStation> trapBuffer,Queue<TomasuloReservationStation> loadBuffer){

        TomasuloProgramCounter pc = TomasuloProgramCounter.getInstance();
        int oldPC = 0;

        if(reservationStation.currentInstruction == null) return 0;
        switch(reservationStation.currentInstruction.opcodeName){
            case "beqz":
                if(reservationStation.Vj == 0) {
                    reservationStation.branchValue = pc.address + reservationStation.A;
                    return pc.address + reservationStation.A;
                }
                else {
                    reservationStation.branchValue = pc.address;
                    return pc.address;
                }
            case "jalr":
                oldPC= pc.address;
                reservationStation.branchValue = reservationStation.Vj + reservationStation.A;
            return oldPC;

            case "jr":
                reservationStation.branchValue = reservationStation.Vj;
            return pc.address;

            case "jal":
                oldPC = pc.address;
                reservationStation.branchValue = pc.address + reservationStation.A;
            return oldPC;

            case "j":
                reservationStation.branchValue = pc.address + reservationStation.A;
            return pc.address;
        }
        return 0;
    }
}
