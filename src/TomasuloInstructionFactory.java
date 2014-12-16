/**
 * Created by sina on 12/2/14.
 */
public class TomasuloInstructionFactory {

    /**
     * 
     * @param newInstructionType
     * @return
     */
    public TomasuloInstruction makeInstruction(String newInstructionType) {

        TomasuloInstruction newInstruction = null;

        switch (newInstructionType) {
            case "int":
                return new TomasuloIntegerInstruction();
            case "mem":
                return new TomasuloMemoryInstruction();
            case "float":
                return new TomasuloFloatingPointInstruction();
            case "branch":
                return new TomasuloBranchInstruction();
            case "trap":
                return new TomasuloTrapInstruction();
            case "error":
                return null;
        };
    return null;
    }
}
