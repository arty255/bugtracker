package integration.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoggedInPage {
    protected WebDriver webDriver;

    public LoggedInPage(WebDriver webDriver) {
        this.webDriver = webDriver;
        PageFactory.initElements(webDriver, this);
    }

    public String getWelcomeMessage() {
        return webDriver.findElement(By.xpath("//*[contains(@id, 'userGreetingsText')]")).getText();
    }

    public boolean containsTicketMenuItem() {
        return webDriver.findElement(By
                .xpath("//*/li/a[contains(@href,'/facelets/pages/tickets.jsf')]"))
                .isDisplayed();
    }

    public boolean containsIssuesMenuItem() {
        return webDriver.findElement(By
                .xpath("//*/li/a[contains(@href,'/bugtracker_war_exploded/facelets/pages/issues.jsf')]"))
                .isDisplayed();
    }

    public boolean containsUsersMenuItem() {
        return webDriver.findElement(By
                .xpath("//*/li/a[contains(@href,'/bugtracker_war_exploded/facelets/pages/users.jsf')]"))
                .isDisplayed();
    }
}