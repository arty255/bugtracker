package org.hetsold.bugtracker.model;

import org.hetsold.bugtracker.dao.util.BooleanToStringConverter;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class ArchivedEntity extends AbstractEntity {
    @Column(name = "archived")
    @Convert(converter = BooleanToStringConverter.class)
    private Boolean archived;

    protected ArchivedEntity() {
    }

    public Boolean getArchived() {
        return archived;
    }

    public void setArchived(Boolean archived) {
        this.archived = archived;
    }
}