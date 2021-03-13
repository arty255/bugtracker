package org.hetsold.bugtracker.service.mapper;

import org.hetsold.bugtracker.dto.AbstractDTO;

import java.util.UUID;

public final class UUIDMapper {

    private UUIDMapper() {
    }

    public static UUID getUUID(AbstractDTO abstractDTO) {
        return getUUID(abstractDTO.getUuid());
    }

    public static UUID getUUID(String variable) {
        if (variable != null && !variable.isEmpty()) {
            return UUID.fromString(variable);
        }
        return null;
    }
}