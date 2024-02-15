package org.example.design;

public class ComputerBuilder {
    private Computer computer;
    public ComputerBuilder() {
        computer = new Computer();
    }
    public ComputerBuilder buildMouse(String mouse) {
        computer.setMouse(mouse);
        return this;
    }

    public ComputerBuilder buildDisplay(String display) {
        computer.setDisplay(display);
        return this;
    }
    public Computer build() {
        return computer;
    }
}
