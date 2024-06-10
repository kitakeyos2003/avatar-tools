/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package me.kitakeyos.network;

/**
 *
 * @author Kitak
 */
public interface IMessageHandler {

    void onMessage(Message message);

    void onConnectionFail();

    void onDisconnected();

    void onConnectOK();
}
