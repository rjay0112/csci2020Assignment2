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
      System.out.println(request);
    }catch(Exception e){
      System.err.println("cant print request");
    }

  }
  public void handleRequest(String request)throws IOException{
    StringTokenizer tokenizer=new StringTokenizer(request);
    String command=tokenizer.nextToken();

    if(command.equalsIgnoreCase("UPLOAD")){
      String fileName=tokenizer.nextToken();
      System.out.println(fileName);
      int size=Integer.parseInt(tokenizer.nextToken());
      System.out.println(size);
      recieveFile(fileName,size);
    }else if(command.equalsIgnoreCase("DOWNLOAD")){
      String fileName=tokenizer.nextToken();
      sendFile(fileName);
      responseOutput.println("download to be added");
    }else if(command.equalsIgnoreCase("DIR")){
      for(int i=0;i<ServerTest.getFileList().size();i++){
        String name=ServerTest.getFileList().get(i);
        responseOutput.println(name);
      }
      responseOutput.close();
    }
  }

  public void recieveFile(String fileName, int size)throws IOException{
    String file="";
    String fileLine=null;
    for (int i=0;i<size;i++){
      file+=requestInput.readLine()+"\n";
    }
    ServerTest.setFiles(fileName);
    File addedFile=new File("Server Files/"+fileName);
    PrintWriter outPutFile = new PrintWriter(addedFile);
    outPutFile.print(file);
    outPutFile.close();
    System.out.println("file "+fileName+"stuff in it \n"+file);
  }

  public void sendFile(String fileName)throws IOException{
    File fileToSend=new File("Server Files/"+fileName);
    BufferedReader fileIn=new BufferedReader(new FileReader(fileToSend));
    String line=null;
    while((line=fileIn.readLine())!=null){
      System.out.println(line);
    }
  }


}
