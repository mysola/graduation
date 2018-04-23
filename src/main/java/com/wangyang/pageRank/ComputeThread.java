package com.wangyang.pageRank;

public class ComputeThread extends Thread{

    //线程序号
    private int order;

    private Matrix matrix;

    //PR数组中间计算结果
    private TempPRs tempPRs;

    private int threadSum;

    private int colIndex;

    private boolean waited = true;


    public ComputeThread(int order, Matrix matrix, int threadSum) {
        this.order = order;
        this.matrix = matrix;
        this.threadSum = threadSum;
    }

    public void setTempPRs(TempPRs tempPRs) {
        this.tempPRs = tempPRs;
    }


    public void resetColIndex(){
        colIndex = order;
    }

    public boolean isWaited() {
        return waited;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while (true) {

            while (colIndex < matrix.getMatrixLen()) {
                tempPRs.newPR[colIndex] = matrix.computeCol(tempPRs.curPR, colIndex);

                colIndex += threadSum;
            }
            if (tempPRs.curFlag.incrementAndGet()==threadSum) {
                synchronized (tempPRs) {
                    tempPRs.notify();
                }
            }

            synchronized (this) {
                try {
                    waited = true;
                    this.notify();
                    wait();
                    waited = false;
                } catch (InterruptedException e) {
                    System.out.println("线程"+order+"退出");
                }
            }
        }
    }
}
