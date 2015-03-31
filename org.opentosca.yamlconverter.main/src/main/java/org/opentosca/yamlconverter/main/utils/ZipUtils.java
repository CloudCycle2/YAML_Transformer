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

	private final List<File> fileList;

	public ZipUtils() {
		this.fileList = new ArrayList<File>();
	}

	public void zipIt(String zipFile) {
		final byte[] buffer = new byte[1024];
		FileOutputStream fos = null;
		ZipOutputStream zos = null;
		try {
			final File zf = new File(zipFile);
			if (zf.getParentFile() != null) {
				zf.getParentFile().mkdirs();
			}
			fos = new FileOutputStream(zf);
			zos = new ZipOutputStream(fos);

			System.out.println("Output to Zip : " + zipFile);
			FileInputStream in = null;

			for (final File file : this.fileList) {
				System.out.println("Trying to add file : " + file.getPath().replace("\\", "/"));
				final ZipEntry ze = new ZipEntry(file.getPath().replace("\\", "/"));
				zos.putNextEntry(ze);
				try {
					in = new FileInputStream(file);
					int len;
					while ((len = in.read(buffer)) > 0) {
						zos.write(buffer, 0, len);
					}
				} catch (final Exception e) {
					e.printStackTrace();
				} finally {
					if (in != null) {
						in.close();
					}
				}
				System.out.println("File Added : " + file);
			}

			zos.closeEntry();
			System.out.println("Folder successfully compressed");

		} catch (final IOException ex) {
			ex.printStackTrace();
		} finally {
			if (zos != null) {
				try {
					zos.close();
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void generateFileList(File node) {
		// add file only
		if (node.isFile()) {
			this.fileList.add(node);
		}

		if (node.isDirectory()) {
			final File[] files = node.listFiles();
			if (files != null) {
				for (final File file : files) {
					generateFileList(file);
				}
			}
		}
	}
}
