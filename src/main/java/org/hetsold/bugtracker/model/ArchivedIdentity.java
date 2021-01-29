package org.hetsold.bugtracker.model;

public abstract class ArchivedIdentity {
    private Boolean archived;

    public Boolean getArchived() {
        return archived;
    }

    public void setArchived(Boolean archived) {
        this.archived = archived;
    }
}
