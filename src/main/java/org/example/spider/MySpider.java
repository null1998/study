package org.example.spider;

import org.example.common.SpiderConfig;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.handler.CompositePageProcessor;
import us.codecraft.webmagic.scheduler.FileCacheQueueScheduler;

/**
 * @author huang
 */
public class MySpider {
    public static void main(String[] args) {
        compositePageProcessors();
    }

    private static void simple() {
        Spider.create(new BaiduPageProcessor()).addUrl("https://www.baidu.com/").run();
    }

    private static void compositePageProcessors() {
        CompositePageProcessor compositePageProcessor = new CompositePageProcessor(Site.me().setUserAgent(SpiderConfig.USER_AGENT));
        compositePageProcessor.addSubPageProcessor(new ListPageProcessor());
        compositePageProcessor.addSubPageProcessor(new DetailPageProcessor());
        FileCacheQueueScheduler fileCacheQueueScheduler = new FileCacheQueueScheduler("C:\\Users\\asus\\Desktop\\test\\url_cache_file");
        Spider.create(compositePageProcessor).setScheduler(fileCacheQueueScheduler).addUrl("https://www.baidu.com/").run();
    }
}
