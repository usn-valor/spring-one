package ru.home.client;

import ru.home.library.Common;
import ru.home.network.SocketThread;
import ru.home.network.SocketThreadListener;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class ClientGUI extends JFrame implements ActionListener, Thread.UncaughtExceptionHandler, SocketThreadListener, ListSelectionListener {

    private static final int WIDTH = 600;
    private static final int HEIGHT = 300;

    private final JTextArea log = new JTextArea();
    private final JPanel panelTop = new JPanel(new GridLayout(2, 3));
    private final JTextField tfIPAddress = new JTextField("127.0.0.1");
    private final JTextField tfPort = new JTextField("8189");
    private final JCheckBox cbAlwaysOnTop = new JCheckBox("Always on top");
    private final JTextField tfLogin = new JTextField("Arsen");
    private final JPasswordField tfPassword = new JPasswordField("123");
    private final JButton btnLogin = new JButton("Login");

    private final JPanel panelBottom = new JPanel(new BorderLayout());
    private final JButton btnDisconnect = new JButton("<html><b>Disconnect</b></html>");
    private final JTextField tfMessage = new JTextField();
    private final JButton btnSend = new JButton("Send");

    private final JList<String> userList = new JList<>();
    private boolean shownIoErrors = false;
    private SocketThread socketThread;
    private final DateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm:ss: ");
    private final String WINDOW_TITLE = "Chat";

    private String recipient; // поле для приватной отправки сообщений
    private String nickName; /* поле для смены ника (при первом входе его назначает сервер, при смене оно передаётся в
    качестве старого ника) */
    private String currentUser; // поле для определения, сообщение какого пользователя будет писаться в лог

    private ClientGUI() {
        Thread.setDefaultUncaughtExceptionHandler(this);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(WIDTH, HEIGHT);
        setTitle(WINDOW_TITLE);
        log.setEditable(false);
        log.setLineWrap(true);
        JScrollPane scrollLog = new JScrollPane(log);
        JScrollPane scrollUser = new JScrollPane(userList);
        scrollUser.setPreferredSize(new Dimension(100, 0));

        cbAlwaysOnTop.addActionListener(this);
        btnSend.addActionListener(this);
        tfMessage.addActionListener(this);
        btnLogin.addActionListener(this);
        btnDisconnect.addActionListener(this);
        userList.addListSelectionListener(this);

        panelTop.add(tfIPAddress);
        panelTop.add(tfPort);
        panelTop.add(cbAlwaysOnTop);
        panelTop.add(tfLogin);
        panelTop.add(tfPassword);
        panelTop.add(btnLogin);
        panelBottom.add(btnDisconnect, BorderLayout.WEST);
        panelBottom.add(tfMessage, BorderLayout.CENTER);
        panelBottom.add(btnSend, BorderLayout.EAST);
        panelBottom.setVisible(false);

        add(scrollLog, BorderLayout.CENTER);
        add(scrollUser, BorderLayout.EAST);
        add(panelTop, BorderLayout.NORTH);
        add(panelBottom, BorderLayout.SOUTH);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() { // Event Dispatching Thread
                new ClientGUI();
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == cbAlwaysOnTop)
            setAlwaysOnTop(cbAlwaysOnTop.isSelected());
        else if (src == btnSend || src == tfMessage)
            sendMessage();
        else if (src == btnLogin)
            connect();
        else if (src == btnDisconnect)
            socketThread.close();
        else
            throw new RuntimeException("Unknown source: " + src);
    }

    @Override
    public void valueChanged(ListSelectionEvent e) { // метод для прослушки поля с подключенными юзерами
        recipient = userList.getSelectedValue();
    }

    private void connect() {
        try {
            Socket socket = new Socket(tfIPAddress.getText(), Integer.parseInt(tfPort.getText()));
            socketThread = new SocketThread(this, "Client", socket);
        } catch (IOException exception) {
            showException(Thread.currentThread(), exception);
        }
    }

    private void sendMessage() {
        String msg = tfMessage.getText();
        if ("".equals(msg))
            return;
        tfMessage.setText(null); // после отправки сообщения поле для ввода должно остаться пустым
        tfMessage.grabFocus(); // метод для возврата каретки в исходное положение после опустошения поля отправки сообщений
        if (userList.isSelectionEmpty()) {
            if (msg.startsWith("change_nickname")) {
                String[] newNickname = msg.split(" ");
                socketThread.sendMessage(Common.getTypeChangeNickname(tfLogin.getText(), nickName, newNickname[1]));
                return;
            }
            socketThread.sendMessage(Common.getTypeBcastClient(msg));
            return;
        }
        socketThread.sendMessage(Common.getTypeClientPrivate(tfLogin.getText(), recipient, msg));
        userList.clearSelection();
    }

    private void wrtMsgToLogFile(String msg, String username) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        String s = dateFormat.format(new Date()) + ": " + username + ": " + msg;
        try (FileWriter out = new FileWriter("log.txt", true)) {
            out.write(s + "\n");
            out.flush();
        } catch (IOException e) {
            if (!shownIoErrors) {
                shownIoErrors = true;
                showException(Thread.currentThread(), e);
            }
        }
    }

    private void rdMsgFromLogFile() {
        File file = new File("log.txt");
        if (file.exists()) {
            try (BufferedReader br1 = new BufferedReader(new FileReader(file));
                 BufferedReader br2 = new BufferedReader(new FileReader(file))) {
                int lines = 0;
                while (br1.readLine() != null)
                    lines++;
                int count = 0;
                while (br2.ready()) {
                    String s = br2.readLine();
                    if (lines - count <= 10)
                        putLog(s);
                    count++;
                }
            } catch (IOException e) {
                if (!shownIoErrors) {
                    shownIoErrors = true;
                    showException(Thread.currentThread(), e);
                }
            }
        }
        putLog("\n");
    }

    private void putLog(String msg) {
        if ("".equals(msg))
            return;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                log.append(msg + "\n");
                log.setCaretPosition(log.getDocument().getLength());
            }
        });
    }

    private void showException(Thread t, Throwable e) {
        String msg;
        StackTraceElement[] ste = e.getStackTrace();
        if (ste.length == 0)
            msg = "Empty Stacktrace";
        else {
            msg = String.format("Exception in \"%s\" %s: %s\n\tat %s",
                    t.getName(), e.getClass().getCanonicalName(), e.getMessage(), ste[0]);
            JOptionPane.showMessageDialog(this, msg, "Exception", JOptionPane.ERROR_MESSAGE);
        }
        JOptionPane.showMessageDialog(null, msg, "Exception", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        e.printStackTrace();
        showException(t, e);
        System.exit(1);
    }

    @Override
    public void onSocketStart(SocketThread thread, Socket socket) {
        putLog("Start");
        rdMsgFromLogFile();
    }

    /**
     * Вызовы методов для сокрытия-появления панелек реализованы именно тут по причине того, что разрывы соединения могут
     * возникнуть не только по инициативе клиента. Если эти методы вызывать, например, из метода connect() или гипотетического
     * метода disconnect(), то это - инициатива клиента. Но может случиться так, что соединение отвалилось на сервере,
     * тогда панельки не появтся или не скроются
     * */
    @Override
    public void onSocketStop(SocketThread thread) {
        panelBottom.setVisible(false);
        panelTop.setVisible(true);
        setTitle(WINDOW_TITLE);
        userList.setListData(new String[0]);
    }

    @Override
    public void onSocketReady(SocketThread thread, Socket socket) {
        panelBottom.setVisible(true);
        panelTop.setVisible(false);
        String login = tfLogin.getText();
        String password = new String(tfPassword.getPassword());
        thread.sendMessage(Common.getAuthRequest(login, password));

    }

    @Override
    public void onReceiveString(SocketThread thread, Socket socket, String msg) {
        handleMessage(msg);
    }

    private void handleMessage(String msg) {
        String[] arr = msg.split(Common.DELIMITER);
        String msgType = arr[0];
        switch (msgType) {
            case Common.AUTH_ACCEPT:
                setTitle(WINDOW_TITLE + " entered with nickname: " + arr[1]);
                currentUser = "Server";
                break;
            case Common.AUTH_DENIED:
                putLog(msg);
                currentUser = "Server";
                wrtMsgToLogFile(msg, currentUser);
                break;
            case Common.MSG_FORMAT_ERROR:
                putLog(msg);
                currentUser = "Server";
                wrtMsgToLogFile(arr[1], currentUser);
                socketThread.close();
                break;
            case Common.TYPE_BROADCAST:
                putLog(DATE_FORMAT.format(Long.parseLong(arr[1])) + arr[2] + ": " + arr[3]);
                currentUser = arr[2];
                wrtMsgToLogFile(arr[3], currentUser);
                break;
            case Common.USER_LIST:
                String users = msg.substring(Common.USER_LIST.length() + Common.DELIMITER.length());
                String[] usersArr = users.split(Common.DELIMITER);
                Arrays.sort(usersArr);
                userList.setListData(usersArr);
                break;
            case Common.TYPE_CLIENT_PRIVATE:
                putLog(DATE_FORMAT.format(System.currentTimeMillis()) + arr[1] + ": " + arr[3]);
                if (arr[1].equals("Server"))
                    nickName = arr[2];
                currentUser = arr[1];
                wrtMsgToLogFile(arr[3], currentUser);
                break;
            case Common.TYPE_SERVER_CHANGE_NICKNAME:
                String s = String.format("The User %s changed his nickname to %s", arr[1], arr[2]);
                putLog(s);
                nickName = arr[2];
                currentUser = "Server";
                wrtMsgToLogFile(s, currentUser);
                break;
            default:
                throw new RuntimeException("Unknown message type: " + msg);
        }
    }

    @Override
    public void onSocketException(SocketThread thread, Exception exception) {
        showException(thread, exception);
    }
}
