package sentimental.topic.printers;

import java.util.HashMap;
import java.util.LinkedHashMap;

import org.springframework.stereotype.Component;

import sentimental.topic.extractors.Topic;
import sentimental.topic.extractors.TopicResults;
import sentimental.topic.extractors.Topics;
import sentimental.topic.extractors.TopicsSpliter;

/**
 * Simple implementation of {@link TopicResultsPrinter} 
 */

@Component
public class SimplePrinter implements TopicResultsPrinter<Topic> {
	
	private int topicFrequency = 10;
	
	public int getTopicFrequency() {
		return topicFrequency;
	}

	public void setTopicFrequency(int topicFrequency) {
		this.topicFrequency = topicFrequency;
	}

	/**
	 * @inheritDoc
	 *
	 * This implementation prints most 
	 * frequent proper nouns, nouns and phrases
	 */
    public String print(TopicResults<Topic> results) {
		
    	if(results == null || results.getSize() == 0)
    		return "There are no results available";
    				
		StringBuilder sb = new StringBuilder();
		TopicsSpliter ts = new TopicsSpliter();
		
		HashMap<Topics, LinkedHashMap<String, Integer>> splitedTopics = 
				ts.splitTopics(results, topicFrequency);
		
    	sb.append("The most frequent proper nouns:" + System.lineSeparator());
        sb.append(output(splitedTopics.get(Topics.PROPER_NOUNS)) + System.lineSeparator());
    	sb.append("The most frequent nouns:" + System.lineSeparator());
        sb.append(output(splitedTopics.get(Topics.NOUNS)) + System.lineSeparator());
        sb.append("The most frequent phrases:" + System.lineSeparator());
        sb.append(output(splitedTopics.get(Topics.PHRASES)));
        System.out.println(sb.toString());
        return sb.toString();
    }
    
    /**
	    * Helper method for iterating through the topics.
	    * 
	    * @param topics
	    *           specific topics to be iterated on.
	    * @return list of most frequent topics.
	    */
    private String output(LinkedHashMap<String, Integer> topics) {
    	StringBuilder s = new StringBuilder();
    	for (String topic : topics.keySet()) {
    		String times = topics.get(topic) > 1 ? " times" : " time";
            s.append(topic + " - " + topics.get(topic) + times + System.lineSeparator());
        }
        return s.toString();
    }
}
