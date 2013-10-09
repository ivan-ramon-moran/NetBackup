public class Usuario {
	
	private String nombre;
	private String password;
	
	public Usuario(String nombre, String password){
		this.nombre = nombre;
		this.password = password;
	}
	
	public void setNombreUsuario(String nombre){
		this.nombre = nombre;
	}
	
	public String getNombreUsuario(){
		return this.nombre;
	}
	
	public void setPassword(String password){
		this.password = password;
	}
	
	public String getPassword(){
		return this.password;
	}
}
