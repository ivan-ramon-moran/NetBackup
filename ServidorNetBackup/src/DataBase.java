import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class DataBase {
	
	private static Statement stmt = null;
	private static Connection connection = null;
	
	public static void iniciarConexion(String nombreDB){
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:" + nombreDB);
			stmt = connection.createStatement();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static boolean ejecutarSentencia(String sentencia){
		boolean bResultado = false;
		
	
		try {
			if (stmt.executeUpdate(sentencia) != 0)
				bResultado = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return bResultado;
	}
	
	public static void cerrarConexion() throws SQLException{
		if (connection != null)
			connection.close();
	}
	
	public static ResultSet ejecutarConsulta(String consulta){
		ResultSet rs = null;
		
		try {
			rs = stmt.executeQuery(consulta);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return rs;
	}
}
