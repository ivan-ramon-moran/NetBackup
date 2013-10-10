import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class VentanaMensaje {

	private Stage stage = null;
	
	public VentanaMensaje(int _iTipo, final String _strMensaje){
		Platform.runLater(new Runnable(){
			@Override
			public void run() {
				stage = new Stage(StageStyle.TRANSPARENT);
				StackPane root = new StackPane();
				root.setId("ventana-mensaje");
				Scene scene = new Scene(root, 550, 230);
				scene.getStylesheets().add("styles.css");
				scene.setFill(Color.TRANSPARENT);
				stage.setScene(scene);
				VBox contenedorPrincipal = new VBox();
				root.getChildren().add(contenedorPrincipal);
				HBox contCabecera = new HBox();
				contCabecera.setPrefHeight(40);
				contCabecera.setId("ventana-mensaje-cabecera");
				Label labelTitulo = new Label("Error de conexión");
				labelTitulo.setStyle("-fx-text-fill: white; -fx-font-size: 13; -fx-font-weight: bold; -fx-padding: 10"); 
				contCabecera.getChildren().add(labelTitulo);
				HBox contCuerpo = new HBox(20);
				ImageView ivTipoMensaje = new ImageView(new Image("images/alert.png"));
				ivTipoMensaje.setFitHeight(96);
				ivTipoMensaje.setFitWidth(96);
				Label labelMensaje = new Label(_strMensaje);
				labelMensaje.setWrapText(true);
				labelMensaje.setStyle("-fx-font-size: 14");
				contCuerpo.setAlignment(Pos.CENTER);
				contCuerpo.setStyle("-fx-padding: 15 15 0 15");
				Button botonAceptar = new Button("Aceptar");
				botonAceptar.setId("ventana-mensaje-aceptar");
				HBox contBoton = new HBox();
				contBoton.setStyle("-fx-padding: 10");
				contBoton.setAlignment(Pos.CENTER_RIGHT);
				contBoton.getChildren().add(botonAceptar);
				contCuerpo.getChildren().addAll(ivTipoMensaje, labelMensaje);
				contenedorPrincipal.getChildren().add(contCabecera);
				contenedorPrincipal.getChildren().add(contCuerpo);
				contenedorPrincipal.getChildren().add(contBoton);

				
				stage.show();
			}
			
		});
	}
	
}
