package org.hetsold.bugtracker.view;

import org.hetsold.bugtracker.dto.user.SecurityUserDetails;
import org.hetsold.bugtracker.dto.user.UserDTO;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class ActiveUserBean {
    public UserDTO getActiveUser() {
        if (!"anonymousUser".equals(SecurityContextHolder.getContext().getAuthentication().getPrincipal())) {
            return ((SecurityUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserDTO();
        }
        return null;
    }
}