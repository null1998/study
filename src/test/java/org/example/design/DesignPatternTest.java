package org.example.design;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * 设计模式测试
 */
public class DesignPatternTest {
    /**
     * TODO 单例
     */
    @Test
    public void testSingleton() {

    }

    /**
     * 简单工厂
     */
    @Test
    public void testSimpleFactory() {
        SimpleFactory simpleFactory = new SimpleFactory();
        assert simpleFactory.create("A") instanceof ConcreteProductA;
        assert simpleFactory.create("B") instanceof ConcreteProductB;
    }

    /**
     * 工厂方法
     */
    @Test
    public void testFactoryMethod() {
        Factory concreteFactoryA = new ConcreteFactoryA();
        Factory concreteFactoryB = new ConcreteFactoryB();
        assert concreteFactoryA.create() instanceof ConcreteProductA;
        assert concreteFactoryB.create() instanceof ConcreteProductB;
    }

    /**
     * 生成器
     */
    @Test
    public void testBuilder() {
        CarDirector carDirector = new CarDirector();
        SportCarBuilder sportCarBuilder = new SportCarBuilder();
        System.out.println(carDirector.constructCar(sportCarBuilder));
        SUVCarBuilder suvCarBuilder = new SUVCarBuilder();
        System.out.println(carDirector.constructCar(suvCarBuilder));
    }

    /**
     * 外观
     */
    @Test
    public void testFacade() {
        Facade facade = new Facade();
        facade.watchMovie("雷神4");
    }

    /**
     * 适配器
     */
    @Test
    public void testAdapter() {
        XmlData xmlData = new XmlData();
        xmlData.setContent("我是xml数据");
        new JsonParser().parse(new XmlAdapter(xmlData));
    }

    /**
     * 桥接模式
     */
    @Test
    public void testBridge() {
        Bridge bridge = new Bridge();
        bridge.drawBlueCircle();
        bridge.drawRedTriangle();
    }

    /**
     * 组合
     */
    @Test
    public void testComposite() {
        Component first = new Composite();
        first.addComponent(new Leaf("a"));
        first.addComponent(new Leaf("b"));
        Component second = new Composite();
        first.addComponent(second);
        second.addComponent(new Leaf("c"));
        Component third = new Composite();
        second.addComponent(third);
        third.addComponent(new Leaf("d"));
        System.out.println(first.execute(0));
    }

    /**
     * 装饰
     */
    @Test
    public void testDecorator() {
        // 创建默认全局通知器
        Notice notice = new DefaultNoticeDecorator(null);
        // 根据配置加强通知器
        notice = new SmsNoticeDecorator(notice);
        notice = new EmailNoticeDecorator(notice);
        notice = new WeChatNoticeDecorator(notice);
        // 使用通知器
        notice.send("你好");
    }

    /**
     * 享元
     * -XX:+PrintGCDetails
     */
    @Test
    public void testFlyweight() {
        List<Lightweight> lightweights = new ArrayList<>();
        for (int i = 0; i < 1024; i++) {
            Lightweight lightweight = new Lightweight();
            lightweight.setId(i);
            lightweight.setFlyweight(Flyweight.getInstance());
            lightweights.add(lightweight);
        }
        assert lightweights.size() == 1024;
    }

    /**
     * 代理
     */
    @Test
    public void testProxy() {
        UserInfoService userInfoService = new CachedUserInfoServiceImpl();
        assert userInfoService.getUserIdList().size() == 3;
    }

    /**
     * 责任链
     */
    @Test
    public void testChainOfResponsibility() {
        Handler handler = new ConcreteHandlerA(new ConcreteHandlerB(null));
        handler.handle(new Request("A", "content a"));
        handler.handle(new Request("B", "content b"));
    }

    /**
     * 策略
     */
    @Test
    public void testStrategy() {
        Context context = new Context();
        context.setStrategy(new ConcreteStrategyA());
        context.doSomething("data a");
        context.setStrategy(new ConcreteStrategyB());
        context.doSomething("data b");
    }

    /**
     * 模板方法
     */
    @Test
    public void testTemplateMethod() {
        Template templateA = new ConcreteTemplateA();
        templateA.doSomething();
        Template templateB = new ConcreteTemplateB();
        templateB.doSomething();
    }

    /**
     * 命令
     */
    @Test
    public void testCommand() {
        Editor editor = new Editor();
        Command copyCommand = new CopyCommand(editor);
        copyCommand.execute();
        Command pasteCommand = new PasteCommand(editor);
        pasteCommand.execute();
    }

    /**
     * TODO 访问者
     */
    @Test
    public void testVisitor() {

    }

    /**
     * 观察者
     */
    @Test
    public void testObserver() {
        Subscriber normalSubscriber = new NormalSubscriber();
        Subscriber vipSubscriber = new VIPSubscriber();
        Publisher publisher = new Publisher();
        publisher.subscribe(normalSubscriber);
        publisher.subscribe(vipSubscriber);
        publisher.notify("data");
    }

    /**
     * 状态
     */
    @Test
    public void testState() {
        // 初始化播放器状态
        Player player = new Player();
        player.setState(new LockedState(player));
        player.setPlaying(false);
        // 锁定状态无法点击播放
        Assert.assertThrows(UnsupportedOperationException.class, player::clickPlay);
        // 解锁
        player.clickLock();
        assert player.getState() instanceof ReadyState;
        assert Boolean.FALSE.equals(player.getPlaying());
        // 播放
        player.clickPlay();
        assert player.getState() instanceof PlayingState;
        assert Boolean.TRUE.equals(player.getPlaying());
        // 锁定
        player.clickLock();
        assert player.getState() instanceof LockedState;
        assert Boolean.TRUE.equals(player.getPlaying());
        // 解锁
        player.clickLock();
        assert player.getState() instanceof PlayingState;
        assert Boolean.TRUE.equals(player.getPlaying());
        // 停止播放
        player.clickPlay();
        assert player.getState() instanceof ReadyState;
        assert Boolean.FALSE.equals(player.getPlaying());
        // 锁定
        player.clickLock();
        assert player.getState() instanceof LockedState;
        assert Boolean.FALSE.equals(player.getPlaying());
    }

    /**
     * TODO 解释器
     */
    @Test
    public void testInterpreter() {

    }

    /**
     * 迭代器
     */
    @Test
    public void testIterator() {
        String[] elements = new String[]{"a", "b", "c"};
        Iterator<String> iterator = new ConcreteIterator(elements);
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }

    /**
     * 中介者
     */
    @Test
    public void testMediator() {
        Mediator mediator = new ConcreteMediator();
        Colleague colleagueA = new ConcreteColleagueA(mediator);
        colleagueA.onEvent();
        Colleague colleagueB = new ConcreteColleagueB(mediator);
        colleagueB.onEvent();
    }

    /**
     * 备忘录
     */
    @Test
    public void testMemento() {
        Originator originator = new Originator();
        originator.setName("张三");
        Caretaker caretaker = new Caretaker(originator);
        caretaker.makeBackup();
        originator.setName("李四");
        caretaker.undo();
        assert "张三".equals(originator.getName());
    }
}
