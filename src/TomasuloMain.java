import java.math.BigInteger;
import java.util.ArrayList;

/**
 * TomasuloMain :: Class that identifies the inputs and passes them along to the TomasuloReader and TomasuloProcessor
 * classes accordingly. Checks for the -v input and signals the processor that it needs to produce verbose output.
 * Also takes in the commandline argument file names with .hex file extensions that are needed by the TomasuloReader
 * class.
 * Created by sina on 11/02/14.
 */
public class TomasuloMain {

    /**
     *  Main method for Tomasulo.
     * @param args
     */
    public static void main(String[] args){

        // Read the file and load the contents as bytes into memory.
        int memoryAddressPointer = 0;
        byte memoryDataEntry;
        boolean isVerboseMode = false;
        String fileName = args[0];

        // Loop through all command line arguments and look for flags -f and -v.
        // Pass file to the TomasuloReader.
        for(int i = 0; i < args.length; i++) {
            if(args[i].equals("-v"))
                isVerboseMode = true;

            if(args[i].equals("-f")){
                fileName = args[i+1];
            }
        }

        // The TomasuloReader class parses through the file and gets it into a usable
        // format
        TomasuloReader reader = new TomasuloReader(fileName);
        reader.readFile();

        // After reading the file we want to load all of the 4 byte words into memory.
        TomasuloMemory memory = TomasuloMemory.getInstance();
        for(String[] line : reader.linesoup){
            memoryAddressPointer = Integer.parseInt(line[0],16); // handle address components of hex file
            memoryDataEntry = 0;
            int j = 0;
            for (int i = 0; i < line[1].length() / 2; i++ ) {
                j = i * 2;
                String hex_pair = line[1].substring(j,j+2);
                byte b = (byte)(Integer.parseInt(hex_pair, 16) & 0xFF);
                memoryDataEntry = b;
                memory.insertDataByte(memoryAddressPointer+i,memoryDataEntry);
            }
        }

        // Initialize the cpu and run the processor.
        TomasuloCentralProcessingUnit cpu = TomasuloCentralProcessingUnit.createInstance();
        cpu.run(isVerboseMode);
    }

    /**
     *  reverseString :: take a string and reverse it. This is a helper method.
     * @param inputString
     * @return
     */
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