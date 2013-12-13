
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

//LA CLASE VENTANAPIDE USUARIO Y CONTRASE�A AL USUARIO

public class VentanaLogin {
	
	//DECLARACION DE VARIABLES
	private TextField usuario = new TextField ();
	private TextField password = new TextField ();
	private Stage primaryStage = null;
	private double xOffset, yOffset;
	private NetBackup vista = null;
	
	public VentanaLogin (NetBackup _vista) {
		this.vista = _vista;
		primaryStage = new Stage(StageStyle.TRANSPARENT);
		VBox contenedorPrincipal = new VBox (15);
		
		HBox contenedorSecundario = new HBox (10);
		contenedorSecundario.setAlignment(Pos.CENTER);
		contenedorSecundario.setStyle("-fx-padding: 15"); 
		contenedorSecundario.setId("ventana");
		
		HBox cTitulo = new HBox (25);
		//ESTO HACE QUE SE MUEVA LA VENTANA
		cTitulo.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });
		cTitulo.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            	primaryStage.setX(event.getScreenX() - xOffset);
            	primaryStage.setY(event.getScreenY() - yOffset);
            }
        });
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
		Label etiquetaPass = new Label("Contrase�a:");
		etiquetaPass.setStyle("-fx-font-size: 14");

		contPass.getChildren().addAll(etiquetaPass, password);
		contCampos.getChildren().addAll(contUsuario, contPass);
		
		try {
			//DECLARACI�N DE LOS BOTONES
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
						
			CheckBox checkInicioAutomatico = new CheckBox("Inicio de sesion autom�tico.");
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
					vista.setUsuario2(usuario.getText());
					vista.setPassword(password.getText());
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
}
