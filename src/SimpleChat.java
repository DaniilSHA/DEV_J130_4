import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class SimpleChat extends JFrame implements ISimpleChat {

    private JButton sendButton;
    private JButton exitButton;
    private JTextField inputText;
    private JTextArea outputText;
    private Socket socket;
    private ServerSocket serverSocket;
    private int userChoose;

    public SimpleChat() {
        init();
        int choose = chooseAppMod();
        if (choose == 0) {
            setTitle("Server mod");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        server();
                    } catch (ChatException e) {
                        System.out.println("ERROR #3 " + e.getMessage());
                    }
                }
            }).start();
        } else {
            setTitle("Client mod");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        client();
                    } catch (ChatException e) {
                        System.out.println("ERROR #1 " + e.getMessage());
                    }
                }
            }).start();
        }
        setVisible(true);
    }

    private void init() {
        setSize(600, 600);
        setLocation(400, 300);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //init buttons and texts
        sendButton = new JButton("SEND");
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (inputText.getText().trim().equals("")) {
                    JOptionPane.showMessageDialog(SimpleChat.this, "ERROR: empty input field");
                    return;
                }
                try {
                    sendMessage(inputText.getText());
                } catch (ChatException ex) {
                    System.out.println("ERROR #2 " + ex.getMessage());
                }
            }
        });
        exitButton = new JButton("EXIT");
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                System.exit(0);
            }
        });
        inputText = new JTextField(30);

        outputText = new JTextArea();
        outputText.setEditable(false);

        JPanel southPanel = new JPanel();
        southPanel.setLayout(new FlowLayout());
        southPanel.add(inputText);
        southPanel.add(sendButton);
        southPanel.add(exitButton);

        add(southPanel, BorderLayout.SOUTH);
        add(outputText, BorderLayout.CENTER);
    }

    private int chooseAppMod() {
        Object[] choose = {"server", "client"};
        userChoose = JOptionPane.showOptionDialog(this,
                "You should choose application mod: ",
                null, JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, choose, null);
        return userChoose;
    }

    private String takeValidAdress() {
        String adress = "";
        while (true) {
            adress = JOptionPane.showInputDialog("Input server address: ");
            if (adress == null) {
                dispose();
                System.exit(0);
            }
            if (adress.trim().equals("")) {
                JOptionPane.showMessageDialog(this, "ERROR: empty adress field");
                continue;
            }
            break;
        }
        return adress;

    }

    private int takeValidPort() {
        String portString = "";
        int portInt = 0;
        while (true) {
            portString = JOptionPane.showInputDialog("Input server port: ");
            if (portString == null) {
                dispose();
                System.exit(0);
            }
            if (portString.trim().equals("")) {
                JOptionPane.showMessageDialog(this, "ERROR: empty port field");
                continue;
            }
            try {
                portInt = Integer.parseInt(portString);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "ERROR: port is not a number");
                continue;
            }
            break;
        }
        return portInt;
    }

    @Override
    public void client() throws ChatException {
        String adress = takeValidAdress();
        int port = takeValidPort();
        try {
            socket = new Socket(adress, port);
            System.out.println("client get connection");
            getMessage();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "ERROR: don't valid port and adress");
            client();
        }
    }

    @Override
    public void server() throws ChatException {
        try {
            serverSocket = new ServerSocket(ISimpleChat.SERVER_PORT);
            socket = serverSocket.accept();
            System.out.println("server get connection");
            getMessage();
        } catch (IOException e) {
            throw new ChatException(e.getMessage());
        }
    }

    @Override
    public void getMessage() throws ChatException {
        try (Scanner scanner = new Scanner(socket.getInputStream())) {
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                if (line.equals("exit")) {
                    JOptionPane.showMessageDialog(this, "disconnection");
                    dispose();
                    System.exit(0);
                    close();
                }
                outputText.append(socket.getInetAddress() + ": " + socket.getPort() + " --> " + line + "\n");
            }
        } catch (IOException e) {
            throw new ChatException(e.getMessage());
        }
    }

    @Override
    public void sendMessage(String message) throws ChatException {
        try {
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
            printWriter.println(message);
            if (message.equals("exit")) {
                JOptionPane.showMessageDialog(this, "disconnection");
                dispose();
                System.exit(0);
                close();
            }
            outputText.append("your massage" + " --> " + message + "\n");
            inputText.setText(null);
        } catch (IOException e) {
            throw new ChatException(e.getMessage());
        }
    }

    @Override
    public void close() throws ChatException {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.getOutputStream().close();
                socket.getInputStream().close();
                socket.close();
            }
            if (serverSocket != null && !serverSocket.isClosed()) serverSocket.close();
        } catch (IOException e) {
            throw new ChatException(e.getMessage());
        }
    }
}
