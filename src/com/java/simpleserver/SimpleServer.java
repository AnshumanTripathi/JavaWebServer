package com.java.simpleserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleServer {
	public static void main(String[] args) {
		try (ServerSocket serverSocket = new ServerSocket(8080)) {
			System.out.println("Listening on port 8080");
			try (Socket client = serverSocket.accept()) {
				InputStreamReader clientReader = new InputStreamReader(client.getInputStream());
				BufferedReader buffer = new BufferedReader(clientReader);
				String line = buffer.readLine();
				while (!line.isEmpty()) {
					System.out.println(line);
					line = buffer.readLine();
				}
				String response = "HTTP/1.1 200 OK\r\n\r\nThis is your response";
				client.getOutputStream().write(response.getBytes("UTF-8"));
			}
		} catch (IOException e) {
			System.err.println("Error Occured while connecting to Server...\n" + e.getMessage());
		}
	}
}
