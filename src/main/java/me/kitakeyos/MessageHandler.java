/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package me.kitakeyos;

import me.kitakeyos.network.IMessageHandler;
import me.kitakeyos.network.Message;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Kitak
 */
public class MessageHandler implements IMessageHandler {

    @Override
    public void onMessage(Message msg) {
        try {
            switch (msg.getCommand()) {
                case -10:
                case -5:
                case -8:
                    String error = msg.reader().readUTF();
                    System.out.println(error);
                    break;

                case -80: {
                    short num20 = msg.reader().readShort();
                    short num21 = msg.reader().readShort();
                    byte[] data3 = new byte[num21];
                    msg.reader().read(data3);
                    Util.saveFile("res/" + (Tool.isResourceHD() ? "hd" : "medium") + "/object/" + num20 + ".png", data3);
                }
                break;

                case -98: {
                    short num20 = msg.reader().readShort();
                    short num21 = msg.reader().readShort();
                    byte[] data3 = new byte[num21];
                    msg.reader().read(data3);
                    Util.saveFile("res/" + (Tool.isResourceHD() ? "hd" : "medium") + "/item/" + num20 + ".png", data3);
                }
                break;

                case 54: {
                    short index3 = msg.reader().readShort();
                    short version = msg.reader().readShort();
                    int num11 = msg.reader().readUnsignedShort();
                    byte[] array = new byte[num11];
                    for (int num12 = 0; num12 < num11; num12++) {
                        array[num12] = msg.reader().readByte();
                    }
                    Util.saveFile("res/" + (Tool.isResourceHD() ? "hd" : "medium") + "/bigFarm/" + index3 + ".png", array);
                    break;
                }

                case 82: {
                    short num20 = msg.reader().readShort();
                    System.out.println("receive icon farm id=" + num20);
                    short num21 = msg.reader().readShort();
                    byte[] data3 = new byte[num21];
                    msg.reader().read(data3);
                    Util.saveFile("res/" + (Tool.isResourceHD() ? "hd" : "medium") + "/farm/" + num20 + ".png", data3);

                }
                break;

                case -14: {
                    short id = msg.reader().readShort();
                    short ver = msg.reader().readShort();
                    int num = msg.reader().readUnsignedShort();
                    byte[] data = new byte[num];
                    for (int i = 0; i < num; i++) {
                        data[i] = msg.reader().readByte();
                    }
                    short follow = -1;
                    if (msg.reader().available() >= 2) {
                        follow = msg.reader().readShort();
                    }
                    System.out.println("id: " + id + " ver: " + ver + " follow: " + follow);
                    Util.saveFile("res/" + (Tool.isResourceHD() ? "hd" : "medium") + "/big/" + id + ".png", data);
                    break;
                }

                case -73: {
                    int wNum = msg.reader().readShort();
                    int num = msg.reader().readInt();
                    byte[] data = new byte[num];
                    msg.reader().read(data);
                    System.out.println("wNum: " + wNum);
                    Util.saveFile("res/" + (Tool.isResourceHD() ? "hd" : "medium") + "/house/tile.png", data);
                }
                break;

                case -16: {
                    byte[] data = new byte[msg.reader().available()];
                    msg.reader().read(data);
                    Util.saveFile("res/part", data);
                }
                break;

                case -41: {
                    byte[] data = new byte[msg.reader().available()];
                    msg.reader().read(data);
                    Util.saveFile("res/map_item", data);
                }
                break;

                case 55: {
                    byte[] data = new byte[msg.reader().available()];
                    msg.reader().read(data);
                    Util.saveFile("res/farm.dat", data);
                }
                break;

                case 56: {
                    byte[] data = new byte[msg.reader().available()];
                    msg.reader().read(data);
                    Util.saveFile("res/farm_info.dat", data);
                }
                break;

                case -15: {
                    byte[] data = new byte[msg.reader().available()];
                    msg.reader().read(data);
                    Util.saveFile("res/item.dat", data);
                }
                break;

                case -94: {
                    byte idTileMap = msg.reader().readByte();
                    byte[] data8 = new byte[msg.reader().available()];
                    msg.reader().read(data8);
                    String path = "res/" + (Tool.isResourceHD() ? "hd" : "medium") + "/tilemap/" + idTileMap + ".png";
                    Util.saveFile(path, data8);
                    return;
                }

                case -97: {
                    msg.reader().mark(100000);
                    short iDPart = msg.reader().readShort();
                    System.out.println("idPart: " + iDPart);
                    msg.reader().reset();
                    byte[] data = new byte[msg.reader().available()];
                    msg.reader().read(data);
                    Util.saveFile("res/p/part_" + iDPart + ".dat", data);
                }
                break;

                case -11:
                    byte b = msg.reader().readByte();
                    for (int j = 0; j < b; j++) {
                        short id = msg.reader().readShort();
                        short ver = msg.reader().readShort();
                        System.out.println("id: " + id + " ver: " + ver);
                    }
                    int verBigImg = msg.reader().readShort();
                    System.out.println("verBigImg: " + verBigImg);
                    int verPart = msg.reader().readShort();
                    System.out.println("verPart: " + verPart);
                    int verBigItemImg = msg.reader().readShort();
                    System.out.println("verBigItemImg: " + verBigItemImg);
                    int vItemType = msg.reader().readShort();
                    System.out.println("vItemType: " + vItemType);
                    int vItem = msg.reader().readShort();
                    System.out.println("vItem: " + vItem);
                    byte b2 = msg.reader().readByte();
                    for (int k = 0; k < b2; k++) {
                        short id = msg.reader().readShort();
                        short ver = msg.reader().readShort();
                        System.out.println("id: " + id + " ver: " + ver);
                    }
                    int vObj = msg.reader().readInt();
                    System.out.println("vObj: " + vObj);
                    break;

                case -84: {
                    byte type = msg.reader().readByte();
                    short effId = msg.reader().readByte();
                    System.out.println("type: " + type + " effId: " + effId);
                    short num6 = msg.reader().readShort();
                    byte[] data2 = new byte[num6];
                    msg.reader().read(data2);
                    Util.saveFile("res/" + (Tool.isResourceHD() ? "hd" : "medium") + "/effect/" + effId + ".png" ,  data2);
                    data2 = new byte[msg.reader().available()];
                    msg.reader().read(data2);
                    Util.saveFile("res/data/effect/" + effId + ".dat" ,  data2);

                }
                break;

                default:
                    System.out.println("cmd: " + msg.getCommand());
                    break;
            }
        } catch (IOException ex) {
            Logger.getLogger(MessageHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void onConnectionFail() {
        System.out.println("onConnectionFail()");
    }

    @Override
    public void onDisconnected() {
        System.out.println("onDisconnected()");
    }

    @Override
    public void onConnectOK() {
        System.out.println("onConnectOK()");
    }

}
