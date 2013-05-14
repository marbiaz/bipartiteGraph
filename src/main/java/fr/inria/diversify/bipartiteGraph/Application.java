package fr.inria.diversify.bipartiteGraph;

import edu.uci.ics.jung.graph.Graph;

import java.util.HashSet;
import java.util.Set;

/**
 * User: Simon
 * Date: 5/7/13
 * Time: 9:28 AM
 */
public class Application extends Node {
    protected int numberOfDependencies;
    protected Set<Platform> dependencies;
    protected boolean multiDependencies;
    private static int count;

    public Application(boolean multiDependencies, int numberOfDependencies) {
        this.multiDependencies = multiDependencies;
        this.numberOfDependencies = numberOfDependencies;
        services = new HashSet<Service>();
        dependencies = new HashSet<Platform>();
    }

    public void addDependence(Platform p) {
        dependencies.add(p);
    }

    public void removeDependence(Platform p) {
        dependencies.remove(p);
    }



    @Override
    public String toString() {
        return "app " + services.toString();
    }

    public boolean isWorking() {
        Set<Service> s = new HashSet<Service>();
        s.addAll(services);
        for (Platform p : dependencies) {
            s.removeAll(p.getServices());
            if(s.isEmpty())
                return true;
            if(!multiDependencies)
                s.addAll(services);
        }
        return false;
    }


    public void addInGraph(Graph<Node, String> graph) {
        graph.addVertex(this);
        for (Node node : dependencies) {
            count++;
            graph.addEdge(count+"", this, node);
        }
    }

    public int getNumberOfDependencies() {
        return numberOfDependencies;
    }

    public boolean workingOn(Platform p) {
        return p.getServices().containsAll(services);
    }
}
