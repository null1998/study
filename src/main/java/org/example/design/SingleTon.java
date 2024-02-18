package org.example.design;

public class SingleTon {
    private static volatile SingleTon singleTon;
    private SingleTon() {

    }
    public static SingleTon getInstance() {
        if (singleTon == null) {
            synchronized (SingleTon.class) {
                if (singleTon == null) {
                    singleTon = new SingleTon();
                }
            }
        }
        return singleTon;
    }
}
