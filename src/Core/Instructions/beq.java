package Core.Instructions;

import Core.Assembler.Assembler;
import Core.Exceptions.IllegalRegisterNumberException;
import Core.Exceptions.SymbolNotFoundException;
import Core.Exceptions.UnformattedInstructionException;
import Core.util.Binary;

public class beq extends Instruction {
    private static int OPCODE = 4;

    public beq(Assembler assembler, String instruction, String instructionPart2, int line, long address) throws UnformattedInstructionException,
            IllegalRegisterNumberException, SymbolNotFoundException {
        super(assembler,instruction, line, address);
        String[] instrucionParts = instructionPart2.split(",");
        boolean correctInstruction = true;
        for (int i = 0; i < 2; i++) {
            if (instrucionParts[i].charAt(0) == '$') {
                instrucionParts[i] = instrucionParts[i].substring(1);
            } else {
                correctInstruction = false;
                break;
            }
        }
        if (!correctInstruction)
            throw new UnformattedInstructionException(line);
        // line is the line of command too. started from 1

        setRs_register(instrucionParts[0]);
        setRt_register(instrucionParts[1]);

        long labelAddress = assembler.getAddressOfLabel(instrucionParts[2],line);
        setOffset((int)(labelAddress - getAddress() - 4)/4);

    }

    public boolean isEqual() {
        if (getAssembler().getRegisters()[getRs_register()] == getAssembler().getRegisters()[getRt_register()])
            return true;
        else
            return false;
    }

    public void run() {

    }

    @Override
    public String getBinaryCode() {
        String opcode = Binary.intToBinaryString(OPCODE,6);
        String rs = Binary.intToBinaryString(getRs_register(),5);
        String rt = Binary.intToBinaryString(getRt_register(),5);
        String offset = Binary.intToBinaryString(getOffset(),16);
        String binaryCode = opcode + rs + rt  + offset;
        return binaryCode;
    }
}
