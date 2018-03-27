import java.io.*;
import java.net.*;
import java.util.*;

public class Server{
  public static String test="Tester";
  private ServerSocket serverSocket=null;
  private static volatile ArrayList<String> serverSideFiles;

  //private SharedState serverArray;

  public static synchronized void setFiles(String fileName){
    serverSideFiles.add(fileName);
  }
  public static ArrayList<String> getFileList(){
    return serverSideFiles;
  }

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
  public void handleRequests()throws IOException{
      while(true){
        Socket clientSocket=serverSocket.accept();
        ClientConnectionHandler handler=new ClientConnectionHandler(clientSocket);
        Thread handlerThread=new Thread(handler);
        handlerThread.start();
      }
  }
}
