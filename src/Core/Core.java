package Core;

import Core.Assembler.Assembler;
import Core.Exceptions.IllegalRegisterNumberException;
import Core.Exceptions.SymbolNotFoundException;
import Core.Exceptions.UndefindInstructionException;
import Core.Exceptions.UnformattedInstructionException;
import Core.Images.Images;
import Core.Instructions.nop;
import Core.Pipeline.Pipeline;
import Core.Pipeline.PipelineRegisters.EX_MEM_Register;
import Core.Pipeline.PipelineRegisters.ID_EX_Register;
import Core.Pipeline.PipelineRegisters.IF_ID_Register;
import Core.Pipeline.PipelineRegisters.MEM_WB_Register;
import Core.Pipeline.Stages.*;
import Core.View.Single_Clock_Cycle_Panel;
import Core.util.Binary;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class Core {
    //    private List<String> normalInstructions;
    private Assembler assembler;


    private int[] registers;
    private long pc_register;
    //IF
    private boolean if_available;
    private long pc_IF;
    private int pcSrc;
    private String instruction_IF;
    private String instructionBinary_IF;
    //IF/ID
    private boolean if_id_available;
    private String instruction_IF_ID;
    private long pc_4_IF_ID;
    //ID
    private boolean id_available;
    private String instructionBinary_ID;
    private String instruction_ID;
    private String offset16;
    private String Offset32;
    private String rt_binary;
    private String rd_binary;
    private int register1 = -1;
    private int register2 = -1;
    private int readData1 = -1;
    private int readData2 = -1;
    // Control Unit
    private int nextRegDst = -1;
    private String nextALUOp = "--";
    private int nextALUSrc = -1;
    private boolean nextIsBranch = false;
    private boolean nextMemRead = false;
    private boolean nextMemWrite = false;
    private int nextMemToReg = -1;
    private boolean nextRegWrite = false;
    //HDU
    private int mux_control_0 = -1;

    //ID/EX
    private long pc_4_id_ex;
    private int registerNumber1_id_ex;
    private int registerNumber2_id_ex;
    private int readData1_id_ex;
    private int readData2_id_ex;
    private int rt_id_ex;
    private int rd_id_ex;
    private String offset_id_ex;

    //EX
    private boolean ex_available;
    private String instruction_EX;
    private String instructionBinary_EX;
    private int aluInput1;
    private int aluInput2;
    private int aluResult;
    private int branchAluInput1;
    private int branchAluInput2;
    private long branchOutput;
    private String forwardA;
    private String forwardB;
    private int regDst;
    private int aluSrc;
    private String aluOp;

    //EX/MEM
    private long branchTarget_ex_mem;
    private boolean zero_ex_mem;
    private int aluResult_ex_mem;
    private int readData2_ex_mem;
    private int destinationRegister_ex_mem;

    //MEM
    private boolean mem_available;
    private String instruction_MEM;
    private String instructionBinary_MEM;
    private long address_mem;
    private int readDataFromMem_mem;
    private int writeData_mem;
    private boolean memRead_mem;
    private boolean memWrite_mem;
    private boolean branch_mem;

    // MEM/WB
    private int readDataFromMem_mem_wb;
    private int aluResult_mem_wb;
    private int destinationRegister_mem_wb;

    // WB
    private boolean wb_available;
    private String instruction_WB;
    private String instructionBinary_WB;
    private int memToReg_wb;
    private boolean regWrite_wb;

    private boolean stalled;

    JLabel if_instruction_lbl;
    JLabel id_instruction_lbl;
    JLabel ex_instruction_lbl;
    JLabel mem_instruction_lbl;
    JLabel wb_instruction_lbl;


    public Core(List<String> normalInstructions, JTable registerTable) throws UnformattedInstructionException, SymbolNotFoundException, UndefindInstructionException, IllegalRegisterNumberException {
        assembler = new Assembler(normalInstructions, registerTable);
        assembler.runPipeline();

        for (int i = 0; i < 31; i++) {
            registerTable.setValueAt(assembler.getPipelines().get(assembler.getPipelines().size() - 1).getRegisters()[i], i, 2);
        }
        registerTable.setValueAt(assembler.getPc_register(), 32, 2);

        if_instruction_lbl = new JLabel();
        if_instruction_lbl.setBounds(20, 15, 150, 20);
        id_instruction_lbl = new JLabel();
        id_instruction_lbl.setBounds(300, 15, 150, 20);
        ex_instruction_lbl = new JLabel();
        ex_instruction_lbl.setBounds(550, 15, 150, 20);
        mem_instruction_lbl = new JLabel();
        mem_instruction_lbl.setBounds(700, 15, 150, 20);
        wb_instruction_lbl = new JLabel();
        wb_instruction_lbl.setBounds(850, 15, 150, 20);


    }

    public String getPipelineDetailsText(int clock) {
        String responce = "";
        Pipeline pipeline = assembler.getPipelines().get(clock);
        responce += "Clock: " + clock;
        //TODO

        return responce;
    }

    public String getPipelineDetailsText() {
        String responce = "";
        for (Pipeline pipeline : assembler.getPipelines()) {
            responce += getPipelineDetailsText(pipeline.getClock());
            responce += "==============================================";
        }
        return responce;
    }

    public void showGraphical(Single_Clock_Cycle_Panel panel, int clock) {
        setDetails(clock);
        panel.update();

        Image[] stage_images = new Image[5];
        if (if_available) {
            stage_images[0] = Images.if2_stage;
        } else {
            stage_images[0] = Images.if_stage;
        }
        if (id_available) {
            if (regWrite_wb) {
                if (instruction_ID.substring(0, 3).equals("beq") || instruction_ID.substring(0, 2).equals("lw"))
                    stage_images[1] = Images.id_beq_lw_wb;
                else if (instruction_ID.substring(0, 2).equals("sw"))
                    stage_images[1] = Images.id_sw_wb;
                else
                    stage_images[1] = Images.id_r_wb;
            } else {
                if (instruction_ID.substring(0, 3).equals("beq") || instruction_ID.substring(0, 2).equals("lw"))
                    stage_images[1] = Images.id_beq_lw;
                else if (instruction_ID.substring(0, 2).equals("sw"))
                    stage_images[1] = Images.id_sw;
                else
                    stage_images[1] = Images.id_r;
            }
        } else {
            if (regWrite_wb)
                stage_images[1] = Images.id_wb;
            else
                stage_images[1] = Images.id_stage;
        }
        if (ex_available) {
            if (instruction_EX.substring(0, 3).equals("beq"))
                stage_images[2] = Images.ex_beq;
            else if (instruction_EX.substring(0, 2).equals("sw"))
                stage_images[2] = Images.ex_sw;
            else
                stage_images[2] = Images.ex_r;
        } else {
            stage_images[2] = Images.ex_stage;
        }
        if (mem_available) {
            if (instruction_MEM.substring(0, 3).equals("beq"))
                stage_images[3] = Images.mem_beq;
            else if (instruction_MEM.substring(0, 2).equals("sw"))
                stage_images[3] = Images.mem_sw;
            else if (instruction_MEM.substring(0, 2).equals("lw"))
                stage_images[3] = Images.mem_lw;
            else
                stage_images[3] = Images.mem_r;
        } else {
            stage_images[3] = Images.mem_stage;
        }
        if (wb_available) {
            if (regWrite_wb)
                stage_images[4] = Images.wb_write;
            else
                stage_images[4] = Images.wb_;
        } else {
            stage_images[4] = Images.wb_stage;
        }


        panel.setStages(stage_images);
        panel.repaint();

        if_instruction_lbl.setText("");
        panel.add(if_instruction_lbl);
        id_instruction_lbl.setText("");
        panel.add(id_instruction_lbl);
        ex_instruction_lbl.setText("");
        panel.add(ex_instruction_lbl);
        mem_instruction_lbl.setText("");
        panel.add(mem_instruction_lbl);
        wb_instruction_lbl.setText("");
        panel.add(wb_instruction_lbl);


        if (if_available) {
            if_instruction_lbl.setText(instruction_IF);
            if_instruction_lbl.setToolTipText(instructionBinary_IF);
        }

        if (id_available) {
            id_instruction_lbl.setText(instruction_ID);
            id_instruction_lbl.setToolTipText(instructionBinary_ID);
        }

        if (ex_available) {
            ex_instruction_lbl.setText(instruction_EX);
            ex_instruction_lbl.setToolTipText(instructionBinary_EX);
        }

        if (mem_available) {
            mem_instruction_lbl.setText(instruction_MEM);
            mem_instruction_lbl.setToolTipText(instructionBinary_MEM);
        }

        if (wb_available) {
            wb_instruction_lbl.setText(instruction_WB);
            wb_instruction_lbl.setToolTipText(instructionBinary_WB);
        }
    }

    public void setDetails(int clock) {
        Pipeline pipeline;
        pipeline = assembler.getPipelines().get(clock);
        resetDatails();

        registers = pipeline.getRegisters();
        pc_register = pipeline.getPc();
        //IF
        IF_Stage if_stage = (IF_Stage) pipeline.getStages()[0];
        if (if_stage != null) {
            if_available = true;
            instruction_IF = if_stage.getInstruction().getStringInstruction();
            instructionBinary_IF = if_stage.getInstruction().getBinaryCode();
            if (if_stage.getPcSrc())
                pcSrc = 1;
            else
                pcSrc = 0;
            pc_IF = if_stage.getPc_register();
            //TODO IF.Flush
        } else {
            if_available = false;
            instruction_IF = "-";
            instructionBinary_IF = "-";
            pcSrc = -1;
            //pc_IF = ; //TODO
        }
        //IF/ID
        IF_ID_Register if_id = (IF_ID_Register) pipeline.getPipelineRegisters()[0];
        if (if_id != null && !if_id.isStaled()) {
            if_id_available = true;
            instruction_IF_ID = if_id.getInstructionText();
            pc_4_IF_ID = if_id.getNext_PC();
        } else {
            if_id_available = false;
            instruction_IF_ID = "-";
            //pc_4_IF_ID = ;//TODO
        }
        //ID
        ID_Stage id_stage = (ID_Stage) pipeline.getStages()[1];
        if (id_stage != null) {
            id_available = true;
            instructionBinary_ID = id_stage.getInstruction().getBinaryCode();
            instruction_ID = id_stage.getInstruction().getStringInstruction();
            if (id_stage.getInstruction() instanceof nop) {
                nop i = (nop) id_stage.getInstruction();
                instruction_ID = "nop(" + i.getMainInstruction().getStringInstruction() + ")";
            }
            offset16 = id_stage.getOffset_16bit_binary();//[15-0]
            Offset32 = id_stage.getOffset_32bit_binary();
            rt_binary = id_stage.getRt_binary();// [20-16]
            rd_binary = id_stage.getRd_binary();// [15-11]
            register1 = id_stage.getRead_register1_number();
            register2 = id_stage.getRead_register2_number();
            readData1 = id_stage.getReadData1();
            readData2 = id_stage.getReadData2();

            if (pipeline.isStalled_beq() || pipeline.isStalled_lw() ){
                stalled = true;
            }
        } else {
            id_available = false;
            instruction_ID = "-";
            instructionBinary_ID = "-";
            register1 = 0;
            register2 = 0;
            readData1 = 0;
            readData2 = 0;
        }

        //ID/EX
        ID_EX_Register id_ex = (ID_EX_Register) pipeline.getPipelineRegisters()[1];
        if (id_ex != null) {
            pc_4_id_ex = id_ex.getNext_PC();
            registerNumber1_id_ex = id_ex.getRegisterNumber1();
            registerNumber2_id_ex = id_ex.getRegisterNumber2();
            readData1_id_ex = id_ex.getRegisterData1();
            readData2_id_ex = id_ex.getRegisterData2();
            rt_id_ex = Binary.binaryStringToInt(id_ex.getRt_binary());
            rd_id_ex = Binary.binaryStringToInt(id_ex.getRd_binary());
            offset_id_ex = id_ex.getOffset_32bit_binary();
        }

        //EX
        EX_Stage ex_stage = (EX_Stage) pipeline.getStages()[2];
        if (ex_stage != null) {
            ex_available = true;
            instruction_EX = ex_stage.getInstruction().getStringInstruction();
            instructionBinary_EX = ex_stage.getInstruction().getBinaryCode();
            if (ex_stage.getInstruction() instanceof nop) {
                nop i = (nop) ex_stage.getInstruction();
                instruction_EX = "nop(" + i.getMainInstruction().getStringInstruction() + ")";
            }
            aluInput1 = ex_stage.getInputALU1();
            aluInput2 = ex_stage.getInputALU2();
            aluResult = ex_stage.getAluResult();
            branchAluInput1 = (int) ex_stage.getNext_PC();
            branchAluInput2 = Binary.binaryStringToInt(ex_stage.getOffset_32bit_binary()) * 4;
            branchOutput = ex_stage.getBranchTarget();
            forwardA = Binary.intToBinaryString(ex_stage.getForwardA(), 2);
            forwardB = Binary.intToBinaryString(ex_stage.getForwardB(), 2);
            regDst = ex_stage.getRegDst();
            aluSrc = ex_stage.getALUSrc();
            aluOp = ex_stage.getALUOp();

        } else {
            ex_available = false;
            instruction_EX = "-";
            instructionBinary_EX = "-";
        }

        //EX/MEM
        EX_MEM_Register ex_mem = (EX_MEM_Register) pipeline.getPipelineRegisters()[2];
        if (ex_mem != null) {
            branchTarget_ex_mem = ex_mem.getBranchTarget();
            zero_ex_mem = ex_mem.isZero();
            aluResult_ex_mem = ex_mem.getAluResult();
            readData2_ex_mem = ex_mem.getReadData2();
            destinationRegister_ex_mem = ex_mem.getWriteRegister();
        }

        //MEM
        MEM_Stage mem_stage = (MEM_Stage) pipeline.getStages()[3];
        if (mem_stage != null) {
            mem_available = true;
            instruction_MEM = mem_stage.getInstruction().getStringInstruction();
            instructionBinary_MEM = mem_stage.getInstruction().getBinaryCode();
            if (mem_stage.getInstruction() instanceof nop) {
                nop i = (nop) mem_stage.getInstruction();
                instruction_MEM = "nop(" + i.getMainInstruction().getStringInstruction() + ")";
            }

            address_mem = mem_stage.getBranchTarget();
            readDataFromMem_mem = mem_stage.getMemoryData();
            writeData_mem = mem_stage.getReadData2();
            branch_mem = mem_stage.isBranch();
            memRead_mem = mem_stage.isMemRead();
            memWrite_mem = mem_stage.isMemWrite();
        } else {
            mem_available = false;
            instruction_MEM = "-";
            instructionBinary_MEM = "-";

            branch_mem = false;
        }

        MEM_WB_Register mem_wb = (MEM_WB_Register) pipeline.getPipelineRegisters()[3];
        if (mem_wb != null) {
            aluResult_mem_wb = mem_wb.getAluResult();
            destinationRegister_mem_wb = mem_wb.getWriteRegister();
            readDataFromMem_mem_wb = mem_wb.getMemoryData();
        } else {
            //TODO
        }

        //WB
        WB_Stage wb_stage = (WB_Stage) pipeline.getStages()[4];
        if (wb_stage != null) {
            wb_available = true;
            instruction_WB = wb_stage.getInstruction().getStringInstruction();
            instructionBinary_WB = wb_stage.getInstruction().getBinaryCode();
            if (wb_stage.getInstruction() instanceof nop) {
                nop i = (nop) wb_stage.getInstruction();
                instruction_WB = "nop(" + i.getMainInstruction().getStringInstruction() + ")";
            }
            memToReg_wb = wb_stage.getMemToReg();
            regWrite_wb = wb_stage.isRegWrite();
        } else {
            wb_available = false;
            instruction_WB = "-";
            instructionBinary_WB = "-";
        }
    }

    public void resetDatails() {
        if_available = false;
        if_id_available = false;
        id_available = false;
        ex_available = false;
        mem_available = false;
        wb_available = false;

        pcSrc = -1;
        regWrite_wb = false;
        instruction_IF = "-";
        instructionBinary_IF = "-";
        instruction_IF_ID = "-";
        instruction_ID = "-";
        instructionBinary_ID = "-";
        instruction_EX = "-";
        instructionBinary_EX = "-";
        instruction_MEM = "-";
        instructionBinary_MEM = "-";
        instruction_WB = "-";
        instructionBinary_WB = "-";

        offset16 = "-";
        Offset32 = "-";
        rt_binary = "-";
        rd_binary = "-";

        stalled = false;
        /*
        private long pc_IF;
        private long pc_4_IF_ID;
        //ID

        private int register1 = -1;
        private int register2 = -1;
        private int readData1 = -1;
        private int readData2 = -1;
        // Control Unit
        private int nextRegDst = -1;
        private String nextALUOp = "--";
        private int nextALUSrc = -1;
        private boolean nextIsBranch = false;
        private boolean nextMemRead = false;
        private boolean nextMemWrite = false;
        private int nextMemToReg = -1;
        private boolean nextRegWrite = false;
        //HDU
        private int mux_control_0 = -1;
        //ID/EX
        private long pc_4_id_ex;
        private int registerNumber1_id_ex;
        private int registerNumber2_id_ex;
        private int readData1_id_ex;
        private int readData2_id_ex;
        private int rt_id_ex;
        private int rd_id_ex;
        private String offset_id_ex;
        //EX
        private boolean ex_available;
        private int aluInput1;
        private int aluInput2;
        private int aluResult;
        private int branchAluInput1;
        private int branchAluInput2;
        private long branchOutput;
        private String forwardA;
        private String forwardB;
        private int regDst;
        private int aluSrc;
        private String aluOp;
        //EX/MEM
        private long branchTarget_ex_mem;
        private boolean zero_ex_mem;
        private int aluResult_ex_mem;
        private int readData2_ex_mem;
        private int destinationRegister_ex_mem;
        //MEM
        private boolean mem_available;
        private long address_mem;
        private int readDataFromMem_mem;
        private int writeData_mem;
        private boolean memRead_mem;
        private boolean memWrite_mem;
        private boolean branch_mem;
        // MEM/WB
        private int readDataFromMem_mem_wb;
        private int aluResult_mem_wb;
        private int destinationRegister_mem_wb;
        // WB
        private boolean wb_available;
        private int memToReg_wb;
        private boolean regWrite_wb;
        */
    }

//    /**
//     * divide instructions into a List and remove additional spaces (except the first one
//     * that be in middle of instruction name and other parts).
//     * labels with instruction that are in one line reformat to this : "label: instruction"
//     *
//     * @param instructions the content of text area.
//     * @return a list that every element is one instruction.
//     */
//    public static List<String> normalizeInstructions(String instructions) {
//        List<String> instructionsList = new ArrayList<>();
//        String[] lines = instructions.split("\n");
//        for (int i = 0; i < lines.length; i++) {
//            boolean firstSpace = false;
//            String ins = "";
//            if (lines[i].contains(":")) {
//                int j = 0;
//                while (lines[i].charAt(j) != ':') {
//                    ins += lines[i].charAt(j++);
//                }
//                int z = ins.length() - 1;
//                while (z > 0 && ins.charAt(z) == ' ') {
//                    ins = ins.substring(0, ins.length() - 1);
//                    z = ins.length() - 1;
//                }
//                ins += ": ";
//                j++;
//                while (j < lines[i].length() && lines[i].charAt(j) == ' ') {
//                    j++;
//                }
//                for (; j < lines[i].length(); j++) {
//                    if (lines[i].charAt(j) != ' ' ||
//                            (lines[i].charAt(j) == ' ' && !firstSpace)) {
//                        ins += lines[i].charAt(j);
//                        if (lines[i].charAt(j) == ' ')
//                            firstSpace = true;
//                    }
//                }
//            } else {
//                for (int j = 0; j < lines[i].length(); j++) {
//                    if (lines[i].charAt(j) != ' ' ||
//                            (lines[i].charAt(j) == ' ' && !firstSpace)) {
//                        ins += lines[i].charAt(j);
//                        if (lines[i].charAt(j) == ' ')
//                            firstSpace = true;
//                    }
//                }
//            }
//            instructionsList.add(ins);
//        }
//        return instructionsList;
//    }


    public Assembler getAssembler() {
        return assembler;
    }

    public int[] getRegisters() {
        return registers;
    }

    public long getPc_IF() {
        return pc_IF;
    }

    public int getPcSrc() {
        return pcSrc;
    }

    public String getInstruction_IF_ID() {
        return instruction_IF_ID;
    }

    public long getPc_4_IF_ID() {
        return pc_4_IF_ID;
    }

    public String getInstructionBinary_ID() {
        return instructionBinary_ID;
    }

    public String getOffset16() {
        return offset16;
    }

    public String getOffset32() {
        return Offset32;
    }

    public String getRt_binary() {
        return rt_binary;
    }

    public String getRd_binary() {
        return rd_binary;
    }

    public int getRegister1() {
        return register1;
    }

    public int getRegister2() {
        return register2;
    }

    public int getReadData1() {
        return readData1;
    }

    public int getReadData2() {
        return readData2;
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

    public int getMux_control_0() {
        return mux_control_0;
    }

    public String getInstructionBinary_IF() {
        return instructionBinary_IF;
    }

    public String getInstruction_ID() {
        return instruction_ID;
    }

    public String getInstruction_IF() {
        return instruction_IF;
    }

    public String getInstruction_EX() {
        return instruction_EX;
    }

    public String getInstructionBinary_EX() {
        return instructionBinary_EX;
    }

    public String getInstruction_MEM() {
        return instruction_MEM;
    }

    public String getInstructionBinary_MEM() {
        return instructionBinary_MEM;
    }

    public String getInstruction_WB() {
        return instruction_WB;
    }

    public String getInstructionBinary_WB() {
        return instructionBinary_WB;
    }

    public boolean isIf_available() {
        return if_available;
    }

    public boolean isIf_id_available() {
        return if_id_available;
    }

    public boolean isId_available() {
        return id_available;
    }

    public long getPc_4_id_ex() {
        return pc_4_id_ex;
    }

    public int getRegisterNumber1_id_ex() {
        return registerNumber1_id_ex;
    }

    public int getRegisterNumber2_id_ex() {
        return registerNumber2_id_ex;
    }

    public int getReadData1_id_ex() {
        return readData1_id_ex;
    }

    public int getReadData2_id_ex() {
        return readData2_id_ex;
    }

    public int getRt_id_ex() {
        return rt_id_ex;
    }

    public int getRd_id_ex() {
        return rd_id_ex;
    }

    public String getOffset_id_ex() {
        return offset_id_ex;
    }

    public boolean isEx_available() {
        return ex_available;
    }

    public int getAluInput1() {
        return aluInput1;
    }

    public int getAluInput2() {
        return aluInput2;
    }

    public int getAluResult() {
        return aluResult;
    }

    public boolean isMem_available() {
        return mem_available;
    }

    public boolean isWb_available() {
        return wb_available;
    }

    public int getBranchAluInput1() {
        return branchAluInput1;
    }

    public int getBranchAluInput2() {
        return branchAluInput2;
    }

    public long getBranchOutput() {
        return branchOutput;
    }

    public long getBranchTarget_ex_mem() {
        return branchTarget_ex_mem;
    }

    public boolean isZero_ex_mem() {
        return zero_ex_mem;
    }

    public int getAluResult_ex_mem() {
        return aluResult_ex_mem;
    }

    public int getReadData2_ex_mem() {
        return readData2_ex_mem;
    }

    public int getDestinationRegister_ex_mem() {
        return destinationRegister_ex_mem;
    }

    public String getForwardA() {
        return forwardA;
    }

    public String getForwardB() {
        return forwardB;
    }

    public long getAddress_mem() {
        return address_mem;
    }

    public int getReadDataFromMem_mem() {
        return readDataFromMem_mem;
    }

    public int getWriteData_mem() {
        return writeData_mem;
    }

    public boolean isMemRead_mem() {
        return memRead_mem;
    }

    public boolean isMemWrite_mem() {
        return memWrite_mem;
    }

    public int getReadDataFromMem_mem_wb() {
        return readDataFromMem_mem_wb;
    }

    public int getAluResult_mem_wb() {
        return aluResult_mem_wb;
    }

    public int getDestinationRegister_mem_wb() {
        return destinationRegister_mem_wb;
    }

    public int getRegDst() {
        return regDst;
    }

    public int getAluSrc() {
        return aluSrc;
    }

    public String getAluOp() {
        return aluOp;
    }

    public boolean isBranch_mem() {
        return branch_mem;
    }

    public int getMemToReg_wb() {
        return memToReg_wb;
    }

    public long getPc_register() {
        return pc_register;
    }

    public boolean isRegWrite_wb() {
        return regWrite_wb;
    }

    public boolean isStalled() {
        return stalled;
    }
}
