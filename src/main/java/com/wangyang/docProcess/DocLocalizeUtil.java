package com.wangyang.docProcess;

import java.io.File;
import java.io.FilenameFilter;

public class DocLocalizeUtil {


    //通过日志找对应的html文件
    public static File findFileByUrl(String url,String jobPath) {

        //以斜线分割url得到文件路径
        String[] filePath = url.split("/");
        //每一级路径后面是否跟有斜线（区分是文件还是文件夹）
        boolean[] isSlantFollow = new boolean[filePath.length];
        for(int i=0;i< isSlantFollow.length-1;i++){
            isSlantFollow[i] = true;
        }

        if(url.endsWith("/")){
            isSlantFollow[isSlantFollow.length-1] = true;
        }
        File mirror = new File(jobPath + File.separator + "mirror" );
        int filePathIndex = 0;
        File temp = mirror;
        if(mirror.isDirectory()){
            //依次处理每一级路径
            while(filePathIndex<filePath.length){
                for(File f : temp.listFiles()){
                    //如果某段url匹配文件名的某前缀
                    if(f.getName().startsWith(filePath[filePathIndex])){
                        //如果某段url后跟有斜线，对应一个文件夹
                        if(isSlantFollow[filePathIndex]&&f.isDirectory()){
                            temp = f;
                            break;
                        }
                        //如果某段url后不跟有斜线，对应一个文件
                        if(!isSlantFollow[filePathIndex]&&f.isFile()){
                            temp = f;
                            break;
                        }
                    }
                }
                filePathIndex++;
            }
            //最后一级路径对应的是文件，直接返回
            if(temp.isFile()){
                return temp;
            }
            //最后一级路径对应的是文件夹，在文件夹下寻找index.html文件或第一个文件，返回
            if(temp.isDirectory()){
                File[] listFiles = temp.listFiles(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String name) {
                        if(name.endsWith("index.html")){
                            return true;
                        }
                        return false;
                    }
                });
                if(listFiles.length>0){
                    return listFiles[0];
                }
            }
        }
        return null;
    }

}
