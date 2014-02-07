package Utilidades;

import java.text.DecimalFormat;

public class Transferencia {
	
	private String nombreArchivo;
	private String rutaArchivo;
	private String tamanyoArchivo;
	private String tipoArchivo;
	private String estadoTransferencia;
	private int idTransferencia;
	
	 //Constantes
    public static final long KILOBYTE = 1024;
    public static final long MEGABYTE = 1048576;
    public static final long GIGABYTE = 1073741824;
	
	public Transferencia(String rutaArchivo, String nombreArchivo, String tamanyoArchivo, String tipoArchivo, String estadoTransferencia, int idTransferencia){
		this.rutaArchivo = rutaArchivo;
		this.nombreArchivo = nombreArchivo;
		this.tipoArchivo = tipoArchivo;
		this.estadoTransferencia = estadoTransferencia;
		this.tamanyoArchivo = tamanyoArchivo;
		this.idTransferencia = idTransferencia;
	}
	
	public void setNombreArchivo(String nombreArchivo){
		this.nombreArchivo = nombreArchivo;
	}
	
	public void setTipoArchivo(String tipoArchivo){
		this.tipoArchivo = tipoArchivo;
	}
	
	public void setTamanyoArchivo(String tamanyoArchivo){
		this.tamanyoArchivo = tamanyoArchivo;
	}
	
	public void setEstadoTransferencia(String estadoTransferencia){
		this.estadoTransferencia = estadoTransferencia;
	}
	
	public String getNombreArchivo(){
		return this.nombreArchivo;
	}
	
	public String getTipoArchivo(){
		return this.tipoArchivo;
	}
	
	public String getTamanyoArchivo(){
		return this.tamanyoArchivo;
	}
	
	public String getEstadoTransferencia(){
		return this.estadoTransferencia;
	}
	
	public int getIdTransferencia(){
		return this.idTransferencia;
	}
	
	public String getRutaArchivo(){
		return this.rutaArchivo;
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

