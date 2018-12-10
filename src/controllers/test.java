package controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class test {

	public static int realname = 0;
	public static int birthday = 0;
	public static int country = 0;
	public static int avatarlink = 0;
	public static int linkSinger = 0;
	public static int height = 0;
	public static int birthPlace = 0;
	public static int linkPlace = 0;
	public static int band = 0;
	public static int instrument = 0;
	public static int genre = 0;

	public static int perform = 0;
	public static int compose = 0;
	public static int lyric = 0;

	public static void main(String[] args) {
//		ArrayList<String> list = load();
//		doIt("/Volumes/Mac OS's Data/Java Projects/JSOUP/database/songDB", list);
		ArrayList<String> list = print("/Users/minhmon/Desktop/result.out");
//		analysis(list, "/Volumes/Mac OS's Data/Java Projects/JSOUP/database/singerDB/singer_list.txt");
		for (String  element: list) {
			System.out.println(element);
		}
	}

	public static void doIt(String url, ArrayList<String> list) {
		try {
			for (String element: list) {
				System.out.println(element);
			}
			File file = new File(url);
			System.out.println(file.isDirectory());
			if (file.isDirectory()) {
				File listFile[] = file.listFiles();
				for (String test: list) {
					for (File elementFile: listFile) {
						FileInputStream fis = new FileInputStream(elementFile);
						InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
						BufferedReader br = new BufferedReader(isr);
						
						while (br.ready()) {
							String line = br.readLine();
							if (line.contains("minhmon:song" + test + " ")) {
								if (line.contains("minhmon:performedBy")) {
									String data[] = line.split("performedBy\\s+");
									if (!data[1].contains("\"\"")) {
										perform++;
									}
									System.out.println(data[1]);
								}
								if (line.contains("minhmon:composedBy")) {
									String data[] = line.split("composedBy\\s+");
									if (!data[1].contains("\"\"")) {
										compose++;
									}
									System.out.println(data[1]);
								}
								if (line.contains("minhmon:lyric")) {
									String data[] = line.split("lyric\\s+");
									if (!data[1].contains("\"\"")) {
										lyric++;
									}
									System.out.println(data[1]);
								}
							}
						}
						
						br.close();
						isr.close();
						fis.close();
						
					}
				}
			}
//			System.out.println("realname: " + realname);
//			System.out.println("birthday: " + birthday);
//			System.out.println("country: " + country);
//			System.out.println("avatarlink: " + avatarlink);
//			System.out.println("linkSinger: " + linkSinger);
//			System.out.println("height: " + height);
//			System.out.println("birthplace: " + birthPlace);
//			System.out.println("linkPlace: " + linkPlace);
//			System.out.println("band: " + band);
//			System.out.println("instrument: " + instrument);
//			System.out.println("genre: " + genre);
			System.out.println("Perform: " +perform);
			System.out.println("Composed:" + compose);
			System.out.println("Lyric:" + lyric);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static ArrayList<String> load() {
//		try {
//			ArrayList<String> list = new ArrayList<>();
//			FileInputStream fis = new FileInputStream(new File("/Users/minhmon/Desktop/list.out"));
//			InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
//			BufferedReader br = new BufferedReader(isr);
//			
//			while (br.ready()) {
//				String line = br.readLine();
//				list.add(line);
//			}
//			
//			br.close();
//			isr.close();
//			fis.close();
//			return list;
//		} catch (Exception ex)  {
//			ex.printStackTrace();
//			return null;
//		}
		ArrayList<String> list = new ArrayList<>();
		Random ran = new Random();
		int check = -1;
		for (int i = 0; i < 500; i++) {
			int number = ran.nextInt(33311);

			while (number == check) {
				number = ran.nextInt(33311);
			}
			list.add(number + "");
		}
		return list;
	}

	public static ArrayList<Integer> run() {
		try {
			ArrayList<Integer> list = new ArrayList<>();
			FileInputStream fis = new FileInputStream(new File("/Users/minhmon/Desktop/list.in"));
			InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
			BufferedReader br = new BufferedReader(isr);

			while (br.ready()) {
				String line = br.readLine();
				list.add(Integer.parseInt(line));
			}

			br.close();
			isr.close();
			fis.close();
			return list;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	public static ArrayList<String> print(String url) {
		try {
			ArrayList<String> list = new ArrayList<>();
			FileInputStream fis = new FileInputStream(new File(url));
			InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
			BufferedReader br = new BufferedReader(isr);
			
			while (br.ready()) {
				String line = br.readLine();
				if (line.length() > 0) {
					String data[] = line.split(",");
					list.add(data[1]);
				}
			}			
			
			br.close();
			isr.close();
			fis.close();
			return list;
			
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	public static void analysis(ArrayList<String> list, String url) {
		try {
			FileInputStream fis = new FileInputStream(new File(url));
			InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
			BufferedReader br = new BufferedReader(isr);
			
			int index = 1;			
			while (br.ready()) {
				String line = br.readLine();
				for (String element: list) {
					String data[] = element.split(",");
					if (data[0].toLowerCase().contains(line.toLowerCase())) {
						System.out.println(line + "," + data[1] + "," + index);
						break;
					}
				}
				index++;
			}
			
			br.close();
			isr.close();
			fis.close();
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
