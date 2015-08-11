package sentimental.topic.test;
import static org.junit.Assert.*;

import org.junit.Test;

import sentimental.topic.NLPModelInitializer;

/**
 * Test fixture for Apache OpenNLP
 */
public class OpenNLPTest {

	/**
	 * Test method for sentence detection.
	 */
	@Test
	public void testDetectSentences() {

		final String content = "This is the first sentence. And here is another one!"
				+ " Should I mention the 3-rd? Would not it be nice "
				+ "for Mr. Noname to give us the fourth, but "
				+ "not later than April 10.";

		final String[] expected = new String[] {
				"This is the first sentence.",
				"And here is another one!",
				"Should I mention the 3-rd?",
				"Would not it be nice for Mr. Noname "
						+ "to give us the fourth, but not later than April 10." };

		final String[] sentences = NLPModelInitializer.sdetector().sentDetect(
				(content));
		// compare each sentence against expectations
		assertEquals("Incorrect number of sentences detected.",
				expected.length, sentences.length);
		for (int i = 0; i < expected.length; i++) {
			assertEquals("Unexpected sentence content", expected[i],
					sentences[i]);
		}
	}

	/**
	 * Test method for tokenization.
	 */
	@Test
	public void testTokenize() {

		final String[] sentences = new String[] {
				"This is the first sentence.",
				"And here is another one!",
				"Should I mention the 3-rd?",
				"Would not it be nice for Mr. Noname "
						+ "to give us the fourth, but not later than April 10." };

		final String[][] expected = new String[][] {
				new String[] { "This", "is", "the", "first", "sentence", "." },
				new String[] { "And", "here", "is", "another", "one", "!"},
				new String[] { "Should", "I", "mention", "the", "3-rd", "?"},
				new String[] { "Would", "not", "it", "be", "nice", "for", "Mr.",
								"Noname", "to", "give", "us", "the", "fourth",
								",", "but", "not", "later", "than", "April",
								"10", "."}
				};

		for (int i = 0; i < sentences.length; i++) {
			final String[] tokens = NLPModelInitializer.wordBreaker().tokenize(sentences[i]);

			assertEquals(
					"Incorrect number of tokens detected for sentence at index "
							+ i, expected[i].length, tokens.length);
			// compare each token against expectations
			for (int j = 0; j < expected[i].length; j++) {
				assertEquals("Unexpected token at sentence index " + i
						+ ", token index " + j, expected[i][j], tokens[j]);
			}
		}
	}

	/**
	 * Test method for PartOfSpeech detection.
	 */
	@Test
	public void testPartOfSpeechTagger() {
		
		final String[][] tokens = new String[][] {
				new String[] { "This", "is", "the", "first", "sentence", "." },
				new String[] { "And", "here", "is", "another", "one", "!"},
				new String[] { "Should", "I", "mention", "the", "3-rd", "?"},
				new String[] { "Would", "not", "it", "be", "nice", "for", "Mr.",
								"Noname", "to", "give", "us", "the", "fourth",
								",", "but", "not", "later", "than", "April",
								"10", "."}
				};
		
		final String[][] expected = new String[][] {
				new String[] { "DT", "VBZ", "DT", "JJ", "NN", "."},
				new String[] { "CC", "RB", "VBZ", "DT", "NN", "."}, 
				new String[] { "MD", "PRP", "VB", "DT", "JJ", "."},
				new String[] { "MD", "RB", "PRP", "VB", "JJ", "IN",
								"NNP", "NNP", "TO", "VB", "PRP", "DT",
								"JJ", ",", "CC", "RB", "RB", "IN",
								"NNP", "CD", "."},
				};

		for (int i = 0; i < tokens.length; i++) {
			final String[] tags = NLPModelInitializer.posme().tag(tokens[i]);
			assertEquals(
					"Incorrect number of tags detected for sentence at index "
							+ i, expected[i].length, tags.length);
			// compare each tag against expectations
			for (int j = 0; j < expected[i].length; j++) {
				assertEquals("Unexpected tag at sentence index " + i
						+ ", token index " + j, expected[i][j], tags[j]);
			}
		}
	}

}
