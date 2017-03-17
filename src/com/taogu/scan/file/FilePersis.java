package com.taogu.scan.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

import com.taogu.config.Config;

public class FilePersis {

	private final static Logger log = Logger.getLogger(FilePersis.class);

	public static void persis(List<String> list) {
		File file = new File(Config.getString("proxy.store.file", "proxy.txt"));
		if (file.exists()) {
			file.delete();
		}
		BufferedWriter bw = null;
		try {
			file.createNewFile();
			bw = new BufferedWriter(new FileWriter(file, true));
			for (String proxy : list) {
				bw.write(proxy + "\n");
			}
			bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
			log.error(e.getStackTrace());
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
