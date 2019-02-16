package javaasmcompiler;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.text.DefaultEditorKit;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AsmCodeArea extends JPanel {
        UndoManager undoManager;
        CodeArea textArea;
        JLabel stats;
        boolean edit = false;
        String name;
        LineNumberingTextArea lineNumberingTextArea;
        String filePath;

        AsmCodeArea(String name, String path) {


            this.filePath = path;
            this.name = name;
            textArea = (new CodeArea());
            textArea.setFont(new Font(JavaAsmCompiler.Font, Font.PLAIN, JavaAsmCompiler.FontSize));
            stats = new JLabel();
            stats.setText("Assembly language source file           |    length: " + textArea.getText().length() + "     lines: " + textArea.getText().split("\n").length);
            textArea.addKeyListener(new KeyAdapter() {
                public void keyPressed(KeyEvent ke) {
                    if (!ke.isControlDown() & !ke.isAltDown() & !ke.isShiftDown()) {
                        AsmCodeArea.this.edit = true;
                    }
                }
            });
            lineNumberingTextArea = new LineNumberingTextArea(textArea);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setRowHeaderView(lineNumberingTextArea);
            lineNumberingTextArea.updateLineNumbers();
            textArea.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent documentEvent) {
                    lineNumberingTextArea.updateLineNumbers();
                    stats.setText("Assembly language source file           |    length: " + textArea.getText().length() + "     lines: " + textArea.getText().split("\n").length);
                }

                @Override
                public void removeUpdate(DocumentEvent documentEvent) {
                    lineNumberingTextArea.updateLineNumbers();
                    stats.setText("Assembly language source file           |    length: " + textArea.getText().length() + "     lines: " + textArea.getText().split("\n").length);
                }

                @Override
                public void changedUpdate(DocumentEvent documentEvent) {
                    lineNumberingTextArea.updateLineNumbers();
                    stats.setText("Assembly language source file           |    length: " + textArea.getText().length() + "     lines: " + textArea.getText().split("\n").length);
                }
            });
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

            setLayout(new BorderLayout());
            add(scrollPane);
            JPanel jj = new JPanel(new BorderLayout());
            jj.setBackground(Color.GREEN);
            jj.add(stats, BorderLayout.LINE_START);
            add(jj, BorderLayout.PAGE_END);
            if (!path.equals("null")) {
                try {
                    Scanner scan = new Scanner(new FileReader(path));
                    while (scan.hasNext())
                        textArea.getDocument().insertString(textArea.getDocument().getLength(), scan.nextLine() + "\n", null);
                } catch (Exception ex) {
                    System.err.println(ex.getMessage());
                }
            }
            undoManager = new UndoManager();
            textArea.getDocument().addUndoableEditListener(e -> undoManager.addEdit(e.getEdit()));

        }


    }