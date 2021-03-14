package org.hetsold.bugtracker.dto;

import java.io.Serializable;

public abstract class ArchivedIdentityDTO extends AbstractDTO implements Serializable {
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