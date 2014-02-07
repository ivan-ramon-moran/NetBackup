package Utilidades;
import java.io.Serializable;

public class FicheroSincronizacion implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String strRutaFichero;
	private String strNombreFichero;
	private long fileSize;
	
	public FicheroSincronizacion(String _strRutaFichero, String _strNombreFichero, long _fileSize){
		this.strNombreFichero = _strNombreFichero;
		this.strRutaFichero = _strRutaFichero;
		this.fileSize = _fileSize;
	}
	
	public void setRutaFichero(String _strRutaFichero){
		this.strRutaFichero = _strRutaFichero;
	}
	
	public void setNombreFichero(String _strNombreFichero){
		this.strNombreFichero = _strNombreFichero;
	}
	
	public String getRutaFichero(){
		return this.strRutaFichero;
	}
	
	public String getNombreFichero(){
		return this.strNombreFichero;
	}
	
	public long getFileSize(){
		return this.fileSize;
	}
	
}
