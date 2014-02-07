package Threads;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

import Net.Cliente;
import Utilidades.Cola;

public class ThreadFileMonitor extends Thread{
	
	private Cliente cliente = null;
	private Cola colaTransferencias = null;
	private int iNumeroThreads = 0;
	
	public ThreadFileMonitor(Cliente cliente, Cola colaTransferencias){
		this.cliente = cliente;
		this.colaTransferencias = colaTransferencias;
	}
	
	public void run(){
		//new WatchDir(path, false, cliente, transferencias, colaTransferencias).processEvents();
		
		File fDir = new File("C:\\Users\\K3rNeL\\Desktop");
		if (esDirectorioValido(fDir)){
			//LANZO EL THREAD Y VUELVO A LLAMAR A LA FUNCION
			ThreadDirectorio tDirectorio = new ThreadDirectorio(fDir.getAbsolutePath(), cliente, colaTransferencias);
			tDirectorio.start();
			iNumeroThreads++;
			
			inspeccionarDirectorio("C:\\Users\\K3rNeL\\Desktop");	
		}
				
	}
	
	private void inspeccionarDirectorio(String directorio){
		File fDir = new File(directorio);
		String [] dirs = fDir.list(); 
		
		ArrayList <String> dirFiltrados = filtrarDirectorios(directorio, dirs);
	
		for (int i = 0; i < dirFiltrados.size(); i++){
			File fDir2 = new File(fDir.getAbsolutePath() + "\\" + dirFiltrados.get(i));
			System.out.println(fDir2.getAbsolutePath());
			
			if (esDirectorioValido(fDir2)){
				//LANZO EL THREAD Y VUELVO A LLAMAR A LA FUNCION
				ThreadDirectorio tDirectorio = new ThreadDirectorio(fDir2.getAbsolutePath(), cliente, colaTransferencias);
				tDirectorio.start();
				iNumeroThreads++;
				inspeccionarDirectorio(fDir2.getAbsolutePath());
			}
				
		}

	}
	
	
	private boolean esDirectorioValido(File fDir){
		boolean resultado = false;
		
		if (fDir.isDirectory() && fDir.getName().charAt(0) != '.' && !fDir.getName().equals("NetBackup"))
			resultado = true;
		
		return resultado;
	}
	
	private  ArrayList<String> filtrarDirectorios(String directorio, String [] dirs){
		ArrayList <String> dirFiltrados = new ArrayList<String>();
		
		for (int i = 0; i < dirs.length; i++){
			File file = new File(directorio + "\\" + dirs[i]);
			
			if (file.list() != null)
				dirFiltrados.add(dirs[i]);
		}
		
		return dirFiltrados;
	}
	
}
