package com.wangyang.pageRank;

public class DispatchThread extends Thread{

    private PRResource prResource;

    //误差下限
    private double minDeviation;

    private double[] initPR;

    private double[] resultPR;

    //开启线程数量
    private static final int THREAD_SUM = 8;

    private Thread[] computeThreads;

    private Matrix matrix;


    public DispatchThread(Matrix matrix,double minDeviation,double[] initPR) {
        this.matrix = matrix;
        this.minDeviation = minDeviation;
        this.initPR = initPR;
    }

    public double[] getResultPR() {
        return resultPR;
    }

    public void init(){
        prResource = new PRResource(matrix,THREAD_SUM);
        prResource.init();
        prResource.setCurPR(initPR);

        computeThreads = new ComputeThread[THREAD_SUM];
        for (int i = 0; i < THREAD_SUM; i++) {
            computeThreads[i] = new ComputeThread(prResource,i);

        }
    }

    @Override
    public void run() {
        for(Thread thread : computeThreads){
            thread.start();
        }

        double error = 1;
        int count = 0;
        while (error >= minDeviation){
            try {
                error = prResource.custom();
            } catch (InterruptedException e) {
                return;
            }

            count++;
            System.out.println(count+":"+error);
        }

        for(Thread thread : computeThreads){
            thread.interrupt();
        }

        resultPR = prResource.getCurPR();
    }
}
