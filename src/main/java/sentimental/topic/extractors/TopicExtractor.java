package sentimental.topic.extractors;

/**
 * Interface for extracting the topics from 
 * the input string
 */

public interface TopicExtractor<T> {
	/**
	    * Takes the input string, returning the list of topics.
	    * 
	    * @param inputText
	    *           the string to be parsed.
	    * @return a collection of topics wrapped in {@link TopicResults}
	    */
    TopicResults<T> extract(String inputText);
}
