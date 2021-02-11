package org.hetsold.bugtracker.view.user;

import org.hetsold.bugtracker.dto.user.UserDTO;
import org.hetsold.bugtracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.jsf.FacesContextUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter("org.hetsold.bugtracker.UserConverter")
public class UserConverter implements Converter {
    @Autowired
    private UserService userService;

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        FacesContextUtils
                .getRequiredWebApplicationContext(FacesContext.getCurrentInstance())
                .getAutowireCapableBeanFactory().autowireBean(this);
        if (!s.isEmpty()) {
            return userService.getUser(new UserDTO(s));
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        if (o instanceof UserDTO) {
            return ((UserDTO) o).getUuid();
        } else {
            return "";
        }
    }
}
