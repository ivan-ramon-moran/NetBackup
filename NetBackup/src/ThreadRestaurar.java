import javafx.application.Platform;


public class ThreadRestaurar extends Thread {
	
	private Cliente clienteMsg = null;
	
	public ThreadRestaurar(Cliente _clienteMsg){
		clienteMsg = _clienteMsg;
	}
	
	public void run(){
		//Abrimos la ventana de progreso de la restauración
    	final VentanaProgresoRestaurar ventanaProgreso = new VentanaProgresoRestaurar();
		//Enviamos al servidor la orden de que vamos a restaurar los ficheros
    	clienteMsg.enviarCadena(new String("2"));
    	final Integer numTransferencias = (Integer)clienteMsg.recibirObjeto();
    	//Recibimos los datos de las transferencias
    	for (int i = 0; i < numTransferencias; i++){
    		final FicheroSincronizacion fSin = (FicheroSincronizacion)clienteMsg.recibirObjeto();
    		final int iTransferenciaActual = i + 1;
    		Platform.runLater(new Runnable(){
    			@Override
				public void run() {
    				ventanaProgreso.setArchivo(fSin.getNombreFichero());
    				ventanaProgreso.setNumArchivos(numTransferencias - iTransferenciaActual);
				}
    			
    		});
    		
    		System.out.println(fSin.getNombreFichero());
    		clienteMsg.recibirFichero(fSin, ventanaProgreso);
    	}
    	
    	ventanaProgreso.cerrarVentana();
	}
	
}
