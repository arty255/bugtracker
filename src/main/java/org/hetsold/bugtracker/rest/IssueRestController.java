package org.hetsold.bugtracker.rest;

import org.hetsold.bugtracker.facade.IssueFacade;
import org.hetsold.bugtracker.model.IssueShortDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v0.1")
public class IssueRestController {
    private IssueFacade issueService;

    @Autowired
    public IssueRestController(IssueFacade issueService) {
        this.issueService = issueService;
    }

    @GetMapping("/issue")
    public List<IssueShortDTO> getIssueSimpleDTOList() {
        return issueService.getIssueList(null);
    }

    @GetMapping("/issue/{uuid}")
    public IssueShortDTO getIssueSimpleDTO(@PathVariable String uuid) {
        return issueService.getIssue(uuid);
    }

    @PostMapping("/issue")
    public void saveIssue(@RequestBody IssueShortDTO issueShortDTO) {
    }

    @PostMapping("/issue/generate")
    public void generateAnSaveIssue() {
        issueService.generateRandomIssue();
    }

    @DeleteMapping("/{uuid}")
    private void deleteIssue(@PathVariable String uuid) {
        issueService.deleteIssue(uuid, false);
    }
}