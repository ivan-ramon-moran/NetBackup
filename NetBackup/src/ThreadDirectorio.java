import java.io.File;
import java.io.InputStream;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javafx.application.Platform;


public class ThreadDirectorio extends Thread{
	
	private boolean bParar = false;
	private String path;
	private int iNumElementos;
	private File fDir;
	private ArrayList<String> aElementos;
	private Cliente cliente;
	private ExtendedTable transferencias;
	private Cola colaTransferencias;
	//Constantes
    static final long KILOBYTE = 1024;
    static final long MEGABYTE = 1048576;
    static final long GIGABYTE = 1073741824;
	
	public ThreadDirectorio(String _path, Cliente _cliente, ExtendedTable _transferencias, Cola _colaTransferencias){
		this.path = _path;
		this.fDir = new File(path);
		this.cliente = _cliente;
		this.transferencias = _transferencias;
		this.colaTransferencias = _colaTransferencias;
		
		String [] elementos = fDir.list();
		this.iNumElementos = elementos.length;
		aElementos = new ArrayList<String>();
		
		for (int i = 0; i < elementos.length; i++)
			aElementos.add(elementos[i]);
	}
	
	public void run(){
		while (!bParar){
			int iAuxNumElementos = fDir.list().length;
			
			if (iAuxNumElementos != this.iNumElementos){
				this.iNumElementos = iAuxNumElementos;
				String [] auxElementos = fDir.list();
				ArrayList<String> aAuxElementos = new ArrayList<String>();
				
				for (int i = 0; i < auxElementos.length; i++)
					aAuxElementos.add(auxElementos[i]);
				
				for (int i = 0; i < aElementos.size(); i++){
					boolean bEncontrado = false;
					int j = 0;
					
					while (!bEncontrado && j < aAuxElementos.size()){
						
						if (aAuxElementos.get(j).equals(aElementos.get(i))){
							bEncontrado = true;
							aAuxElementos.remove(j);
						}
						
						j++;
					}
				}
				
				//AQUI MUESTRO LAS DIFERENCIAS
				for (int i = 0; i < aAuxElementos.size(); i++){
              	    final File file = new File(fDir.getAbsolutePath() + "/" + aAuxElementos.get(i));	
              	    
              	    if (pasaFiltro(file)){
						
              	    	Platform.runLater(new Runnable() {
	          			  @Override
	          			  public void run() {
	          				  transferencias.addItem(new Transferencia(file.getAbsolutePath(), file.getName(), obtenerTamanyo(file.length()), "Archivo", "En cola...", ContadorItems.getNumeroItems()));
	          			  }
	          			});
						
              	    	colaTransferencias.encolar(new Transferencia(file.getAbsolutePath(), file.getName(), obtenerTamanyo(file.length()), "Archivo", "En cola...", ContadorItems.getNumeroItems()));
						ContadorItems.incrementarNumero();
              	    }
				}
				//BORRO EL ArrayList
				aAuxElementos.clear();
				
				for (int i = 0; i < auxElementos.length; i++)
					aAuxElementos.add(auxElementos[i]);
				
				//BORRO EL ArrayList
				this.aElementos.clear();
				this.aElementos = aAuxElementos;
			}
			
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private boolean pasaFiltro(File file){
		boolean bOk = false;
		
		
		if (file.length() != 0){
			if (file.getName().charAt(0) != '.')
				bOk = true;
			else
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
