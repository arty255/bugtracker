package org.hetsold.bugtracker.dto;

import java.io.Serializable;

public abstract class ArchivedIdentityDTO extends AbstractDTO implements Serializable {
    private static final long serialVersionUID = -6616321234434158364L;
    private Boolean archived;

    protected ArchivedIdentityDTO() {
        super();
    }

    public Boolean getArchived() {
        return archived;
    }

    public void setArchived(final Boolean archived) {
        this.archived = archived;
    }
}