/**
 * Created by rt on 11/30/14.
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

    public TomasuloReservationStation(String nameOfStation, String type){
        this.name = nameOfStation;
        busy = false;
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
    }

    /*
    public void setForExecution(){
        this.busy =
     }
    */

    public void resetContents(){
        busy = false;
        opcode = null;
        Vj = 0;
        Vk = 0;
        Qj = null;
        Qk = null;
        A = 0;
        result = 0;
        resultReady = false;
        resultWritten = false;
    }

    public void printContents(){

    }

    public boolean isReadyForExecution(){
        return (busy == true && Qj == null && Qk == null && resultReady == false);
    }

    public void execute(TomasuloFunctionalUnitManager fuManager){

    }

    public String addPadding(){
        return "";
    }

}
