package integration.page.detailedTicketPage;

import integration.page.PageUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class MessagesSectionWidget {
    private WebDriver webDriver;
    @FindBy(xpath = "//ul[@class='ui-datalist-data']")
    private WebElement messagesContainer;
    @FindBy(xpath = "//div[@class='ticketMessagesWrapper']//span[@id='ticketForm:ticketMessages:orderContainer']//a")
    private WebElement sortLink;

    public MessagesSectionWidget(WebDriver webDriver) {
        this.webDriver = webDriver;
        PageFactory.initElements(webDriver, this);
    }

    public int getDisplayedMessagesCount() {
        return messagesContainer.findElements(By.xpath("//li[@class='ui-datalist-item']")).size();
    }

    public MessagesSectionWidget sortByDate() {
        sortLink.click();
        PageUtil.waitAjaxExecution(webDriver);
        return this;
    }


    public void deleteMessage(int index) {

    }

    public void editMessage(int index) {

    }


    public String getMessageContent(int index) {
        WebElement message = getMessageElement(index);
        return message.findElement(By.className("messageContentArea")).getText();
    }

    private WebElement getMessageElement(int index) {
        return webDriver.findElement(By.xpath("//div[@id='ticketForm:ticketMessages_content']//ul[contains(@class,'ui-datalist-data')]/li[" + index + "]"));
    }
}