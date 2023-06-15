package com.example.circleserver;

import com.example.circleserver.client.ServerThread;
import com.example.circleserver.server.Server;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;



public class HelloController {
    public GraphicsContext graphicsContext;
    @FXML
    public Slider radiusSlider;
    @FXML
    public ColorPicker colorPicker;
    @FXML
    public TextField portField;
    @FXML
    public TextField addressField;
    @FXML
    public Canvas canvas;
    private ServerThread serverThread;
    private Server server;


    public void initialize(){
        graphicsContext = canvas.getGraphicsContext2D();
    }
    @FXML
    public void onStartServerClicked() {
        String host = addressField.getText().isEmpty() ? "localhost" : addressField.getText();
        int port = portField.getText().isEmpty() ? 5000 : Integer.parseInt(portField.getText());
        server = new Server(port);
        server.listen();
        serverThread = new ServerThread(host, port);
        serverThread.setDots(dot -> {
            graphicsContext.setFill(dot.color());
            graphicsContext.fillOval(dot.x(), dot.y(), dot.radius(), dot.radius());
        });
        serverThread.start();
        System.out.println("Server started, on port: " + port);
    }

    public void onConnectClicked() {
        String host = addressField.getText().isEmpty() ? "localhost" : addressField.getText();
        int port = portField.getText().isEmpty() ? 5000 : Integer.parseInt(portField.getText());
        serverThread = new ServerThread(host, port);
        serverThread.setDots(dot -> {
            graphicsContext.setFill(dot.color());
            graphicsContext.fillOval(dot.x(), dot.y(), dot.radius(), dot.radius());
        });
        serverThread.start();
        System.out.println("You have been connected to server on port: " + port);
    }

    public void onMouseClicked(MouseEvent mouseEvent) {
        double x= mouseEvent.getX();
        double y = mouseEvent.getY();
        double radius = radiusSlider.getValue();
        Color fillColor = colorPicker.getValue();
        server.saveDot(new Dot(x,y,radius,fillColor));
        serverThread.send(x-radius/2,y-radius/2,radius,fillColor);
    }
}