package javaasmcompiler;

import javax.swing.*;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Objects;

public class Find extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;
    int startIndex;
    int select_start = -1;
    JLabel lab1, lab2;
    JTextField textF, textR;
    JButton findBtn, findNext, replace, replaceAll, cancel;
    private CodeArea txt;

    public Find(CodeArea text) {
        txt = text;

        lab1 = new JLabel("Find:");
        lab2 = new JLabel("Replace:");
        textF = new JTextField(30);
        textR = new JTextField(30);
        findBtn = new JButton("Find");
        findNext = new JButton("Find Next");
        replace = new JButton("Replace");
        replaceAll = new JButton("Replace All");
        cancel = new JButton("Cancel");

        setLayout(null);

        int labWidth = 80;
        int labHeight = 20;

        lab1.setBounds(10, 10, labWidth, labHeight);
        add(lab1);
        textF.setBounds(10 + labWidth, 10, 120, 20);
        add(textF);
        lab2.setBounds(10, 10 + labHeight + 10, labWidth, labHeight);
        add(lab2);
        textR.setBounds(10 + labWidth, 10 + labHeight + 10, 120, 20);
        add(textR);

        findBtn.setBounds(225, 6, 115, 20);
        add(findBtn);
        findBtn.addActionListener(this);

        findNext.setBounds(225, 28, 115, 20);
        add(findNext);
        findNext.addActionListener(this);

        replace.setBounds(225, 50, 115, 20);
        add(replace);
        replace.addActionListener(this);

        replaceAll.setBounds(225, 72, 115, 20);
        add(replaceAll);
        replaceAll.addActionListener(this);

        cancel.setBounds(225, 94, 115, 20);
        add(cancel);
        cancel.addActionListener(this);

        int width = 360;
        int height = 160;

        setSize(width, height);

        setLocationRelativeTo(txt);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public void find() {
        select_start = txt.getText().toLowerCase().indexOf(textF.getText().toLowerCase());
        if (select_start == -1) {
            startIndex = 0;
            JOptionPane.showMessageDialog(null, String.format("Could not find \"%s\"!", textF.getText()));
            return;
        }
        if (select_start == txt.getText().toLowerCase().lastIndexOf(textF.getText().toLowerCase())) {
            startIndex = 0;
        }
        int select_end = select_start + textF.getText().length();
        MutableAttributeSet selWord = new SimpleAttributeSet();

        StyleConstants.setBackground(selWord, Color.RED);
        StyledDocument doc1 = txt   .getStyledDocument();
        doc1.setCharacterAttributes(select_start, select_end-select_start, selWord, false);
    }

    public void findNext() {
        String selection = txt.getSelectedText();
        try {
            selection.equals("");
        } catch (NullPointerException e) {
            selection = textF.getText();
            try {
                selection.equals("");
            } catch (NullPointerException e2) {
                selection = JOptionPane.showInputDialog("Find:");
                textF.setText(selection);
            }
        }
        try {
            int select_start = txt.getText().toLowerCase().indexOf(selection.toLowerCase(), startIndex);
            int select_end = select_start + selection.length();
            txt.select(select_start, select_end);
            startIndex = select_end + 1;

            if (select_start == txt.getText().toLowerCase().lastIndexOf(selection.toLowerCase())) {
                startIndex = 0;
            }
        } catch (NullPointerException e) {
        }
    }

    public void replace() {
        try {
            find();
            if (select_start != -1)
                txt.replaceSelection(textR.getText());
        } catch (NullPointerException e) {
            System.out.printf("Null Pointer Exception: %s", e);
        }
    }

    public void replaceAll() {
        txt.setText(txt.getText().toLowerCase().replaceAll(textF.getText().toLowerCase(), textR.getText()));
    }

    public void actionPerformed(ActionEvent e) {
        if (Objects.equals(e.getSource(), findBtn)) find();
        else if (Objects.equals(e.getSource(), findNext)) findNext();
        else if (Objects.equals(e.getSource(), replace)) replace();
        else if (Objects.equals(e.getSource(), replaceAll)) replaceAll();
        else if (Objects.equals(e.getSource(), cancel)) setVisible(false);
    }

}