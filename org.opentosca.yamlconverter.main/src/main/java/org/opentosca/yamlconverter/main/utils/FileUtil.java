package org.opentosca.yamlconverter.main.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class FileUtil {
	/**
	 * Read file from path and return String.
	 *
	 * @param filepath filepath
	 * @return the file content
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	public String readYamlResource(String filepath) throws URISyntaxException, IOException {
		final URL urlToFile = getClass().getResource(filepath);
		if (urlToFile != null) {
			System.out.println("Try to read YAML file from classpath.");
			return IOUtils.toString(getClass().getResourceAsStream(filepath), "UTF-8");
		} else {
			System.out.println("Can't find resource on classpath. Try to read it as absolute path.");
			return FileUtils.readFileToString(new File(filepath));
		}
	}

	/**
	 * Makes an {@link InputStream} of a file given by filename.
	 * 
	 * @param file Filename of the file
	 * @return {@link InputStream} of file
	 */
	public InputStream getResourceAsStream(String file) {
		return getClass().getResourceAsStream(file);
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

	/**
	 * Deletes given Directory.
	 * 
	 * @param dir The {@link File}-Object of the directory.
	 * @throws IOException if unable to delete.
	 */
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

	/**
	 * Deletes all files inside directory.
	 * 
	 * @param directory The directory which contents should be deleted.
	 * @throws IOException
	 */
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

	/**
	 * Deletes {@link File}, whether it is a directory or not.
	 * 
	 * @param file The file to delete.
	 * @throws IOException
	 */
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
