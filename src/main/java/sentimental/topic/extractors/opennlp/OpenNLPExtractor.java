package sentimental.topic.extractors.opennlp;

import opennlp.tools.util.Span;

import org.springframework.stereotype.Component;

import sentimental.topic.NLPModelInitializer;
import sentimental.topic.extractors.Topic;
import sentimental.topic.extractors.TopicExtractor;
import sentimental.topic.extractors.TopicResults;

/**
 * Apache OpenNLP implementation of {@link TopicExtractor} interface
 */

@Component
public class OpenNLPExtractor implements TopicExtractor<Topic> {

	/**
	 * @inheritDoc
	 *
	 * This implementation divides the input text
	 * into sentences and parses each sentence in order to
	 * find frequent proper nouns, nouns and phrases
	 * using Apache OpenNLP interface
	 */
	public TopicResults<Topic> extract(String inputText) {
		TopicResults<Topic> results = new TopicResults<Topic>();

		for (String sentence : gettingSentences(inputText)) {
			String[] words = null;
			String[] posTags = null;
			Span[] chunks = null;
			//locking the NLPModelInitializer for concurrent access
			NLPModelInitializer.lock();
			try {
				// words is the tokenized sentence
				words = NLPModelInitializer.wordBreaker().tokenize(sentence);
				// posTags are the parts of speech of every word in the
				// sentence
				posTags = NLPModelInitializer.posme().tag(words);
				// chunks are the start end "spans" indices to the chunks in
				// the words array
				chunks = NLPModelInitializer.chunkerME().chunkAsSpans(words,
						posTags);
			} finally {
				NLPModelInitializer.unlock();
			}

			for (int i = 0; i < chunks.length; i++) {
				//detecting noun phrases from the chunks
				if (chunks[i].getType().equals("NP")) {
					int wordCount = 0;
					boolean isProper = false;
					StringBuilder npFinal = new StringBuilder();
					for (int j = chunks[i].getStart(); j < chunks[i].getEnd(); j++) {
						//checking if the word in chunk belongs to
						// nouns, adjectives, adverbs or verbs
						if (posTags[j].startsWith("NN")
								|| posTags[j].startsWith("JJ")
								|| posTags[j].startsWith("RB")
								|| posTags[j].startsWith("VB")) {
							wordCount++;
							// checking if the word is proper noun
							if (posTags[j].startsWith("NNP"))
								isProper = true;
							npFinal.append(words[j]);
							if (j < chunks[i].getEnd() - 1)
								npFinal.append(" ");
						}
					}
					if (npFinal.length() > 0) {
						Topic topic = new Topic();
						topic.setLabel(npFinal.toString());
						if (wordCount > 1)
							topic.setPhrase(true);
						else if (isProper)
							topic.setProper(true);
						results.addTopic(topic);
					}
				}
			}

		}

		return results;
	}

	/**
	    * Method for dividing string into sentences 
	    * @param str
	    *           the string to be divided.
	    * @return an array of sentences.
	    */
	private String[] gettingSentences(String str) {
		String[] sentences = null;
		//locking the NLPModelInitializer for concurrent access
		NLPModelInitializer.lock();
		try {
			sentences = NLPModelInitializer.sdetector().sentDetect(str);
		} finally {
			NLPModelInitializer.unlock();
		}
		return sentences;
	}
}
