import java.math.BigInteger;
import java.util.ArrayList;

/**
 * Created by sina on 11/10/14.
 */
public class TomasuloMain {

    public static void main(String[] args){

        // Read the file and load the contents as bytes into memory.
        int memoryAddressPointer = 0;
        byte memoryDataEntry;
        TomasuloReader reader = new TomasuloReader("test");
        reader.readFile();
        TomasuloMemory memory = TomasuloMemory.getInstance();
        System.out.println("================== file lines ===================");
        for(String[] line : reader.linesoup){
            System.out.println(line[0] + " " + line[1]);
            memoryAddressPointer = Integer.parseInt(line[0],16); // handle address components of hex file
            memoryDataEntry = 0;
            int j = 0;
            for (int i = 0; i < line[1].length() / 2; i++ ) {
                j = i * 2;
                String hex_pair = line[1].substring(j,j+2);
                System.out.println("test1 " + hex_pair);
                byte b = (byte)(Integer.parseInt(hex_pair, 16) & 0xFF);
                memoryDataEntry = b;
                memory.insertDataByte(memoryAddressPointer+i,memoryDataEntry);
                System.out.println("memory data: " + memoryDataEntry);
            }
            System.out.println("memoryDataEntry " + memoryDataEntry);
            System.out.println("============================================");
        }
        System.out.println("============================================");
        memory.printDataContents();

        // Initialize the cpu and run the processor.
        TomasuloCentralProcessingUnit cpu = TomasuloCentralProcessingUnit.createInstance();
        cpu.run();
    }

    public static String reverseString(String inputString){
        String reversedString = "";
        if(inputString.length() == 1){
            return inputString;
        } else {
            reversedString += inputString.charAt(inputString.length()-1)
                + reverseString(inputString.substring(0,inputString.length()-1));
            return reversedString;
        }
    }
}