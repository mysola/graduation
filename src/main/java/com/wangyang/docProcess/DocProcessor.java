package com.wangyang.docProcess;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DocProcessor {

    private Pattern p = Pattern.compile("(?<=charset=)(.+)(?=\")");

    private static final int MAX_BYTE_SIEZ_IN_HTML_HEAD = 2*1024;

    private static final String[] travelKeywords = {"旅游","自助游","团队游","自驾游","周边游"};

    private NormalizedDocProcesser normalizedDocProcesser = new NormalizedDocProcesser();

    public void processUrl(String url,String jobPath) throws IOException {
        File docFile = DocLocalizeUtil.findFileByUrl(url,jobPath);
        if (docFile != null) {
            //解析文档头
            NormalizedDoc normalizedDoc = pasreHeadOfDoc(docFile);
            //若文档是旅游相关
            if(normalizedDoc!=null&&isDocAboutTravel(normalizedDoc)){
                normalizedDoc.setUrl(url);
                //重新解析
                BufferedReader br = new BufferedReader(new FileReader(docFile));
                Document doc = Jsoup.parse(docFile,normalizedDoc.getCharset());
                //移除隐藏元素
                removeHideElements(doc);
                String[] urls = extractUrlsOfDoc(doc,url);
                String text = extractTextOfDoc(doc);
                normalizedDoc.setInnerUrls(urls);
                normalizedDoc.setText(text);
                //写入规格化文档
                normalizedDocProcesser.writeNormalizedDoc(normalizedDoc);
            }
        }

    }



    public static boolean isDocAboutTravel(NormalizedDoc NormalizedDoc) {
        String keywords = NormalizedDoc.getKeywords();
        String description = NormalizedDoc.getDescription();
        String title = NormalizedDoc.getTitle();
        for(String travelKeyword : travelKeywords){
            if(keywords!=null&&!"".equals(keywords)){
                if(keywords.replaceAll("携程旅游","").contains(travelKeyword)){
                    return true;
                }
            }
            if(description!=null&&!"".equals(description)){
                if(description.replaceAll("携程旅游","").contains(travelKeyword)){
                    return true;
                }
            }
            if(title!=null&&!"".equals(title)){
                if(title.replaceAll("携程旅游","").contains(travelKeyword)){
                    return true;
                }
            }
        }
        return false;
    }

    private String[] extractUrlsOfDoc(Document doc,String rootUrl) {
        Element body = doc.body();
        Elements links = body.select("a");
        String[] hrefs = new String[links.size()];
        int index = 0;
        for (Element element : links) {
            String href = element.attr("href");

            if(href.startsWith("http://")){
                href = href.substring(7,href.length());
            }
            else if(href.startsWith("//")){
                href = href.substring(2,href.length());
            }
            else if(href.startsWith("/")){
                if(rootUrl.endsWith("/")){
                    href = rootUrl.substring(0,rootUrl.length()-1)+href;
                }
                else {
                    href = rootUrl+href;
                }
            }
            else {
                continue;
            }
            hrefs[index++] = href;
        }
        return hrefs;
    }

    private void removeHideElements(Document doc){
        Element body = doc.body();
        Elements hideElements = body.select("[style=display:none]");
        for(Element element: hideElements ){
            element.remove();
        }
    }

    private String extractTextOfDoc(Document doc) {
        return doc.body().text();
    }

    private NormalizedDoc pasreHeadOfDoc(File docFile) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(docFile));
        //存放HTML文档前固定长度的内容
        byte[] buffer = new byte[MAX_BYTE_SIEZ_IN_HTML_HEAD];
        bis.read(buffer,0,buffer.length);
        bis.close();

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buffer);
        //解析前固定长度的内容,默认utf-8解析
        Document doc = Jsoup.parse(byteArrayInputStream,"utf-8","");
        byteArrayInputStream.close();

        //找出文档编码
        Elements eles = doc.select("meta[http-equiv=Content-Type]");
        Matcher m = p.matcher(eles.toString());
        if (m.find()){
            String charset = "utf-8";
            //如果文档编码不为utf-8，重新解码
            if(!m.group().equalsIgnoreCase("utf-8")){
                byteArrayInputStream.reset();
                doc = Jsoup.parse(byteArrayInputStream,m.group(),"");
                charset = m.group();
            }
            String keywords = doc.select("meta[name=keywords]").attr("content");
            String description = doc.select("meta[name=keywords]").attr("content");
            String title = doc.title();
            return new NormalizedDoc(keywords,description,title,charset);
        }
        else {
            return null;
        }
    }


}
