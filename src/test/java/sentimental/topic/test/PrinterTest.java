package sentimental.topic.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import sentimental.topic.extractors.Topic;
import sentimental.topic.extractors.TopicResults;
import sentimental.topic.printers.SimplePrinter;
import sentimental.topic.printers.TopicResultsPrinter;

/**
 * Test fixture for {@link SimplePrinter}
 */
public class PrinterTest {
	
	private TopicResultsPrinter<Topic> printer;
	
	private TopicResults<Topic> results;
	private String expectedOutput;
	
	/**
	 * Initial values for {@link TopicResults}
	 * and expected output.
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
		StringBuilder sb = new StringBuilder();
		sb.append("The most frequent proper nouns:" + System.lineSeparator());
        sb.append("Proper - 1 time" + System.lineSeparator());
        sb.append(System.lineSeparator());
    	sb.append("The most frequent nouns:" + System.lineSeparator());
    	sb.append("noun - 2 times" + System.lineSeparator());
    	sb.append(System.lineSeparator());
        sb.append("The most frequent phrases:" + System.lineSeparator());
        sb.append("test phrase - 1 time" + System.lineSeparator());
        expectedOutput = sb.toString();
	}

	/**
	 * Test method to verify the right print out.
	 */
	@Test
	public void testPrint() {
		printer = new SimplePrinter();
		assertEquals("Incorrect output!", expectedOutput, printer.print(results));
	}

}
