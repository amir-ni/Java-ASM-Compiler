package Core.Instructions;

import Core.Assembler.Assembler;
import Core.Exceptions.IllegalRegisterNumberException;

public abstract class Instruction {
    private Assembler assembler;
    private String instruction;
    private long address;
    private int rs_register;
    private int rt_register;
    private int rd_register;
    private int offset;
    private int line;
    private boolean dataHazard;
    private boolean controlHazard;

    public Instruction(Assembler assembler,String instruction, int line, long address) {
        this.assembler = assembler;
        this.instruction = instruction;
        this.line = line;
        this.address  =address;
    }

    public Assembler getAssembler() {
        return assembler;
    }

    public int getRs_register() {
        return rs_register;
    }

    public void setRs_register(int rs_register) throws IllegalRegisterNumberException {
        if (rs_register < 32)
            this.rs_register = rs_register;
        else
            throw new IllegalRegisterNumberException(line);
    }

    public void setRs_register(String registerValue) throws IllegalRegisterNumberException {
        try {
            setRs_register(Integer.parseInt(registerValue));
        } catch (NumberFormatException e) {
            setRs_register(convertRegisterName(registerValue));
        }
    }

    public int getRt_register() {
        return rt_register;
    }

    public void setRt_register(int rt_register) throws IllegalRegisterNumberException {
        if (rt_register < 32)
            this.rt_register = rt_register;
        else
            throw new IllegalRegisterNumberException(line);

    }

    public void setRt_register(String registerValue) throws IllegalRegisterNumberException {
        try {
            setRt_register(Integer.parseInt(registerValue));
        } catch (NumberFormatException e) {
            setRt_register(convertRegisterName(registerValue));
        }
    }

    public int getRd_register() {
        return rd_register;
    }

    public void setRd_register(int rd_register) throws IllegalRegisterNumberException {
        if (rd_register < 32)
            this.rd_register = rd_register;
        else
            throw new IllegalRegisterNumberException(line);

    }

    public void setRd_register(String registerValue) throws IllegalRegisterNumberException {
        try {
            setRd_register(Integer.parseInt(registerValue));
        } catch (NumberFormatException e) {
            setRd_register(convertRegisterName(registerValue));
        }
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public boolean isDataHazard() {
        return dataHazard;
    }

    public void setDataHazard(boolean dataHazard) {
        this.dataHazard = dataHazard;
    }

    public boolean isControlHazard() {
        return controlHazard;
    }

    public void setControlHazard(boolean controlHazard) {
        this.controlHazard = controlHazard;
    }

    public abstract void run();

    public abstract String getBinaryCode();

    public long getAddress() {
        return address;
    }

    public int convertRegisterName(String registerName) throws IllegalRegisterNumberException {
        registerName = registerName.toLowerCase().trim();
        switch (registerName) {
            case "zero":
                return 0;
            case "at":
                return 1;
            case "v0":
                return 2;
            case "v1":
                return 3;
            case "a0":
                return 4;
            case "a1":
                return 5;
            case "a2":
                return 6;
            case "a3":
                return 7;
            case "t0":
                return 8;
            case "t1":
                return 9;
            case "t2":
                return 10;
            case "t3":
                return 11;
            case "t4":
                return 12;
            case "t5":
                return 13;
            case "t6":
                return 14;
            case "t7":
                return 15;
            case "s0":
                return 16;
            case "s1":
                return 17;
            case "s2":
                return 18;
            case "s3":
                return 19;
            case "s4":
                return 20;
            case "s5":
                return 21;
            case "s6":
                return 22;
            case "s7":
                return 23;
            case "t8":
                return 24;
            case "t9":
                return 25;
            case "k0":
                return 26;
            case "k1":
                return 27;
            case "gp":
                return 28;
            case "sp":
                return 29;
            case "fp":
                return 30;
            case "ra":
                return 31;
            default:
                throw new IllegalRegisterNumberException(line);
        }
    }

    public String getStringInstruction() {
        return instruction;
    }
}
