package com.intelligrape.core.impl.servlets;

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
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.google.gson.Gson;

@SuppressWarnings("unchecked")
@Component(immediate = true, metatype = false, label = "RSSservlet")
@Service
@Properties({ @Property(name = "sling.servlet.methods", value = "GET"),
    @Property(name = "sling.servlet.paths", value = "/bin/servlets/RSSservlet") })
public class RSSServlet extends SlingAllMethodsServlet {
    
    private static final long serialVersionUID = 1L;
    
    @Reference
    private QueryBuilder builder;
    final Map jsonDataMap = new HashMap();
    final Map feeds = new TreeMap();
    
    @Override
    public void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
	    throws IOException {
	jsonDataMap.clear();
	feeds.clear();
	jsonDataMap.put("feeds", feeds);
	final String path = request.getParameter("path");
	final String noOfFeeds = request.getParameter("feeds");
	final String pageNumber = request.getParameter("pageNo");
	final int noOfFeedsPerPage = noOfFeeds == null ? 10 : Integer.parseInt(noOfFeeds);
	final int pageNo = pageNumber == null ? 1 : Integer.parseInt(pageNumber);
	
	try {
	    updateFeedsMapUsingQuery(path, noOfFeedsPerPage, request.getResourceResolver().adaptTo(Session.class),
		    pageNo);
	    writeMapToResponseAsJson(response);
	} catch (final InvalidQueryException e) {
	    e.printStackTrace();
	} catch (final RepositoryException e) {
	    e.printStackTrace();
	}
    }
    
    private void updateFeedsMapUsingQuery(final String path, final int noOfFeedsPerPage, final Session session,
	    final int pageNo) throws InvalidQueryException, RepositoryException {
	final Map<String, String> queryConditions = new HashMap<String, String>();
	queryConditions.put("path", path);
	queryConditions.put("type", "cq:Page");
	queryConditions.put("orderby", "@jcr:content/cq:lastModified");
	// map.put("orderby", "jcr:created");
	queryConditions.put("orderby.sort", "desc");
	queryConditions.put("1_property", "jcr:content/jcr:primaryType");
	queryConditions.put("1_property.value", "cq:PageContent");
	
	final Query query = builder.createQuery(PredicateGroup.create(queryConditions), session);
	query.setStart(noOfFeedsPerPage * (pageNo - 1));
	query.setHitsPerPage(noOfFeedsPerPage);
	
	final SearchResult result = query.getResult();
	final long totalMatches = result.getTotalMatches();
	final long noOfPages = totalMatches / noOfFeedsPerPage;
	jsonDataMap.put("total", totalMatches);
	jsonDataMap.put("pages", noOfPages);
	ValueMap prop;
	for (final Hit hit : result.getHits()) {
	    prop = hit.getProperties();
	    addFeed(prop.get("jcr:title"), prop.get("id"));
	}
    }
    
    private void addFeed(final Object title, final Object id) {
	final Map<Object, Object> pageProperties = new HashMap<Object, Object>();
	pageProperties.put("title", title);
	pageProperties.put("id", id);
	final int size = feeds.size() + 1;
	feeds.put("" + size, pageProperties);
    }
    
    private void writeMapToResponseAsJson(final SlingHttpServletResponse response) {
	final String json = new Gson().toJson(jsonDataMap);
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
}
