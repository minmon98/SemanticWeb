package controllers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import models.SongLinks;

public class Main {

	public static void main(String[] args) {
		
//		SongLinks.read("data/zing/songs");
//		SongLinks.readNCT("data/nct/songs");
		analysis("/Users/minhmon/songDB");
	}
	
	public static void run(File file) {
		try {
			FileInputStream fis = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
			BufferedReader br = new BufferedReader(isr);
			
			String data = "";
			
			while (br.ready()) {
				String line = br.readLine();
				data += line + "\n";
			}
			
			FileOutputStream fos = new FileOutputStream(file);
			OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
			BufferedWriter bw = new BufferedWriter(osw);
			
			data = "@prefix group1: <https://sw.uet.vnu.edu.vn/group1/> ." + "\n\n" + data;
			bw.write(data);	
			
			bw.close();
			osw.close();
			fos.close();
			
			br.close();
			isr.close();
			fis.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static void analysis(String url ) {
		try {
			File file = new File(url);
			if (file.isDirectory()) {
				File listFile[] = file.listFiles();
				for (File element: listFile) {
					run(element);
				}
			}
			System.out.println("DONE");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
