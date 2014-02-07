package Threads;
import java.io.IOException;
import java.util.ArrayList;

import Net.Cliente;
import Utilidades.Cola;
import Utilidades.ContadorItems;
import Utilidades.FicheroSincronizacion;
import Utilidades.SistemaOperativo;
import Utilidades.Transferencia;


public class ThreadSincronizacion extends Thread {
	
	private String path;
	private boolean bEjecutar = true;
	private Cliente clienteMsg = null;
	private ArrayList<FicheroSincronizacion> ptrFicheros = SistemaOperativo.ficheros;
	private Cola colaTransferencias = null;
	
	public ThreadSincronizacion(String _path, Cliente _clienteMsg, Cola _colaTransferencias){
		this.path = _path;
		this.clienteMsg = _clienteMsg;
		this.colaTransferencias = _colaTransferencias;
	}
	
	public void run(){
		boolean bExiste;
		
		while (bEjecutar){
			//El thread se ejecuta cada 60 segundos
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//Limpiamos el ArrayList para que este limpio en cada iteración
			SistemaOperativo.ficheros.clear();
			//Buscamos los ficheros en el path que nos pasan como parametro de entrada.
			SistemaOperativo.buscarDatosSensibles(path);
			//Enviamos la orden de sincronizacion
			clienteMsg.enviarCadena(new String("1"));
			clienteMsg.enviarNumero(ptrFicheros.size());
			//Enviamos todos los archivos y esperamos respuesta, si el archivo no se encuentra en el servidor, lo copiamos
			for (int i = 0; i < ptrFicheros.size(); i++){
				final FicheroSincronizacion fSin = ptrFicheros.get(i);
				final String strElementoActual = fSin.getNombreFichero();

				clienteMsg.enviarObjeto(fSin);
				
				try {
					bExiste = clienteMsg.recibirBoolean();
					System.out.println(bExiste);
					
					/*Si no existe, enviamos el fichero, mediante el socket de ficheros, añadiendolo
					 a la lista de transferencias.*/
					
					if (!bExiste){
						colaTransferencias.encolar(new Transferencia(fSin.getRutaFichero(), fSin.getNombreFichero(), Transferencia.obtenerTamanyo(fSin.getFileSize()), "Archivo", "En cola...", ContadorItems.getNumeroItems()));
						ContadorItems.incrementarNumero();					
	                }
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}	
			
			//El thread se ejecuta cada 60 segundos
			try {
				//Si la cola de transferencias no esta vacia, volvemos a esperar X tiempo otra vez
				Thread.sleep(55000);
				while (!colaTransferencias.colaVacia())
					Thread.sleep(55000);
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
