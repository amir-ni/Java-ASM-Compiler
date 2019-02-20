package Core.Pipeline.Stages;

import Core.Assembler.Assembler;
import Core.Instructions.Instruction;
import Core.Pipeline.PipelineRegisters.PipelineRegister;

public abstract class Stage {
    private Assembler assembler;
    private Instruction instruction;
    private boolean staled;

    public Stage(Assembler assembler, Instruction instruction) {
        this.assembler = assembler;
        this.instruction = instruction;
    }

    public Assembler getAssembler() {
        return assembler;
    }

    public Instruction getInstruction() {
        return instruction;
    }

    public void setInstruction(Instruction instruction) {
        this.instruction = instruction;
    }

    public boolean isStaled() {
        return staled;
    }

    public void setStaled(boolean staled) {
        this.staled = staled;
    }

    public abstract PipelineRegister runStage();
}
