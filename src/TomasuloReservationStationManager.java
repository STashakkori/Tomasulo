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
    int floatingPointUnitRSCount = 8;
    int trapUnitRSCount = 4;
    int branchUnitRSCount = 1;
    int memoryUnitRSCount = 8;

    public TomasuloReservationStationManager(){
        // Initialize rstations :: int rstations
        for(int i = 0; i < integerUnitRSCount; i++){
            reservationStations[i] = new TomasuloReservationStation("int" + i,"int");
            reservationStations[i].cycleCounter = 1;
        }

        // Initialize rstations :: float rstations
        for(int i = 0; i < floatingPointUnitRSCount; i++){
            reservationStations[i + 8] = new TomasuloReservationStation("float" + i,"float");
            reservationStations[i + 8].cycleCounter = 4;
        }

        // Initialize rstations :: trap rstations
        for(int i = 0; i < trapUnitRSCount; i++){
            reservationStations[i + 16] = new TomasuloReservationStation("trap" + i,"trap");
            reservationStations[i + 16].cycleCounter = 1;
        }

        // Initialize functional units :: branch units
        for(int i = 0; i < branchUnitRSCount; i++){
            reservationStations[i + 20] = new TomasuloReservationStation("branch" + i,"branch");
            reservationStations[i + 20].cycleCounter = 1;
        }

        // Initialize rstations units :: mem rstations
        for(int i = 0; i < memoryUnitRSCount; i++){
            reservationStations[i + 21] = new TomasuloReservationStation("mem" + i,"mem");
            reservationStations[i + 21].cycleCounter = 2;
        }
    }

    public boolean hasAvailableRStation(String instructionType){
        for(TomasuloReservationStation rs: reservationStations){
            if(rs.type.equals(instructionType) && !rs.busy) return true;
        }
        return false;
    }

    public boolean issue(TomasuloInstruction instruction, TomasuloFunctionalUnitManager fuManager, TomasuloRegisterFileManager rfManager){
        TomasuloRegister srcReg1 = rfManager.getRegister(instruction.firstSourceRegister);
        TomasuloRegister srcReg2 = rfManager.getRegister(instruction.secondSourceRegister);
        TomasuloRegister destReg = rfManager.getRegister(instruction.destinationRegister);

        String instructionType = instruction.instructionType;
        int immediateValue = instruction.immediateValue;
        TomasuloReservationStation freeStation = null;

        //if(srcReg1!=null) //System.out.println("srcReg1:" + srcReg1.value);
        //if(srcReg2!=null) //System.out.println("srcReg2:" + srcReg2.value);
        //if(destReg!=null) //System.out.println("destReg:" + destReg.value);
        //System.out.println("Immmed:" + immediateValue);

        switch(instructionType) {
            case "int":
                //System.out.println("int issue test");
                for (TomasuloReservationStation rs : reservationStations) {
                    if (!rs.busy && rs.type.equals(instructionType)) {
                        freeStation = rs;
                        freeStation.setCurrentInstruction(instruction);
                        break;
                    }
                }
                if(freeStation == null) return true;

                if (srcReg1 != null && srcReg1.Qi != null) {
                    freeStation.Qj = srcReg1.Qi;
                }
                else if(srcReg1 != null) {
                    freeStation.Vj = srcReg1.value;
                    freeStation.Qj = null;
                }

                if (srcReg2 != null && srcReg2.Qi != null) {
                    freeStation.Qk = srcReg2.Qi;
                }
                else if(srcReg2 != null){
                    freeStation.Vj = srcReg2.value;
                    freeStation.Qk = null;
                }
                freeStation.setBusy(true);
                //System.out.println("Free station name: " + freeStation.name);
                destReg.Qi = freeStation.name;
                freeStation.A = immediateValue;
                return false;

            case "trap":
                //System.out.println("trap issue test");
                //System.out.println("trap reg: " + srcReg1.registerName);
                for (TomasuloReservationStation rs : reservationStations) {
                    if (!rs.busy && rs.type.equals(instructionType)) {
                        freeStation = rs;
                        freeStation.setCurrentInstruction(instruction);
                        break;
                    }
                }
                if(freeStation == null) return true;
                for(TomasuloReservationStation rs : fuManager.trapBuffer){
                    //System.out.println("Trap station:" + rs.name);
                }

                if (srcReg1 != null && srcReg1.Qi != null) {
                    freeStation.Qj = srcReg1.Qi;
                }
                else if(srcReg1 != null) {
                    freeStation.Vj = srcReg1.value;
                    freeStation.Qj = null;
                }
                freeStation.A = immediateValue;
                freeStation.setBusy(true);
                fuManager.trapBuffer.add(freeStation);
                for(TomasuloReservationStation rs : fuManager.trapBuffer) {
                    //System.out.println("!!!!!!!"+rs.name);
                }
                return false;

            case "float":
                //System.out.println("float issue test");
                return false;
            //case "branch":
        }
        return true;
    }

    public TomasuloReservationStation grabBusyRStation(){
        for(TomasuloReservationStation rs:reservationStations){
            if(rs.busy){
                return rs;
            }
        }
        return null;
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

    public boolean allUnitsCompleted(){
        for(TomasuloReservationStation rs : this.reservationStations)
            if(rs.busy)
                return false;
        return true;
    }

    public void printStationContents(){
        /*int count = 1;
        for(TomasuloReservationStation rs : this.reservationStations){
            System.out.print("RSname:" + rs.name + " ");
            System.out.print("Busy:" + rs.busy + " ");
            System.out.print("Type:" + rs.type + " ");
            System.out.print("Qj:" + rs.Qj + " ");
            System.out.print("Qk:" + rs.Qk + " ");
            System.out.print("Vj:" + String.format("%08x", rs.Vj) + " ");
            System.out.print("Vk:" + rs.Vk + " ");
            System.out.print("Cycles:" + rs.cycleCounter + " ");
            System.out.print("Ready:" + rs.resultReady + " ");
            System.out.print("Written:" + rs.resultWritten + " ");
            System.out.print("Exec:" + rs.isExecuting + " ");
            System.out.print("Result:" + rs.result + " ");
            System.out.print("Immed: " + rs.A + "   ");
            if(count % 2 ==0) System.out.println();
            count++;
        }
        System.out.println();*/
    }
}
