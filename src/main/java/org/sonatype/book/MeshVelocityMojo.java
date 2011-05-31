package org.sonatype.book;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

import org.apache.commons.io.DirectoryWalker;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.HiddenFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;

/**
 * @goal mesh-velocity
 * 
 * @phase test
 */
public class MeshVelocityMojo extends AbstractMojo {
	/**
	 * Location of the file.
	 * 
	 * @parameter expression="${basedir}/target/site"
	 * @required
	 */
	private File htmlDirectory;

	/**
	 * Velocity Template
	 * 
	 * @parameter
	 * @required
	 */
	private File velocityTemplate;

	/**
	 * Book Title
	 * 
	 * @parameter
	 * @required
	 */
	private String bookTitle;

	public void execute() throws MojoExecutionException {

		// Create a filter for Non-hidden directories
		IOFileFilter fooDirFilter = FileFilterUtils
				.andFileFilter(FileFilterUtils.directoryFileFilter(),
						HiddenFileFilter.VISIBLE);

		Properties properties = new Properties();
		properties.put( "bookTitle", bookTitle);
		
		ContentHandler handler = new ContentHandler(fooDirFilter,
				FileFilterUtils.suffixFileFilter(".html"), -1,
				new VelocityMerger(velocityTemplate, properties));
		try {
			handler.go(htmlDirectory, new ArrayList());
		} catch (IOException e) {
			throw new MojoExecutionException("Problem during directory scan", e);
		}

	}
}
