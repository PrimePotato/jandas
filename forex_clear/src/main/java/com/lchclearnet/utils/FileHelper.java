package com.lchclearnet.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Queue;

public class FileHelper {

    private static Logger logger = LogManager.getLogger();

    public static Queue<Path> find(Path directory, String filePattern, Queue<Path> paths) {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory, filePattern)) {
            for (Path entry : stream) {
                paths.offer(entry);
            }
        } catch (IOException ex) {
            String msg = String.format("error reading folder %s: %s", directory, ex.getMessage());
            throw new RuntimeException(msg, ex);
        }

        return paths;

    }

    /**
     * Open the specified resource - searches classpath first and then looks in the filesystem.
     *
     * @param name the name of the resource to find the url for
     * @return the url of the resource or throw a RuntimeException if the URL is not found.
     */
    public static URL getResource(String name) {
        URL url = Thread.currentThread().getContextClassLoader().getResource(name);

        if (url != null) return url;

        try {
            File file = new File(name);
            if (!file.exists()) {
                throw new RuntimeException(String.format("Cannot find resource '%s'.", name));
            } else {
                return file.toURL();
            }
        } catch (MalformedURLException muex) {
            throw new RuntimeException(String.format("Cannot build URL from resource '%s'.", name), muex);
        }
    }
}
