package org.example.design;

public class Facade {
    private final SubSystem subSystem = new SubSystem();

    public void watchMovie(String movie) {
        subSystem.turnOnTV();
        subSystem.setCD(movie);
        subSystem.startWatch();
    }
}

class SubSystem {
    public void turnOnTV() {

    }

    public void setCD(String movie) {

    }

    public void startWatch() {

    }
}
