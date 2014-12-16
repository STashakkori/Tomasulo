import java.io.PrintWriter;
import java.util.Queue;

/**
 * Created by sina on 12/3/14.
 */
public class TomasuloIntegerFunctionalUnit extends TomasuloFunctionalUnit {

    TomasuloTools tools = new TomasuloTools();

    public int computeResult(TomasuloReservationStation rs, TomasuloRegisterFileManager rf, PrintWriter writer, TomasuloMemory memory,
                             Queue<TomasuloReservationStation> trapBuffer,Queue<TomasuloReservationStation> loadBuffer){
        if(rs.currentInstruction == null) return 0;
        switch(rs.currentInstruction.opcodeName){
            case "addi":
                int source= 0;
                if (rs.Vj >> 31 == 1){
                    System.out.println("two comp test");
                    source = tools.takeTwosCompliment(rs.Vj,32);
                    source += rs.currentInstruction.immediateValue;
                }
                else{
                    source = rs.Vj + rs.currentInstruction.immediateValue;
                }
                return source;
            case "nop":
                return 0;
            case "add":
                return (rs.Vj + rs.Vk);
            case "sub":
                return (rs.Vj - rs.Vk);
            case "and":
                return (rs.Vj & rs.Vk);
            case "or":
                return (rs.Vj | rs.Vk);
            case "xor":
                return (rs.Vj ^ rs.Vk);
            case "movf":
                return rs.Vj;
            case "movfp2i":
                return rs.Vj;
            case "movi2fp":
                return rs.Vj;
        }
        return 0;
    }
}
