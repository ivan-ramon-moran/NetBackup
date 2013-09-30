import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class ExtendedTableExpander extends ImageView{
	
	private int id;
	
	public ExtendedTableExpander(int id){
		Image image = new Image("images/expandir.png");
		this.setCursor(Cursor.HAND);
		this.setFitHeight(24);
        this.setFitWidth(24);
        this.setImage(image);
        this.id = id;
	}
	
	public int getID(){
		return this.id;
	}
	
}
