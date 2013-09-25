
public class ThreadTransferencia extends Thread{
	
	private Cliente cliente = null;
	private Cola colaTransferencias = null;
	
	public ThreadTransferencia(Cliente cliente, Cola colaTransferencias){
		this.cliente = cliente;
		this.colaTransferencias = colaTransferencias;
	}
	
	public void run(){
		while (!colaTransferencias.colaVacia()){
			Transferencia transferencia = (Transferencia)colaTransferencias.desencolar();
			cliente.enviarArchivo(transferencia);
		}
	}
	
	
}
