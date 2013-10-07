import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;


public class ThreadCliente extends Thread {
	private Socket cliente = null;
	private DataInputStream dis = null;
	private DataOutputStream ddos = null;
	private Usuario usuario = null;
	private static final String path = "C:\\Users\\K3rneL\\Desktop\\NetBackup";
	
	ThreadCliente(Socket cliente, Usuario usuario)
	{
		this.cliente = cliente;
		this.usuario = usuario;
		
		if (cliente.isConnected())
			System.out.println("Conectado!!!");

	}
	
	public void run()
	{
		String mensaje = "";

		try {
			dis = new DataInputStream(cliente.getInputStream());
			ddos = new DataOutputStream(cliente.getOutputStream());

			//Leemos la operacion
			mensaje = dis.readUTF();
			
			while (!mensaje.equals("salir")){
				System.out.println(mensaje);
				
				if (mensaje.equals("0"))
					recibirFichero();
				
				mensaje = dis.readUTF();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void recibirFichero()
	{
		byte [] data = new byte[65536];
		int numBytes;
		DataOutputStream dos = null;
		
		try {
			String fileName = dis.readUTF();
			System.out.println("RECIBIENDO FICHERO");
			long fileSize = dis.readLong();
			System.out.println("File Size: " + fileSize);
			long tamanyoRecibido = 0;
			
			System.out.println("Nombre fichero: " + fileName);
			//Creamos la carpeta en el servidor
			File file = new File(path  + "\\" + usuario.getNombreUsuario() + "\\" + fileName);
			
			boolean bExiste = buscarDirectorio(fileName);
			
			if (!bExiste){
				file.mkdir();
			    
				if (fileName.contains(".")){
					String extension = fileName.substring(fileName.lastIndexOf("."), fileName.length());
					dos = new DataOutputStream(new FileOutputStream(path + "\\" + usuario.getNombreUsuario() + "/" + file.getName() + "/" + "1." + extension));
				}else{
					System.out.println(path + usuario.getNombreUsuario() + "\\" + file.getName() + "/" + 1);
					dos = new DataOutputStream(new FileOutputStream(path + "\\" + usuario.getNombreUsuario() + "/" + file.getName() + "/" + 1));
				}
				
				try {
					if (Main.db.ejecutarSentencia("INSERT INTO archivos (id_archivo, nombre) VALUES (1, '" + fileName +"')"))
						System.out.println("AÃ±adido correctamente");
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				try {
					ResultSet rs = Main.db.ejecutarConsulta("SELECT * FROM archivos WHERE nombre LIKE '" + fileName + "'");
					int iNumeroFilas = 0;
		
					while (rs.next())
						iNumeroFilas++;
					
					if (fileName.contains(".")){
						String extension = fileName.substring(fileName.lastIndexOf("."), fileName.length());
						dos = new DataOutputStream(new FileOutputStream(path + "\\" + usuario.getNombreUsuario() + "/" + file.getName() + "/" + (iNumeroFilas + 1) + "." + extension));
					}else{
						dos = new DataOutputStream(new FileOutputStream(path + "\\" + usuario.getNombreUsuario() + "/" + file.getName() + "/" + (iNumeroFilas + 1)));
					}
					
					try {
						if (Main.db.ejecutarSentencia("INSERT INTO archivos (id_archivo, nombre) VALUES (" + (iNumeroFilas + 1) +", '" + fileName +"')"))
							System.out.println("Añadido correctamente");
						
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
					
			while (fileSize > 0 && (numBytes = dis.read(data, 0, (int)Math.min(data.length, fileSize))) != -1)  
			{  
			    dos.write(data, 0, numBytes);  
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
		
		File file = new File(path + "\\k3rnel");
		System.out.println(path + "\\k3rnel");
		String [] directorios = file.list();
		
		for (int i = 0; i < directorios.length; i++)
			if (directorios[i].equals(dir))
				bResultado = true;
		
		return bResultado;
	}
}
