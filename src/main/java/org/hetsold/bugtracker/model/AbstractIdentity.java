package org.hetsold.bugtracker.model;

import javax.persistence.*;
import java.util.UUID;

/*
 * This util class describe typical JPA entity fields.
 */
@Entity
public class AbstractIdentity {
    @Id
    private java.util.UUID UUID;
    @Version
    private Integer version;

    public AbstractIdentity() {
    }

    public java.util.UUID getUUID() {
        return UUID;
    }

    public void setUUID(java.util.UUID UUID) {
        this.UUID = UUID;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}