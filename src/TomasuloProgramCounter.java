/**
 * Created by sina on 11/29/14.
 */
public class TomasuloProgramCounter {

    int address;
    static TomasuloProgramCounter pcInstance = null;

    private TomasuloProgramCounter(){
        address = 0;
    }

    // Singleton design pattern with double checked locking
    static TomasuloProgramCounter getInstance(){
        if (pcInstance == null)
            pcInstance = new TomasuloProgramCounter();
        return pcInstance;
    }

    // Incrementor method --> Go to next clock cycle
    void increment(){
        address += 4;
    }

    // Getter method for the current cycle
    int getCurrentCycle(){
        return address;
    }

    // Getter method for the current cycle
    void setCurrentCycle(int address){
        this.address = address;
    }
}
