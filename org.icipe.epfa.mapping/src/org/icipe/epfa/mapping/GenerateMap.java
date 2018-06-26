package org.icipe.epfa.mapping;

import java.io.File;
import java.text.NumberFormat;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.icipe.epfa.classUtils.EPFAUtils;
import org.icipe.epfa.connectiontor.Rserve;
import org.icipe.epfa.project.windows.ViewProjectsUI;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;




public class GenerateMap 
{
	static RConnection c;
	public static boolean stateMapCreation = false;
	
	public static String[] getLocationExtent(String pathData)
	{
		
			String savetoR="";
		   double doubminX = -180;
		   double doubmaxX = 180;
		   double doubminY = -90;
		   double doubmaxY = 90;
		  
		   double douValue;
		   douValue = -180;
		   
		   String pathDatas  = (pathData).replace("\\","/");
//c=new RConnection();
		//c = EPF.getConnection();
		if (c == null || !c.isConnected())
			if(!Platform.getOS().equalsIgnoreCase("macosx")){
				c = Rserve.launchRserve("",c);
				System.out.println("Launched !!!!!!!!!!!");
			}
			//c= new RConnection();
		System.out.println("Connect!");
		   
		   try {
			
			savetoR += "library(MASS)"+"\r\n";
			c.eval("library(MASS)");
			c.eval("library(sp)");
			   c.eval("library(rgdal)");
			
			c.eval("library(maptools)");		   
		   c.eval("library(maps)");
		   c.eval("library(doRNG)");
		   
		   
		   		   
		   savetoR += "archivos1=list.files('"+pathDatas+"',pattern='flt');archivos1=paste('"+pathDatas+"','/',archivos1,sep='')"+"\r\n";	
	        c.eval("archivos1=list.files('"+pathDatas+"',pattern='flt');archivos1=paste('"+pathDatas+"','/',archivos1,sep='')");
	        
	        c.eval("Tmin1=readGDAL(archivos1[1])");
	        
	        c.eval("matr = bbox(Tmin1)");
	        
	        doubminX = c.eval("round(matr['x','min'],2)").asDouble();
	        doubmaxX = c.eval("round(matr['x','max'],2)").asDouble();	        
	        doubminY = c.eval("round(matr['y','min'],2)").asDouble();
	        doubmaxY = c.eval("round(matr['y','max'],2)").asDouble();
	        		
	        		
	        		
/*	         		
		   
		   while(!(douValue>=doubminX)){
		       douValue=douValue+(dbres);            
		   }        
		   doubminX=douValue;
		   douValue=180;
		   
		   while (!(douValue<=doubmaxX)){
		       douValue=douValue-(dbres);     //16666666667   
		   }   
		   doubmaxX=douValue;
		   douValue=-90;
		   
		   while (!(douValue>=doubminY)){
		       douValue=douValue+(dbres);        
		   }
		   doubminY=douValue;
		   douValue=90;
		   
		   while (!(douValue<=doubmaxY)){
		       douValue=douValue-(dbres);        
		   }
		   doubmaxY=douValue;
		   
		   NumberFormat numberFormat = NumberFormat.getNumberInstance();
		   numberFormat.setMaximumFractionDigits(4);
		   
*/
	        c.close();
		   } catch (RserveException e) {
				// TODO Auto-generated catch block
			   	c.close();
				e.printStackTrace();
				MessageDialog.openError(new Shell(), "Error", "Verity that the path to climatic data is correct ");
    			return null;
			} catch (REXPMismatchException e1) {
			// TODO Auto-generated catch block
				c.close();
				e1.printStackTrace();
				MessageDialog.openError(new Shell(), "Error", "Verity that the path to climatic data is correct ");
				return null;	
			
			
		}	  
		   
		   
	return new String[] {doubminX+"", doubmaxX+"", doubminY+"",doubmaxY+""};
		            
	}

	public static String[] AdjustDimensions(String strminXp, String strmaxXp, String strminYp, String strmaxYp, double dbres)
	{
		
		   double doubminX = Double.valueOf(strminXp).doubleValue();
		   double doubmaxX = Double.valueOf(strmaxXp).doubleValue();
		   double doubminY = Double.valueOf(strminYp).doubleValue();
		   double doubmaxY = Double.valueOf(strmaxYp).doubleValue();
		   double douValue;
		   douValue = -180;
		   
		   while(!(douValue>=doubminX)){
		       douValue=douValue+(dbres);            
		   }        
		   doubminX=douValue;
		   douValue=180;
		   
		   while (!(douValue<=doubmaxX)){
		       douValue=douValue-(dbres);     //16666666667   
		   }   
		   doubmaxX=douValue;
		   douValue=-90;
		   
		   while (!(douValue>=doubminY)){
		       douValue=douValue+(dbres);        
		   }
		   doubminY=douValue;
		   douValue=90;
		   
		   while (!(douValue<=doubmaxY)){
		       douValue=douValue-(dbres);        
		   }
		   doubmaxY=douValue;
		   
		   NumberFormat numberFormat = NumberFormat.getNumberInstance();
		   numberFormat.setMaximumFractionDigits(4);
		   try {
		       return new String[] {numberFormat.parse(numberFormat.format(doubminX))+"", numberFormat.parse(numberFormat.format(doubmaxX))+"", numberFormat.parse(numberFormat.format(doubminY))+"", numberFormat.parse(numberFormat.format(doubmaxY))+""};
		   } catch (Exception e) {
		       return null;
		   }          
	}
	
	
	public static void calculateIndices(String strR, String pathTempMin,String pathTempMax,
    		String dbMinX, String dbMaxX, String dbMinY, String dbMaxY, String strOutput, boolean rbFilter, String tmin, String tmax)
	{
		String strPath = new File(strOutput).getParent().replace("\\", "/");
    	String strName = EPFAUtils.ExtractPath(new File(strOutput).getName()); 
    	
    	System.out.println(new File(strOutput).getName());
    		
    	String savetoR="";
    	
//String pathPhenoGis,          	
//    	strClima
    	/*if(!Platform.getOS().equalsIgnoreCase("macosx"))
			c = Rserve.launchRserve("",c);
		else{*/
			try {
//				c=new RConnection();
//				c = EPF.getConnection();
				if (c == null || !c.isConnected())
					c= new RConnection();
				System.out.println("Connect!");
			} catch (RserveException e2) {
				do{
					try {
						c = new RConnection();
					} catch (RserveException e) {
						e.printStackTrace();
					}
				}while (c == null || !c.isConnected());
			}
//		}
    	
    	//String lib = EPFAUtils.getScriptPath();
    	try{
    		
    		EPFAUtils.createTempScriptFile("/RScripts/spatialTransform.r");
/*    		
    		savetoR += "source('"+MainPageWizardPage.getstrMortalityPath()+"/tempScripFile.r'"+")"+"\r\n";	
    		c.eval("source('"+MainPageWizardPage.getstrMortalityPath()+"/tempScripFile.r'"+")");
*/	           
	           
    		savetoR += "source('"+(ViewProjectsUI.getPathProject()+ File.separator).replace('\\','/') +"tempScripFile.r'"+")"+"\r\n";
	        c.eval("source('"+(ViewProjectsUI.getPathProject()+ File.separator).replace('\\','/')+"tempScripFile.r'"+")");

 //   		savetoR += "source('"+ViewProjectsUI.getPathMortality()+"/tempScripFile.r'"+")"+"\r\n";	
 //   		c.eval("source('"+ViewProjectsUI.getPathMortality()+"/tempScripFile.r'"+")");
    		
//	        savetoR += "source('"+MainPageWizardPage.getstrMortalityPath()+"tempScripFile.r'"+")"+"\r\n";	
//	        c.eval("source('"+MainPageWizardPage.getstrMortalityPath()+"tempScripFile.r'"+")");
    		
    		//savetoR = "source('" + lib + "/spatialTransform.r')" + "\n";
			//c.eval("source('" + lib + "/spatialTransform.r')");
			System.out.println("Scrip load!");
/*			
			savetoR +="load("+ '"' + MainPageWizardPage.getstrMortalityPath() + "/" + "EPFA_"+"Mortality.RData"+'"'+")"+"\n";
			c.eval("load("+ '"' + MainPageWizardPage.getstrMortalityPath() + "/" + "EPFA_"+"Mortality.RData"+'"'+")");
*/			
			
			savetoR +="load("+ '"' +ViewProjectsUI.getPathMortality() + "EPFA_"+"Mortality.RData"+'"'+")"+"\n";
			c.eval("load("+ '"' + ViewProjectsUI.getPathMortality() + "EPFA_"+"Mortality.RData"+'"'+")");
			
			System.out.println("Rdata load!");
			
//			savetoR += "load(" + '"' + pathPhenoGis + '"' + ")"+"\n";
//			c.eval("load(" + '"' + pathPhenoGis + '"' + ")");
		
/*			savetoR += "modelim<-params$modelim"+"\n";
			c.eval("modelim<-params$modelim");
			savetoR += "modelm<-params$modelm"+"\n";
			c.eval("modelm<-params$modelm");
			savetoR += "estadios<-params$estadios"+"\n";
			c.eval("estadios<-params$estadios");
			savetoR += "hfeno<-params$hfeno"+"\n";
			c.eval("hfeno<-params$hfeno");
			savetoR += "xi<-params$xi"+"\n";
			c.eval("xi<-params$xi");
			
			
			savetoR += "library(sp)"+"\n";
			c.eval("library(sp)");
			savetoR += "library(maptools)"+"\n";
			c.eval("library(maptools)");
			savetoR += "library(rgdal)"+"\n";
			c.eval("library(rgdal)");
			savetoR += "library(maps)"+"\n";
			c.eval("library(maps)");
			savetoR += "library(epiR)"+"\n";
			c.eval("library(epiR)");
			savetoR += "library(doRNG)"+"\n";
			c.eval("library(doRNG)");
			*/
			
			savetoR += "ilon=c(" + dbMinX + ',' + dbMaxX + ')'+"\n";
			c.eval("ilon=c(" + dbMinX + ',' + dbMaxX + ')');
			savetoR += "ilat=c(" + dbMinY + ',' + dbMaxY + ')'+"\n";
			c.eval("ilat=c(" + dbMinY + ',' + dbMaxY + ')');
			
			savetoR += "dir1 = " + '"' + pathTempMin + '"'+"\n"; //savetoR += "dir1 = " + '"' + strClima + "/tmin/" + '"'+"\n";
			c.eval("dir1 = " + '"' + pathTempMin + '"');
			savetoR += "dir2 = " + '"' + pathTempMax + '"'+"\n";  //savetoR += "dir2 = " + '"' + strClima + "/tmax/" + '"'+"\n";
			c.eval("dir2 = " + '"' + pathTempMax + '"');
			
			savetoR += "R<-"+strR+"\n";
			c.eval("R<-"+strR);
			
//			savetoR += "steps <- 48"+"\n";
//			c.eval("steps <- 48");
			
			savetoR += "dir.out=" + '"'+ strPath + '/'+'"'+"\n";
			c.eval("dir.out=" + '"'+ strPath + '/'+'"');
			
			savetoR += "name.out <- " + '"' + strName + '"' + "\n";
			c.eval("name.out <- " + '"' + strName + '"');
			/*
			savetoR += "modelim=c(modelim,modelm)" + "\r\n";
			c.eval("modelim=c(modelim,modelm)");
			
			savetoR +="if(length(estadios)>length(modelim)){modelim = c(modelim,15)}"+"\r\n";
			c.eval("if(length(estadios)>length(modelim)){modelim = c(modelim,15)}");
			
			savetoR+= "filtro=NULL" +"\r\n";
			c.eval("filtro=NULL");*/
			if(rbFilter){
				savetoR += "filtro=c("+tmin + ',' + tmax + ")" + "\r\n";
				c.eval("filtro=c("+tmin + ',' + tmax + ")");
			}

//			zone.div<-function(dir1,dir2,ilon,ilat,R,dir.out,name.out=name.out,out,filtro,fmle = NULL,coefEstimated = NULL,alg)
			
			savetoR += "file<-zone.div(dir1,dir2,ilon,ilat,R,dir.out,name.out=name.out,sol_develop$ecuacion,filtro=filtro,tfunc,estshap,alg)"+"\n";
			c.eval("file<-zone.div(dir1,dir2,ilon,ilat,R,dir.out,name.out=name.out,sol_develop$ecuacion,filtro=filtro,tfunc,estshap,alg)");
			
//	file<-zone.div(dir1,dir2,ilon,ilat,R,dir.out,name.out=name.out,sol_develop$ecuacion,filtro,tfunc,estshap,alg)
			
			//IMT.asc
			new File(strPath+File.separator + "IMT.asc").renameTo(new File(new File(strOutput).getParentFile()+File.separator+new File(strOutput).getName().substring(0, new File(strOutput).getName().lastIndexOf('.'))+".asc"));
//			new File(strPath+File.separator + "AI.asc").renameTo(new File(new File(strOutput).getParentFile()+File.separator+new File(strOutput).getName().substring(0, new File(strOutput).getName().lastIndexOf('.'))+"-AI.asc"));
//			new File(strPath+File.separator + "ERI.asc").renameTo(new File(new File(strOutput).getParentFile()+File.separator+new File(strOutput).getName().substring(0, new File(strOutput).getName().lastIndexOf('.'))+"-ERI.asc"));
//			new File(strPath+File.separator + "RO.asc").renameTo(new File(new File(strOutput).getParentFile()+File.separator+new File(strOutput).getName().substring(0, new File(strOutput).getName().lastIndexOf('.'))+"-RO.asc"));
			
			new File(strPath + File.separator + "file1.txt").delete();
			new File(strPath + File.separator + "file2.txt").delete();
			new File(strPath + File.separator + "file3.txt").delete();
			new File(strPath + File.separator + "file4.txt").delete();
			
			c.eval("rm(list = ls())");
			
			EPFAUtils.createTempScriptFile("/RScripts/spatialTransform.r");

/*			
	        savetoR += "source('"+MainPageWizardPage.getstrMortalityPath()+"tempScripFile.r'"+")"+"\r\n";	
	        c.eval("source('"+MainPageWizardPage.getstrMortalityPath()+"tempScripFile.r'"+")");
*/	        
			//savetoR = "source('" + lib + "/spatialTransform.r')" + "\n";
			//c.eval("source('" + lib + "/spatialTransform.r')");
			
			savetoR += "source('"+(ViewProjectsUI.getPathProject()+ File.separator).replace('\\','/') +"tempScripFile.r'"+")"+"\r\n";
	        c.eval("source('"+(ViewProjectsUI.getPathProject()+ File.separator).replace('\\','/')+"tempScripFile.r'"+")");
			
//			savetoR += "source('"+ViewProjectsUI.getPathMortality()+"tempScripFile.r'"+")"+"\r\n";	
//		    c.eval("source('"+ViewProjectsUI.getPathMortality()+"tempScripFile.r'"+")");
			
			System.out.println("RE Scrip load!");
			savetoR += "dir.out=" + '"'+ strPath + '/'+'"'+"\n";
			c.eval("dir.out=" + '"'+ strPath + '/'+'"');
			
			savetoR += "name.out <- " + '"' + strName + '"' + "\n";
			c.eval("name.out <- " + '"' + strName + '"');
			
			c.eval("png(file=" + '"' +strPath + "/"+strName+".png"+'"'+")");
			
			savetoR+="plotMap(dir.out,name.out)"+"\n";
			c.eval("plotMap(dir.out,name.out)");
			
			c.eval("dev.off()");
			
			setStateMapCreation(true);
//			showTextMessages(MappingDialog.lblStatus, "Done", MappingDialog.lblProblems, "");
			c.close();
			
    	}catch (RserveException e) {
			c.close();
			e.printStackTrace();
			Rserve.saveIlcymError(new File(strOutput).getParent()+"\\", "Mapping", savetoR);
//			showTextMessages(MappingDialog.lblStatus, "Error", MappingDialog.lblProblems, "creating map");
		}
	}
	
	public static boolean isStateMapCreation() {
		return stateMapCreation;
	}


	public static void setStateMapCreation(boolean stateMapCreation) {
		GenerateMap.stateMapCreation = stateMapCreation;
	}

}
