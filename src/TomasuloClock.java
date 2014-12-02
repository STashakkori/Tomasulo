/**
 * Clock class : Class that acts as the cycle clock for the Tomasulo processor simulator.
 * Created by sina on 11/10/14.
 */
public class TomasuloClock {

    int cycle;  // The current cycle
    static TomasuloClock clockInstance = null;

    private TomasuloClock(){
        cycle = 0;
    }

    // Singleton design pattern without double checked locking
    static TomasuloClock createInstance(){
        if (clockInstance == null)
            clockInstance = new TomasuloClock();
        return clockInstance;
    }

    // Incrementor method --> Go to next clock cycle
    void increment(){
        cycle++;
    }

    // Getter method for the current cycle
    int getCurrentCycle(){
        return cycle;
    }
}