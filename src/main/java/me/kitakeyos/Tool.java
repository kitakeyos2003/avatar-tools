/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */
package me.kitakeyos;

import me.kitakeyos.network.Session;

/**
 *
 * @author Kitak
 */
public class Tool {

    public static int resourceType = 1;// 0 medium hoặc 1 hd

    public static boolean isResourceHD() {
        return resourceType == 1;
    }

    public static void main(String[] args) {
        Session session = new Session();
        MessageHandler handler = new MessageHandler();
        Service service = new Service(session);
        session.setHandler(handler);
        session.connect("avdk.teamobi.com", 19128);
        service.setProviderAndClientType();
        service.doLogin("username", "password", "2.5.0");// thay đổi tk mk

        for (int i = 13000; i < 20000; i++) {
            service.requestImagePart((short) i);// muốn lấy res gì gọi hàm tương ứng
            try {
                Thread.sleep(10L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
