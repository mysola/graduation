package com.wangyang.pageRank;

public class ComputeThread extends Thread{

    //线程序号
    private int order;

    //PR数组中间计算结果
    private PRResource prResource;


    public ComputeThread(PRResource prResource, int order) {
        this.prResource = prResource;
        this.order = order;
    }

    @Override
    public void run() {
        while (true) {
            try {
                prResource.product(order);
            } catch (InterruptedException e) {
                return;
            }
        }
    }
}
