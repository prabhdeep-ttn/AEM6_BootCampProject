package com.aem.workflowstep;

import java.util.Date;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.osgi.framework.Constants;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowProcess;
//Sling Imports
import com.adobe.granite.workflow.metadata.MetaDataMap;

@Component
@Service
@Properties({ @Property(name = Constants.SERVICE_DESCRIPTION, value = "BootCamp Custom Step 1."),
    @Property(name = Constants.SERVICE_VENDOR, value = "Adobe"),
    @Property(name = "process.label", value = "BootCamp Custom Step 1.") })
public class CustomStep implements WorkflowProcess {
    
    @Override
    public void execute(final WorkItem item, final WorkflowSession wfsession, final MetaDataMap args)
	    throws WorkflowException {
	final Date d = new Date();
	System.out.println(d + "******************************************************");
	System.out.println("Edited for Git");
	final long scheduleTime = d.getTime() + 500 * 1000;
	item.getWorkflow().getWorkflowData().getMetaDataMap().put("absoluteTime", scheduleTime);
	System.out.println(d.toString() + " *****__**__**____ Step 1 Was executed");
    }
}