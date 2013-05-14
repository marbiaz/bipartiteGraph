package fr.inria.diversify.bipartiteGraph;

import java.util.Set;

/**
 * User: Simon
 * Date: 5/7/13
 * Time: 1:14 PM
 */
public abstract class Node {
    protected Set<Service> services;


    public void addService(Service s) {
        services.add(s);
    }
    public Set<Service> getServices() {
        return services;
    }

}
