import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by sina on 11/10/14.
 */
public class TomasuloReader {

    ArrayList<String[]> linesoup;

    public TomasuloReader(String inputFileName){
        linesoup = new  ArrayList<String[]>();
    }

    public void readFile() {
        File file = new File("intUnit1.hex");
        try {
            Scanner sc = new Scanner(file);
            System.out.println("================= Processed Raw Input ==================");
            while (sc.hasNextLine()) {
                String linestring = sc.nextLine();
                String[] splitline = linestring.split(": ");
                splitline[1] = splitline[1].substring(0,splitline[1].indexOf("#")).replace(" ","");
                System.out.println(splitline[0] + " " + splitline[1]);
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