package Core.View;

import Core.Core;
import Core.Exceptions.IllegalRegisterNumberException;
import Core.Exceptions.SymbolNotFoundException;
import Core.Exceptions.UndefindInstructionException;
import Core.Exceptions.UnformattedInstructionException;
import Core.Images.Images;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

public class PipelinePanels extends JFrame {
    private Core core;

    private int clock = 0;

    public void setClock(int clock){
        if (clock < core.getAssembler().getPipelines().size()) {
            clockTextField.setText(Integer.toString(clock));
            showBtn.doClick();
        }
    }

    private JButton previousBtn = new JButton();
    private JButton nextBtn = new JButton();
    private JButton showBtn = new JButton();
    private JTextField clockTextField = new JTextField("0");
    private JLabel clockLbl = new JLabel("Clock:");
    public PipelinePanels(List<String> normalInstructions, JTable registerTable) throws IllegalRegisterNumberException, SymbolNotFoundException, UnformattedInstructionException, UndefindInstructionException {
        setSize(1000, 720);
        setLayout(null);
        setResizable(false);
        
        core = new Core(normalInstructions, registerTable);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBounds(0, 0, 1000, 700);
        add(tabbedPane);

        Text_Panel text_panel = new Text_Panel(core);
        tabbedPane.add(text_panel, "Text");

        JPanel graphicalPanel = new JPanel();
        graphicalPanel.setLayout(null);
        graphicalPanel.setSize(1000, 700);
        tabbedPane.add(graphicalPanel, "Single Clock Cycle");

        Single_Clock_Cycle_Panel singleClockCyclePanel = new Single_Clock_Cycle_Panel(core);
        singleClockCyclePanel.setBounds(0, 0, 1000, 600);
        graphicalPanel.add(singleClockCyclePanel);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(null);
        bottomPanel.setBounds(0, 600, 1000, 100);
        graphicalPanel.add(bottomPanel);

        //bottom panel

        Icon pre = new ImageIcon(Images.previousIcon);
        previousBtn.setIcon(pre);
        Icon next = new ImageIcon(Images.nextIcon);
        nextBtn.setIcon(next);
        Icon apply = new ImageIcon(Images.applyIcon);
        showBtn.setIcon(apply);
        clockLbl.setFont(new Font(Font.SERIF, Font.PLAIN, 18));
        clockTextField.setFont(new Font(Font.SERIF, Font.PLAIN, 18));
        clockTextField.setHorizontalAlignment(0);
        previousBtn.setBounds(35, 10, 40, 40);
        nextBtn.setBounds(900, 10, 40, 40);
        clockLbl.setBounds(405, 10, 50, 40);
        clockTextField.setBounds(460, 10, 50, 40);
        showBtn.setBounds(515, 10, 40, 40);
        bottomPanel.add(previousBtn);
        bottomPanel.add(nextBtn);
        bottomPanel.add(showBtn);
        bottomPanel.add(clockLbl);
        bottomPanel.add(clockTextField);


        core.showGraphical(singleClockCyclePanel, 0);


        // Graphical Panel
        previousBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Integer.parseInt(clockTextField.getText()) > 0) {
                    clockTextField.setText(Integer.toString(Integer.parseInt(clockTextField.getText()) - 1));
                    showBtn.doClick();
                }
            }
        });

        nextBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Integer.parseInt(clockTextField.getText()) + 1 < core.getAssembler().getPipelines().size()) {
                    clockTextField.setText(Integer.toString(Integer.parseInt(clockTextField.getText()) + 1));
                    showBtn.doClick();
                }
            }
        });

        showBtn.addActionListener(e -> {
            if (Integer.parseInt(clockTextField.getText()) >= core.getAssembler().getPipelines().size()){
                clockTextField.setText(Integer.toString(core.getAssembler().getPipelines().size()-1));
            } else if (Integer.parseInt(clockTextField.getText()) < 0) {
                clockTextField.setText("0");
            }
            this.clock = Integer.parseInt(clockTextField.getText());
            core.setDetails(Integer.parseInt(clockTextField.getText()));
            core.showGraphical(singleClockCyclePanel, Integer.parseInt(clockTextField.getText()));
        });
//        showBtn.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                core.setDetails(Integer.parseInt(clockTextField.getText()));
//                core.showGraphical(singleClockCyclePanel, Integer.parseInt(clockTextField.getText()));
//            }
//        });

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

        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int selectedIndex = tabbedPane.getSelectedIndex();
                Component selectedComponent = tabbedPane.getComponentAt(selectedIndex);

                if (selectedComponent instanceof JPanel) {
                    JPanel selectedPanel = (JPanel) selectedComponent;
                    if (selectedPanel == text_panel) {
                        // Update something in panel1 when it's selected
                        text_panel.setClock(clock);
                    } else if (selectedPanel == graphicalPanel) {
                        // Update something in panel2 when it's selected
                        setClock(text_panel.getClock());
                    }
                }
            }
        });
    }
}
