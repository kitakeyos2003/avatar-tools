/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package me.kitakeyos.network;

/**
 *
 * @author Kitak
 */
public interface ISession {

    boolean isConnected();

    void setHandler(IMessageHandler messageHandler);

    void connect(String host, int port);

    void sendMessage(Message message);

    void close();
}
