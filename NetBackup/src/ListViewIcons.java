import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;


public class ListViewIcons extends ScrollPane{
	
	private VBox contenedorPrincipal;
	private VBox contenedorItems;
	private ItemDE [] items;
	private final ContextMenu cm = new ContextMenu();
	private NetBackup vista;
	Label labelNumArchivos = null;
	
	public ListViewIcons(NetBackup _vista){
		this.vista = _vista;
		
		contenedorPrincipal = new VBox();
		VBox.setVgrow(contenedorPrincipal, Priority.ALWAYS);
		
		
		contenedorItems = new VBox();
		contenedorItems.setStyle("-fx-padding: 5 5 5 5");
		VBox.setVgrow(contenedorItems, Priority.ALWAYS);
		HBox cabeceraExplorador = new HBox();
		DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(0);
        dropShadow.setOffsetY(0);
        dropShadow.setColor(Color.rgb(0, 0, 0, 0.7));
        dropShadow.setRadius(6);
        cabeceraExplorador.setEffect(dropShadow);
		cabeceraExplorador.setMinHeight(30);
		Label labelTitulo = new Label("EXPLORACIÓN EN EL SERVIDOR");
		labelTitulo.setStyle("-fx-padding: 7; -fx-text-fill: white");
		labelTitulo.setFont(Font.loadFont(getClass().getResourceAsStream("/fuentes/DroidSans.ttf"), 13));
		cabeceraExplorador.getChildren().add(labelTitulo);
		HBox contBusqueda = new HBox(5);
		HBox.setHgrow(contBusqueda, Priority.ALWAYS);
		contBusqueda.setAlignment(Pos.CENTER_RIGHT);
		contBusqueda.setStyle("-fx-padding: 3 10 3 3");
		Label labelBusqueda = new Label("Buscar:");
		labelBusqueda.setStyle("-fx-text-fill: white");
		labelBusqueda.setFont(Font.loadFont(getClass().getResourceAsStream("/fuentes/DroidSans.ttf"), 13));
		TextField tBusqueda = new TextField();
		tBusqueda.setPrefHeight(20);
		tBusqueda.setStyle("-fx-background-radius: 10; -fx-background-color: linear-gradient(to bottom, #d5d5d5, white)");
		tBusqueda.setAlignment(Pos.CENTER);
		List<String> listaFiltros = new ArrayList<String>();
		listaFiltros.add("Todos");
		listaFiltros.add("Documentos");
		listaFiltros.add("Imágenes");
		listaFiltros.add("Audio");
		ComboBox comboFiltro = new ComboBox();
		comboFiltro.setPrefHeight(20);
		comboFiltro.setItems(FXCollections.observableList(listaFiltros));
		comboFiltro.setValue("Todos");
		contBusqueda.getChildren().addAll(comboFiltro, labelBusqueda, tBusqueda);
		cabeceraExplorador.getChildren().add(contBusqueda);
		contenedorPrincipal.getChildren().add(cabeceraExplorador);
		cabeceraExplorador.setId("cabecera-explorador");
		//RUTA
		HBox contRuta  = new HBox();
		Label labelRuta = new Label("Ruta: ");
		//HBox.setHgrow(contenedorPrincipal, Priority.ALWAYS);
		contenedorPrincipal.setId("exploracion-remota");
		HBox pie = new HBox();
		pie.setPrefHeight(30);
		pie.setId("pie-explorador");
		labelNumArchivos = new Label("TOTAL: No hay archivos en el servidor");
		labelNumArchivos.setFont(Font.loadFont(getClass().getResourceAsStream("/fuentes/DroidSans.ttf"), 13));
		pie.getChildren().add(labelNumArchivos);
		labelNumArchivos.setStyle("-fx-text-fill: white; -fx-padding: 7");
		this.setStyle("-fx-background-color: transparent");
		
		
		DropShadow ds = new DropShadow();
        ds.setOffsetY(3.0);
        ds.setOffsetX(3.0);
        ds.setColor(Color.GRAY);
        ds.setRadius(6);
        contenedorPrincipal.getChildren().add(contenedorItems);
        contenedorPrincipal.getChildren().add(pie);

        this.setEffect(ds);
		
		this.setContent(contenedorPrincipal);
		this.setFitToHeight(true);
		this.setFitToWidth(true);
		this.setMinHeight(500);
		this.setMinWidth(700);
		
		cm.setStyle("-fx-background-color: #5A5A5A ;-fx-border-radius: 5; -fx-background-radius: 5; -fx-border-width: 1");
		//Creamos los menus items
		MenuItem cmItemTransferir = new MenuItem("Descargar");
		cmItemTransferir.setStyle("-fx-text-fill: white");
		MenuItem cmItemInformacion = new MenuItem("Información");
		cmItemInformacion.setStyle("-fx-text-fill: white");
		MenuItem cmItemBorrar = new MenuItem("Borrar");		
		cmItemBorrar.setStyle("-fx-text-fill: white");

		cm.getItems().addAll(cmItemTransferir, cmItemInformacion, cmItemBorrar);
	}
	
	
	public void setItems(ItemDE [] _items){
		HBox fila = null;
		this.items = _items;
		contenedorItems.getChildren().clear();
		int i;
		
		for (i = 0; i < items.length; i++){
			final ItemDE fItem = items[i]; 
			
			items[i].setOnMouseClicked(new EventHandler<MouseEvent>(){
				@Override
				public void handle(MouseEvent event) {
					if (event.getClickCount() == 2 || event.getClickCount() == 1){
						ItemDE item = null;
						
						for (int i = 0; i < items.length; i++)
							items[i].setSeleccionado(false);
						
						if (event.getTarget() instanceof ItemDE){
							 item = (ItemDE)event.getTarget();
						}
						else{
							Node node = (Node)event.getTarget();
				
							while (!(node instanceof ItemDE))
								node = node.getParent();
							
							item = ((ItemDE) node);
						}
						item.setSeleccionado(true);
					}
					
					if (event.getClickCount() == 2)
						vista.descargarFichero(fItem);
					else if (event.getClickCount() == 1)
						System.out.println("ssss");
					
					if (event.getButton() == MouseButton.SECONDARY)
						cm.show(fItem, event.getScreenX(), event.getScreenY());

				
				}
				
			});
						
			
			if (i % 6 == 0){
				fila = new HBox(1);
				contenedorItems.getChildren().add(fila);
				fila.getChildren().add(items[i]);
			}else{
				fila.getChildren().add(items[i]);
			}
		}
		
		labelNumArchivos.setText("TOTAL: " + i  + " archivos");
	}
}
