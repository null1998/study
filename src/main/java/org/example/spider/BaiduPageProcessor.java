package org.example.spider;

import org.example.common.HtmlAttr;
import org.example.common.HtmlTag;
import org.example.common.SpiderConfig;
import org.jsoup.nodes.Element;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * @author huang
 */
public class BaiduPageProcessor implements PageProcessor {
    private final Site site = Site.me()
            .setUserAgent(SpiderConfig.USER_AGENT);

    @Override
    public void process(Page page) {
        Element ulElement = page.getHtml().getDocument().getElementById("hotsearch-content-wrapper");
        if (ulElement != null) {
            for (Element liElement : ulElement.children()) {
                String title = liElement.text();
                String hrefValue = liElement.select(HtmlTag.A).attr(HtmlAttr.HREF);
                System.out.println(title + ":" + hrefValue);
            }
        }
    }

    @Override
    public Site getSite() {
        return site;
    }
}
