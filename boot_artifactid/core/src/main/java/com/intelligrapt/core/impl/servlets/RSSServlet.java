package com.intelligrapt.core.impl.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.InvalidQueryException;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.google.gson.Gson;

@Component(immediate = true, metatype = false, label = "RSSservlet")
@Service
@Properties(value = { @Property(name = "sling.servlet.methods", value = "GET"),
        @Property(name = "sling.servlet.paths", value = "/bin/servlets/RSSservlet") })
public class RSSServlet extends SlingAllMethodsServlet {
    
    private static final long serialVersionUID = 1L;
    
    @Reference
    private QueryBuilder builder;

    final Map<String, Map<Object, Object>> feeds = new TreeMap<String, Map<Object, Object>>();
    
    @Override
    protected void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
	    throws IOException {

	feeds.clear();
	int noOfFeeds = 10;
	final String path = request.getParameter("path");
	final Resource res = request.getResourceResolver().getResource(path);
	final String noOfFeedsToBeDisplayed = request.getParameter("feeds");
	if (noOfFeedsToBeDisplayed != null) {
	    noOfFeeds = Integer.parseInt(noOfFeedsToBeDisplayed);
	}

	try {
	    updateFeedsMapUsingQuery(path, noOfFeeds, request.getResourceResolver().adaptTo(Session.class));
	    writeMapToResponseAsJson(response);
	} catch (final InvalidQueryException e) {
	    e.printStackTrace();
	} catch (final RepositoryException e) {
	    e.printStackTrace();
	}
	// updateFeedsMap(res.adaptTo(Page.class));

    }

    private void updateFeedsMapUsingQuery(final String path, final int noOfFeeds, final Session session)
	    throws InvalidQueryException, RepositoryException {
	final Map<String, String> map = new HashMap<String, String>();
	map.put("path", path);
	map.put("type", "cq:Page");
	// map.put("orderby", "@jcr:content/cq:lastModified");
	map.put("orderby", "jcr:created");
	map.put("orderby.sort", "desc");
	map.put("1_property", "jcr:content/jcr:primaryType");
	map.put("1_property.value", "cq:PageContent");

	final Query query = builder.createQuery(PredicateGroup.create(map), session);
	// query.setStart(0);
	query.setHitsPerPage(noOfFeeds);

	final SearchResult result = query.getResult();
	for (final Hit hit : result.getHits()) {
	    final ValueMap prop = hit.getProperties();
	    addFeed(prop.get("jcr:title"), prop.get("id"));
	}
    }
    
    private void addFeed(final Object title, final Object id) {
	final Map<Object, Object> pageProperties = new HashMap<Object, Object>();
	pageProperties.put("title", title);
	pageProperties.put("id", id);
	final int size = feeds.size() + 1;
	feeds.put("" + size, pageProperties);
	// return size == noOfFeeds;
    }

    private void writeMapToResponseAsJson(final SlingHttpServletResponse response) {
	final String json = new Gson().toJson(feeds);
	response.setContentType("application/json");
	response.setCharacterEncoding("UTF-8");
	try {
	    response.getWriter().write(json);
	} catch (final IOException e) {
	    final StringWriter errors = new StringWriter();
	    e.printStackTrace(new PrintWriter(errors));
	    System.out.println(errors.toString());
	}
    }
    
    // private void updateFeedsMap(final Page sourcePage) {
    // final Iterator<Page> yearIterator = sourcePage.listChildren();
    // while (yearIterator.hasNext()) {
    // final Page year = yearIterator.next(); // 2015
    // final Iterator<Page> monthIterator = year.listChildren();
    // while (monthIterator.hasNext()) {
    // final Page month = monthIterator.next(); // 06
    // final Iterator<Page> feedIterator = month.listChildren();
    // while (feedIterator.hasNext()) {
    // final Page feed = feedIterator.next();
    // final ValueMap prop = feed.getProperties();
    // if (addFeed(prop.get("jcr:title"), prop.get("id")))
    // return;
    // }
    // }
    //
    // }
    // }

    //
    // private void logic1(final SlingHttpServletRequest request) {
    // final ResourceResolver resolver = request.getResourceResolver();
    //
    // // response.getWriter().write(service.createNode(path, name, type));
    //
    // final String parent = "/content/bootcamp/RSSTestPage/";
    // final Date d = new Date();
    // final String year = "2015";
    // final String month = "06";
    // final String completePath = parent + year + "/" + month;
    // final Resource res =
    // request.getResourceResolver().getResource(completePath);
    // final Page page = res.adaptTo(Page.class);
    //
    // final ValueMap prop = page.getProperties("id");
    // }
    //
    // private void calculate(final Page page) {
    // final Iterator<Page> pages = page.listChildren();
    // int size = 0;
    // if (pages instanceof Collection<?>) {
    // size = ((Collection<?>) pages).size();
    // while (pages.hasNext() && noOfFeedsLeft - 1 >= 0) {
    // // feeds.put("1", pages.next());
    // --noOfFeedsLeft;
    // }
    // }
    // // noOfFeedsLeft -= size;
    // if (noOfFeedsLeft > 0) {
    // // calculate(page.getParent().);
    // }
    // }
    //
    
}