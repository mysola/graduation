package com.wangyang.pageRank;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Set;
import java.util.TreeSet;

public class Matrix {

    private class Node implements Comparable {
        private Integer i;
        private Integer j;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Node node = (Node) o;

            if (i != null ? !i.equals(node.i) : node.i != null) return false;
            return j != null ? j.equals(node.j) : node.j == null;
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

        public Node(Integer i, Integer j) {

            this.i = i;
            this.j = j;
        }

        @Override
        public int compareTo(Object o) {
            if (j < ((Node) o).getJ()) {
                return -1;
            } else if (j > ((Node) o).getJ()) {
                return 1;
            } else {
                return i - ((Node) o).getI();
            }
        }
    }

    //存储稀疏矩阵，按主列次行的优先级排列
    private Node[] graph;

    //每列开始处在graph中的索引
    private int[] newColIndexArrayInGraph;

    //矩阵每行非零数量
    private int[] sumPerRow;

    //矩阵边长
    private int matrixLen;

    //全为零行修正后的值
    private double OneOfLength;

    //不全为零行零值修正后的值
    private double alphaOfLength;

    //不全为零行非零值修正后的值
    private double[] amendedNonzero;

    //随机转移概率
    private double alpha;

    //误差下限
    private double minDeviation;

    private Set<Node> tmpSet = new TreeSet<Node>();


    public void insertNode(int i,int j){
        tmpSet.add(new Node(i,j));
        sumPerRow[i]++;
    }

    public double[] computePR(PrintStream ps){
        buildMatrix();
        preMatrix();
        return computeFinalPR(ps);
    }

    /*
       从无到有序再到转随机访问数据结构：
       １．treeset 插入－排序循环　　toArray
       2。linkedList 插入－插入循环　去重 排序（toArray 排序　toList）toArray
        */
    private void buildMatrix(){
        //转换随机访问数据结构,记录外部索引
        newColIndexArrayInGraph = new int[matrixLen];
        graph = new Node[tmpSet.size()];
        int graphIndex = 0, arrayIndex = 0, lastColIndex = -1;
        for (Node node : tmpSet) {
            //复制元素toArray
            graph[graphIndex] = node;
            //若当前列不存在元素
            while (arrayIndex < node.getJ()) {
                newColIndexArrayInGraph[arrayIndex++] = -1;
            }
            //若当前列索引与上一列索引不同，说明是新一列的开头，记录当前元素索引
            if (lastColIndex < node.getJ()) {
                newColIndexArrayInGraph[arrayIndex++] = graphIndex;
                lastColIndex = node.getJ();
            }
            graphIndex++;
        }
        tmpSet = null;
    }

    private void preMatrix() {
        minDeviation = Math.pow(0.1,String.valueOf(matrixLen).length());
        OneOfLength = 1.0 / matrixLen;
        alphaOfLength = alpha / matrixLen;
        double restOfAlphaInOne = 1 - alpha;
        amendedNonzero = new double[matrixLen];
        for (int i = 0; i < matrixLen; i++) {
            amendedNonzero[i] = restOfAlphaInOne / sumPerRow[i] + alphaOfLength;
        }
    }

    private double[] computeFinalPR(PrintStream ps){
        int count = 0;
        double[] initPR = new double[matrixLen];
        for(int i=0;i<matrixLen;i++){
            initPR[i] = 1.0/matrixLen;
        }

        double[] tmpPR = null;
        double error = 1;
        while (error>=minDeviation) {
            ps.print(count + ":"+error +"  ");
            tmpPR = initPR;
            initPR = computeNextPR(initPR);
            error = norm(initPR,tmpPR);
//            for (double d : initPR) {
//                ps.print(d + " ");
//            }
            ps.println();
            count++;
        }
        return initPR;
    }

    //计算两向量之间的差别
    private double norm(double[] a,double[] b){
        double norm = 0;
        for (int i=0;i<a.length;i++){
            norm+=Math.abs(a[i]-b[i]);
        }
        return norm;
    }

    //矩阵相乘
    private double[] computeNextPR(double[] curPR) {
        double[] newPR = new double[matrixLen];
        int offset = 0, newColIndex = 0;
        double tempSum = 0.0;
        Node node = null;
        for (int col = 0; col < newPR.length; col++) {
            //newPR第col列的值
            tempSum = 0;
            //稀疏矩阵中第col列起点
            newColIndex = newColIndexArrayInGraph[col];
            //稀疏矩阵中第col列非零数偏移
            offset = 0;
            //下一个待计算的非零节点
            node = newColIndex == -1 ? null : graph[newColIndex + offset];
            for (int i = 0; i < curPR.length; i++) {
                if (node != null && node.getI() == i) {
                    tempSum += amendedNonzero[i] * curPR[i];
                    offset++;
                    if (newColIndex + offset < graph.length && graph[newColIndex + offset].getJ() == col) {
                        node = graph[newColIndex + offset];
                    } else {
                        node = null;
                    }
                } else {
                    if (sumPerRow[i] == 0) {
                        tempSum += OneOfLength * curPR[i];
                    } else {
                        tempSum += alphaOfLength * curPR[i];
                    }
                }
            }
            newPR[col] = tempSum;
        }
        return newPR;
    }

    public Matrix(int matrixLen) {
        this.matrixLen = matrixLen;
        sumPerRow = new int[matrixLen];
    }

    public Node[] getGraph() {
        return graph;
    }

    public void setGraph(Node[] graph) {
        this.graph = graph;
    }

    public double getAlpha() {
        return alpha;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    public double getMinDeviation() {
        return minDeviation;
    }

    public void setMinDeviation(double minDeviation) {
        this.minDeviation = minDeviation;
    }

    public int[] getNewColIndexArrayInGraph() {
        return newColIndexArrayInGraph;
    }

    public void setNewColIndexArrayInGraph(int[] newColIndexArrayInGraph) {
        this.newColIndexArrayInGraph = newColIndexArrayInGraph;
    }

    public int[] getSumPerRow() {
        return sumPerRow;
    }

    public void setSumPerRow(int[] sumPerRow) {
        this.sumPerRow = sumPerRow;
    }

    public int getMatrixLen() {
        return matrixLen;
    }

    public void setMatrixLen(int matrixLen) {
        this.matrixLen = matrixLen;
    }

    public double getOneOfLength() {
        return OneOfLength;
    }

    public void setOneOfLength(double oneOfLength) {
        OneOfLength = oneOfLength;
    }

    public double getAlphaOfLength() {
        return alphaOfLength;
    }

    public void setAlphaOfLength(double alphaOfLength) {
        this.alphaOfLength = alphaOfLength;
    }

    public double[] getAmendedNonzero() {
        return amendedNonzero;
    }

    public void setAmendedNonzero(double[] amendedNonzero) {
        this.amendedNonzero = amendedNonzero;
    }
}
