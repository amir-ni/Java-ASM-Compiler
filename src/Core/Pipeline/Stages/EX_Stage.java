package Core.Pipeline.Stages;

import Core.Assembler.Assembler;
import Core.Instructions.*;
import Core.Pipeline.Pipeline;
import Core.Pipeline.PipelineRegisters.EX_MEM_Register;
import Core.Pipeline.PipelineRegisters.MEM_WB_Register;
import Core.Pipeline.PipelineRegisters.PipelineRegister;
import Core.util.Binary;

public class EX_Stage extends Stage {
    private long next_PC; // PC + 4
    private int inputALU1;
    private int inputALU2;
    private int registerNumber1;
    private int registerNumber2;
    private int readData1;
    private int readData2;
    private String offset_32bit_binary;
    private String funct;
    private String rt_binary; // Instruction[20-16]
    private String rd_binary; // Instructio [15-11]
    private int writeRegister;
    private long branchTarget;
    private boolean zero;
    private int aluResult;
    private int forwardA;
    private int forwardB;

    //EX
    private int regDst;
    private String ALUOp;
    private int ALUSrc;

    //MEM
    private boolean isBranch;
    private boolean memRead;
    private boolean memWrite;

    //WB
    private int memToReg;
    private boolean regWrite;

    public EX_Stage(Assembler assembler, Instruction instruction, Pipeline previousPipeline,
                    long next_PC, int readData1, int readData2, String offset_32bit_binary,
                    String rt_binary, String rd_binary, int regDst, String ALUOp, int ALUSrc,
                    boolean isBranch, boolean memRead, boolean memWrite, int memToReg,
                    boolean regWrite, int registerNumber1, int registerNumber2, boolean isStaled) {
        super(assembler, instruction);
        this.next_PC = next_PC;
        this.readData1 = readData1;
        this.readData2 = readData2;
        this.offset_32bit_binary = offset_32bit_binary;
        this.rt_binary = rt_binary;
        this.rd_binary = rd_binary;
        this.regDst = regDst;
        this.ALUOp = ALUOp;
        this.ALUSrc = ALUSrc;
        this.isBranch = isBranch;
        this.memRead = memRead;
        this.memWrite = memWrite;
        this.memToReg = memToReg;
        this.regWrite = regWrite;
        this.registerNumber1 = registerNumber1;
        this.registerNumber2 = registerNumber2;
        setStaled(isStaled);

        if (!isStaled) {
            // Forwarding Unit
            if (previousPipeline.getPipelineRegisters()[2] != null) {
                boolean setAlu1 = false;
                boolean setAlu2 = false;
                // MEM_WB check
                if (previousPipeline.getPipelineRegisters()[3] != null) {
                    MEM_WB_Register mem_wb = (MEM_WB_Register) previousPipeline.getPipelineRegisters()[3];
                    if (mem_wb.isRegWrite() & mem_wb.getWriteRegister() != 0 &
                            mem_wb.getWriteRegister() == registerNumber1) {
                        forwardA = 1;//01
                        if (mem_wb.getMemToReg() == 0)
                            inputALU1 = mem_wb.getAluResult();
                        else
                            inputALU1 = mem_wb.getMemoryData();
                        setAlu1 = true;
                    } else {
                        inputALU1 = readData1;
                    }
                    if (mem_wb.isRegWrite() & mem_wb.getWriteRegister() != 0 &
                            mem_wb.getWriteRegister() == registerNumber2) {
                        forwardB = 1; //01
                        if (this.ALUSrc == 0) {
                            if (mem_wb.getMemToReg() == 0)
                                inputALU2 = mem_wb.getAluResult();
                            else
                                inputALU2 = mem_wb.getMemoryData();
                            setAlu2 = true;
                        } else {
                            inputALU2 = Binary.binaryStringToInt(offset_32bit_binary);
                        }
                    } else {
                        if (this.ALUSrc == 0)
                            inputALU2 = readData2;
                        else
                            inputALU2 = Binary.binaryStringToInt(offset_32bit_binary);
                    }
                }

                // EX_MEM check
                EX_MEM_Register ex_mem = (EX_MEM_Register) previousPipeline.getPipelineRegisters()[2];
                if (ex_mem.isRegWrite() & ex_mem.getWriteRegister() != 0 &
                        ex_mem.getWriteRegister() == registerNumber1) {
                    forwardA = 2; // 10
                    inputALU1 = ex_mem.getAluResult();
                } else if (!setAlu1) {
                    inputALU1 = readData1;
                }
                if (ex_mem.isRegWrite() & ex_mem.getWriteRegister() != 0 &
                        ex_mem.getWriteRegister() == registerNumber2) {
                    forwardB = 2; // 10
                    if (this.ALUSrc == 0)
                        inputALU2 = ex_mem.getAluResult();
                    else
                        inputALU2 = Binary.binaryStringToInt(offset_32bit_binary);
                } else if (!setAlu2) {
                    if (this.ALUSrc == 0)
                        inputALU2 = readData2;
                    else
                        inputALU2 = Binary.binaryStringToInt(offset_32bit_binary);
                }
            } else if (previousPipeline.getPipelineRegisters()[3] != null) {
                MEM_WB_Register mem_wb = (MEM_WB_Register) previousPipeline.getPipelineRegisters()[3];
                if (mem_wb.isRegWrite() & mem_wb.getWriteRegister() != 0 &
                        mem_wb.getWriteRegister() == registerNumber1) {
                    forwardA = 1; // 01
                    if (mem_wb.getMemToReg() == 0)
                        inputALU1 = mem_wb.getAluResult();
                    else
                        inputALU1 = mem_wb.getMemoryData();
                } else {
                    inputALU1 = readData1;
                }
                if (mem_wb.isRegWrite() & mem_wb.getWriteRegister() != 0 &
                        mem_wb.getWriteRegister() == registerNumber2) {
                    forwardB = 1; // 01
                    if (this.ALUSrc == 0)
                        if (mem_wb.getMemToReg() == 0)
                            inputALU2 = mem_wb.getAluResult();
                        else
                            inputALU2 = mem_wb.getMemoryData();
                    else
                        inputALU2 = Binary.binaryStringToInt(offset_32bit_binary);
                } else {
                    if (this.ALUSrc == 0)
                        inputALU2 = readData2;
                    else
                        inputALU2 = Binary.binaryStringToInt(offset_32bit_binary);
                }
            } else {
                inputALU1 = readData1;
                if (this.ALUSrc == 0)
                    inputALU2 = readData2;
                else
                    inputALU2 = Binary.binaryStringToInt(offset_32bit_binary);
            }
            funct = offset_32bit_binary.substring(26, 32);
            if (this.regDst == 0)
                writeRegister = Binary.binaryStringToInt(rt_binary);
            else if(this.regDst == 1)
                writeRegister = Binary.binaryStringToInt(rd_binary);
            else
                writeRegister = -1;
        }
    }

    @Override
    public PipelineRegister runStage() {
        if (!isStaled()) {
            calculate_aluResult();
            calculateBranchTarget();
            calculateZero();
        }
        EX_MEM_Register ex_mem = new EX_MEM_Register(getInstruction(),branchTarget, zero, aluResult, readData2,
                writeRegister, isBranch, memRead, memWrite, memToReg, regWrite, isStaled());
        return ex_mem;
    }

    private void calculateBranchTarget() {
        long offset = Binary.binaryStringToInt(offset_32bit_binary) * 4;
        this.branchTarget = offset + next_PC;
    }

    private void calculateZero() {
        if (getInstruction() instanceof beq && inputALU1 == inputALU2) {
            zero = true;
        }
    }

    private void calculate_aluResult() {
        if (getInstruction() instanceof add) {
            this.aluResult = inputALU1 + inputALU2;
        } else if (getInstruction() instanceof sub) {
            this.aluResult = inputALU1 - inputALU2;
        } else if (getInstruction() instanceof and) {
            this.aluResult = inputALU1 & inputALU2;
        } else if (getInstruction() instanceof or) {
            this.aluResult = inputALU1 | inputALU2;
        } else if (getInstruction() instanceof nor) {
            this.aluResult = ~(inputALU1 | inputALU2);
        } else if (getInstruction() instanceof slt) {
            if (inputALU1 < inputALU2)
                this.aluResult = 1;
            else
                this.aluResult = 0;
        } else if (getInstruction() instanceof beq) {
            this.aluResult = inputALU1 - inputALU2;
        } else if (getInstruction() instanceof lw) {
            this.aluResult = inputALU1 + inputALU2;
        } else if (getInstruction() instanceof sw) {
            this.aluResult = inputALU1 + inputALU2;
        }
    }

    public long getNext_PC() {
        return next_PC;
    }

    public int getInputALU1() {
        return inputALU1;
    }

    public int getInputALU2() {
        return inputALU2;
    }

    public int getRegisterNumber1() {
        return registerNumber1;
    }

    public int getRegisterNumber2() {
        return registerNumber2;
    }

    public int getReadData1() {
        return readData1;
    }

    public int getReadData2() {
        return readData2;
    }

    public String getOffset_32bit_binary() {
        return offset_32bit_binary;
    }

    public String getFunct() {
        return funct;
    }

    public String getRt_binary() {
        return rt_binary;
    }

    public String getRd_binary() {
        return rd_binary;
    }

    public int getWriteRegister() {
        return writeRegister;
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

    public int getForwardA() {
        return forwardA;
    }

    public int getForwardB() {
        return forwardB;
    }

    public int getRegDst() {
        return regDst;
    }

    public String getALUOp() {
        return ALUOp;
    }

    public int getALUSrc() {
        return ALUSrc;
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

    /*
    public void hazardDetectionUnit(Core.Pipeline pipeline) {
        // Hazard Detection Unit (beq)
        if (getStringInstruction() instanceof beq) {
            if (pipeline.getPipelineRegisters()[1] != null) {
                ID_EX_Register id_ex = (ID_EX_Register) pipeline.getPipelineRegisters()[1];

                // if forwarding
                // stall 1 or 2


                // if no forwarding
                boolean branchEquality = inputALU1 == inputALU2;
                boolean branchPredicted = getAssembler().getPredictionMap().
                        get(getStringInstruction().getBinaryCode()).isTaken();
                if (branchEquality != branchPredicted) { // mistake in prediction
                    getAssembler().getPredictionMap().get(getStringInstruction().getBinaryCode()).
                            setWrongPredictions();
                    long corrctNext_PC = 0;
                    if (branchEquality) {
                        long branchTarget = Binary.binaryStringToInt(offset_32bit_binary) * 4;
                        corrctNext_PC = getStringInstruction().getAddress() + 4 + branchTarget;
                    } else {
                        corrctNext_PC = getStringInstruction().getAddress() + 4;
                    }

                    pipeline.setStalled_beq_IF_Stage(true);
                    pipeline.setStalled_beq(true);

                    pipeline.setCorrectPcAddress(corrctNext_PC);
                    getAssembler().getPredictionMap().
                            get(getStringInstruction().getBinaryCode()).setTaken(branchEquality);

                } else {

                    getAssembler().getPredictionMap().get(getStringInstruction().getBinaryCode()).
                            setCorrectPredictions();

                }

            }
        }
    }
    */
}
