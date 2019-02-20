package Core.View;

import Core.Core;
import Core.Exceptions.IllegalRegisterNumberException;
import Core.Exceptions.SymbolNotFoundException;
import Core.Exceptions.UndefindInstructionException;
import Core.Exceptions.UnformattedInstructionException;
import Core.Images.Images;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

public class PipelinePanels extends JFrame {
    private Core core;


    public PipelinePanels(List<String> normalInstructions, JTable registerTable) throws IllegalRegisterNumberException, SymbolNotFoundException, UnformattedInstructionException, UndefindInstructionException {
        setSize(1000, 700);
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

        showBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                core.setDetails(Integer.parseInt(clockTextField.getText()));
                core.showGraphical(singleClockCyclePanel, Integer.parseInt(clockTextField.getText()));
            }
        });

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
    }
}
