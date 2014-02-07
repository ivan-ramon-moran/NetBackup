package Threads;
import java.net.SocketException;

import Net.Cliente;
import Utilidades.FicheroSincronizacion;


public class ThreadRestaurar extends Thread {
	
	private Cliente clienteMsg = null;
	
	public ThreadRestaurar(Cliente _clienteMsg){
		clienteMsg = _clienteMsg;
	}
	
	public void run(){
		if (clienteMsg != null)
		{
			if (clienteMsg.isConnected()){
				//Enviamos al servidor la orden de que vamos a restaurar los ficheros
		    	clienteMsg.enviarCadena(new String("2"));
		    	final Integer numTransferencias = (Integer)clienteMsg.recibirObjeto();
		    	//Recibimos los datos de las transferencias
		    	for (int i = 0; i < numTransferencias; i++){
		    		final FicheroSincronizacion fSin = (FicheroSincronizacion)clienteMsg.recibirObjeto();
		    		final int iTransferenciaActual = i + 1;
		    		
		    		System.out.println(fSin.getNombreFichero());
		    		clienteMsg.recibirFichero(fSin);
		    	}
		    	
			}else{
				System.out.println("asasas");
			}
		}else{
			System.out.println("asasas");
		}
		
		
	}
	
}
