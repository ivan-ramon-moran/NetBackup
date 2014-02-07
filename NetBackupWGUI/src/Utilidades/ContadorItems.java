package Utilidades;

public class ContadorItems {
	static int iNumeroItems = 0;
	
	public static void incrementarNumero(){
		iNumeroItems++;
	}
	
	public static int getNumeroItems(){
		return iNumeroItems;
	}
}
