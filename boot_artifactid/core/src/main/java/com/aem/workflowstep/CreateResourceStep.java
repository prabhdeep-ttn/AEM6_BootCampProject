package com.aem.workflowstep;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.framework.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.HistoryItem;
import com.adobe.granite.workflow.exec.Route;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowProcess;
//Sling Imports
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.adobe.granite.workflow.model.WorkflowTransition;
import com.day.cq.mailer.MessageGatewayService;
import com.intelligrape.core.HelloService;

@Component
@Service
@Properties({ @Property(name = Constants.SERVICE_DESCRIPTION, value = "BootCamp Create Resource."),
        @Property(name = Constants.SERVICE_VENDOR, value = "Adobe"),
        @Property(name = "process.label", value = "BootCamp Create a Page Resource at contentbootcamp") })
public class CreateResourceStep implements WorkflowProcess {
    
    /** Default log. */
    protected final Logger log = LoggerFactory.getLogger(this.getClass());
    
    @Reference
    private ResourceResolverFactory resolverFactory;
    
    @Reference(target = "(component.name=com.intelligrape.core.impl.HelloServiceImpl)")
    private HelloService createResourceService;
    
    @Reference(target = "(component.name=com.intelligrape.core.impl.HelloServiceImpl2)")
    private HelloService createResourceService2;
    
    // Inject a MessageGatewayService
    @Reference
    private MessageGatewayService messageGatewayService;
    
    @Override
    public void execute(final WorkItem item, final WorkflowSession wfsession, final MetaDataMap args)
	    throws WorkflowException {
	
	final List<HistoryItem> historyItems = wfsession.getHistory(item.getWorkflow());
	final Object payload = item.getWorkflowData().getPayload();
	
	final HistoryItem lastHistoryItem = historyItems.get(historyItems.size() - 1);
	System.out.println("User ID Of Last History Item is : " + lastHistoryItem.getUserId());
	
	// System.out.println("Participant: "
	// +
	// item.getWorkflow().getWorkflowData().getMetaDataMap().get("lastTaskAction",
	// ""));
	for (final Route route : wfsession.getRoutes(item, false)) {
	    for (final WorkflowTransition dest : route.getDestinations()) {
		dest.getFrom();
		dest.getTo();
	    }
	}
	System.out.println("_____________Current Assignee : " + item.getCurrentAssignee());
	System.out.println("_____________Current Workflow Initiator : " + item.getWorkflow().getInitiator());
	// createResource(item.getWorkflow().getInitiator());
    }
    
    private void createResource(final String wfInitiator) {
	ResourceResolver resolver = null;
	try {
	    final Map<String, Object> param = new HashMap<String, Object>();
	    param.put(ResourceResolverFactory.SUBSERVICE, "datawrite");
	    resolver = resolverFactory.getServiceResourceResolver(param);
	    System.out.println("Service Message :\n"
		    + createResourceService.createResource(resolver, "content/bootcamp", "wfGeneratedResource",
		            "cq:PageContent"));
	} catch (final LoginException e) {
	    final StringWriter errors = new StringWriter();
	    e.printStackTrace(new PrintWriter(errors));
	    System.out.println("Node Wasn't Created due to  :\n" + errors.toString());
	}
    }
    
}