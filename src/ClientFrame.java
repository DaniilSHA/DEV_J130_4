import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientFrame extends JFrame {

    private JLabel label = new JLabel("Enter your massage: ");
    private JTextField input = new JTextField();
    private JButton sendMsg = new JButton("SEND");
    private JButton exit = new JButton("EXIT");
    private int port;
    private String adress;
    private String msg;


    public ClientFrame(SimpleChat simpleChat) {
        simpleChat.setVisible(false);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Client mod");
        setSize(600, 600);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);
        setResizable(false);

        new AuthorizationClientFrame(this);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        sendMsg.setPreferredSize(new Dimension(100, 100));
        sendMsg.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (input.getText().trim().equals("")) {
                    JOptionPane.showMessageDialog(ClientFrame.this, "ERROR: empty input field");
                    return;
                }
                msg = input.getText();
            }
        });
        exit.setPreferredSize(new Dimension(100, 100));
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                simpleChat.dispose();
                System.exit(0);
            }
        });

        buttonPanel.add(sendMsg);
        buttonPanel.add(exit);

        add(label, BorderLayout.NORTH);

        add(input, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void makeVisible() {
        setVisible(true);
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public int getPort() {
        return port;
    }

    public String getAdress() {
        return adress;
    }

}
