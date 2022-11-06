package org.example.design;

public abstract class Handler {
    protected Handler nextHandler;

    public Handler(Handler nextHandler) {
        this.nextHandler = nextHandler;
    }

    abstract void handle(Request request);
}

class ConcreteHandlerA extends Handler {

    public ConcreteHandlerA(Handler nextHandler) {
        super(nextHandler);
    }

    @Override
    void handle(Request request) {
        if ("A".equals(request.getType())) {
            System.out.println("ConcreteHandlerA " + request.getContent());
        }
        if (nextHandler != null) {
            nextHandler.handle(request);
        }
    }
}

class ConcreteHandlerB extends Handler {

    public ConcreteHandlerB(Handler nextHandler) {
        super(nextHandler);
    }

    @Override
    void handle(Request request) {
        if ("B".equals(request.getType())) {
            System.out.println("ConcreteHandlerB " + request.getContent());
        }
        if (nextHandler != null) {
            nextHandler.handle(request);
        }
    }
}

class Request {
    private String type;
    private String content;

    public Request(String type, String content) {
        this.type = type;
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
