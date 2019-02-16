package Core.Instructions;

import Core.Assembler.Assembler;
import Core.util.Binary;
import Core.Exceptions.IllegalRegisterNumberException;
import Core.Exceptions.UnformattedInstructionException;

public class and extends Instruction { // and $rd, $rs, $rt
    private static int OPCODE = 0;
    private static int FUNCT = 36;

    public and(Assembler assembler, String instruction, String instructionPart2,int startClock,  long address) throws UnformattedInstructionException, IllegalRegisterNumberException {
        super(assembler,instruction,startClock, address);
        String[] instrucionParts = instructionPart2.split(",");
        boolean correctInstruction = true;
        for (int i = 0; i < 3; i++) {
            if (instrucionParts[i].charAt(0) == '$') {
                instrucionParts[i] = instrucionParts[i].substring(1);
            } else {
                correctInstruction = false;
                break;
            }
        }
        if (!correctInstruction)
            throw new UnformattedInstructionException(startClock);
        // startClock is the line of command too. started from 1

        setRd_register(instrucionParts[0]);
        setRs_register(instrucionParts[1]);
        setRt_register(instrucionParts[2]);
    }

    public void run(){
        int[] registers = getAssembler().getRegisters();
        registers[getRd_register()] = registers[getRs_register()] & registers[getRt_register()];
    }

    @Override
    public String getBinaryCode() {
        String opcode = Binary.intToBinaryString(OPCODE,6);
        String funct = Binary.intToBinaryString(FUNCT,6);
        String rs = Binary.intToBinaryString(getRs_register(),5);
        String rt = Binary.intToBinaryString(getRt_register(),5);
        String rd = Binary.intToBinaryString(getRd_register(),5);
        String shmat = "00000";
        String binaryCode = opcode + rs + rt + rd + shmat + funct;
        return binaryCode;
    }
}
