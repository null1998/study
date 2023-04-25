package org.example.design;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * 设计模式测试
 * 创建型、结构型、行为型
 */
public class DesignPatternTest {
    /**
     * TODO 单例模式
     * 一个单一的类，该类负责创建自己的对象，同时确保只有单个对象被创建，创建型
     */
    @Test
    public void testSingleton() {

    }

    /**
     * 工厂模式
     * 在创建对象时不会对客户端暴露创建逻辑，并且是通过使用一个共同的接口来指向新创建的对象，创建型
     */
    @Test
    public void testSimpleFactory() {
        SimpleFactory simpleFactory = new SimpleFactory();
        assert simpleFactory.create("A") instanceof ConcreteProductA;
        assert simpleFactory.create("B") instanceof ConcreteProductB;
    }

    /**
     * 抽象工厂模式
     * 围绕一个超级工厂创建其他工厂，不需要显式指定它们的类，创建型
     */
    @Test
    public void testAbstractFactory() {
        Factory concreteFactoryA = new ConcreteFactoryA();
        Factory concreteFactoryB = new ConcreteFactoryB();
        assert concreteFactoryA.create() instanceof ConcreteProductA;
        assert concreteFactoryB.create() instanceof ConcreteProductB;
    }

    /**
     * 建造者模式
     * 使用多个简单的对象一步一步构建成一个复杂的对象，创建型
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
     * 外观模式
     * 隐藏系统的复杂性，并向客户端提供了一个客户端可以访问系统的接口，结构型
     */
    @Test
    public void testFacade() {
        Facade facade = new Facade();
        facade.watchMovie("雷神4");
    }

    /**
     * 适配器模式
     * 作为两个不兼容的接口之间的桥梁，结构型
     */
    @Test
    public void testAdapter() {
        XmlData xmlData = new XmlData();
        xmlData.setContent("我是xml数据");
        new JsonParser().parse(new XmlAdapter(xmlData));
    }

    /**
     * 桥接模式
     * 用于把抽象化与实现化解耦，使得二者可以独立变化，结构型
     */
    @Test
    public void testBridge() {
        Bridge bridge = new Bridge();
        bridge.drawBlueCircle();
        bridge.drawRedTriangle();
    }

    /**
     * 组合模式
     * 依据树形结构来组合对象，用来表示部分以及整体层次，结构型
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
     * 装饰器模式
     * 允许向一个现有的对象添加新的功能，同时又不改变其结构，结构型
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
     * 享元模式
     * 用于减少创建对象的数量，以减少内存占用和提高性能，结构型
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
     * 代理模式
     * 创建具有现有对象的对象，以便向外界提供功能接口，结构型
     */
    @Test
    public void testProxy() {
        UserInfoService userInfoService = new CachedUserInfoServiceImpl();
        assert userInfoService.getUserIdList().size() == 3;
        assert userInfoService.getUserIdList().size() == 3;
    }

    /**
     * 责任链模式
     * 给予请求的类型，对请求的发送者和接收者进行解耦，每个接收者都包含对另一个接收者的引用。如果一个对象不能处理该请求，那么它会把相同的请求传给下一个接收者，依此类推，行为型
     */
    @Test
    public void testChainOfResponsibility() {
        Handler handler = new ConcreteHandlerA(new ConcreteHandlerB(null));
        handler.handle(new Request("A", "content a"));
        handler.handle(new Request("B", "content b"));
    }

    /**
     * 策略模式
     * 创建表示各种策略的对象和一个行为随着策略对象改变而改变的 context 对象。策略对象改变 context 对象的执行算法，行为型
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
     * 模板模式
     * 一个抽象类公开定义了执行它的方法的方式/模板。它的子类可以按需要重写方法实现，但调用将以抽象类中定义的方式进行，行为型
     */
    @Test
    public void testTemplateMethod() {
        Template templateA = new ConcreteTemplateA();
        templateA.doSomething();
        Template templateB = new ConcreteTemplateB();
        templateB.doSomething();
    }

    /**
     * 命令模式
     * 请求以命令的形式包裹在对象中，并传给调用对象。调用对象寻找可以处理该命令的合适的对象，并把该命令传给相应的对象，该对象执行命令，行为型
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
     * TODO 访问者模式
     * 使用了一个访问者类，它改变了元素类的执行算法。通过这种方式，元素的执行算法可以随着访问者改变而改变，行为型
     */
    @Test
    public void testVisitor() {

    }

    /**
     * 观察者模式
     * 当一个对象被修改时，则会自动通知依赖它的对象，行为型
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
     * 状态模式
     * 类的行为是基于它的状态改变的，行为型
     */
    @Test
    public void testState() {
        // 初始化播放器状态
        Player player = new Player();
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
     * TODO 解释器模式
     * 提供了评估语言的语法或表达式的方式，实现了一个表达式接口，该接口解释一个特定的上下文。这种模式被用在 SQL 解析、符号处理引擎等，行为模式
     */
    @Test
    public void testInterpreter() {

    }

    /**
     * 迭代器模式
     * 用于顺序访问集合对象的元素，不需要知道集合对象的底层表示，行为型
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
     * 中介者模式
     * 用来降低多个对象和类之间的通信复杂性。这种模式提供了一个中介类，该类通常处理不同类之间的通信，并支持松耦合，使代码易于维护，行为型
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
     * 备忘录模式
     * 保存一个对象的某个状态，以便在适当的时候恢复对象，行为型
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
