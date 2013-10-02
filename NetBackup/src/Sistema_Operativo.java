
public class Sistema_Operativo {
	private String sistema;
	
	public Sistema_Operativo(){
		sistema = System.getProperty("os.name" );
	}
	
	public String getSistema(){
		return sistema;
	}
}
