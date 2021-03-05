package integration.page.detailedTicketPage;

import integration.page.PageUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class PostMessageSectionWidget {
    private final WebDriver webDriver;

    @FindBy(xpath = "//div[contains(@data-widget,'commentAccordionWidget')]")
    private WebElement expandButton;

    @FindBy(xpath = "//div[contains(@class,'messageEditorWrappedArea')]//textarea[contains(@class,'ui-inputtextarea')]")
    private WebElement textArea;
    @FindBy(xpath = "//button[.//span[contains(text(),'Comment')]]")
    private WebElement sendMessageButton;
    @FindBy(xpath = "//button[.//span[contains(text(),'Clear')]]")
    private WebElement clearMessageButton;
    @FindBy(xpath = "//button[.//span[contains(text(),'Hide coment window')]]")
    private WebElement hideCommentWindowButton;
    @FindBy(xpath = "//ul[contains(@role,'tablist')]//li[contains(@aria-selected,'true')]")
    private WebElement activeTypeHeader;
    @FindBy(className = "renderedPreview")
    private WebElement renderedPreview;

    public PostMessageSectionWidget(WebDriver webDriver) {
        this.webDriver = webDriver;
        PageFactory.initElements(webDriver, this);
    }

    public boolean isExpanded() {
        String expandAttributeValue = webDriver
                .findElement(By.xpath("//div[contains(@data-widget,'commentAccordionWidget')]//div[contains(@class,'ui-accordion-header')]"))
                .getAttribute("aria-expanded");
        return Boolean.parseBoolean(expandAttributeValue);
    }

    public boolean isWriteTabSelected() {
        return "Write".equals(activeTypeHeader.getText());
    }

    public boolean isPreviewHeaderSelected() {
        return "Preview".equals(activeTypeHeader.getText());
    }

    public PostMessageSectionWidget selectWrite() {
        webDriver.findElement(By.xpath("//ul[contains(@role,'tablist')]//a[contains(text(),'Write')]")).click();
        PageUtil.waitForAjaxAndPage(webDriver);
        return this;
    }

    public PostMessageSectionWidget selectPreview() {
        webDriver.findElement(By.xpath("//ul[contains(@role,'tablist')]//a[contains(text(),'Preview')]")).click();
        PageUtil.waitForAjaxAndPage(webDriver);
        PageUtil.waitForVisibility(renderedPreview, webDriver);
        return this;
    }

    public String getPreviewContent() {
        return renderedPreview.findElement(By.className("messageContentArea")).getText();
    }

    public PostMessageSectionWidget expand() {
        expandButton.click();
        PageUtil.waitForAjaxAndPage(webDriver);
        PageUtil.waitForVisibility(clearMessageButton, webDriver);
        return this;
    }

    public PostMessageSectionWidget fillMessage(String messageContent) {
        if (!isExpanded()) {
            expand();
        }
        textArea.sendKeys(messageContent);
        return this;
    }

    public String getFilledMessageContent() {
        return textArea.getAttribute("value");
    }

    public void sendMessage() {
        sendMessageButton.click();
        PageUtil.waitForAjaxAndPage(webDriver);
        PageUtil.waitSeconds(webDriver, 1);
    }

    public PostMessageSectionWidget clearMessage() {
        clearMessageButton.click();
        return this;
    }

    public void hideCommentWindow() {
        hideCommentWindowButton.click();
        PageUtil.waitForAjaxAndPage(webDriver);
    }
}