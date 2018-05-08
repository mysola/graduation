package test;

import com.wangyang.docIndex.Indexer;
import com.wangyang.docIndex.Searcher;
import com.wangyang.pageRank.PageRanker;

import java.util.Map;


public class Test1 {
    public static void main(String[] args) throws Exception {
        Indexer indexer = new Indexer();
        indexer.index();
        indexer.close();
//
//        Searcher searcher = new Searcher();
//        searcher.init();
//        searcher.search("三亚 九",null);
//        searcher.closeReader();
        PageRanker pageRanker = new PageRanker();
//        pageRanker.build();
//        pageRanker.writePR();
        Map<String,Double> map = pageRanker.readPR();
        System.out.println(1);
    }

}
