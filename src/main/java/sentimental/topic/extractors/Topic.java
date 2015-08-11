package sentimental.topic.extractors;

/**
 * The model for the topics
 */
public class Topic {
	//the topic string itself
    private String label;
    //shows whether the topic includes proper nouns 
    private boolean proper;
    //shows whether the topic consists of couple of words
    private boolean phrase;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
    
    
	public boolean isProper() {
		return proper;
	}

	public void setProper(boolean proper) {
		this.proper = proper;
	}

	public boolean isPhrase() {
		return phrase;
	}

	public void setPhrase(boolean phrase) {
		this.phrase = phrase;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Topic)) return false;

        Topic topic = (Topic) o;

        if (label != null ? !label.equals(topic.label) : topic.label != null) return false;
        if (!(proper == topic.isProper() && phrase == topic.isPhrase())) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return label != null ? label.hashCode() : 0;
    }
}
