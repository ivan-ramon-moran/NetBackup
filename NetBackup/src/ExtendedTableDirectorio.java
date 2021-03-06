import java.util.ArrayList;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


public class ExtendedTableDirectorio {
	
	private int id;
	private ArrayList<Transferencia> archivos = null;
	private ArrayList<HBox> subFilas = new ArrayList<HBox>();
	private boolean bExpandido = false;
	private ArrayList<ProgressBar> barras = new ArrayList<ProgressBar>(); 
	private ArrayList<Label> estados = new ArrayList<Label>(); 
	private ArrayList<HBox> separadores = new ArrayList<HBox>();
	private VBox contenedorPadre = null; 
	
	public ExtendedTableDirectorio(ArrayList<Transferencia> archivos, int id, VBox contenedorPadre, ArrayList<ProgressBar> alBarras, ArrayList<Label> alEstado, ArrayList<HBox> alFilas){
		this.id = id;
		this.archivos = archivos;
		this.contenedorPadre = contenedorPadre;
		
		for (int i = 0; i < archivos.size(); i++){
			HBox subFila = new HBox();
			alFilas.add(subFila);
			subFilas.add(subFila);
			contenedorPadre.getChildren().add(subFila);
			HBox separador = new HBox();
			separador.setId("transferencias-separador");
			separadores.add(separador);
			contenedorPadre.getChildren().add(separador);
			ProgressBar barra = new ProgressBar();
			barra.setPrefSize(145, 15);
			barra.setProgress(0.0);	
			barras.add(barra);
			alBarras.add(barra);
			Label labelEstado = new Label(archivos.get(i).getEstadoTransferencia());
			alEstado.add(labelEstado);
			estados.add(labelEstado);
		}
	}
	
	public void expandir(){
		if (!bExpandido){
			for (int i = 0; i < subFilas.size(); i++){
				HBox filaActual = subFilas.get(i);
				
				if (i % 2 == 0)
					filaActual.getStyleClass().add("extended-table-fila-par");
				else
					filaActual.getStyleClass().add("extended-table-fila-impar");

				
				Transferencia transferencia = archivos.get(i);
				filaActual.setPrefHeight(27);
				filaActual.setStyle("-fx-padding: 0 0 0 60");
				//Contenedor y nombre
				HBox contNombre = new HBox();
				Label labelNombre = new Label(transferencia.getNombreArchivo());
				contNombre.setPrefWidth(346);
				contNombre.getChildren().add(labelNombre);
				//Contenedor y estado
				HBox contEstado = new HBox();
				contEstado.setPrefWidth(95);
				contEstado.getChildren().add(estados.get(i));
				//Contador y barra de progreso
				HBox contBarra = new HBox();
				contBarra.setPadding(new Insets(6,0,0,5));
				contBarra.setPrefWidth(153);
				contBarra.getChildren().add(barras.get(i));
				//Contenedor tama�o
				HBox contTamanyo = new HBox();
				Label labelTamanyo = new Label(transferencia.getTamanyoArchivo());
				labelTamanyo.getStyleClass().add("extended-table-celda");
				contTamanyo.getChildren().add(labelTamanyo);
				contTamanyo.setPrefWidth(100);
				//Miniatura
				ImageView ivMiniatura = new ImageView(ExtendedTable.obtenerImagen(transferencia.getNombreArchivo()));
				HBox contMiniatura = new HBox();
				contMiniatura.setPrefWidth(100);
				contMiniatura.getChildren().add(ivMiniatura);
				
				filaActual.getChildren().addAll(contNombre, contEstado, contBarra, contTamanyo, contMiniatura);
				separadores.get(i).setPrefHeight(1);
			}
		}
		else
			for (int i = 0; i < subFilas.size(); i++){
				HBox subFila = subFilas.get(i);
				subFila.getChildren().clear();
				subFila.setPrefHeight(0);
				separadores.get(i).setPrefHeight(0);
			}
		
	}
	public void setExpandido(boolean bExp){
		this.bExpandido = bExp;
	}
	
	public boolean getExpandido(){
		return this.bExpandido;
	}
	
}
