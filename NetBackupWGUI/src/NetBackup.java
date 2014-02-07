import Net.Cliente;
import Threads.ThreadComunicacion;
import Threads.ThreadConectar;
import Threads.ThreadEnvio;
import Threads.ThreadFileMonitor;
import Utilidades.Cola;


public class NetBackup {

	private Cliente cliente;
	private Cola colaTransferencias;
	private ThreadConectar tConectar;
	private ThreadFileMonitor tFileMonitor;
	private ThreadEnvio tEnvio;
	private ThreadComunicacion tComunicacion;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		NetBackup netBackup = new NetBackup();
		
		netBackup.iniciarPrograma();
	}

	public void iniciarPrograma(){
		//Creamos la cola de transferencias
		colaTransferencias = new Cola();
		//Creamos el cliente;		
		cliente = new Cliente(colaTransferencias);
		//Lanzams el thread de conexion
		tConectar = new ThreadConectar(cliente);
		tConectar.start();
		
		tFileMonitor = new ThreadFileMonitor(cliente, colaTransferencias);
		tFileMonitor.start();
		
		tEnvio = new ThreadEnvio(cliente, colaTransferencias);
		tEnvio.start();
		
		tComunicacion = new ThreadComunicacion(cliente);
		tComunicacion.start();
	}
	
}
