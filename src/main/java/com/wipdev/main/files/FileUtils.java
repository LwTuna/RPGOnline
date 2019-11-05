package com.wipdev.main.files;

import java.io.File;
import java.io.IOException;

public class FileUtils {

	
	public static void createIfNotExistent(File file) {
		if(!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
