package com.wangyang.pageRank;

import java.util.concurrent.Semaphore;

public class PRResource {

    private double[] curPR;

    private double[] newPR;

    private int threadSum;

    private Semaphore[] producter;

    private Semaphore[] customer;

    private int[] colIndex;

    private Matrix matrix;

    public PRResource(Matrix matrix, int threadSum) {
        this.matrix = matrix;
        this.threadSum = threadSum;
    }

    public void init(){
        producter = new Semaphore[threadSum];
        for (int i = 0; i < threadSum; i++) {
            producter[i] = new Semaphore(1);
        }
        customer = new Semaphore[threadSum];
        for (int i = 0; i < threadSum; i++) {
            customer[i] = new Semaphore(0);
        }
        colIndex = new int[threadSum];
        newPR = new double[matrix.getMatrixLen()];
    }

    public double[] getCurPR() {
        return curPR;
    }

    public void setCurPR(double[] curPR) {
        this.curPR = curPR;
    }

    public double[] getNewPR() {
        return newPR;
    }

    public void setNewPR(double[] newPR) {
        this.newPR = newPR;
    }

    public void product(int order) throws InterruptedException {
        producter[order].acquire();

        colIndex[order] = order;
        while (colIndex[order] < matrix.getMatrixLen()) {
            newPR[colIndex[order]] = matrix.computeCol(curPR, colIndex[order]);
            colIndex[order] += threadSum;
        }
        customer[order].release();
    }

    public double custom() throws InterruptedException {
        for (Semaphore semaphore : customer) {
            semaphore.acquire();
        }
        double error = matrix.norm(curPR, newPR);
        curPR = newPR;
        newPR = new double[matrix.getMatrixLen()];
        for (Semaphore semaphore : producter) {
            semaphore.release();
        }
        return error;
    }
}
