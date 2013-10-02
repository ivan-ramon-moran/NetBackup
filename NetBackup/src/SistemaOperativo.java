
public class SistemaOperativo {
	public static String getSistema(){
		return System.getProperty("os.name" );
	}
	
	public static boolean isWindows(){
		return (getSistema().contains("Win") || getSistema().contains("win"));
	}
	
	public static boolean isLinux(){
		return (getSistema().contains("Lin") || getSistema().contains("lin"));
	}
	
	
}
