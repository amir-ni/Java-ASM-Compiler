package Core.Pipeline.PipelineRegisters;

import Core.Instructions.Instruction;

public class IF_ID_Register implements PipelineRegister{
    private long next_PC; // PC + 4
    private String instructionText; // instrucion in Binary Form
    private Instruction instruction;
    private boolean staled;

    public IF_ID_Register(long next_PC, String instructionText, Instruction instruction, boolean staled) {
        this.next_PC = next_PC;
        this.instructionText = instructionText;
        this.instruction = instruction;
        this.staled = staled;
    }

    public Instruction getInstruction() {
        return instruction;
    }

    public long getNext_PC() {
        return next_PC;
    }

    public String getInstructionText() {
        return instructionText;
    }

    public boolean isStaled() {
        return staled;
    }

    public void setStaled(boolean staled) {
        this.staled = staled;
    }
}
