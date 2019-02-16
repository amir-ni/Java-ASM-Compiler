package Core.Pipeline.PipelineRegisters;

import Core.Instructions.Instruction;

public class ID_EX_Register implements PipelineRegister{
    private long next_PC; // PC + 4
    private int registerNumber1;
    private int registerNumber2;
    private int registerData1;
    private int registerData2;
    private String offset_32bit_binary;
    private String rt_binary; // Instruction[20-16]
    private String rd_binary; // Instructio [15-11]
    private boolean staled;
    private Instruction instruction;

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


    public ID_EX_Register(Instruction instruction, long next_PC, int registerNumber1, int registerNumber2,
                          int registerData1, int registerData2, String offset_32bit_binary,
                          String rt_binary, String rd_binary, int regDst, String ALUOp,
                          int ALUSrc, boolean isBranch, boolean memRead, boolean memWrite,
                          int memToReg, boolean regWrite, boolean isStaled) {
        this.instruction = instruction;
        this.next_PC = next_PC;
        this.registerData1 = registerData1;
        this.registerData2 = registerData2;
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
        staled = isStaled;

    }

    public long getNext_PC() {
        return next_PC;
    }

    public void setNext_PC(long next_PC) {
        this.next_PC = next_PC;
    }

    public int getRegisterData1() {
        return registerData1;
    }

    public int getRegisterData2() {
        return registerData2;
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

    public int isMemToReg() {
        return memToReg;
    }

    public boolean isRegWrite() {
        return regWrite;
    }

    public int getRegisterNumber1() {
        return registerNumber1;
    }

    public int getRegisterNumber2() {
        return registerNumber2;
    }

    public int getMemToReg() {
        return memToReg;
    }

    public boolean isStaled() {
        return staled;
    }

    public Instruction getInstruction() {
        return instruction;
    }
}
