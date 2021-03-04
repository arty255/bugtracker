package integration.page;

import org.openqa.selenium.WebDriver;
import static integration.page.PageUtil.waitForPageLoad;

public abstract class AbstractPage {
    private final String pageUrl;
    protected WebDriver webDriver;

    protected AbstractPage(String pageUrl, WebDriver webDriver) {
        this.pageUrl = pageUrl;
        this.webDriver = webDriver;
    }

    public void loadPage(String url) {
        if (!webDriver.getCurrentUrl().contains(url)) {
            webDriver.get(url);
            waitForPageLoad(webDriver);
        }
    }

    public void closeWebDriver() {
        webDriver.close();
    }


}