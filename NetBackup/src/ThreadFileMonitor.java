import java.io.IOException;
import java.nio.file.Path;

public class ThreadFileMonitor extends Thread{
	
	private Path path = null;
	private Cliente cliente = null;
	private ExtendedTable transferencias = null;
	private Cola colaTransferencias = null;
	
	ThreadFileMonitor(Path path, Cliente cliente, ExtendedTable transferencias, Cola colaTransferencias){
		this.path = path;
		this.cliente = cliente;
		this.transferencias = transferencias;
		this.colaTransferencias = colaTransferencias;
	}
	
	public void run(){
		try {
			new WatchDir(path, false, cliente, transferencias, colaTransferencias).processEvents();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
