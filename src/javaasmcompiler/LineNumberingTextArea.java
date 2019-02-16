package javaasmcompiler;

import javax.swing.*;
import javax.swing.text.Element;
import java.awt.*;

public class LineNumberingTextArea extends JEditorPane
{
    private JEditorPane textArea;

    public LineNumberingTextArea(JEditorPane textArea)
    {
        this.textArea = textArea;
        setEditable(false);
        setBackground(Color.LIGHT_GRAY);
        setPreferredSize(new Dimension(40,getHeight()));
        setMinimumSize(new Dimension(40,getHeight()));
        setMaximumSize(new Dimension(40,getHeight()));
        setSize(new Dimension(40,getHeight()));
    }

    public void updateLineNumbers()
    {
        String lineNumbersText = getLineNumbersText();
        setText(lineNumbersText);
    }

    private String getLineNumbersText()
    {
        int caretPosition = textArea.getDocument().getLength();
        Element root = textArea.getDocument().getDefaultRootElement();
        StringBuilder lineNumbersTextBuilder = new StringBuilder();
        lineNumbersTextBuilder.append("1").append(System.lineSeparator());

        for (int elementIndex = 2; elementIndex < (root.getElementIndex(caretPosition) + 2); elementIndex++)
        {
            lineNumbersTextBuilder.append(elementIndex).append(System.lineSeparator());
        }

        return lineNumbersTextBuilder.toString();
    }
}