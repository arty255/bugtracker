package org.hetsold.bugtracker.view;

import org.hetsold.bugtracker.facade.TicketConvertor;
import org.hetsold.bugtracker.model.*;
import org.hetsold.bugtracker.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.jsf.FacesContextUtils;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.util.List;
import java.util.stream.Collectors;

@ManagedBean
@ViewScoped
public class TicketListBean {
    private List<TicketDTO> ticketDTOList;
    @Autowired
    private TicketService ticketService;
    @Autowired
    private TicketConvertor ticketConvertor;

    @PostConstruct
    public void init() {
        FacesContextUtils
                .getRequiredWebApplicationContext(FacesContext.getCurrentInstance())
                .getAutowireCapableBeanFactory().autowireBean(this);
        ticketDTOList = ticketService.getTickets().stream().map(ticketConvertor::getTicketDTO).collect(Collectors.toList());
        /*ticketDTOList = new ArrayList<>();
        TicketDTO ticketDTO = new TicketDTO("kjsdfk032=3234231");
        ticketDTO.setCreationTime(new Date());
        ticketDTO.setDescription("derdfkn 32l3k4n2l3 ");
        ticketDTO.setProductVersion("dafdjf ik2");
        ticketDTO.setReproduceSteps("1) sdlafjnb 32oj3n 3o3n1 2)dfasldjfnf 3)0dkjsannn3");
        ticketDTO.setResolveState(TicketResolveState.Resolved);
        ticketDTO.setVerificationState(TicketVerificationState.NotVerified);
        UserDTO userDTO = new UserDTO(new User("finame", "loname"));
        ticketDTO.setUser(userDTO);
        ticketDTOList.add(ticketDTO);
        ticketDTOList.add(ticketDTO);
        ticketDTOList.add(ticketDTO);*/
    }

    public List<TicketDTO> getTicketDTOList() {
        return ticketDTOList;
    }
}