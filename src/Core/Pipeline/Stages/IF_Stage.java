package Core.Pipeline.Stages;

import Core.Assembler.Assembler;
import Core.Instructions.Instruction;
import Core.Pipeline.Pipeline;
import Core.Pipeline.PipelineRegisters.IF_ID_Register;
import Core.Pipeline.PipelineRegisters.PipelineRegister;

public class IF_Stage extends Stage {
    private long pc_register;
    private boolean pcSrc; // 0 : PC+4 --- 1 : branch target


    public IF_Stage(Assembler assembler, Instruction instruction, long branchTarget,
                    boolean pcSrc, boolean stalled, Pipeline pipeline) {
        super(assembler, instruction);
        this.pcSrc = pcSrc;
        if (!pcSrc)
            this.pc_register = getAssembler().getPc_register();
        else
            this.pc_register = branchTarget;
        pipeline.setPc(this.pc_register);
//        if (!stalled) {
            for (Instruction ins : assembler.getInstructions()) {
                if (ins.getAddress() == this.pc_register) {
                    setInstruction(ins);
                    break;
                }
            }
            if (!stalled)
                getAssembler().setPc_register(this.pc_register + 4);
//        }else{
//            setInstruction(new nop(assembler, -1, -1));
//        }
        setStaled(stalled);
        /*if (!stalled) {
            if (pcSrc)
                getAssembler().setPc_register(branchTarget);
            else
                getAssembler().setPc_register(getAssembler().getPc_register() + 4);
        }*/
    }

    public long getPc_register() {
        return pc_register;
    }

    public void setPc_register(long pc_register) {
        this.pc_register = pc_register;
    }

    public boolean getPcSrc() {
        return pcSrc;
    }

    public void setPcSrc(boolean pcSrc) {
        this.pcSrc = pcSrc;
    }

    @Override
    public PipelineRegister runStage() {
        long nextPc = getAssembler().getPc_register(); // ALU
        String binaryInstruction = getInstruction().getBinaryCode();
        IF_ID_Register if_id = new IF_ID_Register(nextPc, binaryInstruction,
                getInstruction(), isStaled());
        return if_id;
    }
}
