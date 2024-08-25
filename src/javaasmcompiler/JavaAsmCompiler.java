package javaasmcompiler;

import javax.swing.JTextPane;

public class JavaAsmCompiler extends JTextPane {

    private static final long serialVersionUID = 1L;
    public final static String NAME = "Java ASM Compiler";
    public final static double VERSION = 1.0;
    public final static String Font = "Century Gothic";
    public final static int FontSize = 12;
    public static void main(String[] args) {
        new UI().setVisible(true);
    }

}
