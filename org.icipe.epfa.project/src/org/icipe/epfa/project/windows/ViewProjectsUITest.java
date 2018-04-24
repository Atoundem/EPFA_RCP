package org.icipe.epfa.project.windows;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import org.icipe.epfa.classUtils.ArrayConvertions;

public class ViewProjectsUITest 
{
	private static Tree tree;
	private static Image fileIm, dir, imgIcon, txtIcon;
	public static String pathProject = "C:/Users/Ritter/Desktop/EPFA/", nameProject = "NameProjttest";
	
//	public static String pathProject = "dfd", nameProject = "MonPojet";  //for test

	public ViewProjectsUITest() {}
	
	
	public static String getPathProject() {
		return pathProject;
	}


	public static void setPathProject(String pathProject) {
		ViewProjectsUITest.pathProject = pathProject;
	}


	public static void refreshTree(){
		try{
			tree.removeAll();
			File[] roots = new File(Platform.getLocation().toFile().getAbsolutePath()).listFiles();
		    for (int i = 1; i < roots.length; i++) {
		      TreeItem root = new TreeItem(tree, 0);
		      root.setText(roots[i].getName().toString());
		      root.setData(roots[i]);
		      if(roots[i].isDirectory())
		    	  root.setImage(dir);
		      if(roots[i].isFile()){
		    	  String ext = ExtractExt(roots[i].toString());
		    	  if(ext.equalsIgnoreCase(".html"))
		    		  root.setImage(fileIm);  
		    	  if(ext.equalsIgnoreCase(".png"))
		    		  root.setImage(imgIcon);
		    	  if(ext.equalsIgnoreCase(".txt"))
		    		  root.setImage(txtIcon);
		    	  if(ext.equalsIgnoreCase(".epfa"))
		    		  root.setImage(txtIcon);
		      }
		    	  
		      
		      new TreeItem(root, 0);
		    }
		}catch (Exception e) {
			e.printStackTrace();
			MessageDialog.openError(new Shell(), "title", e.getMessage());
		}
	}
	
	public static void deleteProject(){
		try {
			Process process = Runtime.getRuntime().exec("taskkill /F /IM Rserve.exe");
			process.destroy();
		} catch (IOException e) {
			e.printStackTrace();
		}
		File file = new File(pathProject);
		
		if(file.isFile())
			deleteFile(file);
		else
			deleteFolder(file.getPath());
		
		file.delete(); 
	}
	
	static void deleteFile(File file){
		file.delete();
	}
	static void deleteFolder(String folder){
		String [] files = new File(folder).list();
		for(int i=0;i<files.length; i++){
			File file1= new File(folder + File.separator + files[i]);
			
			if(file1.isDirectory()){
				deleteFolder(file1.getPath());
				file1.delete();
			}else
				deleteFile(file1);
		}
	}
	
	public static String ExtractExt(String strCadena){
        String strExt = null;
        try{
	        int fin = strCadena.lastIndexOf(".");
	        strExt = strCadena.substring(fin);
        }catch (Exception e) {
			e.printStackTrace();
		}
        return strExt;
    }
	
/*	public static String getRate(){
		String rate="";
		try {
			String rFile = pathProject + File.separator + nameProject + ".epfa";
			
			Properties prop = new Properties();
		
			prop.load(new FileInputStream(rFile));
			rate = prop.getProperty("Ratio");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return rate;
	}
	
	public static String[] getLifeStages(){
		String lifeStages[]= new String[2];
		String rFile = pathProject + File.separator + nameProject + ".epfa";
		Properties prop = new Properties();
		
		try {
			prop.load(new FileInputStream(rFile));
			String[] im = ArrayConvertions.StringtoArray(prop.getProperty("ImmadureStages"),",");
			String[] mad = ArrayConvertions.StringtoArray(prop.getProperty("AdultsStages"),",");
			
			String strIn="", strMad="";
			
			for(int i=0; i<im.length; i++){
				if(i == im.length-1)
					strIn += im[i].trim();
				else
					strIn += im[i].trim() + ',';
			}
			
			for(int i=0; i<mad.length; i++){
				if(i == mad.length-1)
					strMad += mad[i].trim();
				else
					strMad += mad[i].trim() + ',';
			}
			
			lifeStages[0] = strIn;
			lifeStages[1] = strMad;
			
			
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return lifeStages;
	}
	
	public static String getNumStage(String stage){
		//String rFile = pathProject + File.separator + nameProject + ".ilcym";
		String rFile = pathProject + File.separator + nameProject + ".epfa";
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(rFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String[] im = ArrayConvertions.StringtoArray(prop.getProperty("ImmadureStages"),",");
		String[] mad = ArrayConvertions.StringtoArray(prop.getProperty("AdultsStages"),",");
		String posStage="";
		
		for(int i=0; i<im.length;i++){
			if(im[i].trim().equalsIgnoreCase(stage))
				posStage = i+1 +"";
		}
		
		for(int i=0; i<mad.length;i++){
			if(mad[i].trim().equalsIgnoreCase(stage))
				posStage = (i+1)+im.length +"";
		}
		return posStage;
	}
	*/
	


}
