package Core.Instructions;

import Core.Assembler.Assembler;

public class nop extends Instruction {
    Instruction mainInstruction;

    public nop(Assembler assembler, int startClock, long address, Instruction mainInstruction) {
        super(assembler, "nop", startClock, address);
        this.mainInstruction = mainInstruction;
    }

    @Override
    public void run() {

    }

    @Override
    public String getBinaryCode() {
        return "00000000000000000000000000000000";
    }

    public Instruction getMainInstruction() {
        return mainInstruction;
    }
}
