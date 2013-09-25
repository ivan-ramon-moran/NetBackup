import javafx.collections.ObservableList;


public class ThreadEnvio extends Thread {
	
	private Cliente cliente = null;
	private Cola colaTransferencias = null;
	private ExtendedTable transferencias = null;
	
	public ThreadEnvio(Cliente cliente, Cola colaTransferencias, ExtendedTable transferencias){
		this.cliente = cliente;
		this.colaTransferencias = colaTransferencias;
		this.transferencias = transferencias;
	}
	
	public void run(){
		while (true){
			while (!colaTransferencias.colaVacia()){
				cliente.enviarArchivo((Transferencia)colaTransferencias.desencolar());
			}
			
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
