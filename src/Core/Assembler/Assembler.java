package Core.Assembler;

import Core.Exceptions.IllegalRegisterNumberException;
import Core.Exceptions.SymbolNotFoundException;
import Core.Exceptions.UndefinedInstructionException;
import Core.Exceptions.UnformattedInstructionException;
import Core.Instructions.*;
import Core.Pipeline.Pipeline;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Assembler {
    private int clock = 0;
    private List<Instruction> instructions;
    private int[] registers;
    private long pc_register = 16; // 0000 0010_hex
    private long ra_register = 0;
    private Map<Integer, Integer> memory;
    private Map<String, Long> symbolTable;
    private List<Pipeline> pipelines;


    public Assembler(List<String> instructions, JTable registerTable) throws UndefinedInstructionException,
            UnformattedInstructionException, IllegalRegisterNumberException,
            SymbolNotFoundException {
        this.instructions = new ArrayList<>();
        registers = new int[32];
        pipelines = new ArrayList<>();
        symbolTable = new HashMap<>();
        /*predictionMap = new HashMap<String, Prediction>();*/
        memory = new HashMap<>();
        memory.put(256, 100); //  0000 0100_hex : 100
        registers[29] = 16711680; // $sp : 00FF 0000_hex

        for (int i = 0; i < 31; i++) {
            if (registerTable.getValueAt(i,2) instanceof String)
                registers[i] = Integer.parseInt((String) registerTable.getValueAt(i, 2));
            else
                registers[i] = (int)registerTable.getValueAt(i,2);
        }

        instructions = findLabels(instructions);
        int i = 1;
        int insCounter = 0;
//        int branchInsCounter = 0; // this is count instructions + labels in different way // It defines for branch offset
//        boolean branchInsCounterChecker = false;
        for (String instruction : instructions) {
            String[] instructionParts = instruction.split(" ");
            Instruction ins = null;
            switch (instructionParts[0]) {
                case "add":
                    ins = new add(this, instruction, instructionParts[1], i++, pc_register + insCounter++ * 4);
                    break;
                case "sub":
                    ins = new sub(this, instruction, instructionParts[1], i++, pc_register + insCounter++ * 4);
                    break;
                case "and":
                    ins = new and(this, instruction, instructionParts[1], i++, pc_register + insCounter++ * 4);
                    break;
                case "or":
                    ins = new or(this, instruction, instructionParts[1], i++, pc_register + insCounter++ * 4);
                    break;
                case "nor":
                    ins = new nor(this, instruction, instructionParts[1], i++, pc_register + insCounter++ * 4);
                    break;
                case "slt":
                    ins = new slt(this, instruction, instructionParts[1], i++, pc_register + insCounter++ * 4);
                    break;
                case "beq":
                    ins = new beq(this, instruction, instructionParts[1], i++, pc_register + insCounter++ * 4);
                    break;
                case "lw":
                    ins = new lw(this, instruction, instructionParts[1], i++, pc_register + insCounter++ * 4);
                    break;
                case "sw":
                    ins = new sw(this, instruction, instructionParts[1], i++, pc_register + insCounter++ * 4);
                    break;
                default:
                    if (instructionParts[0].charAt(0) == '*') {
                        i++;
//                        if (!branchInsCounterChecker){
//                            branchInsCounter++;
//                            branchInsCounterChecker = true;
//                        }
                    } else {
                        throw new UndefinedInstructionException(i); // start lines from 1
                    }
            }
            if (ins != null) {
                this.instructions.add(ins);
//                branchInsCounter++;
//                branchInsCounterChecker = false;
            }

        }

    }

    public int getClock() {
        return clock;
    }

    public void setClock(int clock) {
        this.clock = clock;
    }

    public List<Instruction> getInstructions() {
        return instructions;
    }

    public int[] getRegisters() {
        return registers;
    }

    public long getPc_register() {
        return pc_register;
    }

    public void setPc_register(long pc_register) {
        this.pc_register = pc_register;
    }

    public List<Pipeline> getPipelines() {
        return pipelines;
    }

    /*public Map<String, Prediction> getPredictionMap() {
        return predictionMap;
    }
    */

    public long getAddressOfLabel(String label, int line) throws SymbolNotFoundException {
        if (symbolTable.containsKey(label)) {
            return symbolTable.get(label);
        } else {
            throw new SymbolNotFoundException(line, label);
        }
    }

    /**
     * this functions at the start find all labels and put them into symbol table.
     * then remove it from the "instruction list" and put at that line an '*'.
     * (if this format "label: instruction" happend do not put an * in the first of line)
     *
     * @param instructions the "instrucion list" that passed to the assembler.
     * @return modified instruction list.
     */
    private List<String> findLabels(List<String> instructions) throws UnformattedInstructionException {
        List<String> finalList = new ArrayList<>();

        int insCounter = 0;
        int i = 1;
        for (String instruction : instructions) {
            if (instruction.contains(":")) {
                String[] instructionParts = instruction.split(" ");
                if (instructionParts.length > 1) {
                    if (instructionParts[0].charAt(instructionParts[0].length() - 1) == ':') {
                        symbolTable.put(instructionParts[0].substring(0, instructionParts[0].length() - 1), pc_register + insCounter * 4);
                        String ins = "";
                        for (int j = 1; j < instructionParts.length; j++) {
                            if (j == 2)
                                ins += " ";
                            ins += instructionParts[j];
                        }
                        finalList.add(ins);
                        insCounter++;
                    } else {
                        throw new UnformattedInstructionException(i);
                    }
                } else {
                    symbolTable.put(instructionParts[0].substring(0, instructionParts[0].length() - 1), pc_register + insCounter * 4);
                    finalList.add("*" + instructionParts[0]);
                    i++;
                }

            } else {
                finalList.add(instruction);
                i++;
                insCounter++;
            }
        }
        return finalList;
    }

    /**
     * run instructions directly without pipeline;
     */
    public void runInstructions() {
        for (int i = 0; i < instructions.size(); i++) {
            Instruction ins = instructions.get(i);
            if (ins instanceof beq && ((beq) ins).isEqual())
                i = ins.getOffset() - 2; // -1 for start form 1 and -1 for will be ++ at for
            else if (ins != null)
                ins.run();
        }
    }

    public int loadMemory(int address) {
        if (memory.containsKey(address))
            return memory.get(address);
        return 0;
    }

    public void saveMemory(int address, int value) {
        memory.put(address, value);
    }

    public void runPipeline() {
        // build pipeline at clock 0
        Instruction[] last5Instructions = {null, null, null, null, null};
        Pipeline pipeline = new Pipeline(this, last5Instructions, null, clock++);
        pipelines.add(pipeline);


//        int insCount = 0;
        while (true) {
            Pipeline newPipeline = pipelines.get(pipelines.size() - 1).buildNextPipeline(instructions);
            pipelines.add(newPipeline);

            // check end of while
            int end = 0;
            for (int i = 0; i < 5; i++) {
                if (newPipeline.getStages()[i] == null)
                    end++;
            }
            if (end == 5)
                break;

////            if ( insCount < instructions.size() && instructions.get(insCount) == null ){
////                // for label lines
////                insCount++;
////                continue;
////            }
//            for (int j = 4; j > 0; j--) {
//                last5Instructions[j] = last5Instructions[j - 1];
//            }
//            if (insCount < instructions.size() && instructions.get(insCount) != null) {
//                last5Instructions[0] = instructions.get(insCount++);
//            } else {
//                last5Instructions[0] = null;
//                insCount++;
//            }
//            Core.Pipeline newPipeline = new Core.Pipeline(this, last5Instructions, pipelines.get(pipelines.size() - 1), clock++);
//            pipelines.add(newPipeline);
//
//            // check end of while
//            int end = 0;
//            for (int i = 0; i < 5; i++) {
//                if (newPipeline.getStages()[i] == null)
//                    end++;
//            }
//            if (end == 5)
//                break;
        }
    }
}
/*
add $t0, $t1, $t2
sub $s0, $s1, $s2
lw $t0, 100($s0)
hello:
sw $5, 100($5)
--------------------
add $t0, $t1, $t2
h:
beq $s0, $s1, End
add $s0, $s0, $t1
slt $s3, $s0, $s1
beq $s3, $t1, h
End:
---------------------
$t1 = 1
$t3 = 3
$t4 = 4
Start:
add $t0, $t0, $t1
beq $t0, $t1, Start
beq $t0, $t1, Start
add $t2, $t2 ,$t1
beq $t2, $t1, Start
beq $t0, $t3, End
beq $t2, $t3, End
End:
add $t0, $t0, $t1
beq $t0,$t4,Start
---------------
Statr:
first:
hello:add $t0, $t1, $t2
beq $t0, $t3, first
beq $t0,$zero,End
add $t0, $t5, $t6
End:
------------------
// Forwarding - branch not happen
Start:
add $t0, $t1,$t2
beq $t0, $zero, Start
sub $s0, $t2, $t1
add $s1, $t3, $t4
// No Forwaridng
Start:
add $t0, $t0,$t2
beq $t0, $t2, Start
sub $s0, $t2, $t1
add $s1, $t3, $t4
--------------------
lw $2, 20($1)
and $4, $2, $5
or $8, $2, $6
and $9, $4, $2
 */


