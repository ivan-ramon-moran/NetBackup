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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ThreadCliente extends Thread {
	private Socket cliente = null;
	private ObjectInputStream dis = null;
	private ObjectOutputStream ddos = null;
	private Usuario usuario = null;
	private static final String path = "/home/k3rnel/Escritorio/NetBackup";
	
	ThreadCliente(Socket cliente, Usuario usuario, ObjectInputStream _dis, ObjectOutputStream _ddos)
	{
		this.cliente = cliente;
		this.usuario = usuario;
		this.ddos = _ddos;
		this.dis = _dis;
		
		if (cliente.isConnected())
			System.out.println("Conectado!!!");

	}
	
	public void run()
	{
		String mensaje = "";

		//Leemos la operacion
		mensaje = (String)recibirObjeto();
		
		while (!mensaje.equals("salir")){
			System.out.println("Mensaje: "  + mensaje);
			
			if (mensaje.equals("0"))
				recibirFichero();
			else if (mensaje.equals("1"))
				sincronizar();
			else if (mensaje.equals("2"))
				restaurar();
			else if (mensaje.equals("3"))
				exploracion();
			else if (mensaje.equals("4"))
				enviarFichero();
			
			mensaje =  (String)recibirObjeto();
		}
		
	}
	
	public void recibirFichero()
	{
		byte [] data = new byte[65536];
		int numBytes;
		DataOutputStream dos = null;
		
		try {
			String fileName = "";
			try {
				fileName = (String)dis.readObject();
			} catch (ClassNotFoundException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			System.out.println("RECIBIENDO FICHERO");
			Long fileSize = (long) 0;
			try {
				fileSize = (Long)dis.readObject();
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.out.println("File Size: " + fileSize);
			long tamanyoRecibido = 0;
			
			System.out.println("Nombre fichero: " + fileName);
			//Creamos la carpeta en el servidor
			File file = new File(path  + "/" + usuario.getNombreUsuario() + "/" + fileName);
			
			boolean bExiste = buscarDirectorio(fileName);
			
			if (!bExiste){
				file.mkdir();
			    
				if (fileName.contains(".")){
					String extension = fileName.substring(fileName.lastIndexOf("."), fileName.length());
					dos = new DataOutputStream(new FileOutputStream(path + "/" + usuario.getNombreUsuario() + "/" + file.getName() + "/" + "1" + extension));
				}else{
					System.out.println(path + usuario.getNombreUsuario() + "/" + file.getName() + "/1");
					dos = new DataOutputStream(new FileOutputStream(path + "/" + usuario.getNombreUsuario() + "/" + file.getName() + "/1"));
				}
				
				if (DataBase.ejecutarSentencia("INSERT INTO archivos (id_archivo, nombre) VALUES (1, '" + fileName +"')"))
					System.out.println("Añadido correctamente");
					
			
			}else{
				
				ResultSet rs = DataBase.ejecutarConsulta("SELECT * FROM archivos WHERE nombre LIKE '" + fileName + "'");
				int iNumeroFilas = 0;
	
				try {
					while (rs.next())
						iNumeroFilas++;
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if (fileName.contains(".")){
					String extension = fileName.substring(fileName.lastIndexOf("."), fileName.length());
					dos = new DataOutputStream(new FileOutputStream(path + "/" + usuario.getNombreUsuario() + "/" + file.getName() + "/" + (iNumeroFilas + 1) + "." + extension));
				}else{
					dos = new DataOutputStream(new FileOutputStream(path + "/" + usuario.getNombreUsuario() + "/" + file.getName() + "/" + (iNumeroFilas + 1)));
				}
				
				
				if (DataBase.ejecutarSentencia("INSERT INTO archivos (id_archivo, nombre) VALUES (" + (iNumeroFilas + 1) +", '" + fileName +"')"))
					System.out.println("A�adido correctamente");
						
			}	
			
			System.out.println("Minimo entre: " + (int)Math.min(data.length, fileSize) );
			while ((fileSize > 0) && (numBytes = dis.read(data, 0, (int)Math.min(data.length, fileSize))) > 0)  
			{  
				dos.write(data, 0, numBytes); 
				dos.flush();
				fileSize -= numBytes;  
			} 
			
			dos.close();
			System.out.println("Recibido");
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	boolean buscarDirectorio(String dir){
		boolean bResultado = false;
		
		File file = new File(path + "/k3rnel");
		System.out.println("El path es: " + path + "/k3rnel");
		String [] directorios = file.list();
		
		for (int i = 0; i < directorios.length; i++)
			if (directorios[i].equals(dir))
				bResultado = true;
		
		return bResultado;
	}
	
	public void sincronizar(){
		try {
			Integer iNumeroElementos = 0;
			try {
				iNumeroElementos = (Integer)dis.readObject();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			System.out.println("Numero de elementos: " + iNumeroElementos);
			//Borramos el contenido de la tabla que tenemos en la base de datos
			DataBase.ejecutarSentencia("DELETE FROM sincronizacion");
			
			for (int i = 0; i < iNumeroElementos; i++){
				//Recibimos la cadena del servidor con el nombre de fichero
				FicheroSincronizacion fichero = (FicheroSincronizacion)recibirObjeto();
				System.out.println("Objeto: " + fichero.getNombreFichero() + " Ruta: " + fichero.getRutaFichero());
				//Creamos un nuevo file para mirar si existe o no el archivo
				File file = new File(path + "/k3rnel/" + fichero.getNombreFichero());
				//Existe? Si no existe enviamos un boolean para indicar que no existe al cliente, y esperamos
				//la transferencia
				if (file.exists())
					ddos.writeObject(new Boolean(true));
				else
					ddos.writeObject(new Boolean(false));
				
				//A�adimos a la base de datos el fichero
				DataBase.ejecutarSentencia("INSERT INTO sincronizacion (id_usuario, nombre, ruta) VALUES (" + 1 +", '" + fichero.getNombreFichero() +"','" + fichero.getRutaFichero() + "')");
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Object recibirObjeto(){
		Object obj = null;
		
		try {
			obj = dis.readObject();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return obj;
	}
	
	private void restaurar(){
		ArrayList<FicheroSincronizacion> ficherosRestaurar = leerUltimaSincronizacion();
		//Enviamos el numero de objetos que vamos a enviar
		enviarObjeto(new Integer(ficherosRestaurar.size()));
		//Enviamos los datos de las transferencias y la transferencia binaria de cada uno.
		for (int i = 0; i < ficherosRestaurar.size(); i++){
			FicheroSincronizacion fSin = ficherosRestaurar.get(i);
			//Enviamos el objeto con los datos del fichero
			enviarObjeto(fSin);
			//Enviamos el fichero
			String nombreFichero = fSin.getNombreFichero();
			String strExtension = OperacionesFichero.obtenerExtension(nombreFichero);
			enviarArchivo(path + "/k3rnel/" + fSin.getNombreFichero() + "/1" + strExtension);
		}
	}
	
	private ArrayList<FicheroSincronizacion> leerUltimaSincronizacion(){
		ArrayList<FicheroSincronizacion> ficherosRestaurar = new ArrayList<FicheroSincronizacion>(); 
		ResultSet rs = DataBase.ejecutarConsulta("SELECT * FROM sincronizacion WHERE id_usuario = 1");
		
		try {
			while(rs.next()){
				FicheroSincronizacion fSin = new FicheroSincronizacion(rs.getString("ruta"), rs.getString("nombre"));
				ficherosRestaurar.add(fSin);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ficherosRestaurar;
	}
	
	private void enviarObjeto(Object object){
		try {
			ddos.writeObject(object);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void enviarArchivo(String rutaArchivo){
		byte [] data = new byte[65536];
		int numBytes;
		
		File file = new File(rutaArchivo);
		
		try{
			DataInputStream dis = new DataInputStream(new FileInputStream(file));
			Long tamanyoFichero = new Long(file.length());
			enviarObjeto(tamanyoFichero);
			
			while ((numBytes = dis.read(data)) > 0)
			{	
				//Enviamos el bloque.
				ddos.write(data, 0, numBytes);
				ddos.flush();
			}
			
			dis.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	private void exploracion(){
		File file = new File(path + "/" + usuario.getNombreUsuario());
		String [] elementos = file.list();
		String [] elementosFiltrados = filtrar(elementos);
		Integer iNumElementos = elementosFiltrados.length; 
				
		try {
			ddos.writeObject(iNumElementos);
			
			for (int i = 0; i < iNumElementos; i++){
				ddos.writeObject(elementosFiltrados[i]);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private String [] filtrar(String [] elementos){
		ArrayList<String> resultado = new ArrayList();
		
		for (int i = 0; i < elementos.length; i++){
			if (elementos[i].charAt(0) != '.' && elementos[i].charAt(0) != '~')
				resultado.add(elementos[i]);
		}
		
		String [] elementosFil = new String[resultado.size()];
		
		for (int i = 0; i < resultado.size(); i++){
			elementosFil[i] = resultado.get(i);
		}
		
		return elementosFil;
	}
	
	private void enviarFichero(){
		try {
			String nombreFichero = (String)dis.readObject();
			System.out.println(nombreFichero);
			enviarArchivo(path + "/" + usuario.getNombreUsuario() + "/" + nombreFichero);
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
