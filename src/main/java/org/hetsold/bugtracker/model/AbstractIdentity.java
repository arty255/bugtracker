package org.hetsold.bugtracker.model;

import javax.persistence.*;
import java.util.Objects;

/*
 * This util class describe typical JPA entity fields.
 */

@Entity
public class AbstractIdentity {
    @Id
    private String uuid;
    @Version
    private Integer version;

    {
        uuid = java.util.UUID.randomUUID().toString();
    }

    public AbstractIdentity() {
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String UUID) {
        this.uuid = UUID;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractIdentity that = (AbstractIdentity) o;
        return uuid.equals(that.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}