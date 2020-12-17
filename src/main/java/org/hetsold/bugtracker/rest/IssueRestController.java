package org.hetsold.bugtracker.rest;

import org.hetsold.bugtracker.model.Issue;
import org.hetsold.bugtracker.service.IssueAssembler;
import org.hetsold.bugtracker.service.IssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v0.1")
public class IssueRestController {
    private IssueService issueService;

    private IssueAssembler issueAssembler;

    @Autowired
    public IssueRestController(IssueService issueService, IssueAssembler issueAssembler) {
        this.issueService = issueService;
        this.issueAssembler = issueAssembler;
    }

    @GetMapping("/issue")
    public List<IssueShortDTO> getIssueSimpleDTOList() {
        return issueService.getIssueList().stream().map(issue -> issueAssembler.getIssueShortDTO(issue)).collect(Collectors.toList());
    }

    @GetMapping("/issue/{uuid}")
    public IssueShortDTO getIssueSimpleDTO(@PathVariable String uuid) {
        return issueAssembler.getIssueShortDTO(new Issue(uuid));
    }

    @PostMapping("/issue")
    public void saveIssue(@RequestBody IssueShortDTO issueShortDTO) {
        issueService.save(issueAssembler.getIssue(issueShortDTO));
    }

    @PostMapping("/issue/generate")
    public void generateAnSaveIssue() {
        issueService.generateAndSaveIssue();
    }

    @DeleteMapping("/{uuid}")
    private void deleteIssue(@PathVariable String uuid) {
        issueService.deleteIssue(new Issue(uuid));
    }
}