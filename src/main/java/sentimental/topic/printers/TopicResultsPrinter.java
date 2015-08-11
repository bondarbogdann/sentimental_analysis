package sentimental.topic.printers;

import sentimental.topic.extractors.TopicResults;

/**
 * Interface for printing resulting string of Topics
 */
public interface TopicResultsPrinter<T> {
	/**
	    * Takes the collection of topics, returning relevant information.
	    * 
	    * @param results
	    *           results received from a parser.
	    * @return Formated string of valuable information.
	    */
    String print(TopicResults<T> results);
}
