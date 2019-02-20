package Core.Pipeline.Stages;

import Core.Assembler.Assembler;
import Core.Instructions.Instruction;
import Core.Pipeline.PipelineRegisters.PipelineRegister;

public class WB_Stage extends Stage{
    private int memoryData;
    private int aluResult;
    private int writeRegister;

    //WB
    private int memToReg;
    private boolean regWrite;


    public WB_Stage(Assembler assembler, Instruction instruction, int memoryData,
                    int aluResult, int writeRegister, int memToReg, boolean regWrite,
                    boolean isStaled) {
        super(assembler, instruction);
        this.memoryData = memoryData;
        this.aluResult = aluResult;
        this.writeRegister = writeRegister;
        this.memToReg = memToReg;
        this.regWrite = regWrite;
        setStaled(isStaled);
    }

    public boolean isRegWrite() {
        return regWrite;
    }

    @Override
    public PipelineRegister runStage() {
        if (!isStaled()) {
            if (memToReg == 1 && regWrite) {
                if (writeRegister != 0)
                getAssembler().getRegisters()[writeRegister] = memoryData;
            } else if (memToReg == 0 && regWrite) {
                if (writeRegister != 0)
                getAssembler().getRegisters()[writeRegister] = aluResult;
            }
        }
        return null; // has not next pipeline register
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


}
