import java.io.PrintWriter;
import java.util.Queue;

/**
 * Created by sina on 11/28/14.
 */
public abstract class TomasuloFunctionalUnit {

    boolean isFunctionalUnitBusy; //flag indicating whether the functional unit is currently executing an instruction
    int executionCycleCount; //number of execution cycles this unit takes to complete
    int remainingExecutionCycles; //number of execution cycles remaining for currently executing instruction
    TomasuloInstruction currentInstruction;
    String referenceToReservationStation; //index into the RS array identifying current instruction being executed
    String name;

    public TomasuloCommonDataBus write(){
        return null;
    }

    abstract int computeResult(TomasuloReservationStation reservationStation, TomasuloRegisterFileManager rf,PrintWriter writer,TomasuloMemory memory,
                               Queue<TomasuloReservationStation> trapBuffer, Queue<TomasuloReservationStation> loadBuffer);

}
