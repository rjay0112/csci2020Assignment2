import java.io.*;
import java.net.*;

public class ServerTest{
  private ServerSocket serverSocket=null;
  public ServerTest(int port)throws IOException{
    serverSocket=new ServerSocket(port);
  }
  public static void main(String[] args) {
    try{
      ServerTest server=new ServerTest(8080);
      server.handleRequests();
    }catch(Exception e){
      System.err.println("cannot connect");
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
