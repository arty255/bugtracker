package org.hetsold.bugtracker.dto;

public abstract class ArchivedIdentityDTO extends AbstractDTO {
    private Boolean archived;

    public ArchivedIdentityDTO() {
    }

    public Boolean getArchived() {
        return archived;
    }

    public void setArchived(Boolean archived) {
        this.archived = archived;
    }
}