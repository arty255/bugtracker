package org.hetsold.bugtracker.rest;

import org.hetsold.bugtracker.facade.IssueFacade;
import org.hetsold.bugtracker.model.IssueShortDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v0.1")
public class IssueRestController {
    private IssueFacade issueFacade;

    public IssueRestController() {
    }

    @Autowired
    public IssueRestController(IssueFacade issueFacade) {
        this.issueFacade = issueFacade;
    }

    @GetMapping("/issue")
    public List<IssueShortDTO> getIssueSimpleDTOList() {
        return issueFacade.getIssueList(null);
    }

    @GetMapping("/issue/{uuid}")
    public IssueShortDTO getIssueSimpleDTO(@PathVariable String uuid) {
        return issueFacade.getIssue(uuid);
    }

    @PostMapping("/issue")
    public void saveIssue(@RequestBody IssueShortDTO issueShortDTO) {
    }

    @PostMapping("/issue/generate")
    public void generateAnSaveIssue() {
        issueFacade.generateRandomIssue();
    }

    @DeleteMapping("/{uuid}")
    private void deleteIssue(@PathVariable String uuid) {
        issueFacade.deleteIssue(uuid, false);
    }
}