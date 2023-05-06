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

    private OutputStream outputStream;
    private InputStream inputStream;

    private DataOutputStream salidaDatos;
    private DataInputStream entradaDatos;

    private boolean opcion = true;
    private Scanner scanner;
    private String esctribir;
    private Banco cuentas;

    //APERTURA DE SOCKET
    public void conexion(int numeroPuerto) {
        try {
            socketServicio = new ServerSocket(numeroPuerto);
            System.out.println("El servidor se esta escuchando en el puerto: " + numeroPuerto);
            System.out.println("Creando cuentas de banco inicial");
            cuentas = new Banco();
            cuentas.mostrarCuentas();
            miServicio = socketServicio.accept();
            Thread hilo = new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("Recibir datos");
                    while (opcion) {
                        System.out.print("SERVIDOR: ");
                        recibirDatosDeCliente();

                    }
                }
            });
            hilo.start();
            /*while (opcion) {
                 System.out.println("Enviando Datos");
                scanner = new Scanner(System.in);
                esctribir = scanner.nextLine();
                if (!esctribir.equals("CLIENTE: fin")) {
                    enviarDatosCuenta("SERVIDOR: " + esctribir);
                } else {
                    opcion = false;
                     miServicio.close();
                }
            }
            System.out.println("Termiando mi Servicio");*/
        } catch (Exception ex) {
            System.out.println("Error al abrir los sockets");
        }
    }

    public void enviarDatosCuenta(String datos) {
        try {
            outputStream = miServicio.getOutputStream();
            salidaDatos = new DataOutputStream(outputStream);
            salidaDatos.writeUTF(datos);
            salidaDatos.flush();
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void recibirDatosDeCliente() {
        try {
            inputStream = miServicio.getInputStream();
            entradaDatos = new DataInputStream(inputStream);
            String data =  entradaDatos.readUTF();
            String nombre = data.substring(0, 8);
            int action = Integer.parseInt(data.substring(8,9));
            int monto  = Integer.parseInt(data.substring(9, data.length())); 
            String resp ="";
            if(action==1) resp = cuentas.sacar(nombre, monto);
            else resp = cuentas.depositar(nombre,monto);
            enviarDatosCuenta(resp);

        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void cerrarTodo() {
        try {
            salidaDatos.close();
            entradaDatos.close();
            socketServicio.close();
            miServicio.close();

        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        Servidor serv = new Servidor();
        serv.conexion(5555);
    }

}
