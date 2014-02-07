package Utilidades;

public class UsuarioSistema {

	private String usuario = null;
	
	public UsuarioSistema(){
		usuario = System.getProperty("user.name");
	}
	
	public String getUsuario(){
		return this.usuario;
	}
}
