/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package me.kitakeyos;

import me.kitakeyos.network.IService;
import me.kitakeyos.network.Message;
import me.kitakeyos.network.Session;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Kitak
 */
public class Service implements IService {

    private Session session;

    public Service(Session session) {
        this.session = session;
    }

    public void setProviderAndClientType() {
        try {
            Message message = new Message((byte) (-1));
            message.writer().writeByte(8);
            sendMessage(message);
            Message message2 = new Message((byte) (-17));
            try {
                message2.writer().writeByte(0);
                message2.writer().writeInt(10000);
                String value = "uni";
                message2.writer().writeUTF(value);
                message2.writer().writeInt(10000);
                message2.writer().writeInt(860);
                message2.writer().writeInt(1600);
                message2.writer().writeBoolean(true);
                message2.writer().writeByte(Tool.resourceType);
                message2.writer().writeUTF("2.5.8");
            } catch (IOException e) {
                e.printStackTrace();
            }
            sendMessage(message2);
            Message message3 = new Message((byte) (-79));
            message3.writer().writeUTF("0");
            sendMessage(message3);
            Message message4 = new Message(-86);
            message4.writer().writeByte(2);
            message4.writer().writeByte(13);
            message4.writer().writeUTF("Hello");
            sendMessage(message4);
        } catch (IOException ex) {
            Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void doGetImgFarmIcon(short id) {
        Message message = new Message((byte) 82);
        try {
            message.writer().writeShort(id);
            sendMessage(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getHandler(int index) {
        Message message = new Message((byte) (-1));
        try {
            message.writer().writeByte(index);
            sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void doGetTileMap() {
        Message m = new Message((byte) (-73));
        sendMessage(m);
    }

    public void getBigImage(short id) {
        Message message = new Message((byte) (-14));
        try {
            message.writer().writeShort(id);
            sendMessage(message);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void getBigData() {
        Message m = new Message((byte) (-11));
        sendMessage(m);
    }

    public void requestImagePart(short id) {
        Message message = new Message((byte) (-98));
        try {
            message.writer().writeShort(id);
            sendMessage(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void doGetImgIcon(short id) {
        Message message = new Message((byte) (-80));
        try {
            message.writer().writeShort(id);
            sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void doLogin(String username, String password, String version) {
        Message message = new Message((byte) (-2));
        try {
            message.writer().writeUTF(username);
            message.writer().writeUTF(password);
            message.writer().writeUTF(version);
            sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getAvatarPart() {
        Message m = new Message((byte) (-16));
        sendMessage(m);
    }

    public void requestPartDynaMic(short idPart) {
        Message message = new Message((byte) (-97));
        try {
            message.writer().writeShort(idPart);
            sendMessage(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void doJoinPark(int roomID, int boardID) {
        Message message = new Message((byte) 50);
        try {
            message.writer().writeByte((byte) roomID);
            message.writer().writeByte((byte) boardID);
            message.writer().writeShort(-1);
            message.writer().writeShort(-1);
            sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getBigImageFarm(short id) {
        Message message = new Message((byte) 54);
        try {
            message.writer().writeShort(id);
            sendMessage(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void requestTileMap(byte idTileImg) {
        Message message = new Message((byte) (-94));
        try {
            message.writer().writeByte(idTileImg);
            sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendMessage(Message ms) {
        session.sendMessage(ms);
    }

    public void getMapItem() {
        Message m = new Message((byte) (-41));
        sendMessage(m);
    }

    public void getImageFarmData() {
        Message m = new Message((byte) 55);
        sendMessage(m);
    }

    public void getTreeInfo() {
        Message m = new Message((byte) 56);
        sendMessage(m);
    }

    public void getImageItemData() {
        Message m = new Message((byte) -15);
        sendMessage(m);
    }
}
