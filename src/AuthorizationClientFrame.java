import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AuthorizationClientFrame extends JFrame {
    private JTextField fieldAdress = new JTextField(10);
    private JTextField fieldPort = new JTextField(10);
    private JLabel labelAdress = new JLabel("enter server adress: ");
    private JLabel labelPort = new JLabel("enter server port: ");
    private JButton buttonOk = new JButton("OK");

    public AuthorizationClientFrame (ClientFrame clientFrame){
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(300, 150);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);
        setTitle("Authorization");
        setResizable(false);

        JPanel adressPanel = new JPanel();
        adressPanel.setLayout(new FlowLayout());
        adressPanel.setPreferredSize(new Dimension(200,40));
        adressPanel.add(labelAdress);
        adressPanel.add(fieldAdress);

        JPanel portPanel = new JPanel();
        portPanel.setLayout(new FlowLayout());
        portPanel.setPreferredSize(new Dimension(200,40));
        portPanel.add(labelPort);
        portPanel.add(fieldPort);

        buttonOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (fieldPort.getText().trim().equals("")) {
                    JOptionPane.showMessageDialog(AuthorizationClientFrame.this, "ERROR: empty port field");
                    return;
                }
                clientFrame.setPort(Integer.parseInt(fieldPort.getText()));
                if (fieldAdress.getText().trim().equals("")) {
                    JOptionPane.showMessageDialog(AuthorizationClientFrame.this, "ERROR: empty adress field");
                    return;
                }
                clientFrame.setAdress(fieldAdress.getText());
                setVisible(false);
                clientFrame.makeVisible();
            }
        });

        add(adressPanel, BorderLayout.NORTH);
        add(portPanel, BorderLayout.CENTER);
        add(buttonOk, BorderLayout.SOUTH);

        setVisible(true);
    }

}
