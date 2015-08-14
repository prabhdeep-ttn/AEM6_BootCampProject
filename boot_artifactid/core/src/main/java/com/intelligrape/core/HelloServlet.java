package com.intelligrape.core;

import java.io.IOException;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;

@Component(immediate = true, metatype = false, label = "CreateNode")
@Service
@Properties(value = { @Property(name = "sling.servlet.methods", value = "GET"),
        @Property(name = "sling.servlet.paths", value = "/bin/servlets/CreateNode") })
public class HelloServlet extends SlingAllMethodsServlet {
    
    @Reference
    HelloService service;
    
    @Override
    protected void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
	    throws IOException {
	final String path = request.getParameter("path");
	final String name = request.getParameter("name");
	final String type = request.getParameter("type");
	// response.getWriter().write(path + name);
	final ResourceResolver resolver = request.getResourceResolver();
	
	// response.getWriter().write(service.createNode(path, name, type));
	response.getWriter().write(service.createResource(resolver, path, name, type));
    }
}