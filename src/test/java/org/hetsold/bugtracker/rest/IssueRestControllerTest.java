package org.hetsold.bugtracker.rest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {org.hetsold.bugtracker.AppConfig.class, org.hetsold.bugtracker.TestAppConfig.class})
@ActiveProfiles("test")
public class IssueRestControllerTest {

    @Test
    public void getIssueSimpleDTOList() {

    }
}