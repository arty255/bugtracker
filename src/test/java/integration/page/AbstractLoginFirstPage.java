package integration.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AbstractLoginFirstPage extends AbstractPage {
    private final String login;
    private final String password;

    public AbstractLoginFirstPage(WebDriver webDriver, String login, String password) {
        super(webDriver);
        this.login = login;
        this.password = password;
    }

    @Override
    public void loadPage(String url) {
        LoginPage.createLoginPage(webDriver).login(login, password);
        waitForLogin();
        super.loadPage(url);
    }

    public void waitForLogin() {
        new WebDriverWait(webDriver, 15)
                .until(ExpectedConditions.not(ExpectedConditions.urlContains(LoginPage.loginUrl)));
    }
}