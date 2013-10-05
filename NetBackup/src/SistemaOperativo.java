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
	
	public static String getPath(String nombre){
		String ruta = "";
		
		if (isWindows())
			ruta = ("C:\\Users\\" + nombre + "\\Desktop");
		
		if (isLinux())
			ruta = ("/home/" + nombre + "/Escritorio");
		
		
		return ruta;
	}
	
	public static char getSeparador (){
		char barra = 0;
		
		if (isWindows())
			barra = '\\';
		
		if (isLinux())
			barra = '/';
		
		return barra;
	}
}
