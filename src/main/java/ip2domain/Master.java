package ip2domain;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Master {
    private String inFilePath;
    private String outFilePath;
    private int threadNum;
    private int maxRetryNum;

    private Map<String, String> ipDomainMap = new HashMap();
    private List<String> ipList = new ArrayList();
    private List<String>[] ipLists;
    private List<String>[] domainLists;

    public Master(Config config) {
        inFilePath = config.getInFilePath();
        outFilePath = config.getOutFilePath();
        threadNum = config.getThreadNum();
        maxRetryNum = config.getMaxRetryNum();

        ipLists = new List[threadNum];
        domainLists = new List[threadNum];
        for (int i = 0; i < threadNum; i++) {
            ipLists[i] = new ArrayList();
            domainLists[i] = new ArrayList();
        }
    }

    private void read() throws IOException {
        BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(new File(inFilePath)))
        );

        String s;
        int i = 0;
        try {
            while ((s = br.readLine()) != null) {
                ipDomainMap.put(s, null);
                ipLists[i % threadNum].add(s);
                ipList.add(s);
                i++;
            }
        } catch (IOException e) {
            throw e;
        } finally {
            try {
                br.close();
            } catch (IOException ignore) {
            }
        }
    }

    private void getDomains() throws InterruptedException {
        Thread[] workers = new Thread[threadNum];
        for (int i = 0; i < threadNum; i++) {
            workers[i] = new Worker(i, ipLists[i], domainLists[i], maxRetryNum);
        }
        for (int i = 0; i < threadNum; i++) {
            workers[i].start();
        }
        for (int i = 0; i < threadNum; i++) {
            workers[i].join();
        }
        for (int i = 0; i < threadNum; i++) {
            List<String> ips = ipLists[i];
            List<String> domains = domainLists[i];
            for (int j = 0; j < ips.size(); j++) {
                ipDomainMap.put(ips.get(j), domains.get(j));
            }
        }
    }

    private void write() throws IOException {
        BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(new File(outFilePath)))
        );

        try {
            for (String ip : ipList) {
                bw.write(ipDomainMap.get(ip));
                bw.write("\n");
            }
        } catch (IOException e) {
            throw e;
        } finally {
            try {
                bw.close();
            } catch (IOException ignore) {
            }
        }
    }

    void run() throws IOException, InterruptedException {
        read();
        getDomains();
        write();
    }
}
