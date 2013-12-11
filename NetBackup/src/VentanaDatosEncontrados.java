import java.awt.FlowLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class VentanaDatosEncontrados{
	
	private ArrayList<String> fotosEncontradas = null;
	private ArrayList<String> audiosEncontrados = null;
	private ArrayList<String> docuEncontrados = null;
	private ArrayList<String> videosEncontrados = null;
	private boolean [] bArrayFotos = null;
	private boolean [] bArrayAudio = null;
	private boolean [] bArrayDocumentos = null;
	private boolean [] bArrayVideos = null;
	private ArrayList<HBox> filasItems = new ArrayList<HBox>();
	private boolean bFotos = false, bMusica = false, bDocumentos = false, bVideos = false;
	private HBox cOpciones[] = new HBox[4]; 
	private Label cLabelOpciones[] = new Label[4]; 
	private VBox contenedorMiniaturas = null;
	private final ArrayList<String> ptrFotosEncontradas, ptrAudiosEncontrados, ptrDocuEncontrados;
	private final Cola ptrColaTransferencias;
	private HBox paginador = null;
	private VBox contenedorMiniaturasPaginador = null;
	private Cola colaTransferencias = null;
	private ExtendedTable transferencias = null;
	private ExtendedTable ptrTransferencias = null;
	private Stage stage = null;
	private ArrayList<Transferencia> elementosSeleccionados = null;
	
	public VentanaDatosEncontrados(ArrayList<String> fotosEncontrados, ArrayList<String> audiosEncontrados, ArrayList<String> docuEncontrados, Cola colaTransferencias, ExtendedTable transferencias){
		this.fotosEncontradas = fotosEncontrados;
		ptrFotosEncontradas = fotosEncontradas; 
		this.audiosEncontrados = audiosEncontrados;
		ptrAudiosEncontrados = audiosEncontrados;
		this.docuEncontrados = docuEncontrados;
		ptrDocuEncontrados = docuEncontrados;
		this.colaTransferencias = colaTransferencias;
		ptrColaTransferencias = this.colaTransferencias;
		this.transferencias = transferencias;
		this.ptrTransferencias = this.transferencias;
		
		bArrayFotos = new boolean[fotosEncontradas.size()];
		bArrayAudio = new boolean[audiosEncontrados.size()];
		bArrayDocumentos = new boolean[docuEncontrados.size()];
		
		elementosSeleccionados = new ArrayList<Transferencia>();
		
		Platform.runLater(new Runnable() {

			 @Override
			 public void run() {
				 stage = new Stage(StageStyle.TRANSPARENT);
			     
				 stage.setTitle("NetBackup - Existen datos sensibles por copiar!");
				 StackPane root = new StackPane();
				 root.setId("ventana-datos-encontrados");
			     VBox contenedorPrincipal = new VBox();
			     root.getChildren().add(contenedorPrincipal);
				 Scene scene = new Scene(root, 1060, 680);
				 stage.setMinWidth(1000);
				 stage.setMinWidth(700);
			     scene.setFill(Color.TRANSPARENT);
			     stage.initStyle(StageStyle.TRANSPARENT);
			     stage.setScene(scene);
			     scene.getStylesheets().add("styles.css");
			     //------------------------TITULO------------------------------------
			     HBox contCabecera = new HBox();
			     HBox contTitulo = new HBox();
			     HBox contBotonCerrar = new HBox();
			     HBox contSelTodo = new HBox();
			     Button botonSelTodo = new Button("SELECCIONAR TODO");
			     botonSelTodo.setPrefHeight(25);
			     botonSelTodo.setId("ventana-datos-encontrados-boton-todo");
			     contSelTodo.getChildren().add(botonSelTodo);
			     contSelTodo.setStyle("-fx-padding: 75 0 0 0");
			     contSelTodo.setPrefHeight(100);
			     contBotonCerrar.setStyle("-fx-padding: 5 10 5 10");
			     Image img = new Image("images/close.png");
			     ImageView iv = new ImageView(img);
			     iv.setOnMouseClicked(new EventHandler<MouseEvent>(){
						@Override
				        public void handle(MouseEvent me) {
							for (int i = 0; i < elementosSeleccionados.size(); i++){
								ptrTransferencias.addItem(elementosSeleccionados.get(i));
								ptrColaTransferencias.encolar(elementosSeleccionados.get(i));
							}
							
							stage.close();
						}
					});
			     iv.setFitHeight(16);
			     iv.setFitWidth(16);
			     iv.setStyle("-fx-cursor: hand;");
			     contBotonCerrar.getChildren().add(iv);
			     contCabecera.getChildren().add(contTitulo);
			     contCabecera.getChildren().add(contSelTodo);
			     contCabecera.getChildren().add(contBotonCerrar);

			     contTitulo.setPrefHeight(100);
			     contTitulo.setId("ventana-datos-encontrados-cabecera");
			     HBox.setHgrow(contTitulo, Priority.ALWAYS);
			     Label labelTitulo = new Label("ARCHIVOS ENCONTRADOS EN EL DISCO DURO");
			     labelTitulo.setFont(Font.loadFont(getClass().getResourceAsStream("/fuentes/DroidSans.ttf"), 20));
			     contTitulo.getChildren().add(labelTitulo);
			     labelTitulo.setId("ventana-datos-encontrados-titulo");
			     contenedorPrincipal.getChildren().add(contCabecera);
			     VBox separador = new VBox();
			     separador.setPrefHeight(1);
			     separador.setPrefWidth(1000);
			     separador.setId("ventana-datos-encontrados-separador");
			     contenedorPrincipal.getChildren().add(separador);
			     //------------------------------------------------------------------
			     //-----------------------CONTENEDOR MENU Y CUERPO-------------------
			     HBox contenedorCuerpo = new HBox();
			     contenedorCuerpo.setId("ventana-datos-encontrados-cuerpo");
			     contenedorPrincipal.getChildren().add(contenedorCuerpo);
			     VBox.setVgrow(contenedorCuerpo, Priority.ALWAYS);
			     VBox contenedorMenu = new VBox(10);
			     VBox separadorCuerpo = new VBox();
			     separadorCuerpo.setPrefWidth(1);
			     separadorCuerpo.setId("ventana-datos-encontrados-separador");
			     contenedorCuerpo.getChildren().addAll(contenedorMenu, separadorCuerpo);
			     contenedorMenu.setPrefWidth(200);
			     contenedorMenu.setId("ventana-datos-encontrados-menu");
			     final HBox cLabelFotos = new HBox();
			     final Label labelFotos = new Label("IMÁGENES/FOTOS" + " (" + ptrFotosEncontradas.size() + ")");
			     labelFotos.setFont(Font.loadFont(getClass().getResourceAsStream("/fuentes/DroidSans.ttf"), 13));

			     cOpciones[0] = cLabelFotos;
			     cLabelOpciones[0] = labelFotos;
			     cLabelFotos.setOnMouseClicked(new EventHandler<MouseEvent>(){
						@Override
				        public void handle(MouseEvent me) {	
							System.out.println("click");
							
							for (int i = 0; i < 4; i++){
								cOpciones[i].setStyle("-fx-background-color: transparent");
								cLabelOpciones[i].setStyle("-fx-text-fill: black");
							}
							
							bFotos = true;
							bDocumentos = false;
							bMusica = false;
							bVideos = false;
							mostrarElementos(0);
							
							cLabelFotos.setStyle("-fx-background-color: #6e6e6e;");
							labelFotos.setStyle("-fx-text-fill: white;");
						}
					});
			     
			     cLabelFotos.setPrefSize(200, 30);
			     cLabelFotos.setAlignment(Pos.CENTER);
			     cLabelFotos.getChildren().add(labelFotos);
			     final Label labelMusica = new Label("AUDIO/MÚSICA" +  " (" + ptrAudiosEncontrados.size() + ")");
			     labelMusica.setFont(Font.loadFont(getClass().getResourceAsStream("/fuentes/DroidSans.ttf"), 13));

			     final HBox cLabelMusica = new HBox();
			     cOpciones[1] = cLabelMusica;
			     cLabelOpciones[1] = labelMusica;
			     cLabelMusica.setPrefSize(200, 30);

			     cLabelMusica.setOnMouseClicked(new EventHandler<MouseEvent>(){
						@Override
				        public void handle(MouseEvent me) {	
							System.out.println("click");
							
							for (int i = 0; i < 4; i++){
								cOpciones[i].setStyle("-fx-background-color: transparent");
								cLabelOpciones[i].setStyle("-fx-text-fill: black");
							}
							
							bFotos = false;
							bDocumentos = false;
							bMusica = true;
							bVideos = false;
							mostrarElementos(0);
							
							cLabelMusica.setStyle("-fx-background-color: #6e6e6e;");
							labelMusica.setStyle("-fx-text-fill: white;");
						}
					});
			     cLabelMusica.setPrefWidth(200);
			     cLabelMusica.setAlignment(Pos.CENTER);
			     cLabelMusica.getChildren().add(labelMusica);
			     final HBox cLabelDocumentos = new HBox();
			     final Label labelDocumentos = new Label("DOCUMENTOS" + " ("  + ptrDocuEncontrados.size() + ")");
			     labelDocumentos.setFont(Font.loadFont(getClass().getResourceAsStream("/fuentes/DroidSans.ttf"), 13));

			     cOpciones[2] = cLabelDocumentos;
			     cLabelOpciones[2] = labelDocumentos;
			     cLabelDocumentos.setAlignment(Pos.CENTER);

			     cLabelDocumentos.setOnMouseClicked(new EventHandler<MouseEvent>(){
						@Override
				        public void handle(MouseEvent me) {	
							System.out.println("click");
							
							for (int i = 0; i < 4; i++){
								cOpciones[i].setStyle("-fx-background-color: transparent");
								cLabelOpciones[i].setStyle("-fx-text-fill: black");
							}
							
							cLabelDocumentos.setStyle("-fx-background-color: #6e6e6e;");
							labelDocumentos.setStyle("-fx-text-fill: white;");
							//Seteamos los flags
							bFotos = false;
							bDocumentos = true;
							bVideos = false;
							bMusica = false;
							
							mostrarElementos(0);
						}
					});
			     cLabelDocumentos.setPrefSize(200, 30);
			     cLabelDocumentos.getChildren().add(labelDocumentos);
			     
			     final HBox cLabelVideos = new HBox();
			     final Label labelVideos = new Label("VIDEOS");
			     labelVideos.setFont(Font.loadFont(getClass().getResourceAsStream("/fuentes/DroidSans.ttf"), 13));

			     cOpciones[3] = cLabelVideos;
			     cLabelOpciones[3] = labelVideos;
			     cLabelVideos.setAlignment(Pos.CENTER);

			     cLabelVideos.setOnMouseClicked(new EventHandler<MouseEvent>(){
						@Override
				        public void handle(MouseEvent me) {	
							System.out.println("click");
							
							for (int i = 0; i < 4; i++){
								cOpciones[i].setStyle("-fx-background-color: transparent");
								cLabelOpciones[i].setStyle("-fx-text-fill: black");
							}
							
							cLabelVideos.setStyle("-fx-background-color: #6e6e6e;");
							labelVideos.setStyle("-fx-text-fill: white;");
						}
					});
			     cLabelVideos.setPrefSize(200, 30);
			     cLabelVideos.getChildren().add(labelVideos);
			     
			     contenedorMenu.getChildren().addAll(cLabelFotos, cLabelMusica, cLabelDocumentos, cLabelVideos);
			     
			     contenedorMiniaturas = new VBox(10);
			     ScrollPane scroller = new ScrollPane();
			     scroller.setStyle("-fx-background-color: transparent");
			     scroller.setPrefSize(805, 520);
			     scroller.setContent(contenedorMiniaturas);
			     contenedorMiniaturas.setStyle("-fx-padding: 10 0 0 0");
			     VBox.setVgrow(contenedorMiniaturas, Priority.ALWAYS);
			     contenedorMiniaturasPaginador = new VBox();
			     contenedorMiniaturasPaginador.getChildren().add(scroller);
			     
			     bDocumentos = true;
			     mostrarElementos(0);
			     
			     contenedorCuerpo.getChildren().add(contenedorMiniaturasPaginador);
			     VBox espaciadoFinal = new VBox();
			     espaciadoFinal.setPrefHeight(10);
			     espaciadoFinal.setId("ventana-datos-encontrados-espaciado-final");
			     contenedorPrincipal.getChildren().add(espaciadoFinal);
			     
			     stage.show();
			 }
		 });
	}
	
	private void mostrarElementos(int iNumPagina){
		HBox filaActual = null;
	   	ArrayList<String> tipoElementos = null;

	   	contenedorMiniaturas.getChildren().clear();
	    filasItems.clear();
	   	
	   	if (bDocumentos)
	   		tipoElementos = ptrDocuEncontrados;
	   	else if (bMusica)
	   		tipoElementos = ptrAudiosEncontrados;
	   	else if (bFotos)
	   		tipoElementos = ptrFotosEncontradas;
	   
	   	
	    for (int i = 0; i < tipoElementos.size(); i++){ 	
	    	ItemDE item = null;
	    	
	    	File file = null;
	    	
	    	if (bFotos){
		    	file = new File(ptrFotosEncontradas.get(i));
	    		item = new ItemDE(file.getAbsolutePath(), file.getName(), 100, 80, i);
	    		
	    		if (bArrayFotos[i])
	    			item.setSeleccionado(true);
	    		
	    	}
	    	else if (bDocumentos){
	    		file = new File(ptrDocuEncontrados.get(i));
	    		item = new ItemDE(file.getAbsolutePath(), file.getName(), 48, 48, i);
	    		
	    		if (bArrayDocumentos[i])
	    			item.setSeleccionado(true);
	    		
	    	}else if(bMusica){
	    		file = new File(ptrAudiosEncontrados.get(i));
	    		item = new ItemDE(file.getAbsolutePath(), file.getName(), 48, 48, i);
	    		
	    		if (bArrayAudio[i])
	    			item.setSeleccionado(true);
	    		
	    	}
	    	
	    	item.setOnMouseClicked(new EventHandler<MouseEvent>(){
				@Override
		        public void handle(MouseEvent me) {
					ItemDE item = null;
					
					if (me.getTarget() instanceof ItemDE){
						 item = (ItemDE)me.getTarget();
					}
					else{
						Node node = (Node)me.getTarget();
			
						while (!(node instanceof ItemDE))
							node = node.getParent();
						
						item = ((ItemDE) node);
					}
					
					if (!item.getSeleccionado()){
						if (bFotos)
							bArrayFotos[item.getNumeroItem()] = true;
						else if (bDocumentos)
							bArrayDocumentos[item.getNumeroItem()] = true;
						else if (bMusica)
							bArrayAudio[item.getNumeroItem()] = true;
							
						item.setSeleccionado(true);
						elementosSeleccionados.add(new Transferencia(item.getRutaFichero(), item.getNombreFichero(), item.getTamanyo(), "Archivo", "En cola...", ContadorItems.getNumeroItems()));
						ContadorItems.incrementarNumero();
					}	
				}
			});
	    	
	    	/*if (i - 24 * iNumPagina < 6)
	    		contenedorFila1.getChildren().add(item);
	    	else if (i - 24 * iNumPagina< 12)
	    		contenedorFila2.getChildren().add(item);
	    	else if(i - 24 * iNumPagina < 18)
	    		contenedorFila3.getChildren().add(item);
	    	else 
	    		contenedorFila4.getChildren().add(item);*/
	    	
	    	if (i % 6 == 0){
	    		HBox contenedorFila = new HBox(15);
	    		contenedorFila.setStyle("-fx-padding: 0 0 0 13");
	    		filaActual = contenedorFila;
	    		filaActual.getChildren().add(item);
	    		filasItems.add(contenedorFila);
	       	 	contenedorMiniaturas.getChildren().add(contenedorFila); 
	    	}else{
	    		filaActual.getChildren().add(item);
	    	}
	     }
   	 
   	 	//crearPaginador(contenedorMiniaturasPaginador);
   	 	
	}
}
