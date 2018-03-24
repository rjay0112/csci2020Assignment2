import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import java.io.*;
import java.net.*;
import javafx.scene.control.ListCell;

public class Controller {
    @FXML Button download;
    @FXML Button upload;
    @FXML ListView<File> localFiles;
    @FXML ListView<File> serverFiles;
    Socket userconn;
    File directName;
    public void initialize(){
      DirectoryChooser chooseDir=new DirectoryChooser();
      chooseDir.setTitle("select folder you want to share");
      directName=chooseDir.showDialog(new Stage());
      filluserfiles();
    }

    public void filluserfiles(){
      ObservableList<File> userFileList=FXCollections.observableArrayList();
      File[] initialFiles=directName.listFiles();
      for(File current:initialFiles){
        if(!current.isDirectory()){
          userFileList.add(current);
          System.out.println(current.getName());
        }
      }
      localFiles.setItems(userFileList);
      localFiles.setCellFactory(lv -> new ListCell<File>(){
        @Override
        protected void updateItem(File file, boolean empty){
          super.updateItem(file,empty);
          setText(file==null ? null : file.getName());
        }
      });
    }

    public void download(ActionEvent event){
        System.out.println("download");
    }
    public void upload(ActionEvent event){
      try{
        userconn=new Socket("localhost",8080);
        PrintWriter out=new PrintWriter(userconn.getOutputStream(),true);
        BufferedReader in=new BufferedReader(new InputStreamReader(
                          userconn.getInputStream()));
        out.println("upload");
        out.flush();
        System.out.println(in.readLine());
        ObservableList<File> serverFileList=FXCollections.observableArrayList();
        serverFileList.add(localFiles.getSelectionModel().getSelectedItem());
        serverFiles.setItems(serverFileList);
      }catch(Exception e){
        System.err.println("cant connect");
      }
    }
}
