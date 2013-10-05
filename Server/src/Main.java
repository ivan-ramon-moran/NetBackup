import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class Main {

	public static DataBase db = null;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub 		
		Main main = new Main();
		
		try {
			main.inicializarBD();
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Server servidor = new Server(65432);
	}

	public void inicializarBD() throws ClassNotFoundException, SQLException{
		db = new DataBase("NetBackup.db");
		
		if ((db.ejecutarSentencia("create table archivos (id integer PRIMARY KEY AUTOINCREMENT, id_archivo integer, nombre string)")))
			System.out.println("ok");
		else
			System.out.println("ERROR");
		
	}
	
}
