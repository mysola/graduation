package com.wangyang.pageRank;

import com.wangyang.docProcess.NormalizedDocProcesser;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

public class PageRanker {

    private NormalizedDocProcesser normalizedDocProcesser = new NormalizedDocProcesser();

    private Matrix matrix;


    public void testPR(){

        matrix = new Matrix(10000);
        matrix.setAlpha(0.1);

        Random random = new Random();
        for(int i=0;i<10000;i++){
            for (int j = 0; j <10000 ; j++) {
                if(i==j){
                    matrix.insertNode(i,j);
                }

            }
        }
        matrix.concurrentComputePR();

    }

    public void build() {
        List<String> urls = normalizedDocProcesser.readAllNormalizedDocUrlList();
        //url映射到存储数组下标
        Map<String, Integer> urlRefIndexMap = new HashMap<String, Integer>(urls.size());
        //url数组
        String[] urlArray = new String[urls.size()];
        int index = 0;
        //设置　url数组以及url到数组索引的映射
        for (String url : urls) {
            urlArray[index] = url;
            urlRefIndexMap.put(url, index);
            index++;
        }

        //插入边

        matrix = new Matrix(urls.size());
        //随机转移概率
        matrix.setAlpha(0.1);
        for (String url : urls) {
            List<String> innerUrls = normalizedDocProcesser.readNormalizedDocInnerUrl(url);
            Integer i = urlRefIndexMap.get(url);
            for (String innerUrl : innerUrls) {
                Integer j = urlRefIndexMap.get(innerUrl);
                if (j != null && !j.equals(i)) {
                    matrix.insertNode(i,j);
                }
            }
        }
        long a = System.currentTimeMillis();
        matrix.concurrentComputePR();
        System.out.println(System.currentTimeMillis()-a);

    }


}
