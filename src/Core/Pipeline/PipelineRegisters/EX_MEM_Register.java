package Core.Pipeline.PipelineRegisters;

import Core.Instructions.Instruction;

public class EX_MEM_Register implements PipelineRegister{
    private long branchTarget;
    private boolean zero;
    private int aluResult;
    private int readData2;
    private int writeRegister;
    private boolean staled;
    private Instruction instruction;

    //MEM
    private boolean isBranch;
    private boolean memRead;
    private boolean memWrite;

    //WB
    private int memToReg;
    private boolean regWrite;

    public EX_MEM_Register(Instruction instruction, long branchTarget, boolean zero, int aluResult, int readData2,
                           int writeRegister, boolean isBranch, boolean memRead,
                           boolean memWrite, int memToReg, boolean regWrite, boolean isStaled) {
        this.instruction =instruction;
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
        this.staled = isStaled;
    }

    public long getBranchTarget() {
        return branchTarget;
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

    public boolean isBranch() {
        return isBranch;
    }

    public boolean isMemRead() {
        return memRead;
    }

    public boolean isMemWrite() {
        return memWrite;
    }

    public int isMemToReg() {
        return memToReg;
    }

    public boolean isRegWrite() {
        return regWrite;
    }

    public boolean isStaled() {
        return staled;
    }

    public Instruction getInstruction() {
        return instruction;
    }

    public int getMemToReg() {
        return memToReg;
    }
}
