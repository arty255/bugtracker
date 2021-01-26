package org.hetsold.bugtracker.model;

public abstract class ArchivedIdentity {
    private Boolean isArchived;

    public Boolean getArchived() {
        return isArchived;
    }

    public void setArchived(Boolean archived) {
        isArchived = archived;
    }
}
