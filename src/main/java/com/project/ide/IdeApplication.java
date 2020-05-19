package com.project.ide;

import java.io.File;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



@SpringBootApplication
public class IdeApplication {

	public static void main(String[] args) {
		SpringApplication.run(IdeApplication.class, args);
		setupResourceDirectory();
		
	}
	
	public static void setupResourceDirectory() {
		File resourceDir = new File("tmpStore");
		Boolean dirCreated; 
		if(!resourceDir.exists()) {
			dirCreated = resourceDir.mkdir();
			if(!dirCreated) {
				System.out.println("Failed to create resouce directory");
			}
		}
		
	}

}
