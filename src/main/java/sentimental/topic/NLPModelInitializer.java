package sentimental.topic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.InvalidFormatException;

/**
 * Initializer of Apache OpenNLP model
 */
public class NLPModelInitializer {
	
	private static final Logger logger = LoggerFactory.getLogger(NLPModelInitializer.class);
	
	/**
	 * The lock for concurrent access
	 */
	private static final ReentrantLock _lock = new ReentrantLock(); 

	/**
	 * path to the properties file for loading 
	 * model files
	 */
	private static final String propertiesPath = "src/main/resources/model.properties";
	private static Properties properties;
	
	/**
	 * Model objects for tokenization,
	 * POS tagging, chunking and sentence division
	 */
	private static TokenizerModel tm;
	private static TokenizerME wordBreaker;
	private static POSModel pm;
	private static POSTaggerME posme;
	private static InputStream modelIn;
	private static ChunkerModel chunkerModel;
	private static ChunkerME chunkerME;
	private static SentenceModel modelS;
	private static SentenceDetectorME sdetector;

	static {
		init();
	}

	/**
	    * Initialization of model files.
	    */
	private static void init() {

		try {
			//loading properties file
			properties = new Properties();
			properties.load(new FileInputStream(propertiesPath));
			
			//initializing model objects
			tm = new TokenizerModel(new FileInputStream(
					new File(properties.getProperty("tokenizerModel"))));
			wordBreaker = new TokenizerME(tm);
			pm = new POSModel(new FileInputStream(new File(properties.getProperty("posModel"))));
			posme = new POSTaggerME(pm);
			modelIn = new FileInputStream(properties.getProperty("chunkerModel"));
			chunkerModel = new ChunkerModel(modelIn);
			chunkerME = new ChunkerME(chunkerModel);
			modelS = new SentenceModel(new FileInputStream(properties.getProperty("sentenceModel")));
			sdetector = new SentenceDetectorME(modelS);
		} catch (InvalidFormatException e) {
			logger.error("The format of NLP model file is wrong " + e.getMessage());
			System.exit(1);
		} catch (FileNotFoundException e) {
			logger.error("The model NLP file was not found " + e.getMessage());
			System.exit(1);
		} catch (IOException e) {
			logger.error("IO problems with NLP model files " + e.getMessage());
			System.exit(1);
		}

	}
	
	public static void lock(){
		_lock.lock();
	}
	
	public static void unlock(){
		_lock.unlock();
	}
	
	/**
	    * @return tokenizer model.
	    */
	public static TokenizerME wordBreaker(){
		return wordBreaker;
	}
	
	/**
	    * @return model for POS tagging.
	    */
	public static POSTaggerME posme(){
		return posme;
	}
	
	/**
	    * @return model for dividing string into chunks.
	    */
	public static ChunkerME chunkerME(){
		return chunkerME;
	}
	
	/**
	    * @return model for dividing string into sentences.
	    */
	public static SentenceDetectorME sdetector(){
		return sdetector;
	}
}
