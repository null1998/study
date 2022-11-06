package org.example.design;

public interface Notice {
    void send(String message);
}

class DefaultNoticeDecorator implements Notice {

    protected Notice notice;

    public DefaultNoticeDecorator(Notice notice) {
        this.notice = notice;
    }

    @Override
    public void send(String message) {
        // 默认通知器不支持发送消息
    }
}

class SmsNoticeDecorator extends DefaultNoticeDecorator {

    public SmsNoticeDecorator(Notice notice) {
        super(notice);
    }

    @Override
    public void send(String message) {
        notice.send(message);
        System.out.println("send sms " + message);
    }
}

class EmailNoticeDecorator extends DefaultNoticeDecorator {

    public EmailNoticeDecorator(Notice notice) {
        super(notice);
    }

    @Override
    public void send(String message) {
        notice.send(message);
        System.out.println("send email " + message);
    }
}

class WeChatNoticeDecorator extends DefaultNoticeDecorator {

    public WeChatNoticeDecorator(Notice notice) {
        super(notice);
    }

    @Override
    public void send(String message) {
        notice.send(message);
        System.out.println("send wechat " + message);
    }
}
