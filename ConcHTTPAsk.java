import java.net.*;
import java.io.*;

public class ConcHTTPAsk {

  public static void main( String[] args) throws IOException {
    int HTTPPort;

    if(args.length > 0) {
      HTTPPort = Integer.parseInt(args[0]);
    } else {
      HTTPPort = 8888;
    }

    try {
      ServerSocket HTTPSocket = new ServerSocket(HTTPPort);
      while(true) {
        (new Thread(new MyRunnable(HTTPSocket.accept()))).start();
      }
    } catch(IOException e) {
      System.err.println(e);
    }
  }
}
