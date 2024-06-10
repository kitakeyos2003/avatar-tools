/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package me.kitakeyos.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kitak
 */
public class Session implements ISession {

    public boolean isCancel;
    public Thread collectorThread;
    public boolean connected;
    public boolean connecting;
    private byte curR;
    private byte curW;
    public DataInputStream dis;
    public int disSecond;
    private DataOutputStream dos;
    boolean getKeyComplete;
    public Thread initThread;
    boolean isReconnectOk = true;
    public byte[] key = null;
    public IMessageHandler messageHandler;
    boolean reconnect;
    int reconnectTurn = 0;
    public int recvByteCount;
    private Socket sc;
    public int sendByteCount;
    public Thread sendThread;
    private final Sender sender = new Sender();
    public String sessionID;
    public String strRecvByteCount = "";
    long timeConnected;

    class MessageCollector implements Runnable {

        public void run() {
            try {
                while (connected) {
                    Message message = this.readMessage();
                    if (message == null) {
                        break;
                    }
                    try {
                        if ((int) message.getCommand() == -27) {
                            this.getKey(message);
                        } else {
                            messageHandler.onMessage(message);
                        }
                    } catch (Exception e) {
                        System.out.println("LOI NHAN  MESS THU 1");
                    }
                    try {
                        Thread.sleep(5);
                    } catch (Exception e) {
                    }
                }
            } catch (Exception ex) {
            }
            if (connected) {
                if (messageHandler != null) {
                    if (System.currentTimeMillis() - timeConnected > 500) {
                        messageHandler.onDisconnected();
                    } else {
                        messageHandler.onConnectionFail();
                    }
                }
                if (sc != null) {
                    cleanNetwork();
                }
            }
        }

        private void getKey(Message message) {
            try {
                byte b = message.reader().readByte();
                key = new byte[(int) b];
                for (int i = 0; i < (int) b; i++) {
                    key[i] = message.reader().readByte();
                }
                for (int j = 0; j < key.length - 1; j++) {
                    byte[] key2 = key;
                    int num = j + 1;
                    key2[num] = (byte) ((int) key2[num] ^ (int) key[j]);
                }
                getKeyComplete = true;
            } catch (IOException ex) {
                Logger.getLogger(Session.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        private Message readMessage() throws Exception {
            try {
                byte b = dis.readByte();
                if (getKeyComplete) {
                    b = readKey(b);
                }
                int num;
                if (getKeyComplete) {
                    byte b2 = dis.readByte();
                    byte b3 = dis.readByte();
                    num = (((int) readKey(b2) & 0xFF) << 8 | ((int) readKey(b3) & 0xFF));
                } else {
                    byte b4 = dis.readByte();
                    byte b5 = dis.readByte();
                    num = (b4 & 0xFF00) | (b5 & 0xFF);
                }
                byte[] data = new byte[num];
                int len = 0;
                int byteRead = 0;
                while (len != -1 && byteRead < num) {
                    len = dis.read(data, byteRead, num - byteRead);
                    if (len > 0) {
                        byteRead += len;
                    }
                }
                if (getKeyComplete) {
                    for (int i = 0; i < data.length; i++) {
                        data[i] = readKey(data[i]);
                    }
                }
                recvByteCount += 5 + num;
                int num2 = recvByteCount + sendByteCount;
                strRecvByteCount = (num2 / 1024) + "." + (num2 % 1024 / 102) + "Kb";
                return new Message(b, data);
            } catch (Exception ex) {
            }
            return null;
        }
    }

    class NetworkInit implements Runnable {

        private final String host;
        int port;

        NetworkInit(String host, int port) {
            this.host = host;
            this.port = port;
        }

        public void run() {
            isCancel = false;
            connecting = true;
            Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
            connected = true;
            try {
                this.doConnect(this.host, this.port);
                messageHandler.onConnectOK();
            } catch (Exception e) {
                if (messageHandler != null) {
                    close();
                    messageHandler.onConnectionFail();
                }
            }
        }

        public void doConnect(String host, int port) throws Exception {
            sc = new Socket(host, port);
            sc.setKeepAlive(true);
            dos = new DataOutputStream(sc.getOutputStream());
            dis = new DataInputStream(sc.getInputStream());
            new Thread(sender).start();
            collectorThread = new Thread(new MessageCollector());
            collectorThread.start();
            timeConnected = System.currentTimeMillis();
            doSendMessage(new Message(-27));
            connecting = false;
        }
    }

    private class Sender implements Runnable {

        private final Vector sendingMessage = new Vector();

        public void AddMessage(Message message) {
            this.sendingMessage.addElement(message);
        }

        public void run() {
            while (connected) {
                try {
                    if (getKeyComplete) {
                        while (this.sendingMessage.size() > 0) {
                            Message m = (Message) sendingMessage.get(0);
                            doSendMessage(m);
                            this.sendingMessage.removeElementAt(0);
                        }
                    }
                    try {
                        Thread.sleep(5);
                    } catch (Exception ex) {
                    }
                } catch (IOException e) {
                }
            }
        }
    }

    public void clearSendingMessage() {
        this.sender.sendingMessage.removeAllElements();
    }

    public boolean isConnected() {
        return this.connected;
    }

    public void setHandler(IMessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    public void connect(String host, int port) {
        if (connected || connecting) {
            return;
        }
        getKeyComplete = false;
        sc = null;
        System.out.println("connecting...!");
        System.out.println("host: " + host);
        System.out.println("port: " + port);
        initThread = new Thread(new NetworkInit(host, port));
        initThread.start();
    }

    public void reConnect(String host, int port) {
        this.reconnectTurn++;
        connect(host, port);
    }

    public void sendMessage(Message message) {
        sender.AddMessage(message);
    }

    private synchronized void doSendMessage(Message m) throws IOException {
        byte[] data = m.getData();
        try {
            if (getKeyComplete) {
                byte value = writeKey(m.getCommand());
                dos.writeByte(value);
            } else {
                dos.writeByte(m.getCommand());
            }
            if (data != null) {
                int num = data.length;
                if (getKeyComplete) {
                    int num2 = (int) writeKey((byte) (num >> 8));
                    dos.writeByte((byte) num2);
                    int num3 = (int) writeKey((byte) (num & 0xFF));
                    dos.writeByte((byte) num3);
                } else {
                    dos.writeShort((short) num);
                }
                if (getKeyComplete) {
                    for (int i = 0; i < data.length; i++) {
                        byte value2 = writeKey(data[i]);
                        dos.writeByte(value2);
                    }
                }
                sendByteCount += 5 + data.length;
            } else {
                if (getKeyComplete) {
                    int num4 = 0;
                    int num5 = (int) writeKey((byte) (num4 >> 8));
                    dos.writeByte((byte) num5);
                    int num6 = (int) writeKey((byte) (num4 & 0xFF));
                    dos.writeByte((byte) num6);
                } else {
                    dos.writeShort(0);
                }
                sendByteCount += 5;
            }
            dos.flush();
        } catch (Exception ex) {
        }
    }

    private byte readKey(byte b) {
        byte[] array = key;
        byte b2 = curR;
        curR = (byte) ((int) b2 + 1);
        byte result = (byte) ((array[(int) b2] & 255) ^ ((int) b & 255));
        if ((int) curR >= key.length) {
            curR = (byte) ((int) curR % (int) ((byte) key.length));
        }
        return result;
    }

    private byte writeKey(byte b) {
        byte[] array = key;
        byte b2 = curW;
        curW = (byte) ((int) b2 + 1);
        byte result = (byte) ((array[(int) b2] & 255) ^ ((int) b & 255));
        if ((int) curW >= key.length) {
            curW = (byte) ((int) curW % (int) ((byte) key.length));
        }
        return result;
    }

    public void close() {
        System.out.println("Đóng socket");
        cleanNetwork();
    }

    private void cleanNetwork() {
        this.curR = (byte) 0;
        this.curW = (byte) 0;
        long currentTimeMillis = System.currentTimeMillis();
        currentTimeMillis = System.currentTimeMillis();
        try {
            this.getKeyComplete = false;
            this.key = null;
            this.reconnect = false;
            this.connected = false;
            this.connecting = false;
            if (this.sc != null) {
                this.sc.close();
                this.sc = null;
            }
            if (this.dos != null) {
                this.dos.close();
                this.dos = null;
            }
            if (this.dis != null) {
                this.dis.close();
                this.dis = null;
            }
            this.sendThread = null;
            this.collectorThread = null;
            System.gc();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public void clearReconnect() {
        this.sessionID = null;
        this.reconnect = false;
    }

}
