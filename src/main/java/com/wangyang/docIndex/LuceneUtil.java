package com.wangyang.docIndex;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;

public class LuceneUtil {
    public static Analyzer getAnalyzer(){
        return new SmartChineseAnalyzer();
    }
}
