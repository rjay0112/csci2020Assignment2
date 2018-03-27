import java.io.*;
import java.net.*;
import java.util.*;

public class ServerHandler implements Runnable{
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
      String request=requestInput.readLine();
      handleRequest(request);
    }catch(Exception e){
      System.err.println("cant print request");
    }

  }
  public void handleRequest(String request)throws IOException{
    StringTokenizer tokenizer=new StringTokenizer(request);
    String command=tokenizer.nextToken();

    if(command.equalsIgnoreCase("UPLOAD")){
      String fileName=tokenizer.nextToken();
      recieveFile(fileName);
    }else if(command.equalsIgnoreCase("DOWNLOAD")){
      String fileName=tokenizer.nextToken();
      sendFile(fileName);
      responseOutput.println("download to be added");
    }else if(command.equalsIgnoreCase("DIR")){
      for(int i=0;i<Server.getFileList().size();i++){
        String name=Server.getFileList().get(i);
        responseOutput.println(name);
      }
      responseOutput.close();
    }
  }

  public void recieveFile(String fileName)throws IOException{
    String file="";
    String fileLine=null;
    while((fileLine=requestInput.readLine())!=null){
      file+=fileLine+"\n";
    }
    Server.setFiles(fileName);
    File addedFile=new File("Server Files/"+fileName);
    PrintWriter outPutFile = new PrintWriter(addedFile);
    outPutFile.print(file);
    outPutFile.close();
  }

  public void sendFile(String fileName)throws IOException{
    File fileToSend=new File("Server Files/"+fileName);
    BufferedReader fileIn=new BufferedReader(new FileReader(fileToSend));
    String line=null;
    while((line=fileIn.readLine())!=null){
      responseOutput.println(line);
    }
    responseOutput.close();
  }


}
