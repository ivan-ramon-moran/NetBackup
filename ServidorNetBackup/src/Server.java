import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class Server {
	private ServerSocket servidor = null;
	private boolean bEscuchar = true;
	private ArrayList<ThreadCliente> listaClientes = new ArrayList<ThreadCliente>();
	private ObjectInputStream dis = null;
	private ObjectOutputStream dos = null;
	
	Server(int puerto)
	{
		try {
			servidor = new ServerSocket(puerto);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		while (bEscuchar)
		{
			Socket cliente = null;
			Usuario usuario = null;
			
			try {
				cliente = servidor.accept();
				dis = new ObjectInputStream(cliente.getInputStream());
				dos = new ObjectOutputStream(cliente.getOutputStream());
				String nombreUsuario = null, password = null;
				
				try {
					nombreUsuario = (String)dis.readObject();
					password = (String)dis.readObject();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if (nombreUsuario.equals("k3rnel") && password.equals("admin")){
					usuario = new Usuario(nombreUsuario, password);
					dos.writeObject(new Integer(1));
				}else
					dos.writeObject(new Integer(0));
					
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (usuario != null){
				ThreadCliente tCliente = new ThreadCliente(cliente, usuario, dis, dos);
		        tCliente.start();
		        listaClientes.add(tCliente);
		        System.out.println("Numero de clientes: " + listaClientes.size());
			}else{
				try {
					cliente.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
	}
}
