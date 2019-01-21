package com.lchclearnet.fx;

import com.lchclearnet.request.SmartRequest;
import com.lchclearnet.request.SmartService;
import com.lchclearnet.utils.Tuple;
import org.apache.commons.cli.*;

import java.time.Duration;
import java.time.Instant;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class DemoHelper {

    public static Tuple parseArgs(String[] args) {
        try {
            Options options = new Options();
            options.addOption("conf", "configuration", true, "The configuration file used to setup the different services.");
            //options.addOption("date", "value_date", true, "Load the trade jandas from the given value date.");

            // parse the command line arguments
            CommandLineParser parser = new DefaultParser();
            CommandLine line = parser.parse(options, args);

            // has the buildfile argument been passed?
            if (line.hasOption("conf")) {
                // initialise the member variable
                String conf = line.getOptionValue("conf");
                //String date = line.getOptionValue("date");

                //Load the config service
                return Tuple.of(AppConfig.instance(conf));
            } else {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("Trade Service API", options);
            }
        } catch (ParseException ex) {
            // oops, something went wrong
            System.err.println("Parsing failed.  Reason: " + ex.getMessage());
        }
        return null;
    }


    public static <T> T execute_request(SmartService<SmartRequest, T> smartService, SmartRequest request, String requestName) {
        Instant start = Instant.now();
        T response = smartService.submit(request);
        Instant end = Instant.now();

        Duration duration = Duration.between(start, end);
        String msg =
                String.format("Execute Request[%s] in %ss %sms",
                        requestName != null && requestName.trim().length() > 0 ? String.format("%s", requestName) : "", duration.getSeconds(),
                        duration.getNano() / 1000_000);

        System.out.println(msg);
        return response;
    }

    public static <T> T executeS(Supplier<T> f, String requestName) {
        Instant start = Instant.now();
        T response = f.get();
        Instant end = Instant.now();

        Duration duration = Duration.between(start, end);
        String msg =
                String.format("Execute %s in %ss %sms",
                        requestName != null && requestName.trim().length() > 0 ? String.format("%s", requestName) : "", duration.getSeconds(),
                        duration.getNano() / 1000_000);

        System.out.println(msg);

        return response;
    }


    public static <T> void monitor(Consumer<T> f, T input, String message) {
        Instant start = Instant.now();
        f.accept(input);
        Instant end = Instant.now();

        Duration duration = Duration.between(start, end);
        String msg =
                String.format("%s in %ss %sms",
                        message != null && message.trim().length() > 0 ? String.format("%s", message) : "", duration.getSeconds(),
                        duration.getNano() / 1000_000);

        System.out.println(msg);
    }


    public static <T, U> U monitor(Function<T, U> f, T input, String message) {
        Instant start = Instant.now();
        U response = f.apply(input);
        Instant end = Instant.now();

        Duration duration = Duration.between(start, end);
        String msg =
                String.format("%s in %ss %sms",
                        message != null && message.trim().length() > 0 ? String.format("%s", message) : "", duration.getSeconds(),
                        duration.getNano() / 1000_000);

        System.out.println(msg);
        return response;
    }

}
