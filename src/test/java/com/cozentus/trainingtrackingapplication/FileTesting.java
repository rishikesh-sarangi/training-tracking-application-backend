package com.cozentus.trainingtrackingapplication;

import java.io.File;
import java.io.FileNotFoundException;

import org.springframework.util.ResourceUtils;

public class FileTesting {
	
		void test() {
			try {
				File resourceDirectory = ResourceUtils.getFile("classpath:");
				System.out.println(resourceDirectory);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
}
