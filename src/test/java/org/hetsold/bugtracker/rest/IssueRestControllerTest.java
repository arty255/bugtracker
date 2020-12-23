package org.hetsold.bugtracker.rest;

import org.hetsold.bugtracker.AppConfig;
import org.hetsold.bugtracker.TestAppConfig;
import org.hetsold.bugtracker.WebAppConfig;
import org.hetsold.bugtracker.facade.IssueFacade;
import org.hetsold.bugtracker.facade.SimpleIssueFacade;
import org.hetsold.bugtracker.model.IssueShortDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class, TestAppConfig.class, WebAppConfig.class})
@ActiveProfiles(profiles = {"dev", ""})
@WebAppConfiguration
public class IssueRestControllerTest {

    @Mock
    private IssueFacade issueFacade = new SimpleIssueFacade();

    @Autowired
    @InjectMocks
    private IssueRestController issueRestController;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;
    private List<IssueShortDTO> issueShortDTOList;

    @Before
    public void prepareData() {
        Mockito.reset();
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        issueShortDTOList = new ArrayList<>();
        issueShortDTOList.add(new IssueShortDTO("d1"));
        issueShortDTOList.add(new IssueShortDTO("d2"));
        issueShortDTOList.add(new IssueShortDTO("d3"));
    }

    @Test
    public void checkIfCanGetData() throws Exception {
        Mockito.when(issueFacade.getIssueList(Mockito.any())).thenReturn(issueShortDTOList);
        mockMvc.perform(get("/api/v0.1/issues"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)));
        Mockito.verify(issueFacade, Mockito.atLeastOnce()).getIssueList(Mockito.any());
    }

    @Test
    public void checkIfIncorrectIdRequestGiveErrorCode() throws Exception {
        String correctIdCode = "21121-12311-1121322";
        IssueShortDTO correctShortDTO = new IssueShortDTO(correctIdCode);
        Mockito.when(issueFacade.getIssue(correctIdCode)).thenReturn(null);
        mockMvc.perform(get("/api/v0.1/issues/{id}", correctIdCode))
                .andExpect(status().isNotFound());
        Mockito.verify(issueFacade, Mockito.atLeastOnce()).getIssue(correctIdCode);
    }


    @Test
    public void checkIfIncorrectIdCanBeFound() throws Exception {
        String correctIdCode = "21121-12311-1121322";
        IssueShortDTO correctShortDTO = new IssueShortDTO(correctIdCode);
        Mockito.when(issueFacade.getIssue(correctIdCode)).thenReturn(correctShortDTO);
        mockMvc.perform(get("/api/v0.1/issues/{id}", correctIdCode))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.uuid", is(correctIdCode)));
        Mockito.verify(issueFacade, Mockito.times(1)).getIssue(correctIdCode);
    }
}