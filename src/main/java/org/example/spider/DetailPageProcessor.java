package org.example.spider;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.handler.SubPageProcessor;

/**
 * @author huang
 */
public class DetailPageProcessor implements SubPageProcessor {
    @Override
    public MatchOther processPage(Page page) {
        System.out.println(page.getHtml());
        return MatchOther.NO;
    }

    @Override
    public boolean match(Request page) {
        return page.getUrl().startsWith("https://www.baidu.com/s");
    }
}
