import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
 
public class NetBackup extends Application {
    
	private StackPane paneles;
	private VBox panelExploracion, panelTransferencias, panelInicio, panelConfiguracion;
	private ThreadFileMonitor tFileMonitor = null;
	private ThreadConectar tConectar = null;
	private Cliente cliente = null;
	private Cola colaTransferencias = new Cola();
	private ThreadEnvio tEnvio = null;
	private ExtendedTable transferencias = new ExtendedTable();
	private ArrayList<Object> aOpciones = new ArrayList<Object>();
	private ArrayList<Object> aLabelOpciones = new ArrayList<Object>();
	private VBox cuerpoConfiguracion;
	private UsuarioSistema usuarioSistema = null;
	
	public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
    	//------------------------USUARIO DEL SISTEMA-----------------------------
    	usuarioSistema = new UsuarioSistema();
    	//------------------------------------------------------------------------
    	//----------------------INICIAMOS EL CLIENTE------------------------------
    	IniciarCliente();
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
        HBox cTitulo = new HBox();
        HBox cBotonCerrar = new HBox();
        cabecera.setPrefHeight(100);
        cabecera.setId("ventana-principal-cabecera");
        HBox.setHgrow(cTitulo, Priority.ALWAYS);
        Label labelTitulo = new Label("NetBackup");
        labelTitulo.setFont(Font.loadFont("file:resources/fonts/adrip1.ttf", 120));
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
        labelNombre.setStyle("-fx-text-fill: white; -fx-padding: 5 5 5 10");
        barraEstado.getChildren().add(labelNombre);
        HBox contBarraProgreso = new HBox(5);
        contBarraProgreso.setStyle("-fx-padding: 0 10 0 0");
        contBarraProgreso.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(contBarraProgreso, Priority.ALWAYS);
        barraEstado.getChildren().add(contBarraProgreso);
        Label labelEtiTareaActual = new Label("Tarea actual: ");
        labelEtiTareaActual.setStyle("-fx-text-fill: white;");
        Label labelTareaActual = new Label("Ninguna");
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
        botonInicio.getStyleClass().add("boton_toolbar");
        botonInicio.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
            public void handle(ActionEvent event) {
               opcionInicio();
            }
        });
        
        //Boton explorar
        Button botonExplorar = new Button("EXPLORAR ARCHIVOS EN EL SERVIDOR");
        botonExplorar.getStyleClass().add("boton_toolbar");
        botonExplorar.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
            public void handle(ActionEvent event) {
                OpcionExplorarServidor();
            }
        });
        
        //Boton transferencias
        Button botonTransferencias = new Button("TRANSFERENCIAS");
        botonTransferencias.getStyleClass().add("boton_toolbar");
        botonTransferencias.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
            public void handle(ActionEvent event) {
                OpcionTransferencias();
            }
        });
        //Boton configuracion
        Button botonConfiguracion = new Button("CONFIGURACIÓN");
        botonConfiguracion.getStyleClass().add("boton_toolbar");
        botonConfiguracion.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
            public void handle(ActionEvent event) {
                opcionConfiguracion();
            }
        });
        //Boton ayuda
        Button botonAyuda = new Button("AYUDA");
        botonAyuda.getStyleClass().add("boton_toolbar");
        
        contenedorOpciones.getChildren().addAll(botonInicio, botonExplorar, botonTransferencias, botonConfiguracion, botonAyuda);
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
        transferencias.addColumn("TAMAÑO", 0);
        
        panelTransferencias.getChildren().add(transferencias);
        //-------------------------------------------------------------------------
        //-------------------PANEL CONFIGURACION-----------------------------------
        panelConfiguracion = new VBox();
        HBox contMenuCuerpo = new HBox(10);
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
        dropCuerpoConfig.setOffsetX(0.0);
        dropCuerpoConfig.setOffsetY(3.0);
        dropCuerpoConfig.setColor(Color.color(0.4, 0.5, 0.5));
        cuerpoConfiguracion.setEffect(dropShadow);
        HBox.setHgrow(cuerpoConfiguracion, Priority.ALWAYS);
        ScrollPane scrollConfiguracion = new ScrollPane();
        scrollConfiguracion.setStyle("-fx-background-color: transparent; -fx-border-radius: 6; -fx-background-radius: 6");
        scrollConfiguracion.setContent(cuerpoConfiguracion);
        scrollConfiguracion.setPrefWidth(1000);
        scrollConfiguracion.setHbarPolicy(ScrollBarPolicy.NEVER);
        //scrollConfiguracion.setFitToHeight(true);   
        scrollConfiguracion.setFitToWidth(true);        

        contMenuCuerpo.getChildren().addAll(menuConfiguracion, scrollConfiguracion);
        menuConfiguracion.setMinSize(200, 500);
        VBox.setVgrow(panelConfiguracion, Priority.ALWAYS);
        VBox.setVgrow(menuConfiguracion, Priority.ALWAYS);
        
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
        labelTituloConexion.setStyle("-fx-font-weight: bold; -fx-font-size: 14"); 
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
        labelEtiEstado.getStyleClass().add("label-inicio");
        Label labelEstado = new Label("Intentando conectarse al servidor");
        labelEstado.getStyleClass().add("label-inicio");
        HBox contUsuario = new HBox(84);
        Label labelEtiUsuario = new Label("Usuario: ");
        Label labelUsuario = new Label("Sin especificar...");
        labelUsuario.getStyleClass().add("label-inicio");
        labelEtiUsuario.getStyleClass().add("label-inicio");
        
        HBox contEspacio = new HBox(54);
        Label labelEtiEspacio = new Label("Espacio libre: ");
        Label labelEspacio = new Label("Sin especificar...");
        labelEtiEspacio.getStyleClass().add("label-inicio");
        labelEspacio.getStyleClass().add("label-inicio");
        
        HBox contUsuarios = new HBox(8);
        Label labelEtiUsuarios = new Label("Usuarios conectados:");
        Label labelUsuarios = new Label("Ningun usuario conectado");
        labelEtiUsuarios.getStyleClass().add("label-inicio");
        labelUsuarios.getStyleClass().add("label-inicio");
        
        HBox contRutaServidor = new HBox(6);
        Label labelEtiRutaServidor = new Label("Carpeta en el servidor:");
        Label labelRutaServidor = new Label("/home/k3rnel/Escritorio/NetBackup");
        labelEtiRutaServidor.getStyleClass().add("label-inicio");
        labelRutaServidor.getStyleClass().add("label-inicio");
        
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
        VBox contExtModulos = new VBox();
        contExtModulos.getStyleClass().add("detalles-conexion");
        Label labelTituloModulos = new Label("MÓDULOS");
        labelTituloModulos.setStyle("-fx-font-weight: bold; -fx-font-size: 14"); 
        HBox contModulos = new HBox();
        contExtModulos.getChildren().add(labelTituloModulos);
        contExtModulos.getChildren().add(contModulos);
        contModulos.getStyleClass().add("detalles-conexion");
        HBox.setHgrow(contModulos, Priority.ALWAYS);
        ImageView ivModulos = new ImageView(new Image("images/puzzle.png"));
        ivModulos.setFitHeight(128);
        ivModulos.setFitWidth(128);
        contModulos.getChildren().add(ivModulos);
        VBox contDetallesModulos = new VBox(5);
        HBox contTR = new HBox();
        Label labelEtiModReal = new Label("Copia de seguridad en tiempo real: ");
        labelEtiModReal.getStyleClass().add("label-inicio");
        ImageView ivReal = new ImageView(new Image("images/ok.png"));
        ivReal.setFitHeight(24);
        ivReal.setFitWidth(24);
        contTR.getChildren().addAll(labelEtiModReal, ivReal);
        HBox contSincro = new HBox();
        Label labelEtiSincro = new Label("Módulo de sincronización:  ");
        labelEtiSincro.getStyleClass().add("label-inicio");
        ImageView ivSincro = new ImageView(new Image("images/ok.png"));
        ivSincro.setFitHeight(24);
        ivSincro.setFitWidth(24);
        contSincro.getChildren().addAll(labelEtiSincro, ivSincro);
        HBox contRestaurar = new HBox();
        Label labelEtiRestaurar = new Label("Módulo de restauración en un 1 click:  ");
        labelEtiRestaurar.getStyleClass().add("label-inicio");
        ImageView ivRestaurar = new ImageView(new Image("images/ok.png"));
        ivRestaurar.setFitHeight(24);
        ivRestaurar.setFitWidth(24);
        contRestaurar.getChildren().addAll(labelEtiRestaurar, ivRestaurar);
        //Cont versiones
        HBox contVersiones = new HBox(5);
        Label labelEtiVersiones = new Label("Módulo de versiones: ");
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
        VBox contModuloRestaurar = new VBox(10);
        contModuloRestaurar.setMaxSize(485, 200);
        contModuloRestaurar.setPrefSize(485, 200);
        contModuloRestaurar.getStyleClass().add("detalles-conexion");
        Label labelTituloRestaurar = new Label("RESTAURACIÓN CON UN CLICK");
        labelTituloRestaurar.setStyle("-fx-font-weight: bold; -fx-font-size: 14"); 
        Label labelInfoRestaurar = new Label("La función de restauración le permite restaurar su equipo en caso de perdida de datos masiva. Todos los ficheros se ubicaran donde estaban ubicados antes de la perdida.");
        labelInfoRestaurar.getStyleClass().add("label-inicio");
        labelInfoRestaurar.setWrapText(true);
        HBox contBotonRestaurar = new HBox(100);
        Button botonRestaurar = new Button("Restaurar");
        Label labelUltimaCopia = new Label("Última copia: 23/10/2013 18:55");
        contBotonRestaurar.getChildren().addAll(labelUltimaCopia, botonRestaurar);
        HBox.setHgrow(contBotonRestaurar, Priority.ALWAYS);
        contModuloRestaurar.getChildren().addAll(labelTituloRestaurar,labelInfoRestaurar, contBotonRestaurar);
        panelInicio.getChildren().add(contModuloRestaurar);
        //----------------------------------------------------------------------------------------
        
        
        ListViewIcons list = new ListViewIcons();
    	list.setId("lista");
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
        
    	list.setItems(aa);
    	panelExploracion.getChildren().add(list);
    	panelExploracion.setId("panelExploracion");
        
        contenedorCuerpo.getChildren().add(paneles);
        //------------------------FILE MONITOR--------------------------------------
        Path dir = Paths.get("C:\\Users\\" + usuarioSistema.getUsuario() + "\\Desktop");
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
            }
        });
        primaryStage.show();
    }
    
    void OpcionExplorarServidor(){
    	paneles.getChildren().clear();
    	paneles.getChildren().add(panelExploracion);
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
    
    void IniciarCliente()
    {
    	cliente = new Cliente(colaTransferencias, transferencias);
    	tConectar = new ThreadConectar(cliente);
    	tConectar.start();
    	tEnvio = new ThreadEnvio(cliente, colaTransferencias, transferencias);
    	tEnvio.start();
    	//Este thread solo la primera vez!
    	//ThreadCDSensibles tDatos = new ThreadCDSensibles("C:\\Users\\" + usuarioSistema.getUsuario(), colaTransferencias, transferencias);
    	//tDatos.start();
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
    	CheckBox checkInicioMinimizada = new CheckBox("Iniciar la aplicación minimizada");
    	//Seccion Politica de alertas
    	HBox contOpcionesAlertas = new HBox();
    	contOpcionesAlertas.setPrefHeight(25);
    	contOpcionesAlertas.getStyleClass().add("apartado-configuracion");
    	Label labelSeccionAlertas = new Label("POLÍTICA DE ALERTAS");
    	labelSeccionAlertas.setStyle("-fx-text-fill: #716e6e");
    	CheckBox checkErrorGrave = new CheckBox("Alertarme cuando ocurra un error grave");
    	CheckBox checkAvisoTransferencia = new CheckBox("Avisarme cuando se vaya a copiar un archivo o conjunto de archivos en el servidor");
    	CheckBox checkEliminarArchivos = new CheckBox("Avisarme cuando se borre un archivo del servidor");
    	CheckBox checkNuevoArchivo = new CheckBox("Avisarme cuando en mi carpeta de sincronización se produzcan cambios");
    	//Sección idioma
    	HBox contOpcionesIdioma = new HBox();
    	contOpcionesIdioma.setPrefHeight(25);
    	contOpcionesIdioma.getStyleClass().add("apartado-configuracion");
    	Label labelSeccionIdioma = new Label("IDIOMA DE LA APLICACIÓN");
    	labelSeccionIdioma.setStyle("-fx-text-fill: #716e6e");
    	HBox contIdioma = new HBox(20);
    	Label labelSeleccionIdioma = new Label("Seleccione el idioma en el que quiere que se muestre la aplicación");
    	ComboBox<String> cbIdioma = new ComboBox<String>();
    	ObservableList<String> opcionesIdioma = 
    		    FXCollections.observableArrayList(
    		        "Español",
    		        "Inglés",
    		        "Valenciano",
    		        "Catalán"
    		    );
    	cbIdioma.setItems(opcionesIdioma);
    	cbIdioma.setValue("Español");
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
    	Label labelExMonitor = new Label("Por defecto NetBackup solo monitorea tu carpeta personal, si quieres que se monitoreen más directorios tendrás que añadirlos a la lista. Ten cuidado !!! no tiene sentido añadir directorios que no utilizas, esto podría llenar el servidor de archivos que no utilizarás.");
    	labelExMonitor.setWrapText(true);
    	labelExMonitor.getStyleClass().add("label-configuracion");
    	ListView dirMonitorear = new ListView();
    	dirMonitorear.setPrefHeight(200);
    	Button botonAnyadirMon = new Button("Añadir directorio");
    	//SECCION SINCRO
    	HBox contDirectoriosSincro = new HBox();
    	contDirectoriosSincro.getStyleClass().add("apartado-configuracion");
    	Label labelSeccionSincro = new Label("DIRECTORIOS A SINCRONIZAR");
    	labelSeccionSincro.setStyle("-fx-text-fill: #716e6e");
    	contDirectoriosSincro.getChildren().add(labelSeccionSincro);
    	Label labelExSincro = new Label("Por defecto NetBackup solo sincroniza un directorio para todas las máquinas de una misma red. Si quieres más directorios sincronizados tendrás que añadirlos.");
    	labelExSincro.setWrapText(true);
    	ListView dirSincro = new ListView();
    	dirSincro.setPrefHeight(200);
    	
    	cuerpoConfiguracion.getChildren().addAll(contDirectoriosMonitor, labelExMonitor, dirMonitorear, botonAnyadirMon);
    	cuerpoConfiguracion.getChildren().addAll(contDirectoriosSincro, labelExSincro, dirSincro);
    }
    
    
}

