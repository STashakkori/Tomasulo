import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Created by rt on 11/30/14.
 */
public class TomasuloFunctionalUnitManager {

    TomasuloFunctionalUnit[] functionalUnits = new TomasuloFunctionalUnit[8];
    public Queue<TomasuloReservationStation> trapBuffer;
    public Queue<TomasuloReservationStation> loadstoreBuffer;

    public TomasuloFunctionalUnitManager(){

        trapBuffer = new LinkedList<TomasuloReservationStation>();
        loadstoreBuffer = new LinkedList<TomasuloReservationStation>();

        // Initialize functional units :: integer units
        for(int i = 0; i < 3; i++){
            functionalUnits[i] = new TomasuloIntegerFunctionalUnit();
            functionalUnits[i].name = "Int" + i;
        }

        // Initialize functional units :: trap units
            functionalUnits[3] = new TomasuloTrapFunctionalUnit();
            functionalUnits[3].name = "Trap0";

        // Initialize functional units :: memory units
            functionalUnits[4] = new TomasuloMemoryFunctionalUnit();
            functionalUnits[4].name = "Mem0";

        // Initialize functional units :: floating point units
        for(int i = 5; i < 7; i++){
            functionalUnits[i] = new TomasuloFloatingPointFunctionalUnit();
            functionalUnits[i].name = "Float" + i % 5;
        }

        // Initialize functional units :: branch units
            functionalUnits[7] = new TomasuloBranchFunctionalUnit();
            functionalUnits[7].name = "Branch0";
    }

    public void printFunctionalUnitContents(){

    }

    public void takeInstruction(TomasuloInstruction instruction){

    }

    public boolean allUnitsCompleted(){
        return false;
    }

    public TomasuloFunctionalUnit nextAvailableFunctionalUnit(String instructionType) {
        switch(instructionType){
            case "int":
                for (TomasuloFunctionalUnit fu : functionalUnits) {
                    if (fu.getClass().getName().equals("TomasuloIntegerFunctionalUnit")
                            && !fu.isFunctionalUnitBusy) {
                        return fu;
                    }
                }
            case "trap":
                for (TomasuloFunctionalUnit fu : functionalUnits) {
                    if (fu.getClass().getName().equals("TomasuloTrapFunctionalUnit")
                            && !fu.isFunctionalUnitBusy) {
                        return fu;
                    }
                }
            case "mem":
                for (TomasuloFunctionalUnit fu : functionalUnits) {
                    if (fu.getClass().getName().equals("TomasuloMemoryFunctionalUnit")
                            && !fu.isFunctionalUnitBusy) {
                        return fu;
                    }
                }
            case "float":
                for (TomasuloFunctionalUnit fu : functionalUnits) {
                    if (fu.getClass().getName().equals("TomasuloFloatingPointFunctionalUnit")
                            && !fu.isFunctionalUnitBusy) {
                        return fu;
                    }
                }
            case "branch":
                for (TomasuloFunctionalUnit fu : functionalUnits) {
                    if (fu.getClass().getName().equals("TomasuloBranchFunctionalUnit")
                            && !fu.isFunctionalUnitBusy) {
                        return fu;
                    }
                }
        }
        return null;
    }

    public void execute(TomasuloReservationStationManager rsManager){
        TomasuloFunctionalUnit functionalUnit = null;
        for(TomasuloReservationStation rs : rsManager.reservationStations){
            if(rs.isReadyForExecution()){
                //System.out.println(rs.name + " STATIONNNN READYYYYYYYYYY");
                rs.isExecuting = true;
                functionalUnit = this.nextAvailableFunctionalUnit(rs.type);
                if(functionalUnit == null) continue;
                rs.cycleCounter = functionalUnit.executionCycleCount;
                functionalUnit.isFunctionalUnitBusy = true;
                functionalUnit.referenceToReservationStation = rs.name;
                rs.cycleCounter--;
            }
            else if(rs.isExecuting && rs.cycleCounter > 0){
                rs.cycleCounter--;
            }
            else if(rs.isExecuting && rs.cycleCounter == 0){
                int result = 0;
                for(TomasuloFunctionalUnit fUnit : functionalUnits){
                    if(fUnit.referenceToReservationStation.equals(rs.name)){
                        result = this.computeResult(fUnit,rs.currentInstruction);
                        rs.result = result;
                        rs.resultReady = true;
                        fUnit.isFunctionalUnitBusy = false;
                        fUnit.referenceToReservationStation = null;
                    }
                }
            }
        }
    }

    public int computeResult(TomasuloFunctionalUnit fUnit, TomasuloInstruction instruction){
        return fUnit.computeResult(instruction);
    }

}
