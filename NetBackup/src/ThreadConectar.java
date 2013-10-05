
public class ThreadConectar extends Thread{
	
	private Cliente cliente = null;
	private boolean bEjecutar = true;
	
	public ThreadConectar(Cliente cliente){
		this.cliente = cliente;
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
