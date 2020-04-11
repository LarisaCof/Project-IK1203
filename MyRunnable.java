import java.net.*;
import java.io.*;
import tcpclient.TCPClient;

public class MyRunnable implements Runnable {

  Socket connectionsock;

  // data
  String port = null;
  String hostname = null;
  String string = null;
  String request; // data from client

  // HTTP response messages
  String HTTP200 = "HTTP/1.1 200 OK\r\n\r\n";
  String HTTP400 = "HTTP/1.1 400 Bad Request\r\n";
  String HTTP404 = "HTTP/1.1 404 Not Found\r\n";

  public MyRunnable(Socket connectionsock) {
    this.connectionsock = connectionsock;
  }
  public void run() {
    try {
      BufferedReader inStream = new BufferedReader(new InputStreamReader(connectionsock.getInputStream()));
      DataOutputStream outStream = new DataOutputStream(new DataOutputStream(connectionsock.getOutputStream()));

      StringBuilder response =  new StringBuilder(); // response to server
      request = inStream.readLine(); // get client request

      if(request != null) {

        String[] str = request.split("[?&= ]"); // split request

        for(int i = 0; i < str.length; i++){
          if(str[i].equals("hostname"))
            hostname = str[++i];
          else if(str[i].equals("port"))
            port = str[++i];
          else if(str[i].equals("string"))
            string  = str[++i];
        }

        if(hostname != null && port != null && port.matches("[0-9]+") && str[1].equals("/ask")) {
          try {
            String serverResponse = TCPClient.askServer(hostname, Integer.parseInt(port), string); // get server response
            response.append(HTTP200); // add HTTP header response
            response.append(serverResponse);
          } catch(IOException e) {
            response.append(HTTP404); // could not connect to server
          }
        } else {
          response.append(HTTP400);
        }
        outStream.writeBytes(response.toString()); // return response to client
      }
      connectionsock.close(); // close connection
    } catch(IOException e) {
      System.err.println(e); // catch error
    }
  }
}
