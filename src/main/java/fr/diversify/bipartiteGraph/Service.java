package fr.diversify.bipartiteGraph;

/**
 * User: Simon
 * Date: 5/7/13
 * Time: 9:28 AM
 */
public class Service {
    protected String name;

    public Service(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
