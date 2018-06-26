package org.icipe.epfa.connectiontor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;

public class Rserve {
	
//	static String pathRWinInstalled = "C:/R-2.15.1/";
	static String pathRWinInstalled = "c:/R-3.2.3/";
	static String pathRMacInstalled = "/Applications/R.app";
	static String strOSArq="i386";
	
	public static void installWinRequisites(){
		InstallRequirementsUI ui = new InstallRequirementsUI();
		ui.createContents();
		
		String[][] arrayInstallResume = new String[2][2];
		arrayInstallResume[0][0] = "Is R 3.2.3 installed in " +'"' + "C:\\" + '"' + '?';
		arrayInstallResume[1][0] = "Are Rserve and R libraries installed ?";
		
		if(new File(pathRWinInstalled).exists()){
			arrayInstallResume[0][1] = "Installed";
			InstallRequirementsUI.progressBar.setSelection(InstallRequirementsUI.progressBar.getSelection() + 50);
		}else
			arrayInstallResume[0][1] = "Not Installed";
		
		if(new File(pathRWinInstalled + File.separator + "bin" + File.separator + strOSArq + File.separator +"Rserve.exe").exists()){
			arrayInstallResume[1][1] = "Installed";
			InstallRequirementsUI.progressBar.setSelection(InstallRequirementsUI.progressBar.getSelection() + 50);
		}else
			arrayInstallResume[1][1] = "Not Installed";
		
		TableItem tItem = new TableItem(ui.table, SWT.NONE);
		tItem.setText(arrayInstallResume[0]);
		TableItem tItem1 = new TableItem(ui.table, SWT.NONE);
		tItem1.setText(arrayInstallResume[1]);
		
		ui.open();
	}
	
	public static void installMacRequisites(){
		InstallRequirementsMacUI ui = new InstallRequirementsMacUI();
		ui.createContents();
		
		String[][] arrayInstallResume = new String[13][2];
		arrayInstallResume[0][0] = "Is R 3.2.3 installed ?";
		arrayInstallResume[1][0] = "Rserve library";
		arrayInstallResume[2][0] = "Minpack library";
		arrayInstallResume[3][0] = "R2HTML library";
		arrayInstallResume[4][0] = "Sp library";
		arrayInstallResume[5][0] = "EpiR library";
		arrayInstallResume[6][0] = "Maps library";
		arrayInstallResume[7][0] = "Rgdal library";
		arrayInstallResume[8][0] = "Maptools library";
		arrayInstallResume[9][0] = "Rgl library";
		arrayInstallResume[10][0] = "Raster library";
		arrayInstallResume[11][0] = "RColorBrewer library";
		arrayInstallResume[12][0] = "Shapefiles library";
		
		if(new File(pathRMacInstalled).exists())
			arrayInstallResume[0][1] = "Installed";
		else
			arrayInstallResume[0][1] = "Not Installed";
		
		if(new File("/Library/Frameworks/R.framework/Versions/3.2.3/Resources/library/Rserve/").exists())
			arrayInstallResume[1][1] = "Installed";
		else
			arrayInstallResume[1][1] = "Not Installed";
		if(new File("/Library/Frameworks/R.framework/Versions/3.2.3/Resources/library/minpack.lm/").exists())
			arrayInstallResume[2][1] = "Installed";
		else
			arrayInstallResume[2][1] = "Not Installed";
		if(new File("/Library/Frameworks/R.framework/Versions/3.2.3/Resources/library/R2HTML/").exists())
			arrayInstallResume[3][1] = "Installed";
		else
			arrayInstallResume[3][1] = "Not Installed";
		if(new File("/Library/Frameworks/R.framework/Versions/3.2.3/Resources/library/sp/").exists())
			arrayInstallResume[4][1] = "Installed";
		else
			arrayInstallResume[4][1] = "Not Installed";
		if(new File("/Library/Frameworks/R.framework/Versions/3.2.3/Resources/library/epiR/").exists())
			arrayInstallResume[5][1] = "Installed";
		else
			arrayInstallResume[5][1] = "Not Installed";
		if(new File("/Library/Frameworks/R.framework/Versions/3.2.3/Resources/library/maps/").exists())
			arrayInstallResume[6][1] = "Installed";
		else
			arrayInstallResume[6][1] = "Not Installed";
		if(new File("/Library/Frameworks/R.framework/Versions/3.2.3/Resources/library/rgdal/").exists())
			arrayInstallResume[7][1] = "Installed";
		else
			arrayInstallResume[7][1] = "Not Installed";
		if(new File("/Library/Frameworks/R.framework/Versions/3.2.3/Resources/library/maptools/").exists())
			arrayInstallResume[8][1] = "Installed";
		else
			arrayInstallResume[8][1] = "Not Installed";
		if(new File("/Library/Frameworks/R.framework/Versions/3.2.3/Resources/library/rgl/").exists())
			arrayInstallResume[9][1] = "Installed";
		else
			arrayInstallResume[9][1] = "Not Installed";
		if(new File("/Library/Frameworks/R.framework/Versions/3.2.3/Resources/library/raster/").exists())
			arrayInstallResume[10][1] = "Installed";
		else
			arrayInstallResume[10][1] = "Not Installed";
		if(new File("/Library/Frameworks/R.framework/Versions/3.2.3/Resources/library/RColorBrewer/").exists())
			arrayInstallResume[11][1] = "Installed";
		else
			arrayInstallResume[11][1] = "Not Installed";
		if(new File("/Library/Frameworks/R.framework/Versions/3.2.3/Resources/library/shapefiles/").exists())
			arrayInstallResume[12][1] = "Installed";
		else
			arrayInstallResume[12][1] = "Not Installed";
		
		for(int i=0; i<arrayInstallResume.length; i++){
			TableItem tItem = new TableItem(ui.table, SWT.NONE);
			tItem.setText(arrayInstallResume[i]);
		}
		
		ui.open();
	}
	
	public static void installSoftwares(String strPath, int intItem, Table table){
		if(strPath.equalsIgnoreCase("CD:\\\\ Requirements\\\\")){
			MessageDialog.openError(new Shell(), "Error", "You must to select a existing path");
			return;
		}
		
		String state = table.getItem(intItem).getText(1);
		switch (intItem) {
		case 0:/**R-3.2.3 **/
			if(state.equalsIgnoreCase("Installed")){
				MessageDialog.openInformation(new Shell(), "System Requirements", "R-3.2.3 is alredy installed.");
				return;
			}
			try {
				Runtime.getRuntime().exec(strPath + File.separator+ "R" + File.separator + "R-3.2.3-win.exe");
				table.getItem(intItem).setText(1, "Installed");
				
				if(new File(pathRWinInstalled + "/bin/x64").exists())
					strOSArq = "x64";
				else
					strOSArq = "i386";
				
			} catch (IOException e) {
				e.printStackTrace();
				MessageDialog.openError(new Shell(), "System Requirements", "The executable file can't be loaded");
				return;
			}
			
			InstallRequirementsUI.progressBar.setSelection(InstallRequirementsUI.progressBar.getSelection() + 50);
			break;
			
		case 1:/** Rserve**/
			RConnection c=null;
			
			try {
				if(new File(pathRWinInstalled + "/bin/x64").exists())
					copyFile(new File(strPath + File.separator+ "R" + File.separator + "Rserve" + File.separator + "x64" + File.separator+"Rserve.exe"), 
							new File(pathRWinInstalled + File.separator+"bin"+File.separator+"x64"+File.separator+"Rserve.exe"));
					copyFile(new File(strPath + File.separator+ "R" + File.separator + "Rserve" + File.separator + strOSArq + File.separator+"Rserve.exe"), 
							new File(pathRWinInstalled + File.separator+"bin"+File.separator+strOSArq+File.separator+"Rserve.exe"));
				
			} catch (IOException e1) {
				e1.printStackTrace();
				MessageDialog.openError(new Shell(), "System Requirements", "The executable file can't be installed");
				return;
			}
			
			if(!Platform.getOS().equalsIgnoreCase("macosx"))
				c = Rserve.launchRserve("",c);
			else{
				try {
					c=new RConnection();
				} catch (RserveException e2) {
					do{
						try {
							c = new RConnection();
						} catch (RserveException e) {
							e.printStackTrace();
						}
					}while (c == null || !c.isConnected());
				}
			}
			
			strPath = (strPath + File.separator+ "R" + File.separator).replace("\\", "/");
			try {
				c.eval("install.packages(pkgs=" + '"' + strPath +  "minpack.lm_1.2-1.zip" + '"' + ",repos=NULL, contriburl=NULL)");
				
				c.eval("install.packages(pkgs=" + '"' + strPath +  "DBI_0.5-1.zip" + '"' + ",repos=NULL, contriburl=NULL)");
				c.eval("install.packages(pkgs=" + '"' + strPath +  "assertthat_0.1.zip" + '"' + ",repos=NULL, contriburl=NULL)");
				c.eval("install.packages(pkgs=" + '"' + strPath +  "R6_2.2.0.zip" + '"' + ",repos=NULL, contriburl=NULL)");
				c.eval("install.packages(pkgs=" + '"' + strPath +  "Rcpp_0.12.9.zip" + '"' + ",repos=NULL, contriburl=NULL)");
				c.eval("install.packages(pkgs=" + '"' + strPath +  "tibble_1.2.zip" + '"' + ",repos=NULL, contriburl=NULL)");				
				c.eval("install.packages(pkgs=" + '"' + strPath +  "lazyeval_0.2.0.zip" + '"' + ",repos=NULL, contriburl=NULL)");				
				c.eval("install.packages(pkgs=" + '"' + strPath +  "dplyr_0.5.0.zip" + '"' + ",repos=NULL, contriburl=NULL)");
				
			//	c.eval("install.packages(pkgs=" + '"' + strPath +  "MASS_7.3-49.zip" + '"' + ",repos=NULL, contriburl=NULL)");

				c.eval("install.packages(pkgs=" + '"' + strPath +  "R2HTML_2.3.2.zip" + '"' + ",repos=NULL, contriburl=NULL)");
				c.eval("install.packages(pkgs=" + '"' + strPath +  "sp_1.2-4.zip" + '"' + ",repos=NULL, contriburl=NULL)");
				c.eval("install.packages(pkgs=" + '"' + strPath +  "epiR_0.9-79.zip" + '"' + ",repos=NULL, contriburl=NULL)");
				c.eval("install.packages(pkgs=" + '"' + strPath +  "maps_3.1.1.zip" + '"' + ",repos=NULL, contriburl=NULL)");
				c.eval("install.packages(pkgs=" + '"' + strPath +  "rgdal_0.8-9.zip" + '"' + ",repos=NULL, contriburl=NULL)");
				c.eval("install.packages(pkgs=" + '"' + strPath +  "maptools_0.8-41.zip" + '"' + ",repos=NULL, contriburl=NULL)");
				c.eval("install.packages(pkgs=" + '"' + strPath +  "rgl_0.97.0.zip" + '"' + ",repos=NULL, contriburl=NULL)");
				c.eval("install.packages(pkgs=" + '"' + strPath +  "raster_2.5-8.zip" + '"' + ",repos=NULL, contriburl=NULL)");
				c.eval("install.packages(pkgs=" + '"' + strPath +  "RColorBrewer_1.1-2.zip" + '"' + ",repos=NULL, contriburl=NULL)");
				c.eval("install.packages(pkgs=" + '"' + strPath +  "shapefiles_0.7.zip" + '"' + ",repos=NULL, contriburl=NULL)");
				c.eval("install.packages(pkgs=" + '"' + strPath +  "iterators_1.0.8.zip" + '"' + ",repos=NULL, contriburl=NULL)");
				c.eval("install.packages(pkgs=" + '"' + strPath +  "foreach_1.4.3.zip" + '"' + ",repos=NULL, contriburl=NULL)");
				c.eval("install.packages(pkgs=" + '"' + strPath +  "digest_0.6.12.zip" + '"' + ",repos=NULL, contriburl=NULL)");
				c.eval("install.packages(pkgs=" + '"' + strPath +  "stringr_1.1.0.zip" + '"' + ",repos=NULL, contriburl=NULL)");
				c.eval("install.packages(pkgs=" + '"' + strPath +  "xtable_1.8-2.zip" + '"' + ",repos=NULL, contriburl=NULL)");				
				c.eval("install.packages(pkgs=" + '"' + strPath +  "registry_0.3.zip" + '"' + ",repos=NULL, contriburl=NULL)");
				c.eval("install.packages(pkgs=" + '"' + strPath +  "stringi_1.1.2.zip" + '"' + ",repos=NULL, contriburl=NULL)");
				c.eval("install.packages(pkgs=" + '"' + strPath +  "magrittr_1.5.zip" + '"' + ",repos=NULL, contriburl=NULL)");				
				c.eval("install.packages(pkgs=" + '"' + strPath +  "pkgmaker_0.22.zip" + '"' + ",repos=NULL, contriburl=NULL)");
				c.eval("install.packages(pkgs=" + '"' + strPath +  "rngtools_1.2.4.zip" + '"' + ",repos=NULL, contriburl=NULL)");
				c.eval("install.packages(pkgs=" + '"' + strPath +  "plyr_1.8.4.zip" + '"' + ",repos=NULL, contriburl=NULL)");
				c.eval("install.packages(pkgs=" + '"' + strPath +  "doRNG_1.6.zip" + '"' + ",repos=NULL, contriburl=NULL)");
				
				c.eval("install.packages(pkgs=" + '"' + strPath +  "mda_0.4-9.zip" + '"' + ",repos=NULL, contriburl=NULL)");
				c.eval("install.packages(pkgs=" + '"' + strPath +  "fields_8.10.zip" + '"' + ",repos=NULL, contriburl=NULL)");
				c.eval("install.packages(pkgs=" + '"' + strPath +  "spam_1.4-0.zip" + '"' + ",repos=NULL, contriburl=NULL)");
				c.eval("install.packages(pkgs=" + '"' + strPath +  "ggplot2_2.2.1.zip" + '"' + ",repos=NULL, contriburl=NULL)");
				c.eval("install.packages(pkgs=" + '"' + strPath +  "gtable_0.2.0.zip" + '"' + ",repos=NULL, contriburl=NULL)");
				c.eval("install.packages(pkgs=" + '"' + strPath +  "memoise_1.0.0.zip" + '"' + ",repos=NULL, contriburl=NULL)");
				c.eval("install.packages(pkgs=" + '"' + strPath +  "proto_1.0.0.zip" + '"' + ",repos=NULL, contriburl=NULL)");
				c.eval("install.packages(pkgs=" + '"' + strPath +  "reshape2_1.4.2.zip" + '"' + ",repos=NULL, contriburl=NULL)");
				c.eval("install.packages(pkgs=" + '"' + strPath +  "scales_0.4.1.zip" + '"' + ",repos=NULL, contriburl=NULL)");
				c.eval("install.packages(pkgs=" + '"' + strPath +  "munsell_0.4.3.zip" + '"' + ",repos=NULL, contriburl=NULL)");
				c.eval("install.packages(pkgs=" + '"' + strPath +  "colorspace_1.3-2.zip" + '"' + ",repos=NULL, contriburl=NULL)");
				c.eval("install.packages(pkgs=" + '"' + strPath +  "dichromat_2.0-0.zip" + '"' + ",repos=NULL, contriburl=NULL)");
				c.eval("install.packages(pkgs=" + '"' + strPath +  "labeling_0.3.zip" + '"' + ",repos=NULL, contriburl=NULL)");
				c.eval("install.packages(pkgs=" + '"' + strPath +  "RODBC_1.3-14.zip" + '"' + ",repos=NULL, contriburl=NULL)");
				c.eval("install.packages(pkgs=" + '"' + strPath +  "plotGoogleMaps_2.2.zip" + '"' + ",repos=NULL, contriburl=NULL)");
				c.eval("install.packages(pkgs=" + '"' + strPath +  "rgeos_0.3-22.zip" + '"' + ",repos=NULL, contriburl=NULL)");
				
				
				
				
				c.close();
			} catch (RserveException e) {
				e.printStackTrace();
				c.close();
				return;
			} 
			
			InstallRequirementsUI.progressBar.setSelection(InstallRequirementsUI.progressBar.getSelection() + 50);
			table.getItem(intItem).setText(1, "Installed");
			
			break;
		}
	}
	
	static void copyFile(File src, File dst) throws IOException {
		InputStream in = new FileInputStream(src);
		OutputStream out = new FileOutputStream(dst);
		// Transfer bytes from in to out 
		byte[] buf = new byte[1024]; 
		int len;
		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		} 
		in.close();
		out.close();
	}
	
	public static RConnection launchRserve(String strPath, RConnection c){
		
		if(new File(pathRWinInstalled + File.separator + "bin" + File.separator + "x64").exists())
			strOSArq = "x64";
		else
			strOSArq = "i386";
		
		if(!listRunningProcesses(strPath)){
			try {
				Runtime.getRuntime().exec(pathRWinInstalled + File.separator + "bin"+ File.separator + strOSArq + File.separator + "Rserve.exe");
				System.out.println("Rserve is running");
			} catch (IOException e) {
				e.printStackTrace();
				
			}
		}
		
		try {
			c=new RConnection();
		} catch (RserveException e2) {
			do{
				try {
					c = new RConnection();
				} catch (RserveException e) {
					e.printStackTrace();
				}
			}while (c == null || !c.isConnected());
		}
		
		return c;
	}
	
	static boolean listRunningProcesses(String strPath) {
		List<String> processes = new ArrayList<String>();
		Process p=null;
		
		String line;
		try {
			p = Runtime.getRuntime().exec("tasklist.exe /nh");
		}catch (Exception err) {
			err.printStackTrace();
			
			try {
				if(!new File("c:/windows/system32/" + File.separator + "tasklist.exe").exists())
					copyFile(new File(strPath + "\\tasklist.exe"), new File("c:/windows/system32/" + File.separator + "tasklist.exe"));
				
				listRunningProcesses(strPath);
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
		
		try {
			while ((line = input.readLine()) != null) {
				if (!line.trim().equals("")) {
					processes.add(line.substring(0, line.indexOf(" ")));
				}

			}
			input.close();
			
			for(int i=0;i<processes.size();i++){
				if(processes.get(i).equalsIgnoreCase("Rserve.exe"))
					return true;			
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		p.destroy();
		
		return false;
	}

	public static void saveIlcymError(String strPath, String nameFile, String saveToR){
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(strPath + File.separator + nameFile + "-Error.r"));
			bw.write(saveToR);
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
