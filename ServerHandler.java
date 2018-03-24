import java.io.*;
import java.net.*;

public class ServerHandler implements Runnable{
  private String items;
  private Socket socket;
  private BufferedReader requestInput=null;
  private PrintWriter responseOutput=null;
  public ServerHandler(Socket socket){
    this.socket=socket;
    try{
      requestInput=new BufferedReader(new InputStreamReader(
      socket.getInputStream()));
      responseOutput=new PrintWriter(socket.getOutputStream(),true);
    }catch(Exception e){
      System.err.println("cant get inputStream");
    }

  }

  public void run(){
    try{
      items=requestInput.readLine();
      System.out.println(items);
      responseOutput.println(items+"SENT BACK");
      responseOutput.flush();
    }catch(Exception e){
      System.err.println("cant print request");
    }

  }

}
