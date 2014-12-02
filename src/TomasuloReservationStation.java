/**
 * Created by rt on 11/30/14.
 */
public class TomasuloReservationStation {

    public String name; // name of the reservation station
    public boolean busy; // set if the reservation station
    public String opcode; // the opcode
    public long Vj; // first operand
    public long Vk; // second operand
    public String Qj; // reservation station for Vj
    public String Qk; // reservation station for Vk
    public long A; // immediate field or effective address
    public long result; // the result
    public boolean resultReady; // result ready for writing yet?
    public boolean resultWritten; // result written yet?

    public TomasuloReservationStation(String nameOfStation){
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

    public String addPadding(){
        return "";
    }

}
