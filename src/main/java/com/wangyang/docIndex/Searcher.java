package com.wangyang.docIndex;

import com.wangyang.entity.UrlClick;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.*;
import org.apache.lucene.queries.CustomScoreProvider;
import org.apache.lucene.queries.CustomScoreQuery;
import org.apache.lucene.queries.function.FunctionValues;
import org.apache.lucene.queries.function.valuesource.BytesRefFieldSource;
import org.apache.lucene.queries.function.valuesource.FieldCacheSource;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

@Component
public class Searcher {


    private static final float TITLE_BOOST = 1F;

    private static final float DESCRI_BOOST = 1F;

    private static final float TEXT_BOOST = 1F;

    private static final String[] fields = {"text","title","description"};

    private static final String indexDir = "/home/mysola/IdeaProjects/indexs/";

    private IndexSearcher indexSearcher;

    //重用indexReader
    private IndexReader indexReader;

    private QueryParser queryParser;

    private Map<String,Double> pageRank;

    private Map<String,UrlClick> anonymousUrlClick;

    private Map<String,UrlClick> realNameUrlClick;

    private SortedDocValues[] docUrlValues;

    public void setPageRank(Map<String, Double> pageRank) {
        this.pageRank = pageRank;
    }

    public void setAnonymousUrlClick(Map<String, UrlClick> anonymousUrlClick) {
        this.anonymousUrlClick = anonymousUrlClick;
    }

    public void setRealNameUrlClick(Map<String, UrlClick> realNameUrlClick) {
        this.realNameUrlClick = realNameUrlClick;
    }

    @PostConstruct
    public void init() throws IOException {
        Directory dir = FSDirectory.open(Paths.get(indexDir));
        indexReader = DirectoryReader.open(dir);
        indexSearcher = new IndexSearcher(indexReader);
        Map<String, Float> boosts = new HashMap<>(fields.length);
        boosts.put(fields[0],TEXT_BOOST);
        boosts.put(fields[1],TITLE_BOOST);
        boosts.put(fields[2],DESCRI_BOOST);

        queryParser = new MultiFieldQueryParser(fields,LuceneUtil.getAnalyzer(), boosts);

        initDocUrlValues();

    }

    private void initDocUrlValues() throws IOException {
        int len = indexSearcher.getIndexReader().leaves().size();
        docUrlValues = new SortedDocValues[len];
        for (int i = 0; i < len; i++) {
            docUrlValues[i] = DocValues.getSorted(indexSearcher.getIndexReader()
                    .leaves().get(i).reader(),"url");
        }
    }

    private String getDocUrlFromDocValues(int docID){
        BytesRef urlByteRef = null;
        for(SortedDocValues docValues : docUrlValues){
            urlByteRef = docValues.get(docID);

            if(urlByteRef!=null&&urlByteRef.length!=0){
                return urlByteRef.utf8ToString();
            }
        }
        return null;
    }

    private Query buildQuery(String queryStr) throws ParseException {
        Query query = queryParser.parse(queryStr);
        return new MyCustomScoreQuery(query);
    }

    public SearchResult[] search(String queryStr,int pageNum) {
        try {
            Query query = buildQuery(queryStr);

            TopDocs topDocs = indexSearcher.search(query,3*10);
            Document document = null;
            QueryScorer scorer = new QueryScorer(query);
            Fragmenter fragmenter = new SimpleSpanFragmenter(scorer);
            SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter("<b><font color='red'>", "</font>");
            Highlighter highlighter = new Highlighter(simpleHTMLFormatter, scorer);
            highlighter.setTextFragmenter(fragmenter);

            SearchResult[] results = new SearchResult[10];
            int index = 0;
            for (int i = (pageNum-1)*10; i < pageNum*10&&i<topDocs.totalHits; i++) {
                document = indexSearcher.doc(topDocs.scoreDocs[i].doc);
                String url = getDocUrlFromDocValues(topDocs.scoreDocs[i].doc);

                String title = document.get("title");
                String highLightTitle = highlighter.getBestFragment(
                        LuceneUtil.getAnalyzer(),"title",title);

                if(highLightTitle==null||"".equals(highLightTitle)){
                    highLightTitle = title;
                }

                String content = document.get("text");
                String highLightContent = highlighter.getBestFragment(
                        LuceneUtil.getAnalyzer(),"text",content);

                if(highLightContent==null||"".equals(highLightContent)){
                    content = document.get("description");
                    highLightContent = highlighter.getBestFragment(
                            LuceneUtil.getAnalyzer(),"description",content);
                }


                results[index++] = new SearchResult(highLightTitle,highLightContent,queryStr,url);
            }
            return results;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     *重写评分的实现方式
     * **/
    private class MyScoreProvider extends CustomScoreProvider {
        private LeafReaderContext context;

        public MyScoreProvider(LeafReaderContext context) {
            super(context);
            this.context = context;
        }

        private static final float QUERY_BOOST = 0.4f;

        private static final float PAGE_RANK_BOOST = 0.4f;

        private static final float ANONYMOUS_CLICK_BOOST = 0.1f;

        private static final float REAL_NAME_CLICK_BOOST = 0.1f;

        /**
         * 重写评分方法
         **/
        @Override
        public float customScore(int doc, float subQueryScore, float valSrcScore){
            String url = getDocUrlFromDocValues(doc);

            float prScore = pageRank.get(url).floatValue();

            int anonymousClickScore = 0;
            if(anonymousUrlClick!=null){
                UrlClick anonymousClick = anonymousUrlClick.get(url);
                if(anonymousClick!=null){
                    anonymousClickScore = anonymousClick.getAnonymousClick();
                }
            }


            int realNameClickScore = 0;
            if(realNameUrlClick!=null){
                UrlClick realNameClick = realNameUrlClick.get(url);
                if(realNameClick!=null){
                    realNameClickScore = realNameClick.getRealNameClick();
                }
            }

            return subQueryScore * QUERY_BOOST + prScore * PAGE_RANK_BOOST +
                    anonymousClickScore * ANONYMOUS_CLICK_BOOST +
                    realNameClickScore * REAL_NAME_CLICK_BOOST;
        }
    }
    /**
     * 重写CustomScoreQuery 的getCustomScoreProvider方法
     * 引用自定义的Provider
     */
    private class MyCustomScoreQuery extends CustomScoreQuery {

        public MyCustomScoreQuery(Query subQuery) {
            super(subQuery);
        }
        @Override
        protected CustomScoreProvider getCustomScoreProvider(
                LeafReaderContext context){
            return new MyScoreProvider(context);
        }
    }

    @PreDestroy
    public void closeReader() throws IOException {
        indexReader.close();
    }
}
