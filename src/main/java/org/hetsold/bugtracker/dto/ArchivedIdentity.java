package org.hetsold.bugtracker.dto;

public abstract class ArchivedIdentity extends AbstractDTO {
    private Boolean archived;

    public Boolean getArchived() {
        return archived;
    }

    public void setArchived(Boolean archived) {
        this.archived = archived;
    }
}