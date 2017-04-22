package com.supergreenowl.blobables.framework;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Environment;

public class FileManager implements FileIO {

	
	private Activity activity;
	private AssetManager assets;
	private String externalStorage;
	
	public FileManager(Activity activity, AssetManager am) {
		this.activity = activity;
		assets = am;
		externalStorage = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
	}
	
	@Override
	public InputStream readAsset(String filename) throws IOException {
		return assets.open(filename);
	}

	@Override
	public InputStream readFile(String filename) throws IOException {
		return new FileInputStream(externalStorage + filename);
	}

	@Override
	public OutputStream writeFile(String filename) throws IOException {
		String filepath = externalStorage + filename;
		
		File file = new File(filepath.substring(0, filepath.lastIndexOf(File.separator)));
		if(!file.exists())
			file.mkdirs();
		
		return new FileOutputStream(filepath);
	}
	
	@Override
	public int getPreference(String key, int defaultValue) {
		SharedPreferences prefs = activity.getPreferences(Activity.MODE_PRIVATE);
		return prefs.getInt(key, defaultValue);
	}
	
	@Override
	public void setPreference(String key, int value) {
		activity.getPreferences(Activity.MODE_PRIVATE).edit().putInt(key, value).commit();
	}

}