package com.example.circleserver.server;

import com.example.circleserver.Dot;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class Server {
    private ServerSocket serverSocket;
    private ArrayList<ClientThread> clients = new ArrayList<>();
    private Connection databaseConnection;
    int port;
    public Server(int port){
        try {
            this.port = port;
            this.serverSocket = new ServerSocket(port);
            Class.forName("org.sqlite.JDBC");
            this.databaseConnection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\oleks\\IdeaProjects\\CircleServer\\Dot");
            System.out.println("Connected to DB");
        } catch (IOException | SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void listen(){
        Thread listenThread = new Thread(()->{
            while(true){
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println(clientSocket + " joined!");
                    ClientThread clientThread = new ClientThread(this, clientSocket);
                    clients.add(clientThread);
                    clientThread.start();
                    ArrayList<Dot> dots = getSavedDots();
                    for(var dot : dots){
                        clientThread.send(dot.x()+" "+ dot.y()+" "+dot.radius()+" "+dot.color().toString());
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
        }}) ;
        listenThread.start();
    }
    public void saveDot(Dot dot){
        try {
            PreparedStatement statement = databaseConnection.prepareStatement("INSERT INTO dot (x,y,radius,color) VALUES (?,?,?,?)");
            statement.setDouble(1,dot.x());
            statement.setDouble(2,dot.y());
            statement.setDouble(3,dot.radius());
            statement.setString(4,dot.color().toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public ArrayList<Dot> getSavedDots(){
        try {
            PreparedStatement statement = databaseConnection.prepareStatement("SELECT * FROM dot ");
            ResultSet resultSet = statement.executeQuery();
            ArrayList<Dot> dots = new ArrayList<>();
            while(resultSet.next()){
                dots.add(new Dot(resultSet.getDouble("x"), resultSet.getDouble("y"), resultSet.getDouble("radius"), Color.valueOf(resultSet.getString("color"))));
            }
            return dots;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void broadcast(String message){
       for(var client : clients){
            client.send(message);
       }
    }
    public void removeClient(ClientThread client){
        clients.remove(client);
    }

}
