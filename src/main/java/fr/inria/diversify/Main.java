package fr.inria.diversify;

import fr.inria.diversify.bipartiteGraph.BipartiteGraph;
import org.uncommons.maths.random.PoissonGenerator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * User: Simon
 * Date: 5/7/13
 * Time: 1:24 PM
 */
public class Main {
    int nbPlatform;
    public static void main(String[] args) throws Exception {
        Main main = new Main();

        main.graph().displayGraph();
        main.printExtinctionSequence("test.csv",100);
    }


    public void printExtinctionSequence(String fileName, int n) throws IOException {
        Map<String, Double[]> es = new HashMap<String, Double[]>();
        FileWriter fstream = new FileWriter(fileName);
        BufferedWriter out = new BufferedWriter(fstream);

        es.put("random", extinctionSequence(n, "random"));
        es.put("maxApp", extinctionSequence(n, "maxApp"));
        es.put("minApp", extinctionSequence(n, "minApp"));
        es.put("maxService", extinctionSequence(n, "maxService"));
        es.put("minService", extinctionSequence(n, "minService"));


        out.write("random;maxApp;minApp;maxService;minService\n");
        for(int i = 0; i < nbPlatform; i++) {
            out.write(es.get("random")[i]+";");
            out.write(es.get("maxApp")[i]+";");
            out.write(es.get("minApp")[i]+";");
            out.write(es.get("maxService")[i]+";");
            out.write(es.get("minService")[i]+"\n");
        }
        out.close();
    }

    public Double[] extinctionSequence(int n, String strategy) {
        int[][] m = new int[n][];

        int j = 0;
        for(int i = 0; i < n; i++) {
            System.out.println(i+"  "+strategy);
            try {
                m[j] = graph().extinctionSequence(strategy);
                j++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Double ret[] = new Double[nbPlatform];
        for(int i = 0; i < m[0].length; i++) {
            double tmp = 0;
            for(int k = 0; k < j; k++) {
                tmp = m[k][i] + tmp;
            }
            if(i < nbPlatform*2 )
                ret[i] = tmp/(m[0].length*j);
        }
        return ret;
    }


    protected BipartiteGraph graph() throws Exception {
        nbPlatform = 200;
        BipartiteGraph graph = new BipartiteGraph();
        graph.initService(15,new PoissonGenerator(6, new Random()));

        graph.initApplication(200, false, 1, new PoissonGenerator(5, new Random()));
//        graph.initPlatform(nbPlatform,10);
//       graph.initRandomPlatform(40,10, new PoissonGenerator(4, new Random()));
        graph.initPlatformAndDependencies(10, new PoissonGenerator(4, new Random()));
//        graph.initDependencies();
        return  graph;
    }

}
