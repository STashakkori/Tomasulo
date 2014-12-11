import java.util.Collection;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Created by rt on 11/30/14.
 */
public class TomasuloReservationStationManager {

    public TomasuloReservationStation[] reservationStations = new TomasuloReservationStation[29];

    int integerUnitRSCount = 8;
    int floatingPointUnitRSCount = 4;
    int trapUnitRSCount = 1;
    int branchUnitRSCount = 8;
    int memoryUnitRSCount = 8;

    public TomasuloReservationStationManager(){
        // Initialize functional units :: integer units
        for(int i = 0; i < integerUnitRSCount; i++){
            reservationStations[i] = new TomasuloReservationStation("int" + i,"int");
        }

        // Initialize functional units :: integer units
        for(int i = 0; i < floatingPointUnitRSCount; i++){
            reservationStations[i + 8] = new TomasuloReservationStation("float" + i,"float");
        }

        // Initialize functional units :: integer units
        for(int i = 0; i < trapUnitRSCount; i++){
            reservationStations[i + 12] = new TomasuloReservationStation("trap" + i,"trap");
        }

        // Initialize functional units :: integer units
        for(int i = 0; i < branchUnitRSCount; i++){
            reservationStations[i + 13] = new TomasuloReservationStation("branch" + i,"branch");
        }

        // Initialize functional units :: integer units
        for(int i = 0; i < memoryUnitRSCount; i++){
            reservationStations[i + 21] = new TomasuloReservationStation("mem" + i,"mem");
        }
    }

    public boolean hasAvailableRStation(){
        for(TomasuloReservationStation rs: reservationStations){
            if(!rs.busy) return true;
        }
        return false;
    }

    /*String fuName;
                for(int i = 0; i < fuManager.functionalUnits.length; i++) {
                    if (fuManager.functionalUnits[i].getClass().getName().equals("TomasuloIntegerFunctionalUnit")
                            && !fuManager.functionalUnits[i].isFunctionalUnitBusy) {
                        fuName = fuManager.functionalUnits[i].name;
                        fuManager.functionalUnits[i].isFunctionalUnitBusy = true;
                        break;
                    }


                    for(int i = 0 ; i < rfManager.registers.length; i++){
                    if(instruction.getDestinationRegister().equals(rfManager.registers[i].registerName)){
                        if(rfManager.registers[i].Qi == null){
                            rfManager.registers[i].Qi = fuName;
                            return false;
                        }
                        else{
                            for(TomasuloReservationStation rs : reservationStations){
                                if(!rs.busy) rs.Qj = rfManager.registers[i].Qi;
                                break;
                            }
                        }
                    }
                        return false;
                    else if(fuManager.functionalUnits[i].isFunctionalUnitBusy) return true;
                }
                }*/

    public boolean issue(String instructionType, TomasuloInstruction instruction, TomasuloFunctionalUnitManager fuManager, TomasuloRegisterFileManager rfManager){
        TomasuloRegister srcReg1 = rfManager.getRegister(instruction.firstSourceRegister);
        TomasuloRegister srcReg2 = rfManager.getRegister(instruction.secondSourceRegister);
        TomasuloRegister destReg = rfManager.getRegister(instruction.destinationRegister);
        int immediateValue = instruction.immediateValue;
        TomasuloReservationStation freeStation = null;

        switch(instructionType) {
            case "int":
                for (TomasuloReservationStation rs : reservationStations) {
                    if (!rs.busy && rs.type.equals(instructionType)) {
                        freeStation = rs;
                        break;
                    }
                }
                if (srcReg1 != null && srcReg1.Qi != null) {
                    freeStation.Qj = srcReg1.Qi;
                } else if(srcReg1 != null) {
                    freeStation.Vj = srcReg1.value;
                    freeStation.Qj = null;
                }
                if (srcReg2 != null && srcReg2.Qi != null) {
                    freeStation.Qk = srcReg2.Qi;
                } else if(srcReg2 != null){
                    freeStation.Vj = srcReg2.value;
                    freeStation.Qk = null;
                }
                freeStation.busy = true;
                System.out.println("Free station name: " + freeStation.name);
                destReg.Qi = freeStation.name;
                return false;

            case "trap":

                for (TomasuloReservationStation rs : reservationStations) {
                    if (!rs.busy && rs.type.equals(instructionType)) {
                        freeStation = rs;
                        break;
                    }
                }
                fuManager.trapBuffer.add(freeStation);
                if(freeStation == null) return true;
                if (srcReg1 != null && srcReg1.Qi != null) {
                    freeStation.Qj = srcReg1.Qi;
                } else if(srcReg1 != null) {
                    freeStation.Vj = srcReg1.value;
                    freeStation.Qj = null;
                }
                freeStation.A = immediateValue;
                freeStation.busy = true;
                return false;

                /*
            case "mem":
                String fuName;
                for(int i = 0; i < fuManager.functionalUnits.length; i++) {
                    if (fuManager.functionalUnits[i].getClass().getName().equals("TomasuloIntegerFunctionalUnit")
                            && !fuManager.functionalUnits[i].isFunctionalUnitBusy) {
                        fuName = fuManager.functionalUnits[i].name;
                        fuManager.functionalUnits[i].isFunctionalUnitBusy = true;
                        break;
                    }
                }


                for(int i = 0; i < fuManager.functionalUnits.length; i++){
                    if(fuManager.functionalUnits[i].getClass().getName().equals("TomasuloMemoryFunctionalUnit")
                            && !fuManager.functionalUnits[i].isFunctionalUnitBusy){
                        for(int j = 0 ; j < rfManager.registers.length; j++){
                            if(instruction.getDestinationRegister().equals(rfManager.registers[j].registerName)){
                                if(rfManager.registers[j].Qi == null){
                                    rfManager.registers[j].Qi = fuManager.functionalUnits[i].name;
                                    fuManager.functionalUnits[i].isFunctionalUnitBusy = true;
                                    return false;
                                }
                                else{
                                    for(TomasuloReservationStation rs : reservationStations){
                                        if(!rs.busy) rs.Qj = rfManager.registers[j].Qi;
                                        fuManager.functionalUnits[i].isFunctionalUnitBusy = true;
                                        break;
                                    }
                                }
                            }
                        }
                        return false;
                    }
                    else if(fuManager.functionalUnits[i].isFunctionalUnitBusy) return true;
                }
            case "fp":
                String fuName;
                for(int i = 0; i < fuManager.functionalUnits.length; i++) {
                    if (fuManager.functionalUnits[i].getClass().getName().equals("TomasuloIntegerFunctionalUnit")
                            && !fuManager.functionalUnits[i].isFunctionalUnitBusy) {
                        fuName = fuManager.functionalUnits[i].name;
                        fuManager.functionalUnits[i].isFunctionalUnitBusy = true;
                        break;
                    }
                }


                for(int i = 0; i < fuManager.functionalUnits.length; i++){
                    if(fuManager.functionalUnits[i].getClass().getName().equals("TomasuloFloatingPointFunctionalUnit")
                            && !fuManager.functionalUnits[i].isFunctionalUnitBusy){
                        for(int j = 0 ; j < rfManager.registers.length; j++){
                            if(instruction.getDestinationRegister().equals(rfManager.registers[j].registerName)){
                                if(rfManager.registers[j].Qi == null){
                                    rfManager.registers[j].Qi = fuManager.functionalUnits[i].name;
                                    fuManager.functionalUnits[i].isFunctionalUnitBusy = true;
                                    return false;
                                }
                                else{
                                    for(TomasuloReservationStation rs : reservationStations){
                                        if(!rs.busy) rs.Qj = rfManager.registers[j].Qi;
                                        fuManager.functionalUnits[i].isFunctionalUnitBusy = true;
                                        break;
                                    }
                                }
                            }
                        }
                        return false;
                    }
                    else if(fuManager.functionalUnits[i].isFunctionalUnitBusy) return true;
                }
            case "branch":
                String fuName;
                for(int i = 0; i < fuManager.functionalUnits.length; i++) {
                    if (fuManager.functionalUnits[i].getClass().getName().equals("TomasuloIntegerFunctionalUnit")
                            && !fuManager.functionalUnits[i].isFunctionalUnitBusy) {
                        fuName = fuManager.functionalUnits[i].name;
                        fuManager.functionalUnits[i].isFunctionalUnitBusy = true;
                        break;
                    }
                }


                for(int i = 0; i < fuManager.functionalUnits.length; i++){
                    if(fuManager.functionalUnits[i].getClass().getName().equals("TomasuloBranchFunctionalUnit")
                            && !fuManager.functionalUnits[i].isFunctionalUnitBusy){
                        for(int j = 0 ; j < rfManager.registers.length; j++){
                            if(instruction.getDestinationRegister().equals(rfManager.registers[j].registerName)){
                                if(rfManager.registers[j].Qi == null){
                                    rfManager.registers[j].Qi = fuManager.functionalUnits[i].name;
                                    fuManager.functionalUnits[i].isFunctionalUnitBusy = true;
                                    return false;
                                }
                                else{
                                    for(TomasuloReservationStation rs : reservationStations){
                                        if(!rs.busy) rs.Qj = rfManager.registers[j].Qi;
                                        fuManager.functionalUnits[i].isFunctionalUnitBusy = true;
                                        break;
                                    }
                                }
                            }
                        }
                        return false;
                    }
                    else if(fuManager.functionalUnits[i].isFunctionalUnitBusy) return true;
                }
            case "trap":
                String fuName;
                for(int i = 0; i < fuManager.functionalUnits.length; i++) {
                    if (fuManager.functionalUnits[i].getClass().getName().equals("TomasuloIntegerFunctionalUnit")
                            && !fuManager.functionalUnits[i].isFunctionalUnitBusy) {
                        fuName = fuManager.functionalUnits[i].name;
                        fuManager.functionalUnits[i].isFunctionalUnitBusy = true;
                        break;
                    }
                }


                for(int i = 0; i < fuManager.functionalUnits.length; i++){
                    if(fuManager.functionalUnits[i].getClass().getName().equals("TomasuloTrapFunctionalUnit")
                            && !fuManager.functionalUnits[i].isFunctionalUnitBusy){
                        for(int j = 0 ; j < rfManager.registers.length; j++){
                            if(instruction.getDestinationRegister().equals(rfManager.registers[j].registerName)){
                                if(rfManager.registers[j].Qi == null){
                                    rfManager.registers[j].Qi = fuManager.functionalUnits[i].name;
                                    fuManager.functionalUnits[i].isFunctionalUnitBusy = true;
                                    return false;
                                }
                                else{
                                    for(TomasuloReservationStation rs : reservationStations){
                                        if(!rs.busy) rs.Qj = rfManager.registers[j].Qi;
                                        fuManager.functionalUnits[i].isFunctionalUnitBusy = true;
                                        break;
                                    }
                                }
                            }
                        }
                        return false;
                    }
                    else if(fuManager.functionalUnits[i].isFunctionalUnitBusy) return true;
                }*/
        }
        return true;
    }

    public TomasuloReservationStation[] getReservationStations() {
        return reservationStations;
    }

    public int getIntegerUnitRSCount() {
        return integerUnitRSCount;
    }

    public int getFloatingPointUnitRSCount() {
        return floatingPointUnitRSCount;
    }

    public int getTrapUnitRSCount() {
        return trapUnitRSCount;
    }

    public int getBranchUnitRSCount() {
        return branchUnitRSCount;
    }

    public int getMemoryUnitRSCount() {
        return memoryUnitRSCount;
    }

    public void execute(TomasuloFunctionalUnitManager fuManager){
        for(TomasuloReservationStation rs : reservationStations){
            rs.execute(fuManager);
        }
    }

    public void printStationContents(){

    }
}
