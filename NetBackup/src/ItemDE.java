import java.io.File;
import java.text.DecimalFormat;

import javafx.geometry.Pos;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;


public class ItemDE extends VBox{
	
	private String rutaFichero;
	private String nombreFichero;
	private boolean bSeleccionado;
	private int iNumeroItem;
	
	public ItemDE(String rutaFichero, String nombreFichero, int anchura, int altura, int iNumeroItem){
		this.rutaFichero = rutaFichero;
		this.nombreFichero = nombreFichero;
		this.bSeleccionado = false;
		this.iNumeroItem = iNumeroItem;
		this.setPrefSize(112, 90);
		
		//Creamos los componentes graficos del objeto
		HBox contenedorImage = new HBox();
		contenedorImage.setAlignment(Pos.CENTER);
		HBox contenedorNombre = new HBox();
		contenedorNombre.setPrefWidth(112);
		ImageView iv = new ImageView(obtenerImagen());
		iv.setFitHeight(altura);
		iv.setFitWidth(anchura);
		
		if (nombreFichero.contains(".jpg"))
			iv.setPreserveRatio(false);
		else
			iv.setPreserveRatio(true);
		
		DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(10);
        dropShadow.setOffsetY(10);
        dropShadow.setColor(Color.rgb(50, 50, 50, 0.7));
        iv.setEffect(dropShadow);
		Text tNombre = new Text();
		
		if (nombreFichero.length() > 30){
			tNombre.setText(nombreFichero.substring(0, 30) + "...");
		}else
			tNombre.setText(nombreFichero);
		
		
		tNombre.setTextAlignment(TextAlignment.CENTER);
		tNombre.setWrappingWidth(100);
		contenedorImage.getChildren().add(iv);
		contenedorImage.setStyle("-fx-padding: 5 0 0 0");
		contenedorNombre.getChildren().add(tNombre);
		
		this.getChildren().addAll(contenedorImage, contenedorNombre);
	}
	
	public void setRutaFichero(String rutaFichero){
		this.rutaFichero = rutaFichero;
	}
	
	public void setNombreFichero(String nombreFichero){
		this.nombreFichero = nombreFichero;
	}
	
	public void setSeleccionado(boolean bSeleccionado){
		this.bSeleccionado = bSeleccionado;
		
		if (bSeleccionado)
			this.setStyle("-fx-background-color: rgba(120,170,252, 0.5); -fx-border-color: #498cfc ;-fx-background-radius: 5; -fx-border-radius: 5;");
	}
	
	public String getNombreFichero(){
		return this.nombreFichero;
	}
	
	public String getRutaFichero(){
		return this.rutaFichero;
	}
	
	public boolean getSeleccionado(){
		return this.bSeleccionado;
	}
	
	public void setNumeroItem(int iNumeroItem){
		this.iNumeroItem = iNumeroItem;
	}
	
	public int getNumeroItem(){
		return this.iNumeroItem;
	}
	
	public String getTamanyo(){
		String strTamanyo = "";
		File file = new File(this.getRutaFichero());
		long tamanyo = file.length();
		strTamanyo = obtenerTamanyo(tamanyo);
		return strTamanyo;
	}
	
	 private String obtenerTamanyo(Long fileSize){
	    	String strTamanyo = "";
	    	DecimalFormat df = new DecimalFormat("#.##");
	    	double dFileSize = fileSize;
	    	
	    	if (fileSize <= WatchDir.KILOBYTE){
	    		strTamanyo = fileSize + " Bytes";
	    	}else if (fileSize > WatchDir.KILOBYTE && fileSize <=  WatchDir.MEGABYTE){
	    		strTamanyo = df.format((dFileSize / WatchDir.KILOBYTE)) + "KBytes";
	    	}else if (fileSize > WatchDir.MEGABYTE && fileSize <= WatchDir.GIGABYTE){
	    		strTamanyo = df.format((dFileSize / WatchDir.MEGABYTE)) + "MBytes";
	    	}else{
	    		strTamanyo = df.format((dFileSize / WatchDir.GIGABYTE)) + "GBytes";
	    	}
	    	
	    	return strTamanyo;
	    }
	
	private Image obtenerImagen(){
		Image image = null;
		
		if (nombreFichero.contains(".txt"))
			image = new Image("images/TXTcopia.png");
		else if (nombreFichero.contains(".docx"))
			image = new Image("images/word.png");
		else if (nombreFichero.contains(".pdf"))
			image = new Image("images/pdf.png");
		else if (nombreFichero.contains(".mp3"))
			image = new Image("images/audio-x-generic.png");
		else if (nombreFichero.contains(".jpg"))
			image = new Image("file:" + getRutaFichero());
		
		return image;
	}
}
