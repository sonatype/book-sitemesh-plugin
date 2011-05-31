package org.sonatype.book;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

public class VelocityMerger {
	
	private File velocityTemplate;
	private Properties properties;
	
	public VelocityMerger(File velocityTemplate, Properties properties) {
		this.velocityTemplate = velocityTemplate;
		this.properties = properties;
	}
	
	public void mergeFile( File htmlFile, SiteMeshPageExtractor extractor ) throws Exception {
	    Velocity.setProperty("input.encoding", "UTF-8");
	    Velocity.setProperty("output.encoding", "UTF-8");
	    Velocity.setProperty("response.encoding", "UTF-8");
		Velocity.init();
		
		VelocityContext context = new VelocityContext();
		context.put( "title", extractor.getProperties().get("title") );
		context.put( "head", extractor.getHead() );
		context.put( "body", extractor.getBody() );
		for( Object key : properties.keySet() ) {
			context.put( (String) key, properties.get(key));
		}
		
		Reader template = new FileReader( velocityTemplate );
		
		StringWriter writer = new StringWriter();
		
		Velocity.evaluate( context, writer, "MERGE", template );
		
		FileWriter output = new FileWriter( htmlFile );
		IOUtils.write( writer.toString(), output );
		output.close();
	}

}
