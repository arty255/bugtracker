package org.hetsold.bugtracker.model;

public abstract class ArchivedIdentity {
    private boolean isArchived;

    public boolean isArchived() {
        return isArchived;
    }

    public void setArchived(boolean archived) {
        isArchived = archived;
    }
}
