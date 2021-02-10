package org.hetsold.bugtracker.view;

import org.hetsold.bugtracker.dto.FullUserDetails;
import org.hetsold.bugtracker.dto.UserDTO;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class ActiveUserBean {
    public UserDTO getActiveUser() {
        if (!SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            return ((FullUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserDTO();
        }
        return null;
    }
}