package com.wangyang.pageRank;

import com.wangyang.docProcess.NormalizedDocProcesser;
import org.omg.PortableInterceptor.INACTIVE;

import java.util.*;

public class PageRanker {

    private NormalizedDocProcesser normalizedDocProcesser = new NormalizedDocProcesser();

    private class Triplet implements Comparable{
        private Integer i;
        private Integer j;
        private double score;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Triplet triplet = (Triplet) o;

            if (i != null ? !i.equals(triplet.i) : triplet.i != null) return false;
            return j != null ? j.equals(triplet.j) : triplet.j == null;
        }

        @Override
        public int hashCode() {
            int result = i != null ? i.hashCode() : 0;
            result = 31 * result + (j != null ? j.hashCode() : 0);
            return result;
        }

        public Integer getI() {
            return i;
        }

        public void setI(Integer i) {
            this.i = i;
        }

        public Integer getJ() {
            return j;
        }

        public void setJ(Integer j) {
            this.j = j;
        }

        public double getScore() {
            return score;
        }

        public void setScore(double score) {
            this.score = score;
        }

        public Triplet(Integer i, Integer j, double score) {

            this.i = i;
            this.j = j;
            this.score = score;
        }

        @Override
        public int compareTo(Object o) {
            if(j<((Triplet)o).getJ()){
                return -1;
            }
            else if(j>((Triplet)o).getJ()){
                return 1;
            }
            else {
                return i-((Triplet)o).getI();
            }
        }
    }

    //三元组存储稀疏矩阵，按主列次行的优先级排列
    private Triplet[] graph;

    //三元组每列开始处的索引
    private int[] outerIndexArray;

    //url映射到存储数组下标
    private Map<String,Integer> urlRefIndexMap;

    private String[] urlArray;


    public void buildGraph() {
        List<String> urls = normalizedDocProcesser.readAllNormalizedDocUrlList();
        urlRefIndexMap = new HashMap<String, Integer>(urls.size());
        urlArray = new String[urls.size()];
        int index = 0;
        //插入点
        for (String url : urls) {
            urlArray[index] = url;
            urlRefIndexMap.put(url, index);
            index++;
        }
        //插入边
        /*
        从无到有序再到转随机访问数据结构：
        １．treeset 插入－排序循环　　toArray
        2。linkedList 插入－插入循环　去重 排序（toArray 排序　toList）toArray
         */
        Set<Triplet> tempSet = new TreeSet<Triplet>();
        int k = 0;
        for (String url : urls) {
            System.out.println(k++);
            List<String> innerUrls = normalizedDocProcesser.readNormalizedDocInnerUrl(url);
            Integer i = urlRefIndexMap.get(url);
            for (String innerUrl : innerUrls) {
                Integer j = urlRefIndexMap.get(innerUrl);
                if (j != null && !j.equals(i)) {
                    tempSet.add(new Triplet(i, j, 1.0));
                }
            }
        }

        //转换随机访问数据结构,记录外部索引
        outerIndexArray = new int[urlArray.length];
        graph = new Triplet[tempSet.size()];
        int graphIndex = 0, outIndex = 0, lastColIndex = -1;
        for (Triplet triplet : tempSet) {
            //复制元素toArray
            graph[graphIndex] = triplet;
            //若当前列不存在元素
            while (outIndex < triplet.getJ()) {
                outerIndexArray[outIndex++] = -1;
            }
            //若当前列索引与上一列索引不同，说明是新一列的开头，记录当前元素索引
            if (lastColIndex < triplet.getJ()) {
                outerIndexArray[outIndex++] = graphIndex;
                lastColIndex = triplet.getJ();
            }
            graphIndex++;
        }
    }


    public void computeSocre(){

    }
}
