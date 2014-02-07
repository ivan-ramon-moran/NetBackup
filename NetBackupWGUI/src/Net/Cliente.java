package Net;

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
import java.text.DecimalFormat;

import Configuracion.Configuracion;
import Utilidades.Cola;
import Utilidades.FicheroSincronizacion;
import Utilidades.Transferencia;

public class Cliente {
	
	private Socket cliente = null;
	private ObjectOutputStream dos = null;
	private ObjectInputStream ddis = null;
	private Cola colaTransferencias = null;
	
	public Cliente(Cola colaTransferencias){
		this.colaTransferencias = colaTransferencias;
	}
	
	public int conectar(String direccion, int puerto){
		Integer iResultado = 0;
		try {
			cliente = new Socket(direccion, puerto);
			
			if (cliente.isConnected()){
				dos = new ObjectOutputStream(cliente.getOutputStream());
				ddis = new ObjectInputStream(cliente.getInputStream());
				
				enviarObjeto("k3rnel");
				enviarObjeto("admin");
				
				try {
					iResultado = (Integer)ddis.readObject();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if (iResultado != 1){
					cliente.close();
				}else
				{
					System.out.println("Conectado");
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
				
		File file = new File(transferencia.getRutaArchivo());
		
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
				dos.flush();
				System.out.println("ENVIADO");
				bytesEnviados += numBytes;
			}
			
			dis.close();
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
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
	
	public void recibirFichero(FicheroSincronizacion _fSin)
	{
		byte [] data = new byte[65536];
		int numBytes;
		DataOutputStream dos = null;
		
			
		Long fileSize = (long) 0;
		try {
			fileSize = (Long)ddis.readObject();
		} catch (ClassNotFoundException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		long tamanyoTotal = fileSize;
		double tamanyoRecibido = 0.0;
		long tamanyoVelocidad = 0;
		
		//Creamos la carpeta en el servidor
		String strRutaFichero = _fSin.getRutaFichero();
		
		File file = new File(strRutaFichero.substring(0, strRutaFichero.lastIndexOf("\\")));
		
		file.mkdirs();
		try {
			dos = new DataOutputStream(new FileOutputStream(_fSin.getRutaFichero()));
	    	final DecimalFormat df = new DecimalFormat("#.##");
	    	
	    	long lTiempoInicial = System.currentTimeMillis();
			//Recibimos el fichero
			while (fileSize > 0 && (numBytes = ddis.read(data, 0, (int)Math.min(data.length, fileSize))) != -1)  
			{  
			    dos.write(data, 0, numBytes);
			    tamanyoRecibido += numBytes;
			    fileSize -= numBytes;
			    tamanyoVelocidad += numBytes;
			    //Comprobamos si ha pasado un segundo
			    if (System.currentTimeMillis() - lTiempoInicial >= 1000){
			    	lTiempoInicial = System.currentTimeMillis();
			    	tamanyoVelocidad = 0;
			    }
			    
			    //final double fTamanyoRecibido = tamanyoRecibido;
			    //final long fTamanyoTotal = tamanyoTotal;
	
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
	
	public void recibirFichero(String nombreFichero){
		byte [] data = new byte[65536];
		int numBytes;
		DataOutputStream ddos = null;
		
		try {
			Long fileSize = (long) 0;
			try {
				fileSize = (Long)ddis.readObject();
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			long tamanyoRecibido = 0;
			
			//Creamos la carpeta en el servidor
			File file = new File(Configuracion.getInstance().getDirectorioEntrante() + "\\" + nombreFichero);
			ddos = new DataOutputStream(new FileOutputStream(file));
			
			while (fileSize > 0 && ((numBytes = ddis.read(data, 0, (int)Math.min(data.length, fileSize))) > 0))  
			{  
				ddos.write(data, 0, numBytes); 
				//ddos.flush();
				fileSize -= numBytes;  
			} 
			
			ddos.close();
			System.out.println("Recibido");
		}catch (Exception e){
			
		}
	}

}
