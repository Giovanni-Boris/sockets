package socket;
public class MainRunnable implements Runnable {

    private Cliente cliente;

    public MainRunnable(Cliente cliente) {
        this.cliente = cliente;
    }

    public static void main(String[] args) {
        Cliente cliente1 = new Cliente("Cliente1");
        Cliente cliente2 = new Cliente("Cliente2");
        System.out.println("Creo dos clientes");
        Runnable proceso1 = new MainRunnable(cliente1);
        Runnable proceso2 = new MainRunnable(cliente2);
        new Thread(proceso1).start();
        new Thread(proceso2).start();
    }

    @Override
    public void run() {
        this.cliente.conexion(5555, "localhost");
        this.cliente.enviarDatos(1, 12);
        this.cliente.enviarDatos(2, 10);
        this.cliente.enviarDatos(1, 50);
    }

}
