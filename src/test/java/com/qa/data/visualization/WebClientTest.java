package com.qa.data.visualization;


import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebClientTest {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void WebClient() throws Exception {
        WebClient client = new WebClient(BrowserVersion.FIREFOX_45);
        client.getOptions().setJavaScriptEnabled(true);
        client.getOptions().setActiveXNative(false);
        client.getOptions().setCssEnabled(false);
        client.getOptions().setThrowExceptionOnScriptError(false);
        client.setAjaxController(new NicelyResynchronizingAjaxController());
        ProxyConfig proxyConfig = new ProxyConfig();
        proxyConfig.setProxyHost("localhost");
        proxyConfig.setProxyPort(8888);
        client.getOptions().setProxyConfig(proxyConfig);
        HtmlPage page = client.getPage("http://localhost:8080/");
        page.getAnchorByName("pay---payContent").click();
    }
}
