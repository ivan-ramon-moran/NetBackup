
public class Cola {
	
	private Nodo inicio = null;
	private Nodo fin = null;
	private int numElementos;
	
	class Nodo{
		private Nodo sig;
		private Object objeto;
	}
	
	public Cola(){
		this.numElementos = 0;
	}
	
	public void encolar(Object objeto){
		Nodo nodo_nuevo = new Nodo();
		
		nodo_nuevo.objeto = objeto;
		nodo_nuevo.sig = null;
		
		if (inicio == null){
			inicio = nodo_nuevo;
			fin = nodo_nuevo;
		}else{
			this.fin.sig = nodo_nuevo;
			this.fin = nodo_nuevo;
		}
		
		this.numElementos++;
	}
	
	public Object desencolar(){
		Nodo nod_aux = this.inicio;
		
		this.numElementos--;
		
		if (!colaVacia()){
			this.inicio = inicio.sig;
			return nod_aux.objeto;
		}else{
			this.inicio = null;
			return null;
		}
	}
	
	public int getNumElementos(){
		return this.numElementos;
	}
	
	public boolean colaVacia(){
		return (this.inicio == null);
	}
	
	public void mostrarElementos(){
		Nodo nod_aux = this.inicio;
		
		while (nod_aux != null){
			System.out.println(nod_aux.objeto);
			nod_aux = nod_aux.sig;
		}
	}
	
}
