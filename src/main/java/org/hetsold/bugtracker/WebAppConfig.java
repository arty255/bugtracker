package org.hetsold.bugtracker;

<<<<<<< HEAD
import org.springframework.context.annotation.ComponentScan;
=======
>>>>>>> 81dc8e6... Separate configs created. Redundant bean definition in AppConfig replaced to @service and @registry annotations, component scan updated.
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebMvc
@Configuration()
@Import(AppConfig.class)
//@ComponentScan(basePackages = {"org.hetsold.bugtracker.rest"})
public class WebAppConfig implements WebMvcConfigurer {

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
}
