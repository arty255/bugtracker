package integration.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoggedInPage {
    protected WebDriver webDriver;

    public LoggedInPage(WebDriver webDriver) {
        this.webDriver = webDriver;
        PageFactory.initElements(webDriver, this);
    }

    private void waitForLoading() {
        new WebDriverWait(webDriver, 10)
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(@id, 'userGreetingsText')]")));
    }

    public String getWelcomeMessage() {
        waitForLoading();
        return webDriver.findElement(By.xpath("//*[contains(@id, 'userGreetingsText')]")).getText();
    }

    public boolean containsTicketMenuItem() {
        waitForLoading();
        return webDriver.findElement(By
                .xpath("//*/li/a[contains(@href,'/facelets/pages/tickets.jsf')]"))
                .isDisplayed();
    }

    public boolean containsIssuesMenuItem() {
        waitForLoading();
        return webDriver.findElement(By
                .xpath("//*/li/a[contains(@href,'/bugtracker_war_exploded/facelets/pages/issues.jsf')]"))
                .isDisplayed();
    }

    public boolean containsUsersMenuItem() {
        waitForLoading();
        return webDriver.findElement(By
                .xpath("//*/li/a[contains(@href,'/bugtracker_war_exploded/facelets/pages/users.jsf')]"))
                .isDisplayed();
    }
}