/*
 * 
 *  
 */
package socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author js
 */
public class Servidor {

    private Socket miServicio;
    private ServerSocket socketServicio;
    private Banco cuentas;

    //APERTURA DE SOCKET
    public void conexion(int numeroPuerto) {
        try {
            socketServicio = new ServerSocket(numeroPuerto);
            System.out.println("El servidor se esta escuchando en el puerto: " + numeroPuerto);
            System.out.println("Creando cuentas de banco inicial");
            cuentas = new Banco();
            cuentas.mostrarCuentas();
            while (true) {
                miServicio = socketServicio.accept();
                Runnable connectionHandler = new ConnectionHandler(miServicio, cuentas);
                System.out.println("Server connected to the client");
                new Thread(connectionHandler).start();
            }
        } catch (Exception ex) {
            System.out.println("Error al abrir los sockets");
        }
    }

    public static void main(String[] args) {
        Servidor serv = new Servidor();
        serv.conexion(5555);
    }

}
