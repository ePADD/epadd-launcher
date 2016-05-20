package test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.DosFileAttributes;

public class DirLocation {
	
	
	private int MAX_PENETRATION = 3;
	private String loc;
	public String getDirAbsoluteLoc(String directory){
		try {
			System.out.println(searchDirPath(directory));
			return "";
		} catch (Exception e) {
			System.out.println(e);
			return loc;
		}
	}
	
	private String searchDirPath(String dir) throws Exception{
		File[] partitions = File.listRoots();
		for(File partition: partitions){
			System.out.println("Iterating through " + partition.getAbsolutePath() + "...");
			if(!partition.toString().equals("A:\\")){	
				for (String directory : partition.list()) {
					iterateDir(partition+directory, dir, 0);
				}
			}
		}
		return "Not Found!!";
	}
	
	private String iterateDir(String base, String matchingDir, int itr) throws Exception{
		if(loc==null){
			if(base.contains(matchingDir)){
				loc = base;
				throw new Exception();
			}
			if(!isSystemFile(base)){
				itr++;
				File baseDir = new File(base);
				if(baseDir.isDirectory()){
					if(baseDir.list() != null){
						for(String curr: baseDir.list()){
							if(itr < MAX_PENETRATION)
								iterateDir(base +"/" +curr, matchingDir, itr);
						}
					}
				}
			}
		}
		
		return null;
	}
	
	private boolean isSystemFile(String name){
		try {
			Path path = Paths.get(name);
			return Files.readAttributes(path, DosFileAttributes.class).isSystem();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
}
