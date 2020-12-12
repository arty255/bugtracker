package org.hetsold.bugtracker.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class IssueRestController {

    @GetMapping("/issue")
    public List<IssueSimpleDTO> getIssueSimpleDTOList() {
        List<IssueSimpleDTO> list = new ArrayList<>();
        list.add(new IssueSimpleDTO("", "data_1"));
        return list;
    }

}
