package fr.inria.diversify;

import fr.diversify.bipartiteGraph.BipartiteGraph;
import org.uncommons.maths.random.PoissonGenerator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * User: Simon
 * Date: 5/7/13
 * Time: 1:24 PM
 */
public class Main {

    public static void main(String[] args) throws Exception {
        Main main = new Main();


        main.printExtinctionSequence(100, "maxService");
        main.printExtinctionSequence(100, "minService");
        main.printExtinctionSequence(100, "maxApp");
        main.printExtinctionSequence(100, "minApp");
    }


    public void printExtinctionSequence(String fileName, int n) throws IOException {
        FileWriter fstream = new FileWriter(fileName);
        BufferedWriter out = new BufferedWriter(fstream);

        out.write(strategy+"\n");
        for(double d: extinctionSequence(n, strategy))
            out.write(d+"\n");

        out.close();
    }

    public List<Double> extinctionSequence(int n, String strategy) {
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

        List<Double> ret = new ArrayList<Double>();
        for(int i = 0; i < m[0].length; i++) {
            double tmp = 0;
            for(int k = 0; k < j; k++) {
                tmp = m[k][i] + tmp;
            }
            if(tmp != 0)
                ret.add(tmp/(m[0].length*j));
        }
        return  ret;
    }


    protected BipartiteGraph graph() throws Exception {
        BipartiteGraph graph = new BipartiteGraph();
        graph.initService(50,new PoissonGenerator(40, new Random()));

        graph.initApplication(200, false, 1, new PoissonGenerator(15, new Random()));
        graph.initPlatform(25,10);
        graph.initDependencies();
        return  graph;
    }

}
