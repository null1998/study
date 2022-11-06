package org.example.design;

public class Snapshot {
    private Originator originator;
    private String id;
    private String name;

    public Snapshot(Originator originator, String id, String name) {
        this.originator = originator;
        this.id = id;
        this.name = name;
    }

    public Originator restore() {
        originator.setId(id);
        originator.setName(name);
        return originator;
    }

}

class Originator {
    private String id;
    private String name;

    public Snapshot createSnapshot() {
        return new Snapshot(this, id, name);

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Originator{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}

class Caretaker {
    private Snapshot snapshot;

    private Originator originator;

    public Caretaker(Originator originator) {
        this.originator = originator;
    }

    public void makeBackup() {
        snapshot = originator.createSnapshot();
    }

    public void undo() {
        snapshot.restore();
    }
}
