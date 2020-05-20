package com.project.ide.service;

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
		FileReader fr=new FileReader(dirname);    
        int i; String content = "";
        while((i=fr.read())!=-1)    
        	content += (char)i;
        fr.close(); 
        return content;
	}

}