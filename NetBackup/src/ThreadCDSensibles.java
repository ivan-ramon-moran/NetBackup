import java.io.File;
import java.util.ArrayList;


public class ThreadCDSensibles extends Thread{
	
	private String rutaDeInicio;
	private ArrayList<String> fotosEncontradas = null;
	private ArrayList<String> audiosEncontrados = null;
	private ArrayList<String> docuEncontrados = null;
	private Cola colaTransferencias = null;
	private ExtendedTable transferencias = null;
	
	public ThreadCDSensibles(String rutaDeInicio, Cola colaTransferencias, ExtendedTable transferencias){
		this.rutaDeInicio = rutaDeInicio;
		fotosEncontradas = new ArrayList<String>();
		audiosEncontrados = new ArrayList<String>();
		docuEncontrados = new ArrayList<String>();
		this.colaTransferencias = colaTransferencias;
		this.transferencias = transferencias;
	}
	
	public void run(){
		buscarDatosSensibles(rutaDeInicio);
		VentanaDatosEncontrados ventana = new VentanaDatosEncontrados(fotosEncontradas, audiosEncontrados, docuEncontrados, colaTransferencias,transferencias);
	}
	
	private void buscarDatosSensibles(String ruta){
		File file = new File(ruta);
		
		String [] elementos = file.list();
		
		if (elementos != null)
		for (int i = 0; i < elementos.length; i++){
			File file2 = new File(ruta + "/" + elementos[i]);
			
			if (file2.isDirectory()){
				buscarDatosSensibles(ruta + "/" + file2.getName());
			}
			else{
				if (elementos[i].contains(".jpg")){
					fotosEncontradas.add(ruta + "/" + file2.getName());
				}else if (elementos[i].contains(".mp3")){
					audiosEncontrados.add(ruta + "/" + file2.getName());
				}else if (elementos[i].contains(".docx") || elementos[i].contains(".txt") || elementos[i].contains(".pdf")){
					docuEncontrados.add(ruta + "/" + file2.getName());
				}
			}
		}
	
	}
}
