package com.example.circleserver.client;

import com.example.circleserver.Dot;
import javafx.scene.paint.Color;

import java.io.*;
import java.net.Socket;
import java.util.function.Consumer;

public class ServerThread extends Thread{
    private Socket serverSocket;
    private PrintWriter writer;
    private BufferedReader reader;
    public Consumer<Dot> dots;

    public void setDots(Consumer<Dot> dots) {
        this.dots = dots;
    }

    public ServerThread(String address, int port){
        try {
            serverSocket = new Socket(address,port);
            OutputStream output = serverSocket.getOutputStream();
            writer = new PrintWriter(output,true);
            InputStream input = serverSocket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void run(){
        try{
            String message;
            while((message = reader.readLine())!=null){
                dots.accept(Dot.fromMessage(message));
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void send(double x, double y, double radius, Color color){
        writer.println(x + " " + y + " " + radius+" "+color);
    }
}
