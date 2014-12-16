import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by sina on 11/10/14.
 */
public class TomasuloReader {

    ArrayList<String[]> linesoup;
    String inputFileName;

    public TomasuloReader(String inputFileName){
        linesoup = new  ArrayList<String[]>();
        this.inputFileName = inputFileName;
    }

    public void readFile() {
        File file = new File(inputFileName);
        try {
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                String linestring = sc.nextLine();
                String[] splitline = linestring.split(": ");
                splitline[1] = splitline[1].substring(0,splitline[1].indexOf("#")).replace(" ","");
                linesoup.add(splitline);
            }
            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String[]> getLineSoup(){
        return linesoup;
    }
}