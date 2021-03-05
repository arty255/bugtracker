package integration.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage extends AbstractPage {
    public static final String loginUrl = "http://localhost:8080/bugtracker_war_exploded/facelets/pages/login.jsf";
    @FindBy(xpath = "//*[@id=\"j_idt7:j_idt15\"]")
    private WebElement loginField;
    @FindBy(xpath = "//*[@id=\"j_idt7:j_idt19\"]")
    private WebElement passField;
    @FindBy(xpath = "//*[@id=\"j_idt7:j_idt21\"]")
    private WebElement loginButton;
    @FindBy(xpath = "//*[@id=\"j_idt7:j_idt11\"]/div/ul/li/span")
    private WebElement errorMessage;

    private LoginPage(WebDriver webDriver) {
        super(loginUrl, webDriver);
        PageFactory.initElements(webDriver, this);
    }

    public static LoginPage createLoginPage(WebDriver webDriver) {
        LoginPage loginPage = new LoginPage(webDriver);
        loginPage.loadPage(loginUrl);
        return loginPage;
    }

    public void login(String login, String password) {
        loginField.sendKeys(login);
        passField.sendKeys(password);
        loginButton.click();
        PageUtil.waitForAjaxAndPage(webDriver);
    }

    public boolean containsErrorMessage(){
        PageUtil.waitForVisibility(By.className("ui-messages-error"), webDriver);
        return errorMessage.isDisplayed();
    }

    public String errorMessageText() {
        PageUtil.waitForVisibility(By.className("ui-messages-error"), webDriver);
        return errorMessage.getText();
    }
}