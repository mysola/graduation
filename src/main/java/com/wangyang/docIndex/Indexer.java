package com.wangyang.docIndex;


import com.wangyang.docProcess.NormalizedDoc;
import com.wangyang.docProcess.NormalizedDocProcesser;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.DocValuesType;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class Indexer {

    private static final String indexDir = "/home/mysola/IdeaProjects/indexs/";

    private IndexWriter indexWriter;

    private NormalizedDocProcesser normalizedDocProcesser = new NormalizedDocProcesser();

    public Indexer() throws IOException {
        Directory dir = FSDirectory.open(Paths.get(indexDir));
        indexWriter = new IndexWriter(dir,new IndexWriterConfig(LuceneUtil.getAnalyzer()));
        normalizedDocProcesser.openDocFile();
    }

    private Document getDocument(NormalizedDoc normalizedDoc){
        Document doc = new Document();
        String description = normalizedDoc.getDescription();
        String title = normalizedDoc.getTitle();
        String text = normalizedDoc.getText();
        String url = normalizedDoc.getUrl();

        if(title!=null&&!"".equals(title)){
            doc.add(new TextField("title",title,Field.Store.YES));
        }

        if(description!=null&&!"".equals(description)){
            doc.add(new TextField("description", description, Field.Store.YES));
        }

        if(text!=null&&!"".equals(text)){
            doc.add(new TextField("text", text, Field.Store.YES));
        }

        if(url!=null&&!"".equals(url)){
            doc.add(new SortedDocValuesField("url",new BytesRef(url)));
            doc.add(new StoredField("url", url));
        }
        return doc;
    }

    public void index() throws IOException {
        long start = System.currentTimeMillis();
        NormalizedDoc normalizedDoc = null;
        Document document = null;
        int count = 0;
        while (normalizedDocProcesser.hasNext()){
            normalizedDoc = normalizedDocProcesser.nextDocData();
            document = getDocument(normalizedDoc);
            indexDoc(document);
            count++;
        }
        close();
        long end = System.currentTimeMillis();

        System.out.println("Indexing " + count + " files took "
                + (end - start) + " milliseconds");
    }

    private long indexDoc(Document document) throws IOException {
        return indexWriter.addDocument(document);
    }

    public void close() throws IOException {
        indexWriter.close();
    }
}
