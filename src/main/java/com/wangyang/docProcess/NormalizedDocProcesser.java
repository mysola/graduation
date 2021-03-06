package com.wangyang.docProcess;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class NormalizedDocProcesser {
    private static final String DOC_WRITE_DIR = "/home/mysola/IdeaProjects/normalizedDocs";

    private static final String DOC_DEFAULT_NAME = "default";

    private static final String DOC_DATA_NAME = "data";

    private static final String DOC_URLS_NAME = "urls";

    private ObjectMapper mapper = new ObjectMapper();

    public void writeNormalizedDoc(NormalizedDoc normalizedDoc) {

        String url = normalizedDoc.getUrl();
        int firstSlantIndex = url.indexOf("/");
        String domain = url.substring(0, firstSlantIndex);
        String fileName = url.substring(firstSlantIndex + 1, url.length())
                .replaceAll("/", "*");
        if ("".equals(fileName)) {
            fileName = DOC_DEFAULT_NAME;
        }
        File file = new File(DOC_WRITE_DIR + File.separator + domain + File.separator + fileName);

        if (file.mkdirs()) {
            File data = new File(file.getAbsolutePath() + File.separator + DOC_DATA_NAME);
            try {
                if (data.createNewFile()) {
                    BufferedWriter bw = new BufferedWriter(new FileWriter(data));
                    bw.write(mapper.writeValueAsString(normalizedDoc));
                    bw.close();
                }
                File urls = new File(file.getAbsolutePath() + File.separator + DOC_URLS_NAME);
                if (urls.createNewFile()) {
                    BufferedWriter bw = new BufferedWriter(new FileWriter(urls));
                    bw.write(mapper.writeValueAsString(normalizedDoc.getInnerUrls()));
                    bw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private File rootDir;

    private File[] domains;

    private String[] docs;

    private int domainIndex;

    private int docIndex;

    public void openDocFile() {
        rootDir = new File(DOC_WRITE_DIR);
        domains = rootDir.listFiles();
        domainIndex = 0;
        docIndex = 0;
        docs = domains[domainIndex].list();
    }

    public boolean hasNext() {

        return domains != null && domainIndex < domains.length &&
                docs != null && docIndex < docs.length;
    }

    public NormalizedDoc nextDocData() throws IOException {
        NormalizedDoc normalizedDoc = null;
        if (hasNext()) {
            File target = new File(domains[domainIndex].getAbsolutePath() +
                    File.separator + docs[docIndex] + File.separator + DOC_DATA_NAME);

            normalizedDoc = mapper.readValue(target, new TypeReference<NormalizedDoc>() {
            });
            if (docIndex + 1 < docs.length) {
                docIndex++;
            } else {
                docIndex = 0;
                domainIndex++;
                while (domainIndex < domains.length) {
                    File tmp = domains[domainIndex];
                    String[] filenames = tmp.list();
                    if (filenames != null && filenames.length > 0) {
                        break;
                    } else {
                        domainIndex++;
                    }
                }
                if(domainIndex<domains.length){
                    docs = domains[domainIndex].list();
                }
                else {
                    docs = null;
                }
            }

            return normalizedDoc;
        } else {
            return null;
        }
    }

    public List<String> readNormalizedDocInnerUrl(String url) {
        int firstSlantIndex = url.indexOf("/");
        String domain = url.substring(0, firstSlantIndex);
        String fileName = url.substring(firstSlantIndex + 1, url.length())
                .replaceAll("/", "*");
        if ("".equals(fileName)) {
            fileName = DOC_DEFAULT_NAME;
        }
        File target = new File(DOC_WRITE_DIR + File.separator + domain +
                File.separator + fileName + File.separator + DOC_URLS_NAME);
        List<String> urlList = new LinkedList<String>();
        if (target.exists()) {
            try {
                urlList = mapper.readValue(target, new TypeReference<List<String>>() {
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return urlList;
    }

    public List<String> readAllNormalizedDocUrlList() {
        List<String> urlList = new LinkedList<>();
        File rootDir = new File(DOC_WRITE_DIR);
        String url = null;
        for (File domain : rootDir.listFiles()) {
            for (String filename : domain.list()) {
                if (DOC_DEFAULT_NAME.equals(filename)) {
                    url = domain.getName() + "/";
                } else {
                    url = domain.getName() + "/" + filename.replaceAll("\\*", "/");
                }
                urlList.add(url);
            }
        }
        return urlList;
    }
}
