package org.example.spider;

import org.example.common.HtmlAttr;
import org.example.common.HtmlTag;
import org.jsoup.nodes.Element;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.handler.SubPageProcessor;

/**
 * @author huang
 */
public class ListPageProcessor implements SubPageProcessor {
    @Override
    public MatchOther processPage(Page page) {
        Element ulElement = page.getHtml().getDocument().getElementById("hotsearch-content-wrapper");
        if (ulElement != null) {
            for (Element liElement : ulElement.children()) {
                String title = liElement.text();
                String hrefValue = liElement.select(HtmlTag.A).attr(HtmlAttr.HREF);
                System.out.println(title + ":" + hrefValue);
                Request request = new Request();
                request.setUrl(hrefValue);
                page.addTargetRequest(request);
            }
        }
        return MatchOther.YES;
    }

    @Override
    public boolean match(Request page) {
        return page.getUrl().equals("https://www.baidu.com/");
    }
}
