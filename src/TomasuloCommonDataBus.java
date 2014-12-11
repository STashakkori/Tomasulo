/**
 * Created by rt on 11/29/14.
 */
public class TomasuloCommonDataBus {

    long result;
    String nameOfWritingReservationStation;
    static TomasuloCommonDataBus cdbInstance = null;

    static TomasuloCommonDataBus getInstance(){
        if (cdbInstance == null)
            cdbInstance = new TomasuloCommonDataBus();
        return cdbInstance;
    }

    public void setReservationStation(String rsName){
        this.nameOfWritingReservationStation = rsName;
    }

    public String getRSName() {
        return nameOfWritingReservationStation;
    }
    public long getResult(){
        return result;
    }
}
