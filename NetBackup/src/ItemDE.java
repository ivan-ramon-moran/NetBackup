import java.io.File;
import java.text.DecimalFormat;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;


public class ItemDE extends VBox{
	
	private String rutaFichero;
	private String nombreFichero;
	private boolean bSeleccionado;
	private int iNumeroItem;
	
	public ItemDE(String rutaFichero, String nombreFichero, int anchura, int altura, int iNumeroItem){
		this.setSpacing(10);
		this.rutaFichero = rutaFichero;
		this.nombreFichero = nombreFichero;
		this.bSeleccionado = false;
		this.iNumeroItem = iNumeroItem;
		this.setMinSize(112, 90);
		this.setMaxWidth(112);
		//Creamos los componentes graficos del objeto
		HBox contenedorImage = new HBox();
		contenedorImage.setAlignment(Pos.CENTER);
		HBox contenedorNombre = new HBox();
		contenedorNombre.setPrefWidth(112);
		contenedorNombre.setMaxWidth(112);

		ImageView iv = new ImageView(obtenerImagen());
		iv.setFitHeight(altura);
		iv.setFitWidth(anchura);
		
		if (nombreFichero.contains(".jpg"))
			iv.setPreserveRatio(false);
		else
			iv.setPreserveRatio(true);
		
		DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(5);
        dropShadow.setOffsetY(5);
        dropShadow.setColor(Color.rgb(50, 50, 50, 0.7));
        iv.setEffect(dropShadow);
        iv.setSmooth(true);
		Text tNombre = new Text();
		tNombre.setTextAlignment(TextAlignment.CENTER);
		tNombre.setFont(Font.loadFont(getClass().getResourceAsStream("/fuentes/DroidSans.ttf"), 13));
		tNombre.setWrappingWidth(100);
		
		if (nombreFichero.length() > 30){
			tNombre.setText(nombreFichero.substring(0, 30) + "...");
		}else
			tNombre.setText(nombreFichero);
		
		
		contenedorImage.getChildren().add(iv);
		contenedorImage.setStyle("-fx-padding: 5 0 0 0");
		contenedorNombre.getChildren().add(tNombre);
		contenedorNombre.setAlignment(Pos.CENTER);
		
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
			this.setStyle("-fx-background-color: #F0F0F0; -fx-border-color: #D3D3D3 ;-fx-background-radius: 5; -fx-border-radius: 5;");
		else
			this.setStyle("-fx-background-color: transparent; -fx-border-color: transparent");
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
			image = new Image("images/application-text.png");
		else if (nombreFichero.contains(".docx"))
			image = new Image("images/application-msword.png");
		else if (nombreFichero.contains(".pdf"))
			image = new Image("images/application-pdf.png");
		else if (nombreFichero.contains(".mp3"))
			image = new Image("images/audio-x-generic.png");
		else if (nombreFichero.contains(".jpg"))
			image = new Image("file:" + getRutaFichero());
		else if (nombreFichero.contains(".png"))
			image = new Image("images/image.png");
		else
			image = new Image("images/image.png");

		
		return image;
	}
}
