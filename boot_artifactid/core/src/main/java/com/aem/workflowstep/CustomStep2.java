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
@Properties({ @Property(name = Constants.SERVICE_DESCRIPTION, value = "BootCamp Custom Step 2."),
        @Property(name = Constants.SERVICE_VENDOR, value = "Adobe"),
        @Property(name = "process.label", value = "BootCamp Custom Step 2") })
public class CustomStep2 implements WorkflowProcess {

    @Override
    public void execute(final WorkItem item, final WorkflowSession wfsession, final MetaDataMap args)
	    throws WorkflowException {
	System.out.println(new Date() + " ___**___**___ Step 2 Was executed");
    }
}