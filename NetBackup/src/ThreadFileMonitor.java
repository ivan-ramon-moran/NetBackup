import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class ThreadFileMonitor extends Thread{
	
	private Path path = null;
	private Cliente cliente = null;
	private ExtendedTable transferencias = null;
	private Cola colaTransferencias = null;
	private int iNumeroThreads = 0;
	
	ThreadFileMonitor(Path path, Cliente cliente, ExtendedTable transferencias, Cola colaTransferencias){
		this.path = path;
		this.cliente = cliente;
		this.transferencias = transferencias;
		this.colaTransferencias = colaTransferencias;
	}
	
	public void run(){
		//new WatchDir(path, false, cliente, transferencias, colaTransferencias).processEvents();
		inspeccionarDirectorio("/home/k3rnel");	
	}
	
	private void inspeccionarDirectorio(String directorio){
		File fDir = new File(directorio);
		String [] dirs = fDir.list(); 
		
		for (int i = 0; i < dirs.length; i++){
			File fDir2 = new File(fDir.getAbsolutePath() + "/" + dirs[i]);
			
			if (fDir2.isDirectory() && fDir2.getName().charAt(0) != '.' && !fDir.getName().equals("NetBackup")){
				//LANZO EL THREAD Y VUELVO A LLAMAR A LA FUNCION
				ThreadDirectorio tDirectorio = new ThreadDirectorio(fDir2.getAbsolutePath(), cliente, transferencias, colaTransferencias);
				tDirectorio.start();
				iNumeroThreads++;
				inspeccionarDirectorio(fDir2.getAbsolutePath());
			}
				
		}

	}
}
