package Core.Instructions;

import Core.Assembler.Assembler;
import Core.util.Binary;
import Core.Exceptions.IllegalRegisterNumberException;
import Core.Exceptions.UnformattedInstructionException;

public class sw extends Instruction { // sw $rt, offset($rs)
    private static int OPCODE = 43;

    public sw(Assembler assembler,String instruction,String instructionPart2, int startClock, long address)throws UnformattedInstructionException, IllegalRegisterNumberException {
        super(assembler,instruction,startClock, address);
        String[] instrucionParts = instructionPart2.split(",");
        if (instrucionParts[0].charAt(0) == '$') {
            instrucionParts[0] = instrucionParts[0].substring(1);
        } else {
            throw new UnformattedInstructionException(startClock);
            // startClock is the line of command too. started from 1
        }
        setRt_register(instrucionParts[0]);

        try {
            String offset = "";
            int i = 0;
            while (instrucionParts[1].charAt(i) != '(') {
                offset += instrucionParts[1].charAt(i++);
            }
            try {
                if (offset.length() > 1 && (offset.charAt(1) == 'x' || offset.charAt(1) == 'X'))
                    setOffset(Binary.binaryStringToInt(Binary.hexStringToBinaryString(offset)));
                else
                    setOffset(Integer.parseInt(offset));
            } catch (NumberFormatException e) {
                throw new UnformattedInstructionException(startClock);
            }


            String rs = "";
            if (instrucionParts[1].charAt(++i) != '$')
                throw new UnformattedInstructionException(startClock);
            i++;
            while (instrucionParts[1].charAt(i) != ')') {
                rs += instrucionParts[1].charAt(i++);
            }
            setRs_register(rs);

        } catch (StringIndexOutOfBoundsException e) {
            throw new UnformattedInstructionException(startClock);
        }
    }

    public void run(){
        int[] registers = getAssembler().getRegisters();
        int address = registers[getRs_register()] + getOffset();
        int savedValue = registers[getRt_register()];
        getAssembler().saveMemory(address, savedValue);
    }

    @Override
    public String getBinaryCode() {
        String opcode = Binary.intToBinaryString(OPCODE, 6);
        String rs = Binary.intToBinaryString(getRs_register(), 5);
        String rt = Binary.intToBinaryString(getRt_register(), 5);
        String offset = Binary.intToBinaryString(getOffset(), 16);
        String binaryCode = opcode + rs + rt + offset;
        return binaryCode;
    }
}
