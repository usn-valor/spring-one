package ru.home.server.core;

import ru.home.library.Common;
import ru.home.network.SocketThread;
import ru.home.network.SocketThreadListener;

import java.net.Socket;

public class ClientThread extends SocketThread {

    private String nickname;
    private boolean isAuthorized;
    private boolean isReconnecting;

    public boolean isReconnecting() {
        return isReconnecting;
    }

    void reconnect() {
        isReconnecting = true;
        close();
    }

    public ClientThread(SocketThreadListener listener, String name, Socket socket) {
        super(listener, name, socket);
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public boolean isAuthorized() {
        return isAuthorized;
    }

    void authAccept(String nickname) {
        isAuthorized = true;
        this.nickname = nickname;
        sendMessage(Common.getAuthAccept(nickname));
    }

    void authFail() {
        sendMessage(Common.getAuthDenied());
        close();
    }

    void msgFormatError(String msg) {
        sendMessage(Common.getMsgFormatError(msg));
        close();
    }
}
