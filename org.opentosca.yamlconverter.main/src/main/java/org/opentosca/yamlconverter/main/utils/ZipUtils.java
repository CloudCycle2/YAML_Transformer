package org.opentosca.yamlconverter.main.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtils {

	private final List<String> fileList;
	private final String sourceFolder;

	public ZipUtils(String sourceFolder) {
		this.fileList = new ArrayList<String>();
		this.sourceFolder = sourceFolder;
	}

	public void zipIt(String zipFile) {
		final byte[] buffer = new byte[1024];
		String source = "";
		FileOutputStream fos = null;
		ZipOutputStream zos = null;
		try {
			try {
				source = this.sourceFolder.substring(this.sourceFolder.lastIndexOf("\\") + 1, this.sourceFolder.length());
			} catch (final Exception e) {
				source = this.sourceFolder;
			}
			fos = new FileOutputStream(zipFile);
			zos = new ZipOutputStream(fos);

			System.out.println("Output to Zip : " + zipFile);
			FileInputStream in = null;

			for (final String file : this.fileList) {
				System.out.println("File Added : " + file);
				final ZipEntry ze = new ZipEntry(file);
				zos.putNextEntry(ze);
				try {
					in = new FileInputStream(this.sourceFolder + File.separator + file);
					int len;
					while ((len = in.read(buffer)) > 0) {
						zos.write(buffer, 0, len);
					}
				} finally {
					in.close();
				}
			}

			zos.closeEntry();
			System.out.println("Folder successfully compressed");

		} catch (final IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				zos.close();
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void generateFileList(File node) {

		// add file only
		if (node.isFile()) {
			this.fileList.add(generateZipEntry(node.toString()));

		}

		if (node.isDirectory()) {
			final String[] subNote = node.list();
			for (final String filename : subNote) {
				generateFileList(new File(node, filename));
			}
		}
	}

	private String generateZipEntry(String file) {
		return file.substring(this.sourceFolder.length() + 1, file.length());
	}
}
