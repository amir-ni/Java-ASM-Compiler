package Core.Pipeline.Stages;

import Core.Assembler.Assembler;
import Core.Instructions.Instruction;
import Core.Pipeline.PipelineRegisters.MEM_WB_Register;
import Core.Pipeline.PipelineRegisters.PipelineRegister;

public class MEM_Stage extends Stage{
    private long branchTarget;
    private boolean zero;
    private int aluResult;
    private int readData2;
    private int writeRegister;
    private boolean pcSrc;
    private int memoryData;

    //MEM
    private boolean isBranch;
    private boolean memRead;
    private boolean memWrite;

    //WB
    private int memToReg;
    private boolean regWrite;

    public MEM_Stage(Assembler assembler, Instruction instruction, long branchTarget,
                     boolean zero, int aluResult, int readData2, int writeRegister,
                     boolean isBranch, boolean memRead, boolean memWrite, int memToReg,
                     boolean regWrite, boolean isStaled) {
        super(assembler, instruction);
        this.branchTarget = branchTarget;
        this.zero = zero;
        this.aluResult = aluResult;
        this.readData2 = readData2;
        this.writeRegister = writeRegister;
        this.isBranch = isBranch;
        this.memRead = memRead;
        this.memWrite = memWrite;
        this.memToReg = memToReg;
        this.regWrite = regWrite;
        setStaled(isStaled);

        if (!isStaled && isBranch && zero)
            pcSrc = true;

    }

    public boolean isPcSrc() {
        return pcSrc;
    }

    public long getBranchTarget() {
        return branchTarget;
    }

    @Override
    public PipelineRegister runStage() {
        if (!isStaled()) {
            if (memRead)
                memoryData = getAssembler().loadMemory(aluResult);
            else if (memWrite)
                getAssembler().saveMemory(aluResult, readData2);
        }
        MEM_WB_Register mem_wb = new MEM_WB_Register(getInstruction(),memoryData,aluResult,writeRegister,
                memToReg,regWrite, isStaled());
        return mem_wb;
    }

    public boolean isZero() {
        return zero;
    }

    public int getAluResult() {
        return aluResult;
    }

    public int getReadData2() {
        return readData2;
    }

    public int getWriteRegister() {
        return writeRegister;
    }

    public int getMemoryData() {
        return memoryData;
    }

    public boolean isBranch() {
        return isBranch;
    }

    public boolean isMemRead() {
        return memRead;
    }

    public boolean isMemWrite() {
        return memWrite;
    }

    public int getMemToReg() {
        return memToReg;
    }

    public boolean isRegWrite() {
        return regWrite;
    }
}
