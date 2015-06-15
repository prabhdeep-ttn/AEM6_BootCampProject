package com.intelligrapt.core;

import org.apache.sling.api.resource.ResourceResolver;

/**
 * A simple service interface
 */
public interface HelloService {
    
    /**
     * Creates A Node At a Predefined Location in 'Content' folder.
     *
     * @param path
     * @param type
     */
    public String createNode(String path, String name, String type);
    
    /**
     * Creates A Resource At a Predefined Location in 'Content' folder.
     *
     * @param resolver
     * @param path
     * @param type
     * @param workFlowInitiator
     */
    public String createResource(ResourceResolver resolver, String path, String name, String type);
    
}