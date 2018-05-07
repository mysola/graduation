package com.wangyang.pageRank;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wangyang.docProcess.NormalizedDocProcesser;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PageRanker {

    private NormalizedDocProcesser normalizedDocProcesser = new NormalizedDocProcesser();

    private Matrix matrix;

    private Map<String, Integer> urlRefIndexMap;

    private double[] pr;

    private static final String PR_PATH = "/home/mysola/IdeaProjects/pageRank.pr";

    private ObjectMapper mapper = new ObjectMapper();

    public void build() {
        List<String> urls = normalizedDocProcesser.readAllNormalizedDocUrlList();
        //url映射到存储数组下标
        urlRefIndexMap = new HashMap<>(urls.size());
        int index = 0;
        //设置　url数组以及url到数组索引的映射
        for (String url : urls) {
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
        pr = matrix.concurrentComputePR();
        System.out.println(System.currentTimeMillis()-a);

    }


    public void writePR() throws IOException {
        Map<String,Double> PRMap = new HashMap<>(urlRefIndexMap.size());
        for(Map.Entry<String,Integer> entry : urlRefIndexMap.entrySet()){
            PRMap.put(entry.getKey(),pr[entry.getValue()]);
        }
        BufferedWriter bw = new BufferedWriter(new FileWriter(PR_PATH));
        bw.write(mapper.writeValueAsString(PRMap));
        bw.close();
    }

    public Map<String,Double> readPR() throws IOException {

        BufferedReader bw = new BufferedReader(new FileReader(PR_PATH));

        Map<String,Double> PRMap = mapper.readValue(bw, new TypeReference<Map<String,Double>>() {
        });
        bw.close();
        return PRMap;
    }

}
