package Threads;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import Net.Cliente;

public class ThreadComunicacion extends Thread{

	private boolean bParar = false;
	private BufferedReader br;
	private PrintWriter out;
	private Cliente cliente;
	
	public ThreadComunicacion(Cliente _cliente){
		this.cliente = _cliente;
		
		try {
			br = new BufferedReader(new FileReader("C:\\Users\\K3rNeL\\Desktop\\communication"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void run(){
		String linea;
		String orden;
		
		while (!bParar){
			
			try {
				
				while (!br.readLine().equals("request")){
					br = new BufferedReader(new FileReader("C:\\Users\\K3rNeL\\Desktop\\communication"));
				}
				
				orden = br.readLine();
				br.close();
				
				out = new PrintWriter(new FileWriter("C:\\Users\\K3rNeL\\Desktop\\communication"));
				if (orden.equals("listfiles")){
					cliente.enviarObjeto(new String("3"));
					Integer numFicheros = (Integer)cliente.recibirObjeto();
					
					String [] listaFicheros = new String[numFicheros];
					
					for (int i = 0; i < numFicheros; i++){
						listaFicheros[i] = (String) cliente.recibirObjeto();
					}
					
					out.print("reply");
					
					for (int i = 0; i < numFicheros; i++){
						out.println();
						out.print(listaFicheros[i]);
					}
				}
				
				out.close();
			}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (NullPointerException e){
				System.out.println("asasas");
			}
		}
		
		try {
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void pararThread(){		
		bParar = true;
	}
	
}
