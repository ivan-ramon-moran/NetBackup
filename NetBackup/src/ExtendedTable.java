import java.util.ArrayList;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class ExtendedTable extends ScrollPane{
	
	private HBox contenedorColumnas = null;
	private ArrayList<ProgressBar> alBarras = new ArrayList<ProgressBar>(); 
	private ArrayList<Label> alEstado = new ArrayList<Label>();
	private ArrayList<HBox> alFilas = new ArrayList<HBox>(); 
	private ArrayList<ExtendedTableDirectorio> directorios = new ArrayList<ExtendedTableDirectorio>(); 

	private VBox contenedorPrincipal = null;
	private int iNumeroItems = 0;
	
	public ExtendedTable(){
		contenedorPrincipal = new VBox();
		contenedorPrincipal.setId("extended-table");
		//el hbox que contendra las columnas
		contenedorColumnas = new HBox();
		contenedorColumnas.setPrefHeight(30);
		contenedorColumnas.setMinHeight(30);
		contenedorColumnas.setId("extended-table-header");
		contenedorPrincipal.getChildren().add(contenedorColumnas);
		HBox separador = new HBox();
		separador.setId("transferencias-separador");
		separador.setPrefHeight(1);
		contenedorPrincipal.getChildren().add(separador);
		DropShadow ds = new DropShadow();
        ds.setOffsetY(3.0);
        ds.setOffsetX(3.0);
        ds.setColor(Color.GRAY);
        this.setEffect(ds);
        this.setMinHeight(500);
		this.setPrefHeight(500);
		this.setId("extended-table-scroller");
		this.setContent(contenedorPrincipal);
		this.setFitToHeight(true);
		this.setFitToWidth(true);
	}
	
	public void addColumn(String tituloColumna, int tamanyo){
		HBox cont = new HBox();
		
		if (tamanyo != 0){
			cont.setPrefWidth(tamanyo);
		}else{
			HBox.setHgrow(cont, Priority.ALWAYS);
		}
		
		cont.setId("extended-table-cont-label-column");
		cont.setPadding(new Insets(6,0,0,5));
		Label label = new Label(tituloColumna);
		label.setId("extended-table-label-column");
		cont.getChildren().add(label);
		this.contenedorColumnas.getChildren().add(cont);
		/*HBox separador = new HBox();
		separador.getStyleClass().add("separador-columna");
		separador.setPrefWidth(1);
		this.contenedorColumnas.getChildren().add(separador);*/
	}
	
	public void addItem(Transferencia transferencia){
		final HBox fila = new HBox();
		fila.setPrefHeight(27);
		fila.setMinHeight(27);

		if (iNumeroItems % 2 == 0)
			fila.getStyleClass().add("extended-table-fila-par");
		else
			fila.getStyleClass().add("extended-table-fila-impar");

			
		alFilas.add(fila);
		fila.setOnMouseClicked(new EventHandler<MouseEvent>(){
			@Override
	        public void handle(MouseEvent me) {
				for (int i = 0; i < alFilas.size(); i++){
					alFilas.get(i).setStyle("-fx-background-color: transparent");
				}
				
				fila.setStyle("-fx-background-color: rgb(48,109,202);");
			}
		});
		
		HBox contNombre = new HBox();
		contNombre.setPrefWidth(400);
		Label labelNombre = new Label(transferencia.getNombreArchivo());
		labelNombre.getStyleClass().add("extended-table-celda");
		contNombre.getChildren().add(labelNombre);
		fila.getChildren().add(contNombre);
		HBox contEstado = new HBox();
		contEstado.setPrefWidth(100);
		Label labelEstado = new Label(transferencia.getEstadoTransferencia());
		alEstado.add(labelEstado);
		labelEstado.getStyleClass().add("extended-table-celda");
		contEstado.getChildren().add(labelEstado);
		fila.getChildren().add(contEstado);
		HBox contBarra = new HBox();
		contBarra.setPadding(new Insets(6,0,0,5));
		contBarra.setPrefWidth(153);
		ProgressBar barra = new ProgressBar();
		/*Insertamos la barra en el vector de barras, para poder luego acceder a ella
		en el transcurso de la transferencia.*/
		alBarras.add(barra);
		barra.setPrefSize(145, 15);
		barra.setProgress(0.0);
		contBarra.getChildren().add(barra);
		fila.getChildren().add(contBarra);
		HBox contTamanyo = new HBox();
		contTamanyo.setPrefWidth(100);
		Label labelTamanyo = new Label(transferencia.getTamanyoArchivo());
		labelTamanyo.getStyleClass().add("extended-table-celda");
		contTamanyo.getChildren().add(labelTamanyo);
		fila.getChildren().add(contTamanyo);
		//Miniatura
		ImageView ivMiniatura = new ImageView(obtenerImagen(transferencia.getNombreArchivo()));
		HBox contMiniatura = new HBox();
		contMiniatura.setPrefWidth(100);
		contMiniatura.getChildren().add(ivMiniatura);
		fila.getChildren().add(contMiniatura);
		this.contenedorPrincipal.getChildren().add(fila);
		HBox separador = new HBox();
		separador.setId("transferencias-separador");
		separador.setPrefHeight(1);
		contenedorPrincipal.getChildren().add(separador);
		iNumeroItems++;
	}
	
	public void addDirectory(String nombreDir, ArrayList<Transferencia> transferencias){
		final HBox fila = new HBox();
		fila.setPrefHeight(27);
		
		if (iNumeroItems % 2 == 0)
			fila.getStyleClass().add("extended-table-fila-par");
		else
			fila.getStyleClass().add("extended-table-fila-impar");

			
		alFilas.add(fila);
		/*fila.setOnMouseClicked(new EventHandler<MouseEvent>(){
			@Override
	        public void handle(MouseEvent me) {
				for (int i = 0; i < alFilas.size(); i++){
					alFilas.get(i).setStyle("-fx-background-color: transparent");
				}
				
				fila.setStyle("-fx-background-color: rgb(48,109,202);");
			}
		});*/
		
		HBox contNombre = new HBox();
		HBox contExpandir = new HBox();
		contExpandir.setStyle("-fx-padding: 3 0 0 0");
		ExtendedTableExpander expander = new ExtendedTableExpander(iNumeroItems);
		contExpandir.getChildren().add(expander);
		
		expander.setOnMouseClicked(new EventHandler<MouseEvent>(){
			@Override
	        public void handle(MouseEvent me) {
				ExtendedTableExpander exp = (ExtendedTableExpander)me.getTarget();
				int id = exp.getID();
				System.out.println("ID: " + id);
				ExtendedTableDirectorio dir = directorios.get(id);
				dir.expandir();		
				
				if (dir.getExpandido())
					dir.setExpandido(false);
				else
					dir.setExpandido(true);
				
			}
		});
		
		
		contNombre.setPrefWidth(377);
		fila.getChildren().add(contExpandir);
		
		Label labelNombre = new Label(nombreDir);
		labelNombre.getStyleClass().add("extended-table-celda");
		contNombre.getChildren().addAll(labelNombre);
		fila.getChildren().add(contNombre);
		HBox contEstado = new HBox();
		contEstado.setPrefWidth(100);
		Label labelEstado = new Label("En cola");
		//alEstado.add(labelEstado);
		labelEstado.getStyleClass().add("extended-table-celda");
		contEstado.getChildren().add(labelEstado);
		fila.getChildren().add(contEstado);
		HBox contBarra = new HBox();
		contBarra.setPadding(new Insets(6,0,0,5));
		contBarra.setPrefWidth(153);
		ProgressBar barra = new ProgressBar();
		/*Insertamos la barra en el vector de barras, para poder luego acceder a ella
		en el transcurso de la transferencia.*/
		//alBarras.add(barra);
		barra.setPrefSize(145, 15);
		barra.setProgress(0.0);
		contBarra.getChildren().add(barra);
		fila.getChildren().add(contBarra);
		HBox contTamanyo = new HBox();
		Label labelTamanyo = new Label("Sin establecer");
		labelTamanyo.getStyleClass().add("extended-table-celda");
		contTamanyo.getChildren().add(labelTamanyo);
		contTamanyo.setPrefWidth(100);
		fila.getChildren().add(contTamanyo);
		//Miniatura
		ImageView ivMiniatura = new ImageView(new Image("images/folder.png"));
		HBox contMiniatura = new HBox();
		contMiniatura.setPrefWidth(100);
		contMiniatura.getChildren().add(ivMiniatura);
		fila.getChildren().add(contMiniatura);
		
		this.contenedorPrincipal.getChildren().add(fila);
		HBox separador = new HBox();
		separador.setId("transferencias-separador");
		separador.setPrefHeight(1);
		contenedorPrincipal.getChildren().add(separador);
		
		ExtendedTableDirectorio dir = new ExtendedTableDirectorio(transferencias, iNumeroItems, this.contenedorPrincipal, this.alBarras, this.alEstado, this.alFilas);
		directorios.add(dir);
		iNumeroItems++;
	}
	
	public static  Image obtenerImagen(String strNombreArchivo){
		Image image = null;
		
		if (strNombreArchivo.contains(".jpg") || strNombreArchivo.contains(".png"))
			image = new Image("images/image-png.png");
		else if (strNombreArchivo.contains(".pdf"))
			image = new Image("images/application-pdf.png");
		else if (strNombreArchivo.contains(".mp3"))
			image = new Image("images/mp3.png");
		else if (strNombreArchivo.contains(".zip"))
			image = new Image("images/application-x-zip.png");
		else if (strNombreArchivo.contains(".rar"))
			image = new Image("images/application-x-rar.png");
		else if (strNombreArchivo.contains(".doc") || strNombreArchivo.contains(".docx"))
			image = new Image("images/x-office-document.png");
		else
			image = new Image("images/application-x-executable.png");
		return image;
	}
	
	public ProgressBar getProgressBar(int indice){
		return this.alBarras.get(indice);
	}
	
	public Label getLabelEstado(int indice){
		return this.alEstado.get(indice);
	}
	
	public int getNumeroElementos(){
		return this.iNumeroItems;
	}
	
	public HBox getFila(int indice){
		return this.alFilas.get(indice);
	}
}
