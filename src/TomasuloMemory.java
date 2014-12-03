/**
 * Created by rt on 11/30/14.
 */
public class TomasuloMemory {

    static TomasuloMemory memoryInstance = null;
    byte[] data = new byte[4000];

    // Singleton design pattern without double checked locking
    static TomasuloMemory getInstance(){
        if (memoryInstance == null)
            memoryInstance = new TomasuloMemory();
        return memoryInstance;
    }

    void insertDataByte(int address, byte dataItem){
       data[address] = dataItem;
    }

    int[] fetchFourBytes(int address){
        if (address % 4 != 0){ // make sure data is aligned
            System.out.println("Error with PC input. Program ended.");
            System.exit(0);
        }
        int[] fourBytes = new int[4];
        for(int i = 0; i < 4; i++){
            fourBytes[i] = data[address + i];
        }
        return fourBytes;
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
