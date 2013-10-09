import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;

public class Cliente {
	
	private Socket cliente = null;
	private ObjectOutputStream dos = null;
	private ObjectInputStream ddis = null;
	private Cola colaTransferencias = null;
	private ExtendedTable transferencias = null;
	private Label labelEstado = null;
	
	Cliente(Cola colaTransferencias, ExtendedTable transferencias){
		this.colaTransferencias = colaTransferencias;
		this.transferencias = transferencias;
	}
	
	public int conectar(String direccion, int puerto){
		Integer iResultado = 0;
		try {
			cliente = new Socket(direccion, puerto);
			
			if (cliente.isConnected()){
				dos = new ObjectOutputStream(cliente.getOutputStream());
				ddis = new ObjectInputStream(cliente.getInputStream());
				String nombreUsuario = "k3rnel";
				String password = "admin";
				enviarObjeto(nombreUsuario);
				enviarObjeto(password);
				try {
					iResultado = (Integer)ddis.readObject();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
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
		enviarObjeto(cadena);
	}
	
	public void enviarNumero(int _numero){
		enviarObjeto(new Integer(_numero));
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
			enviarCadena(new String("0"));
			enviarCadena(new String(file.getName()));
			Long tamanyoFichero = new Long(file.length());
			dos.writeObject(tamanyoFichero);
			
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
	
	public void enviarObjeto(Object object){
		try {
			dos.writeObject(object);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Object recibirObjeto(){
		Object obj = null;
		
		try {
			obj =  ddis.readObject();
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return obj;
	}
	
	public boolean recibirBoolean() throws IOException{
		boolean reply = true;
		try {
			reply = (Boolean)ddis.readObject();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return reply;
	}
	
	public void recibirFichero(FicheroSincronizacion fSin)
	{
		byte [] data = new byte[65536];
		int numBytes;
		DataOutputStream dos = null;
		
			
		System.out.println("RECIBIENDO FICHERO");
		Long fileSize = (long) 0;
		
		try {
			fileSize = (Long)ddis.readObject();
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		System.out.println("File Size: " + fileSize);
		long tamanyoRecibido = 0;
		
		//Creamos la carpeta en el servidor
		String strRutaFichero = fSin.getRutaFichero();
		
		File file = new File(strRutaFichero.substring(0, strRutaFichero.lastIndexOf("\\")));
		
		file.mkdirs();
		try {
			dos = new DataOutputStream(new FileOutputStream(fSin.getRutaFichero()));
			
			//Recibimos el fichero
			while (fileSize > 0 && (numBytes = ddis.read(data, 0, (int)Math.min(data.length, fileSize))) != -1)  
			{  
			    dos.write(data, 0, numBytes);  
			    fileSize -= numBytes;  
			}  
			
			dos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Recibido");
	}

}
