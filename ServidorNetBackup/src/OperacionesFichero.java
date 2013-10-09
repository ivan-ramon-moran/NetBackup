
public class OperacionesFichero {

	public static String obtenerExtension(String nombreFichero){
		String strExtension = "";
		
		strExtension = nombreFichero.substring(nombreFichero.lastIndexOf("."), nombreFichero.length());
		
		return strExtension;
	}
	
}
