package org.hetsold.bugtracker.dto;

import java.io.Serializable;

public abstract class ArchivedIdentityDTO extends AbstractDTO implements Serializable {
    private Boolean archived;

    protected ArchivedIdentityDTO() {
    }

    public Boolean getArchived() {
        return archived;
    }

    public void setArchived(Boolean archived) {
        this.archived = archived;
    }
}