package org.example.design;

import java.util.ArrayList;
import java.util.List;

public interface Component {

    String execute(Integer level);

    void addComponent(Component component);
}

class Leaf implements Component {

    private final String value;

    public Leaf(String value) {
        this.value = value;
    }

    @Override
    public String execute(Integer level) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < level; i++) {
            sb.append("-");
        }
        sb.append(value);
        sb.append("\n");

        return sb.toString();
    }

    @Override
    public void addComponent(Component component) {
        throw new UnsupportedOperationException("叶子节点不支持添加子组件");
    }
}

class Composite implements Component {

    private final List<Component> components;

    public Composite() {
        components = new ArrayList<>();
    }

    @Override
    public String execute(Integer level) {
        StringBuilder sb = new StringBuilder();
        for (Component component : components) {
            sb.append(component.execute(level + 1));
        }
        return sb.toString();
    }

    @Override
    public void addComponent(Component component) {
        components.add(component);
    }
}

