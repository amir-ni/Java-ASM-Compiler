package javaasmcompiler;

import javax.swing.*;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class About {

    private final JFrame frame;
    private final JPanel panel;
    private String contentText;
    private final JLabel text;

    public About(UI ui) {
        panel = new JPanel(new FlowLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        frame = new JFrame();

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.dispose();
            }
        });

        frame.setVisible(true);
        frame.setSize(500, 300);
        frame.setLocationRelativeTo(ui);
        text = new JLabel();
    }

    public void me() {
        frame.setTitle("About US - " + JavaAsmCompiler.NAME);

        contentText = "<html><body><p><br />" + "Developers: <br /><br />Amir hosein Nadiri<br />" + "Contact me at: " + "<a href='mailto:"
                + "amir77ni@gmail.com?subject=About the Java ASM Compiler'>"
                + "amir77ni@gmail.com</a><br /><br />"
                + "Mohamad javad Shariati<br />" + "Contact me at: " + "<a href='mailto:"
                + "mjshariati98@gmail.com?subject=About the Java ASM Compiler'>"
                + "mjshariati98@gmail.com</a><br />"
                + "</p></body></html>";

        text.setText(contentText);
        panel.add(text);
        frame.add(panel);
    }

    public void software() {
        frame.setTitle(String.format("About software - %s", JavaAsmCompiler.NAME));

        contentText = String.format("<html><body><p>Name: %s<br />Version: %s</p></body></html>", JavaAsmCompiler.NAME, JavaAsmCompiler.VERSION);

        text.setText(contentText);
        panel.add(text);
        frame.add(panel);
    }

}