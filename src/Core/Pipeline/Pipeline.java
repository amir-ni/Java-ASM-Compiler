package Core.Pipeline;

import Core.Assembler.Assembler;
import Core.Instructions.Instruction;
import Core.Instructions.beq;
import Core.Instructions.nop;
import Core.Pipeline.PipelineRegisters.*;
import Core.Pipeline.Stages.*;

import java.util.List;

public class Pipeline {
    Instruction[] stagesInstruction;
    private int clock;
    private Stage[] stages;
    private PipelineRegister[] pipelineRegisters;
    private int[] registers;
    private Assembler assembler;
    private Pipeline previousPipeline;
    private long pc;

    private boolean stalled_lw = false;
    /*private boolean stalled_beq_IF_Stage;*/
    /*private long correctPcAddress ;*/
    /*private boolean stalled_beq_IF_and_ID_Satge;*/
    private boolean stalled_beq;


    public Pipeline(Assembler assembler, Instruction[] stagesInstruction,
                    Pipeline previousPipeline, int clock) {
        this.stagesInstruction = stagesInstruction;
        this.clock = clock;
        this.assembler = assembler;
        this.previousPipeline = previousPipeline;
        this.stages = new Stage[5];
        this.pipelineRegisters = new PipelineRegister[4];
        registers = new int[32];


        if (previousPipeline != null &&
                (previousPipeline.stagesInstruction[2] instanceof beq ||
                        previousPipeline.stagesInstruction[3] instanceof beq)) {
            setStalled_beq(true);
        }


        // Stage WB (First Half of Clock)
        if (stagesInstruction[4] != null) {
            stages[4] = buildWB();
            if (stages[4] != null)
                stages[4].runStage();
        }

        // Stage ID
        if (stagesInstruction[1] != null) {
            IF_ID_Register if_id = (IF_ID_Register) previousPipeline.getPipelineRegisters()[0];
            if (if_id != null) {
                if (!if_id.isStaled()) {
                    long next_PC = if_id.getNext_PC();
                    stages[1] = new ID_Stage(assembler, if_id.getInstruction(), next_PC, this);
                    pipelineRegisters[1] = stages[1].runStage();
                    ((ID_Stage) stages[1]).hazardDetectionUnit(this);
                } else {
                    stages[1] = new ID_Stage(assembler, new nop(assembler, -1, -1, if_id.getInstruction()), 0, this);
                    stages[1].setStaled(true);
                    pipelineRegisters[1] = stages[1].runStage();
                }
            }
        }

        // Stage EX
        if (stagesInstruction[2] != null) {
            stages[2] = buildEX();
            if (stages[2] != null)
                pipelineRegisters[2] = stages[2].runStage();
        }

        // Stage IF ( In Second Half Read The Instruction Memory)
        if (stagesInstruction[0] != null) {
            boolean pcSrc = false;
            long branchTarget = 0;
            if (previousPipeline.stages[3] != null) {
                MEM_Stage mem = (MEM_Stage) previousPipeline.stages[3];
                pcSrc = mem.isPcSrc();
                branchTarget = mem.getBranchTarget();
            }
            /*
            ID_Stage id_stage = (ID_Stage) stages[1];
            if (id_stage != null) {
                pcSrc = id_stage.isPcSrc();
                branchTarget = id_stage.getBranchTarget();
            }
            */
            boolean stalled = false;
            /*if (stalled_beq_IF_Stage || stalled_lw)
                stalled = true;
                */
            if (stalled_lw || stalled_beq)
                stalled = true;
            stages[0] = new IF_Stage(assembler, null, branchTarget,
                    pcSrc, stalled, this);
            pipelineRegisters[0] = stages[0].runStage();
        }

        /*
        // HazardDetectionUnit for Beq
        if (stalled_beq_IF_Stage)
            assembler.setPc_register(correctPcAddress);

        // Stage EX
        if (stagesInstruction[2] != null) {
            stages[2] = buildEX();
            if (stages[2] != null)
                pipelineRegisters[2] = stages[2].runStage();
        }
        */
        // Stage MEM
        if (stagesInstruction[3] != null) {
            stages[3] = buildMEM();
            if (stages[3] != null)
                pipelineRegisters[3] = stages[3].runStage();
        }


        // Set Registers
        for (int i = 0; i < 32; i++) {
            this.registers[i] = assembler.getRegisters()[i];
        }

    }

    public int getClock() {
        return clock;
    }

    public PipelineRegister[] getPipelineRegisters() {
        return pipelineRegisters;
    }

    public Stage[] getStages() {
        return stages;
    }

    public int[] getRegisters() {
        return registers;
    }

    public Instruction[] getStagesInstruction() {
        return stagesInstruction;
    }

    private EX_Stage buildEX() {
        EX_Stage ex_stage = null;
        ID_EX_Register id_ex = (ID_EX_Register) previousPipeline.getPipelineRegisters()[1];
        if (id_ex != null) {
            long next_PC = id_ex.getNext_PC();
            int readData1 = id_ex.getRegisterData1();
            int readData2 = id_ex.getRegisterData2();
            String offset = id_ex.getOffset_32bit_binary();
            String rt = id_ex.getRt_binary();
            String rd = id_ex.getRd_binary();
            int regDst = id_ex.getRegDst();
            String aluOp = id_ex.getALUOp();
            int aluSrc = id_ex.getALUSrc();
            boolean isBranch = id_ex.isBranch();
            boolean memRead = id_ex.isMemRead();
            boolean memWrite = id_ex.isMemWrite();
            int memToReg = id_ex.isMemToReg();
            boolean regWrite = id_ex.isRegWrite();
            int registerNumber1 = id_ex.getRegisterNumber1();
            int registerNumber2 = id_ex.getRegisterNumber2();
            boolean staled = id_ex.isStaled();

            ex_stage = new EX_Stage(assembler, id_ex.getInstruction(), previousPipeline, next_PC, readData1, readData2, offset, rt,
                    rd, regDst, aluOp, aluSrc, isBranch, memRead, memWrite, memToReg, regWrite,
                    registerNumber1, registerNumber2, staled);
        }
        return ex_stage;
    }

    private MEM_Stage buildMEM() {
        MEM_Stage mem_stage = null;
        EX_MEM_Register ex_mem = (EX_MEM_Register) previousPipeline.getPipelineRegisters()[2];
        if (ex_mem != null) {
            long branchTarget = ex_mem.getBranchTarget();
            boolean zero = ex_mem.isZero();
            int aluResult = ex_mem.getAluResult();
            int readData2 = ex_mem.getReadData2();
            int writeRegister = ex_mem.getWriteRegister();
            boolean isBranch = ex_mem.isBranch();
            boolean memRead = ex_mem.isMemRead();
            boolean memWrite = ex_mem.isMemWrite();
            int memToReg = ex_mem.isMemToReg();
            boolean regWrite = ex_mem.isRegWrite();
            boolean staled = ex_mem.isStaled();

            mem_stage = new MEM_Stage(assembler, ex_mem.getInstruction(), branchTarget,
                    zero, aluResult, readData2, writeRegister, isBranch, memRead, memWrite,
                    memToReg, regWrite, staled);
        }
        return mem_stage;
    }

    private WB_Stage buildWB() {
        WB_Stage wb_stage = null;
        MEM_WB_Register mem_wb = (MEM_WB_Register) previousPipeline.getPipelineRegisters()[3];
        if (mem_wb != null) {
            int memoryData = mem_wb.getMemoryData();
            int aluResult = mem_wb.getAluResult();
            int writeRegister = mem_wb.getWriteRegister();
            int memToReg = mem_wb.getMemToReg();
            boolean regWrite = mem_wb.isRegWrite();
            boolean staled = mem_wb.isStaled();


            wb_stage = new WB_Stage(assembler, mem_wb.getInstruction(), memoryData,
                    aluResult, writeRegister, memToReg, regWrite, staled);
        }
        return wb_stage;
    }

    public Pipeline buildNextPipeline(List<Instruction> instructions) {
        Instruction[] last5Instructions = {null, null, null, null, null};//TODO remove this
        if (previousPipeline != null)
            last5Instructions = getStagesInstruction();
        for (int j = 4; j > 0; j--) {
            last5Instructions[j] = last5Instructions[j - 1];
        }
        last5Instructions[0] = null;
        // Hazard Detection Unit (lw)
        if (stalled_lw) {
            Instruction mainInstruction = last5Instructions[1];
            last5Instructions[1] = new nop(assembler, -1, -1, mainInstruction);
//            assembler.setPc_register(assembler.getPc_register() - 4);
        }

        long nextPC = assembler.getPc_register();
        Instruction nextInstruction = null;
        for (Instruction ins : instructions) {
            if (ins.getAddress() == nextPC) {
                nextInstruction = ins;
                break;
            }
        }
        last5Instructions[0] = nextInstruction;


        Pipeline nextPipeline = new Pipeline(assembler, last5Instructions, this, clock + 1);
        return nextPipeline;
    }

    public void setStalled_lw(boolean stalled_lw) {
        this.stalled_lw = stalled_lw;
    }

    /*public boolean isStalled_beq_IF_Stage() {
        return stalled_beq_IF_Stage;
    }

    public void setStalled_beq_IF_Stage(boolean stalled_beq_IF_Stage) {
        this.stalled_beq_IF_Stage = stalled_beq_IF_Stage;
    }

    public void setCorrectPcAddress(long correctPcAddress) {
        this.correctPcAddress = correctPcAddress;
    }
    */

    public boolean isStalled_beq() {
        return stalled_beq;
    }

    public void setStalled_beq(boolean stalled_beq) {
        this.stalled_beq = stalled_beq;
    }

    public long getPc() {
        return pc;
    }

    public void setPc(long pc) {
        this.pc = pc;
    }

    public boolean isStalled_lw() {
        return stalled_lw;
    }
}
