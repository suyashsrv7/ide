package com.project.ide.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.springframework.stereotype.Service;

@Service
public class FileHandlerService {
	
	public void setFileContents(String dirname, String content) throws IOException {
		FileWriter fw = new FileWriter(dirname);
		fw.write(content);
		fw.close();
	}
	
	public String getFileContents(String dirname) throws IOException {
		System.out.println(dirname);
		FileReader fr=new FileReader(dirname);    
        int i; String content = "";
        while((i=fr.read())!=-1)    
        	content += (char)i;
        fr.close(); 
        System.out.println("Content :" + content);
        return content;
	}
	
	public void createNewFile(String filename) throws IOException {
		File nf = new File(filename);
		if(nf.exists()) return;
		nf.createNewFile();
	}

}
