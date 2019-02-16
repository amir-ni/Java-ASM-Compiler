package javaasmcompiler;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

public class CodeArea extends JTextPane {

    String[] redCommands = { "add", "sub", "and", "or", "nor", "slt", "beq", "lw", "sw" };
    private String[] blueCommands = { "\\$zero", "\\$0", "\\$at", "\\$1", "\\$v0", "\\$2", "\\$v1", "\\$3", "\\$a0",
            "\\$4", "\\$a1", "\\$5", "\\$a2", "\\$6", "\\$a3", "\\$7", "\\$t0", "\\$8", "\\$t1", "\\$9", "\\$t2",
            "\\$10", "\\$t3", "\\$11", "\\$t4", "\\$12", "\\$t5", "\\$13", "\\$t6", "\\$14", "\\$t7", "\\$15", "\\$s0",
            "\\$16", "\\$s1", "\\$17", "\\$s2", "\\$18", "\\$s3", "\\$19", "\\$s4", "\\$20", "\\$s5", "\\$21", "\\$s6",
            "\\$22", "\\$s7", "\\$23", "\\$t8", "\\$24", "\\$t9", "\\$25", "\\$k0", "\\$26", "\\$k1", "\\$27", "\\$gp",
            "\\$28", "\\$sp", "\\$29", "\\$fp", "\\$30", "\\$ra", "\\$31" };

    public CodeArea() {
        final StyleContext cont = StyleContext.getDefaultStyleContext();
        final AttributeSet attrRed = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.RED);
        final AttributeSet attrBlue = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.BLUE);
        final AttributeSet attrBlack = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.BLACK);
        DefaultStyledDocument doc = new DefaultStyledDocument() {
            public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
                super.insertString(offset, str, a);

                String text = getText(0, getLength());
                int before = findLastNonWordChar(text, offset);
                if (before < 0)
                    before = 0;
                int after = findFirstNonWordChar(text, offset + str.length());
                int wordL = before;
                int wordR = before;

                while (wordR <= after) {
                    if (wordR == after || String.valueOf(text.charAt(wordR)).matches("\\W")) {
                        if (text.substring(wordL, wordR).matches(String.format("(\\W)*(%s)", String.join("|", redCommands))))
                            setCharacterAttributes(wordL, wordR - wordL, attrRed, false);
                        else if (text.substring(wordL, wordR).matches(String.format("(\\W)*(%s)", String.join("|", blueCommands))))
                            setCharacterAttributes(wordL, wordR - wordL, attrBlue, false);
                        else
                            setCharacterAttributes(wordL, wordR - wordL, attrBlack, false);
                        wordL = wordR;
                    }
                    wordR++;
                }
            }

            public void remove(int offs, int len) throws BadLocationException {
                super.remove(offs, len);

                String text = getText(0, getLength());
                int before = findLastNonWordChar(text, offs);
                if (before < 0)
                    before = 0;
                int after = findFirstNonWordChar(text, offs);

                if (text.substring(before, after).matches(String.format("(\\W)*(%s)", String.join("|", redCommands))))
                    setCharacterAttributes(before, after - before, attrRed, false);
                else if (text.substring(before, after).matches(String.format("(\\W)*(%s)", String.join("|", blueCommands))))
                    setCharacterAttributes(before, after - before, attrBlue, false);
                else setCharacterAttributes(before, after - before, attrBlack, false);
            }
        };
        setStyledDocument(doc);
    }

    private int findLastNonWordChar(String text, int index) {
        while (--index >= 0) if (String.valueOf(text.charAt(index)).matches("\\W")) break;
        return index;
    }

    private int findFirstNonWordChar(String text, int index) {
        while (index < text.length()) {
            if (String.valueOf(text.charAt(index)).matches("\\W")) break;
            index++;
        }
        return index;
    }

}
