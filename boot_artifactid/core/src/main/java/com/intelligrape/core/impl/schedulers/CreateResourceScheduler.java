package com.intelligrape.core.impl.schedulers;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;

import com.day.cq.commons.jcr.JcrUtil;
import com.intelligrape.core.HelloService;

@Component
@Service(value = Runnable.class)
// @Property(name = "scheduler.expression", value = "0 0/2 * 1/1 * ? *")
public class CreateResourceScheduler implements Runnable {

    @Reference
    private ResourceResolverFactory resolverFactory;
    
    @Reference
    private HelloService createResourceService;

    @Override
    public void run() {
	final String path = "/content/bootcamp/schedulerPages/";
	try {
	    final Map<String, Object> param = new HashMap<String, Object>();
	    param.put(ResourceResolverFactory.SUBSERVICE, "datawrite");
	    final ResourceResolver resolver = resolverFactory.getServiceResourceResolver(param);
	    final Resource resource = resolver.getResource(path);
	    final String nodeName = JcrUtil.createValidChildName(resource.adaptTo(Node.class), "prabhdeep");
	    System.out.println("Service Message :\n"
		    + createResourceService.createResource(resolver, path, nodeName, "cq:Page"));
	} catch (final LoginException e) {
	    final StringWriter errors = new StringWriter();
	    e.printStackTrace(new PrintWriter(errors));
	    System.out.println("Node Wasn't Created due to  :\n" + errors.toString());
	} catch (final RepositoryException e) {
	    final StringWriter errors = new StringWriter();
	    e.printStackTrace(new PrintWriter(errors));
	    System.out.println("Node Wasn't Created due to Repository Exception : <br>" + errors.toString());
	}
    }
}