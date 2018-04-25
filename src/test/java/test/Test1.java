package test;

import com.wangyang.pageRank.PageRanker;


public class Test1 {
    public static void main(String[] args) throws Exception {
        while (true){
            run();
        }
    }

    public static void run(){
        PageRanker pageRanker = new PageRanker();
       //     pageRanker.testPR();
        pageRanker.build();
    }
}
