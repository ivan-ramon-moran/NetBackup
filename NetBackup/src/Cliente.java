import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.DecimalFormat;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;

public class Cliente {
	
	private Socket cliente = null;
	private DataOutputStream dos = null;
	private DataInputStream ddis = null;
	private Cola colaTransferencias = null;
	private ExtendedTable transferencias = null;
	private Label labelEstado = null;
	
	Cliente(Cola colaTransferencias, ExtendedTable transferencias){
		this.colaTransferencias = colaTransferencias;
		this.transferencias = transferencias;
	}
	
	public int conectar(String direccion, int puerto){
		int iResultado = 0;
		try {
			cliente = new Socket(direccion, puerto);
			
			if (cliente.isConnected()){
				dos = new DataOutputStream(cliente.getOutputStream());
				ddis = new DataInputStream(cliente.getInputStream());
				String nombreUsuario = "k3rnel";
				String password = "admin";
				dos.writeUTF(nombreUsuario);
				dos.writeUTF(password);
				iResultado = ddis.readInt();
				
				if (iResultado != 1){
					cliente.close();
					System.out.println("Nombre de usuario o contraseña incorrectos");
				}
			}
		
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return iResultado;
	}
	
	public void enviarCadena(String cadena)
	{
		try {
			dos.writeUTF(cadena);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void enviarArchivo(Transferencia transferencia)
	{
		byte [] data = new byte[65536];
		int numBytes;
		double bytesEnviados = 0;
				
		labelEstado = transferencias.getLabelEstado(transferencia.getIdTransferencia());
		File file = new File(transferencia.getRutaArchivo());
		//transferencia.setEstadoTransferencia("Enviando...");
		ProgressBar barra = (ProgressBar)transferencias.getProgressBar(transferencia.getIdTransferencia());
		HBox fila = transferencias.getFila(transferencia.getIdTransferencia());
		
		fila.setStyle("-fx-background-color: #3ffd6b");
		
		Platform.runLater(new Runnable() {
			  @Override
			  public void run() {
				  labelEstado.setText("Enviando...");
			  }
		});

		
		try{
			DataInputStream dis = new DataInputStream(new FileInputStream(transferencia.getRutaArchivo()));
			//Enviamos la operacion que vamos a realizar
			enviarCadena("0");
			enviarCadena(file.getName());
			long tamanyoFichero = file.length();
			dos.writeLong(tamanyoFichero);
			
			while ((numBytes = dis.read(data)) > 0)
			{	
				//Enviamos el bloque.
				dos.write(data, 0, numBytes);
				bytesEnviados += numBytes;
				
				if (Double.valueOf(bytesEnviados / tamanyoFichero) - barra.getProgress() > 0.01){
					barra.setProgress((Double.valueOf(bytesEnviados / tamanyoFichero)));
				}
			}
			
			dis.close();
			
			Platform.runLater(new Runnable() {
				  @Override
				  public void run() {
					  labelEstado.setText("Completado");
				  }
			});
		
			if (transferencia.getIdTransferencia() % 2 == 0){
				fila.setStyle("extended-table-fila-par");
			}
			else{
				fila.setStyle("extended-table-fila-impar");
			}	
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			barra.setProgress(1.0);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public boolean isConnected(){
		if (cliente != null)
			return cliente.isConnected();
		else
			return false;
	}
	
	/*private int buscarTransferencia(String rutaArchivo)
	{
		int indice = -1, i = 0;
		boolean bEncontrado = false;
		
		File file = new File(rutaArchivo);
		
		while (!bEncontrado){
			Transferencia transferencia = (Transferencia)transferencias.get(i);
			
			if (file.getName().equals(transferencia.getNombreArchivo())){
				indice = i;
				bEncontrado = true;
			}
			
			i++;
		}
		
		return indice;
	}
	*/
	public void cerrarSocket()
	{
		if (cliente != null){
			try {
				cliente.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
