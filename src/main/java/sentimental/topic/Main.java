package sentimental.topic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.beust.jcommander.JCommander;
/**
 * The Main entry point of application.
 * It uses the SpringBoot framework for 
 * fast developing of REST API on embedded 
 * Tomcat server
 */

@Component
@SpringBootApplication
public class Main {

	@Autowired
	private Demo demo;

	public static void main(String[] args) {
		System.out.println("Topic Extraction Demo");
		ApplicationContext context = SpringApplication.run(Main.class, args);
		TopicDemoConfig config = new TopicDemoConfig();
		new JCommander(config, args);
		Main main = context.getBean(Main.class);
		main.demo.runDemo(config);
	}
}
