package Core.Pipeline.Stages;

import Core.Assembler.Assembler;
import Core.Instructions.*;
import Core.Pipeline.Pipeline;
import Core.Pipeline.PipelineRegisters.ID_EX_Register;
import Core.Pipeline.PipelineRegisters.PipelineRegister;
import Core.util.Binary;

public class ID_Stage extends Stage {
    private long next_PC; // PC + 4
    private int read_register1_number;
    private String read_register1_binary;
    private int read_register2_number;
    private String read_register2_binary;
    private String offset_16bit_binary;
    private String offset_32bit_binary;
    private String rt_binary; // Instruction[20-16]
    private String rd_binary; // Instructio [15-11]
    private boolean pcSrc;
    private long branchTarget = 0;
    private int readData1;
    private int readData2;

    //EX
    private int nextRegDst;
    private String nextALUOp;
    private int nextALUSrc;

    //MEM
    private boolean nextIsBranch;
    private boolean nextMemRead;
    private boolean nextMemWrite;

    //WB
    private int nextMemToReg;
    private boolean nextRegWrite;

    public ID_Stage(Assembler assembler, Instruction instruction, long next_PC,
                    Pipeline pipeline) {
        super(assembler, instruction);
        this.next_PC = next_PC;
        read_register1_number = instruction.getRs_register();
        read_register2_number = instruction.getRt_register();
        read_register1_binary = Binary.intToBinaryString(read_register1_number, 5);
        read_register2_binary = Binary.intToBinaryString(read_register2_number, 5);
        offset_16bit_binary = instruction.getBinaryCode().substring(16, 32);
        offset_32bit_binary = Binary.sign_extend(offset_16bit_binary);
        rt_binary = instruction.getBinaryCode().substring(11, 16);
        rd_binary = instruction.getBinaryCode().substring(16, 21);
//        this.prevoiusPipeline = previousPipeline;

        readData1 = getAssembler().getRegisters()[read_register1_number];
        readData2 = getAssembler().getRegisters()[read_register2_number];

        pcSrc = false;
        if (instruction instanceof beq) {
            pipeline.setStalled_beq(true);
            /*
            long offset = Binary.binaryStringToInt(offset_32bit_binary) * 4;
            this.branchTarget = offset + next_PC;

            boolean predict = false;
            if (assembler.getPredictionMap().containsKey(instruction.getBinaryCode())) {
                predict = assembler.getPredictionMap().get(instruction.getBinaryCode()).isTaken();
            } else {
                Prediction prediction = new Prediction((beq) instruction);
                assembler.getPredictionMap().put(instruction.getBinaryCode(), prediction);
                predict = prediction.isTaken();
            }
            pcSrc = predict;
            */
        }
    }

    @Override
    public PipelineRegister runStage() {
        setControlLines(getInstruction());

        ID_EX_Register id_ex = new ID_EX_Register(getInstruction(), next_PC, read_register1_number,
                read_register2_number, readData1, readData2, offset_32bit_binary, rt_binary,
                rd_binary, nextRegDst, nextALUOp, nextALUSrc, nextIsBranch, nextMemRead,
                nextMemWrite, nextMemToReg, nextRegWrite, isStaled());
        return id_ex;
    }

    private void setControlLines(Instruction instruction) {
        if (isStaled() || instruction instanceof nop) {
            nextRegDst = -1;
            nextALUOp = "--";
            nextALUSrc = -1;
            nextIsBranch = false;
            nextMemRead = false;
            nextMemWrite = false;
            nextMemToReg = -1;
            nextRegWrite = false;
            setStaled(true);
            return;
        }
        if (instruction instanceof add || instruction instanceof sub || instruction instanceof and || instruction instanceof or
                || instruction instanceof nor || instruction instanceof slt) {//R-Format
            nextRegDst = 1;
            nextALUOp = "10";
            nextALUSrc = 0;

            //MEM
            nextIsBranch = false;
            nextMemRead = false;
            nextMemWrite = false;

            //WB
            nextMemToReg = 0;
            nextRegWrite = true;
        } else if (instruction instanceof beq) {
            nextRegDst = -1; // X
            nextALUOp = "01";
            nextALUSrc = 0;

            //MEM
            nextIsBranch = true;
            nextMemRead = false;
            nextMemWrite = false;

            //WB
            nextMemToReg = -1; // X
            nextRegWrite = false;
        } else if (instruction instanceof lw) {
            nextRegDst = 0;
            nextALUOp = "00";
            nextALUSrc = 1;

            //MEM
            nextIsBranch = false;
            nextMemRead = true;
            nextMemWrite = false;

            //WB
            nextMemToReg = 1;
            nextRegWrite = true;
        } else if (instruction instanceof sw) {
            nextRegDst = -1; //  X
            nextALUOp = "00";
            nextALUSrc = 1;

            //MEM
            nextIsBranch = false;
            nextMemRead = false;
            nextMemWrite = true;

            //WB
            nextMemToReg = -1; // X
            nextRegWrite = false;
        }
    }

    public void hazardDetectionUnit(Pipeline pipeline) {
        // Hazard Detection Unit (lw)
        if (pipeline.getPipelineRegisters()[1] != null) {
            ID_EX_Register id_ex = (ID_EX_Register) pipeline.getPipelineRegisters()[1];
            if (!(getInstruction() instanceof lw) &&id_ex.isMemRead() &&
                    (id_ex.getRegisterNumber2() == read_register1_number ||
                            id_ex.getRegisterNumber2() == read_register2_number)) {
//                pipeline.getStages()[0].setStaled(true);
                pipeline.setStalled_lw(true);
            }
        }
/*
        // Hazard Detection Unit (beq)
        if (getStringInstruction() instanceof beq) {
            if (pipeline.getPipelineRegisters()[1] != null) {
                ID_EX_Register id_ex = (ID_EX_Register) pipeline.getPipelineRegisters()[1];

                // if forwarding
                // stall 1 or 2


                // if no forwarding
                boolean branchEquality = id_ex.getRegisterData1() == id_ex.getRegisterData2();
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
                    pipeline.setCorrectPcAddress(corrctNext_PC);
                    getAssembler().getPredictionMap().
                            get(getStringInstruction().getBinaryCode()).setTaken(branchEquality);

                }else{
                    getAssembler().getPredictionMap().get(getStringInstruction().getBinaryCode()).
                            setCorrectPredictions();
                }

            }
        }
*/
    }

    public boolean isPcSrc() {
        return pcSrc;
    }

    public long getBranchTarget() {
        return branchTarget;
    }

    public long getNext_PC() {
        return next_PC;
    }

    public int getRead_register1_number() {
        return read_register1_number;
    }

    public String getRead_register1_binary() {
        return read_register1_binary;
    }

    public int getRead_register2_number() {
        return read_register2_number;
    }

    public String getRead_register2_binary() {
        return read_register2_binary;
    }

    public String getOffset_16bit_binary() {
        return offset_16bit_binary;
    }

    public String getOffset_32bit_binary() {
        return offset_32bit_binary;
    }

    public String getRt_binary() {
        return rt_binary;
    }

    public String getRd_binary() {
        return rd_binary;
    }

    public int getNextRegDst() {
        return nextRegDst;
    }

    public String getNextALUOp() {
        return nextALUOp;
    }

    public int getNextALUSrc() {
        return nextALUSrc;
    }

    public boolean isNextIsBranch() {
        return nextIsBranch;
    }

    public boolean isNextMemRead() {
        return nextMemRead;
    }

    public boolean isNextMemWrite() {
        return nextMemWrite;
    }

    public int getNextMemToReg() {
        return nextMemToReg;
    }

    public boolean isNextRegWrite() {
        return nextRegWrite;
    }

    public int getReadData1() {
        return readData1;
    }

    public int getReadData2() {
        return readData2;
    }
}
