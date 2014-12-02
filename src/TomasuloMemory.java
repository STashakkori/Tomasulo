/**
 * Created by rt on 11/30/14.
 */
public class TomasuloMemory {

    static TomasuloMemory memoryInstance = null;
    byte[] data = new byte[4000];
    int memorySize;

    // Singleton design pattern without double checked locking
    static TomasuloMemory createInstance(){
        if (memoryInstance == null)
            memoryInstance = new TomasuloMemory();
        return memoryInstance;
    }

    void addLineOfData(int address, byte dataItem){
        data[address] = dataItem;
    }

    void printDataContents(){
        int memAddress = 0;
        System.out.println("================= Memory Dump ==================");
        System.out.print("0000:  ");
        for(int i = 0 ; i < data.length; i++){
            if((i+1) % 32 == 0 && i != 0){
                memAddress+=32;
                System.out.println(String.format("%02X", data[i]));
                System.out.print(String.format("%04X",memAddress) + ":  ");
            }
            else if((i+1) % 4 == 0) System.out.print(String.format("%02X", data[i]) + " ");
            else System.out.print(String.format("%02X", data[i]));
        }
    }
}
