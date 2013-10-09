
public class ThreadRestaurar extends Thread {
	
	private Cliente clienteMsg = null;
	
	public ThreadRestaurar(Cliente _clienteMsg){
		clienteMsg = _clienteMsg;
	}
	
	public void run(){
		//Enviamos al servidor la orden de que vamos a restaurar los ficheros
    	clienteMsg.enviarCadena(new String("2"));
    	Integer numTransferencias = (Integer)clienteMsg.recibirObjeto();
    	//Recibimos los datos de las transferencias
    	for (int i = 0; i < numTransferencias; i++){
    		FicheroSincronizacion fSin = (FicheroSincronizacion)clienteMsg.recibirObjeto();
    		System.out.println(fSin.getNombreFichero());
    		clienteMsg.recibirFichero(fSin);
    	}
	}
	
}
