import java.io.File;
import java.util.ArrayList;


public class SistemaOperativo {
	
	public static ArrayList<String> ficheros = new ArrayList<String>();
	
	public static String getSistema(){
		return System.getProperty("os.name" );
	}
	
	public static boolean isWindows(){
		return (getSistema().contains("Win") || getSistema().contains("win"));
	}
	
	public static boolean isLinux(){
		return (getSistema().contains("Lin") || getSistema().contains("lin"));
	}
	
	
	public static void buscarDatosSensibles(String ruta){
		File file = new File(ruta);
		String [] elementos = file.list();
		
		if (elementos != null){
			for (int i = 0; i < elementos.length; i++){
				File file2 = new File(ruta + "/" + elementos[i]);
				
				if (file2.isDirectory()){
					buscarDatosSensibles(ruta + "/" + file2.getName());
				}
				else{
					ficheros.add(file2.getName());
				}
			}
		}
	}
	
}
