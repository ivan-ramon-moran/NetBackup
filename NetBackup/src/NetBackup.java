import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
 
public class NetBackup extends Application {
    
	private StackPane paneles;
	private VBox panelExploracion, panelTransferencias, panelInicio, panelConfiguracion;
	private ThreadFileMonitor tFileMonitor = null;
	private ThreadConectar tConectar = null, tConectarMsg = null;
	private Cliente cliente = null, clienteMsg = null;
	private Cola colaTransferencias = new Cola();
	private ThreadEnvio tEnvio = null;
	private ExtendedTable transferencias = new ExtendedTable();
	private ArrayList<Object> aOpciones = new ArrayList<Object>();
	private ArrayList<Object> aLabelOpciones = new ArrayList<Object>();
	private VBox cuerpoConfiguracion;
	private UsuarioSistema usuarioSistema = null;
	private VentanaLogin logging = null;
	private Label lElementoActual = null;
	private double xOffset, yOffset;
	private Label labelEstado, labelUsuario, labelEspacio, labelUsuarios;
	Text labelRutaServidor;
	ListViewIcons list;
	
	public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
    	//---------------------------CONFIGURACION--------------------------------
    	//Leemos la configuracion del fichero
    	System.out.println(Configuracion.getInstance().bPrimeraVez);
    	//------------------------------------------------------------------------
    	//-----------------SE INICIA LA VENTANA DE LOGGING------------------------
    	logging = new VentanaLogin();
    	//------------------------USUARIO DEL SISTEMA-----------------------------
    	usuarioSistema = new UsuarioSistema();
    	//------------------------------------------------------------------------
    	//------------------------SISTEMA OPERATIVO-------------------------------
    	System.out.println(SistemaOperativo.getSistema());
    	System.out.println(SistemaOperativo.isWindows());
    	//------------------------------------------------------------------------
    	
    	
    	final Stage ptrStage = primaryStage;
    	primaryStage.initStyle(StageStyle.TRANSPARENT);
    	Button btn = new Button();
        btn.setId("boton");
        btn.setText("Say 'Hello World'");
        
        btn.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
            public void handle(ActionEvent event) {
                System.out.println("Hello World!");
            }
        });
        StackPane root = new StackPane();
        root.setId("ventana-principal");
        VBox contenedorPrincipal = new VBox();
        root.getChildren().add(contenedorPrincipal);
        root.getStyleClass().add("root");
        Scene scene = new Scene(root, 1060, 730);
        scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets().add("styles.css");        
       
        HBox cabecera = new HBox();
        //ESTO HACE QUE SE MUEVA LA VENTANA
        cabecera.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });
        cabecera.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            	ptrStage.setX(event.getScreenX() - xOffset);
            	ptrStage.setY(event.getScreenY() - yOffset);
            }
        });
        
        HBox cTitulo = new HBox();
        HBox cBotonCerrar = new HBox();
        cabecera.setPrefHeight(100);
        cabecera.setId("ventana-principal-cabecera");
        HBox.setHgrow(cTitulo, Priority.ALWAYS);
        Label labelTitulo = new Label("NetBackup");
        labelTitulo.setFont(Font.loadFont(getClass().getResourceAsStream("/fuentes/adrip1.ttf"), 20));
        labelTitulo.setStyle("-fx-font-size: 70;");
        
        ImageView iv = new ImageView(new Image("images/close.png"));
        iv.setOnMouseClicked(new EventHandler<MouseEvent>(){
			@Override
	        public void handle(MouseEvent me) {
				Screen screen = Screen.getPrimary();
				Rectangle2D bounds = screen.getVisualBounds();

				ptrStage.setX(bounds.getMinX());
				ptrStage.setY(bounds.getMinY());
				ptrStage.setWidth(bounds.getWidth());
				ptrStage.setHeight(bounds.getHeight());
			}
		});
        iv.setFitHeight(16);
	    iv.setFitWidth(16);
	    iv.setStyle("-fx-cursor: hand;");
	    cBotonCerrar.setStyle("-fx-padding: 5 10 5 10");
	    cBotonCerrar.getChildren().add(iv);
        cTitulo.getChildren().add(labelTitulo);
        labelTitulo.setId("ventana-principal-titulo");
        cabecera.getChildren().add(cTitulo);
        cabecera.getChildren().add(cBotonCerrar);
        HBox separador = new HBox();
        separador.setPrefHeight(1);
        separador.getStyleClass().add("separador");
        
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(5.0);
        dropShadow.setOffsetX(0.0);
        dropShadow.setOffsetY(3.0);
        dropShadow.setColor(Color.color(0.4, 0.5, 0.5));
        cabecera.setEffect(dropShadow);
        
        //Contenedor del cuerpo
        VBox contenedorCuerpo = new VBox();
        VBox.setVgrow(contenedorCuerpo, Priority.ALWAYS);
        //Barra de estado
        HBox barraEstado = new HBox();
        barraEstado.setPrefHeight(30);
        barraEstado.setId("barra-estado");
        Label labelNombre = new Label("NetBackup v0.1- Automatic backup for your files!!!");
        labelNombre.setFont(Font.loadFont(getClass().getResourceAsStream("/fuentes/DroidSans.ttf"), 13));
        labelNombre.setStyle("-fx-text-fill: white; -fx-padding: 5 5 5 10");
        barraEstado.getChildren().add(labelNombre);
        HBox contBarraProgreso = new HBox(5);
        contBarraProgreso.setStyle("-fx-padding: 0 10 0 0");
        contBarraProgreso.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(contBarraProgreso, Priority.ALWAYS);
        barraEstado.getChildren().add(contBarraProgreso);
        Label labelEtiTareaActual = new Label("Tarea actual: ");
        labelEtiTareaActual.setFont(Font.loadFont(getClass().getResourceAsStream("/fuentes/DroidSans.ttf"), 13));
        labelEtiTareaActual.setStyle("-fx-text-fill: white;");
        Label labelTareaActual = new Label("Ninguna");
        labelTareaActual.setFont(Font.loadFont(getClass().getResourceAsStream("/fuentes/DroidSans.ttf"), 13));
        labelTareaActual.setStyle("-fx-text-fill: white;");
        ProgressBar barraTareaActual = new ProgressBar();
        barraTareaActual.setPrefHeight(15);
        barraTareaActual.setProgress(0.5);
        
        contBarraProgreso.getChildren().addAll(labelEtiTareaActual, labelTareaActual, barraTareaActual);
        
        HBox contenedorOpciones = new HBox(5);
        contenedorOpciones.setId("contenedorOpciones");
        contenedorOpciones.setPrefSize(0, 30);
        
        
        //------------------------BOTONES TOOLBAR---------------------------------
        //Boton Inicio
        //Boton explorar
        Button botonInicio = new Button("INICIO");   
        botonInicio.setFont(Font.loadFont(getClass().getResourceAsStream("/fuentes/DroidSans-Bold.ttf"), 13));

        botonInicio.getStyleClass().add("boton_toolbar");
        botonInicio.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
            public void handle(ActionEvent event) {
               opcionInicio();
            }
        });
        
        //Boton explorar
        Button botonExplorar = new Button("EXPLORAR ARCHIVOS EN EL SERVIDOR");
        botonExplorar.setFont(Font.loadFont(getClass().getResourceAsStream("/fuentes/DroidSans-Bold.ttf"), 13));

        botonExplorar.getStyleClass().add("boton_toolbar");
        botonExplorar.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
            public void handle(ActionEvent event) {
                OpcionExplorarServidor();
            }
        });
        
        //Boton transferencias
        Button botonTransferencias = new Button("TRANSFERENCIAS");
        botonTransferencias.setFont(Font.loadFont(getClass().getResourceAsStream("/fuentes/DroidSans-Bold.ttf"), 13));

        botonTransferencias.getStyleClass().add("boton_toolbar");
        botonTransferencias.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
            public void handle(ActionEvent event) {
                OpcionTransferencias();
            }
        });
        //Boton configuracion
        Button botonConfiguracion = new Button("CONFIGURACIÓN");
        botonConfiguracion.setFont(Font.loadFont(getClass().getResourceAsStream("/fuentes/DroidSans-Bold.ttf"), 13));

        botonConfiguracion.getStyleClass().add("boton_toolbar");
        botonConfiguracion.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
            public void handle(ActionEvent event) {
                opcionConfiguracion();
            }
        });
        //Boton ayuda
        Button botonAyuda = new Button("AYUDA");
        botonAyuda.setFont(Font.loadFont(getClass().getResourceAsStream("/fuentes/DroidSans-Bold.ttf"), 13));
        botonAyuda.getStyleClass().add("boton_toolbar");
        //Alertas de usuario
        HBox contAlertas = new HBox(5);
        contAlertas.setStyle("-fx-padding: 0 20 0 0");
        contAlertas.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(contAlertas, Priority.ALWAYS);
        ImageView ivAlertaError = new ImageView(new Image("images/alert_error.png"));
        ivAlertaError.setFitHeight(26);
        ivAlertaError.setFitWidth(26);
        ImageView ivAlertaInfo = new ImageView(new Image("images/alert_info.png"));
        ivAlertaInfo.setFitHeight(23);
        ivAlertaInfo.setFitWidth(23);
        Label labelMensajesInfo = new Label("0 Mensajes");
        labelMensajesInfo.setStyle("-fx-text-fill: white");
        labelMensajesInfo.setFont(Font.loadFont(getClass().getResourceAsStream("/fuentes/DroidSans.ttf"), 13));
        Label labelMensajesError = new Label("0 Errores");
        labelMensajesError.setFont(Font.loadFont(getClass().getResourceAsStream("/fuentes/DroidSans.ttf"), 13));
        labelMensajesError.setStyle("-fx-text-fill: white");

        contAlertas.getChildren().addAll(ivAlertaInfo, labelMensajesInfo, ivAlertaError, labelMensajesError);
        
        contenedorOpciones.getChildren().addAll(botonInicio, botonExplorar, botonTransferencias, botonConfiguracion, botonAyuda, contAlertas);
        
        
        //------------------------------------------------------------------------
        contenedorPrincipal.getChildren().add(cabecera);
        contenedorPrincipal.getChildren().add(contenedorOpciones);

        contenedorPrincipal.getChildren().add(contenedorCuerpo);
        contenedorPrincipal.getChildren().add(barraEstado);
 
        paneles = new StackPane();
        
        panelExploracion = new VBox();
        //------------------------PANEL TRANSFERENCIAS-----------------------------
        panelTransferencias = new VBox();
        VBox.setVgrow(panelTransferencias, Priority.ALWAYS);
        panelTransferencias.setId("panelTransferencias");
        
        transferencias.addColumn("NOMBRE", 400);
        transferencias.addColumn("ESTADO", 100);
        transferencias.addColumn("PROGRESO", 150);
        transferencias.addColumn("TAMAÑO", 100);
        transferencias.addColumn("TIPO", 0);
        
        panelTransferencias.getChildren().add(transferencias);
        //-------------------------------------------------------------------------
        //-------------------PANEL CONFIGURACION-----------------------------------
        panelConfiguracion = new VBox();
        HBox contMenuCuerpo = new HBox(10);
        //HBox.setHgrow(contMenuCuerpo, Priority.ALWAYS);
        contMenuCuerpo.setStyle("-fx-padding: 10 10 10 10");
        VBox menuConfiguracion = new VBox();
        DropShadow dropMenuConfig = new DropShadow();
        dropMenuConfig.setRadius(5.0);
        dropMenuConfig.setOffsetX(0.0);
        dropMenuConfig.setOffsetY(3.0);
        dropMenuConfig.setColor(Color.color(0.4, 0.5, 0.5));
        menuConfiguracion.setEffect(dropShadow);

        cuerpoConfiguracion = new VBox(10);
        DropShadow dropCuerpoConfig = new DropShadow();
        dropCuerpoConfig.setRadius(5.0);
        dropCuerpoConfig.setOffsetX(3.0);
        dropCuerpoConfig.setOffsetY(3.0);
        dropCuerpoConfig.setColor(Color.color(0.4, 0.5, 0.5));
        cuerpoConfiguracion.setEffect(dropShadow);
        ScrollPane scrollConfiguracion = new ScrollPane();
        scrollConfiguracion.setStyle("-fx-background-color: transparent; -fx-border-radius: 6; -fx-background-radius: 6");
        scrollConfiguracion.setContent(cuerpoConfiguracion);
        //scrollConfiguracion.setFitToHeight(true);
        scrollConfiguracion.setFitToWidth(true);
        scrollConfiguracion.setPrefWidth(800);
        scrollConfiguracion.setHbarPolicy(ScrollBarPolicy.NEVER);
        
        
        contMenuCuerpo.getChildren().addAll(menuConfiguracion, scrollConfiguracion);
        menuConfiguracion.setMinSize(200, 500);
        
        final HBox contLabelGeneral = new HBox();
        final Label labelCGeneral = new Label("GENERAL");
        aOpciones.add(contLabelGeneral);
        aLabelOpciones.add(labelCGeneral);

        contLabelGeneral.setOnMouseClicked(new EventHandler<MouseEvent>(){
			 @Override
	          public void handle(MouseEvent me) {
				 for (int i = 0; i < aOpciones.size(); i++){
					 ((Node) aOpciones.get(i)).setStyle("-fx-background-color: transparent; -fx-text-fill: black");
					 ((Node)aLabelOpciones.get(i)).setStyle("-fx-text-fill: black");
				 }
				 
				 contLabelGeneral.setStyle("-fx-background-color: #474747; -fx-background-radius: 5 5 0 0");
				 labelCGeneral.setStyle("-fx-text-fill: white");
				 
				 generarConfiguracionGeneral();
			  }
	 
	      });
        
        contLabelGeneral.getStyleClass().add("contenedor-opcion");
        contLabelGeneral.setPrefSize(180, 25);
        labelCGeneral.getStyleClass().add("opcion-configuracion");
        contLabelGeneral.getChildren().add(labelCGeneral);
        //Opcion Directorios
        final HBox contLabelDirectorios = new HBox();
        final Label labelCDirectorios = new Label("DIRECTORIOS");
        aOpciones.add(contLabelDirectorios);
        aLabelOpciones.add(labelCDirectorios);

        
        contLabelDirectorios.setOnMouseClicked(new EventHandler<MouseEvent>(){
			 @Override
	          public void handle(MouseEvent me) {
				 for (int i = 0; i < aOpciones.size(); i++){
					 ((Node)aOpciones.get(i)).setStyle("-fx-background-color: transparent; -fx-text-fill: black");
					 ((Node)aLabelOpciones.get(i)).setStyle("-fx-text-fill: black");
				 }
				 
				 contLabelDirectorios.setStyle("-fx-background-color: #474747");
				 labelCDirectorios.setStyle("-fx-text-fill: white");
				 generarConfiguracionDirectorios();
			  }
	 
	      });
        
        contLabelDirectorios.getStyleClass().add("contenedor-opcion");
        labelCDirectorios.getStyleClass().add("opcion-configuracion");
        contLabelDirectorios.getChildren().add(labelCDirectorios);
        contLabelDirectorios.setPrefSize(180, 25);

        final HBox contLabelUsuarios = new HBox();
        final Label labelCUsuarios = new Label("USUARIOS");
        aOpciones.add(contLabelUsuarios);
        aLabelOpciones.add(labelCUsuarios);

        contLabelUsuarios.setOnMouseClicked(new EventHandler<MouseEvent>(){
			 @Override
	          public void handle(MouseEvent me) {
				 for (int i = 0; i < aOpciones.size(); i++){
					 ((Node)aOpciones.get(i)).setStyle("-fx-background-color: transparent");
					 ((Node)aLabelOpciones.get(i)).setStyle("-fx-text-fill: black");
				 }
				 
				 contLabelUsuarios.setStyle("-fx-background-color: #474747");
				 labelCUsuarios.setStyle("-fx-text-fill: white");
			  }
	 
	      });

        contLabelUsuarios.getStyleClass().add("contenedor-opcion");
        labelCUsuarios.getStyleClass().add("opcion-configuracion");
        contLabelUsuarios.getChildren().add(labelCUsuarios);
        
        final HBox contLabelModulos = new HBox();
        final Label labelCModulos = new Label("MÓDULOS");
        aOpciones.add(contLabelModulos);
        aLabelOpciones.add(labelCModulos);

        
        contLabelModulos.setOnMouseClicked(new EventHandler<MouseEvent>(){
			 @Override
	          public void handle(MouseEvent me) {
				 for (int i = 0; i < aOpciones.size(); i++){
					 ((Node)aOpciones.get(i)).setStyle("-fx-background-color: transparent");
					 ((Node)aLabelOpciones.get(i)).setStyle("-fx-text-fill: black");
				 }
				 
				 contLabelModulos.setStyle("-fx-background-color: #474747");
				 labelCModulos.setStyle("-fx-text-fill: white");
			  }
	 
	      });
        
        contLabelModulos.getStyleClass().add("contenedor-opcion");
        labelCModulos.getStyleClass().add("opcion-configuracion");
        contLabelModulos.getChildren().add(labelCModulos);
        
        menuConfiguracion.getChildren().addAll(contLabelGeneral, contLabelDirectorios, contLabelUsuarios, contLabelModulos);
        menuConfiguracion.setId("menu-configuracion");
        cuerpoConfiguracion.setId("cuerpo-configuracion");
        panelConfiguracion.getChildren().addAll(contMenuCuerpo);
        //-------------------------------------------------------------------------
        
        //---------------------------------PANEL INICIO----------------------------
        panelInicio = new VBox(10);
        VBox.setVgrow(panelInicio, Priority.ALWAYS);
        panelInicio.setId("panel-inicio");
        
        HBox contConexionMod = new HBox(5);
        VBox contExtConexion = new VBox(10);
        HBox contConexion = new HBox(10);
        Label labelTituloConexion = new Label("ESTADO DE LA CONEXIÓN");
        labelTituloConexion.setFont(Font.loadFont(getClass().getResourceAsStream("/fuentes/DroidSans-Bold.ttf"), 14));
        contExtConexion.getChildren().add(labelTituloConexion);
        contExtConexion.getChildren().add(contConexion);
        
        VBox contDetallesConexion = new VBox(5);
        DropShadow dsConexion = new DropShadow();
        dsConexion.setOffsetY(3.0);
        dsConexion.setOffsetX(3.0);
        dsConexion.setColor(Color.GRAY);
        ImageView ivServidor = new ImageView(new Image("images/server.png"));
        ivServidor.setFitHeight(128);
        ivServidor.setFitWidth(128);
        HBox contEstado = new HBox(90);
        Label labelEtiEstado = new Label("Estado: ");
        labelEtiEstado.setFont(Font.loadFont(getClass().getResourceAsStream("/fuentes/DroidSans.ttf"), 13));
        labelEstado = new Label("Intentando conectarse al servidor");
        labelEstado.setFont(Font.loadFont(getClass().getResourceAsStream("/fuentes/DroidSans.ttf"), 13));
        HBox contUsuario = new HBox(84);
        Label labelEtiUsuario = new Label("Usuario: ");
        labelEtiUsuario.setFont(Font.loadFont(getClass().getResourceAsStream("/fuentes/DroidSans.ttf"), 13));
        labelUsuario = new Label("Sin especificar...");
        labelUsuario.setFont(Font.loadFont(getClass().getResourceAsStream("/fuentes/DroidSans.ttf"), 13));
        HBox contEspacio = new HBox(54);
        Label labelEtiEspacio = new Label("Espacio libre: ");
        labelEtiEspacio.setFont(Font.loadFont(getClass().getResourceAsStream("/fuentes/DroidSans.ttf"), 13));
        labelEspacio = new Label("Sin especificar...");
        labelEspacio.setFont(Font.loadFont(getClass().getResourceAsStream("/fuentes/DroidSans.ttf"), 13));
        HBox contUsuarios = new HBox(8);
        Label labelEtiUsuarios = new Label("Usuarios conectados:");
        labelEtiUsuarios.setFont(Font.loadFont(getClass().getResourceAsStream("/fuentes/DroidSans.ttf"), 13));
        labelUsuarios = new Label("Ningun usuario conectado");
        labelUsuarios.setFont(Font.loadFont(getClass().getResourceAsStream("/fuentes/DroidSans.ttf"), 13));
        labelEtiUsuarios.getStyleClass().add("label-inicio");
        labelUsuarios.getStyleClass().add("label-inicio");
        HBox contRutaServidor = new HBox(6);
        Label labelEtiRutaServidor = new Label("Carpeta en el servidor:");
        labelEtiRutaServidor.setFont(Font.loadFont(getClass().getResourceAsStream("/fuentes/DroidSans.ttf"), 13));
        labelRutaServidor = new Text("Sin especificar");
        labelRutaServidor.setWrappingWidth(200);
        labelRutaServidor.setFont(Font.loadFont(getClass().getResourceAsStream("/fuentes/DroidSans.ttf"), 13));
        labelRutaServidor.setSmooth(true);
        
        contUsuario.getChildren().addAll(labelEtiUsuario, labelUsuario);
        contEstado.getChildren().addAll(labelEtiEstado, labelEstado);
        contEspacio.getChildren().addAll(labelEtiEspacio, labelEspacio);
        contUsuarios.getChildren().addAll(labelEtiUsuarios, labelUsuarios);
        contRutaServidor.getChildren().addAll(labelEtiRutaServidor, labelRutaServidor);
        
        
        contExtConexion.getStyleClass().add("detalles-conexion");
        contDetallesConexion.getChildren().addAll(contEstado, contUsuario, contEspacio, contUsuarios, contRutaServidor);
        contConexion.getChildren().add(ivServidor);
        contConexion.getChildren().add(contDetallesConexion);
        
        opcionInicio();
        //----------------------------MODULOS-----------------------------------------------------
        VBox contExtModulos = new VBox(10);
        contExtModulos.getStyleClass().add("detalles-conexion");
        Label labelTituloModulos = new Label("MÓDULOS");
        labelTituloModulos.setFont(Font.loadFont(getClass().getResourceAsStream("/fuentes/DroidSans.ttf"), 13));

        labelTituloModulos.setStyle("-fx-font-weight: bold; -fx-font-size: 14"); 
        HBox contModulos = new HBox(20);
        contExtModulos.getChildren().add(labelTituloModulos);
        contExtModulos.getChildren().add(contModulos);
        contExtModulos.getStyleClass().add("detalles-conexion");
        HBox.setHgrow(contModulos, Priority.ALWAYS);
        ImageView ivModulos = new ImageView(new Image("images/codeblocks.png"));
        ivModulos.setFitHeight(128);
        ivModulos.setFitWidth(128);
        contModulos.getChildren().add(ivModulos);
        VBox contDetallesModulos = new VBox();
        HBox contTR = new HBox();
        Label labelEtiModReal = new Label("Copia de seguridad en tiempo real: ");
        labelEtiModReal.setFont(Font.loadFont(getClass().getResourceAsStream("/fuentes/DroidSans.ttf"), 13));

        labelEtiModReal.getStyleClass().add("label-inicio");
        ImageView ivReal = new ImageView(new Image("images/ok.png"));
        ivReal.setFitHeight(24);
        ivReal.setFitWidth(24);
        contTR.getChildren().addAll(labelEtiModReal, ivReal);
        HBox contSincro = new HBox();
        Label labelEtiSincro = new Label("Módulo de sincronización:  ");
        labelEtiSincro.setFont(Font.loadFont(getClass().getResourceAsStream("/fuentes/DroidSans.ttf"), 13));

        labelEtiSincro.getStyleClass().add("label-inicio");
        ImageView ivSincro = new ImageView(new Image("images/ok.png"));
        ivSincro.setFitHeight(24);
        ivSincro.setFitWidth(24);
        contSincro.getChildren().addAll(labelEtiSincro, ivSincro);
        HBox contRestaurar = new HBox();
        Label labelEtiRestaurar = new Label("Módulo de restauración en un 1 click:  ");
        labelEtiRestaurar.setFont(Font.loadFont(getClass().getResourceAsStream("/fuentes/DroidSans.ttf"), 13));

        labelEtiRestaurar.getStyleClass().add("label-inicio");
        ImageView ivRestaurar = new ImageView(new Image("images/ok.png"));
        ivRestaurar.setFitHeight(24);
        ivRestaurar.setFitWidth(24);
        contRestaurar.getChildren().addAll(labelEtiRestaurar, ivRestaurar);
        //Cont versiones
        HBox contVersiones = new HBox(5);
        Label labelEtiVersiones = new Label("Módulo de versiones: ");
        labelEtiVersiones.setFont(Font.loadFont(getClass().getResourceAsStream("/fuentes/DroidSans.ttf"), 13));

        labelEtiVersiones.getStyleClass().add("label-inicio");
        ImageView ivVersiones = new ImageView(new Image("images/ok.png"));
        ivVersiones.setFitHeight(24);
        ivVersiones.setFitWidth(24);
        contVersiones.getChildren().addAll(labelEtiVersiones, ivVersiones);
        contDetallesModulos.getChildren().addAll(contTR, contSincro, contRestaurar, contVersiones);
        contModulos.getChildren().add(contDetallesModulos);
        //---------------------------------------------------------------------------------------
        contConexionMod.getChildren().addAll(contExtConexion, contExtModulos);
        panelInicio.getChildren().add(contConexionMod);
        //-------------------------------------------------------------------------
        //---------------------------------RESTAURACION CON UN CLIC-------------------------------
        HBox contFila2 = new HBox();
        VBox contModuloRestaurar = new VBox(10);
        contFila2.getChildren().add(contModuloRestaurar);
        contModuloRestaurar.setMaxSize(485, 200);
        contModuloRestaurar.setPrefSize(485, 170);
        contModuloRestaurar.getStyleClass().add("detalles-conexion");
        Label labelTituloRestaurar = new Label("RESTAURACIÓN CON UN CLICK");
        labelTituloRestaurar.setFont(Font.loadFont(getClass().getResourceAsStream("/fuentes/DroidSans-Bold.ttf"), 20));
        labelTituloRestaurar.setStyle("-fx-font-weight: bold; -fx-font-size: 14"); 
        Label labelInfoRestaurar = new Label("La función de restauración le permite restaurar su equipo en caso de perdida de datos masiva. Todos los ficheros se ubicaran donde estaban ubicados antes de la perdida.");
        labelInfoRestaurar.setFont(Font.loadFont(getClass().getResourceAsStream("/fuentes/DroidSans.ttf"), 20));
        labelInfoRestaurar.getStyleClass().add("label-inicio");
        labelInfoRestaurar.setWrapText(true);
        HBox contBotonRestaurar = new HBox(100);
        Button botonRestaurar = new Button("Restaurar");
        botonRestaurar.setFont(Font.loadFont(getClass().getResourceAsStream("/fuentes/DroidSans.ttf"), 13));

        
        botonRestaurar.setOnAction(new EventHandler<ActionEvent>(){
        	@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
        		restaurar();
			}
        	
        });
        
        Label labelUltimaCopia = new Label("Última copia: 23/10/2013 18:55");
        labelUltimaCopia.setFont(Font.loadFont(getClass().getResourceAsStream("/fuentes/DroidSans.ttf"), 13));
        contBotonRestaurar.getChildren().addAll(labelUltimaCopia, botonRestaurar);
        HBox.setHgrow(contBotonRestaurar, Priority.ALWAYS);
        Label lEtiElementoActual = new Label("Sincronizando actualmente: ");
        lEtiElementoActual.setFont(Font.loadFont(getClass().getResourceAsStream("/fuentes/DroidSans.ttf"), 13));
        lElementoActual = new Label("En reposo...");
        HBox contActual = new HBox();
        contActual.getChildren().addAll(lEtiElementoActual, lElementoActual);
        
        contModuloRestaurar.getChildren().addAll(labelTituloRestaurar,labelInfoRestaurar, contBotonRestaurar, contActual);
        panelInicio.getChildren().add(contFila2);
        //----------------------------------------------------------------------------------------
        //---------------------------------------GRAFICA CARGA------------------------------------
        VBox contGrafica = new VBox(20);
        Label labelGrafica = new Label("CARGA DEL SERVIDOR");
        contGrafica.setStyle("-fx-background-color: light-grey");
        labelGrafica.setFont(Font.loadFont(getClass().getResourceAsStream("/fuentes/DroidSans-Bold.ttf"), 14));
        Canvas canvas = new Canvas(500, 150);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        
        Double [] xPoints = new Double[2];
        Double [] yPoints = new Double[1];
        Double [] nPoints = new Double[1];

        
        xPoints[0] = 0.0;
        xPoints[1] = 100.0;
        
        gc.fillPolygon(new double[]{10, 40, 10, 40},
                new double[]{210, 210, 240, 240}, 4);        
        Line l0 = new Line(0, 0, 0, 100);
        l0.setStroke(Color.GRAY);
        Line l1 = new Line(1, 0, 500, 0);
        l1.setStroke(Color.GRAY);
        Line l2 = new Line(1, 25, 500, 25);
        l2.setStroke(Color.LIGHTGRAY);
        Line l3 = new Line(1, 50, 500, 50);
        l3.setStroke(Color.LIGHTGRAY);
        Line l4 = new Line(1, 75, 500, 75);
        l4.setStroke(Color.LIGHTGRAY);
        Line l5 = new Line(1, 100, 500, 100);
        l5.setStroke(Color.GRAY);
        Line l6 = new Line(500, 0, 500, 100);
        l6.setStroke(Color.GRAY);
        Line p1 = new Line(1, 100, 60, 60);
        p1.setStroke(Color.ORANGE);
        Line p2 = new Line(60, 60, 120, 80);
        p2.setStroke(Color.ORANGE);
        p1.setSmooth(true);

        Group g = new Group();
        
        g.getChildren().addAll(l0,l1, l2, l3, l4, l5, l6, p1,p2);
        contGrafica.getChildren().addAll(labelGrafica, g);
        contGrafica.setPrefSize(500, 150);
        contFila2.getChildren().add(contGrafica);
        
        //----------------------------------------------------------------------------------------
        
        list = new ListViewIcons(this);
        ArrayList aa = new ArrayList();
        aa.add("item1");
        aa.add("item2");
        aa.add("item3");
        aa.add("item4");
        aa.add("item5");
        aa.add("item6");
        aa.add("item7");
        aa.add("item8");
        aa.add("item9");
        aa.add("item10");
        aa.add("item11");
        aa.add("item12");
        
    	//list.setItems(aa);
    	panelExploracion.getChildren().add(list);
    	panelExploracion.setId("panelExploracion");
        
        contenedorCuerpo.getChildren().add(paneles);
        //------------------------FILE MONITOR--------------------------------------
        String nombre_usuario = usuarioSistema.getUsuario();
        Path dir = Paths.get(SistemaOperativo.getPath(nombre_usuario));
        tFileMonitor = new ThreadFileMonitor(dir, cliente, transferencias, colaTransferencias);
        tFileMonitor.start();
        //--------------------------------------------------------------------------
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                //Paramos el thread para que no se quede colgada la app
            	tFileMonitor.interrupt();
            	tConectar.pararThread();
            	//tEnvio.pararThread();
            	//tSincro.pararThread();
            }
        });
        
        primaryStage.show();
        
        //----------------------INICIAMOS EL CLIENTE------------------------------
    	iniciarCliente();
    	//------------------------------------------------------------------------
    }
    
    void OpcionExplorarServidor(){
    	paneles.getChildren().clear();
    	paneles.getChildren().add(panelExploracion);
    	cliente.enviarObjeto("3");
    	Integer iNumElementos = (Integer) cliente.recibirObjeto();
    	ItemDE [] items = new ItemDE[iNumElementos];
    	
    	for (int i = 0; i < iNumElementos; i++){
    		String nombreFichero = (String)cliente.recibirObjeto();
    		ItemDE item = new ItemDE("", nombreFichero, 48, 48, i);
    		items[i] = item;
    	}
		
    	list.setItems(items);

    }
    
    void OpcionTransferencias(){
    	paneles.getChildren().clear();
    	paneles.getChildren().add(panelTransferencias);
    }
    
    void opcionInicio(){
    	paneles.getChildren().clear();
    	paneles.getChildren().add(panelInicio);
    }
    
    void opcionConfiguracion(){
    	paneles.getChildren().clear();
    	paneles.getChildren().add(panelConfiguracion);
    	generarConfiguracionGeneral();	
    }
    
    void iniciarCliente()
    {
    	cliente = new Cliente(colaTransferencias, transferencias, this);
    	clienteMsg = new Cliente(colaTransferencias, transferencias, this);
    	tConectar = new ThreadConectar(cliente);
    	tConectar.start();
    	tConectarMsg = new ThreadConectar(clienteMsg);
    	tConectarMsg.start();
    	
    	tEnvio = new ThreadEnvio(cliente, colaTransferencias, transferencias);
    	tEnvio.start();
    	//Este thread solo la primera vez!
    	//ThreadCDSensibles tDatos = new ThreadCDSensibles("/home/" + usuarioSistema.getUsuario(), colaTransferencias, transferencias);
    	//tDatos.start();
    	//ThreadSincronizacion tSincro = new ThreadSincronizacion("C:\\Users\\" + usuarioSistema.getUsuario() + "\\Desktop\\aaa", clienteMsg, lElementoActual, colaTransferencias, transferencias);
    	//tSincro.start();
    }
    
    void generarConfiguracionGeneral(){ 
    	//Limpiamos primero todo el contenido
    	cuerpoConfiguracion.getChildren().clear();
    	//Seccion Opciones de inicio
    	HBox contOpcionesInicio = new HBox();
    	contOpcionesInicio.setPrefHeight(25);
    	contOpcionesInicio.getStyleClass().add("apartado-configuracion");
    	Label labelSeccionInicio = new Label("OPCIONES DE INICIO");
    	labelSeccionInicio.setStyle("-fx-text-fill: #716e6e");
    	contOpcionesInicio.getChildren().add(labelSeccionInicio);
    	CheckBox checkInicioSistema = new CheckBox("Iniciar NetBackup con el sistema operativo");
    	CheckBox checkInicioMinimizada = new CheckBox("Iniciar la aplicaci�n minimizada");
    	//Seccion Politica de alertas
    	HBox contOpcionesAlertas = new HBox();
    	contOpcionesAlertas.setPrefHeight(25);
    	contOpcionesAlertas.getStyleClass().add("apartado-configuracion");
    	Label labelSeccionAlertas = new Label("POLÍTICA DE ALERTAS");
    	labelSeccionAlertas.setStyle("-fx-text-fill: #716e6e");
    	CheckBox checkErrorGrave = new CheckBox("Alertarme cuando ocurra un error grave");
    	CheckBox checkAvisoTransferencia = new CheckBox("Avisarme cuando se vaya a copiar un archivo o conjunto de archivos en el servidor");
    	CheckBox checkEliminarArchivos = new CheckBox("Avisarme cuando se borre un archivo del servidor");
    	CheckBox checkNuevoArchivo = new CheckBox("Avisarme cuando en mi carpeta de sincronizaci�n se produzcan cambios");
    	//Secci�n idioma
    	HBox contOpcionesIdioma = new HBox();
    	contOpcionesIdioma.setPrefHeight(25);
    	contOpcionesIdioma.getStyleClass().add("apartado-configuracion");
    	Label labelSeccionIdioma = new Label("IDIOMA DE LA APLICACI�N");
    	labelSeccionIdioma.setStyle("-fx-text-fill: #716e6e");
    	HBox contIdioma = new HBox(20);
    	Label labelSeleccionIdioma = new Label("Seleccione el idioma en el que quiere que se muestre la aplicaci�n");
    	ComboBox<String> cbIdioma = new ComboBox<String>();
    	ObservableList<String> opcionesIdioma = 
    		    FXCollections.observableArrayList(
    		        "Espa�ol",
    		        "Ingl�s",
    		        "Valenciano",
    		        "Catal�n"
    		    );
    	cbIdioma.setItems(opcionesIdioma);
    	cbIdioma.setValue("Espa�ol");
    	contIdioma.getChildren().addAll(labelSeleccionIdioma, cbIdioma);
    	
    	contOpcionesAlertas.getChildren().add(labelSeccionAlertas);
    	contOpcionesIdioma.getChildren().add(labelSeccionIdioma);
    	cuerpoConfiguracion.getChildren().addAll(contOpcionesInicio, checkInicioSistema, checkInicioMinimizada, contOpcionesAlertas);
    	cuerpoConfiguracion.getChildren().addAll(checkErrorGrave, checkAvisoTransferencia, checkEliminarArchivos, checkNuevoArchivo);
    	cuerpoConfiguracion.getChildren().addAll(contOpcionesIdioma, contIdioma);

    }
    
    private void generarConfiguracionDirectorios(){
    	cuerpoConfiguracion.getChildren().clear();
    	//Seccion monitoreo directorios
    	HBox contDirectoriosMonitor = new HBox();
    	contDirectoriosMonitor.getStyleClass().add("apartado-configuracion");
    	Label labelSeccionMonitor = new Label("DIRECTORIOS A MONITOREAR");
    	labelSeccionMonitor.setStyle("-fx-text-fill: #716e6e");
    	contDirectoriosMonitor.getChildren().add(labelSeccionMonitor);
    	Label labelExMonitor = new Label("Por defecto NetBackup solo monitorea tu carpeta personal, si quieres que se monitoreen m�s directorios tendr�s que a�adirlos a la lista. Ten cuidado !!! no tiene sentido a�adir directorios que no utilizas, esto podr�a llenar el servidor de archivos que no utilizar�s.");
    	labelExMonitor.setWrapText(true);
    	labelExMonitor.getStyleClass().add("label-configuracion");
    	ListView dirMonitorear = new ListView();
    	dirMonitorear.setPrefHeight(200);
    	Button botonAnyadirMon = new Button("A�adir directorio");
    	//SECCION SINCRO
    	HBox contDirectoriosSincro = new HBox();
    	contDirectoriosSincro.getStyleClass().add("apartado-configuracion");
    	Label labelSeccionSincro = new Label("DIRECTORIOS A SINCRONIZAR");
    	labelSeccionSincro.setStyle("-fx-text-fill: #716e6e");
    	contDirectoriosSincro.getChildren().add(labelSeccionSincro);
    	Label labelExSincro = new Label("Por defecto NetBackup solo sincroniza un directorio para todas las m�quinas de una misma red. Si quieres m�s directorios sincronizados tendr�s que a�adirlos.");
    	labelExSincro.setWrapText(true);
    	ListView dirSincro = new ListView();
    	dirSincro.setPrefHeight(200);
    	
    	cuerpoConfiguracion.getChildren().addAll(contDirectoriosMonitor, labelExMonitor, dirMonitorear, botonAnyadirMon);
    	cuerpoConfiguracion.getChildren().addAll(contDirectoriosSincro, labelExSincro, dirSincro);
    }
    
    private void restaurar(){
    	//Creamos e iniciamos el thread para restaurar archivos para que no se quede bloqueada la aplicaci�n
    	ThreadRestaurar tRestaurar = new ThreadRestaurar(clienteMsg);
    	tRestaurar.start();
    }
    
    public void setConectado(){
    	Platform.runLater(new Runnable(){
    		public void run(){
    	    	labelEstado.setText("Conectado");
    		}
    	});
    }
    
    public void setUsuario(String _usuario){
    	final String usuario = _usuario;

    	Platform.runLater(new Runnable(){
    		public void run(){
    	    	labelUsuario.setText(usuario);
    		}
    	});
    }
    
    public void setEspacioLibre(Long _lEspacioLibre){
    	final Long lEspacioLibre = _lEspacioLibre;

    	Platform.runLater(new Runnable(){
    		public void run(){
    	    	labelEspacio.setText(obtenerTamanyo(lEspacioLibre));
    		}
    	});
    }
    
    public void setUsuarioConectados(Integer _numUsuarios){
    	final Integer numUsuarios = _numUsuarios;

    	Platform.runLater(new Runnable(){
    		public void run(){
    	    	labelUsuarios.setText(numUsuarios + " Usuarios conectados");
    		}
    	});
    }
    
    public void setDirectorioPersonal(String _dirPersonal){
    	final String dirPersonal = _dirPersonal;

    	Platform.runLater(new Runnable(){
    		public void run(){
    	    	labelRutaServidor.setText(dirPersonal);
    		}
    	});
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
    
    
    public void descargarFichero(ItemDE item){
    	cliente.enviarObjeto("4");
    	cliente.enviarObjeto(item.getNombreFichero());
    	cliente.recibirFichero();
    }
}

