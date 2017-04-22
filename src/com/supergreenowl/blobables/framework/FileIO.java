package com.supergreenowl.blobables.framework;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface FileIO {

	public InputStream readAsset(String filename) throws IOException;
	
	public InputStream readFile(String filename) throws IOException;
	
	public OutputStream writeFile(String filename) throws IOException;
	
	public int getPreference(String key, int defaultValue);
	
	public void setPreference(String key, int value);
	
}
