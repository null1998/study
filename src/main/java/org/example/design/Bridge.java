package org.example.design;

interface Color {
    String color();
}

public class Bridge {
    public void drawBlueCircle() {
        Blue blue = new Blue();
        new Circle(blue).draw();
    }

    public void drawRedTriangle() {
        Red red = new Red();
        new Triangle(red).draw();
    }
}

abstract class Shape {
    protected Color color;

    public Shape(Color color) {
        this.color = color;
    }

    abstract void draw();
}

class Circle extends Shape {

    public Circle(Color color) {
        super(color);
    }

    @Override
    void draw() {
        System.out.println(color.color() + " circle");
    }
}

class Rectangle extends Shape {

    public Rectangle(Color color) {
        super(color);
    }

    @Override
    void draw() {
        System.out.println(color.color() + " rectangle");
    }
}

class Triangle extends Shape {

    public Triangle(Color color) {
        super(color);
    }

    @Override
    void draw() {
        System.out.println(color.color() + " triangle");
    }
}

class Red implements Color {

    @Override
    public String color() {
        return "red";
    }
}

class Green implements Color {

    @Override
    public String color() {
        return "green";
    }
}

class Blue implements Color {

    @Override
    public String color() {
        return "blue";
    }
}
