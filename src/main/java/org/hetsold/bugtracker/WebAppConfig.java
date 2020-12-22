package org.hetsold.bugtracker;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@ComponentScan(basePackages = {"org.hetsold.bugtracker.rest"})
public class WebAppConfig {

}
