package com.wangyang;


import com.wangyang.docIndex.Indexer;
import com.wangyang.docProcess.DocProcessor;
import com.wangyang.logReader.CrawlLogReader;
import com.wangyang.logReader.LineInLog;
import com.wangyang.pageRank.PageRanker;
import com.wangyang.utils.LogUtil;

import java.io.File;

public class FlowController {
    private static final String jobRootPath = "/home/mysola/IdeaProjects/jobs";

    CrawlLogReader crawlLogReader = new CrawlLogReader();

    DocProcessor docProcessor = new DocProcessor();

    Indexer indexer;

    PageRanker pageRanker = new PageRanker();

    public static void main(String[] args) throws Exception {
    //    new FlowController().execute();
    }

    public void execute() throws Exception {

        File jobs = new File(jobRootPath);
        if (!jobs.isDirectory())
            throw new Exception("dir not found");
        String jobPath = null;
        //处理每个爬取的结果
        for (File job : jobs.listFiles()) {
            jobPath = job.getAbsolutePath();
            crawlLogReader.openNewReader(jobPath);
            LineInLog lineInLog = null;
            //读取爬虫日志文件
            while ((lineInLog = crawlLogReader.readLineOfLog()) != null) {
                //过滤日志文件
                if (LogUtil.isCorrectDoc(lineInLog)) {
                    //去掉http前缀
                    String url = lineInLog.getUrl();
                    if(url.startsWith("http://")){
                        url = url.substring(7, url.length());
                    }
                    //处理日志中url对应文档
                    docProcessor.processUrl(url,jobPath);
                }
            }
        }
    }
}
