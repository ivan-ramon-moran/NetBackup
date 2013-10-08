import java.io.IOException;
import java.util.ArrayList;

import javafx.application.Platform;
import javafx.scene.control.Label;


public class ThreadSincronizacion extends Thread {
	
	private String path;
	private boolean bEjecutar = true;
	private Cliente clienteMsg = null;
	private ArrayList<FicheroSincronizacion> ptrFicheros = SistemaOperativo.ficheros;
	private Label labelArchivos = null;
	
	public ThreadSincronizacion(String _path, Cliente _clienteMsg, Label _labelArchivos){
		this.path = _path;
		this.clienteMsg = _clienteMsg;
		this.labelArchivos = _labelArchivos;
	}
	
	public void run(){
		boolean bExiste;
		
		while (bEjecutar){
			//Limpiamos el ArrayList para que este limpio en cada iteración
			SistemaOperativo.ficheros.clear();
			//Buscamos los ficheros en el path que nos pasan como parametro de entrada.
			SistemaOperativo.buscarDatosSensibles(path);
			//Enviamos la orden de sincronizacion
			clienteMsg.enviarCadena(new String("1"));
			clienteMsg.enviarNumero(ptrFicheros.size());
			//Enviamos todos los archivos y esperamos respuesta, si el archivo no se encuentra en el servidor, lo copiamos
			for (int i = 0; i < ptrFicheros.size(); i++){
				final String strElementoActual = ptrFicheros.get(i).getNombreFichero();
				clienteMsg.enviarObjeto(ptrFicheros.get(i));
				/*
				try {
					bExiste = clienteMsg.recibirBoolean();
					System.out.println(bExiste);
					
					Si no existe, enviamos el fichero, mediante el socket de ficheros, añadiendolo
					 a la lista de transferencias.
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				*/
				
				Platform.runLater(new Runnable(){
					public void run(){
						labelArchivos.setText(strElementoActual);
					}
				});
			}	
				
			
			//El thread se ejecuta cada 60 segundos
			try {
				Thread.sleep(60000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void pararThread(){
		bEjecutar = false;
	}
	
}
