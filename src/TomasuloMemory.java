/**
 * Created by sina on 11/30/14.
 */
public class TomasuloMemory {

    static TomasuloMemory memoryInstance = null;
    byte[] data = new byte[2048];

    // Singleton design pattern without double checked locking
    static TomasuloMemory getInstance(){
        if (memoryInstance == null)
            memoryInstance = new TomasuloMemory();
        return memoryInstance;
    }

    /**
     *
     * @param address
     * @param dataItem
     */
    void insertDataByte(int address, byte dataItem){
       data[address] = dataItem;
    }

    /**
     *
     * @param address
     * @return
     */
    int[] fetchFourBytes(int address){
        int[] fourBytes = new int[4];
        for(int i = 0; i < 4; i++){
            fourBytes[i] = data[address + i];
        }
        return fourBytes;
    }

    /**
     *
     * @param address
     * @return
     */
    public int fetchWord(int address) {
        int result = 0;
        for (int i = 0; i < 4; i++) {
            result |= (data[address + i] & 0xFF);
            if (i < 3) result <<= 8;
        }
        return result;
    }

    /**
     *
     * @param address
     * @param word
     */
    public void storeWord(int address,int word) {
        int eightBitMask = 0xFF;
        int toByte = 0;
        toByte |= word;
        toByte &= eightBitMask;
        data[address+3] = (byte)(toByte);
        toByte |= word;
        toByte >>= 8;
        toByte &= eightBitMask;
        data[address+2] = (byte)(toByte);
        toByte |= word;
        toByte >>= 16;
        toByte &= eightBitMask;
        data[address+1] = (byte)(toByte);
        toByte |= word;
        toByte >>= 24;
        toByte &= eightBitMask;
        data[address] = (byte)(toByte);
    }

    /**
     *
     * @param address
     * @return
     */
    public String getDataString(int address){
        String dataString = "";
        for(int i = address; i < data.length; i++){
            if(data[i] == 0)
                break;
            dataString += (char)data[i];
        }
        return dataString;
    }

    /**
     *
     */
    void printDataContents(){
        int memAddress = 0;
        System.out.println("=================================== Memory Dump =====================================");
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
        System.out.println("\n======================================================================================");
    }
}
