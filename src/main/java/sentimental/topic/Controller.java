package sentimental.topic;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import sentimental.topic.extractors.Topic;
import sentimental.topic.extractors.TopicExtractor;
import sentimental.topic.extractors.TopicsSpliter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The REST Controller for web access to the parser
 */

@RestController
public class Controller {
	
	private static final Logger logger = LoggerFactory.getLogger(Controller.class);

	@Autowired
	private TextParser provider;
	@Autowired
	private TopicExtractor<Topic> extractor;
	@Autowired
	TopicsSpliter ts;
	

	/**
	    * Parsing the text from request parameters.
	    * 
	    * @param input
	    *           the string to parse.
	    * @return Formated string of valuable information in JSON format.
	    */
	@RequestMapping(value = "/text", method = RequestMethod.POST, produces = "application/json")
	public String getTopicText(@RequestParam("text") String input, 
			@RequestParam(value = "maxTopics", defaultValue = "10") int maxTopics) {
		String text = null;
		try{
		ObjectMapper m = new ObjectMapper();
		text = m.writeValueAsString(
				ts.splitTopics(extractor.extract(input), maxTopics));
		} catch(JsonProcessingException e){
			logger.error("bad json conversion " + e.getMessage());
			text = "There was a problem with data conversion, please try again";
		}
		return text;
	}

	/**
	    * Parsing the text from multipart file,
	    * sent by POST request.
	    * 
	    * @param file
	    *           the file to parse.
	    * @return Formated string of valuable information in JSON format.
	    */
	@RequestMapping(value = "/file", method = RequestMethod.POST, produces = "application/json")
	public String getTopicFile(@RequestParam("file") MultipartFile file, 
			@RequestParam(value = "maxTopics", defaultValue = "10") int maxTopics) {
		String text = null;
		if (!file.isEmpty()) {
			try {
				ObjectMapper m = new ObjectMapper();
				text = m.writeValueAsString(
						ts.splitTopics(provider.readText(file.getInputStream()), maxTopics));
			} catch (IOException e) {
				logger.warn("IO problem " + e.getMessage());
				text = "There were problems with I/O, please try again";
			} catch (InterruptedException | ExecutionException e) {
				logger.error("Execution was interrupted " + e.getMessage());
				text = "Sorry, some problems occured during the execution "
						+ "of programm, please try again";
			}
		} else {
			text = "You failed to upload because the file was empty.";
		}
		return text;
	}

	/**
	    * Parsing the text from URL
	    * 
	    * @param urlStr
	    *           the url string to parse.
	    * @return Formated string of valuable information in JSON format.
	    */
	@RequestMapping(value = "/url", produces = "application/json")
	public String getTopicUrl(
			@RequestParam("url") String urlStr,
			@RequestParam(value = "maxTopics", defaultValue = "10") int maxTopics) {
		String text = null;
		URL url = null;
		if (!urlStr.isEmpty()) {
			try {
				url = new URL(urlStr);
				ObjectMapper m = new ObjectMapper();
				text = m.writeValueAsString(ts.splitTopics(
						provider.readText(url), maxTopics));
			} catch (MalformedURLException e) {
				logger.info("The url was malformed " + e.getMessage());
				text = "The format of url was wrong, please try again";
			} catch (IOException e) {
				logger.warn("IO problem " + e.getMessage());
				text = "There were problems with I/O, please try again";
			} catch (InterruptedException | ExecutionException e) {
				logger.error("Execution was interrupted " + e.getMessage());
				text = "Sorry, some problems occured during the execution "
						+ "of programm, please try again";
			}
		} else {
			return "Specify the url as a parameter!";
		}
		return text;
	}
}
