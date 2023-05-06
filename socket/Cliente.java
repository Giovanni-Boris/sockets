package socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author js
 */
public class Cliente {

    private Socket socketCliente;

    private InputStream inputStream;
    private OutputStream outputStream;

    private DataInputStream entradaDatos;
    private DataOutputStream SalidaDatos;

    private boolean opcion = true;

    private Scanner scanner;
    private String esctribir = "SI";
    private String nombre;
    private int action = 0;
    private int monto = 0;
    public Cliente(String nombre){
        this.nombre = nombre;
    }
    public void conexion(int numeroPuerto, String ipMaquina) {
        try {
            socketCliente = new Socket(ipMaquina, numeroPuerto);
            Thread hilo1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (opcion) {
                        escucharDatos(socketCliente);
                        //System.out.print("CLIENTE: ");
                    }
                }
            });
            hilo1.start();
            /*while (opcion) {
                scanner = new Scanner(System.in);
                //esctribir = scanner.nextLine();
                if (!esctribir.equals("SERVIDOR: fin")) {
                    if (action == 0 && monto == 0) {
                        System.out.println(nombre+ "  Ingresar action 1: retirar 2: depositar y monto ");
                        action = scanner.nextInt();
                        monto = scanner.nextInt();
                    }
                    enviarDatos(action,monto);
                    action = 0;
                    monto = 0;
                } else {
                    opcion = false;
                    cerrarTodo();
                }
            }*/

        } catch (Exception ex) {
            System.out.println("ERROR AL ABRIR LOS SOCKETS CLIENTE " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void escucharDatos(Socket socket) {
        try {
            inputStream = socket.getInputStream();
            entradaDatos = new DataInputStream(inputStream);
            System.out.println(entradaDatos.readUTF());
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void enviarDatos(int action,int monto) {
        try {
            if (action==1) System.out.println(nombre +"  quiere sacar "+ monto);
            else System.out.println(nombre +"  quiere depositar "+ monto);
            String datos = nombre+action+monto;
            outputStream = socketCliente.getOutputStream();
            SalidaDatos = new DataOutputStream(outputStream);
            SalidaDatos.writeUTF(datos);
            SalidaDatos.flush();
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void cerrarTodo() {
        try {
            SalidaDatos.close();
            entradaDatos.close();
            socketCliente.close();
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        Cliente cli1 = new Cliente("Cliente1");
        cli1.conexion(5555, "localhost");
        cli1.enviarDatos(1, 12);
        cli1.enviarDatos(2, 10);
        cli1.enviarDatos(1, 50);
        cli1.enviarDatos(1, 20);
       
    }
}
