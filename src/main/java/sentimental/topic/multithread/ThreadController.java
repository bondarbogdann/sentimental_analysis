package sentimental.topic.multithread;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import sentimental.topic.extractors.TopicResults;

/**
 * Controller for processing thread scheduling,
 * using threadpool and CompletionService
 * @see CompletionService
 */
public class ThreadController<T> {
	
	//processor load coefficient
	private static final int PROC_LOAD = 2;
	//the number of threads in threadpool
	private static final int THREAD_COUNT =  Runtime.getRuntime().availableProcessors()*PROC_LOAD;
	private TopicResults<T> finalResult;
	private ExecutorService threadPool;
	private CompletionService<TopicResults<T>> pool;

	public ThreadController() {
		finalResult = new TopicResults<T>();
		threadPool = Executors.newFixedThreadPool(THREAD_COUNT);
		pool = new ExecutorCompletionService<TopicResults<T>>(threadPool);
	}
	
	 /**
	    * Adding new task to the threadpool 
	    * @param task
	    *           specific task to be processed.
	    */
	public void newThread(Callable<TopicResults<T>> task){
		pool.submit(task);
	}
	
	 /**
	    * Method for gathering the results from 
	    * all the tasks
	    * @param count
	    *           the number of tasks.
	    * @return a collection of all topics wrapped in {@link TopicResults}.
	    */
	public TopicResults<T> getResults(int count) throws InterruptedException, ExecutionException{
		for(int i = 0; i < count; i++){
			TopicResults<T> result = pool.take().get();
			finalResult.addAllTopics(result.getTopics());
		}
		return finalResult;
	}
	
	/**
	    * Threadpool shutdown method
	    */
	public void shutDown(){
		threadPool.shutdown();
	}
	
}
