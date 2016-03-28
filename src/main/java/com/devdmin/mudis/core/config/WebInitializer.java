package com.devdmin.mudis.core.config;

import com.devdmin.mudis.core.security.SecurityConfig;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class WebInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[] {SecurityConfig.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] { WebAppConfiguration.class };
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/", "/home"};
    }
}
