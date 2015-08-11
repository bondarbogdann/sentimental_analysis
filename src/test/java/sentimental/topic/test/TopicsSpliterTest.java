package sentimental.topic.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.LinkedHashMap;

import org.junit.Before;
import org.junit.Test;

import sentimental.topic.extractors.Topic;
import sentimental.topic.extractors.TopicResults;
import sentimental.topic.extractors.Topics;
import sentimental.topic.extractors.TopicsSpliter;

/**
 * Test fixture for {@link TopicsSpliter}
 */
public class TopicsSpliterTest {

	private TopicResults<Topic> results;
	private TopicsSpliter spliter;
	
	/**
	 * Initial values for {@link TopicResults}
	 */
	@Before
	public void init(){
		results = new TopicResults<Topic>();
		Topic properNoun = new Topic();
		properNoun.setLabel("Proper");
		properNoun.setProper(true);
		Topic noun = new Topic();
		noun.setLabel("noun");
		Topic phrase = new Topic();
		phrase.setLabel("test phrase");
		phrase.setPhrase(true);
		results.addTopic(properNoun);
		results.addTopic(noun);
		results.addTopic(noun);
		results.addTopic(phrase);
	}

	/**
	 * Test method to verify the right 
	 * division into proper nouns, nouns and phrases.
	 */
	@Test
	public void testSplit() {
		spliter = new TopicsSpliter();
		HashMap<Topics, LinkedHashMap<String, Integer>> splitedResults =
				spliter.splitTopics(results, 2);
		LinkedHashMap<String, Integer> properNouns = 
				splitedResults.get(Topics.PROPER_NOUNS);
		LinkedHashMap<String, Integer> nouns = 
				splitedResults.get(Topics.NOUNS);
		LinkedHashMap<String, Integer> phrases = 
				splitedResults.get(Topics.PHRASES);
		for(Topic topic : results.getTopics()){
			if(topic.isPhrase()){
				assertTrue("Phrase was not included.", 
						phrases.containsKey(topic.getLabel()));
				assertEquals("The count of phrase is different", 
						results.getTopics().count(topic),
						phrases.get(topic.getLabel()).intValue());
			} else if(topic.isProper()){
				assertTrue("Proper noun was not included.", 
						properNouns.containsKey(topic.getLabel()));
				assertEquals("The count of proper noun is different", 
						results.getTopics().count(topic),
						properNouns.get(topic.getLabel()).intValue());
			} else{
				assertTrue("Noun was not included.", 
						nouns.containsKey(topic.getLabel()));
				assertEquals("The count of noun is different", 
						results.getTopics().count(topic),
						nouns.get(topic.getLabel()).intValue());
			}
		}
	}

}
