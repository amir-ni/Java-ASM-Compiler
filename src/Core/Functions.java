//package Core;
//
//import Core.Assembler.Assembler;
//import Core.Exceptions.IllegalRegisterNumberException;
//import Core.Exceptions.SymbolNotFoundException;
//import Core.Exceptions.UndefinedInstructionException;
//import Core.Exceptions.UnformattedInstructionException;
//import Core.Pipeline.Pipeline;
//import Core.Pipeline.Stages.IF_Stage;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Scanner;
//
//public class Functions {
//    public static void main(String[] args) {
//        Scanner scanner = new Scanner(System.in);
//        String instructions = "";
//        while (scanner.hasNextLine()) {
//            instructions += scanner.nextLine() + "\n";
//        }
//        List<String> normalInstructions = normalizeInstructions(instructions);
//        try {
//            Assembler assembler = new Assembler(normalInstructions,null);
////            assembler.runInstructions();
//            assembler.runPipeline();
//            for (Pipeline pipeline : assembler.getPipelines()) {
//                System.out.println("Clock: " + pipeline.getClock());
//                System.out.println("Stage IF : ");
//                if (pipeline.getStages()[0] != null) {
//                    System.out.println("Instruction: " + pipeline.getStages()[0].getInstruction().getBinaryCode());
//                    System.out.println("PC: " + ((IF_Stage) pipeline.getStages()[0]).getPc_register());
//
//                } else {
//                    System.out.println("Instruction: null");
//                }
//                System.out.println("Stage ID : ");
//                if (pipeline.getStages()[1] != null) {
//                    System.out.println("Instruction: " + pipeline.getStages()[1].getInstruction().getBinaryCode());
//                } else {
//                    System.out.println("Instruction: null");
//                }
//                System.out.println("Stage EX : ");
//                if (pipeline.getStages()[2] != null) {
//                    System.out.println("Instruction: " + pipeline.getStages()[2].getInstruction().getBinaryCode());
//                } else {
//                    System.out.println("Instruction: null");
//
//                }
//                System.out.println("Stage MEM : ");
//                if (pipeline.getStages()[3] != null) {
//                    System.out.println("Instruction: " + pipeline.getStages()[3].getInstruction().getBinaryCode());
//                } else {
//                    System.out.println("Instruction: null");
//
//                }
//                System.out.println("Stage WB : ");
//                if (pipeline.getStages()[4] != null) {
//                    System.out.println("Instruction: " + pipeline.getStages()[4].getInstruction().getBinaryCode());
//                } else {
//                    System.out.println("Instruction: null");
//                }
//
//                System.out.println("Registers:");
//                for (int i = 0; i < 32; i++) {
//                    System.out.println("Register " + i + " = " + pipeline.getRegisters()[i]);
//                }
//                System.out.println("=====================================");
//            }
//
//
//        } catch (UndefinedInstructionException e) {
//            System.err.println("Execution terminated with an error:");
//            System.err.println("UndefinedInstructionException at line " + e.getLine() + ": " +
//                    normalInstructions.get(e.getLine() - 1));
//
//        } catch (UnformattedInstructionException e) {
//            System.err.println("Execution terminated with an error:");
//            System.err.println("UnformattedInstructionException at line " + e.getLine() + ": " +
//                    normalInstructions.get(e.getLine() - 1));
//        } catch (IllegalRegisterNumberException e) {
//            System.err.println("Execution terminated with an error:");
//            System.err.println("IllegalRegisterNumberException at line " + e.getLine() + ": " +
//                    normalInstructions.get(e.getLine() - 1));
//        } catch (SymbolNotFoundException e) {
//            System.err.println("Execution terminated with an error:");
//            System.err.println("SymbolNotFoundException at line " + e.getLine() + ": " +
//                    normalInstructions.get(e.getLine() - 1));
//            System.err.println("Label \"" + e.getLabel() + "\" not found in symbol table.");
//        }
//    }
//
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
//}
