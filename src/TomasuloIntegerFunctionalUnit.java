import java.io.PrintWriter;

/**
 * Created by rt on 12/3/14.
 */
public class TomasuloIntegerFunctionalUnit extends TomasuloFunctionalUnit {

    public int computeResult(TomasuloReservationStation rs,PrintWriter writer){
        if(rs.currentInstruction == null) return 0;
        switch(rs.currentInstruction.opcodeName){
            case "addi":
                writer.println("The sum of " + rs.Vj + " and " + rs.currentInstruction.immediateValue + " is " + (rs.Vj + rs.currentInstruction.immediateValue + "."));
                writer.close();
                return (rs.Vj + rs.currentInstruction.immediateValue);
            case "nop":
                writer.println("nothing done");
                writer.close();
                return 0;
            case "add":
                writer.println(rs.Vj + " add " + rs.Vk + " is " + (rs.Vj + rs.Vk) + ".");
                writer.close();
                return (rs.Vj + rs.Vk);
            case "sub":
                writer.println(rs.Vj + " minus " + rs.Vk + " is " + (rs.Vj - rs.Vk));
                writer.close();
                return (rs.Vj - rs.Vk);
            case "and":
                writer.println(rs.Vj + " and " + rs.Vk + " is " + (rs.Vj & rs.Vk));
                writer.close();
                return (rs.Vj & rs.Vk);
            case "or":
                writer.println(rs.Vj + " or " + rs.Vk + " is " + (rs.Vj | rs.Vk));
                writer.close();
                return (rs.Vj | rs.Vk);
            case "xor":
                writer.println(rs.Vj + " xor " + rs.Vk + " is " + (rs.Vj ^ rs.Vk));
                writer.close();
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
