import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class NuevoUsser {
	
	private TextField nuevoUsuario = new TextField ();
	private TextField nuevoPassword = new TextField ();
	private Stage primaryStage = null;
	
	public NuevoUsser(){
		
		
		primaryStage = new Stage();
		StackPane root = new StackPane();
		
		//------CONTENEDORES-------
		VBox contenedorPrincipal = new VBox (10);
		
		HBox contenedorSecundario = new HBox (25);
		contenedorSecundario.setId("ventana");
		
		HBox cTitulo = new HBox (25);
		cTitulo.setId("cTitulo");
		
		HBox barra = new HBox();
		barra.setPrefHeight(20);
		barra.setId("barra");
		
		//-------ETIQUETAS Y COMENTARIOS--------
		
		Label titulo = new Label ("NetBackup");
		titulo.setFont(Font.loadFont("file:resources/fonts/adrip1.ttf", 120));
        titulo.setStyle("-fx-font-size: 45;");
        titulo.setId("ventana-titulo");
        
		Label etiqueta = new Label("Introducir Nuevo usuario:");
		etiqueta.setFont(new Font("Bradley Hand ITC", 15));
		
		Label etiqueta2 = new Label("Contraseña:");
		etiqueta2.setFont(new Font("Bradley Hand ITC", 15));
		
		Label etiquetaBarra = new Label("NetBackup v0.1- Automatic backup for your files!!!");
		etiquetaBarra.setFont(new Font("Bradley Hand ITC", 15));
		etiquetaBarra.setStyle("-fx-text-fill:white;");
		
		//-------BOTONES------------
	try{
		Button aceptar = new Button ("Aceptar");
		aceptar.setId("aceptar");
		Button cancelar = new Button ("Cancelar");
		cancelar.setId("cancelar");
		
		//------VENTANA--------------
		
		root.getChildren().add(contenedorPrincipal);
		
		root.getStyleClass().add("root");
		
		contenedorPrincipal.getChildren().add (cTitulo);
		
		cTitulo.getChildren().add(titulo);
		
		contenedorPrincipal.getChildren().add(etiqueta);			
		
		contenedorPrincipal.getChildren().add(nuevoUsuario);
		
		contenedorPrincipal.getChildren().add(etiqueta2);
		
		contenedorPrincipal.getChildren().add(nuevoPassword);
		
		contenedorPrincipal.getChildren().add(contenedorSecundario);
		
		contenedorSecundario.getChildren().add(aceptar);
		
		contenedorSecundario.getChildren().add(cancelar);
		
		contenedorPrincipal.getChildren().add(barra);
		
		barra.getChildren().add(etiquetaBarra);
		
		
		
		HBox.setHgrow(cTitulo, Priority.ALWAYS);
		
		primaryStage.initStyle( StageStyle.UNDECORATED );
		
		Scene scene = new Scene(root,475,275);
		scene.getStylesheets().add("ventana.styles.css");
		
		
		primaryStage.setScene(scene);
		primaryStage.setTitle("Create an Account");
		primaryStage.show();
		
		//------ACCIONES DE LOS BOTONES------
		
		aceptar.setOnAction (new EventHandler<ActionEvent>(){
			public void handle(ActionEvent event){
				//COMPROBAR QUE NO EXISTE YA EL USUARIO
				//GUARDAR EL USUARIO EN UNA BASE DE DATOS
				primaryStage.close();
			}
		});
		
		cancelar.setOnMouseClicked(new EventHandler<MouseEvent>(){
			@Override
	        public void handle(MouseEvent me) {				
				primaryStage.close();
			}
		});
		
	} catch(Exception e) {
		e.printStackTrace();
	}
	primaryStage.showAndWait();


	}
}
