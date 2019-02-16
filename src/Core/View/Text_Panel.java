package Core.View;

import Core.Core;
import Core.Images.Images;
import Core.View.TableModels.*;
import Core.util.Binary;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Text_Panel extends JPanel {
    private Core core;

    public Text_Panel(Core core) {
        this.core = core;
        setLayout(null);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(null);
        bottomPanel.setBounds(0, 600, 1000, 100);
        add(bottomPanel);

        //BottomPanel
        JButton previousBtn = new JButton();
        JButton nextBtn = new JButton();
        JButton showBtn = new JButton();
        JTextField clockTextField = new JTextField("0");
        JLabel clockLbl = new JLabel("Clock:");
        Icon pre = new ImageIcon(Images.previousIcon);
        previousBtn.setIcon(pre);
        Icon next = new ImageIcon(Images.nextIcon);
        nextBtn.setIcon(next);
        Icon apply = new ImageIcon(Images.applyIcon);
        showBtn.setIcon(apply);
        clockLbl.setFont(new Font(Font.SERIF, Font.PLAIN, 18));
        clockTextField.setFont(new Font(Font.SERIF, Font.PLAIN, 18));
        clockTextField.setHorizontalAlignment(0);
        previousBtn.setBounds(37, 3, 40, 40);
        nextBtn.setBounds(900, 3, 40, 40);
        clockLbl.setBounds(405, 3, 50, 40);
        clockTextField.setBounds(460, 3, 50, 40);
        showBtn.setBounds(515, 3, 40, 40);
        bottomPanel.add(previousBtn);
        bottomPanel.add(nextBtn);
        bottomPanel.add(showBtn);
        bottomPanel.add(clockLbl);
        bottomPanel.add(clockTextField);

        clockTextField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    showBtn.doClick();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        //registerTabel
        JPanel registersPanel = new JPanel();
        registersPanel.setLayout(null);
        registersPanel.setBounds(90, 10, 280, 325);
        registersPanel.setBorder(BorderFactory.createTitledBorder("Registers"));

        TableModel registerTabelModel = new RegisterTableModel();
        JTable registerTable = new JTable(registerTabelModel);
        registerTable.getColumnModel().getColumn(0).setMaxWidth(90);
        registerTable.getColumnModel().getColumn(1).setMaxWidth(90);
        registerTable.getColumnModel().getColumn(2).setMaxWidth(90);
        registerTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
                        column);
                c.setBackground(row % 2 == 0 ? Color.decode("#C8DDF2") : Color.WHITE);
                if (isSelected)
                    c.setForeground(Color.BLACK);

                return c;
            }
        });
        JScrollPane registerTableScrollPane = new JScrollPane(registerTable);
        registerTableScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        registerTableScrollPane.setBounds(5, 20, 270, 300);
        registersPanel.add(registerTableScrollPane);
        add(registersPanel);

        //InstructionTable
        JPanel instructionPanel = new JPanel();
        instructionPanel.setLayout(null);
        instructionPanel.setBounds(420, 10, 470, 125);
        instructionPanel.setBorder(BorderFactory.createTitledBorder("Stages Instructions"));

        TableModel instructionModel = new InstructionTableModel();
        JTable instructionTable = new JTable(instructionModel);

        instructionTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
                        column);
                c.setBackground(row % 2 == 0 ? Color.decode("#C8DDF2") : Color.WHITE);
                if (isSelected)
                    c.setForeground(Color.BLACK);

                return c;
            }
        });
        JScrollPane instructionScrollPanel = new JScrollPane(instructionTable);
        instructionScrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        instructionTable.getColumnModel().getColumn(0).setMaxWidth(45);
        instructionTable.getColumnModel().getColumn(1).setMaxWidth(130);
        instructionTable.getColumnModel().getColumn(1).setMinWidth(130);
        instructionTable.getColumnModel().getColumn(2).setMaxWidth(270);
        instructionScrollPanel.setBounds(5, 20, 460, 100);
        instructionPanel.add(instructionScrollPanel);
        add(instructionPanel);

        // Register File Table
        JPanel registerFilePanel = new JPanel();
        registerFilePanel.setLayout(null);
        registerFilePanel.setBounds(420, 137, 470, 110);
        registerFilePanel.setBorder(BorderFactory.createTitledBorder("Register File"));

        TableModel registerFileModel = new RegisterFileModel();
        JTable registerFileTable = new JTable(registerFileModel);
        registerFileTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
                        column);
                c.setBackground(row % 2 == 0 ? Color.decode("#C8DDF2") : Color.WHITE);
                if (isSelected)
                    c.setForeground(Color.BLACK);

                return c;
            }
        });
        JScrollPane registerFileScrollPanel = new JScrollPane(registerFileTable);
        registerFileScrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        registerFileTable.getColumnModel().getColumn(0).setMaxWidth(120);
        registerFileTable.getColumnModel().getColumn(1).setMaxWidth(55);
        registerFileTable.getColumnModel().getColumn(2).setMaxWidth(270);
        registerFileScrollPanel.setBounds(5, 20, 460, 85);
        registerFilePanel.add(registerFileScrollPanel);
        add(registerFilePanel);

        // MemoryTable
        JPanel memoryPanel = new JPanel();
        memoryPanel.setLayout(null);
        memoryPanel.setBounds(420, 250, 470, 93);
        memoryPanel.setBorder(BorderFactory.createTitledBorder("Memory"));

        TableModel memoryModel = new MemoryModel();
        JTable memoryTable = new JTable(memoryModel);
        memoryTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
                        column);
                c.setBackground(row % 2 == 0 ? Color.decode("#C8DDF2") : Color.WHITE);
                if (isSelected)
                    c.setForeground(Color.BLACK);

                return c;
            }
        });
        JScrollPane memoryScrollPanel = new JScrollPane(memoryTable);
        memoryScrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        memoryTable.getColumnModel().getColumn(0).setMaxWidth(120);
        memoryTable.getColumnModel().getColumn(1).setMaxWidth(55);
        memoryTable.getColumnModel().getColumn(2).setMaxWidth(270);
        memoryScrollPanel.setBounds(5, 20, 460, 68);
        memoryPanel.add(memoryScrollPanel);
        add(memoryPanel);

        // ALU
        JPanel aluPanel = new JPanel();
        aluPanel.setLayout(null);
        aluPanel.setBounds(420, 350, 470, 93);
        aluPanel.setBorder(BorderFactory.createTitledBorder("ALUs"));

        TableModel aluModel = new ALUModel();
        JTable aluTable = new JTable(aluModel);
        aluTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
                        column);
                c.setBackground(row % 2 == 0 ? Color.decode("#C8DDF2") : Color.WHITE);
                if (isSelected)
                    c.setForeground(Color.BLACK);

                return c;
            }
        });
        JScrollPane aluScrollPanel = new JScrollPane(aluTable);
        aluScrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        aluTable.getColumnModel().getColumn(0).setMaxWidth(120);
        aluTable.getColumnModel().getColumn(1).setMaxWidth(120);
        aluTable.getColumnModel().getColumn(2).setMaxWidth(120);
        aluTable.getColumnModel().getColumn(3).setMaxWidth(120);
        aluScrollPanel.setBounds(5, 20, 460, 68);
        aluPanel.add(aluScrollPanel);
        add(aluPanel);

        // Controls
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(null);
        controlPanel.setBounds(90, 340, 280, 253);
        controlPanel.setBorder(BorderFactory.createTitledBorder("Control Lines"));

        TableModel controlModel = new ControlModel();
        JTable controlTable = new JTable(controlModel);
        controlTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
                        column);
                c.setBackground(row % 2 == 0 ? Color.decode("#C8DDF2") : Color.WHITE);
                if (isSelected)
                    c.setForeground(Color.BLACK);

                return c;
            }
        });
        JScrollPane controlScrollPanel = new JScrollPane(controlTable);
        controlScrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        controlTable.getColumnModel().getColumn(0).setMaxWidth(125);
        controlTable.getColumnModel().getColumn(1).setMaxWidth(130);
        controlScrollPanel.setBounds(5, 20, 270, 228);
        controlPanel.add(controlScrollPanel);
        add(controlPanel);

        // PipelineRegisters
        JPanel prPanel = new JPanel();
        prPanel.setLayout(null);
        prPanel.setBounds(420, 450, 470, 145);
        prPanel.setBorder(BorderFactory.createTitledBorder("Pipeline Registers Details"));

        TableModel prModel = new PRModel();
        JTable prTable = new JTable(prModel);
        prTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
                        column);
                c.setBackground(row % 2 == 0 ? Color.decode("#C8DDF2") : Color.WHITE);
                if (isSelected)
                    c.setForeground(Color.BLACK);

                return c;
            }
        });
        JScrollPane prScrollPanel = new JScrollPane(prTable);
        prScrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        prTable.getColumnModel().getColumn(0).setMaxWidth(125);
        prTable.getColumnModel().getColumn(1).setMaxWidth(55);
        prTable.getColumnModel().getColumn(2).setMaxWidth(270);
        prScrollPanel.setBounds(5, 20, 460, 120);
        prPanel.add(prScrollPanel);
        add(prPanel);


        core.setDetails(0);
        // register table
        for (int i = 0; i < 32; i++) {
            registerTable.setValueAt(core.getRegisters()[i], i, 2);
        }
        registerTable.setValueAt(16, 32, 2);



        previousBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Integer.parseInt(clockTextField.getText()) > 0) {
                    clockTextField.setText(Integer.toString(Integer.parseInt(clockTextField.getText()) - 1));
                    showBtn.doClick();
                }
            }
        });

        nextBtn.addActionListener(e -> {
            if (Integer.parseInt(clockTextField.getText()) + 1 < core.getAssembler().getPipelines().size()) {
                clockTextField.setText(Integer.toString(Integer.parseInt(clockTextField.getText()) + 1));
                showBtn.doClick();
            }
        });

        showBtn.addActionListener(e -> {
            core.setDetails(Integer.parseInt(clockTextField.getText()));

            // register table
            for (int i = 0; i < 32; i++) {
                registerTable.setValueAt(core.getRegisters()[i], i, 2);
            }
            registerTable.setValueAt(core.getPc_register(), 32, 2);

            // instruction table
            instructionTable.setValueAt(core.getInstruction_IF(), 0, 1);
            instructionTable.setValueAt(core.getInstructionBinary_IF(), 0, 2);
            instructionTable.setValueAt(core.getInstruction_ID(), 1, 1);
            instructionTable.setValueAt(core.getInstructionBinary_ID(), 1, 2);
            instructionTable.setValueAt(core.getInstruction_EX(), 2, 1);
            instructionTable.setValueAt(core.getInstructionBinary_EX(), 2, 2);
            instructionTable.setValueAt(core.getInstruction_MEM(), 3, 1);
            instructionTable.setValueAt(core.getInstructionBinary_MEM(), 3, 2);
            instructionTable.setValueAt(core.getInstruction_WB(), 4, 1);
            instructionTable.setValueAt(core.getInstructionBinary_WB(), 4, 2);

            // IF

            controlTable.setValueAt(core.getPcSrc(), 0, 1);

            if (core.isIf_available()) {
                aluTable.setValueAt(core.getPc_IF(), 0, 1);
                aluTable.setValueAt(4, 0, 2);
                aluTable.setValueAt(core.getPc_IF() + 4, 0, 3);
                prTable.setValueAt(core.getInstruction_IF_ID(), 1, 2);
                prTable.setValueAt(core.getPc_4_IF_ID(), 0, 2);
            } else {
                aluTable.setValueAt("-", 0, 1);
                aluTable.setValueAt("-", 0, 2);
                aluTable.setValueAt("-", 0, 3);
                prTable.setValueAt("-",0,2);
                prTable.setValueAt("-",1,2);
            }
            // ID
            if (core.isId_available()) {
                registerFileTable.setValueAt(core.getRegister1(), 0, 1);
                registerFileTable.setValueAt(Binary.intToBinaryString(core.getRegister1(), 32), 0, 2);
                registerFileTable.setValueAt(core.getRegister2(), 1, 1);
                registerFileTable.setValueAt(Binary.intToBinaryString(core.getRegister2(), 32), 1, 2);
                registerFileTable.setValueAt(core.getReadData1(), 2, 1);
                registerFileTable.setValueAt(Binary.intToBinaryString(core.getReadData1(), 32), 2, 2);
                registerFileTable.setValueAt(core.getReadData2(), 3, 1);
                registerFileTable.setValueAt(Binary.intToBinaryString(core.getReadData2(), 32), 3, 2);

                prTable.setValueAt(core.getPc_4_id_ex(), 2, 2);
                prTable.setValueAt(core.getRegisterNumber1_id_ex(), 3, 2);
                prTable.setValueAt(core.getRegisterNumber2_id_ex(), 4, 2);
                prTable.setValueAt(core.getReadData1_id_ex(), 5, 2);
                prTable.setValueAt(core.getReadData2_id_ex(), 6, 2);
                prTable.setValueAt(core.getRt_id_ex(), 7, 2);
                prTable.setValueAt(core.getRd_id_ex(), 8, 2);
                prTable.setValueAt(core.getOffset_id_ex(), 9, 2);


                controlTable.setValueAt(core.isStalled() ? 1 : 0, 2, 1);
            } else {
                registerFileTable.setValueAt("-", 0, 1);
                registerFileTable.setValueAt("-", 0, 2);
                registerFileTable.setValueAt("-", 1, 1);
                registerFileTable.setValueAt("-", 1, 2);
                registerFileTable.setValueAt("-", 2, 1);
                registerFileTable.setValueAt("-", 2, 2);
                registerFileTable.setValueAt("-", 3, 1);
                registerFileTable.setValueAt("-", 3, 2);

                prTable.setValueAt("-", 2, 2);
                prTable.setValueAt("-", 3, 2);
                prTable.setValueAt("-", 4, 2);
                prTable.setValueAt("-", 5, 2);
                prTable.setValueAt("-", 6, 2);
                prTable.setValueAt("-", 7, 2);
                prTable.setValueAt("-", 8, 2);
                prTable.setValueAt("-", 9, 2);

                controlTable.setValueAt("-", 2, 1);

            }

            //EX
            if (core.isEx_available()) {
                aluTable.setValueAt(core.getAluInput1(), 1, 1);
                aluTable.setValueAt(core.getAluInput2(), 1, 2);
                aluTable.setValueAt(core.getAluResult(), 1, 3);
                aluTable.setValueAt(core.getBranchAluInput1(), 2, 1);
                aluTable.setValueAt(core.getBranchAluInput2(), 2, 2);
                aluTable.setValueAt(core.getBranchOutput(), 2, 3);
                prTable.setValueAt(core.getBranchTarget_ex_mem(), 10, 2);
                if (core.isZero_ex_mem())
                    prTable.setValueAt(1, 11, 2);
                else
                    prTable.setValueAt(0, 11, 2);
                prTable.setValueAt(core.getAluResult(), 12, 2);
                prTable.setValueAt(core.getReadData2_ex_mem(), 13, 2);
                if (core.getDestinationRegister_ex_mem() != -1)
                    prTable.setValueAt(core.getDestinationRegister_ex_mem(), 14, 2);
                else
                    prTable.setValueAt("-", 14, 2);
                controlTable.setValueAt(core.getForwardA(), 6, 1);
                controlTable.setValueAt(core.getForwardB(), 7, 1);
                if (core.getAluSrc() == -1)
                    controlTable.setValueAt("X", 3, 1);
                else
                    controlTable.setValueAt(core.getAluSrc(), 3, 1);
                controlTable.setValueAt(core.getAluOp(), 4, 1);
                if (core.getRegDst() == -1)
                    controlTable.setValueAt("X", 5, 1);
                else
                    controlTable.setValueAt(core.getRegDst(), 5, 1);
                if (core.isBranch_mem())
                    controlTable.setValueAt(1, 8, 1);
                else
                    controlTable.setValueAt(0, 8, 1);
            }else{
                controlTable.setValueAt("-",3,1);
                controlTable.setValueAt("--",4,1);
                controlTable.setValueAt("-",5,1);
                controlTable.setValueAt("--",6,1);
                controlTable.setValueAt("--",7,1);
                controlTable.setValueAt("-",8,1);
                controlTable.setValueAt("-",9,1);
                controlTable.setValueAt("-",10,1);
                controlTable.setValueAt("-",11,1);
                controlTable.setValueAt("-",12,1);
                aluTable.setValueAt("-",1,1);
                aluTable.setValueAt("-",1,2);
                aluTable.setValueAt("-",1,3);
                aluTable.setValueAt("-",2,1);
                aluTable.setValueAt("-",2,2);
                aluTable.setValueAt("-",2,3);
                prTable.setValueAt("-",10,2);
                prTable.setValueAt("-",11,2);
                prTable.setValueAt("-",12,2);
                prTable.setValueAt("-",13,2);
                prTable.setValueAt("-",14,2);

            }

            //MEM
            if (core.isMem_available()) {
                prTable.setValueAt(core.getReadDataFromMem_mem_wb(), 15, 2);
                prTable.setValueAt(core.getAluResult_mem_wb(), 16, 2);
                if (core.getDestinationRegister_mem_wb() != -1)
                    prTable.setValueAt(core.getDestinationRegister_mem_wb(), 17, 2);
                else
                    prTable.setValueAt("-", 17, 2);
                memoryTable.setValueAt(core.getAddress_mem(), 0, 1);
                memoryTable.setValueAt(Binary.intToBinaryString((int) core.getAddress_mem(), 32), 0, 2);
                if (core.isMemRead_mem()) {
                    memoryTable.setValueAt(core.getReadDataFromMem_mem(), 1, 1);
                    memoryTable.setValueAt(Binary.intToBinaryString(core.getReadDataFromMem_mem(), 32), 1, 2);
                } else {
                    memoryTable.setValueAt("-", 1, 1);
                    memoryTable.setValueAt("-", 1, 2);
                }
                memoryTable.setValueAt(core.getWriteData_mem(), 2, 1);
                memoryTable.setValueAt(Binary.intToBinaryString(core.getWriteData_mem(), 32), 2, 2);
                if (core.isMemRead_mem())
                    controlTable.setValueAt(1, 9, 1);
                else
                    controlTable.setValueAt(0, 9, 1);
                if (core.isMemWrite_mem())
                    controlTable.setValueAt(1, 10, 1);
                else
                    controlTable.setValueAt(0, 10, 1);
            } else {
//                    prTable.setValueAt("-",15,2);
                memoryTable.setValueAt("-", 0, 1);
                memoryTable.setValueAt("-", 0, 2);
                memoryTable.setValueAt("-", 1, 1);
                memoryTable.setValueAt("-", 1, 2);
                memoryTable.setValueAt("-", 2, 1);
                memoryTable.setValueAt("-", 2, 2);
                prTable.setValueAt("-",15,2);
                prTable.setValueAt("-",16,2);
                prTable.setValueAt("-",17,2);

            }

            //WB
            if (core.isWb_available()) {
                controlTable.setValueAt(core.getMemToReg_wb(), 11, 1);
                if (core.isRegWrite_wb())
                    controlTable.setValueAt(1, 12, 1);
                else
                    controlTable.setValueAt(0, 12, 1);

            }


        });


    }
}

/*
lw $t0,256($zero)
add $t3,$t3,$t3
add $t1,$t0,$t0
beq $t2,$t0,5
sw $t2,8($zero)

 */
