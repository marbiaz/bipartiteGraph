package fr.inria.diversify.bipartiteGraph;

import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import org.uncommons.maths.number.NumberGenerator;

import java.util.*;

/**
 * User: Simon
 * Date: 5/7/13
 * Time: 11:01 AM
 */
public class BipartiteGraph{
    protected List<Application> applications;
    protected List<Platform> platforms;
    protected Set<Service> services;
    protected Map<Service, Integer> servicesDistribution;

    public BipartiteGraph() {
        applications = new ArrayList<Application>();
        platforms = new ArrayList<Platform>();
    }
    /**
     *
    * @param n The number of services
    * @param ng The service distribution.
    */
    public void initService(int n, NumberGenerator ng) {
        services = new HashSet<Service>();
        for(int i = 0; i < n; i++)
            services.add(new Service("s"+i));

        initServiceDistribution(ng);
    }

    /**
     *
     * @param numberOfApplication The number of application in this bipartite graph
     * @param multiDependencies if true, a application can used services from several platform
     * @param numberOfDependencies The maximum number of platform used by a application
     * @param nbService A number generator for the number of service in applications
     */
    public void initApplication(int numberOfApplication, boolean multiDependencies, int numberOfDependencies, NumberGenerator nbService) {
        applications = new ArrayList<Application>(numberOfApplication);
        for(int i = 0; i < numberOfApplication; i++) {
            Application app = new Application(multiDependencies, numberOfDependencies);
            initServiceFor(app, nbService);
            applications.add(app);
        }
    }

    /**
     * Initialize the platforms.
     * The platforms contains all the services. We need other types of initialization
     * @param numberOfPlatform The number of platform in this bipartite graph
     * @param maxApplicationPerPlatform  The maximum number of services provided by a platform
     */
    public void initPlatform(int numberOfPlatform, int maxApplicationPerPlatform) {
        for (int i = 0; i < numberOfPlatform; i++) {
            Platform p = new Platform(maxApplicationPerPlatform);
            p.setServices(services);
            platforms.add(p);
        }

    }

    public void initRandomPlatform(int numberOfPlatform, int maxApplicationPerPlatform, NumberGenerator nbService) {
        for (int i = 0; i < numberOfPlatform; i++) {
            Platform p = new Platform(maxApplicationPerPlatform);
            initServiceFor(p, nbService);
            platforms.add(p);
        }
    }

    /**
     * Initialize the links between applications and platforms
     * @throws Exception
     */
    public void initDependencies() throws Exception {
        Random r = new Random();
        List<Platform> notFullPlatform = new ArrayList<Platform>();
        notFullPlatform.addAll(platforms);
        for(Application app : applications) {
            int borne = Math.max(r.nextInt(app.getNumberOfDependencies()+1), 1);
            for(int i = 0; i < borne; i++) {
                Platform p = getPlatformFor(app, notFullPlatform);
                p.addApplication(app);
                if(p.full())
                    notFullPlatform.remove(p);
                if(notFullPlatform.isEmpty())
                    throw new Exception("Pas de plateforme libre pour l'application: "+app);
            }
        }
    }

    /**
     * Todo: add strategy
     * @param app
     * @param notFullPlatform
     * @return
     * @throws Exception
     */
    protected Platform getPlatformFor(Application app, List<Platform> notFullPlatform) {
       Platform ret = null;
        for (Platform p : notFullPlatform) {
            if(app.workingOn(p))
                if(ret == null ||
                        p.getServices().size() < ret.getServices().size())
                    ret = p;
        }
        return  ret;
    }




    /**
     * Compute the extinction sequence for this bipartite graph
     * @param strategy The strategy used for killing the platform (maxApp, minApp, maxService, minService, random)
     * @return The extinction sequence
     */
    public int[] extinctionSequence(String strategy) {
        int kill[] = new int[applications.size()];

        int borne = platforms.size();
        for(int i = 0; i < borne; i++) {
            kill[i] = applications.size();
            Platform p = getPlatformToRemove(strategy);
            Set<Application> toRemove = new HashSet<Application>();
            for (Application app: applications) {
                app.removeDependence(p);
                if(!app.isWorking())
                    toRemove.add(app);
            }
            applications.removeAll(toRemove);
        }
        return kill;
    }

    /**
     * Compute the surface under the extinction sequence for this bipartite graph
     * @param strategy The strategy used for killing the platform (maxApp, minApp, maxService, minService, random)
     * @return
     */
    public double esSurface(String strategy) {
        double es = 0.0;
        int[] ess = extinctionSequence(strategy);
        double normal = ess.length*ess[0];

        for (int i : ess)
            es = es + i;

        return es/normal;
    }

    public double getCost() {
        // ToDo
        return 0d;
    }

    public void initPlatformAndDependencies(int maxApplicationPerPlatform, NumberGenerator nbService){
        Random r = new Random();
        List<Platform> notFullPlatform = new ArrayList<Platform>();

        for(Application app : applications) {
            int borne = Math.max(r.nextInt(app.getNumberOfDependencies()+1), 1);
            for(int i = 0; i < borne; i++) {
                Platform p = getPlatformFor(app, notFullPlatform);
                while(p == null) {
                    p = initPlatform(maxApplicationPerPlatform, nbService);
                    if(app.workingOn(p)){
                        platforms.add(p);
                        notFullPlatform.add(p);
                    }
                    else
                        p = null;
                }
                p.addApplication(app);
                if(p.full())
                    notFullPlatform.remove(p);
            }
        }
    }

    protected Platform initPlatform(int maxApplicationPerPlatform, NumberGenerator nbService) {
        Platform p = new Platform(maxApplicationPerPlatform);
        initServiceFor(p, nbService);
        return p;
    }

    protected void initServiceFor(Node app, NumberGenerator nbService) {
        int max = 0;
        List<Service> list = new ArrayList<Service>();
        for(Service s : servicesDistribution.keySet()) {
            max = max + servicesDistribution.get(s);
            for(int i = 0; i < servicesDistribution.get(s); i++)
                list.add(s);
        }
        Random r = new Random();
        Integer borne = Math.min((Integer)nbService.nextValue(), services.size());
        while(app.getServices().size() < borne) {
            app.addService(list.get(r.nextInt(list.size())));
        }
    }

    protected void initServiceDistribution(NumberGenerator ng) {
        servicesDistribution = new HashMap<Service, Integer>();
        for(Service s : services)
            servicesDistribution.put(s,Math.max(1,(Integer)ng.nextValue()));
    }


    protected Platform getPlatformToRemove(String strategy) {
        Random r = new Random();
        Platform p = platforms.get(r.nextInt(platforms.size()));

        if(strategy.equals("maxService"))
            for(Platform pp : platforms)
                if(p.getServices().size() < pp.getServices().size())
                    p = pp;

        if(strategy.equals("minService"))
            for(Platform pp : platforms)
                if(p.getServices().size() > pp.getServices().size())
                    p = pp;

        if(strategy.equals("maxApp"))
            for(Platform pp : platforms)
                if(p.getApplications().size() < pp.getApplications().size())
                    p = pp;

        if(strategy.equals("minApp"))
            for(Platform pp : platforms)
                if(p.getApplications().size() > pp.getApplications().size())
                    p = pp;

        platforms.remove(p);
        return p;
    }


    public Graph<Node, String> getJungGraph(){
        Graph<Node, String> graph = new DirectedSparseGraph<Node, String>();

        for(Application node: applications)
            node.addInGraph(graph);

        for(Node node: platforms)
            graph.addVertex(node);
        return graph;
    }

    public void displayGraph() {
        Design d = new Design(getJungGraph());
        d.display();
    }
}
