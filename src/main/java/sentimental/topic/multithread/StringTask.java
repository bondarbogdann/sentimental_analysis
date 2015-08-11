package sentimental.topic.multithread;

import java.util.concurrent.Callable;

import sentimental.topic.extractors.Topic;
import sentimental.topic.extractors.TopicResults;
import sentimental.topic.extractors.opennlp.OpenNLPExtractor;

/**
 * The specific task for parsing the input string in a thread
 * by {@link OpenNLPExtractor} 
 */
public final class StringTask implements Callable<TopicResults<Topic>>{
	
	//the input text to process
	private String input;
	
	public StringTask(String input) {
		this.input = input;
	}
	
	public void setInput(String input){
		this.input = input;
	}

	/**
	    * call to extract data with {@link OpenNLPExtractor}
	    * @return a collection of topics wrapped in {@link TopicResults}
	    */
	public TopicResults<Topic> call() {
		return new OpenNLPExtractor().extract(input);
	}

}
