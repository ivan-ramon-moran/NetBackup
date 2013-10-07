import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class DataBase {
	
	private Statement stmt = null;
	private Connection connection = null;
	
	public DataBase(String nombreDB) throws ClassNotFoundException, SQLException{
		Class.forName("org.sqlite.JDBC");
		connection = DriverManager.getConnection("jdbc:sqlite:" + nombreDB);
		stmt = connection.createStatement();
	}
	
	public boolean ejecutarSentencia(String sentencia) throws SQLException{
		boolean bResultado = false;
		
		if (stmt.executeUpdate(sentencia) == 0)
			bResultado = true;
		
		return bResultado;
	}
	
	public void cerrarConexion() throws SQLException{
		if (connection != null)
			connection.close();
	}
	
	public ResultSet ejecutarConsulta(String consulta) throws SQLException{
		System.out.println("Por aki paso");
		ResultSet rs = stmt.executeQuery(consulta);
		return rs;
	}
}
