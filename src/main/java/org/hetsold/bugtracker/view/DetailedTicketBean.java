package org.hetsold.bugtracker.view;

import org.hetsold.bugtracker.model.MessageDTO;
import org.hetsold.bugtracker.model.TicketDTO;
import org.hetsold.bugtracker.model.UserDTO;
import org.hetsold.bugtracker.service.MessageService;
import org.hetsold.bugtracker.service.TicketService;
import org.hetsold.bugtracker.service.UserService;
import org.primefaces.component.tabview.Tab;
import org.primefaces.event.TabChangeEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.jsf.FacesContextUtils;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ManagedBean
@ViewScoped
public class DetailedTicketBean implements Serializable {
    private String uuid;
    private TicketDTO selectedTicketDTO;

    private List<MessageDTO> ticketMessages;

    private String messageContent;
    private MessageDTO selectedMessage;

    @Autowired
    private TicketService ticketService;
    @Autowired
    private UserService userService;
    @Autowired
    private MessageService messageService;

    @PostConstruct
    public void init() {
        FacesContextUtils
                .getRequiredWebApplicationContext(FacesContext.getCurrentInstance())
                .getAutowireCapableBeanFactory().autowireBean(this);
        ticketMessages = new ArrayList<>();
        initMessage();
    }

    public void preInitData() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            selectedTicketDTO = ticketService.getTicketDTO(uuid);
            initMessageList();
        }
    }

    public TicketDTO getSelectedTicketDTO() {
        return selectedTicketDTO;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void createIssueFromTicket() {

    }

    public void addMessageToTicket() {
        /*todo: getUser FromSecurityContext*/
        UserDTO user = userService.getUserById("19f0a834-4324-419a-8828-50494c2353e4");
        ticketService.addTicketMessage(selectedTicketDTO, new MessageDTO(messageContent), user);
        initMessage();
        initMessageList();
    }

    public void deleteMessage() {
        messageService.deleteMessage(selectedMessage);
        initMessage();
        initMessageList();
    }

    public void initMessageList() {
        ticketMessages = ticketService.getTicketMessages(selectedTicketDTO, 0, 99);
    }

    public void initMessage() {
        messageContent = "";
    }

    public List<MessageDTO> getTicketMessages() {
        return ticketMessages;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public MessageDTO getSelectedMessage() {
        return selectedMessage;
    }

    public void setSelectedMessage(MessageDTO selectedMessage) {
        this.selectedMessage = selectedMessage;
    }
}
