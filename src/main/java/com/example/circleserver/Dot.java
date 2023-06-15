package com.example.circleserver;

import javafx.scene.paint.Color;

public record Dot(double x, double y, double radius, Color color) {
    public static String toMessage(Dot dot){
        return dot.x+" "+dot.y+" "+dot.radius+" "+dot.color.toString();
    }
    public static Dot fromMessage(String message){
        String[] parts = message.split(" ");
        double x = Double.parseDouble(parts[0]);
        double y  = Double.parseDouble(parts[1]);
        double radius = Double.parseDouble(parts[2]);
        Color color = Color.valueOf(parts[3]);
        return new Dot(x,y,radius,color);
    }
}
