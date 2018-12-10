package models;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.Writer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SongLinks {
	public static int countFile = 1;
	
	public static int countEntity = 1;
	
	public static String songName = "";
	
	public static boolean check(String string) {
		try {
			FileInputStream fis = new FileInputStream(new File("specialCharacters.in"));
			InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
			BufferedReader br = new BufferedReader(isr);
			
			while (br.ready()) {
				String line = br.readLine();
				if (string.contains(line)) {
					br.close();
					return false;
				}
			}
			
			char array[] = string.toCharArray();
			for (char element: array) {
				if (element == '\'') {
					br.close();
					return false;
				}
			}
			
			br.close();
			isr.close();
			fis.close();
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}
	
	public static boolean checkSong(String name) {
		try {
			FileInputStream fis = new FileInputStream(new File("database/songDB/song_list.txt"));
			InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
			BufferedReader br = new BufferedReader(isr);
			
			while (br.ready()) {
				String line = br.readLine();
				if (line.toLowerCase().equals(name.toLowerCase())) {
					br.close();
					isr.close();
					fis.close();
					return true;
				}
			}
			
			br.close();
			isr.close();
			fis.close();
			return false;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}
	
	public static boolean performedBy(String html, String path) {
		try {
			File newFile = new File(path);
			Writer writer = new FileWriter(newFile, true);
			Writer writer1 = new FileWriter(new File("database/songDB/song_list.txt"),true);
			Document doc = Jsoup.parse(html);
			
			Elements nameClass = doc.getElementsByClass("txt-primary");
			
			String performedByProperty = "";
			
			for (Element element: nameClass) {
				for (Element line: element.getElementsByTag("h1")) {
					String text = line.text();
					String data[] = text.split(".-.");
					if (!check(data[0])) {
						writer.close();
						writer1.close();
						return false;
					} else {
						if (checkSong(data[0])) {
							writer.close();
							writer1.close();
							return false;
						}
						char array[] = data[0].toCharArray();
						if (array[array.length-1] == ' ') {
							writer.close();
							writer1.close();
							return false;
						}
					}
					
					songName = data[0].replaceAll(" ", "_");
					writer1.append(data[0]);
					writer1.append("\n");
					if (data[1].contains("\"")) {
						data[1] = "";
					}
					performedByProperty = "group1:" + songName + " group1:performedBy " + "\"" + data[1] + "\" .";
					writer.append("\n");					
					writer.append(performedByProperty);
					writer.append("\n");
					writer.close();
					writer1.close();
					return true;
				}
			}
			
			performedByProperty = "group1:" + songName + " group1:performedBy " + "\"" +  "\" .";
			writer.append("\n");	
			writer.append(performedByProperty);
			writer.append("\n");
			writer.close();
			writer1.close();
			return false;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}
	
	public static boolean performedNCTBy(String html, String path) {
		try {
			File newFile = new File(path);
			Writer writer = new FileWriter(newFile, true);
			Writer writer1 = new FileWriter(new File("database/songDB/song_list.txt"),true);
			Document doc = Jsoup.parse(html);
			
			Elements nameClass = doc.getElementsByClass("name_title");
			
			String performedByProperty = "";
			
			for (Element element: nameClass) {
				for (Element line: element.getElementsByTag("div")) {
					String text = line.text();
					String data[] = text.split(".-.");
					if (!check(data[0])) {
						writer.close();
						writer1.close();
						return false;
					} else {
						if (checkSong(data[0])) {
							writer.close();
							writer1.close();
							return false;
						}
						char array[] = data[0].toCharArray();
						if (array[array.length-1] == ' ') {
							writer.close();
							writer1.close();
							return false;
						}
					}
					songName = data[0].replaceAll(" ", "_");
					writer1.append(data[0]);
					writer1.append("\n");
					if (data[1].contains("\"")) {
						data[1] = "";
					}
					performedByProperty = "group1:" + songName + " group1:performedBy " + "\"" + data[1] + "\" .";
					writer.append("\n");					
					writer.append(performedByProperty);
					writer.append("\n");
					writer.close();
					writer1.close();
					return true;
				}
			}
			
			performedByProperty = "group1:" + songName + " group1:performedBy " + "\"" +  "\" .";
			writer.append("\n");	
			writer.append(performedByProperty);
			writer.append("\n");
			writer.close();
			writer1.close();
			return false;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}
	
	public static void composedBy(String html, String path) {
		try {
			Writer writer = new FileWriter(new File(path), true);
			Document doc = Jsoup.parse(html);
			
			Elements composedClass = doc.getElementsByClass("info-song-top otr fl");
			
			String composedByProperty = "";
			String composer = "";
			
			for (Element container: composedClass) {
				for (Element line: container.getElementsByTag("div")) {
					if (line.attr("id").equals("composer-container")) {
						composer += line.text() + " ";
					}
				}
			}
			
			if (composer.contains("\"")) {
				composer = "";
			}
			composedByProperty = "group1:" + songName + " group1:composedBy " + "\"" + composer + "\" .";
			writer.append(composedByProperty);
			writer.append("\n");
			writer.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static void composedNCTBy(String html, String path) {
		try {
			Writer writer = new FileWriter(new File(path), true);
			Document doc = Jsoup.parse(html);
			
			Elements composedClass = doc.getElementsByClass("name_post");
			
			String composedByProperty = "";
			String composer = "";			
		
			task:
			for (Element container: composedClass) {
				for (Element line: container.getElementsByTag("p")) {
					if (line.text().length() > 9) {
						composer += line.text().substring(9);
					} else {
						composer += line.text();
					}
					break task;
				}
			}
			
			if (composer.contains("\"")) {
				composer = "";
			}
			composedByProperty = "group1:" + songName + " group1:composedBy " + "\"" + composer + "\" .";
			writer.append(composedByProperty);
			writer.append("\n");
			writer.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static void lyric(String html, String path) {
		try {
			Writer writer = new FileWriter(new File(path), true);
			Document doc = Jsoup.parse(html);
			
			
			Elements lyricClass = doc.getElementsByClass("fn-container");
			
			String lyricProperty = ""; 
			
			for (Element element: lyricClass) {
				for (Element lyric: element.getElementsByClass("fn-wlyrics fn-content")) {
					String lyricText = lyric.text();
					if (lyricText.contains("\"")) {
						lyricText = lyricText.replaceAll("\"", "-");
					}
					lyricProperty = "group1:" + songName + " group1:lyric " + "\"" + lyric.text() + "\" .";
					writer.append(lyricProperty);
					writer.append("\n");
					writer.append("\n");
					writer.close();
					return;
				}
			}
			
			lyricProperty = "group1:" + songName + " group1:lyric " + "\"" + "\" .";
			writer.append(lyricProperty);
			writer.append("\n");
			writer.append("\n");
			writer.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static void lyricNCT(String html, String path) {
		try {
			Writer writer = new FileWriter(new File(path), true);
			Document doc = Jsoup.parse(html);
			
			
			Elements lyricClass = doc.getElementsByClass("pd_lyric trans");
			
			String lyricProperty = ""; 
			
			for (Element element: lyricClass) {
				for (Element lyric: element.getElementsByTag("p")) {
					String lyricText = lyric.text();
					if (lyricText.contains("\"")) {
						lyricText = lyricText.replaceAll("\"", "-");
					}
					lyricProperty = "group1:" + songName + " group1:lyric " + "\"" + lyric.text() + "\" .";
					writer.append(lyricProperty);
					writer.append("\n");
					writer.append("\n");
					writer.close();
					return;
				}
			}
			
			lyricProperty = "group1:" + songName + " group1:lyric " + "\"" + "\" .";
			writer.append(lyricProperty);
			writer.append("\n");
			writer.append("\n");
			writer.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static void read(String path) {
		boolean check = false;
		try {
			File newFile = new File(path);
			if (newFile.isDirectory()) {
				File listFile[] = newFile.listFiles();
				for (int i = 0; i < listFile.length; i++) {
					String data = "";
					String filePath = "database/songDB/songDB" + countFile + ".ttl";
					
					
					FileInputStream fis = new FileInputStream(listFile[i]);
					InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
					BufferedReader br = new BufferedReader(isr);

					while (br.ready()) {

						String line = br.readLine();
						if (line.contains("<html")) {
							check = true;
						}
						if (check) {
							data += line + "\n";
						}
						if (line.contains("</html>")) {
							check = false;
							if (countEntity % 200 == 0) {
								countFile++;
								filePath = "database/songDB/songDB" + countFile + ".ttl";
							}
							if (performedBy(data,filePath)) {
								composedBy(data,filePath);
								lyric(data,filePath);
								countEntity++;
							}
							data = "";
						}
					}
					
					br.close();
					isr.close();
					fis.close();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static void readNCT(String path) {
		boolean check = false;
		try {
			File newFile = new File(path);
			if (newFile.isDirectory()) {
				File listFile[] = newFile.listFiles();
				for (int i = 0; i < listFile.length; i++) {
					String data = "";
					String filePath = "database/songDB/songDB" + countFile  + ".ttl";
					
					FileInputStream fis = new FileInputStream(listFile[i]);
					InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
					BufferedReader br = new BufferedReader(isr);

					while (br.ready()) {

						String line = br.readLine();
						if (line.contains("<html")) {
							check = true;
						}
						if (check) {
							data += line + "\n";
						}
						if (line.contains("</html>")) {
							check = false;
							if (countEntity % 200 == 0) {
								countFile++;
								filePath = "database/songDB/songDB" + countFile + ".ttl";
							}
							if (performedNCTBy(data,filePath)) {
								composedNCTBy(data,filePath);
								lyricNCT(data,filePath);
								countEntity++;
							}
							data = "";
						}
					}
					
					br.close();
					isr.close();
					fis.close();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
