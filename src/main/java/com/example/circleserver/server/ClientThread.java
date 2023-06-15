package com.example.circleserver.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread extends Thread{
    private Socket clientSocket;
    private PrintWriter writer;
    private Server server;
    public ClientThread(Server server, Socket socket){
        this.clientSocket = socket;
        this.server= server;
    }

    public void run(){
        try {
            BufferedReader inputStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            writer = new PrintWriter(clientSocket.getOutputStream(),true);
            String message;
            while((message = inputStream.readLine()) != null){
                server.broadcast(message);
                //System.out.println("Received message from client: "+ message);
            }
            inputStream.close();
            writer.close();
            clientSocket.close();
            System.out.println("Closed\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void send(String message){
        writer.println(message);
    }
}
