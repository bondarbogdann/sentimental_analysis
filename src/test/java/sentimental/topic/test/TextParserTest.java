package sentimental.topic.test;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import org.junit.Before;
import org.junit.Test;

import sentimental.topic.TextParser;
import sentimental.topic.extractors.Topic;
import sentimental.topic.extractors.TopicResults;

/**
 * Test fixture for {@link TextParser}
 */
public class TextParserTest {
	
	private TopicResults<Topic> expectedResults;
	private TextParser parser;
	private URL url;
	private File fileTxt;
	private File filePdf;
	private InputStream in;
	
	/**
	 * Initializing the data
	 * @throws FileNotFoundException 
	 * @throws MalformedURLException 
	 */
	@Before
	public void init() throws FileNotFoundException, MalformedURLException{
		parser = new TextParser();
		expectedResults = new TopicResults<Topic>();
		Topic phrase = new Topic();
		phrase.setLabel("first sentence");
		phrase.setPhrase(true);
		Topic noun = new Topic();
		noun.setLabel("one");
		expectedResults.addTopic(phrase);
		expectedResults.addTopic(noun);
		fileTxt = new File("src/test/resources/test.txt");
		in = new FileInputStream(fileTxt);
		filePdf = new File("src/test/resources/test.pdf");
		url = filePdf.toURI().toURL();
	}
	
	/**
	 * Test method to verify the right parsing 
	 * of the input txt file.
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	@Test
	public void readFileTxtTest() throws IOException, 
										 InterruptedException, 
										 ExecutionException {
		TopicResults<Topic> results = parser.readText(fileTxt);
		assertEquals("The results have different size.", 
				expectedResults.getSize(), results.getSize());
		
		assertEquals("The results don't match.", 
				expectedResults.getTopics(), results.getTopics());
	}
	
	/**
	 * Test method to verify the right parsing 
	 * of the input pdf file.
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	@Test
	public void readFilePdfTest() throws IOException, 
										 InterruptedException, 
										 ExecutionException {
		TopicResults<Topic> results = parser.readText(filePdf);
		assertEquals("The results have different size.", 
				expectedResults.getSize(), results.getSize());
		
		assertEquals("The results don't match.", 
				expectedResults.getTopics(), results.getTopics());
	}
	
	
	/**
	 * Test method to verify the right parsing 
	 * of the input url.
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	@Test
	public void readURLTest() throws IOException, 
									 InterruptedException, 
									 ExecutionException {
		TopicResults<Topic> results = parser.readText(url);
		assertEquals("The results have different size.", 
				expectedResults.getSize(), results.getSize());
		
		assertEquals("The results don't match.", 
				expectedResults.getTopics(), results.getTopics());
	}
	
	/**
	 * Test method to verify the right parsing 
	 * of the input InputStream.
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	@Test
	public void readInputStreamTest() throws IOException, 
											 InterruptedException, 
											 ExecutionException {
		TopicResults<Topic> results = parser.readText(in);
		assertEquals("The results have different size.", 
				expectedResults.getSize(), results.getSize());
		
		assertEquals("The results don't match.", 
				expectedResults.getTopics(), results.getTopics());
	}
	
	/**
	 * Test method to check the private 
	 * method {@link TextParser#sentencesFinder(String)}
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 */
	@Test
	public void sentencesFinderTest() throws NoSuchMethodException, 
											 SecurityException, 
											 IllegalAccessException, 
											 IllegalArgumentException, 
											 InvocationTargetException{
		String inputText = "This is the first sentence. "
				+ "And here is another one! And remaining";
		Method method = TextParser.class.getDeclaredMethod("sentencesFinder", String.class);
        method.setAccessible(true);
        String[] output = (String[]) method.invoke(parser, inputText);
        assertEquals("Sentences don't match",
        		"This is the first sentence. And here is another one!", output[0]);
        assertEquals("Sentences don't match",
        		" And remaining", output[1]);
	}

}
