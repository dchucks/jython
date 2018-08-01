package com.example.jython;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.python.util.PythonInterpreter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

@SpringBootApplication
public class JythonApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(JythonApplication.class, args);
		String[] result = executeScript("World");
		System.out.println("############################# " + result);
	}
	
	public static String[] executeScript(String param) throws Exception {
		String[] result = new String[2];
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ByteArrayOutputStream err = new ByteArrayOutputStream();
		Properties preprops = System.getProperties();
		Properties props = new Properties();
        //props.put("python.home", "?");
        //props.put("jython.home", "?");
		//props.put("python.path", "?");
        props.put("python.console.encoding", "UTF-8");
        props.put("python.security.respectJavaAccessibility", "false"); 
        props.put("python.import.site", "false");
        PythonInterpreter.initialize(preprops, props, new String[0]);
		PythonInterpreter pyInterpreter = new PythonInterpreter();

		try {
			Resource resource = new ClassPathResource("hello.py");			
			pyInterpreter.set("args", param);
			pyInterpreter.setOut(out);
			pyInterpreter.setErr(err);
			pyInterpreter.execfile(resource.getInputStream());
			result[0] = out.toString();// reading the output
			result[1] = err.toString();// reading the error
		} catch (Exception e) {
			System.out.println("Error in executeScript: " + e);
			throw new Exception(e);
		} finally {
			try {
				if (out != null)
					out.close();
				if (err != null)
					err.close();
				pyInterpreter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
}
