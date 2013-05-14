package fr.diversify.bipartiteGraph;


import java.util.HashSet;
import java.util.Set;

/**
 * User: Simon
 * Date: 5/7/13
 * Time: 9:28 AM
 */
public class Platform extends Node{
    protected int maxApplicationPerPlatform;
    protected Set<Service> services;
    private Set<Application> applications;


    public Platform(int maxApplicationPerPlatform) {
        this.maxApplicationPerPlatform = maxApplicationPerPlatform;
        services = new HashSet<Service>();
        applications = new HashSet<Application>();
    }


    public void addService(Service s) {
        services.add(s);
    }

    @Override
    public String toString() {
        return "Platform" + services.toString();
    }

    public Set<Service> getServices() {
        return services;
    }

    public void setServices(Set<Service> services) {
        this.services = services;
    }

    public boolean full() {
        return applications.size() == maxApplicationPerPlatform;
    }

    public void addApplication(Application app) {
        applications.add(app);
        app.addDependence(this);
    }

    public Set<Application> getApplications() {
        return applications;
    }

    public double getCost() {
        // ToDo
        return 0d;
    }
}
