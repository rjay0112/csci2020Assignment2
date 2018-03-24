import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import java.io.*;
import java.net.*;

public class Controller {
    @FXML Button download;
    @FXML Button upload;
    Socket userconn;
    public void download(ActionEvent event){
        System.out.println("download");
    }
    public void upload(ActionEvent event){
      try{
        Socket socket=new Socket("localhost",8080);
        PrintWriter out=new PrintWriter(socket.getOutputStream(),true);
        out.println("upload");
        out.flush();
      }catch(Exception e){
        System.err.println("cant connect");
      }
    }
}
