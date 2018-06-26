package org.icipe.epfa.autodissemination;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Properties;

import org.icipe.epfa.connectiontor.Rserve;
import org.icipe.epfa.autodissemination.wizards.MainPageWizardPage;
import org.icipe.epfa.autodissemination.wizards.OneModelWizardPage1;
import org.icipe.epfa.autodissemination.wizards.OneModelWizardPage2;
import org.icipe.epfa.autodissemination.wizards.SeveralModelsWizardPage;
import org.icipe.epfa.autodissemination.windows.LoadPlotAutoDissData;
import org.icipe.epfa.classUtils.ArrayConvertions;
import org.icipe.epfa.classUtils.EPFAUtils;
import org.icipe.epfa.project.windows.ViewProjectsUI;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;

public class AutoDissemination {
	
	public static String[] lstArrayNames = new String[]{"Logistic Models"};
	
	public static String[][] lstMatrixSubNames = new String[][]{{"Simple Logistic","Logisitc 1 parameter","Logisitc 2 parameters","Logisitc 4 parameters","Logistic 3 parameter"}};
	
	public static String[][] lstModelNames = new String[][]{{"Simple Logistic","Logisitc 1 parameter","Logisitc 2 parameters","Logisitc 4 parameters","Logistic 3 parameter"},{"1","2","3","4","5"}};
	
	public static String title = "AutoDissemination";
	public static String imageName = title+".png";
	
	static String saveToR="", strSentSaveImage, strExt;
	static RConnection c;
	public static Parameters pars = new Parameters();
	static REXP rexpAnt, rexpNew;
	static boolean bolBackPars;
	static DecimalFormat df = new DecimalFormat("#####.##########");
	public static String strMinP="", strMaxP="";
	private static String framesSel[]= new String[6];
	public static Shell[] shellFunctions;
	
	static Shell[] arraysShell;
	static Double[] dbStdpars;
	public static ImageProperties ip = new ImageProperties();

	/** Metodo para definir las caracteristicas de la imagen resultante, axis, labels, title, legend **/
	public static void definirParamsImage(){
		try {
			ip = ModifyImageUI.ip;
			ip.setCorrX1("0");
			ip.setCorrX2("150");
			ip.setCorrY1("0");
			ip.setCorrY2("1");
			ip.setMini("0");
			ip.setMaxi("200");
			ip.setLegX("0");
			ip.setLegY("0");
			ip.setLabX("Distance (Meter)");
			ip.setLabY("Insects Proportion (%)");
			ip.setTitle("");
			ip.setScaleY("0.2");
			ip.setScaleX("20");
			
//			if(!Platform.getOS().equalsIgnoreCase("macosx")){
				c = Rserve.launchRserve("",c);
				
				imageName = title+".png";
				strExt = "png";
				strSentSaveImage = "png(file=" + '"' +MainPageWizardPage.getStrAutoDisseminationPath() + "/"+ imageName+'"'+")";
				
/*				
				imageName = "MaizeDevRate-"+ MainPageWizardPage.comboDevStage.getText()+".png";
				strExt = "png";
				
				strSentSaveImage = "png(file=" + '"' +MainPageWizardPage.getStrAutoDisseminationPath() + "/"+"MaizeDevRate-"+ MainPageWizardPage.comboDevStage.getText()+"."+strExt+'"'+")";
//				c.eval("png(file=" + '"' +MainPageWizardPage.getStrAutoDisseminationPath() + "/"+MainPageWizardPage.comboDevStage.getText()+"-Model"+ modelm+".png"+'"'+")");
			}else{
				imageName = title+".jpeg";
				strExt = "jpeg";
				
				strSentSaveImage = "jpeg(file=" + '"' +MainPageWizardPage.getStrAutoDisseminationPath() + "/"+ imageName+'"'+")";
//				strSentSaveImage = "jpeg(file=" + '"' +CreatedMaizeProject.getStrPathProject() + "/"+ imageName+'"'+")";
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
			}*/
			
			saveToR+="corrx=c("+ip.getCorrX1()+','+ip.getCorrX2()+')'+"\r\n";
			c.eval("corrx=c("+ip.getCorrX1()+','+ip.getCorrX2()+')');
			saveToR+="corry=c("+ip.getCorrY1()+','+ip.getCorrY2()+')'+"\r\n";
			c.eval("corry=c("+ip.getCorrY1()+','+ip.getCorrY2()+')');
			saveToR+="mini<-"+ip.getMini()+"\r\n";
			c.eval("mini<-"+ip.getMini());
			saveToR+="maxi<-"+ip.getMaxi()+"\r\n";
			c.eval("maxi<-"+ip.getMaxi());
			saveToR+="labx="+'"'+ip.getLabX()+'"'+"\r\n";
			c.eval("labx="+'"'+ip.getLabX()+'"');
			saveToR+="laby="+'"'+ip.getLabY()+'"'+"\r\n";
			c.eval("laby="+'"'+ip.getLabY()+'"');
			saveToR+="titulo="+'"'+ip.getTitle()+'"'+"\r\n";
			c.eval("titulo="+'"'+ip.getTitle()+'"');
			saveToR+="scaleY="+ip.getScaleY()+"\r\n";
			c.eval("scaleY="+ip.getScaleY());
			saveToR+="scaleX="+ip.getScaleX()+"\r\n";
			c.eval("scaleX="+ip.getScaleX());
			saveToR+="grises=FALSE"+"\r\n";
			c.eval("grises=FALSE");
			
			c.close();
		} catch (RserveException e) {
			Rserve.saveIlcymError(MainPageWizardPage.getStrAutoDisseminationPath(), title, saveToR);
			c.close();
			e.printStackTrace();
			MessageDialog.openError(new Shell(), title, "Problems while trying to set the features of the graph");
		}
	}
	
	/** Metrodo que carga los parametros guardados del tiempo de desarrollo **/
	static void loadParamsFromDTNew(String lib){
		
		saveToR+="## Development Rate ##"+"\r\n";
		saveToR+="## Load params from Development Time ##"+"\r\n";
		
		try {
			//MessageDialog.openInformation(new Shell(), "path", "source("+'"' + lib + "/dev_rate_new.rtf"+'"'+")");//TODO
			saveToR += "source("+'"' + lib + "/dev_rate_new.r"+'"'+")" + "\r\n";
			c.eval("source("+'"' + lib + "/dev_rate_new.r"+'"'+")");
			
			String strwd = (MainActionAutoDiss.pathProj + File.separator + "Data").replace('\\', '/');
			
			saveToR+= "setwd(" + '"' + strwd + '"'+ ')'+"\r\n";
			c.eval("setwd(" + '"' + strwd + '"'+ ')');
			
			saveToR+= "library(minpack.lm)"+"\r\n";
			c.eval("library(minpack.lm)");
			
			saveToR+= "library(MASS)"+"\r\n";
			c.eval("library(MASS)");
			
			saveToR+= "library(dplyr)"+"\r\n";
			c.eval("library(dplyr)");
					
			String pathProject = MainActionAutoDiss.pathProj + File.separator + "DevelopmentTime" + File.separator + MainPageWizardPage.getStageSel();
			pathProject = pathProject.replace("\\", "/");
			saveToR+="load(" + '"' + pathProject + "/toTemp.RData" + '"' +")"+"\r\n";
			c.eval("load(" + '"' + pathProject + "/toTemp.RData" + '"' +")");
			//MessageDialog.openInformation(new Shell(), "path", "cargo datos de dev time");//TODO
		} catch (RserveException e) {
			Rserve.saveIlcymError(MainPageWizardPage.getStrAutoDisseminationPath(), title, saveToR);
			c.close();
			e.printStackTrace();
			MessageDialog.openError(new Shell(), title, "Problems while trying to get the values from Autodissemination");
		}
	}
	
	/** Metodo que procesa todos los modelos seleccionados (cmpAllModelsPage) **/
	
	public static void proceesAllModelsNew(boolean bolExtremMin){
		String path= MainPageWizardPage.getStrAutoDisseminationPath() + File.separator + imageName;
		 
//		String stageSel = MainPageWizardPage.getStageSel();
		String algo = MainPageWizardPage.getSelectAlgo();
		SeveralModelsWizardPage.tableCriterias.removeAll();
		SeveralModelsWizardPage.lblModelSelAll.setText("");
		SeveralModelsWizardPage.txtParametersEstimatedAll.setText("");
//		String pathMotData = MainPageWizardPage.getFileDevStageData();
		String pathAutoDissData = LoadPlotAutoDissData.getFileAutoDisseminationData();
		
		Boolean charge = false;
		
		try {
//			String lib = configTool.getLibPath();
			c = new RConnection();
//			loadParamsFromDTNew(lib);
			if (charge == false)
			{
				saveToR+="datm<-read.table('"+pathAutoDissData+"',header = T)"+"\n";
				c.eval("datm<-read.table('"+pathAutoDissData+"',header = T)");
				
				saveToR+="datm[,2]= datm[,2]+0.00001"+"\n";
				c.eval("datm[,2]= datm[,2]+0.00001");
				
				saveToR+="datashap <- data.frame(x =datm[,1] ,y =datm[,2],Lower =datm[,2], Upper=datm[,2])"+"\n";
				c.eval("datashap <- data.frame(x =datm[,1] ,y =datm[,2],Lower =datm[,2], Upper=datm[,2])");
				
				saveToR+="datao <- data.frame(x =c(datm[,1],datm[,1],datm[,1]) ,y =c(datm[,2],datm[,2],datm[,2]))"+"\n";
				c.eval("datao <- data.frame(x =c(datm[,1],datm[,1],datm[,1]) ,y =c(datm[,2],datm[,2],datm[,2]))");
	//			c.eval("max(datashap[,1])+20");
				ip.setCorrX2(c.eval("max(datashap[,1])+20").asString());
				saveToR+="corry=c("+ip.getCorrY1()+','+ip.getCorrY2()+')'+"\r\n";
				c.eval("corry=c("+ip.getCorrY1()+','+ip.getCorrY2()+')');
				
//			EPFAUtils.createTempScriptFile("/RScripts/mortalityDesigner_New.r");
				
//		        saveToR += "source('"+MainPageWizardPage.getStrAutoDisseminationPath()+"tempScripFile.r'"+")"+"\r\n";	
//		        c.eval("source('"+MainPageWizardPage.getStrAutoDisseminationPath()+"tempScripFile.r'"+")");
		       
				EPFAUtils.createTempScriptFile("/RScripts/AutoDisseminationDesigner.r");
		           
		           saveToR += "source('"+ (ViewProjectsUI.getPathProject()+ File.separator).replace('\\','/')+"tempScripFile.r'"+")"+"\r\n";		          
		           c.eval("source('"+(ViewProjectsUI.getPathProject()+ File.separator).replace('\\','/')+"tempScripFile.r'"+")");
		        
		        System.out.println("charger again");
		        charge = true;
			}
			
			if(bolExtremMin){
				saveToR += "valxs <- c(" + MainPageWizardPage.getExtremValues()[0] + ')'+"\r\n";
				c.eval("valxs <- c(" + MainPageWizardPage.getExtremValues()[0] + ')');
				saveToR += "valxy <- c(" + MainPageWizardPage.getExtremValues()[1] + ')'+"\r\n";
				c.eval("valxy <- c(" + MainPageWizardPage.getExtremValues()[1] + ')');
				
				saveToR += "valx1=data.frame(x=valxs,y=valxy,Lower=valxy,Upper=valxy)" + "\r\n";
				c.eval("valx1=data.frame(x=valxs,y=valxy,Lower=valxy,Upper=valxy)");
				
				saveToR += "datashap<-datashap2<-rbind(valx1,datashap);colnames(datashap2)="+ '"'+'"'+';' + "\r\n";
				c.eval("datashap<-datashap2<-rbind(valx1,datashap);colnames(datashap2)="+ '"'+'"'+';');
				
				saveToR += "datao <- rbind(datashap2[,c(1,2)],datashap2[,c(1,3)],datashap2[,c(1,4)])"+"\r\n";
				c.eval("datao <- rbind(datashap2[,c(1,2)],datashap2[,c(1,3)],datashap2[,c(1,4)])");
				
				saveToR += "colnames(datao)=c("+'"'+"x"+'"'+","+'"'+"y"+'"'+")"+"\r\n";
				c.eval("colnames(datao)=c("+'"'+"x"+'"'+","+'"'+"y"+'"'+")");
				
			}
			strMinP = c.eval("min(as.numeric(rownames(datashap)))").asDouble()+"";
			strMaxP = c.eval("max(as.numeric(rownames(datashap)))").asDouble()+"";
			
			c.close();
		} catch (RserveException e1) {
			c.close();
			e1.printStackTrace();
			MessageDialog.openError(new Shell(), title, "Problems while trying to get the extreme values");
			return;
		} catch (REXPMismatchException e) {
			c.close();
			e.printStackTrace();
			MessageDialog.openError(new Shell(), title, "Problems while trying to get the extreme values");
			return;
		}
		
		arraysShell = new Shell[MainPageWizardPage.lstSelectedModels.getItemCount()];
		for(int i=0; i<MainPageWizardPage.lstSelectedModels.getItemCount(); i++){
			try {	
				int model = getModelNumber(MainPageWizardPage.lstSelectedModels.getItem(i));
//				path =MainPageWizardPage.getStrAutoDisseminationPath() + "/"+MainPageWizardPage.comboDevStage.getText()+"-Model"+ model+"."+strExt;
				Sharpe1UI uiShape1 = new Sharpe1UI(SeveralModelsWizardPage.container.getShell(), model, path, algo);
				uiShape1.createContents(MainPageWizardPage.lstSelectedModels.getItem(i));
				uiShape1.lblFunctImageDR.setBackgroundImage(null);
				
				c = new RConnection();
// Reading of data and shaping them in the required forma. :: TODO: insert it in loadParamsFromDTNew function				
				
				
				double nelt = c.eval("nrow(datashap)").asDouble();
				System.out.println(nelt);
				
				saveToR += "limit<-"+'"'+ MainPageWizardPage.getLimits() +'"' +"\n";
				c.eval("limit<-"+'"'+ MainPageWizardPage.getLimits() +'"');
				
				estimateParameters(c, model, true,  false,"", "", "", "", "", "", "");
				
				
				
//				saveToR += "corry <- c(0,1.5*max(datashap[,2]))" + "\r\n";
//				c.eval("corry <- c(0,1.5*max(datashap[,2]))");
		//		c.eval("corry <- c(0,2*max(datashap[,2]))");   ip.setCorrY1("0");
				
				
//				ip.setCorrY2(c.eval("1.5*max(datashap[,2])").asString());
				
				saveToR += "punt="+strMinP+":" + strMaxP +"\n";
				c.eval("punt="+strMinP+":" + strMaxP);
				//MessageDialog.openInformation(new Shell(), "path", "prueba");//TODO
				saveToR+= "shapprueba<-prueba(model,datashap,datao,ini,corrx,corry,punt,labx, laby, titulo, grises)"+"\r\n";
				c.eval("shapprueba<-prueba(model,datashap,datao,ini,corrx,corry,punt,labx, laby, titulo, grises)");
				//MessageDialog.openInformation(new Shell(), "path", "fin prueba");//TODO
				c.voidEval("dev.off()");
				
				saveToR+= "ini<-shapprueba$ini"+"\r\n";
				c.eval("ini<-shapprueba$ini");
				
				saveToR+= "coefi<-shapprueba$coefi"+"\r\n";
				c.eval("coefi<-shapprueba$coefi");
				
				saveToR+= "p<-shapprueba$p"+"\r\n";
				c.eval("p<-shapprueba$p");
				//MessageDialog.openInformation(new Shell(), "path", "shape");//TODO
				saveToR+="shap<-shape(model,datashap,datao,ini,coefi)"+"\r\n";
				c.eval("shap<-shape(model,datashap,datao,ini,coefi)");
				//MessageDialog.openInformation(new Shell(), "path", "fin shape");//TODO
				saveToR+="g<-shap$f"+"\r\n";
				c.eval("g<-shap$f");
				
				saveToR+="p  <-  shap$p"+"\r\n";
				c.eval("p  <-  shap$p");
				
				saveToR+="estshap<-shap$estimados"+"\r\n";
				c.eval("estshap<-shap$estimados");
				
				saveToR+="stderro  <-  shap$stderro"+"\r\n";
				c.eval("stderro  <-  shap$stderro");
				//MessageDialog.openInformation(new Shell(), "path", "stat develop");//TODO
//				c.eval("sink("+'"'+MainPageWizardPage.getStrAutoDisseminationPath() + "/Dev_Rate-Model"+(model)+".txt"+'"'+")");
				c.eval("sink("+'"'+MainPageWizardPage.getStrAutoDisseminationPath() + "/AutoDiss-Model" +model+".txt"+'"'+")");
				
//				c.eval("sink("+'"'+MainPageWizardPage.getStrAutoDisseminationPath() + "/Mort-Model" +modelm+".txt"+'"'+")");

				saveToR+="sol_develop<-stat_develop(model,datashap,datao,estshap,coefi,stderro)"+"\r\n";
				c.eval("sol_develop<-stat_develop(model,datashap,datao,estshap,coefi,stderro)");
				//MessageDialog.openInformation(new Shell(), "path", "fin stat develop");//TODO
				saveToR+="frsel<-sol_develop$frames"+"\r\n";
				c.eval("frsel<-sol_develop$frames");
				
				int lList= c.eval("frsel").asList().keys().length;
				for(int ii=0; ii<lList;ii++)
					framesSel[ii] = c.eval("frsel").asList().at(ii).asDouble()+"";
				
				saveToR+="fle<-sol_develop$ecuacion"+"\r\n";
				c.eval("fle<-sol_develop$ecuacion");
				
				saveToR+="qtt<-sol_develop$q"+"\r\n";
				c.eval("qtt<-sol_develop$q");
				
				saveToR+="sdli<-sol_develop$sdli"+"\r\n";
				c.eval("sdli<-sol_develop$sdli");
				
				saveToR+="tempar <-  sol_develop$parmer"+"\r\n";
				c.eval("tempar <-  sol_develop$parmer");
				
				saveToR+="tfunc <- sol_develop$ecuaci"+"\r\n";
				c.eval("tfunc <- sol_develop$ecuaci");
				
				saveToR+="table2  <-  sol_develop$param"+"\r\n";
				c.eval("table2  <-  sol_develop$param");
				
				c.eval(strExt+"(file=" + '"' +MainPageWizardPage.getStrAutoDisseminationPath() + "/"+"AutoDiss-Model"+ model+"."+strExt+'"'+")");
				//MessageDialog.openInformation(new Shell(), "path", "plot");//TODO
				saveToR+="plot_shape<-grafshape(model,estshap,datashap,sdli,qtt,corrx,corry,mini=0,maxi=200,coefi,limit,1,labx,laby,titulo, grises, scaleY, scaleX)"+"\r\n";
				c.eval("plot_shape<-grafshape(model,estshap,datashap,sdli,qtt,corrx,corry,mini=0,maxi=200,coefi,limit,1,labx,laby,titulo,grises, scaleY, scaleX)");
				//MessageDialog.openInformation(new Shell(), "path", "fin plot");//TODO
				c.eval("dev.off()");
				c.eval("sink()");
				
				uiShape1.lblFunctImageDR.setImage(new Image(Display.getCurrent(), MainPageWizardPage.getStrAutoDisseminationPath() + File.separator + "AutoDiss-Model"+ model+"."+strExt));
				uiShape1.lblFunctImageDR.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
			  	
			  	EPFAUtils.createHTMLfile(MainPageWizardPage.getStrAutoDisseminationPath(),"AutoDiss-Model"+model);
	//		  	uiShape1.bwrResultDR.setUrl("file:///" + MainPageWizardPage.getStrAutoDisseminationPath() + "/Dev_Rate-Model"+(model)+".html");
			  	uiShape1.bwrResultDR.setUrl(MainPageWizardPage.getStrAutoDisseminationPath()+"AutoDiss-Model"+model+".html");

				
				TableItem ti = new TableItem(SeveralModelsWizardPage.tableCriterias, SWT.None);
				ti.setText(new String[]{getModelNumber(MainPageWizardPage.lstSelectedModels.getItem(i))+"",MainPageWizardPage.lstSelectedModels.getItem(i),framesSel[0],framesSel[1],framesSel[2],framesSel[3],framesSel[4],framesSel[5]});
				
				c.close();
				arraysShell[i] = uiShape1.shell;
				uiShape1.shell.open();
				
			} catch (RserveException e) {
				Rserve.saveIlcymError(MainPageWizardPage.getStrAutoDisseminationPath(), title, saveToR);
				try {
					c.eval("sink()");
				} catch (RserveException e1) {
					c.close();
					e1.printStackTrace();
				}
				c.close();
				e.printStackTrace();
				MessageDialog.openError(new Shell(), title, "Problems of convergence of the initial parameter values of "+MainPageWizardPage.lstSelectedModels.getItem(i)+" model");
			} catch (REXPMismatchException e) {
				Rserve.saveIlcymError(MainPageWizardPage.getStrAutoDisseminationPath(), title, saveToR);
				try {
					c.eval("sink()");
				} catch (RserveException e1) {
					c.close();
					e1.printStackTrace();
				}
				c.close();
				e.printStackTrace();
				MessageDialog.openError(new Shell(), title, "Problems of convergence of the initial parameter values of "+MainPageWizardPage.lstSelectedModels.getItem(i)+" model");
			}
		}
	}
	
	/** Metodo para  procesar modelo uno a uno**/
	public static void selectionModelOneByOneNew(int modelo, boolean bolExtremMin/*, boolean bolExtremMax*/){
		//String path = CreatedProject.getStrPathProject() + File.separator + imageName;
		//String path =MainPageWizardPage.getStrAutoDisseminationPath() + "/"+MainPageWizardPage.comboDevStage.getText()+"-Model"+ model+"."+strExt;
//		String stageSel = MainPageWizardPage.getStageSel();
		String algo = MainPageWizardPage.getSelectAlgo();
		String pathAutoDissData = LoadPlotAutoDissData.getFileAutoDisseminationData();
		Boolean charge = false;
		
		try {
			c = new RConnection();
			OneModelWizardPage1.lblImageTemp.setImage(null);
			
//			loadParamsFromDTNew(EPFAUtils.getLibPath());
			if (charge == false)
			{
				saveToR+="datm<-read.table('"+pathAutoDissData+"',header = T)"+"\n";
				c.eval("datm<-read.table('"+pathAutoDissData+"',header = T)");
				
				saveToR+="datm[,2]= datm[,2]+0.00001"+"\n";
				c.eval("datm[,2]= datm[,2]+0.00001");
				
				saveToR+="datashap <- data.frame(x =datm[,1] ,y =datm[,2],Lower =datm[,2], Upper=datm[,2])"+"\n";
				c.eval("datashap <- data.frame(x =datm[,1] ,y =datm[,2],Lower =datm[,2], Upper=datm[,2])");
				
				saveToR+="datao <- data.frame(x =c(datm[,1],datm[,1],datm[,1]) ,y =c(datm[,2],datm[,2],datm[,2]))"+"\n";
				c.eval("datao <- data.frame(x =c(datm[,1],datm[,1],datm[,1]) ,y =c(datm[,2],datm[,2],datm[,2]))");
						
				ip.setCorrX2(c.eval("max(datashap[,1])+20").asString());
				saveToR+="corry=c("+ip.getCorrY1()+','+ip.getCorrY2()+')'+"\r\n";
				c.eval("corry=c("+ip.getCorrY1()+','+ip.getCorrY2()+')');
				
				EPFAUtils.createTempScriptFile("/RScripts/AutoDisseminationDesigner.r");
				
				saveToR += "source('"+ (ViewProjectsUI.getPathProject()+ File.separator).replace('\\','/')+"tempScripFile.r'"+")"+"\r\n";		          
		        c.eval("source('"+(ViewProjectsUI.getPathProject()+ File.separator).replace('\\','/')+"tempScripFile.r'"+")");
		        
		        System.out.println("charger again");
		        charge = true;
			}
			
			if(bolExtremMin){
				saveToR += "valxs <- c(" + OneModelWizardPage1.getExtremValuesOne()[0] + ')'+"\r\n";
				c.eval("valxs <- c(" + OneModelWizardPage1.getExtremValuesOne()[0] + ')');
				saveToR += "valxy <- c(" + OneModelWizardPage1.getExtremValuesOne()[1] + ')'+"\r\n";
				c.eval("valxy <- c(" + OneModelWizardPage1.getExtremValuesOne()[1] + ')');
				
				saveToR += "valx1=data.frame(x=valxs,y=valxy,Lower=valxy,Upper=valxy)" + "\r\n";
				c.eval("valx1=data.frame(x=valxs,y=valxy,Lower=valxy,Upper=valxy)");
				
				saveToR += "datashap<-datashap2<-rbind(valx1,datashap);colnames(datashap2)="+ '"'+'"'+';' + "\r\n";
				c.eval("datashap<-datashap2<-rbind(valx1,datashap);colnames(datashap2)="+ '"'+'"'+';');
				
				saveToR += "datao <- rbind(datashap2[,c(1,2)],datashap2[,c(1,3)],datashap2[,c(1,4)])"+"\r\n";
				c.eval("datao <- rbind(datashap2[,c(1,2)],datashap2[,c(1,3)],datashap2[,c(1,4)])");
				
				saveToR += "colnames(datao)=c("+'"'+"x"+'"'+","+'"'+"y"+'"'+")"+"\r\n";
				c.eval("colnames(datao)=c("+'"'+"x"+'"'+","+'"'+"y"+'"'+")");
				
			}
			
			strMinP = c.eval("min(as.numeric(rownames(datashap)))").asDouble()+"";
			strMaxP = c.eval("max(as.numeric(rownames(datashap)))").asDouble()+"";
			
			estimateParameters(c, modelo, true,  false,"", "", "", "", "", "", "");
			
			saveToR += "ini1=ini;niv=1"+ "\r\n";
			c.eval("ini1=ini;niv=1");
			
			c.eval(strExt+"(file=" + '"' +MainPageWizardPage.getStrAutoDisseminationPath() + "/"+"AutoDiss-Model"+ modelo+"."+strExt+'"'+")");
			
//			saveToR += "corry <- c(0,1.5*max(datashap[,2]))" + "\r\n";
//			c.eval("corry <- c(0,1.5*max(datashap[,2]))");
	//		c.eval("corry <- c(0,2*max(datashap[,2]))");   ip.setCorrY1("0");
			
			
//			ip.setCorrY2(c.eval("1.5*max(datashap[,2])").asString());
			
	//		c.eval(strExt+"(file=" + '"' +MainPageWizardPage.getStrAutoDisseminationPath() + "/Dev_Rate-Model"+ (modelo)+"."+strExt+'"'+")");
			saveToR+= "shapprueba<-prueba(model,datashap,datao,ini1,corrx,corry,punt=" +strMinP+":" + strMaxP + ",labx, laby, titulo,grises=F)"+"\r\n";
			c.eval("shapprueba<-prueba(model,datashap,datao,ini1,corrx,corry,punt=" +strMinP+":" + strMaxP + ",labx, laby, titulo,grises=F)");
			c.voidEval("dev.off()");
			
			saveToR+= "ini<-shapprueba$ini"+"\r\n";
			c.eval("ini<-shapprueba$ini");
			
			saveToR+= "coefi<-shapprueba$coefi"+"\r\n";
			c.eval("coefi<-shapprueba$coefi");
			
			saveToR+= "p<-shapprueba$p"+"\r\n";
			c.eval("p<-shapprueba$p");
			
			c.eval("Std.Error <- c(rep(0,length(ini)))");
			Double[] dbpars= saveTempar("ini", modelo);
			
			OneModelWizardPage1.txtAdjust.setText("1");
//			OneModelWizardPage1.lblImageTemp.setImage(new Image(Display.getCurrent(), MainPageWizardPage.getStrAutoDisseminationPath() + "/Dev_Rate-Model"+ (modelo)+"."+strExt));
			OneModelWizardPage1.lblImageTemp.setImage(new Image(Display.getCurrent(), MainPageWizardPage.getStrAutoDisseminationPath() + "/"+"AutoDiss-Model"+ modelo+"."+strExt));
																				
			
			OneModelWizardPage1.txtPar1.setText(df.format(dbpars[0]));
			OneModelWizardPage1.txtPar2.setText(df.format(dbpars[1]));
			OneModelWizardPage1.txtPar3.setText(df.format(dbpars[2]));
			OneModelWizardPage1.txtPar4.setText(df.format(dbpars[3]));
			OneModelWizardPage1.txtPar5.setText(df.format(dbpars[4]));
			OneModelWizardPage1.txtPar6.setText(df.format(dbpars[5]));
			OneModelWizardPage1.txtPar7.setText(df.format(dbpars[6]));
		  	
			c.close();
		
		}catch (RserveException e) {
			Rserve.saveIlcymError(MainPageWizardPage.getStrAutoDisseminationPath(), title, saveToR);
			c.close();
			e.printStackTrace();
			MessageDialog.openError(new Shell(), title, "Problems while trying to process one model");
		} catch (REXPMismatchException e) {
			Rserve.saveIlcymError(MainPageWizardPage.getStrAutoDisseminationPath(), title, saveToR);
			c.close();
			e.printStackTrace();
			MessageDialog.openError(new Shell(), title, "Problems while trying to process one model");
		}
	}

	/** Metodo para estimar los parametros modificando los valores d los parametros **/
	public static void spinnerListener(int model, String str1, String str2, String str3, String str4, String str5,
			String str6, String str7 , boolean bolExtremMin){
		
		try {
			c = new RConnection();
			
			if(bolExtremMin){
				saveToR += "valxs <- c(" + OneModelWizardPage1.getExtremValuesOne()[0] + ')'+"\r\n";
				c.eval("valxs <- c(" + OneModelWizardPage1.getExtremValuesOne()[0] + ')');
				saveToR += "valxy <- c(" + OneModelWizardPage1.getExtremValuesOne()[1] + ')'+"\r\n";
				c.eval("valxy <- c(" + OneModelWizardPage1.getExtremValuesOne()[1] + ')');
				
				saveToR += "valx1=data.frame(x=valxs,y=valxy,Lower=valxy,Upper=valxy)" + "\r\n";
				c.eval("valx1=data.frame(x=valxs,y=valxy,Lower=valxy,Upper=valxy)");
				
				saveToR += "datashap<-datashap2<-rbind(valx1,datashap);colnames(datashap2)="+ '"'+'"'+';' + "\r\n";
				c.eval("datashap<-datashap2<-rbind(valx1,datashap);colnames(datashap2)="+ '"'+'"'+';');
				
				saveToR += "datao <- rbind(datashap2[,c(1,2)],datashap2[,c(1,3)],datashap2[,c(1,4)])"+"\r\n";
				c.eval("datao <- rbind(datashap2[,c(1,2)],datashap2[,c(1,3)],datashap2[,c(1,4)])");
				
				saveToR += "colnames(datao)=c("+'"'+"x"+'"'+","+'"'+"y"+'"'+")"+"\r\n";
				c.eval("colnames(datao)=c("+'"'+"x"+'"'+","+'"'+"y"+'"'+")");
				
			}else{
				loadParamsFromDTNew(EPFAUtils.getLibPath());
			}
			
			strMinP = c.eval("min(as.numeric(rownames(datashap)))").asDouble()+"";
			strMaxP = c.eval("max(as.numeric(rownames(datashap)))").asDouble()+"";
			
			c.eval(strExt+"(file=" + '"' +MainPageWizardPage.getStrAutoDisseminationPath() + "/Dev_Rate-Model"+ (model)+"."+strExt+'"'+")");
			estimateParameters(c,model, false, false, str1, str2, str3, str4, str5, str6, str7);
			
			saveToR+="shapprueba<-prueba(model,datashap,datao,ini,corrx,corry,punt="+strMinP+":" + strMaxP + ",labx, laby, titulo,grises=F)"+"\r\n";
			c.eval("shapprueba<-prueba(model,datashap,datao,ini,corrx,corry,punt="+strMinP+":" + strMaxP + ",labx, laby, titulo,grises=F)");
			
			c.eval("dev.off()");
			
			saveToR+="ini<-shapprueba$ini"+"\r\n";
			c.eval("ini<-shapprueba$ini");
			
			saveToR+="coefi<-shapprueba$coefi"+"\r\n";
			c.eval("coefi<-shapprueba$coefi");
			
			saveToR+="p<-shapprueba$p"+"\r\n";
			c.eval("p<-shapprueba$p");
			
//			OneModelWizardPage1.lblImageTemp.setImage(new Image(Display.getCurrent(), MainPageWizardPage.getStrAutoDisseminationPath() + "/Dev_Rate-Model"+ (model)+"."+strExt));
			OneModelWizardPage1.lblImageTemp.setImage(new Image(Display.getCurrent(), MainPageWizardPage.getStrAutoDisseminationPath() + "/"+"AutoDiss-Model"+ model+"."+strExt));
			c.close();
		} catch (RserveException e) {
			Rserve.saveIlcymError(MainPageWizardPage.getStrAutoDisseminationPath(), title, saveToR);
			e.printStackTrace();
			MessageDialog.openError(new Shell(), title, "Problems while trying to estimate the parameters separately");
			c.close();
		} catch (REXPMismatchException e) {
			Rserve.saveIlcymError(MainPageWizardPage.getStrAutoDisseminationPath(), title, saveToR);
			e.printStackTrace();
			MessageDialog.openError(new Shell(), title, "Problems while trying to estimate the parameters separately");
			c.close();
		} 
	}
	
	/** Metodo que guarda el modelo seleccionado (cmpOneModelPage) **/
	public static void setModelSelectedOne(int modelo, String str1, String str2, String str3, String str4, String str5, String str6, String str7){
		Double[] dbpars = null;
		String algo = MainPageWizardPage.getSelectAlgo();
		try {
			c = new RConnection();
			
			estimateParameters(c, modelo, false,  false,str1, str2, str3, str4, str5, str6, str7);
//			saveToR += "corry <- c(0,1.5*max(datashap[,2]))" + "\r\n";
//			c.eval("corry <- c(0,1.5*max(datashap[,2]))");
	//		c.eval("corry <- c(0,2*max(datashap[,2]))");   ip.setCorrY1("0");
						
//			ip.setCorrY2(c.eval("1.5*max(datashap[,2])").asString());
			
			saveToR+= "shapprueba<-prueba(model,datashap,datao,ini,corrx,corry,punt="+strMinP+":" + strMaxP + ",labx, laby, titulo)"+"\r\n";
			c.eval("shapprueba<-prueba(model,datashap,datao,ini,corrx,corry,punt="+strMinP+":" + strMaxP + ",labx, laby, titulo)");
			
			c.eval("limit <- " + '"'+OneModelWizardPage1.getLimitsOne()+'"');
			
			c.voidEval("dev.off()");
			
			saveToR+= "ini<-shapprueba$ini"+"\r\n";
			c.eval("ini<-shapprueba$ini");
			
			saveToR+= "coefi<-shapprueba$coefi"+"\r\n";
			c.eval("coefi<-shapprueba$coefi");
			
			saveToR+= "p<-shapprueba$p"+"\r\n";
			c.eval("p<-shapprueba$p");
			
			saveToR+="shap<-shape(model,datashap,datao,ini,coefi)"+"\r\n";
			c.eval("shap<-shape(model,datashap,datao,ini,coefi)");
			
			saveToR+="g<-shap$f"+"\r\n";
			c.eval("g<-shap$f");
			
			saveToR+="p  <-  shap$p"+"\r\n";
			c.eval("p  <-  shap$p");
			
			saveToR+="estshap<-shap$estimados"+"\r\n";
			c.eval("estshap<-shap$estimados");
			
			saveToR+="stderro  <-  shap$stderro"+"\r\n";
			c.eval("stderro  <-  shap$stderro");
			
			c.eval("sink("+'"'+ MainPageWizardPage.getStrAutoDisseminationPath() + "/" + "EPFA_"+title + ".txt"+'"'+")");
			//c.eval("sink("+'"'+MainPageWizardPage.getStrAutoDisseminationPath() + "/"+MainPageWizardPage.comboDevStage.getText()+"-Model" +modelo+".txt"+'"'+")");
//			c.eval("sink("+'"'+ MainPageWizardPage.getStrAutoDisseminationPath() + "/" +"MaizeDevRate-"+ MainPageWizardPage.comboDevStage.getText() + ".txt"+'"'+")");
			
			saveToR += "sol_develop<-stat_develop(model,datashap,datao,estshap,coefi,stderro)"+"\r\n";
			c.eval("sol_develop<-stat_develop(model,datashap,datao,estshap,coefi,stderro)");
			
			saveToR += "qtt<-sol_develop$q"+"\r\n";
			c.eval("qtt<-sol_develop$q");
			
			saveToR += "sdli<-sol_develop$sdli"+"\r\n";
			c.eval("sdli<-sol_develop$sdli");
			
			saveToR+="fle<-sol_develop$ecuacion"+"\r\n";
			c.eval("fle<-sol_develop$ecuacion");
			
			saveToR += "tempar <-  sol_develop$parmer"+"\r\n";
			c.eval("tempar <-  sol_develop$parmer");
			
			saveToR += "tfunc <- sol_develop$ecuaci"+"\r\n";
			c.eval("tfunc <- sol_develop$ecuaci");
			
			saveToR += "table2  <-  sol_develop$param"+"\r\n";
			c.eval("table2  <-  sol_develop$param");
			
			saveToR+="Std.Error<-sol_develop$Std.Error"+"\r\n";
			c.eval("Std.Error<-sol_develop$Std.Error");
			
			saveToR+="alg<-'"+algo+"'"+"\n";
			c.eval("alg<-'"+algo+"'");
			
			c.eval("sink()");
		
			//c.eval(strSentSaveImage);
			//strSentSaveImage = "png(file=" + '"' +MainPageWizardPage.getStrAutoDisseminationPath() + "/" +MainPageWizardPage.comboDevStage.getText()+ imageName+'"'+")";
			c.eval(strExt+"(file=" + '"' +MainPageWizardPage.getStrAutoDisseminationPath() + "/"+"EPFA_"+title+"."+strExt+'"'+")");
			
			saveToR += "plot_shape<-grafshape(model,estshap,datashap,sdli,qtt,corrx,corry,mini,maxi,coefi,limit,1,labx,laby,titulo,grises=FALSE, scaleY, scaleX)"+"\r\n";
			c.eval("plot_shape<-grafshape(model,estshap,datashap,sdli,qtt,corrx,corry,mini,maxi,coefi,limit,1,labx,laby,titulo,grises=FALSE, scaleY, scaleX)");
		
			c.eval("dev.off()");
			
			dbpars= saveTempar("tempar", modelo);
			pars.setParameters(dbpars);
			pars.setStrModel(getModelName(modelo+""));
			pars.setStdParameters(dbStdpars);
			
			
			EPFAUtils.createHTMLfile(MainPageWizardPage.getStrAutoDisseminationPath(),"EPFA_"+title );
//		  	EPFAUtils.createHTMLfile(MainPageWizardPage.getStrAutoDisseminationPath(), MainPageWizardPage.comboDevStage.getText()+"-Model"+title);
//			EPFAUtils.createHTMLfile(MainPageWizardPage.getStrAutoDisseminationPath(), title);
//			File temp = new File(MainPageWizardPage.getStrAutoDisseminationPath() + File.separator + title + ".txt");
//		  	File temp = new File(MainPageWizardPage.getStrAutoDisseminationPath() + "/"+MainPageWizardPage.comboDevStage.getText()+"-Model" +modelo+ ".txt");
//			temp.delete();
			
/*			uiShape1.lblFunctImageDR.setImage(new Image(Display.getCurrent(), MainPageWizardPage.getStrAutoDisseminationPath() + File.separator + MainPageWizardPage.comboDevStage.getText()+"-Model"+ model+"."+strExt));
			uiShape1.lblFunctImageDR.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		  	
		  	EPFAUtils.createHTMLfile(MainPageWizardPage.getStrAutoDisseminationPath(), MainPageWizardPage.comboDevStage.getText()+"-Model"+model);
//		  	uiShape1.bwrResultDR.setUrl("file:///" + MainPageWizardPage.getStrAutoDisseminationPath() + "/Dev_Rate-Model"+(model)+".html");
		  	uiShape1.bwrResultDR.setUrl(MainPageWizardPage.getStrAutoDisseminationPath() + MainPageWizardPage.comboDevStage.getText()+"-Model"+model+".html");

*/			
			
//			OneModelWizardPage1.lblImageTemp.setImage(new Image(Display.getCurrent(), MainPageWizardPage.getStrAutoDisseminationPath() + "/"+MainPageWizardPage.comboDevStage.getText()+"-Model"+ model+"."+strExt));
			OneModelWizardPage2.lblImageFinal.setImage(new Image(Display.getCurrent(),MainPageWizardPage.getStrAutoDisseminationPath()+ File.separator +"EPFA_"+imageName));
//			OneModelWizardPage2.brwModelSel.setUrl("file:///" + MainPageWizardPage.getStrAutoDisseminationPath() + File.separator + title +".html");
			OneModelWizardPage2.brwModelSel.setUrl(MainPageWizardPage.getStrAutoDisseminationPath() + "EPFA_"+title+".html");
			OneModelWizardPage2.lblModelSelOne.setText(pars.getStrModel()); 
			
			//here we have to save R workSave in for the use for the mapping
			saveToR+="save(list = ls(all.names = TRUE), file = "+ '"' + MainPageWizardPage.getStrAutoDisseminationPath() + "/" + "EPFA_"+title + ".RData"+'"'+", envir = .GlobalEnv)"+"\n";
			c.eval("save(list = ls(all.names = TRUE), file = "+ '"' + MainPageWizardPage.getStrAutoDisseminationPath() + "/" + "EPFA_"+title + ".RData"+'"'+", envir = .GlobalEnv)");
			
			c.close();
		} catch (RserveException e) {
			Rserve.saveIlcymError(MainPageWizardPage.getStrAutoDisseminationPath(), title, saveToR);
			try {
				c.eval("sink()");
			} catch (RserveException e1) {
				c.close();
				e1.printStackTrace();
			}
			c.close();
			e.printStackTrace();
			MessageDialog.openError(new Shell(), title, "Problems while trying to set one model selected");
		} catch (REXPMismatchException e) {
			Rserve.saveIlcymError(MainPageWizardPage.getStrAutoDisseminationPath(), title, saveToR);
			try {
				c.eval("sink()");
			} catch (RserveException e1) {
				c.close();
				e1.printStackTrace();
			}
			
			c.close();
			e.printStackTrace();
		}
	}
	
	/** Metodo para reiniciar los valores  de las caracteristicas de la imagen resultante**/
	public static void restoreImage(String strStageSel, String pathImage, Label lblImage, int model){
		
		try {
			c=new RConnection();
			c.eval(strExt+"(file=" + '"' +pathImage.replace("\\", "/") +'"'+")");
			
			if(MainPageWizardPage.getSeveralModels())
				estimateParameters(c,model, true, false, "", "", "", "", "", "", "");
			else{
				String str1,str2,str3,str4,str5,str6,str7;
				str1 = pars.getParameters()[0];
				str2 = pars.getParameters()[1];
				str3 = pars.getParameters()[2];
				str4 = pars.getParameters()[3];
				str5 = pars.getParameters()[4];
				str6 = pars.getParameters()[5];
				str7 = pars.getParameters()[6];
				estimateParameters(c,model, false, true, str1, str2, str3, str4, str5, str6, str7);
			}
			
			c.eval("shapprueba<-prueba(model,datashap,datao,ini,corrx,corry,punt="+strMinP+":"+strMaxP+",labx, laby, titulo, grises)");
			c.eval("ini<-shapprueba$ini");
			c.eval("coefi<-shapprueba$coefi");
			c.eval("p<-shapprueba$p");
			
			c.eval("shap<-shape(model,datashap,datao,ini,coefi)");
			c.eval("estshap<-shap$estimados");
			c.eval("g<-shap$f");
			c.eval("p  <-  shap$p");
			c.eval("stderro  <-  shap$stderro");
			c.eval("sol_develop<-stat_develop(model,datashap,datao,estshap,coefi,stderro)");
			c.eval("qtt<-sol_develop$q");
			c.eval("sdli<-sol_develop$sdli");
			c.eval("tempar <-  sol_develop$parmer");
			c.eval("tfunc <- sol_develop$ecuaci");
			c.eval("table2  <-  sol_develop$param");
		    c.eval("plot_shape<-grafshape(model,estshap,datashap,sdli,qtt,corrx=c(0,max(datashap[,1])+20),corry=c(0,1),mini=0,maxi=200,coefi,limit,1,labx="+'"'+"Distance (Meter)"+'"'+",laby="+'"'+"Insects Proportion (%)"+'"'+",titulo="+'"'+""+'"'+",grises=FALSE, scaleY=0.1, scaleX=5)");
			
			c.eval("dev.off()");
			lblImage.setImage(new Image(Display.getCurrent(), pathImage));
			c.close();
		} catch (RserveException e) {
			Rserve.saveIlcymError(MainPageWizardPage.getStrAutoDisseminationPath(), title, saveToR);
			c.close();
			e.printStackTrace();
			MessageDialog.openError(new Shell(), title, "Problems while trying to restore the graph");
		}
		
	}
	
	/** Metodo que guarda el modelo seleccionado (cmpAllModelsPage) **/
	public static void setModelSelected(int modelo){
		Double[] dbpars = null;
		DecimalFormat df = new DecimalFormat("##.###");
		String algo = MainPageWizardPage.getSelectAlgo();
		try {
			c = new RConnection();
			
			estimateParameters(c, modelo, true,  false,"", "", "", "", "", "", "");
			
			saveToR+= "shapprueba<-prueba(model,datashap,datao,ini,corrx,corry,punt="+strMinP+":" + strMaxP + ",labx, laby, titulo)"+"\r\n";
			c.eval("shapprueba<-prueba(model,datashap,datao,ini,corrx,corry,punt="+strMinP+":" + strMaxP + ",labx, laby, titulo)");
			
			c.voidEval("dev.off()");
			
			saveToR+= "ini<-shapprueba$ini"+"\r\n";
			c.eval("ini<-shapprueba$ini");
			
			saveToR+= "coefi<-shapprueba$coefi"+"\r\n";
			c.eval("coefi<-shapprueba$coefi");
			
			saveToR+= "p<-shapprueba$p"+"\r\n";
			c.eval("p<-shapprueba$p");
			
			saveToR+="shap<-shape(model,datashap,datao,ini,coefi)"+"\r\n";
			c.eval("shap<-shape(model,datashap,datao,ini,coefi)");
			
			saveToR+="g<-shap$f"+"\r\n";
			c.eval("g<-shap$f");
			
			saveToR+="p  <-  shap$p"+"\r\n";
			c.eval("p  <-  shap$p");
			
			saveToR+="estshap<-shap$estimados"+"\r\n";
			c.eval("estshap<-shap$estimados");
			
			saveToR+="stderro  <-  shap$stderro"+"\r\n";
			c.eval("stderro  <-  shap$stderro");
			
			
//			c.eval("sink("+'"'+ MainPageWizardPage.getStrAutoDisseminationPath() + "/" +"MaizeDevRate-"+ MainPageWizardPage.comboDevStage.getText() + ".txt"+'"'+")");
			c.eval("sink("+'"'+MainPageWizardPage.getStrAutoDisseminationPath() + "/" +"EPFA_"+title + ".txt"+'"'+")");
			
			saveToR += "sol_develop<-stat_develop(model,datashap,datao,estshap,coefi,stderro)"+"\r\n";
			c.eval("sol_develop<-stat_develop(model,datashap,datao,estshap,coefi,stderro)");
			
			saveToR += "qtt<-sol_develop$q"+"\r\n";
			c.eval("qtt<-sol_develop$q");
			
			saveToR += "sdli<-sol_develop$sdli"+"\r\n";
			c.eval("sdli<-sol_develop$sdli");
			
			saveToR += "tempar <-  sol_develop$parmer"+"\r\n";
			c.eval("tempar <-  sol_develop$parmer");
			
			saveToR += "tfunc <- sol_develop$ecuaci"+"\r\n";
			c.eval("tfunc <- sol_develop$ecuaci");
			
			saveToR += "table2  <-  sol_develop$param"+"\r\n";
			c.eval("table2  <-  sol_develop$param");
			
			saveToR+="Std.Error<-sol_develop$Std.Error"+"\r\n";
			c.eval("Std.Error<-sol_develop$Std.Error");
			
			saveToR+="alg<-'"+algo+"'"+"\n";
			c.eval("alg<-'"+algo+"'");
			
			c.eval("sink()");
		
			//c.eval(strSentSaveImage);
			c.eval(strExt+"(file=" + '"' +MainPageWizardPage.getStrAutoDisseminationPath() + "/" + "EPFA_"+imageName+'"'+")");
			
			saveToR += "plot_shape<-grafshape(model,estshap,datashap,sdli,qtt,corrx,corry,mini,maxi,coefi,limit,1,labx,laby,titulo,grises, scaleY, scaleX)"+"\r\n";
			c.eval("plot_shape<-grafshape(model,estshap,datashap,sdli,qtt,corrx,corry,mini,maxi,coefi,limit,1,labx,laby,titulo,grises, scaleY, scaleX)");
		
			c.eval("dev.off()");
			
			dbpars= saveTempar("tempar", modelo);
			pars.setParameters(dbpars);
			pars.setStdParameters(dbStdpars);
			
			EPFAUtils.createHTMLfile(MainPageWizardPage.getStrAutoDisseminationPath(), "EPFA_"+title);
//			File temp = new File(MainPageWizardPage.getStrAutoDisseminationPath()+ File.separator + title + ".txt");
//			temp.delete();
			
			//here we have to save R workSave in for the use for the mapping
			saveToR+="save(list = ls(all.names = TRUE), file = "+ '"' + MainPageWizardPage.getStrAutoDisseminationPath() + "/" +"EPFA_"+ title+ ".RData"+'"'+", envir = .GlobalEnv)"+"\n";
			c.eval("save(list = ls(all.names = TRUE), file = "+ '"' + MainPageWizardPage.getStrAutoDisseminationPath() + "/" + "EPFA_"+title+ ".RData"+'"'+", envir = .GlobalEnv)");
			
			c.close();
		} catch (RserveException e) {
			Rserve.saveIlcymError(MainPageWizardPage.getStrAutoDisseminationPath(), title, saveToR);
			try {
				c.eval("sink()");
			} catch (RserveException e1) {
				c.close();
				e1.printStackTrace();
			}
			c.close();
			e.printStackTrace();
			MessageDialog.openError(new Shell(), title, "Problems while trying to set the model selected");
		} catch (REXPMismatchException e) {
			Rserve.saveIlcymError(MainPageWizardPage.getStrAutoDisseminationPath(), title, saveToR);
			try {
				c.eval("sink()");
			} catch (RserveException e1) {
				c.close();
				e1.printStackTrace();
			}
			c.close();
			e.printStackTrace();
			MessageDialog.openError(new Shell(), title, "Problems while trying to set the model selected");
		}
		
		SeveralModelsWizardPage.lblModelSelAll.setText(getModelName(modelo+""));
		pars.setStrModel(getModelName(modelo+""));
		String str="";
		
		for(int i=0; i<dbpars.length; i++){
			if(!pars.getParametersName()[i].equalsIgnoreCase("")){
				if(i == dbpars.length-1)
					str+= pars.getParametersName()[i] + " = " + df.format(dbpars[i]);
				else
					str+= pars.getParametersName()[i] + " = " + df.format(dbpars[i]) + ", ";
			}else{
				str = str.substring(0, str.length()-2);
				break;
			}
		}
		SeveralModelsWizardPage.txtParametersEstimatedAll.setText(str);
		
		for(int i=0;i<arraysShell.length;i++){
			if(arraysShell[i]!= null)
				if(!arraysShell[i].isDisposed())
					arraysShell[i].close();
		}
	}
	
	
	/** Guarda en el archivo resume el modelo seleccionado **/
	public static boolean saveModelSelected(){
		if(MainPageWizardPage.getSeveralModels()){
			if(SeveralModelsWizardPage.lblModelSelAll.getText().equalsIgnoreCase("") 
					&& SeveralModelsWizardPage.txtParametersEstimatedAll.getText().equalsIgnoreCase("")){
					MessageDialog.openError(SeveralModelsWizardPage.container.getShell(),title, 
					"You must to select correctly the model");
					return false;
			}
		}
		deleteAllTempFiles();
		guardarResume();
	//	newSaveProgress();
		return true;
	}
	
	/** elimina los archivos temporales que se generan a la hora de procesar los modelos **/
	private static void deleteAllTempFiles(){
		String[] arrayFiles = new File(MainPageWizardPage.getStrAutoDisseminationPath()).list();
		
		for(int i=0; i<arrayFiles.length; i++){
			if(new File(MainPageWizardPage.getStrAutoDisseminationPath().replace('/', '\\')+'\\'+arrayFiles[i]).isFile())
				if(!arrayFiles[i].startsWith("EPFA_"))
					new File(MainPageWizardPage.getStrAutoDisseminationPath().replace('/', '\\')+'\\'+arrayFiles[i]).delete();
		}
	}
	
	
	/** guarda el archivo resume con el modelo y parametros seleccionados **/
	private static void guardarResume(){
		File fResume = new File(MainPageWizardPage.getStrAutoDisseminationPath().replace('/', '\\')+"resume.EPFA");  //file.createNewFile()
		try {
			fResume.createNewFile();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
				//new File("\""+(MainActionMaizeDevRate.pathProj + File.separator +"resume.ilcym").replace("\\", "/")+"\"");//modified for testing
		System.out.println(fResume);
		//File fResume = new File(MainActionMaizeDevRate.pathProj + File.separator+title + File.separator +"resume.epfa");4
		
		
		
		//String numStages=ViewProjectsUI.getNumStage(MainPageWizardPage.getStageSel());
		String p="", std="";
		
		try{
			c = new RConnection();
			for(int i=0;i<pars.getParametersName().length;i++){
				if(!pars.getParametersName()[i].equalsIgnoreCase("")){
					p += pars.getParametersName()[i] + "=" + pars.getParameters()[i] + " ";
					std += pars.getParametersName()[i] + "=" + pars.getStdParameters()[i] + " ";
				}
				
			}
			
			Properties prop = new Properties();
			try {
				prop.load(new FileInputStream(fResume));
//				prop.setProperty("Stage"+numStages, MainPageWizardPage.getStageSel());
				
				prop.setProperty("Algorithm", MainPageWizardPage.getSelectAlgo());
				prop.setProperty("Model", pars.getStrModel());
				prop.setProperty("Parameters", p.trim());
				prop.setProperty("Std.Error", std.trim());
//				prop.setProperty("Formula", functToString(c.eval("toString(tt)")));
				prop.setProperty("Formula", c.eval("toString(fle)").asString());
				prop.store(new FileOutputStream(fResume), "EPFA AutoDissemination Modelling Summary");
				prop.clear();
				
				c.close();
			} catch (FileNotFoundException e1) {
				MessageDialog.openError(new Shell(), title, "Problems while trying to save the resume sheet");
				c.close();
				e1.printStackTrace();
			} catch (IOException e1) {
				MessageDialog.openError(new Shell(), title, "Problems while trying to save the resume sheet");
				c.close();
				e1.printStackTrace();
			} catch (RserveException e) {
				Rserve.saveIlcymError(MainPageWizardPage.getStrAutoDisseminationPath(), title, saveToR);
				c.close();
				e.printStackTrace();
			} catch (REXPMismatchException e) {
				Rserve.saveIlcymError(MainPageWizardPage.getStrAutoDisseminationPath(), title, saveToR);
				c.close();
				e.printStackTrace();
			}
			
		}catch (Exception e) {
			c.close();
			e.printStackTrace();
			MessageDialog.openError(new Shell(), title, "Problems while saving the parameters in the resume sheet");
			return;
		}
		
	
	}
	
/*	
	/** guarda el archivo resume con el modelo y parametros seleccionados **
	private static void guardarResume(){
		File fResume = new File(MainActionAutoDiss.pathProj + File.separator + title + File.separator +"resume.ilcym");//verificar ruta
		String numStages=ViewProjectsUI.getNumStage(MainPageWizardPage.getStageSel());
		
		String tempar="", std="";
		
		try{
			c = new RConnection();
			c.eval("tt<-toString(tfunc)");
			
			for(int i=0;i<pars.getParametersName().length;i++){
				if(!pars.getParametersName()[i].equalsIgnoreCase("")){
					tempar += pars.getParametersName()[i] + "=" + pars.getParameters()[i] + " ";
					std += pars.getParametersName()[i] + "=" + pars.getStdParameters()[i] + " ";
				}
				
			}
			
			Properties prop = new Properties();
			try {
				prop.load(new FileInputStream(fResume));
				prop.setProperty("Stage"+numStages, MainPageWizardPage.getStageSel());
				prop.setProperty("Model"+numStages, pars.getStrModel());
				prop.setProperty("Parameters"+numStages, tempar.trim());
				prop.setProperty("Std.Error"+numStages, std.trim());
				prop.setProperty("Formula"+numStages, functToString(c.eval("tt")));
				prop.store(new FileOutputStream(fResume), "Development Rate Resume");
				
				prop.clear();
				fResume=null;
				c.close();
			} catch (FileNotFoundException e1) {
				c.close();
				MessageDialog.openError(new Shell(), title, e1.getMessage());
				e1.printStackTrace();
			} catch (IOException e1) {
				c.close();
				MessageDialog.openError(new Shell(), title, e1.getMessage());
				e1.printStackTrace();
			} catch (RserveException e) {
				Rserve.saveIlcymError(MainPageWizardPage.getStrAutoDisseminationPath(), title, saveToR);
				MessageDialog.openError(new Shell(), title, "Problems while saving the parameters in the resume sheet");
				c.close();
				e.printStackTrace();
			} catch (REXPMismatchException e) {
				Rserve.saveIlcymError(MainPageWizardPage.getStrAutoDisseminationPath(), title, saveToR);
				MessageDialog.openError(new Shell(), title, "Problems while saving the parameters in the resume sheet");
				c.close();
				e.printStackTrace();
			}
			
		}catch (RserveException e) {
			Rserve.saveIlcymError(MainPageWizardPage.getStrAutoDisseminationPath(), title, saveToR);
			e.printStackTrace();
			c.close();
			MessageDialog.openError(new Shell(), title, "Problems while saving the parameters in the resume sheet");
			return;
        }catch (Exception e) {
			Rserve.saveIlcymError(MainPageWizardPage.getStrAutoDisseminationPath(), title, saveToR);
        	c.close();
			e.printStackTrace();
			MessageDialog.openError(new Shell(), title, e.getMessage());
		}
	}

*/	
	
	/** guarda el proceso completado en el archivo progress **/
	private static void newSaveProgress(){
		File fileProg = new File(MainActionAutoDiss.pathProj + File.separator + "Progress.ilcym");
		try {
			
			BufferedReader in = new BufferedReader(new FileReader(fileProg));
	        String str,newfile="";
	        
	        
	        while ((str = in.readLine()) != null) {
	        	String[]lineT = ArrayConvertions.StringtoArray(str, "\t");
	        	if(lineT[0].trim().equalsIgnoreCase(MainPageWizardPage.getStageSel())){
	        		lineT[2] = "true";
	        	}
	       /* 	       	if(ViewProjectsUI.getRate().contains("Age") || ViewProjectsUI.getRate().contains("Distance"))
	        		newfile+=lineT[0]+"\t"+lineT[1]+"\t"+lineT[2]+"\t"+lineT[3]+"\t"+lineT[4]+"\t"+lineT[5]+"\t"+lineT[6]+"\t"+lineT[7]+"\r\n";
	        	else*/
	        		newfile+=lineT[0]+"\t"+lineT[1]+"\t"+lineT[2]+"\t"+lineT[3]+"\t"+lineT[4]+"\t"+lineT[5]+"\t"+lineT[6]+"\r\n";
	        }
	        
	        BufferedWriter bw = new BufferedWriter(new FileWriter(fileProg));
	        bw.write(newfile);
			bw.close();
		} catch (Exception e) {
			MessageDialog.openError(new Shell(), title, "Problems while trying to save the progress project");
			e.printStackTrace();
		}
	}
	
	/** Metodo que modifica los paramteros aleatoriamente **/
	public static void randomParameters(){
		try {
			c = new RConnection();
			
			rexpAnt = rexpNew;
			
			c.eval("niv=" + OneModelWizardPage1.txtAdjust.getText());
//			c.eval(strSentSaveImage);
			c.eval(strExt+"(file=" + '"' +MainPageWizardPage.getStrAutoDisseminationPath() + "/"+ imageName+'"'+")");
			
			c.eval("iniback <- ini1");
			c.eval("ini1=recalc(ini,niv=niv"+")");
			
			c.eval("shapprueba<-prueba(model,datashap,datao,ini1,corrx,corry,punt=" +strMinP+":" + strMaxP + ",labx, laby, titulo,grises=F)");
			c.voidEval("dev.off()");
			
//			OneModelWizardPage1.lblImageTemp.setImage(new Image(Display.getCurrent(), MainPageWizardPage.getStrAutoDisseminationPath() + "/"+ imageName));
			OneModelWizardPage1.lblImageTemp.setImage(new Image(Display.getCurrent(), MainPageWizardPage.getStrAutoDisseminationPath() + "/"+ imageName));
			
			rexpNew = c.eval("ini1");
			
			try {
				OneModelWizardPage1.txtPar1.setText(df.format(c.eval("ini1["+'"'+OneModelWizardPage1.lblPar1.getText()+'"'+"]").asList().at(0).asDouble()));
				OneModelWizardPage1.txtPar2.setText(df.format(c.eval("ini1["+'"'+OneModelWizardPage1.lblPar2.getText()+'"'+"]").asList().at(0).asDouble()));
				if(!OneModelWizardPage1.lblPar3.getText().equalsIgnoreCase("") && !OneModelWizardPage1.lblPar3.getText().equalsIgnoreCase("          "))
					OneModelWizardPage1.txtPar3.setText(df.format(c.eval("ini1["+'"'+OneModelWizardPage1.lblPar3.getText()+'"'+"]").asList().at(0).asDouble()));
				if(!OneModelWizardPage1.lblPar4.getText().equalsIgnoreCase("") && !OneModelWizardPage1.lblPar4.getText().equalsIgnoreCase("          "))
					OneModelWizardPage1.txtPar4.setText(df.format(c.eval("ini1["+'"'+OneModelWizardPage1.lblPar4.getText()+'"'+"]").asList().at(0).asDouble()));
				if(!OneModelWizardPage1.lblPar5.getText().equalsIgnoreCase("") && !OneModelWizardPage1.lblPar5.getText().equalsIgnoreCase("          "))
					OneModelWizardPage1.txtPar5.setText(df.format(c.eval("ini1["+'"'+OneModelWizardPage1.lblPar5.getText()+'"'+"]").asList().at(0).asDouble()));
				if(!OneModelWizardPage1.lblPar6.getText().equalsIgnoreCase("") && !OneModelWizardPage1.lblPar6.getText().equalsIgnoreCase("          "))
					OneModelWizardPage1.txtPar6.setText(df.format(c.eval("ini1["+'"'+OneModelWizardPage1.lblPar6.getText()+'"'+"]").asList().at(0).asDouble()));
				if(!OneModelWizardPage1.lblPar7.getText().equalsIgnoreCase("") && !OneModelWizardPage1.lblPar7.getText().equalsIgnoreCase("          "))
					OneModelWizardPage1.txtPar7.setText(df.format(c.eval("ini1["+'"'+OneModelWizardPage1.lblPar7.getText()+'"'+"]").asList().at(0).asDouble()));
				
			} catch (REXPMismatchException e) {
				Rserve.saveIlcymError(MainPageWizardPage.getStrAutoDisseminationPath(), title, saveToR);
				c.close();
				e.printStackTrace();
				MessageDialog.openError(new Shell(), title, "Problems while trying to random the parameters");
			}catch (Exception e) {
				Rserve.saveIlcymError(MainPageWizardPage.getStrAutoDisseminationPath(), title, saveToR);
				MessageDialog.openError(new Shell(), title, "Problems while trying to random the parameters");
				c.close();
				e.printStackTrace();
			}
			
			bolBackPars = false;
			c.close();
		} catch (RserveException e) {
			Rserve.saveIlcymError(MainPageWizardPage.getStrAutoDisseminationPath(), title, saveToR);
			c.close();
			e.printStackTrace();
			MessageDialog.openError(new Shell(), title, "Problems while trying to random the parameters");
		}
	}
	/** Metodo que devuelve los parametros anteriores **/
	public static void backParameters(){
		try {
			c = new RConnection();
			
			if(rexpNew == null){
				c.close();
				return;
			}
			
			try {
				OneModelWizardPage1.txtPar1.setText(df.format(rexpAnt.asList().at(OneModelWizardPage1.lblPar1.getText()).asDouble()));
				OneModelWizardPage1.txtPar2.setText(df.format(rexpAnt.asList().at(OneModelWizardPage1.lblPar2.getText()).asDouble()));
				
				if(!OneModelWizardPage1.lblPar3.getText().equalsIgnoreCase("") && !OneModelWizardPage1.lblPar3.getText().equalsIgnoreCase("          "))
					OneModelWizardPage1.txtPar3.setText(df.format(rexpAnt.asList().at(OneModelWizardPage1.lblPar3.getText()).asDouble()));
				if(!OneModelWizardPage1.lblPar4.getText().equalsIgnoreCase("") && !OneModelWizardPage1.lblPar4.getText().equalsIgnoreCase("          "))
					OneModelWizardPage1.txtPar4.setText(df.format(rexpAnt.asList().at(OneModelWizardPage1.lblPar4.getText()).asDouble()));
				if(!OneModelWizardPage1.lblPar5.getText().equalsIgnoreCase("") && !OneModelWizardPage1.lblPar5.getText().equalsIgnoreCase("          "))
					OneModelWizardPage1.txtPar5.setText(df.format(rexpAnt.asList().at(OneModelWizardPage1.lblPar5.getText()).asDouble()));
				if(!OneModelWizardPage1.lblPar6.getText().equalsIgnoreCase("") && !OneModelWizardPage1.lblPar6.getText().equalsIgnoreCase("          "))
					OneModelWizardPage1.txtPar6.setText(df.format(rexpAnt.asList().at(OneModelWizardPage1.lblPar6.getText()).asDouble()));
				if(!OneModelWizardPage1.lblPar7.getText().equalsIgnoreCase("") && !OneModelWizardPage1.lblPar7.getText().equalsIgnoreCase("          "))
					OneModelWizardPage1.txtPar7.setText(df.format(rexpAnt.asList().at(OneModelWizardPage1.lblPar7.getText()).asDouble()));
				
			} catch (REXPMismatchException e) {
				Rserve.saveIlcymError(MainPageWizardPage.getStrAutoDisseminationPath(), title, saveToR);
				MessageDialog.openError(new Shell(), title, "Problems while trying to get the previous parameters");
				c.close();
				e.printStackTrace();
			}catch (Exception e) {
				Rserve.saveIlcymError(MainPageWizardPage.getStrAutoDisseminationPath(), title, saveToR);
				MessageDialog.openError(new Shell(), title, "Problems while trying to get the previous parameters");
				c.close();
				e.printStackTrace();
			}
			
//			c.eval(strSentSaveImage);
			c.eval(strExt+"(file=" + '"' +MainPageWizardPage.getStrAutoDisseminationPath() + "/"+ imageName+'"'+")");

			c.eval("shapprueba<-prueba(model,datashap,datao,iniback,corrx,corry,punt=" +strMinP+":" + strMaxP + ",labx, laby, titulo,grises=F)");
			c.voidEval("dev.off()");
			
			OneModelWizardPage1.lblImageTemp.setImage(new Image(Display.getCurrent(), MainPageWizardPage.getStrAutoDisseminationPath() + "/"+ imageName));

//			OneModelWizardPage1.lblImageTemp.setImage(new Image(Display.getCurrent(), MainPageWizardPage.getStrAutoDisseminationPath() + "/"+ imageName));
			bolBackPars = true;
			c.close();
		} catch (RserveException e) {
			Rserve.saveIlcymError(MainPageWizardPage.getStrAutoDisseminationPath(), title, saveToR);
			c.close();
			e.printStackTrace();
			MessageDialog.openError(new Shell(), title, "Problems while trying to get the previous parameters");
		}
	}
	/** Metodo que guarda los parametros momentaneamente **/
	public static void setParameters(){
		try{
			c = new RConnection();
			if(bolBackPars)
				c.eval("ini=iniback;niv=niv/2");
			else
				c.eval("ini=ini1;niv=niv/2");
			
			c.eval("ini<-shapprueba$ini");
			c.eval("coefi<-shapprueba$coefi");
			c.eval("p<-shapprueba$p");
			
			OneModelWizardPage1.txtAdjust.setText(c.eval("niv").asDouble()+"");
			c.close();
		}catch (RserveException e) {
			Rserve.saveIlcymError(MainPageWizardPage.getStrAutoDisseminationPath(), title, saveToR);
			c.close();
			MessageDialog.openError(new Shell(), title, "Problems while trying to set the parameters");
			e.printStackTrace();
	    } catch (REXPMismatchException e) {
			Rserve.saveIlcymError(MainPageWizardPage.getStrAutoDisseminationPath(), title, saveToR);
			c.close();
			MessageDialog.openError(new Shell(), title, "Problems while trying to set the parameters");
			e.printStackTrace();
		}
	}
	
	/** Metodo llamado por otros metodos para estimar los parametros **/
	public static void estimateParameters(RConnection c, int model, boolean bolAllModels, boolean modify, String str1, String str2, String str3, String str4, String str5, String str6, String str7){
		try{
			saveToR+="\r\n"+ "model<-"+ model +"\r\n";
			c.eval("model<-"+model);
			
			switch (model) {
			
			case 1:/** Simple logistic **/
				if(bolAllModels){
					saveToR+="ini=c(c1=0.4305,a=3.33407,b=-0.19878)"+"\r\n";
					c.eval("ini=c(c1=0.4305,a=3.33407,b=-0.19878)");
				}else{
					saveToR+="ini <- list(c1=" + str1 + ",a=" + str2 + ",b =" + str3+ ")"+"\r\n";
					c.eval("ini <- list(c1=" + str1 + ",a=" + str2 + ",b =" + str3 + ")");
				}
				break;
				
			case 2:/** Logisitc 1 parameter **/
				if(bolAllModels){
					saveToR+="ini=c(Dm=3.33407)"+"\r\n";
					c.eval("ini=c(Dm=3.33407)");
				}else{
					saveToR+="ini <- list(Dm=" + str2 + ")"+"\r\n";
					c.eval("ini <- list(Dm=" + str2 + ")");
				}
				break;
				
			case 3:/** Logisitc 2 parameters**/
				if(bolAllModels){
					saveToR+="ini=c(Dm=3.33407,k=0.19878)"+"\r\n";
					c.eval("ini=c(Dm=3.33407,k=0.19878)");
				}else{
					saveToR+="ini <- list(Dm=" + str2 + ",k =" + str3+ ")"+"\r\n";
					c.eval("ini <- list(Dm=" + str2 + ",k =" + str3 + ")");
				}
				break;
				
				
			case 4:/**Logisitc 4 parameters **/
				if(bolAllModels){
					saveToR+= "ini <- list(Dm = 3.33407, k = 0.19878, Yo= 0.8,Yasym=-1)"+"\n";
					c.eval("ini <- list(Dm = 3.33407, k = 0.19878, Yo= 0.8,Yasym=-1)");
				}else{
					saveToR+="ini <- list(Dm=" + str1 + ",k=" + str2 + ",Yo =" + str3  + ", Yasym =" + str4 + ")"+"\r\n";
					c.eval("ini <- list(Dm=" + str1 + ",k=" + str2 + ",Yo =" + str3  + ", Yasym =" + str4 + ")");
				}
				break;
				
				
			case 5:/**Logistic  3 parameter **/
				if(bolAllModels){
					saveToR+="ini=c(c1=0.4305,a=3.33407,b=-0.19878)"+"\r\n";
					c.eval("ini=c(c1=0.4305,a=3.33407,b=-0.19878)");
				}else{
					saveToR+="ini <- list(c1=" + str1 + ",a=" + str2 + ",b =" + str3+ ")"+"\r\n";
					c.eval("ini <- list(c1=" + str1 + ",a=" + str2 + ",b =" + str3 + ")");
				}
				break;
			
		
		
			default:
				break;
			}
		}catch (RserveException e) {
			Rserve.saveIlcymError(MainPageWizardPage.getStrAutoDisseminationPath(), title, saveToR);
			c.close();
			e.printStackTrace();
			MessageDialog.openError(new Shell(), title, "Problems while trying converge the parameters in the model: " + getModelName(model+""));
		}
		
	}
	
	/** Metodo que guarda en los parametros estimados **/
	private static Double[] saveTempar(String tempar, int model) throws RserveException, REXPMismatchException{
		Double[] dbpars=new Double[]{0.0,0.0,0.0,0.0,0.0,0.0,0.0};
		dbStdpars = new Double[]{0.0,0.0,0.0,0.0,0.0,0.0,0.0};
		String[] parsName = new String[]{"","","","","","",""};
		
		switch (model) {
		case 1:
			dbpars[0] = c.eval(tempar + "["+'"'+"c1"+'"'+"]").asList().at(0).asDouble();
			dbpars[1] = c.eval(tempar + "["+'"'+"a"+'"'+"]").asList().at(0).asDouble();
			dbpars[2] = c.eval(tempar + "["+'"'+"b"+'"'+"]").asList().at(0).asDouble();
			
			dbStdpars[0] = c.eval("Std.Error").asDoubles()[0];
			dbStdpars[1] = c.eval("Std.Error").asDoubles()[1];
			dbStdpars[2] = c.eval("Std.Error").asDoubles()[2];
			
			parsName[0] = "c1";
			parsName[1] = "a";
			parsName[2] = "b";
			break;
		case 2:
			dbpars[0] = c.eval(tempar + "["+'"'+"Dm"+'"'+"]").asList().at(0).asDouble();
					
			dbStdpars[0] = c.eval("Std.Error").asDoubles()[0];
	
			parsName[0] = "Dm";
	
			break;
		case 3:
			dbpars[0] = c.eval(tempar + "["+'"'+"Dm"+'"'+"]").asList().at(0).asDouble();
			dbpars[1] = c.eval(tempar + "["+'"'+"k"+'"'+"]").asList().at(0).asDouble();
		
			
			dbStdpars[0] = c.eval("Std.Error").asDoubles()[0];
			dbStdpars[1] = c.eval("Std.Error").asDoubles()[1];
	
			
			parsName[0] = "Dm";
			parsName[1] = "k";
	
			break;
		case 4:
			dbpars[0] = c.eval(tempar + "["+'"'+"Dm"+'"'+"]").asList().at(0).asDouble();
			dbpars[1] = c.eval(tempar + "["+'"'+"k"+'"'+"]").asList().at(0).asDouble();
			dbpars[2] = c.eval(tempar + "["+'"'+"Yo"+'"'+"]").asList().at(0).asDouble();
			dbpars[3] = c.eval(tempar + "["+'"'+"Yasym"+'"'+"]").asList().at(0).asDouble();
			
			dbStdpars[0] = c.eval("Std.Error").asDoubles()[0];
			dbStdpars[1] = c.eval("Std.Error").asDoubles()[1];
			dbStdpars[2] = c.eval("Std.Error").asDoubles()[2];
			dbStdpars[3] = c.eval("Std.Error").asDoubles()[2];
			
			parsName[0] = "Dm";
			parsName[1] = "k";
			parsName[2] = "Yo";
			parsName[3] = "Yasym";
			break;
		case 5:
			dbpars[0] = c.eval(tempar + "["+'"'+"c1"+'"'+"]").asList().at(0).asDouble();
			dbpars[1] = c.eval(tempar + "["+'"'+"a"+'"'+"]").asList().at(0).asDouble();
			dbpars[2] = c.eval(tempar + "["+'"'+"b"+'"'+"]").asList().at(0).asDouble();
			
			dbStdpars[0] = c.eval("Std.Error").asDoubles()[0];
			dbStdpars[1] = c.eval("Std.Error").asDoubles()[1];
			dbStdpars[2] = c.eval("Std.Error").asDoubles()[2];
			
			parsName[0] = "c1";
			parsName[1] = "a";
			parsName[2] = "b";
			break;
	
		default:
			break;
		}
		
		pars.setParametersName(parsName);
		return dbpars;
	}
	
	public static int getModelNumber(String StrNameModel){
		int strModelNumber=-1;
		for(int i=0; i<lstModelNames[0].length; i++){
			if(lstModelNames[0][i].equalsIgnoreCase(StrNameModel)){
				strModelNumber = Integer.valueOf(lstModelNames[1][i]);
				break;
			}
		}
		return strModelNumber;
	}
	public static String getModelName(String StrNumberModel){
		String strModelNumber="";
		for(int i=0; i<lstModelNames[0].length; i++){
			if(lstModelNames[1][i].equalsIgnoreCase(StrNumberModel)){
				strModelNumber = lstModelNames[0][i];
				break;
			}
		}
		return strModelNumber;
	}

	public static String functToString(REXP func) throws RserveException, REXPMismatchException{
		  
		   String newValue="";
		   String strfunc = func.asString();
		   String[] arrayfunc = ArrayConvertions.StringtoArray(strfunc, ",");
		   
		   newValue = arrayfunc[1].trim() + ' '+ arrayfunc[0].trim() + ' ';
		   
		   for(int i=2;i<arrayfunc.length; i++){
			   if(i==arrayfunc.length-1)
				   newValue += arrayfunc[i];
			   else
				   newValue += arrayfunc[i]+',';
		   }
		   
		   return newValue;
	}
}
