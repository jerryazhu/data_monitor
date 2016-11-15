package com.qa.data.visualization.cron;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Scheduled(cron = "1 1 8 * * *")
    public void cacheDataToRedis() throws Exception {
        logger.info("cacheDataToRedis start");
        WebClient client = new WebClient(BrowserVersion.FIREFOX_45);
        client.getOptions().setJavaScriptEnabled(true);
        client.getOptions().setActiveXNative(false);
        client.getOptions().setCssEnabled(false);
        client.getOptions().setThrowExceptionOnScriptError(false);
        client.setAjaxController(new NicelyResynchronizingAjaxController());
        HtmlPage page = client.getPage("http://localhost/");
        page.getAnchorByName("pay---payContent").click();
        page.getAnchorByName("reg---regContent").click();
        logger.info("cacheDataToRedis end");
    }
}
