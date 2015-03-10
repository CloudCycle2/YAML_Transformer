package org.opentosca.yamlconverter.main.utils;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;

public class FileUtil {
	/**
	 * Read file from path and return String.
	 *
	 * @param path filepath
	 * @return the file content
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	public String readYamlResource(String path) throws URISyntaxException, IOException {
		File file = new File(getClass().getResource(path).toURI());
		try {
			return FileUtils.readFileToString(file);
		} catch (Exception e) {
			System.out.println("Can't find resource on classpath. Try to read it was absolute path.");
			file = new File(path);
		}
		return FileUtils.readFileToString(file);
	}

	/**
	 * Saves a String to a file.
	 *
	 * @param filename The filename.
	 * @param content Content to write to file.
	 * @throws IOException
	 */
	public static void saveStringAsFile(String filename, String content) throws IOException {
		FileUtils.writeStringToFile(new File(filename), content);
	}

	public static void deleteDirectory(File dir) throws IOException {
		if (!dir.exists()) {
			return;
		}

		cleanDirectory(dir);

		if (!dir.delete()) {
			final String message = "Unable to delete directory " + dir + ".";
			throw new IOException(message);
		}
	}

	public static void cleanDirectory(final File directory) throws IOException {
		if (!directory.exists()) {
			final String message = directory + " does not exist";
			throw new IllegalArgumentException(message);
		}

		if (!directory.isDirectory()) {
			final String message = directory + " is not a directory";
			throw new IllegalArgumentException(message);
		}

		final File[] files = directory.listFiles();
		if (files == null) { // null if security restricted
			throw new IOException("Failed to list contents of " + directory);
		}

		IOException exception = null;
		for (final File file : files) {
			try {
				forceDelete(file);
			} catch (final IOException ioe) {
				exception = ioe;
			}
		}

		if (null != exception) {
			throw exception;
		}
	}

	public static void forceDelete(final File file) throws IOException {
		if (file.isDirectory()) {
			deleteDirectory(file);
		} else {
			final boolean filePresent = file.exists();
			if (!file.delete()) {
				if (!filePresent) {
					throw new FileNotFoundException("File does not exist: " + file);
				}
				final String message = "Unable to delete file: " + file;
				throw new IOException(message);
			}
		}
	}
}
