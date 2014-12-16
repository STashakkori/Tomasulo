/**
 * Created by sina on 11/30/14.
 */
public class TomasuloReservationStation {

    public String name; // name of the reservation station
    public boolean busy; // set if the reservation station
    public String opcode; // the opcodeName
    public int Vj; // first operand
    public int Vk; // second operand
    public String Qj; // reservation station for Vj
    public String Qk; // reservation station for Vk
    public int A; // immediate field or effective address
    public int result; // the result
    public boolean resultReady; // result ready for writing yet?
    public boolean resultWritten; // result written yet?
    public String type;
    public int cycleCounter;
    public boolean isExecuting;
    public TomasuloInstruction currentInstruction;
    public TomasuloFunctionalUnit executingUnit;
    public int branchValue;

    public TomasuloReservationStation(String nameOfStation, String type){
        this.name = nameOfStation;
        opcode = null;
        Vj = 0;
        Vk = 0;
        Qj = null;
        Qk = null;
        A = 0;
        result = 0;
        resultReady = false;
        resultWritten = false;
        this.type = type;
        cycleCounter = 0;
        isExecuting = false;
        currentInstruction = null;
        executingUnit = null;
        branchValue = 0;
    }

    public void resetContents(){
        this.setBusy(false);
        opcode = null;
        Vj = 0;
        Vk = 0;
        Qj = null;
        Qk = null;
        A = 0;
        result = 0;
        resultReady = false;
        resultWritten = false;
        currentInstruction = null;
        executingUnit = null;
        branchValue = 0;

        switch(type){
            case "int":
                cycleCounter = 1;
                break;
            case "float":
                cycleCounter = 4;
                break;
            case "trap":
                cycleCounter = 1;
                break;
            case "branch":
                cycleCounter = 1;
                break;
            case "mem":
                cycleCounter = 2;
                break;
        }
    }

    public void setExecutingUnit(TomasuloFunctionalUnit executingUnit) {
        this.executingUnit = executingUnit;
    }

    public void printContents(){

    }

    public void setCurrentInstruction(TomasuloInstruction currentInstruction) {
        this.currentInstruction = currentInstruction;
    }

    public void setBusy(boolean busy) {
        this.busy = busy;
    }

    public boolean isReadyForExecution(){
        return (busy == true && Qj == null && Qk == null && resultReady == false && isExecuting == false);
    }

    public void execute(TomasuloFunctionalUnitManager fuManager){

    }

    public String addPadding(){
        return "";
    }

}
