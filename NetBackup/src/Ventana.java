
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;

//LA CLASE VENTANAPIDE USUARIO Y CONTRASEÑA AL USUARIO

public class Ventana {
	
	//DECLARACION DE VARIABLES
	
	private TextField usuario = new TextField ();
	private TextField password = new TextField ();
	private String usser = null;
	private String pass = null;
	private Stage primaryStage = null;
	
public Ventana () {
		
		
		primaryStage = new Stage();
		
		
		VBox contenedorPrincipal = new VBox (10);
		
		HBox contenedorSecundario = new HBox (25);
		contenedorSecundario.setId("ventana");
		
		HBox cTitulo = new HBox (25);
		cTitulo.setId("cTitulo");
		
		HBox barra = new HBox();
		barra.setPrefHeight(20);
		barra.setId("barra");
		
		//ETIQUETAS Y COMENTARIOS EN LA VENTANA
		Label titulo = new Label ("NetBackup");
		titulo.setFont(Font.loadFont("file:resources/fonts/adrip1.ttf", 120));
        titulo.setStyle("-fx-font-size: 45;");
        titulo.setId("ventana-titulo");
        
		Label etiqueta = new Label("Introducir Usuario:");
		etiqueta.setFont(new Font("Bradley Hand ITC", 15));
		
		Label etiqueta2 = new Label("Contraseña:");
		etiqueta2.setFont(new Font("Bradley Hand ITC", 15));
		
		Label etiquetaBarra = new Label("NetBackup v0.1- Automatic backup for your files!!!");
		etiquetaBarra.setFont(new Font("Bradley Hand ITC", 15));
		etiquetaBarra.setStyle("-fx-text-fill:white;");
		
		try {
			//DECLARACIÓN DE LOS BOTONES
			Button aceptar = new Button ("Aceptar");
			aceptar.setId("aceptar");
			Button cancelar = new Button ("Cancelar");
			cancelar.setId("cancelar");
			Button nuevoUsuario = new Button("Nuevo Usuario");
			nuevoUsuario.setId("Nuevo-Usuario");
			
			StackPane root = new StackPane();
			
			root.getChildren().add(contenedorPrincipal);
			
			root.getStyleClass().add("root");
			
			contenedorPrincipal.getChildren().add (cTitulo);
			
			cTitulo.getChildren().add(titulo);
			
			contenedorPrincipal.getChildren().add(etiqueta);			
			
			contenedorPrincipal.getChildren().addAll(usuario);
			
			contenedorPrincipal.getChildren().add(etiqueta2);
			
			contenedorPrincipal.getChildren().add(password);
			
			contenedorPrincipal.getChildren().add(contenedorSecundario);
			
			contenedorSecundario.getChildren().add(aceptar);
			
			contenedorSecundario.getChildren().add(cancelar);
			
			contenedorSecundario.getChildren().add(nuevoUsuario);
			
			contenedorPrincipal.getChildren().add(barra);
			
			barra.getChildren().add(etiquetaBarra);
			
			
			HBox.setHgrow(cTitulo, Priority.ALWAYS);
			
			primaryStage.initStyle( StageStyle.UNDECORATED );
			
			Scene scene = new Scene(root,420,275);
			scene.getStylesheets().add("ventana.styles.css");
			
			
			primaryStage.setScene(scene);
			primaryStage.setTitle("Logging...");
			
			//ACCIONES QUE REALIZAN LOS BOTONES AL ACCIONARSE
			aceptar.setOnAction (new EventHandler<ActionEvent>(){
				public void handle(ActionEvent event){
					usser = usuario.getPromptText();
					pass = password.getPromptText();
					primaryStage.close();
				}
				});
			
			cancelar.setOnAction(new EventHandler<ActionEvent>(){
				public void handle(ActionEvent event){
					System.exit(0);
				}
				});
				
			nuevoUsuario.setOnAction(new EventHandler <ActionEvent>(){
				public void handle (ActionEvent event){
					NuevoUsser nuevoUsser = new NuevoUsser(); //Abre una ventana para registrar un nuevo usuario
				}
			});
		
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		primaryStage.showAndWait();

	}

	//MÉTODOS GET
	public String getUsser()
	{
		return this.usser;
	}
	public String getPass()
	{
		return this.pass;
	}	
}
