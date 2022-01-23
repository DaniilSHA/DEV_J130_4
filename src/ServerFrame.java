import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;

public class ServerFrame extends JFrame {

    private JTextArea text;
    private JButton exit;

    public ServerFrame(SimpleChat simpleChat) {
        simpleChat.setVisible(false);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Server mod");
        setSize(600, 600);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);
        setResizable(false);

        exit = new JButton("EXIT");
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                simpleChat.dispose();
                System.exit(0);
            }
        });

        text = new JTextArea();
        text.setEditable(false);

        add(text, BorderLayout.CENTER);
        add(exit, BorderLayout.SOUTH);

        setVisible(true);
    }

    public void printMsg(String msg) {
        text.setText(null);
        text.append(msg);
    }

}
