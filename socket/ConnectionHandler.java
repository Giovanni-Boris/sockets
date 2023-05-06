package socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionHandler implements Runnable {

    private Socket miServicio;
    private OutputStream outputStream;
    private InputStream inputStream;
    private DataOutputStream salidaDatos;
    private DataInputStream entradaDatos;
    private Banco cuentas;

    public ConnectionHandler(Socket socket, Banco cuentas) {
        this.miServicio = socket;
        this.cuentas = cuentas;
    }

    @Override
    public void run() {
        System.out.println("Nueva conexion");
        Thread hilo = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Recibir datos");
                while (true) {
                    recibirDatosDeCliente();

                }
            }
        });
        hilo.start();

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
            String data = entradaDatos.readUTF();
            String nombre = data.substring(0, 8);
            int action = Integer.parseInt(data.substring(8, 9));
            int monto = Integer.parseInt(data.substring(9, data.length()));
            String resp = "";
            if (action == 1) {
                resp = cuentas.sacar(nombre, monto);
            } else {
                resp = cuentas.depositar(nombre, monto);
            }
            enviarDatosCuenta(resp);

        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void cerrarTodo() {
        try {
            salidaDatos.close();
            entradaDatos.close();
            miServicio.close();

        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
