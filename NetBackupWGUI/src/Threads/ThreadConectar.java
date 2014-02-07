package Threads;

import Net.Cliente;

public class ThreadConectar extends Thread{
	
	private boolean bEjecutar = true;
	private Cliente cliente;
	
	public ThreadConectar(Cliente _cliente){
		this.cliente = _cliente;
	}
	
	public void run(){
		while (!cliente.isConnected() && bEjecutar){
    		cliente.conectar("localhost", 65432);
    		try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
	}
	
	public void pararThread(){
		this.bEjecutar = false;
	}
}
