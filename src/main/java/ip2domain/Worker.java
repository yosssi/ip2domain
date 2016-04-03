package ip2domain;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

public class Worker extends Thread {
    private int no;
    private List<String> ipList;
    private List<String> domainList;
    private int maxRetryNum;

    public Worker(int no, List<String> ipList, List<String> domainList, int maxRetryNum) {
        this.no = no;
        this.ipList = ipList;
        this.domainList = domainList;
        this.maxRetryNum = maxRetryNum;
    }

    private String getDomain(String ip, int retryNum) {
        String domain = "";
        try {
            domain = InetAddress.getByName(ip).getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            if (retryNum < maxRetryNum) {
                domain = getDomain(ip, retryNum + 1);
            }
        }
        return domain;
    }

    @Override
    public void run() {
        for (String ip : ipList) {
            domainList.add(getDomain(ip, 0));
        }
    }
}
