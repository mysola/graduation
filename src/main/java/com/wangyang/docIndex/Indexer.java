package com.wangyang.docIndex;


import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.IOException;

public class Indexer {

    private static final float TITLE_BOOST = 1F;

    private static final float DESCRI_BOOST = 1F;
    private static final float KEYWORD_BOOST = 1F;

    private static final String indexDir = "/home/mysola/IdeaProjects/indexs";

    private static final Version version = Version.LUCENE_6_6_1;

    private IndexWriter indexWriter;

    public Indexer() throws IOException {
//        Directory dir = FSDirectory.open(new File(indexDir).toPath());
//        indexWriter = new IndexWriter(dir,new IndexWriterConfig(new StandardAnalyzer()));
    }

    public long indexDoc(DocText docText) throws IOException {
        Document doc = new Document();
        String keywords = docText.getKeywords();
        String description = docText.getDescription();
        if(keywords!=null&&!"".equals(keywords)){
            Field field = new TextField("keywords", keywords, Field.Store.NO);
            doc.add(field);
        }
        if(description!=null&&!"".equals(description)){
            Field field = new TextField("description", description, Field.Store.NO);
            field.setBoost(DESCRI_BOOST);
            doc.add(field);
        }
        Field textField = new TextField("title",docText.getTitle(),Field.Store.NO);
        textField.setBoost(TITLE_BOOST);
        doc.add(textField);

        doc.add(new StringField("url",docText.getTitle(),Field.Store.NO));

        doc.add(new TextField("text",docText.getBody(),Field.Store.NO));
        return indexWriter.addDocument(doc);
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            throw new IllegalArgumentException("Usage: java " + Indexer.class.getName()
                    + " <index dir> <data dir>");
        }
        String indexDir = args[0];         //1
        String dataDir = args[1];          //2

//        long start = System.currentTimeMillis();
//        Indexer indexer = new Indexer();
//        int numIndexed;
//        try {
//            numIndexed = indexer.index(dataDir);
//        } finally {
//            indexer.close();
//        }
//        long end = System.currentTimeMillis();
//
//        System.out.println("Indexing " + numIndexed + " files took "
//                + (end - start) + " milliseconds");
    }


}
