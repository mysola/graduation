package com.wangyang.pageRank;

import com.wangyang.docProcess.NormalizedDocProcesser;

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

    private Set<Triplet> graph;

    //url映射到存储数组下标
    private Map<String,Integer> urlRefIndexMap;

    private String[] urlArray;

    public void buildGraph(){
        List<String> urls = normalizedDocProcesser.readAllNormalizedDocUrlList();
        urlRefIndexMap = new HashMap<String, Integer>(urls.size());
        urlArray = new String[urls.size()];
        int index = 0;
        for(String url : urls){
            urlArray[index] = url;
            urlRefIndexMap.put(url,index);
            index++;
        }
        graph = new TreeSet<Triplet>();
        for(String url : urls){
            List<String> innerUrls = normalizedDocProcesser.readNormalizedDocInnerUrl(url);
            Integer i = urlRefIndexMap.get(url);
            for(String innerUrl : innerUrls){
                Integer j = urlRefIndexMap.get(innerUrl);
                if(j!=null&&!j.equals(i)){
                    graph.add(new Triplet(i,j,1.0));
                }
            }
        }

    }

    public void test(){
        Random random = new Random();

    }

    public void computeSocre(){

    }
}
