package com.intelligrape.core.impl.servlets.tests;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;

import javax.jcr.Session;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.SearchResult;
import com.intelligrape.core.impl.servlets.RSSServlet;

@RunWith(MockitoJUnitRunner.class)
public class RSSServletTest {

    @Mock
    QueryBuilder builder;

    @Mock
    PredicateGroup predGroup;
    
    @Mock
    SearchResult srchResult;
    
    @InjectMocks
    RSSServlet rss;
    
    SlingHttpServletRequest mockRequest;
    SlingHttpServletResponse mockResponse;
    ResourceResolver mockResolver;
    Session mockSession;
    Query mockQuery;

    @Mock
    PrintWriter printWriter;

    private static final long serialVersionUID = 1L;
    
    @Before
    public void setUp() throws IOException {
	mockRequest = Mockito.mock(SlingHttpServletRequest.class);
	mockResponse = Mockito.mock(SlingHttpServletResponse.class);
	mockResolver = Mockito.mock(ResourceResolver.class);
	when(mockRequest.getResourceResolver()).thenReturn(mockResolver);
	mockSession = Mockito.mock(Session.class);
	when(mockResolver.adaptTo(Session.class)).thenReturn(mockSession);
	mockQuery = Mockito.mock(Query.class);
	// when(PredicateGroup.create(queryConditions)).thenReturn(predGroup);
	when(builder.createQuery(any(PredicateGroup.class), eq(mockSession))).thenReturn(mockQuery);
	when(mockQuery.getResult()).thenReturn(srchResult);
	when(srchResult.getTotalMatches()).thenReturn(100l);
	when(mockResponse.getWriter()).thenReturn(printWriter);
	
    }
    
    @After
    public void destroy() {
	mockRequest = null;
	mockResponse = null;
    }

    @Test
    public void doGetTest() throws IOException {
	stubParameters("/content/bootcamp/RSSTestPage", "10", "2");
	// when(srchResult.getHits()).thenReturn(new ArrayList<Hit>());
	rss.doGet(mockRequest, mockResponse);
    }
    
    private void stubParameters(final String path, final String noOfFeeds, final String pageNo) {
	when(mockRequest.getParameter("path")).thenReturn(path);
	when(mockRequest.getParameter("feeds")).thenReturn(noOfFeeds);
	when(mockRequest.getParameter("pageNo")).thenReturn(pageNo);
    }
}
