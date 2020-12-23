package org.hetsold.bugtracker.rest;

import org.hetsold.bugtracker.facade.IssueFacade;
import org.hetsold.bugtracker.model.Issue;
import org.hetsold.bugtracker.model.IssueShortDTO;
import org.hetsold.bugtracker.rest.exceptions.IssueNotFoundException;
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

    @GetMapping("/issues")
    public @ResponseBody
    List<IssueShortDTO> getIssueSimpleDTOList() {
        return issueFacade.getIssueList(new IssueShortDTO(new Issue.Builder().withIssueEmpty().build()));
    }

    @GetMapping("/issues/{uuid}")
    public IssueShortDTO getIssueSimpleDTO(@PathVariable String uuid) {
        IssueShortDTO issue = issueFacade.getIssue(uuid);
        if (issue == null) {
            throw new IssueNotFoundException();
        }
        return issue;
    }

    @PostMapping("/issues")
    public void saveIssue(@RequestBody IssueShortDTO issueShortDTO) {
    }

    @PostMapping("/issues/generate")
    public void generateAnSaveIssue() {
        issueFacade.generateRandomIssue();
    }

    @DeleteMapping("issues/{uuid}")
    private void deleteIssue(@PathVariable String uuid) {
        issueFacade.deleteIssue(uuid, false);
    }
}