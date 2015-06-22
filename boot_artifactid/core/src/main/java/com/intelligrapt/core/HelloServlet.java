package com.intelligrapt.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;

import com.day.cq.wcm.api.Page;

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
	//
	//
	//
	//
	//
	//
	//
	//
	//
	//
	
	final int noOfFeedsLeft = 10;
	final String parent = "/content/bootcamp/RSSTestPage/";
	final Date d = new Date();
	final String year = "2015";
	final String month = "06";
	final String completePath = parent + year + "/" + month;
	final Resource res = request.getResourceResolver().getResource(completePath);
	final Page page = res.adaptTo(Page.class);
	
	final ValueMap prop = page.getProperties("id");
	
    }

    final List<Page> feeds = new ArrayList<Page>();
    int noOfFeedsLeft = 10;
    
    private void calculate(final Page page) {
	final Iterator<Page> pages = page.listChildren();
	int size = 0;
	if (pages instanceof Collection<?>) {
	    size = ((Collection<?>) pages).size();
	    while (pages.hasNext() && noOfFeedsLeft - 1 >= 0) {
		feeds.add(pages.next());
		--noOfFeedsLeft;
	    }
	}
	// noOfFeedsLeft -= size;
	if (noOfFeedsLeft > 0) {
	    // calculate(page.getParent().);
	}
    }
}