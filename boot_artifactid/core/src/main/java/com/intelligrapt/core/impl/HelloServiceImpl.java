package com.intelligrapt.core.impl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.ItemExistsException;
import javax.jcr.LoginException;
import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.jcr.api.SlingRepository;

import com.intelligrapt.core.HelloService;

/**
 * One implementation of the {@link HelloService}. Note that the repository is
 * injected, not retrieved.
 */
@Service
@Component(metatype = false, name = "com.intelligrapt.core.impl.HelloServiceImpl")
public class HelloServiceImpl implements HelloService {
    
    @Reference
    private SlingRepository repository;
    
    @Override
    public String createNode(final String parentPath, final String nodeName, final String nodeType) {
	// final String parentPath = "content/bootcamp/";
	String message = "";
	try {
	    final Session session = repository.login(new SimpleCredentials("admin", "admin".toCharArray()));
	    final Node parentNode = session.getRootNode().getNode(parentPath);
	    final Node newNode = parentNode.addNode(nodeName, nodeType);
	    // newNode.setProperty("message",
	    // "This is Newly created Node");
	    
	    message = "Node Named <b>" + newNode.getName() + "</b> Was Created On Path <b>"
		    + newNode.getPath().toString() + "</b>";
	    session.save();
	    session.logout();
	    
	} catch (final LoginException e) {
	    message = "Node Wasn't Created due to Login Exception";
	} catch (final PathNotFoundException e) {
	    message = "Node Wasn't Created As The Specified Path For Node Creation Was Not Found";
	} catch (final ItemExistsException e) {
	    message = "Node Wasn't Created as a Node with Same Name is Already Present On Specified Path!!";
	} catch (final RepositoryException e) {
	    final StringWriter errors = new StringWriter();
	    e.printStackTrace(new PrintWriter(errors));
	    message = "Node Wasn't Created due to Repository Exception : <br>" + errors.toString();
	}
	return message;
    }
    
    @Override
    public String createResource(final ResourceResolver resolver, final String path, final String name,
	    final String type) {
	final Resource res = resolver.getResource("/" + path);
	System.out.println("Path : /" + path);
	System.out.println("" + res);
	try {
	    final Map<String, Object> map = new HashMap<String, Object>();// res.adaptTo(HashMap<String,
	    // Object>.class);
	    map.put("jcr:primaryType", type);
	    // map.put("approvedBy", wfInitiator);
	    final Resource newRes = resolver.create(res, name, map);
	    newRes.getResourceResolver().commit();
	    return "Resource created with name " + newRes.getName() + " at " + newRes.getPath();
	} catch (final PersistenceException e) {
	    final StringWriter errors = new StringWriter();
	    e.printStackTrace(new PrintWriter(errors));
	    return "Node Wasn't Created due to PersistenceException : <br>" + errors.toString();
	}
    }
}
