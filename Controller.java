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
    ObservableList<File> userFileList;
    @FXML ListView<File> serverFiles;
    ObservableList<File> serverFileList;
    BufferedReader recieve;
    PrintWriter sendCommand;
    Socket userconn;
    File directName;
    public void initialize()throws IOException{
      DirectoryChooser chooseDir=new DirectoryChooser();
      chooseDir.setTitle("select folder you want to share");
      directName=chooseDir.showDialog(new Stage());
      fillUserFiles();
      fillServerFiles();
    }

    public void fillServerFiles()throws IOException{
      serverFileList=FXCollections.observableArrayList();
      userconn=new Socket("localhost",8080);
      sendCommand=new PrintWriter(userconn.getOutputStream(),true);
      sendCommand.println("DIR");
      recieve=new BufferedReader(new InputStreamReader(userconn.getInputStream()));
      String file="";
      String line=null;
      //recieve already stored files
      while((line=recieve.readLine())!=null){
        serverFileList.add(new File(line));
      }
      serverFiles.setItems(serverFileList);
      recieve.close();
      userconn.close();
      //have server list show file name not whole directory
      serverFiles.setCellFactory(lv -> new ListCell<File>(){
        @Override
        protected void updateItem(File file, boolean empty){
          super.updateItem(file,empty);
          setText(file==null ? null : file.getName());
        }
      });
    }

    public void fillUserFiles(){
      userFileList=FXCollections.observableArrayList();
      File[] initialFiles=directName.listFiles();
      for(File current:initialFiles){
        if(!current.isDirectory()){
          userFileList.add(current);
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

    public void download(ActionEvent event)throws IOException{
      userconn=new Socket("localhost",8080);
        sendCommand=new PrintWriter(userconn.getOutputStream(),true);
        File requestedFile=serverFiles.getSelectionModel().getSelectedItem();
        sendCommand.println("DOWNLOAD "+requestedFile.getName());
        PrintWriter outputFile=new PrintWriter(requestedFile);
        recieve=new BufferedReader(new InputStreamReader(userconn.getInputStream()));
        String line=null;
        while((line=recieve.readLine())!=null){
          outputFile.println(line);
        }
        recieve.close();
        outputFile.close();
        localFiles.getItems().add(requestedFile);

    }
    public void upload(ActionEvent event){
      try{
        //connect to server
        userconn=new Socket("localhost",8080);
        //add selected file to server
        File selectedFile=localFiles.getSelectionModel().getSelectedItem();
        serverFiles.getItems().add(selectedFile);
        sendCommand=new PrintWriter(userconn.getOutputStream(),true);
        BufferedReader fileIn=new BufferedReader(new FileReader(selectedFile));
        String line=null;
        String contents="";
        while((line=fileIn.readLine())!=null){
          contents+=line+"\n";
        }
        fileIn.close();

        sendCommand.println("UPLOAD "+selectedFile.getName()
                    +"\n"+contents);
        sendCommand.close();
        userconn.close();
      }catch(Exception e){
        System.err.println("cant connect");
      }
    }
}
