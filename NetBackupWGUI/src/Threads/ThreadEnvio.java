package Threads;

import Net.Cliente;
import Utilidades.Cola;
import Utilidades.Transferencia;

public class ThreadEnvio extends Thread {
	
	private Cliente cliente = null;
	private Cola colaTransferencias = null;
	
	public ThreadEnvio(Cliente cliente, Cola colaTransferencias){
		this.cliente = cliente;
		this.colaTransferencias = colaTransferencias;
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
