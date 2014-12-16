/**
 * Created by sina on 11/29/14.
 */
public class TomasuloCommonDataBus {

    int result;
    String nameOfWritingReservationStation;

    public String getNameOfSourceFunctionalUnit() {
        return nameOfSourceFunctionalUnit;
    }

    String nameOfSourceFunctionalUnit;

    public void setNameOfSourceFunctionalUnit(String nameOfSourceFunctionalUnit) {
        this.nameOfSourceFunctionalUnit = nameOfSourceFunctionalUnit;
    }

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

    public void setResult(int result) {
        this.result = result;
    }

    public int getResult(){
        return result;
    }
}
