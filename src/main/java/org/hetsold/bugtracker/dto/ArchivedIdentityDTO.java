package org.hetsold.bugtracker.dto;

public abstract class ArchivedIdentityDTO extends AbstractDTO {
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