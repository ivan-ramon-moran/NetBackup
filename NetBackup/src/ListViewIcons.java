import java.util.ArrayList;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;


public class ListViewIcons extends ScrollPane{
	
	private ArrayList<String> listaArchivos = new ArrayList<String>();
	private FlowPane flow = null;
	private static ArrayList<HBox> arrayContenedores = new ArrayList<HBox>();
	
	public ListViewIcons(){
		VBox contenedorPrincipal = new VBox();
		//HBox.setHgrow(contenedorPrincipal, Priority.ALWAYS);
		contenedorPrincipal.setId("exploracion-remota");
		this.setStyle("-fx-background-color: transparent");
		this.setMinHeight(500);
		this.setPrefHeight(500);
		
		DropShadow ds = new DropShadow();
        ds.setOffsetY(3.0);
        ds.setOffsetX(3.0);
        ds.setColor(Color.GRAY);
        this.setEffect(ds);
		Label label = new Label("POOO");
		contenedorPrincipal.getChildren().add(label);
		this.setContent(contenedorPrincipal);
		this.setFitToHeight(true);
		this.setFitToWidth(true);
		//flow = new FlowPane();
		//flow.setHgap(10);
		//flow.setVgap(5);
	    
	    //this.getChildren().add(flow);
	}
	
	public void setItems(ArrayList<String> inListaArchivos)
	{
		listaArchivos.clear();
		arrayContenedores.clear();
		this.listaArchivos = inListaArchivos;
		
		for (int i = 0; i < listaArchivos.size(); i++)
		{
			HBox contenedorItem = new HBox();
			arrayContenedores.add(contenedorItem);
			contenedorItem.setOnMouseClicked(new EventHandler<MouseEvent>(){
				 @Override
		          public void handle(MouseEvent me) {
					 System.out.println(arrayContenedores.size());
					 
					 for (int i = 0; i < arrayContenedores.size(); i++){
						 arrayContenedores.get(i).getStyleClass().clear();
						 arrayContenedores.get(i).getStyleClass().add("contenedorItem");
					 }
					 
					 HBox contenedorItem = (HBox)me.getSource();
		             contenedorItem.getStyleClass().add("itemSeleccionado");
		          }
		 
		      });
			
			contenedorItem.setAlignment(Pos.CENTER);
			contenedorItem.setMaxWidth(100);
			contenedorItem.setMinWidth(100);

			contenedorItem.getStyleClass().add("contenedorItem");
			Label labelItem = new Label(listaArchivos.get(i) + "KKKKKKKKKKKKKKKKK" , new ImageView(new Image("images/close.png")));
			labelItem.setWrapText(true);
			labelItem.setOnMouseClicked(new EventHandler<MouseEvent>(){
				 @Override
		          public void handle(MouseEvent me) {
		              Labeled labeled = (Labeled)me.getSource();
		        	  System.out.println("Seleccionado: " + labeled.getText());
		          }
		 
		      });
			labelItem.setContentDisplay(ContentDisplay.TOP);
			contenedorItem.getChildren().add(labelItem);
			flow.getChildren().add(contenedorItem);
		}
		
	}
}
