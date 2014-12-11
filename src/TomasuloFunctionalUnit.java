/**
 * Created by rt on 11/28/14.
 */
public class TomasuloFunctionalUnit {

    boolean isFunctionalUnitBusy; //flag indicating whether the functional unit is currently executing an instruction
    int executionCycleCount; //number of execution cycles this unit takes to complete
    int remainingExecutionCycles; //number of execution cycles remaining for currently executing instruction
    TomasuloInstruction currentInstruction;
    int referenceToReservationStation; //index into the RS array identifying current instruction being executed
    String name;

    public TomasuloCommonDataBus write(){
        return null;
    }

    public void execute(){
        /*if (!isFunctionalUnitBusy)
        {
            TomasuloFunctionalUnitcurrentInstruction = findInstructionToExecute();
            // If FU is free & an instruction is ready to go
            if (currentInstruction != -1)
            {
                StatusTable.getInstance().updateStartEX(RS[currentInstruction].name);
                FUbusy = true;
                executionCycles = (executionCount-1);
            }
        }
        else
        {
            executionCycles--;
            // If FU just finished executing an instruction
            if (executionCycles == 0)
            {
                FUbusy = false;
                RS[currentInstruction].resultReady = true;
                computeResult(currentInstruction);
                StatusTable.getInstance().updateEndEX(RS[currentInstruction].name);
            }
        }
        return false; */

        //return false; //change this eventually
    }

}
