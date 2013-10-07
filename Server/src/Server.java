import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class Server {
	private ServerSocket servidor = null;
	private boolean bEscuchar = true;
	private ArrayList<ThreadCliente> listaClientes = new ArrayList<ThreadCliente>();
	private DataInputStream dis = null;
	private DataOutputStream dos = null;
	
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
				dis = new DataInputStream(cliente.getInputStream());
				dos = new DataOutputStream(cliente.getOutputStream());
				String nombreUsuario = dis.readUTF();
				String password = dis.readUTF();
				
				if (nombreUsuario.equals("k3rnel") && password.equals("admin")){
					usuario = new Usuario(nombreUsuario, password);
					dos.writeInt(1);
				}else
					dos.writeInt(0);
					
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (usuario != null){
				ThreadCliente tCliente = new ThreadCliente(cliente, usuario);
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
