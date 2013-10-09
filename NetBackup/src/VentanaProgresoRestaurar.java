import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class VentanaProgresoRestaurar {
	
	private ProgressBar barra = null;
	private Label labelNombre = null;
	private ImageView ivMiniatura = null;
	private Stage stage = null;
	private Label labelFicheros = null;
	private Label labelVelocidad = null;
	
	public VentanaProgresoRestaurar(){
		Platform.runLater(new Runnable(){

			@Override
			public void run() {
				stage = new Stage(StageStyle.TRANSPARENT);
				StackPane root = new StackPane();
				Scene scene = new Scene(root, 600, 260);
				scene.setFill(Color.TRANSPARENT);
				stage.setScene(scene);
				VBox contenedorPrincipal = new VBox();
				root.getChildren().add(contenedorPrincipal);
				root.setId("ventana-progreso-restaurar");
			    scene.getStylesheets().add("styles.css");
			    VBox contenedorCabecera = new VBox();
			    contenedorCabecera.setPrefSize(0, 50);
			    contenedorCabecera.setId("ventana-progreso-restaurar-cabecera");
			    contenedorPrincipal.getChildren().add(contenedorCabecera);
				Label labelTitulo = new Label("Progreso de restauración"); 
				labelTitulo.setId("ventana-progreso-restauracion-titulo");
				contenedorCabecera.getChildren().add(labelTitulo);
				DropShadow dropShadow = new DropShadow();
		        dropShadow.setRadius(5.0);
		        dropShadow.setOffsetX(0.0);
		        dropShadow.setOffsetY(3.0);
		        dropShadow.setColor(Color.color(0.4, 0.5, 0.5));
		        //contenedorCabecera.setEffect(dropShadow);
				barra = new ProgressBar();
				barra.setProgress(0.0);
				barra.setPrefSize(500, 30);
				HBox contenedorCuerpo = new HBox(15);
				VBox contenedorDetalles = new VBox(10);
				contenedorCuerpo.setStyle("-fx-padding: 20");
				ivMiniatura = new ImageView(new Image("images/puzzle.png"));
				ivMiniatura.setFitHeight(64);
				ivMiniatura.setFitWidth(64);
				contenedorCuerpo.getChildren().addAll(ivMiniatura);
				contenedorDetalles.getChildren().add(barra);
				contenedorCuerpo.getChildren().add(contenedorDetalles);
				HBox contenedorNombre = new HBox(10);
				Label labelEtiNombre = new Label("Copiando actualmente: ");
				labelEtiNombre.getStyleClass().add("label-restaurar");
				labelNombre = new Label("Hola.pdf");
				labelNombre.getStyleClass().add("label-restaurar");
				contenedorNombre.getChildren().addAll(labelEtiNombre, labelNombre);
				HBox contenedorTamRestante = new HBox(10);
				Label labelEtiTam = new Label("Tamaño restante: ");
				labelEtiTam.getStyleClass().add("label-restaurar");
				Label labelTam = new Label("38.2 MB");
				labelTam.getStyleClass().add("label-restaurar");
				contenedorTamRestante.getChildren().addAll(labelEtiTam, labelTam);
				HBox contFicherosRestantes = new HBox(10);
				Label labelEtiFicheros = new Label("Ficheros restantes: ");
				labelEtiFicheros.getStyleClass().add("label-restaurar");
				labelFicheros = new Label();
				labelFicheros.getStyleClass().add("label-restaurar");
				Label labelEtiVelocidad = new Label("Velocidad MBytes/s: ");
				labelEtiVelocidad.getStyleClass().add("label-restaurar");
			    labelVelocidad = new Label("3 MBytes/s");
				labelEtiVelocidad.getStyleClass().add("label-restaurar");
				
				contFicherosRestantes.getChildren().addAll(labelEtiFicheros, labelFicheros, labelEtiVelocidad, labelVelocidad);
				 
				
				contenedorDetalles.getChildren().addAll(contenedorNombre, contenedorTamRestante, contFicherosRestantes);
				contenedorPrincipal.getChildren().add(contenedorCuerpo);
				
				stage.show();
				
			}
			
		});
		
	}
	
	public void setProgreso(double dProgreso){
		barra.setProgress(dProgreso);
	}
	
	public void setArchivo(String strNombreArchivo){
		labelNombre.setText(strNombreArchivo);
		barra.setProgress(0.0);
	}
	
	public double getProgreso(){
		return barra.getProgress();
	}
	
	public void setNumArchivos(int _iNumArchivos){
		labelFicheros.setText(Integer.toString(_iNumArchivos));
	}
	
	public void setVelocidad(final double dVelocidad){
		Platform.runLater(new Runnable(){
			@Override
			public void run() {
				labelVelocidad.setText(dVelocidad + " MBytes/s");
			}
			
		});
	}
	
	public void cerrarVentana(){
		Platform.runLater(new Runnable(){
			@Override
			public void run() {
				stage.close();
			}
			
		});
	}
}
