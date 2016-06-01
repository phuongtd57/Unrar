package com.example.phuongtd.moolamoola.de.innosystec.unrar;

import com.example.phuongtd.moolamoola.de.innosystec.unrar.rarfile.FileHeader;

import java.io.File;
import java.io.FileOutputStream;


public class MVTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String filename = "C:/Users/qs109/Desktop/aaa.rar";
		File f = new File(filename);
		Archive a = null;
		boolean result = false;

		boolean test = false;// test mode

		try {
			a = new Archive(f, "", false); // extract mode
			if(a.isPass()){
				System.out.println("Pass word");
				a = new Archive(f, "123", false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (a != null) {
			a.getMainHeader().print();
			FileHeader fh = a.nextFileHeader();
			while (fh != null) {
				try {
					File out = new File("C:/Users/qs109/Desktop/"
							+ fh.getFileNameString().trim());
					System.out.println(out.getAbsolutePath());
					FileOutputStream os = new FileOutputStream(out);
					a.extractFile(fh, os);
					os.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				fh = a.nextFileHeader();
			}
		}

	}
}
