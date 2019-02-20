package Core.Pipeline.PipelineRegisters;

import Core.Instructions.Instruction;

public class MEM_WB_Register implements PipelineRegister {
    private int memoryData;
    private int aluResult;
    private int writeRegister;
    private boolean staled;
    private Instruction instruction;


    //WB
    private int memToReg;
    private boolean regWrite;

    public MEM_WB_Register(Instruction instruction,int memoryData, int aluResult, int writeRegister,
                           int memToReg, boolean regWrite, boolean isStaled) {
        this.instruction = instruction;
        this.memoryData = memoryData;
        this.aluResult = aluResult;
        this.writeRegister = writeRegister;
        this.memToReg = memToReg;
        this.regWrite = regWrite;
        this.staled = isStaled;
    }

    public int getMemoryData() {
        return memoryData;
    }

    public int getAluResult() {
        return aluResult;
    }

    public int getWriteRegister() {
        return writeRegister;
    }

    public int getMemToReg() {
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


}
