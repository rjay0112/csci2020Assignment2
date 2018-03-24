import java.io.*;
import java.net.*;

public class ServerTest{
  public static void main(String[] args) {
    try{
      ServerSocket serverSocket=new ServerSocket(8080);
      while(true){
        Socket clientSocket=serverSocket.accept();
        System.out.println("hi success");
        BufferedReader input=new BufferedReader(
        new InputStreamReader(clientSocket.getInputStream()));
        //while()
        String message=input.readLine();
        System.out.println(message);
      }
    }catch(Exception e){
      System.err.println("cannot connect");
    }

  }
}
