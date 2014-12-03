/**
 * Created by rt on 12/2/14.
 */
public class TomasuloInstructionFactory {

    public TomasuloInstruction makeInstruction(String newInstructionType) {

        TomasuloInstruction newInstruction = null;

        switch (newInstructionType) {
            case "int":
                return new TomasuloIntegerInstruction();
            case "mem":
                return new TomasuloMemoryInstruction();
            case "fp":
                return new TomasuloFloatingPointInstruction();
            case "branch":
                return new TomasuloBranchInstruction();
            case "trap":
                return new TomasuloTrapInstruction();
        };
    return null;
    }
}
