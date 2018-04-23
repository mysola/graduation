package com.wangyang.pageRank;

import java.util.concurrent.atomic.AtomicInteger;

public class TempPRs {
    protected double[] newPR;

    protected double[] curPR;

    protected AtomicInteger curFlag = new AtomicInteger(0);

    protected volatile boolean stop;


}
