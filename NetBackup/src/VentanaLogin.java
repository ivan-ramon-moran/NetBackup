
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

//LA CLASE VENTANAPIDE USUARIO Y CONTRASEÑA AL USUARIO

public class VentanaLogin {
	
	//DECLARACION DE VARIABLES
	private TextField usuario = new TextField ();
	private TextField password = new TextField ();
	private String usser = null;
	private String pass = null;
	private Stage primaryStage = null;
	
	public VentanaLogin () {
		primaryStage = new Stage(StageStyle.TRANSPARENT);
		VBox contenedorPrincipal = new VBox (15);
		
		HBox contenedorSecundario = new HBox (10);
		contenedorSecundario.setAlignment(Pos.CENTER);
		contenedorSecundario.setStyle("-fx-padding: 15"); 
		contenedorSecundario.setId("ventana");
		
		HBox cTitulo = new HBox (25);
		cTitulo.setId("cTitulo");
		
		//ETIQUETAS Y COMENTARIOS EN LA VENTANA
		Label titulo = new Label ("NetBackup - Pantalla de Login");
        titulo.setStyle("-fx-font-size: 13;");
        titulo.setId("ventana-titulo");
        HBox contCuerpo = new HBox(20);
        VBox contCampos = new VBox(15);
        ImageView ivLogin = new ImageView(new Image("images/login.png"));
        ivLogin.setFitHeight(96);
        ivLogin.setFitWidth(96);
        contCuerpo.getChildren().addAll(ivLogin, contCampos);
        
        HBox contUsuario = new HBox(15);
        contUsuario.setStyle("-fx-padding:0");
		contUsuario.setAlignment(Pos.CENTER);
        usuario.setStyle("-fx-background-radius: 10px");
        usuario.setPrefWidth(200);
        usuario.setAlignment(Pos.CENTER);
        password.setPrefWidth(200);
        password.setStyle("-fx-background-radius: 10px");
        password.setAlignment(Pos.CENTER);
        Label etiquetaUsuario = new Label("Usuario:");
        etiquetaUsuario.setPrefWidth(68);
        etiquetaUsuario.setStyle("-fx-font-size: 14");
		contUsuario.getChildren().addAll(etiquetaUsuario, usuario);
		
		HBox contPass = new HBox(10);
		contPass.setAlignment(Pos.CENTER);
		Label etiquetaPass = new Label("Contraseña:");
		etiquetaPass.setStyle("-fx-font-size: 14");

		contPass.getChildren().addAll(etiquetaPass, password);
		contCampos.getChildren().addAll(contUsuario, contPass);
		
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
			
			root.setId("ventana-principal");
			
			contenedorPrincipal.getChildren().add (cTitulo);
			
			cTitulo.getChildren().add(titulo);
			
			contenedorPrincipal.getChildren().add(contCuerpo);			
						
			CheckBox checkInicioAutomatico = new CheckBox("Inicio de sesion automático.");
			contCampos.getChildren().add(checkInicioAutomatico);
			contenedorPrincipal.getChildren().add(contenedorSecundario);
			contenedorSecundario.setAlignment(Pos.CENTER_RIGHT);
			contenedorSecundario.getChildren().add(aceptar);
			
			contenedorSecundario.getChildren().add(cancelar);
			
			contenedorSecundario.getChildren().add(nuevoUsuario);
			
			//contenedorPrincipal.getChildren().add(barra);
			
			//barra.getChildren().add(etiquetaBarra);
			
			
			HBox.setHgrow(cTitulo, Priority.ALWAYS);
			
			
			Scene scene = new Scene(root,475,275);
			scene.setFill(Color.TRANSPARENT);
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
