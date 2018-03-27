import java.io.*;
import java.net.*;
import java.util.*;

public class Server{
  private ServerSocket serverSocket=null;
  private static volatile ArrayList<String> serverSideFiles;

  //add name of file added to the server list
  public static synchronized void setFiles(String fileName){
    serverSideFiles.add(fileName);
  }

  public static ArrayList<String> getFileList(){
    return serverSideFiles;
  }

  //initialize server socket and list
  public Server(int port)throws IOException{
    serverSocket=new ServerSocket(port);
    serverSideFiles=new ArrayList<String>();
  }

  public static void main(String[] args) {
    try{
      Server server=new Server(8080);
      server.initialFileSetup();
      server.handleRequests();
    }catch(Exception e){
      System.err.println("cannot connect");
    }
  }

  //sets up server file directory and any files already stored
  //useful in case server closes, all files are not lost
  public void initialFileSetup()throws IOException{
    File serverFiles=new File("Server Files");
    if(!serverFiles.exists()){
      serverFiles.mkdir();
    }
    File[] initialFiles=serverFiles.listFiles();
    for(File current:initialFiles){
      setFiles(current.getName());
    }
  }

  //accept all incoming socket connections and handle their
  //requests in new threads
  public void handleRequests()throws IOException{
      while(true){
        Socket clientSocket=serverSocket.accept();
        ClientConnectionHandler handler=new ClientConnectionHandler(clientSocket);
        Thread handlerThread=new Thread(handler);
        handlerThread.start();
      }
  }
}
