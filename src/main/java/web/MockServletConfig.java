package web;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import java.util.Collections;
import java.util.Enumeration;

public class MockServletConfig implements ServletConfig {
    private final ServletContext servletContext;

    public MockServletConfig(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    public String getServletName() {
        return "MockServlet";
    }

    @Override
    public ServletContext getServletContext() {
        return servletContext;
    }

    @Override
    public String getInitParameter(String name) {
        return null;
    }

    @Override
    public Enumeration<String> getInitParameterNames() {
        return Collections.emptyEnumeration();
    }
}
