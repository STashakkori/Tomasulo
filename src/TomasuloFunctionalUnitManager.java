import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * TomasuloFunctionalUnitManager :: Class that holds all 8 functional units and assignes them to reservation stations
 * during the execute stage.
 *
 * Created by sina on 11/30/14.
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
            functionalUnits[i].name = "int" + i;
        }

        // Initialize functional units :: trap units
            functionalUnits[3] = new TomasuloTrapFunctionalUnit();
            functionalUnits[3].name = "trap0";

        // Initialize functional units :: memory units
            functionalUnits[4] = new TomasuloMemoryFunctionalUnit();
            functionalUnits[4].name = "mem0";

        // Initialize functional units :: floating point units
        for(int i = 5; i < 7; i++){
            functionalUnits[i] = new TomasuloFloatingPointFunctionalUnit();
            functionalUnits[i].name = "float" + i % 5;
        }

        // Initialize functional units :: branch units
            functionalUnits[7] = new TomasuloBranchFunctionalUnit();
            functionalUnits[7].name = "branch0";
    }

    /**
     * nextAvailableFunctionalUnit :: Method that returns a functional unit of a certain
     * type that has its busy bit set to false meaning that it is not in use.
     *
     * @param instructionType
     * @return
     */
    public TomasuloFunctionalUnit nextAvailableFunctionalUnit(String instructionType) {
        switch(instructionType){
            case "int":
                for (TomasuloFunctionalUnit fu : functionalUnits) {
                    if (fu.getClass().getName().equals("TomasuloIntegerFunctionalUnit")
                            && !fu.isFunctionalUnitBusy) {
                        return fu;
                    }
                }
                return null;

            case "trap":
                for (TomasuloFunctionalUnit fu : functionalUnits) {
                    if (fu.getClass().getName().equals("TomasuloTrapFunctionalUnit")
                            && !fu.isFunctionalUnitBusy) {
                        return fu;
                    }
                }
                return null;

            case "mem":
                for (TomasuloFunctionalUnit fu : functionalUnits) {
                    if (fu.getClass().getName().equals("TomasuloMemoryFunctionalUnit")
                            && !fu.isFunctionalUnitBusy) {
                        return fu;
                    }
                }
                return null;

            case "float":
                for (TomasuloFunctionalUnit fu : functionalUnits) {
                    if (fu.getClass().getName().equals("TomasuloFloatingPointFunctionalUnit")
                            && !fu.isFunctionalUnitBusy) {
                        return fu;
                    }
                }
                return null;

            case "branch":
                for (TomasuloFunctionalUnit fu : functionalUnits) {
                    if (fu.getClass().getName().equals("TomasuloBranchFunctionalUnit")
                            && !fu.isFunctionalUnitBusy) {
                        return fu;
                    }
                }
                return null;
        }
        return null;
    }

    /**
     * execute :: method that assigns functional units to reervation stations and then calls computeResult.
     *
     * @param rsManager
     * @param rfManager
     * @param writer
     * @param memory
     */
    public void execute(TomasuloReservationStationManager rsManager,TomasuloRegisterFileManager rfManager, PrintWriter writer,TomasuloMemory memory){
        TomasuloFunctionalUnit functionalUnit = null;
        for(TomasuloReservationStation rs : rsManager.reservationStations){
            if(rs.busy == false) continue;
            functionalUnit = this.nextAvailableFunctionalUnit(rs.type);
            if(rs.isReadyForExecution() && functionalUnit != null){
                if(rs.type.equals("trap") && trapBuffer.peek() != rs){
                    if(trapBuffer.peek()!=null)
                    continue;
                }
                if(!trapBuffer.isEmpty() && rs.type.equals("trap"))  {
                    trapBuffer.remove();
                }
                if(rs.type.equals("mem") && loadstoreBuffer.peek() != rs){
                    if(loadstoreBuffer.peek()!=null)
                        continue;
                }
                if(!loadstoreBuffer.isEmpty() && rs.type.equals("mem"))  {
                    loadstoreBuffer.remove();
                }
                rs.isExecuting = true;
                rs.resultWritten = false;
                rs.resultReady = false;
                rs.setExecutingUnit(functionalUnit);
                rs.cycleCounter--;
                rs.executingUnit.isFunctionalUnitBusy = true;
                rs.executingUnit.referenceToReservationStation = rs.name;
            }
            else if(rs.isExecuting && rs.cycleCounter > 0){
                rs.cycleCounter--;
            }
            else if(rs.isExecuting && !rs.resultReady && rs.cycleCounter == 0){
                int result = 0;
                result = this.computeResult(rs,rfManager,writer,memory,trapBuffer,loadstoreBuffer);
                rs.result = result;
                rs.resultReady = true;
                rs.executingUnit.isFunctionalUnitBusy = false;
                rs.executingUnit.referenceToReservationStation = null;
                rs.isExecuting = false;
            }
        }
    }

    /**
     *
     * @param reservationStation
     * @param registerFileManager
     * @param writer
     * @param memory
     * @param trapBuffer
     * @param loadBuffer
     * @return
     */
    public int computeResult(TomasuloReservationStation reservationStation, TomasuloRegisterFileManager registerFileManager, PrintWriter writer, TomasuloMemory memory,
                             Queue<TomasuloReservationStation> trapBuffer,Queue<TomasuloReservationStation> loadBuffer){
        return reservationStation.executingUnit.computeResult(reservationStation,registerFileManager,writer,memory,trapBuffer,loadBuffer);
    }

    /**
     *
     */
    public void printFunctionalUnitContents(){
        int count = 1;
        for(TomasuloFunctionalUnit fu : this.functionalUnits){
            System.out.print("FUname:" + fu.name + " ");
            System.out.print("station:" + fu.referenceToReservationStation + "   ");
            if(count%4==0) System.out.println();
            count++;
        }
        System.out.println("..................................................................................");
    }

}
