//package Core.View;
//
//import javax.swing.*;
//import javax.swing.table.DefaultTableCellRenderer;
//import javax.swing.table.TableModel;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//
//public class GraphicalProgram extends JFrame {
//    private JPanel mainPanel;
//    private JPanel textPanel;
//    private JTabbedPane memoriesTabbedPane;
//    private JPanel registersPanel;
//    private JPanel floatingpointRegisters;
//    private JPanel memoryPanel;
//    private JTextField[] registerValueTextFields;
//
//
//    public GraphicalProgram() {
//        mainPanel = new JPanel();
//        setContentPane(mainPanel);
//        setSize(1200, 800);
//        setLayout(null);
//
//        JLabel assemblyLbl = new JLabel("Assembly Code :");
//        assemblyLbl.setFont(new Font(Font.SERIF, Font.PLAIN, 20));
//        assemblyLbl.setBounds(20, 5, 200, 35);
//        mainPanel.add(assemblyLbl);
//
//        textPanel = new JPanel();
//        textPanel.setLayout(null);
//        textPanel.setBounds(20, 35, 5 * getWidth() / 8, 2 * getHeight() / 3);
//        JTextArea assemblyTextArea = new JTextArea();
//        assemblyTextArea.setBounds(0, 0, textPanel.getWidth(), textPanel.getHeight());
//        textPanel.add(assemblyTextArea);
//        add(textPanel);
//
//        memoriesTabbedPane = new JTabbedPane();
//        memoriesTabbedPane.setSize(getWidth() / 3, getHeight() - 80);
//        memoriesTabbedPane.setBounds(getWidth() - memoriesTabbedPane.getWidth() - 20, 20, memoriesTabbedPane.getWidth(), memoriesTabbedPane.getHeight());
//        add(memoriesTabbedPane);
//
//        registersPanel = new JPanel();
//        registersPanel.setLayout(null);
//        registersPanel.setSize(getWidth() / 3, getHeight() - 80);
//
//        TableModel registerTabelModel = new RegisterTableModel();
//        JTable table = new JTable(registerTabelModel);
//        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
//            @Override
//            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
//                final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
//                c.setBackground(row % 2 == 0 ? Color.LIGHT_GRAY : Color.WHITE);
//                if (isSelected)
//                    c.setForeground(Color.BLACK);
//
//                return c;
//            }
//        });
//        JScrollPane registerTableScrollPane = new JScrollPane(table);
//        registerTableScrollPane.setBounds(0, 0, registersPanel.getWidth(), registersPanel.getHeight());
//        registersPanel.add(registerTableScrollPane);
//        memoriesTabbedPane.add("Registers", registersPanel);
//
//        floatingpointRegisters = new JPanel();
//        floatingpointRegisters.setLayout(null);
//        memoriesTabbedPane.add("Floating Point Registers", floatingpointRegisters);
//
//        memoryPanel = new JPanel();
//        memoryPanel.setLayout(null);
//        memoriesTabbedPane.add("Memory", memoryPanel);
//
//        setResizable(false);
//        setJMenuBar(menuBar());
//        setDefaultCloseOperation(EXIT_ON_CLOSE);
//        setVisible(true);
//    }
//
//    private JMenuBar menuBar() {
//        JMenuBar menuBar = new JMenuBar();
//        JMenu fileMenu = new JMenu("File");
//        menuBar.add(fileMenu);
//
//        JMenuItem newItem = new JMenuItem("New");
//        fileMenu.add(newItem);
//        JMenuItem openItem = new JMenuItem("Open");
//        fileMenu.add(openItem);
//        JMenuItem saveItem = new JMenuItem("Save");
//        fileMenu.add(saveItem);
//        fileMenu.addSeparator();
//        JMenuItem exitItem = new JMenuItem("Exit");
//        fileMenu.add(exitItem);
//
//        exitItem.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                System.exit(0);
//            }
//        });
//
//        return menuBar;
//    }
//}
