package com.wangyang.logReader;


import com.wangyang.logReader.LineInLog;

public class LogUtil {

    private static final int MIN_DOC_SIZE = 20*1024;


    //过滤日志文件
    public static boolean isCorrectDoc(LineInLog lineInLog){
        if(!"200".equals(lineInLog.getStatusCode())){
            return false;
        }
        if(lineInLog.getSize()<=MIN_DOC_SIZE){
            return false;
        }
        if(!lineInLog.getUrl().startsWith("ht")){
            return false;
        }
        return true;
    }

    public static boolean isLInkedType(LineInLog lineInLog) {
        return lineInLog.getRelation().contains("L");
    }
}
