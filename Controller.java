import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
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
    @FXML Button refreshButton;
    @FXML TextField warningArea;
    @FXML ListView<File> localFiles;
    ObservableList<File> userFileList;
    @FXML ListView<File> serverFiles;
    ObservableList<File> serverFileList;
    BufferedReader recieve;
    PrintWriter sendCommand;
    Socket userconn;
    File directName;
    boolean repeated;
    String clientSavePath;

    public void initialize()throws IOException{
      DirectoryChooser chooseDir=new DirectoryChooser();
      chooseDir.setTitle("select folder you want to share");
      directName=chooseDir.showDialog(new Stage());
      clientSavePath=directName.getAbsolutePath();
      warningArea.setEditable(false);
      fillUserFiles();
      fillServerFiles();
    }

    public void refresh(ActionEvent event)throws IOException{
      warningArea.setText(" ");
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
        if(serverFiles.getSelectionModel().getSelectedItem()!=null){
          repeated=false;
          warningArea.setText(" ");
          String requestedFileName=serverFiles.getSelectionModel().getSelectedItem().getName();
          File requestedFile=new File(clientSavePath+"/"+requestedFileName);
          ObservableList<File>runThrough=localFiles.getItems();
          for (File currentFile:runThrough){
            if(currentFile.getName().equalsIgnoreCase(requestedFileName)){
              repeated=true;
            }
          }
          if(!repeated){
            userconn=new Socket("localhost",8080);
            sendCommand=new PrintWriter(userconn.getOutputStream(),true);
            sendCommand.println("DOWNLOAD "+requestedFileName);
            PrintWriter outputFile=new PrintWriter(requestedFile);
            recieve=new BufferedReader(new InputStreamReader(userconn.getInputStream()));
            String line=null;
            while((line=recieve.readLine())!=null){
              outputFile.println(line);
            }
            recieve.close();
            localFiles.getItems().add(requestedFile);
            outputFile.close();
          }else{
            warningArea.setText("ERROR: File "+requestedFileName+" already exists locally");
          }
        }else{
          warningArea.setText("ERROR: Please select a file to download");
        }

    }
    public void upload(ActionEvent event)throws IOException{
      //connect to server
      if(localFiles.getSelectionModel().getSelectedItem()!=null){
        repeated=false;
        warningArea.setText(" ");
        //add selected file to server
        File selectedFile=localFiles.getSelectionModel().getSelectedItem();
        String selectedFileName=selectedFile.getName();

        ObservableList<File>runThrough=serverFiles.getItems();
        for (File currentFile:runThrough){
          if(currentFile.getName().equalsIgnoreCase(selectedFileName)){
            repeated=true;
          }
        }
        if(!repeated){
          userconn=new Socket("localhost",8080);
          sendCommand=new PrintWriter(userconn.getOutputStream(),true);
          BufferedReader fileIn=new BufferedReader(new FileReader(selectedFile));
          String line=null;
          String contents="";
          while((line=fileIn.readLine())!=null){
            contents+=line+"\n";
          }
          fileIn.close();
          serverFiles.getItems().add(selectedFile);
          sendCommand.println("UPLOAD "+selectedFile.getName()
                      +"\n"+contents);
          sendCommand.close();
          userconn.close();
        }else{
          warningArea.setText("ERROR: File "+selectedFileName+" already exists on server");
        }

      }else{
        warningArea.setText("ERROR: Please select a file to upload");
      }

    }
}
