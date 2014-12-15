import java.util.Collection;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * TomasuloReservationStationManager :: Class that holds all 32 reservation stations and issues instructions
 * to reservation stations.
 *
 * Created by sina on 11/30/14.
 */
public class TomasuloReservationStationManager {

    public TomasuloReservationStation[] reservationStations = new TomasuloReservationStation[29];

    int integerUnitRSCount = 8;
    int floatingPointUnitRSCount = 8;
    int trapUnitRSCount = 4;
    int branchUnitRSCount = 1;
    int memoryUnitRSCount = 8;
    public TomasuloInstruction issued;

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

    public boolean hasBranch(){
        for(TomasuloReservationStation rs: reservationStations){
            if(rs.type.equals("branch") && rs.busy) return true;
        }
        return false;
    }

    /**
     * issue :: Method that for each instruction moving through the pipeline, looks at the type of the operation
     * and issues a reservation station to that instruction, setting fields accordingly.
     *
     * @param instruction
     * @param fuManager
     * @param rfManager
     * @return
     */
    public boolean issue(TomasuloInstruction instruction, TomasuloFunctionalUnitManager fuManager, TomasuloRegisterFileManager rfManager){
        TomasuloRegister srcReg1 = rfManager.getRegister(instruction.firstSourceRegister);
        TomasuloRegister srcReg2 = rfManager.getRegister(instruction.secondSourceRegister);
        TomasuloRegister destReg = rfManager.getRegister(instruction.destinationRegister);
        int immediateValue = instruction.immediateValue;
        String instructionType = instruction.instructionType;
        issued = instruction;
        TomasuloReservationStation freeStation = null;

        switch(instructionType) {
            case "int":
            case "float":
                for (TomasuloReservationStation rs : reservationStations) {
                    if (!rs.busy && rs.type.equals(instructionType)) {
                        //System.out.println("---"+instruction.opcodeName+" "+rs.name+" "+instruction.getFirstSourceRegister()+" "+instruction.getSecondSourceRegister());
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
                    freeStation.Vk = srcReg2.value;
                    freeStation.Qk = null;
                }
                freeStation.setBusy(true);
                destReg.Qi = freeStation.name;
                freeStation.A = immediateValue;
                return false;

            case "trap":
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
                freeStation.A = immediateValue;
                freeStation.setBusy(true);
                fuManager.trapBuffer.add(freeStation);
                return false;

            case "mem":
                for (TomasuloReservationStation rs : reservationStations) {
                    if (!rs.busy && rs.type.equals(instructionType)) {
                        freeStation = rs;
                        freeStation.setCurrentInstruction(instruction);
                        break;
                    }
                }
                if(instruction.opcodeName.equals("lw")||instruction.opcodeName.equals("lf")) {
                    if (freeStation == null) return true;
                    if (srcReg1 != null && srcReg1.Qi != null) {
                        freeStation.Qj = srcReg1.Qi;
                    } else if (srcReg1 != null) {
                        freeStation.Vj = srcReg1.value;
                        freeStation.Qj = null;
                    }
                    freeStation.A = immediateValue;
                    freeStation.setBusy(true);
                    fuManager.loadstoreBuffer.add(freeStation);
                    if (destReg != null) destReg.Qi = freeStation.name;
                    return false;
                }
                else if(instruction.opcodeName.equals("sw")||instruction.opcodeName.equals("sf")){
                    if (freeStation == null) return true;
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
                        freeStation.Vk = srcReg2.value;
                        freeStation.Qk = null;
                    }
                    freeStation.A = immediateValue;
                    freeStation.setBusy(true);
                    fuManager.loadstoreBuffer.add(freeStation);
                    return false;
                }

            case "branch":
                for (TomasuloReservationStation rs : reservationStations) {
                    if (!rs.busy && rs.type.equals(instructionType)) {
                        freeStation = rs;
                        freeStation.setCurrentInstruction(instruction);
                        break;
                    }
                }
                if(freeStation == null) return true;

                if(instruction.opcodeName.equals("beqz")||instruction.opcodeName.equals("jalr")||instruction.opcodeName.equals("jr")){
                    if (srcReg1 != null && srcReg1.Qi != null) {
                        freeStation.Qj = srcReg1.Qi;
                    }
                    else if(srcReg1 != null) {
                        freeStation.Vj = srcReg1.value;
                        freeStation.Qj = null;
                    }
                }
                if(instruction.opcodeName.equals("jal")||instruction.opcodeName.equals(("jalr"))){
                    rfManager.getRegister("r31").Qi = freeStation.name;
                    instruction.destinationRegister = "r31";
                }
                freeStation.A = immediateValue;
                freeStation.setBusy(true);
                return false;
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
        int count = 1;
        for(TomasuloReservationStation rs : this.reservationStations){
            System.out.print("RSname:" + rs.name + " ");
            System.out.print("Busy:" + rs.busy + " ");
            System.out.print("Type:" + rs.type + " ");
            System.out.print("Qj:" + rs.Qj + " ");
            System.out.print("Qk:" + rs.Qk + " ");
            System.out.print("Vj:" + String.format("%08x", rs.Vj) + " ");
            System.out.print("Vk:" + String.format("%08x", rs.Vk) + " ");
            System.out.print("Cycles:" + rs.cycleCounter + " ");
            System.out.print("Ready:" + rs.resultReady + " ");
            System.out.print("Written:" + rs.resultWritten + " ");
            System.out.print("Exec:" + rs.isExecuting + " ");
            System.out.print("Result:" + String.format("%08x", rs.result) + " ");
            System.out.print("Immed:" + String.format("%08x", rs.A) + "   \n");
        }
        System.out.println("............................................................................................");
    }

    public void printIssuedInstruction(){
        if(issued == null) return;
        System.out.println("............. Issued this cycle .............");
        System.out.print("Opcode:" + issued.getOpcodeName());
        System.out.print(" type:" + "\"" + issued.instructionType + "\"");
        System.out.print(" src1:" + issued.firstSourceRegister);
        System.out.print(" src2:" + issued.secondSourceRegister);
        System.out.print(" dest:" + issued.destinationRegister);
        System.out.print(" immed:" + issued.immediateValue);
        System.out.println("\n.............................................");
    }
}
