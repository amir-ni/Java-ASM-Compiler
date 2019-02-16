package Core.View;

import Core.Core;
import Core.Images.Images;

import javax.swing.*;
import java.awt.*;

public class Single_Clock_Cycle_Panel extends JPanel {
    private Core core;
    private Image[] stages;
    private JLabel pc_id_lbl = new JLabel();
    private JLabel pcSrc_id_lbl = new JLabel();
    private JLabel aluIn1_id_lbl = new JLabel();
    private JLabel if_id_lbl = new JLabel();
    private JLabel control_id_lbl = new JLabel();
    private JLabel read_register1 = new JLabel();
    private JLabel read_register2 = new JLabel();
    private JLabel read_data1 = new JLabel();
    private JLabel read_data2 = new JLabel();
    private JLabel write_register = new JLabel();
    private JLabel write_data = new JLabel();
    private JLabel sign_extend = new JLabel();


    public Single_Clock_Cycle_Panel(Core core) {
        this.core = core;
        setLayout(null);
        ////////
        pc_id_lbl.setBounds(87, 365, 24, 60);
        pcSrc_id_lbl.setBounds(47, 323, 15, 43);
        aluIn1_id_lbl.setBounds(122,250,50,10);
        if_id_lbl.setBounds(227,250,24,300);
        control_id_lbl.setBounds(312,180,33,60);
//        read_register1.setBounds();
//        read_register2.setBounds();



        add(pc_id_lbl);
        add(pcSrc_id_lbl);
        add(aluIn1_id_lbl);
        add(if_id_lbl);
        add(control_id_lbl);
        add(read_register1);
        add(read_register2);
        add(read_data1);
        add(read_data2);
        add(write_data);
        add(write_register);





//        pc_id_lbl.addMouseListener(new MouseListener() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//
//            }
//
//            @Override
//            public void mousePressed(MouseEvent e) {
//
//            }
//
//            @Override
//            public void mouseReleased(MouseEvent e) {
//
//            }
//
//            @Override
//            public void mouseEntered(MouseEvent e) {
//                x.setText("PC");
//                x.setVisible(true);
//            }
//
//            @Override
//            public void mouseExited(MouseEvent e) {
//                x.setVisible(false);
//            }
//        });
//
//
//
//        pcSrc_id_lbl.addMouseListener(new MouseListener() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//
//            }
//
//            @Override
//            public void mousePressed(MouseEvent e) {
//
//            }
//
//            @Override
//            public void mouseReleased(MouseEvent e) {
//
//            }
//
//            @Override
//            public void mouseEntered(MouseEvent e) {
//                x.setText("PCSrc");
//                x.setVisible(true);
//            }
//
//            @Override
//            public void mouseExited(MouseEvent e) {
//                x.setVisible(false);
//            }
//        });
//        aluIn1_id_lbl.addMouseListener(new MouseListener() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//
//            }
//
//            @Override
//            public void mousePressed(MouseEvent e) {
//
//            }
//
//            @Override
//            public void mouseReleased(MouseEvent e) {
//
//            }
//
//            @Override
//            public void mouseEntered(MouseEvent e) {
//                x.setText("Alu Input 1");
//                x.setVisible(true);
//            }
//
//            @Override
//            public void mouseExited(MouseEvent e) {
//                x.setVisible(false);
//            }
//        });
//        if_id_lbl.addMouseListener(new MouseListener() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//
//            }
//
//            @Override
//            public void mousePressed(MouseEvent e) {
//
//            }
//
//            @Override
//            public void mouseReleased(MouseEvent e) {
//
//            }
//
//            @Override
//            public void mouseEntered(MouseEvent e) {
//                x.setText("IF/ID");
//                x.setVisible(true);
//            }
//
//            @Override
//            public void mouseExited(MouseEvent e) {
//                x.setVisible(false);
//            }
//        });
//        instruction_memory_id_lbl.addMouseListener(new MouseListener() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//
//            }
//
//            @Override
//            public void mousePressed(MouseEvent e) {
//
//            }
//
//            @Override
//            public void mouseReleased(MouseEvent e) {
//
//            }
//
//            @Override
//            public void mouseEntered(MouseEvent e) {
//                x.setText("Instruction Memory");
//                x.setVisible(true);
//            }
//
//            @Override
//            public void mouseExited(MouseEvent e) {
//                x.setVisible(false);
//            }
//        });
//        control_id_lbl.addMouseListener(new MouseListener() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//
//            }
//
//            @Override
//            public void mousePressed(MouseEvent e) {
//
//            }
//
//            @Override
//            public void mouseReleased(MouseEvent e) {
//
//            }
//
//            @Override
//            public void mouseEntered(MouseEvent e) {
//                x.setText("Control Unit");
//                x.setVisible(true);
//            }
//
//            @Override
//            public void mouseExited(MouseEvent e) {
//                x.setVisible(false);
//            }
//        });


    }

    public void setStages(Image[] stages) {
        this.stages = stages;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Image x = null;
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        int w = 0;
        g2.drawImage(stages[0], w, 0, Images.if_width, Images.height, null);
        w += Images.if_width;
        g2.drawImage(stages[1], w, 0, Images.id_width, Images.height, null);
        w += Images.id_width;
        g2.drawImage(stages[2], w, 0, Images.ex_width, Images.height, null);
        w += Images.ex_width;
        g2.drawImage(stages[3], w, 0, Images.mem_width, Images.height, null);
        w += Images.mem_width;
        g2.drawImage(stages[4], w, 0, Images.wb_width, Images.height, null);


    }

    public void update(){
        pcSrc_id_lbl.setToolTipText(Integer.toString(core.getPcSrc()));
        pc_id_lbl.setToolTipText(Integer.toString((int)core.getPc_IF()));
        aluIn1_id_lbl.setToolTipText(Integer.toString((int)core.getPc_IF()));
        if_id_lbl.setToolTipText("PC+4 = " + core.getPc_4_IF_ID() + "\n" + "Instruction: " + core.getInstructionBinary_IF());
    }
}
