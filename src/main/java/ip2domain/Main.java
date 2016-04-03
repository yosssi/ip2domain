package ip2domain;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
    private static Config parse(String[] args) throws CmdLineException {
        Config config = new Config();

        CmdLineParser parser = new CmdLineParser(config);

        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            parser.printUsage(System.err);
            System.err.println();
            throw e;
        }

        return config;
    }

    public static void main(String[] args) {
        try {
            Config config = parse(args);
            new Master(config).run();
        } catch (CmdLineException ignore) {
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
