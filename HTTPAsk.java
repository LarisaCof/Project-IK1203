import java.net.*;
import java.io.*;
import tcpclient.TCPClient;

public class HTTPAsk {
    public static void main( String[] args) throws IOException {

      String HTTP200 = "HTTP/1.1 200 OK\r\n\r\n";
      String HTTP400 = "HTTP/1.1 400 Bad Request\r\n";
      String HTTP404 = "HTTP/1.1 404 Not Found\r\n";

      int HTTPPort;
      if(args.length < 1)
        HTTPPort = 8888;
      else
        HTTPPort = Integer.parseInt(args[0]);

      ServerSocket socket = new ServerSocket(HTTPPort);

      String host;
      String port;
      String string;
      String request;

      while(true) {
            try {
                Socket consock = socket.accept();
                BufferedReader inStream = new BufferedReader(new InputStreamReader(consock.getInputStream()));
                DataOutputStream outStream = new DataOutputStream(consock.getOutputStream());

                StringBuilder response = new StringBuilder();

                request = inStream.readLine();

                if(request != null) {
                    host = null;
                    port = null;
                    string = null;

                    String[] parts = request.split("[?&= ]");
                    for(int i = 0; i < parts.length; i++) {
                        if(parts[i].equals("hostname"))
                            host = parts[++i];
                        else if(parts[i].equals("port"))
                            port = parts[++i];
                        else if(parts[i].equals("string"))
                            string = parts[++i];
                    }

                    if(parts[1].equals("/ask") && host != null && port != null && port.matches("[0-9]+")) {
                        try {
                            String tempResponse = TCPClient.askServer(host, Integer.parseInt(port), string);
                            response.append(HTTP200);
                            response.append(tempResponse);
                            System.out.println(tempResponse);
                        } catch(IOException e) {
                            response.append(HTTP404);
                        }
                    } else {
                        response.append(HTTP400);
                    }
                    outStream.writeBytes(response.toString());
                }
                consock.close();
            } catch(IOException e) {
                System.err.println(e);
            }
        }
    }
}
