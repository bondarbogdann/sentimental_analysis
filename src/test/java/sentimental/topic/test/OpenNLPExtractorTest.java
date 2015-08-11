package sentimental.topic.test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import sentimental.topic.extractors.Topic;
import sentimental.topic.extractors.TopicResults;
import sentimental.topic.extractors.opennlp.OpenNLPExtractor;

/**
 * Test fixture for {@link OpenNLPExtractor}
 */
public class OpenNLPExtractorTest {

	private OpenNLPExtractor extractor;
	private TopicResults<Topic> expectedResults;
	private String inputText;
	
	/**
	 * Expected values for {@link TopicResults}
	 * and initial input string.
	 */
	@Before
	public void init(){
		inputText = "This is the first sentence. And here is another one!";
		expectedResults = new TopicResults<Topic>();
		Topic phrase = new Topic();
		phrase.setLabel("first sentence");
		phrase.setPhrase(true);
		Topic noun = new Topic();
		noun.setLabel("one");
		expectedResults.addTopic(phrase);
		expectedResults.addTopic(noun);
	}
	
	/**
	 * Test method to verify the right parsing 
	 * of the input string.
	 */
	@Test
	public void extract() {
		extractor = new OpenNLPExtractor();
		TopicResults<Topic> results = extractor.extract(inputText);
		assertEquals("The results have different size.", 
				expectedResults.getSize(), results.getSize());
		
		assertEquals("The results don't match.", 
				expectedResults.getTopics(), results.getTopics());
	}

}
