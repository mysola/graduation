package com.wangyang.pageRank;

public class ComputeThread extends Thread{

    //线程序号
    private int order;

    private Matrix matrix;

    //PR数组中间计算结果
    private TempPRs tempPRs;

    private int threadSum;

    private int colIndex;

    private volatile boolean waited = true;


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
        while (!tempPRs.stop) {

//            System.out.println(order+"------>1   cur:"+tempPRs.curFlag);
            while (colIndex < matrix.getMatrixLen()) {
                tempPRs.newPR[colIndex] = matrix.computeCol(tempPRs.curPR, colIndex);

                colIndex += threadSum;
            }
//            System.out.println(order+"------>2   cur:"+tempPRs.curFlag);
            if (tempPRs.curFlag.incrementAndGet()==threadSum) {
                synchronized (tempPRs) {
                    tempPRs.notify();
                }
            }

            synchronized (this) {
                try {
                    waited = true;
                    this.notify();
//                    System.out.println(order+"------>3   cur:"+tempPRs.curFlag);
                    wait(4000);
                    waited = false;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
//        System.out.println(order + "------退出");
    }
}
