package org.example.design;

public class Flyweight {
    private final byte[] payload = new byte[1024 * 1024];

    private Flyweight() {

    }

    public static Flyweight getInstance() {
        return Inner.INSTANCE;
    }

    private static class Inner {
        private static final Flyweight INSTANCE = new Flyweight();
    }
}

class Lightweight {
    private Integer id;
    private Flyweight flyweight;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Flyweight getFlyweight() {
        return flyweight;
    }

    public void setFlyweight(Flyweight flyweight) {
        this.flyweight = flyweight;
    }
}
