package sentimental.topic;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import sentimental.topic.extractors.Topic;
import sentimental.topic.extractors.TopicResults;
import sentimental.topic.printers.TopicResultsPrinter;

import com.google.common.base.Stopwatch;

@Component
public class DemoImpl implements Demo {

	private static final Logger logger = LoggerFactory
			.getLogger(DemoImpl.class);

	@Autowired
	private TextParser provider;
	@Autowired
	private TopicResultsPrinter<Topic> printer;

	/**
	 * @inheritDoc
	 *
	 * This implementation parses the provided 
	 * file or url but not both
	 */
	public void runDemo(TopicDemoConfig config) {
		TopicResults<Topic> results = null;
		//measuring the time of processing 
		Stopwatch stopwatch = Stopwatch.createStarted();
		try {
			if(config.file != null && config.url != null){
				System.out.println("Specify either file or url, not both!");
				System.exit(0);
			} else if (config.file != null && config.file.isFile()) {
				results = provider.readText(config.file);
				System.out.println("The time of proccesing: " + stopwatch.stop());
			} else if (config.url != null && !config.url.isEmpty()) {
				try {
					URL url = new URL(config.url);
					results = provider.readText(url);
					System.out.println("The time of proccesing: " + stopwatch.stop());
				} catch (MalformedURLException e) {
					logger.info("The url was malformed " + e.getMessage());
					System.out.println("The format of url was wrong, please try again");
				}
			} else {
				System.out.println("Specify file or url to process, or use REST API");
			}

			printer.print(results);
		} catch (IOException e) {
			logger.warn("IO problem " + e.getMessage());
			System.out.println("There were problems with I//O, please try again");
		} catch (InterruptedException | ExecutionException e) {
			logger.error("Execution was interrupted " + e.getMessage());
			System.out.println("Sorry, some problems occured during the execution "
							+ "of programm, please try again");
		}
	}
}
