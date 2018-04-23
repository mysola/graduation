package com.wangyang.pageRank;

import com.wangyang.docProcess.NormalizedDocProcesser;

import java.io.*;
import java.util.*;

public class PageRanker {

    private NormalizedDocProcesser normalizedDocProcesser = new NormalizedDocProcesser();

    private Matrix matrix;

    //url数组
    private String[] urlArray;

    private PrintStream ps;

//    {
//        try {
//            ps = new PrintStream(new File("/home/mysola/IdeaProjects/testPR"));
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//    }

    public void testPR() throws FileNotFoundException {

//        matrix = new Matrix(10000);
//        matrix.setAlpha(0.1);
//
//        Random random = new Random();
//        for(int i=0;i<1000000;i++){
//            int j = random.nextInt(10000);
//            int k = random.nextInt(10000);
//            matrix.insertNode(j,k);
//        }
//        matrix.computePR(System.out);

        Scanner sc = new Scanner(new File("/home/mysola/IdeaProjects/testPR"));
        int nodeSum = Integer.valueOf(sc.nextLine());

        matrix = new Matrix(nodeSum);
        matrix.setAlpha(0.1);
        while(sc.hasNext()){
            String[] strings = sc.nextLine().split(" ");
            matrix.insertNode(Integer.valueOf(strings[0]),Integer.valueOf(strings[1]));
        }
        matrix.concurrentComputePR(System.out);
    }

    public void build() {
        List<String> urls = normalizedDocProcesser.readAllNormalizedDocUrlList();
        //url映射到存储数组下标
        Map<String, Integer> urlRefIndexMap = new HashMap<String, Integer>(urls.size());
        urlArray = new String[urls.size()];
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
//        long a = System.currentTimeMillis();
//        matrix.serialComputePR(System.out);
//        System.out.println(System.currentTimeMillis()-a);

        long a = System.currentTimeMillis();
        matrix.concurrentComputePR(System.out);
        System.out.println(System.currentTimeMillis()-a);
    }


}
