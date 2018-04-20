package com.wangyang.logReader;

import java.io.*;

public class CrawlLogReader {
    private static final String subPathOfLog = File.separator + "logs" + File.separator + "crawl.log";

    private BufferedReader LogReader;

    public void openNewReader(String jobPath) throws IOException {
        if(LogReader!=null){
            LogReader.close();
        }
        LogReader = new BufferedReader(new FileReader(jobPath+subPathOfLog));
    }

    public void close() throws IOException {
        if(LogReader!=null){
            LogReader.close();
        }
    }

    public LineInLog readLineOfLog() throws IOException {
        String lineInLogStr = LogReader.readLine();
        if(lineInLogStr==null){
            return null;
        }
        else {
            LineInLog lineInLog = new LineInLog();
            String[] temp = lineInLogStr.split("\\s+");
            lineInLog.setStatusCode(temp[1]);
            if("-".equals(temp[2])){
                lineInLog.setSize(-1);
            }
            else {
                lineInLog.setSize(Integer.valueOf(temp[2]));
            }
            lineInLog.setUrl(temp[3]);
            lineInLog.setRelation(temp[4]);
            lineInLog.setDocType(temp[6]);
            return lineInLog;
        }
    }

}
