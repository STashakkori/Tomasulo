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
}
