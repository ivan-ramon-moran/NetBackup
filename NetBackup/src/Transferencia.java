public class Transferencia {
	
	private String nombreArchivo;
	private String rutaArchivo;
	private String tamanyoArchivo;
	private String tipoArchivo;
	private String estadoTransferencia;
	private int idTransferencia;
	
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
}

