package org.hetsold.bugtracker.service.mapper;

import org.hetsold.bugtracker.dto.IssueEventDTO;
import org.hetsold.bugtracker.model.IssueEvent;

import java.util.List;
import java.util.stream.Collectors;

public class IssueEventMapper {
    public static List<IssueEventDTO> getIssueEventList(List<IssueEvent> issueEvents) {
        return issueEvents.stream()
                .map(IssueEventDTO::new)
                .collect(Collectors.toList());
    }
}