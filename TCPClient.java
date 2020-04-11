package tcpclient;

import java.net.*;
import java.io.*;

public class TCPClient {

	private static int bufsize = 1024;

	public static String askServer(String hostname, int port, String ToServer) throws IOException {

		if (ToServer == null) {
			return askServer(hostname, port);
		}

		Socket clientSocket = new Socket(hostname, port); // connects the stream socket (TCP) to the specified port
															// number on the host
		clientSocket.setSoTimeout(2000); // set timeout to 2000 ms

		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream()); // returns an output stream
																								// for writing bytes to
																								// this socket
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

		outToServer.writeBytes(ToServer + '\n'); // data sent in byte sequences to the server

		StringBuilder build = new StringBuilder();
		String str;

		try {
			while ((str = inFromServer.readLine()) != "\n" && str != null) {
				build.append(str + '\n');
      }
			clientSocket.close();
			return build.toString();
		} catch (IOException e) {
			clientSocket.close();
			return build.toString();
		}

	}

	public static String askServer(String hostname, int port) throws IOException {
		Socket clientSocket = new Socket(hostname, port);
		clientSocket.setSoTimeout(2000);
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

		StringBuilder build = new StringBuilder();
		String s;

		int counter = 0;
		try {
			while ((s = inFromServer.readLine()) != "\n" && s != null) {
				build.append(s + '\n');
				counter++;
				if (counter >= bufsize)
					return build.toString();
			}
			clientSocket.close();
			return build.toString();
		} catch (IOException e) {
			clientSocket.close();
			return build.toString();
		}
	}

}
