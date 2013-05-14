package fr.inria.diversify;

import fr.diversify.bipartiteGraph.BipartiteGraph;
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


        main.printExtinctionSequence("test.csv",100);
        main.printExtinctionSequence("test.csv",100);
        main.printExtinctionSequence("test.csv",100);
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
//        out.write("nbPlatform");
//        for(int i = 0; i < nbPlatform; i++)
//            out.write(";"+(nbPlatform - i));
//
//        out.write("\nrandom");
//        for(double d: extinctionSequence(n, "random"))
//            out.write(";"+d);
//        out.write("\nmaxApp");
//        for(double d: extinctionSequence(n, ""))
//            out.write(";"+d);
//        out.write("\nminApp");
//        for(double d: extinctionSequence(n, "minApp"))
//            out.write(";"+d);
//        out.write("\nmaxService");
//        for(double d: extinctionSequence(n, "maxService"))
//            out.write(";"+d);
//        out.write("\nminService");
//        for(double d: extinctionSequence(n, "minService"))
//            out.write(";"+d);
        out.close();
    }

    public Double[] extinctionSequence(int n, String strategy) {
        int[][] m = new int[n][];

        int j = 0;
        for(int i = 0; i < n; i++) {
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
            if(i < nbPlatform )
                ret[i] = tmp/(m[0].length*j);
        }
        return ret;
    }


    protected BipartiteGraph graph() throws Exception {
        nbPlatform = 25;
        BipartiteGraph graph = new BipartiteGraph();
        graph.initService(50,new PoissonGenerator(40, new Random()));

        graph.initApplication(200, false, 1, new PoissonGenerator(15, new Random()));
        graph.initPlatform(nbPlatform,10);
        graph.initDependencies();
        return  graph;
    }

}
