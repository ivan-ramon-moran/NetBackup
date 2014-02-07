package Threads;
import java.io.File;
import java.io.InputStream;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Hashtable;

import Net.Cliente;
import Utilidades.Cola;
import Utilidades.ContadorItems;
import Utilidades.Transferencia;


public class ThreadDirectorio extends Thread{
	
	private boolean bParar = false;
	private String path;
	private int iNumElementos;
	private File fDir;
	//private ArrayList<String> aElementos;
	private Cliente cliente;
	private Cola colaTransferencias;
	private Hashtable<String, String> ficheros = new Hashtable<String, String>();
	
	//Constantes
    static final long KILOBYTE = 1024;
    static final long MEGABYTE = 1048576;
    static final long GIGABYTE = 1073741824;
	
	public ThreadDirectorio(String _path, Cliente _cliente, Cola _colaTransferencias){
		this.path = _path;
		this.fDir = new File(path);
		System.out.println("gsgagsgags: " + _path);
		this.cliente = _cliente;
		this.colaTransferencias = _colaTransferencias;
				
		String [] elementos = fDir.list();
		this.iNumElementos = elementos.length;
		//aElementos = new ArrayList<String>();
		
		
		
		
		for (int i = 0; i < elementos.length; i++){
			//aElementos.add(elementos[i]);
			ficheros.put(elementos[i], elementos[i]);
		}
	}
	
	public void run(){
		while (!bParar){
			int iAuxNumElementos = fDir.list().length;
			
			if (iAuxNumElementos != this.iNumElementos){
				this.iNumElementos = iAuxNumElementos;
				String [] auxElementos = fDir.list();
				
				for (int i = 0; i < iAuxNumElementos; i++){
					//Si no esta en la tabla HASH entonces lo añadimos a la tabla y a la cola
					if (!ficheros.contains(auxElementos[i])){
						 final File file = new File(fDir.getAbsolutePath() + "\\" + auxElementos[i]);					
						 
						 //Añadimos el fichero a la tabla
						 ficheros.put(auxElementos[i], auxElementos[i]);
						 
						 if (file.isDirectory()){
							 copiarDirectorio(file.getAbsolutePath());
		              	 }
		              	 else{
		              		 if (pasaFiltro(file)){
		              			 
		              	    	colaTransferencias.encolar(new Transferencia(file.getAbsolutePath(), file.getName(), obtenerTamanyo(file.length()), "Archivo", "En cola...", ContadorItems.getNumeroItems()));
								ContadorItems.incrementarNumero();
			              	 }//FIN IF FILTRO
		              	 }
						
					}//FIN IF PRINCIPAL
				}//FIN FOR
				
			}
			
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void copiarDirectorio(String path){
		File file = new File(path);
		String [] files = file.list();
		
		for (int i = 0; i < files.length; i++){
			final File file2 = new File(file.getAbsolutePath() + "\\" + files[i]);
			
			if (file2.isDirectory())
				copiarDirectorio(path + "\\" + files[i]);
			else{
				if (pasaFiltro(file)){
					colaTransferencias.encolar(new Transferencia(file2.getAbsolutePath(), file2.getName(), obtenerTamanyo(file2.length()), "Archivo", "En cola...", ContadorItems.getNumeroItems()));
					ContadorItems.incrementarNumero();
          	    }
			}
				
		}
	}
	
	private boolean pasaFiltro(File file){
		boolean bOk = false;
		
		
		if (file.length() != 0){
			if (file.getName().charAt(0) != '.'){
				System.out.println(file.getName().substring(file.getName().length() - 3));
				
				if (file.getName().lastIndexOf(".") == file.getName().length() - 4 && !file.getName().substring(file.getName().length() - 3).equals("tmp"))
					bOk = true;
				else
					bOk = false;
			}else
				bOk = false;
		}else{
			bOk = false;
		}
			
		
		return bOk;
	}
	
	public static String obtenerTamanyo(Long fileSize){
    	String strTamanyo = "";
    	DecimalFormat df = new DecimalFormat("#.##");
    	double dFileSize = fileSize;
    	
    	if (fileSize <= KILOBYTE){
    		strTamanyo = fileSize + " Bytes";
    	}else if (fileSize > KILOBYTE && fileSize <=  MEGABYTE){
    		strTamanyo = df.format((dFileSize / KILOBYTE)) + " KBytes";
    	}else if (fileSize > MEGABYTE && fileSize <= GIGABYTE){
    		strTamanyo = df.format((dFileSize / MEGABYTE)) + " MBytes";
    	}else{
    		strTamanyo = df.format((dFileSize / GIGABYTE)) + " GBytes";
    	}
    	
    	return strTamanyo;
    }
}
