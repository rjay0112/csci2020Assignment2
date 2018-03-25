import java.io.*;
import java.net.*;
import java.util.*;

public class ServerTest{
  public static String test="Tester";
  private ServerSocket serverSocket=null;
  private static ArrayList<String> serverSideFiles;

  //private SharedState serverArray;

  public static void setFiles(String fileName){
    serverSideFiles.add(fileName);
    System.out.println(fileName);
  }
  public static ArrayList<String> getFileList(){
    return serverSideFiles;
  }

  public ServerTest(int port)throws IOException{
    serverSocket=new ServerSocket(port);
    serverSideFiles=new ArrayList<String>();
  }
  public static void main(String[] args) {
    try{
      ServerTest server=new ServerTest(8080);
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
      System.out.println("hiiii");
      setFiles(current.getName());
    }
  }
  public void handleRequests()throws IOException{
      while(true){
        Socket clientSocket=serverSocket.accept();
        ServerHandler handler=new ServerHandler(clientSocket);
        Thread handlerThread=new Thread(handler);
        handlerThread.start();
      }
  }
}
