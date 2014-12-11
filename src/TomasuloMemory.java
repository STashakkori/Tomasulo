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

    public int fetchWord(int address) {
        if (address % 4 != 0){ // make sure data is aligned
            System.out.println("Error with PC input. Program ended.");
            System.exit(0);
        }
        int result = 0;
        for (int i = 0; i < 4; i++) {
            result |= (data[address + i] & 0xFF);
            if (i < 3) result <<= 8;
        }
        return result;
    }

    //result = ( result << 8 ) - Byte.MIN_VALUE + (int) bytes[i];

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
