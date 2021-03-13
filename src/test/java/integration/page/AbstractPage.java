package integration.page;

import org.openqa.selenium.WebDriver;

import static integration.page.PageUtil.waitForPageLoad;

public abstract class AbstractPage {
    protected WebDriver webDriver;

    protected AbstractPage(WebDriver webDriver) {
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