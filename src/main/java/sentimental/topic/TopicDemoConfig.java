package sentimental.topic;

import java.io.File;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.FileConverter;

/**
 * Configuring the console arguments
 * for the input file and url
 */
public class TopicDemoConfig {
    @Parameter(names = "-input", converter = FileConverter.class)
    File file;
    @Parameter(names = "-url")
    String url;
}
