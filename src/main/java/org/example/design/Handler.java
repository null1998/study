package org.example.design;

public abstract class Handler {
    protected Handler nextHandler;

    public Handler() {
    }

    public Handler(Handler nextHandler) {
        this.nextHandler = nextHandler;
    }

    public Handler next(Handler nextHandler) {
        this.nextHandler = nextHandler;
        return this;
    }

    abstract void handle(Request request);

    public static class HandlerBuilder {
        private Handler head;
        private Handler tail;
        public HandlerBuilder addHandler(Handler handler) {
            if (head == null) {
                 head = tail = handler;
                 return this;
            }
            tail.next(handler);
            tail = handler;
            return this;
        }

        public Handler build() {
            return head;
        }
    }
}

class ConcreteHandlerA extends Handler {

    public ConcreteHandlerA() {
    }

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

    public ConcreteHandlerB() {
    }

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
