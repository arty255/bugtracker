package org.hetsold.bugtracker.dto;

import java.util.Objects;

public abstract class AbstractDTO {
    private String uuid;

    protected AbstractDTO() {
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractDTO abstractDTO = (AbstractDTO) o;
        return Objects.equals(uuid, abstractDTO.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}