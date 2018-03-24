import java.io.*;
import java.net.*;
public class ClientTest{
  public static void main(String[] args) {
    try{
      Socket socket=new Socket("localhost",8080);
      PrintWriter out=new PrintWriter(socket.getOutputStream(),true);
      out.println("HIIIIIII");
      out.flush();
    }catch(Exception e){
      System.err.println("cant connect");
    }

  }
}
