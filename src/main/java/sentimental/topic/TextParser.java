package sentimental.topic;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.tika.Tika;
import org.springframework.stereotype.Component;

import sentimental.topic.extractors.Topic;
import sentimental.topic.extractors.TopicResults;
import sentimental.topic.multithread.StringTask;
import sentimental.topic.multithread.ThreadController;

/**
 * The text parser that uses Apache Tika library
 * to read data of different formats.
 * Simplified implementation of MapReduce concept with 
 * the {@link StringTask} created and submitted to threadpool by
 * {@link ThreadController#newThread(Callable)}
 * acting as maps and {@link ThreadController#getResults(int)} acting as reduce 
 */

@Component
public class TextParser {

	/**
	 * The number of lines to be processed by 
	 * one task
	 */
	private int linesPerThread = 10;
	/**
	 * The Apache Tika field
	 */
	private Tika tika;

	public TextParser() {
		tika = new Tika();
	}
	
	public int getLinesPerThread() {
		return linesPerThread;
	}

	public void setLinesPerThread(int linesPerThread) {
		this.linesPerThread = linesPerThread;
	}

	/**
	    * Parsing a file.
	    * 
	    * @param file
	    *           the file to parse.
	    * @return a collection of topics wrapped in {@link TopicResults}.
	    * @throws IOException
	    *            If an error occurs while loading the file
	    * @throws InterruptedException
	    *            If the thread was interrupted
	    * @throws ExecutionException
	    *            If there was thread execution error           
	    */
	public TopicResults<Topic> readText(File file) throws IOException,
			InterruptedException, ExecutionException {
			return read(tika.parse(file));
	}

	/**
	    * Parsing a url.
	    * 
	    * @param url
	    *           the url to parse.
	    * @return a collection of topics wrapped in {@link TopicResults}.
	    * @throws IOException
	    *            If an error occurs while loading the file
	    * @throws InterruptedException
	    *            If the thread was interrupted
	    * @throws ExecutionException
	    *            If there was thread execution error           
	    */
	public TopicResults<Topic> readText(URL url) throws IOException,
			InterruptedException, ExecutionException {
			return read(tika.parse(url));
	}

	/**
	    * Parsing an InputStream.
	    * 
	    * @param in
	    *           the InputStream to be parsed.
	    * @return a collection of topics wrapped in {@link TopicResults}.
	    * @throws IOException
	    *            If an error occurs while loading the file
	    * @throws InterruptedException
	    *            If the thread was interrupted
	    * @throws ExecutionException
	    *            If there was thread execution error           
	    */
	public TopicResults<Topic> readText(InputStream in) throws IOException,
			InterruptedException, ExecutionException {
			return read(tika.parse(in));
	}

	/**
	    * The actual parser implementation.
	    * 
	    * @param in
	    *           the Reader to be parsed.
	    * @return a collection of topics wrapped in {@link TopicResults}.
	    * @throws IOException
	    *            If an error occurs while loading the file
	    * @throws InterruptedException
	    *            If the thread was interrupted
	    * @throws ExecutionException
	    *            If there was thread execution error           
	    */
	private TopicResults<Topic> read(Reader in) throws IOException,
			InterruptedException, ExecutionException {
		//controller for multithreading
		ThreadController<Topic> threadControl = new ThreadController<Topic>();
		StringBuilder builder = new StringBuilder();
		BufferedReader reader = new BufferedReader(in);
		String line;
		//the number of created tasks
		int count = 0;
		//lines iterator
		int it = 0;
		while ((line = reader.readLine()) != null) {
			it++;
			builder.append(line);
			builder.append(System.lineSeparator());
			//creating new task for the number of lines
			//specified by linesPerThread
			if (it >= linesPerThread) {
				count++;
				//finding the last incomplete sentence 
				// and everything before it
				String[] matches = sentencesFinder(builder.toString());
				if (matches != null) {
					threadControl.newThread(new StringTask(matches[0]));
					builder = new StringBuilder();
					//appending incomplete string
					builder.append(matches[1]);
				} else {
					threadControl.newThread(new StringTask(builder.toString()));
					builder = new StringBuilder();
				}
				it = 0;
			}
		}
		if (it > 0) {
			count++;
			threadControl.newThread(new StringTask(builder.toString()));
		}

		reader.close();

		//getting results from all tasks back
		TopicResults<Topic> results = threadControl.getResults(count);
		//shutting down the threadpool 
		threadControl.shutDown();

		return results;
	}

	/**
	    * Method for finding the last incomplete sentence
	    *  and everything before it.
	    * @param str
	    *           string to match.
	    * @return array consisting of 2 elements: 
	    * 			string with complete sentences
	    * 			and one incomplete remaining sentence
	    */
	private String[] sentencesFinder(String str) {
		Pattern pattern = Pattern.compile("(.*)(?<=[\\.!?])(.*)",
				Pattern.DOTALL);
		Matcher matcher = pattern.matcher(str);
		String match1 = null;
		String match2 = null;
		if (matcher.find()) {
			//string with complete sentences
			match1 = matcher.group(1);
			//remaining string
			match2 = matcher.group(2);
			return new String[] { match1, match2 };
		} else {
			return null;
		}
	}

}
