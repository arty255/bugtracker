package org.hetsold.bugtracker.view.user;

import org.hetsold.bugtracker.model.SecurityUserAuthority;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter("org.hetsold.bugtracker.SecurityUserAuthorityConvertor")
public class SecurityUserAuthorityConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        return SecurityUserAuthority.valueOf(s);
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        if (o instanceof SecurityUserAuthority) {
            return ((SecurityUserAuthority) o).name();
        }
        return "";
    }
}
