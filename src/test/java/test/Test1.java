package test;

import com.wangyang.docIndex.Searcher;


public class Test1 {
    public static void main(String[] args) throws Exception {
//        Indexer indexer = new Indexer();
//        indexer.index();
        Searcher searcher = new Searcher();
        searcher.init();
        searcher.search("三亚 九");
        searcher.closeReader();
//        PageRanker pageRanker = new PageRanker();
//        pageRanker.build();
    }

}
