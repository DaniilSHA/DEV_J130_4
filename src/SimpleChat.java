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

    private JButton clientMod;
    private JButton serverMod;
    private JFrame mainAppFrame;
    private ServerSocket serverSocket;
    private Socket socket;


    public SimpleChat() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Application mod");
        setSize(500, 200);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);
        setResizable(false);

        clientMod = new JButton("Client mod");
        serverMod = new JButton("Server mod");
        serverMod.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    server();
                } catch (ChatException ex) {
                    ex.printStackTrace();
                }
            }
        });
        clientMod.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    client();
                } catch (ChatException ex) {
                    ex.printStackTrace();
                }
            }
        });
        clientMod.setPreferredSize(new Dimension(200, 150));
        serverMod.setPreferredSize(new Dimension(200, 150));

        setLayout(new FlowLayout(FlowLayout.CENTER));

        add(clientMod);
        add(serverMod);

        setVisible(true);
    }

    @Override
    public void client() throws ChatException {

        mainAppFrame = new ClientFrame(this);
        ClientFrame clientFrame = (ClientFrame) mainAppFrame;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    String msg = clientFrame.getMsg();
                    if (clientFrame.getAdress() != null && clientFrame.getPort() != 0 && msg != null) {
                        try {
                            socket = new Socket(clientFrame.getAdress(), clientFrame.getPort());
                            sendMessage(msg);
                            clientFrame.setMsg(null);
                        } catch (IOException | ChatException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }

    @Override
    public void server() throws ChatException {
        try {
            mainAppFrame = new ServerFrame(this);
            serverSocket = new ServerSocket(ISimpleChat.SERVER_PORT);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            socket = serverSocket.accept();
                            ServerFrame serverFrame = (ServerFrame) mainAppFrame;
                            serverFrame.printMsg(getMessage());
                        }
                    } catch (IOException | ChatException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
            throw new ChatException(e.getMessage());
        }
    }

    @Override
    public String getMessage() throws ChatException {
        if (socket != null) {
            try {
                Scanner scanner = new Scanner(socket.getInputStream());
                StringBuilder resultMsg = new StringBuilder();
                while (scanner.hasNext()) {
                    String msg = scanner.nextLine();
                    resultMsg.append(msg);
                    if (msg.equals("exit")) break;
                }
                scanner.close();
                return new String(resultMsg);
            } catch (IOException e) {
                e.printStackTrace();
                throw new ChatException(e.getMessage());
            }
        } else throw new ChatException("Empty socket");
    }

    @Override
    public void sendMessage(String message) throws ChatException {
        if (socket != null) {
            try {
                PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
                printWriter.print(message);
                printWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
                throw new ChatException(e.getMessage());
            }
        }
    }

    @Override
    public void close() throws ChatException {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) serverSocket.close();
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new ChatException(e.getMessage());
        }
    }
}
