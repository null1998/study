package org.example.design;

public class CarDirector {
    public Car constructCar(CarBuilder carBuilder) {
        carBuilder.buildWindows();
        carBuilder.buildSeats();
        carBuilder.buildEngine();
        carBuilder.buildTripComputer();
        carBuilder.buildGPS();
        return carBuilder.getCar();
    }
}

abstract class CarBuilder {
    protected Car car;

    public CarBuilder() {
        this.reset();
        car = new Car();
    }

    abstract void buildWindows();

    abstract void buildSeats();

    abstract void buildEngine();

    abstract void buildTripComputer();

    abstract void buildGPS();

    public void reset() {
        this.car = new Car();
    }

    public Car getCar() {
        return this.car;
    }
}

class SportCarBuilder extends CarBuilder {

    @Override
    void buildWindows() {
        car.setWindows(4);
    }

    @Override
    void buildSeats() {
        car.setSeats(4);
    }

    @Override
    void buildEngine() {
        car.setEngine("sport engine");
    }

    @Override
    void buildTripComputer() {
        car.setTripComputer("sport trip computer");
    }

    @Override
    void buildGPS() {
        car.setGps(true);
    }
}

class SUVCarBuilder extends CarBuilder {

    @Override
    void buildWindows() {
        car.setWindows(4);
    }

    @Override
    void buildSeats() {
        car.setSeats(4);
    }

    @Override
    void buildEngine() {
        car.setEngine("suv engine");
    }

    @Override
    void buildTripComputer() {
        car.setTripComputer("suv trip computer");
    }

    @Override
    void buildGPS() {
        car.setGps(true);
    }
}

class Car {
    private Integer windows;
    private Integer seats;
    private String engine;
    private String tripComputer;
    private Boolean gps;

    public Integer getWindows() {
        return windows;
    }

    public void setWindows(Integer windows) {
        this.windows = windows;
    }

    public Integer getSeats() {
        return seats;
    }

    public void setSeats(Integer seats) {
        this.seats = seats;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public String getTripComputer() {
        return tripComputer;
    }

    public void setTripComputer(String tripComputer) {
        this.tripComputer = tripComputer;
    }

    public Boolean getGps() {
        return gps;
    }

    public void setGps(Boolean gps) {
        this.gps = gps;
    }

    @Override
    public String toString() {
        return "Car{" +
                "windows=" + windows +
                ", seats=" + seats +
                ", engine='" + engine + '\'' +
                ", tripComputer='" + tripComputer + '\'' +
                ", gps=" + gps +
                '}';
    }
}
