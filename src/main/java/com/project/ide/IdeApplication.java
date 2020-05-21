package com.project.ide;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.project.ide.service.MailClient;



@SpringBootApplication
public class IdeApplication implements CommandLineRunner {
	
	@Autowired
	private  MailClient mailClient;

	public static void main(String[] args) {
		SpringApplication.run(IdeApplication.class, args);
		setupResourceDirectory();
		setupUploadDirectory();
		
		
		
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
	
	public static void setupUploadDirectory() {
		String uploadDir = "src/main/resources/static/uploads/";
		File UploadDir = new File(uploadDir);
		Boolean dirCreated;
		if(!UploadDir.exists()) {
			dirCreated = UploadDir.mkdir();
			if(!dirCreated) {
				System.out.println("Failed to create uploads directory");
			}
			
		}
	}

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("sdf");
		mailClient.prepareAndSend("suyashsrv7@gmail.com", "sushantsrv17", "quark124");
		System.out.println("sent");

		
	}
	
}
