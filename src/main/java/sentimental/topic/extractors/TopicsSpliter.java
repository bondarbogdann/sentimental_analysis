package sentimental.topic.extractors;

import java.util.HashMap;
import java.util.LinkedHashMap;

import org.springframework.stereotype.Component;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multisets;

@Component
public class TopicsSpliter {

	/**
	    * Divides topics into proper nouns, nouns and phrases.
	    * 
	    * @param results
	    *           topics to be splited.
	    * @param maxCount
	    *           number of most frequent topics.
	    * @return map of proper nouns, nouns and phrases.
	    */
	public HashMap<Topics, LinkedHashMap<String, Integer>> splitTopics(
			TopicResults<Topic> results, int maxCount) {

		ImmutableMultiset<Topic> resultBreakdown = results.getTopics();

		// the set of proper nouns
		ImmutableMultiset<Topic> properNouns = ImmutableMultiset
				.copyOf(Iterables.filter(resultBreakdown,
						new Predicate<Topic>() {

							@Override
							public boolean apply(Topic topic) {
								return !topic.isPhrase() && topic.isProper();
							}

						}));

		// the set of nouns
		ImmutableMultiset<Topic> nouns = ImmutableMultiset.copyOf(Iterables
				.filter(resultBreakdown, new Predicate<Topic>() {

					@Override
					public boolean apply(Topic topic) {
						return !topic.isPhrase() && !topic.isProper();
					}

				}));

		// the set of phrases
		ImmutableMultiset<Topic> phrases = ImmutableMultiset.copyOf(Iterables
				.filter(resultBreakdown, new Predicate<Topic>() {

					@Override
					public boolean apply(Topic topic) {
						return topic.isPhrase();
					}

				}));

		HashMap<Topics, LinkedHashMap<String, Integer>> splitedTopics = 
				new HashMap<Topics, LinkedHashMap<String, Integer>>();
		splitedTopics.put(Topics.PROPER_NOUNS, frequentTopics(properNouns, maxCount));
		splitedTopics.put(Topics.NOUNS, frequentTopics(nouns, maxCount));
		splitedTopics.put(Topics.PHRASES, frequentTopics(phrases, maxCount));
		
		return splitedTopics;
	}

	/**
	    * Helper method for gathering specific topics.
	    * 
	    * @param topics
	    *           specific topics to be gathered.
	    * @param maxCount
	    *           number of most frequent topics.
	    * @return map of most frequent specific topics.
	    */
	private LinkedHashMap<String, Integer> frequentTopics(
			ImmutableMultiset<Topic> topics, int maxCount) {
		LinkedHashMap<String, Integer> res = new LinkedHashMap<String, Integer>();
		for (Topic topic : Iterables.limit(
				Multisets.copyHighestCountFirst(topics).elementSet(), maxCount)) {
			res.put(topic.getLabel(), topics.count(topic));
		}
		return res;
	}
}
