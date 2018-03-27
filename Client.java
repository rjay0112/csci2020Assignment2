import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.application.Application;

import java.io.IOException;

public class Client extends Application{
    public static void main(String[] args){
        Application.launch(Client.class,args);
    }
    @Override
    public void start(Stage stage)throws Exception{
      Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
      stage.setScene(new Scene(root,500,500));
      stage.show();
    }
}
