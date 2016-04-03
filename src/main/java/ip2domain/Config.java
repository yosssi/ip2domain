package ip2domain;

import org.kohsuke.args4j.Option;

public class Config {
    @Option(name = "-in", usage = "input file path", required = true)
    private String inFilePath;

    @Option(name = "-out", usage = "output file path", required = true)
    private String outFilePath;

    @Option(name = "-n", usage = "number of threads")
    private int threadNum = 1;

    @Option(name = "-r", usage = "number of max retries")
    private int maxRetryNum = 1;

    String getInFilePath() {
        return inFilePath;
    }

    String getOutFilePath() {
        return outFilePath;
    }

    int getThreadNum() {
        return threadNum;
    }

    int getMaxRetryNum() {
        return maxRetryNum;
    }
}
