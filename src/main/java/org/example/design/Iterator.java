package org.example.design;

public interface Iterator<T> {
    boolean hasNext();

    T next();
}

class ConcreteIterator implements Iterator<String> {
    private final String[] elements;

    private int position = 0;

    public ConcreteIterator(String[] elements) {
        this.elements = elements;
    }

    @Override
    public boolean hasNext() {
        return position < elements.length;
    }

    @Override
    public String next() {
        return elements[position++];
    }
}
