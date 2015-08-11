package sentimental.topic.extractors;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.Multiset;

/**
 * The wrapper for the collection of topics
 * It is made generic to introduce
 * different models (topic representation)
 */
public class TopicResults<T>{
	
	//the multiset of topics
    private Multiset<T> topics = HashMultiset.create();

    /**
	    * Adding topic to the list
	    * @param topic
	    *           the topic model.
	    */
    public void addTopic(T topic) {
        topics.add(topic);
    }
    
    /**
	    * Adding the list of topics to the list
	    * @param topicList
	    *           the list of topics.
	    */
    public void addAllTopics(ImmutableMultiset<T> topicList){
    	topics.addAll(topicList);
    }

    /**
	    * Getting the topic list as ImmutableMultiset
	    * @return collection of topics
	    */
    public ImmutableMultiset<T> getTopics() {
        return ImmutableMultiset.copyOf(topics);
    }
    
    public int getSize(){
    	return topics.size();
    }
    
}
