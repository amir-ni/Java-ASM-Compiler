package javaasmcompiler;

import Core.Exceptions.IllegalRegisterNumberException;
import Core.Exceptions.SymbolNotFoundException;
import Core.Exceptions.UndefinedInstructionException;
import Core.Exceptions.UnformattedInstructionException;
import Core.View.PipelinePanels;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.text.DefaultEditorKit.CopyAction;
import javax.swing.text.DefaultEditorKit.CutAction;
import javax.swing.text.DefaultEditorKit.PasteAction;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.undo.CannotRedoException;
import java.awt.*;
import java.awt.dnd.*;
import java.awt.event.*;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class UI extends JFrame implements ActionListener {

    public static final String PROCESS_FINISHED_WITH_EXIT_CODE_0 = "Process finished with exit code 0";
    public static final String EXECUTION_TERMINATED_WITH_AN_ERROR_ILLEGAL_REGISTER_NUMBER_EXCEPTION_AT_LINE = "Execution terminated with an error:\nIllegalRegisterNumberException at line ";
    public static final String EXECUTION_TERMINATED_WITH_AN_ERROR_SYMBOL_NOT_FOUND_EXCEPTION_AT_LINE = "Execution terminated with an error:\nSymbolNotFoundException at line ";
    public static final String EXECUTION_TERMINATED_WITH_AN_ERROR_UNFORMATTED_INSTRUCTION_EXCEPTION_AT_LINE = "Execution terminated with an error:\nUnformattedInstructionException at line ";
    public static final String EXECUTION_TERMINATED_WITH_AN_ERROR_UNDEFINED_INSTRUCTION_EXCEPTION_AT_LINE = "Execution terminated with an error:\nUndefinedInstructionException at line ";
    public static final String NOT_FOUND_IN_SYMBOL_TABLE = "\" not found in symbol table.";
    public static final String LABEL = "\nLabel \"";
    public static final String WORKING_DIRECTORY = "Working directory";
    public static final String ARE_YOU_SURE_TO_CLEAR_THE_TEXT_AREA = "Are you sure to clear the text Area ?";
    public static final String QUESTION = "Question";
    public static final String YES = "Yes";
    public static final String NO = "No";
    private static long serialVersionUID = 1L;

    private CodeArea textArea;
    private final JTextPane consoleLog, outputLog;
    private final ArrayList<AsmCodeArea> codeAreas;
    private final JMenuBar menuBar;
    private final JComboBox fontSize, fontType;
    private final JSplitPane leftSplitPane, rightSplitPane, fullSplitPane;
    private final JMenu menuFile, menuView, menuEdit, menuRun, menuFind, menuAbout;
    private final JMenuItem newFile, openFile, runFile, pipeline, collapseTab, saveFile, saveAsFile, close, redo, undo, cut, copy, paste, clearFile, selectAll, quickFind, aboutMe, aboutSoftware;
    private final JToolBar mainToolbar;
    private final JButton newButton, openButton, collapseButton, saveButton, undoButton, redoButton, runButton, piplineButton, clearButton, quickButton, aboutMeButton, aboutButton, closeButton;
    private final Action selectAllAction;
    private final JPanel consolePanel, outputPanel;
    private final JScrollPane consoleLogScrollPane, outputLogScrollPane;
    private final JTabbedPane filesTabbedPane, memoriesTabbedPane, fileSystemTabbedPane, footBar;
    private final TableModel registerTableModel;
    private final JScrollPane registerTableScrollPane;
    private JTable table;
    private PipelinePanels pipelinePanels;

    private final ImageIcon newIcon = new ImageIcon(getClass().getResource("icons/new.png"));
    private final ImageIcon openIcon = new ImageIcon(getClass().getResource("icons/open.png"));
    private final ImageIcon saveIcon = new ImageIcon(getClass().getResource("icons/save.png"));
    private final ImageIcon redoIcon = new ImageIcon(getClass().getResource("icons/redo.png"));
    private final ImageIcon undoIcon = new ImageIcon(getClass().getResource("icons/undo.png"));
    private final ImageIcon closeIcon = new ImageIcon(getClass().getResource("icons/close.png"));
    private final ImageIcon runIcon = new ImageIcon(getClass().getResource("icons/run.png"));
    private final ImageIcon pipelineIcon = new ImageIcon(getClass().getResource("icons/pipeline.png"));
    private final ImageIcon collapseIcon = new ImageIcon(getClass().getResource("icons/collapse.png"));
    private final ImageIcon clearIcon = new ImageIcon(getClass().getResource("icons/clear.png"));
    private final ImageIcon cutIcon = new ImageIcon(getClass().getResource("icons/cut.png"));
    private final ImageIcon copyIcon = new ImageIcon(getClass().getResource("icons/copy.png"));
    private final ImageIcon pasteIcon = new ImageIcon(getClass().getResource("icons/paste.png"));
    private final ImageIcon selectAllIcon = new ImageIcon(getClass().getResource("icons/select_all.png"));
    private final ImageIcon searchIcon = new ImageIcon(getClass().getResource("icons/search.png"));
    private final ImageIcon aboutMeIcon = new ImageIcon(getClass().getResource("icons/about_me.png"));
    private final ImageIcon aboutIcon = new ImageIcon(getClass().getResource("icons/about.png"));

    private int untitledCounter;

    UI() {
        codeAreas = new ArrayList<>();
        ImageIcon image = new ImageIcon(getClass().getResource("icons/app.png"));
        setIconImage(image.getImage());
        setSize(1080, 768);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        String folder = System.getProperty("user.dir");
        DefaultMutableTreeNode top = new DefaultMutableTreeNode(folder);
        File dir = new File(folder);

        for (String file : dir.list(new GenericExtFilter(".asm"))) {
            DefaultMutableTreeNode fileName = new DefaultMutableTreeNode(String.format("%s%s%s", folder, File.separator, file));
            top.add(fileName);
        }

        JTree tree = new JTree(top);
        tree.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                TreePath tp = tree.getPathForLocation(me.getX(), me.getY());
                if (tp != null) try {
                    String fileName = tp.toString().replace("[", "")
                            .replace("]", "");
                    fileName = fileName.split(", ")[1];
                    File openFile = new File(fileName);
                    addNewTextPanel(openFile.getName(), openFile.getPath());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        JScrollPane fileSystemPanel = new JScrollPane(tree);

        fileSystemTabbedPane = new JTabbedPane();
        fileSystemTabbedPane.add(WORKING_DIRECTORY, fileSystemPanel);
        fileSystemTabbedPane.setPreferredSize(new Dimension(280, 320));
        memoriesTabbedPane = new JTabbedPane();

        registerTableModel = new RegisterTableModel();
        table = new JTable(registerTableModel);
        table.getColumnModel().getColumn(0).setMaxWidth(90);
        table.getColumnModel().getColumn(1).setMaxWidth(90);
        table.getColumnModel().getColumn(2).setMaxWidth(90);
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                final Component tableCellRendererComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
                        column);
                tableCellRendererComponent.setBackground(((row % 2) == 0) ? Color.decode("#C8DDF2") : Color.WHITE);
                if (isSelected)
                    tableCellRendererComponent.setForeground(Color.BLACK);
                if (!hasFocus && value.equals(""))
                    setText("0");
                return tableCellRendererComponent;
            }
        });
        registerTableScrollPane = new JScrollPane(table);
        registerTableScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        memoriesTabbedPane.add("Registers", registerTableScrollPane);

        memoriesTabbedPane.setPreferredSize(new Dimension(280, 320));

        leftSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                fileSystemTabbedPane, memoriesTabbedPane);
        leftSplitPane.setOneTouchExpandable(true);


        filesTabbedPane = new JTabbedPane();
        filesTabbedPane.addChangeListener(e -> {
            textArea = codeAreas.get(filesTabbedPane.getSelectedIndex()).textArea;
            setTitle(codeAreas.get(filesTabbedPane.getSelectedIndex()).name + " | " + JavaAsmCompiler.NAME);
        });
        filesTabbedPane.setPreferredSize(new Dimension(760, 800));

        consoleLog = new JTextPane();
        consoleLog.setEditable(false);
        consoleLog.setText("");
        consoleLogScrollPane = new JScrollPane(consoleLog);
        consoleLogScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        consolePanel = new JPanel(new GridLayout(0, 1));
        consolePanel.setPreferredSize(new Dimension(760, 150));
        consolePanel.add(consoleLogScrollPane);

        outputLog = new JTextPane();
        outputLog.setEditable(false);
        outputLog.setText("");
        outputLogScrollPane = new JScrollPane(outputLog);
        outputLogScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        outputPanel = new JPanel(new GridLayout(0, 1));
        outputPanel.setPreferredSize(new Dimension(760, 150));
        outputPanel.add(outputLogScrollPane);

        footBar = new JTabbedPane();
        footBar.setPreferredSize(new Dimension(760, 150));
        footBar.add(consolePanel, "console");
        footBar.add(outputPanel, "output");

        rightSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, filesTabbedPane, footBar);
        rightSplitPane.setOneTouchExpandable(true);
        rightSplitPane.setResizeWeight(0.75);
        rightSplitPane.setOneTouchExpandable(true);

        fullSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftSplitPane, rightSplitPane);
        fullSplitPane.setOneTouchExpandable(true);
        getContentPane().add(fullSplitPane);

        addNewTextPanel("Untitled 0", "null");

        menuFile = new JMenu("File");
        menuEdit = new JMenu("Edit");
        menuView = new JMenu("View");
        menuRun = new JMenu("Run");
        menuFind = new JMenu("Search");
        menuAbout = new JMenu("About");
        newFile = new JMenuItem("New", newIcon);
        openFile = new JMenuItem("Open", openIcon);
        saveFile = new JMenuItem("Save", saveIcon);
        saveAsFile = new JMenuItem("Save As", saveIcon);
        close = new JMenuItem("Close file", closeIcon);
        clearFile = new JMenuItem("Clear", clearIcon);
        runFile = new JMenuItem("Run", runIcon);
        pipeline = new JMenuItem("Pipeline", pipelineIcon);
        collapseTab = new JMenuItem("Collapse", collapseIcon);
        quickFind = new JMenuItem("Quick", searchIcon);
        aboutMe = new JMenuItem("About Us", aboutMeIcon);
        aboutSoftware = new JMenuItem("About Software", aboutIcon);
        undo = new JMenuItem("Undo", undoIcon);
        redo = new JMenuItem("Redo", redoIcon);
        menuBar = new JMenuBar();
        menuBar.add(menuFile);
        menuBar.add(menuEdit);
        menuBar.add(menuView);
        menuBar.add(menuRun);
        menuBar.add(menuFind);
        menuBar.add(menuAbout);
        setJMenuBar(menuBar);

        selectAllAction = new SelectAllAction("Select All", clearIcon, "Select all text", KeyEvent.VK_A);

        newFile.addActionListener(this);
        newFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
        menuFile.add(newFile);

        openFile.addActionListener(this);
        openFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
        menuFile.add(openFile);

        saveFile.addActionListener(this);
        saveFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
        menuFile.add(saveFile);

        saveAsFile.addActionListener(this);
        saveAsFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.SHIFT_MASK | InputEvent.CTRL_MASK));
        menuFile.add(saveAsFile);

        close.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_MASK));
        close.addActionListener(this);
        menuFile.add(close);

        undo.addActionListener(this);
        undo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK));
        menuEdit.add(undo);

        redo.addActionListener(this);
        redo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK & InputEvent.SHIFT_MASK));
        menuEdit.add(redo);

        selectAll = new JMenuItem(selectAllAction);
        selectAll.setText("Select All");
        selectAll.setIcon(selectAllIcon);
        selectAll.setToolTipText("Select All");
        selectAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK));
        menuEdit.add(selectAll);

        clearFile.addActionListener(this);
        clearFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_K, InputEvent.CTRL_MASK));
        menuEdit.add(clearFile);

        cut = new JMenuItem(new CutAction());
        cut.setText("Cut");
        cut.setIcon(cutIcon);
        cut.setToolTipText("Cut");
        cut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK));
        menuEdit.add(cut);

        copy = new JMenuItem(new CopyAction());
        copy.setText("Copy");
        copy.setIcon(copyIcon);
        copy.setToolTipText("Copy");
        copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));
        menuEdit.add(copy);

        paste = new JMenuItem(new PasteAction());
        paste.setText("Paste");
        paste.setIcon(pasteIcon);
        paste.setToolTipText("Paste");
        paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK));
        menuEdit.add(paste);

        runFile.addActionListener(this);
        runFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK));
        menuRun.add(runFile);

        pipeline.addActionListener(this);
        pipeline.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK));
        menuRun.add(pipeline);

        collapseTab.addActionListener(this);
        collapseTab.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, InputEvent.CTRL_MASK));
        menuView.add(collapseTab);

        quickFind.addActionListener(this);
        quickFind.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_MASK));
        menuFind.add(quickFind);

        aboutMe.addActionListener(this);
        aboutMe.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
        menuAbout.add(aboutMe);

        aboutSoftware.addActionListener(this);
        aboutSoftware.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));
        menuAbout.add(aboutSoftware);

        mainToolbar = new JToolBar();
        add(mainToolbar, BorderLayout.NORTH);

        newButton = new JButton(newIcon);
        newButton.setToolTipText("New");
        newButton.addActionListener(this);
        mainToolbar.add(newButton);
        mainToolbar.addSeparator();

        openButton = new JButton(openIcon);
        openButton.setToolTipText("Open");
        openButton.addActionListener(this);
        mainToolbar.add(openButton);
        mainToolbar.addSeparator();

        saveButton = new JButton(saveIcon);
        saveButton.setToolTipText("Save");
        saveButton.addActionListener(this);
        mainToolbar.add(saveButton);
        mainToolbar.addSeparator();

        undoButton = new JButton(undoIcon);
        undoButton.setToolTipText("Undo");
        undoButton.addActionListener(this);
        mainToolbar.add(undoButton);
        mainToolbar.addSeparator();

        redoButton = new JButton(redoIcon);
        redoButton.setToolTipText("Redo");
        redoButton.addActionListener(this);
        mainToolbar.add(redoButton);
        mainToolbar.addSeparator();

        runButton = new JButton(runIcon);
        runButton.setToolTipText("Run");
        runButton.addActionListener(this);
        mainToolbar.add(runButton);
        mainToolbar.addSeparator();

        piplineButton = new JButton(pipelineIcon);
        piplineButton.setToolTipText("Pipeline");
        piplineButton.addActionListener(this);
        mainToolbar.add(piplineButton);
        mainToolbar.addSeparator();

        collapseButton = new JButton(collapseIcon);
        collapseButton.setToolTipText("Collapse");
        collapseButton.addActionListener(this);
        mainToolbar.add(collapseButton);
        mainToolbar.addSeparator();

        clearButton = new JButton(clearIcon);
        clearButton.setToolTipText("Clear All");
        clearButton.addActionListener(this);
        mainToolbar.add(clearButton);
        mainToolbar.addSeparator();

        quickButton = new JButton(searchIcon);
        quickButton.setToolTipText("Quick Search");
        quickButton.addActionListener(this);
        mainToolbar.add(quickButton);
        mainToolbar.addSeparator();

        aboutMeButton = new JButton(aboutMeIcon);
        aboutMeButton.setToolTipText("About Me");
        aboutMeButton.addActionListener(this);
        mainToolbar.add(aboutMeButton);
        mainToolbar.addSeparator();

        aboutButton = new JButton(aboutIcon);
        aboutButton.setToolTipText("About " + JavaAsmCompiler.NAME);
        aboutButton.addActionListener(this);
        mainToolbar.add(aboutButton);
        mainToolbar.addSeparator();

        closeButton = new JButton(closeIcon);
        closeButton.setToolTipText("Close file");
        closeButton.addActionListener(this);
        mainToolbar.add(closeButton);
        mainToolbar.addSeparator();


        fontType = new JComboBox();

        int ceIndex = 0;
        for (String font : GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()) {
            fontType.addItem(font);
            if (font.equals(JavaAsmCompiler.Font))
                ceIndex = fontType.getItemCount() - 1;
        }
        fontType.setSelectedIndex(ceIndex);
        fontType.setMaximumSize(new Dimension(170, 30));
        fontType.setToolTipText("Font Type");
        mainToolbar.add(fontType);
        mainToolbar.addSeparator();

        fontType.addActionListener(ev -> {
            String p = fontType.getSelectedItem().toString();
            int s = textArea.getFont().getSize();
            textArea.setFont(new Font(p, Font.PLAIN, s));
            codeAreas.get(filesTabbedPane.getSelectedIndex()).lineNumberingTextArea.setFont(new Font(p, Font.PLAIN, s));
        });

        fontSize = new JComboBox();

        for (int i = 5; i <= 100; i++)
            fontSize.addItem(i);

        fontSize.setSelectedIndex(JavaAsmCompiler.FontSize - 5);
        fontSize.setMaximumSize(new Dimension(70, 30));
        fontSize.setToolTipText("Font Size");
        mainToolbar.add(fontSize);

        fontSize.addActionListener(ev -> {
            String sizeValue = fontSize.getSelectedItem().toString();
            int sizeOfFont = Integer.parseInt(sizeValue);
            String fontFamily = textArea.getFont().getFamily();

            Font font1 = new Font(fontFamily, Font.PLAIN, sizeOfFont);
            textArea.setFont(font1);
            codeAreas.get(filesTabbedPane.getSelectedIndex()).lineNumberingTextArea.setFont(font1);

        });
    }

    @Override
    protected void processWindowEvent(WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            saveOrExit();
            System.exit(0);
        }
    }

    private void closeCodeAreaTab() {
        codeAreas.remove(filesTabbedPane.getSelectedIndex());
        if (codeAreas.size() == 0) {
            addNewTextPanel(String.format("Untitled %d", ++untitledCounter), "null");
            filesTabbedPane.remove(0);
        } else {
            filesTabbedPane.remove(filesTabbedPane.getSelectedIndex());
        }
        textArea = codeAreas.get(filesTabbedPane.getSelectedIndex()).textArea;
        setTitle(String.format("%s | %s", codeAreas.get(filesTabbedPane.getSelectedIndex()).name, JavaAsmCompiler.NAME));

    }

    private void showPipeline() {
        pipelinePanels.setVisible(true);
    }

    private void runFile(String assemblyCode, JTable registersTable, JTextPane log) {
        List<String> normalInstructions = Helper.normalizeInstructions(assemblyCode);
        try {
            pipelinePanels = new PipelinePanels(normalInstructions, registersTable);
            log.setText(PROCESS_FINISHED_WITH_EXIT_CODE_0);
        } catch (IllegalRegisterNumberException e) {
            log.setText(String.format("%s%d: %s", EXECUTION_TERMINATED_WITH_AN_ERROR_ILLEGAL_REGISTER_NUMBER_EXCEPTION_AT_LINE, e.getLine(), normalInstructions.get(e.getLine() - 1)));
        } catch (SymbolNotFoundException e) {
            log.setText(String.format("%s%d: %s%s%s%s", EXECUTION_TERMINATED_WITH_AN_ERROR_SYMBOL_NOT_FOUND_EXCEPTION_AT_LINE, e.getLine(), normalInstructions.get(e.getLine() - 1), LABEL, e.getLabel(), NOT_FOUND_IN_SYMBOL_TABLE));
        } catch (UnformattedInstructionException e) {
            log.setText(String.format("%s%d: %s", EXECUTION_TERMINATED_WITH_AN_ERROR_UNFORMATTED_INSTRUCTION_EXCEPTION_AT_LINE, e.getLine(), normalInstructions.get(e.getLine() - 1)));
        } catch (UndefinedInstructionException e) {
            log.setText(String.format("%s%d: %s", EXECUTION_TERMINATED_WITH_AN_ERROR_UNDEFINED_INSTRUCTION_EXCEPTION_AT_LINE, e.getLine(), normalInstructions.get(e.getLine() - 1)));
        }
    }


    private void saveOrExit() {
        if (codeAreas.get(filesTabbedPane.getSelectedIndex()).edit &&
                !(codeAreas.get(filesTabbedPane.getSelectedIndex()).textArea.getText().length() == 0 && codeAreas.get(filesTabbedPane.getSelectedIndex()).filePath.equals("null"))) {
            Object[] options = {"Save and exit", "Exit without saving", "Return"};
            int n = JOptionPane.showOptionDialog(this, "Do you want to save the file ?", QUESTION,
                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
            if (n == 0) {
                if (codeAreas.get(filesTabbedPane.getSelectedIndex()).filePath.equals("null"))
                    saveAsFile();
                else
                    saveFile();
                closeCodeAreaTab();
            } else if (n == 1) {
                closeCodeAreaTab();
            }
        } else {
            closeCodeAreaTab();
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == close || e.getSource() == closeButton) {
            saveOrExit();
        } else if (e.getSource() == newFile || e.getSource() == newButton) {
            addNewTextPanel(String.format("Untitled %d", ++untitledCounter), "null");
        } else if (e.getSource() == openFile || e.getSource() == openButton) {
            JFileChooser open = new JFileChooser();
            int option = open.showOpenDialog(this);
            if (option == JFileChooser.APPROVE_OPTION) try {
                File openFile = open.getSelectedFile();
                addNewTextPanel(openFile.getName(), openFile.getPath());
                Scanner scan = new Scanner(new FileReader(openFile.getPath()));
                while (scan.hasNext()) {
                    textArea.getDocument().insertString(textArea.getDocument().getLength(), scan.nextLine() + "\n", null);
                }
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
            }
        } else if (e.getSource() == saveFile || e.getSource() == saveButton) {
            if (codeAreas.get(filesTabbedPane.getSelectedIndex()).filePath.equals("null"))
                saveAsFile();
            else
                saveFile();
        } else if (e.getSource() == redoButton) {
            try {
                if (codeAreas.get(filesTabbedPane.getSelectedIndex()).undoManager.canRedo())
                    codeAreas.get(filesTabbedPane.getSelectedIndex()).undoManager.redo();
            } catch (CannotRedoException cre) {
                cre.printStackTrace();
            }
        } else if (e.getSource() == undoButton) {
            try {
                if (codeAreas.get(filesTabbedPane.getSelectedIndex()).undoManager.canUndo())
                    codeAreas.get(filesTabbedPane.getSelectedIndex()).undoManager.undo();
            } catch (CannotRedoException cre) {
                cre.printStackTrace();
            }
        } else if (e.getSource() == saveAsFile) {
            saveAsFile();
        } else if (e.getSource() == runFile || e.getSource() == runButton) {
            runFile(codeAreas.get(filesTabbedPane.getSelectedIndex()).textArea.getText(), table, consoleLog);
        } else if (e.getSource() == pipeline || e.getSource() == piplineButton) {
            showPipeline();
        } else if (e.getSource() == collapseTab || e.getSource() == collapseButton) {
            if (fullSplitPane.getComponentCount() > 2) {
                fullSplitPane.remove(leftSplitPane);
                revalidate();
                repaint();
            } else {
                fullSplitPane.setLeftComponent(leftSplitPane);
                revalidate();
                repaint();
                getContentPane().revalidate();
                leftSplitPane.revalidate();
                filesTabbedPane.revalidate();
                memoriesTabbedPane.revalidate();
                getContentPane().repaint();
                leftSplitPane.repaint();
                filesTabbedPane.repaint();
                memoriesTabbedPane.repaint();
            }
        } else if (e.getSource() == clearFile || e.getSource() == clearButton) {
            Object[] options = {YES, NO};
            int n = JOptionPane.showOptionDialog(this, ARE_YOU_SURE_TO_CLEAR_THE_TEXT_AREA, QUESTION,
                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
            if (n == 0) {
                textArea.setText("");
            }
        } else if (e.getSource() == quickFind || e.getSource() == quickButton) {
            new Find(textArea);
        } else if (e.getSource() == aboutMe || e.getSource() == aboutMeButton) {
            new About(this).me();
        } else if (e.getSource() == aboutSoftware || e.getSource() == aboutButton) {
            new About(this).software();
        }
    }

    class SelectAllAction extends AbstractAction {

        private static final long serialVersionUID = 1L;

        SelectAllAction(String text, ImageIcon icon, String desc, Integer mnemonic) {
            super(text, icon);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(MNEMONIC_KEY, mnemonic);
        }

        public void actionPerformed(ActionEvent e) {
            textArea.selectAll();
        }
    }

    private void saveFile() {
        try {
            File openFile = new File(codeAreas.get(filesTabbedPane.getSelectedIndex()).filePath);
            BufferedWriter out = new BufferedWriter(new FileWriter(openFile.getPath()));
            out.write(textArea.getText());
            out.close();
            codeAreas.get(filesTabbedPane.getSelectedIndex()).edit = false;
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }

    private void saveAsFile() {
        JFileChooser fileChoose = new JFileChooser();
        int option = fileChoose.showSaveDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) try {
            File openFile = fileChoose.getSelectedFile();
            setTitle(String.format("%s | %s", openFile.getName(), JavaAsmCompiler.NAME));

            BufferedWriter out = new BufferedWriter(new FileWriter(openFile.getPath()));
            out.write(textArea.getText());
            out.close();
            codeAreas.get(filesTabbedPane.getSelectedIndex()).edit = false;
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }

    public AsmCodeArea addNewTextPanel(String name, String path){
        for (int i = 0; i < 31; i++)
            table.setValueAt(0,i,2);

        table.setValueAt(16711680, 29,2);
        table.setValueAt(16,32,2);
        AsmCodeArea asmCodeArea = new AsmCodeArea(name,path);
        new DropTarget(asmCodeArea.textArea, new CustomDropTargetListener(this));
        textArea = asmCodeArea.textArea;
        codeAreas.add(asmCodeArea);
        filesTabbedPane.add(asmCodeArea.name, asmCodeArea);
        filesTabbedPane.setSelectedIndex(codeAreas.size() - 1);
        setTitle(String.format("%s | %s", codeAreas.get(filesTabbedPane.getSelectedIndex()).name, JavaAsmCompiler.NAME));
        return asmCodeArea;
    }



}
