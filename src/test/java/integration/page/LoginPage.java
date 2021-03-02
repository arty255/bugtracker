package integration.page;

import integration.WebDriverConfig;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {
    private WebDriver webDriver;
    @FindBy(xpath = "//*[@id=\"j_idt7:j_idt15\"]")
    private WebElement loginField;
    @FindBy(xpath = "//*[@id=\"j_idt7:j_idt19\"]")
    private WebElement passField;
    @FindBy(xpath = "//*[@id=\"j_idt7:j_idt21\"]")
    private WebElement loginButton;
    @FindBy(xpath = "//*[@id=\"j_idt7:j_idt11\"]/div/ul/li/span")
    private WebElement errorMessage;

    public LoginPage(WebDriver webDriver) {
        this.webDriver = webDriver;
        if (!webDriver.getCurrentUrl().equals(WebDriverConfig.getProperty("login.page.url"))) {
            throw new IllegalArgumentException();
        }
        PageFactory.initElements(webDriver, this);
    }

    public LoggedInPage login(String login, String password) {
        loginField.sendKeys(login);
        passField.sendKeys(password);
        loginButton.click();
        return new LoggedInPage(webDriver);
    }

    public String errorMessage() {
        return errorMessage.getText();
    }
}