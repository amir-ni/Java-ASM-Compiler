//package Core;
//
//public class ExampleFrame {
//    public static void main(String[] args) {
////        try {
////            frame frame = new frame();
////        } catch (IllegalRegisterNumberException e) {
////            e.printStackTrace();
////        } catch (SymbolNotFoundException e) {
////            e.printStackTrace();
////        } catch (UnformattedInstructionException e) {
////            e.printStackTrace();
////        } catch (UndefindInstructionException e) {
////            e.printStackTrace();
////        }
////        try {
////            PipelinePanels pipelinePanels = new PipelinePanels("Start:\n" +
////                    "add $t0, $t0,$t2\n" +
////                    "beq $t0, $t2, Start\n" +
////                    "sub $s0, $t2, $t1\n" +
////                    "add $s1, $t3, $t4");
////        }catch (Exception e){
////            e.printStackTrace();
////        }
//
//    }
//}
//
////class frame extends JFrame {
////
////
////    public frame() throws IllegalRegisterNumberException, SymbolNotFoundException, UnformattedInstructionException, UndefindInstructionException {
////        Core core = new Core("Start:\n" +
////                "add $t0, $t0,$t2\n" +
////                "beq $t0, $t2, Start\n" +
////                "sub $s0, $t2, $t1\n" +
////                "add $s1, $t3, $t4");
////
////
////        setSize(1000,700);
////        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
////        setResizable(false);
////        Single_Clock_Cycle_Panel panel = new Single_Clock_Cycle_Panel(core);
////        setContentPane(panel);
//////        panel.setLayout(null);
////
////        core.showGraphical(panel,0);
////        setVisible(true);
////    }
////}
