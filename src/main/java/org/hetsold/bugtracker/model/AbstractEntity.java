package org.hetsold.bugtracker.model;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.util.Objects;

/*
 * This util class describe typical JPA entity fields.
 */

@MappedSuperclass
public abstract class AbstractEntity {
    @Id
    private String uuid;
    @Version
    private Integer version;

    {
        version = 0;
        uuid = java.util.UUID.randomUUID().toString();
    }

    public AbstractEntity() {
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
        AbstractEntity that = (AbstractEntity) o;
        return uuid.equals(that.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}