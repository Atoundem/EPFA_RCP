package org.icipe.epfa.modeldesigner;

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
import org.icipe.epfa.modeldesigner.wizards.MainPageWizardPage;
import org.icipe.epfa.modeldesigner.wizards.OneModelWizardPage1;
import org.icipe.epfa.modeldesigner.wizards.OneModelWizardPage2;
import org.icipe.epfa.modeldesigner.wizards.SeveralModelsWizardPage;
import org.icipe.epfa.modeldesigner.windows.LoadPlotData;
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

public class DevelopmentRate {
	
	public static String[] lstArrayNames = new String[]{"Anlytis", "Briere", "Deva", "Hilbert & Logan", "Janish", "Kontodimas", "Lactin",
		"Logan", "Rawtosky", "SharpeDeMichelle", "Stinner", "Beta", "Lorentzian","Agricultural model", "Other models"};
	
	public static String[][] lstMatrixSubNames = new String[][]{{"Anlytis 1","Anlytis 2","Anlytis 3"},{"Briere 1","Briere 2","Briere 3",
		"Briere 4"},{"Deva 1","Deva 2"},{"Hilbert & Logan 1","Hilbert & Logan 2","Hilbert & Logan 3"},{"Janish 1","Janish 2"},
		{"Kontodimas 1", "Kontodimas 2", "Kontodimas 3"},{"Lactin 1","Lactin 2","Lactin 3"},{"Logan 1","Logan 2","Logan 3","Logan 4","Logan 5"},
		{"Rawtosky 1", "Rawtosky 2", "Rawtosky 3"},{"SharpeDeMichelle 1", "SharpeDeMichelle 2", "SharpeDeMichelle 3", "SharpeDeMichelle 4","SharpeDeMichelle 5",
		"SharpeDeMichelle 6","SharpeDeMichelle 7", "SharpeDeMichelle 8", "SharpeDeMichelle 9", "SharpeDeMichelle 10","SharpeDeMichelle 11",
		"SharpeDeMichelle 12", "SharpeDeMichelle 13","SharpeDeMichelle 14"},{"Stinner 1","Stinner 2", "Stinner 3","Stinner 4"},{"Beta 1","Beta 2", 
			"Beta 3","Beta 4","Beta 5","Beta 6", "Beta 7","Beta 8"},{"Lorentzian 3-parameter","Lorentzian 4-parameter"},
			{"MAIZSIM","Enzymatic Response","Wang et Engel","Richards","Gompertz","Q10 function","Bell curve","Gaussian function","Exponential first order plus logistic","Modified exponential","Log normal 3-parameter","Pseudo-voigt 4 parameter"},
			{"Linear","Exponential Simple","Tb Model","Exponential Model","Exponential Tb", "Davidson", "Pradham", "Angilletta Jr.", "Hilbert", "Allahyari",
		"Tanigoshi", "Wang-Lan-Ding","Taylor","Sigmoid or logistic"}};
	
	public static String[][] lstModelNames = new String[][]{{"SharpeDeMichelle 1", "SharpeDeMichelle 2", "SharpeDeMichelle 3", "SharpeDeMichelle 4", 
		"SharpeDeMichelle 5","SharpeDeMichelle 6","SharpeDeMichelle 7", "SharpeDeMichelle 8", "SharpeDeMichelle 9", "SharpeDeMichelle 10", 
		"SharpeDeMichelle 11","SharpeDeMichelle 12", "SharpeDeMichelle 13","SharpeDeMichelle 14","Deva 1","Deva 2","Logan 1","Logan 2","Logan 3",
		"Logan 4","Logan 5","Briere 1","Briere 2","Briere 3","Briere 4","Stinner 1","Stinner 2","Stinner 3","Stinner 4", "Lactin 1","Lactin 2","Lactin 3",
		"Kontodimas 1", "Kontodimas 2", "Kontodimas 3","Janish 1","Janish 2","Rawtosky 1", "Rawtosky 2","Anlytis 1","Anlytis 2","Anlytis 3",
		"Hilbert & Logan 1","Hilbert & Logan 2","Hilbert & Logan 3", "Linear","Exponential Simple", "Tb Model", "Exponential Model", "Exponential Tb",
		"Davidson", "Pradham", "Angilletta Jr.", "Hilbert", "Allahyari", "Tanigoshi","Wang-Lan-Ding","Taylor","Sigmoid or logistic","MAIZSIM",
		"Enzymatic Response","beta 1","Wang et Engel","Richards","Gompertz","Beta 2","Q10 function","Rawtosky 3","Beta 3","Bell curve","Gaussian function",
		"Beta 4","Exponential first order plus logistic","Beta 5","Beta 6","Beta 7","Beta 8","Modified exponential","Lorentzian 3-parameter",
		"Lorentzian 4-parameter","Log normal 3-parameter","Pseudo-voigt 4 parameter"},{"1","2","3",
		"4","5", "6","7","8","9","10","11","12","13","14","15","16","17","18","52","53","54","19","20","40","41","21","33","50","51","23","35","58",
		"42","43","44","46","47","29","45","36","37","38","22","55","56","24","25","26","27","28","30","31","32","34","39","48","49","57","59","60","61"
		,"62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82"}};
	
	public static String title = "Mortality";
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
			ip.setCorrX2("50");
			ip.setCorrY1("0");
			ip.setCorrY2("100");
			ip.setMini("0");
			ip.setMaxi("100");
			ip.setLegX("0");
			ip.setLegY("0");
			ip.setLabX("Temperature (Degree Celsius)");
			ip.setLabY("Mortality (%)");
			ip.setTitle("");
			ip.setScaleY("10");
			ip.setScaleX("5");
			
//			if(!Platform.getOS().equalsIgnoreCase("macosx")){
				c = Rserve.launchRserve("",c);
				
				imageName = title+".png";
				strExt = "png";
				strSentSaveImage = "png(file=" + '"' +MainPageWizardPage.getstrMortalityPath() + "/"+ imageName+'"'+")";
				
/*				
				imageName = "MaizeDevRate-"+ MainPageWizardPage.comboDevStage.getText()+".png";
				strExt = "png";
				
				strSentSaveImage = "png(file=" + '"' +MainPageWizardPage.getstrMortalityPath() + "/"+"MaizeDevRate-"+ MainPageWizardPage.comboDevStage.getText()+"."+strExt+'"'+")";
//				c.eval("png(file=" + '"' +MainPageWizardPage.getstrMortalityPath() + "/"+MainPageWizardPage.comboDevStage.getText()+"-Model"+ modelm+".png"+'"'+")");
			}else{
				imageName = title+".jpeg";
				strExt = "jpeg";
				
				strSentSaveImage = "jpeg(file=" + '"' +MainPageWizardPage.getstrMortalityPath() + "/"+ imageName+'"'+")";
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
			Rserve.saveIlcymError(MainPageWizardPage.getstrMortalityPath(), title, saveToR);
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
			
			String strwd = (MainActionRate.pathProj + File.separator + "Data").replace('\\', '/');
			
			saveToR+= "setwd(" + '"' + strwd + '"'+ ')'+"\r\n";
			c.eval("setwd(" + '"' + strwd + '"'+ ')');
			
			saveToR+= "library(minpack.lm)"+"\r\n";
			c.eval("library(minpack.lm)");
			
			saveToR+= "library(MASS)"+"\r\n";
			c.eval("library(MASS)");
			
			String pathProject = MainActionRate.pathProj + File.separator + "DevelopmentTime" + File.separator + MainPageWizardPage.getStageSel();
			pathProject = pathProject.replace("\\", "/");
			saveToR+="load(" + '"' + pathProject + "/toTemp.RData" + '"' +")"+"\r\n";
			c.eval("load(" + '"' + pathProject + "/toTemp.RData" + '"' +")");
			//MessageDialog.openInformation(new Shell(), "path", "cargo datos de dev time");//TODO
		} catch (RserveException e) {
			Rserve.saveIlcymError(MainPageWizardPage.getstrMortalityPath(), title, saveToR);
			c.close();
			e.printStackTrace();
			MessageDialog.openError(new Shell(), title, "Problems while trying to get the values from Development Time");
		}
	}
	
	/** Metodo que procesa todos los modelos seleccionados (cmpAllModelsPage) **/
	
	public static void proceesAllModelsNew(boolean bolExtremMin){
		String path= MainPageWizardPage.getstrMortalityPath() + File.separator + imageName;
		 
//		String stageSel = MainPageWizardPage.getStageSel();
		String algo = MainPageWizardPage.getSelectAlgo();
		SeveralModelsWizardPage.tableCriterias.removeAll();
		SeveralModelsWizardPage.lblModelSelAll.setText("");
		SeveralModelsWizardPage.txtParametersEstimatedAll.setText("");
//		String pathMotData = MainPageWizardPage.getFileDevStageData();
		String pathMotData = LoadPlotData.getFileMortalityData();
		
		Boolean charge = false;
		
		try {
//			String lib = configTool.getLibPath();
			c = new RConnection();
//			loadParamsFromDTNew(lib);
			if (charge == false)
			{
				saveToR+="datm<-read.table('"+pathMotData+"',header = T)"+"\n";
				c.eval("datm<-read.table('"+pathMotData+"',header = T)");
				
				saveToR+="datm[,2]= datm[,2]+0.00001"+"\n";
				c.eval("datm[,2]= datm[,2]+0.00001");
				
				saveToR+="datashap <- data.frame(x =datm[,1] ,y =datm[,2],Lower =datm[,2], Upper=datm[,2])"+"\n";
				c.eval("datashap <- data.frame(x =datm[,1] ,y =datm[,2],Lower =datm[,2], Upper=datm[,2])");
				
				saveToR+="datao <- data.frame(x =c(datm[,1],datm[,1],datm[,1]) ,y =c(datm[,2],datm[,2],datm[,2]))"+"\n";
				c.eval("datao <- data.frame(x =c(datm[,1],datm[,1],datm[,1]) ,y =c(datm[,2],datm[,2],datm[,2]))");
						
				
//				EPFAUtils.createTempScriptFile("/RScripts/mortalityDesigner_New.r");
				
//		        saveToR += "source('"+MainPageWizardPage.getstrMortalityPath()+"tempScripFile.r'"+")"+"\r\n";	
//		        c.eval("source('"+MainPageWizardPage.getstrMortalityPath()+"tempScripFile.r'"+")");
		       
				EPFAUtils.createTempScriptFile("/RScripts/mortalityDesigner_New.r");
		           
		           saveToR += "source('"+MainPageWizardPage.getstrMortalityPath()+"/tempScripFile.r'"+")"+"\r\n";	
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
//				path =MainPageWizardPage.getstrMortalityPath() + "/"+MainPageWizardPage.comboDevStage.getText()+"-Model"+ model+"."+strExt;
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
//				c.eval("sink("+'"'+MainPageWizardPage.getstrMortalityPath() + "/Dev_Rate-Model"+(model)+".txt"+'"'+")");
				c.eval("sink("+'"'+MainPageWizardPage.getstrMortalityPath() + "/Mort-Model" +model+".txt"+'"'+")");
				
//				c.eval("sink("+'"'+MainPageWizardPage.getstrMortalityPath() + "/Mort-Model" +modelm+".txt"+'"'+")");

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
				
				c.eval(strExt+"(file=" + '"' +MainPageWizardPage.getstrMortalityPath() + "/"+"Mort-Model"+ model+"."+strExt+'"'+")");
				//MessageDialog.openInformation(new Shell(), "path", "plot");//TODO
				saveToR+="plot_shape<-grafshape(model,estshap,datashap,sdli,qtt,corrx,corry,mini=0,maxi=100,coefi,limit,1,labx,laby,titulo, grises, scaleY, scaleX)"+"\r\n";
				c.eval("plot_shape<-grafshape(model,estshap,datashap,sdli,qtt,corrx,corry,mini=0,maxi=100,coefi,limit,1,labx,laby,titulo,grises, scaleY, scaleX)");
				//MessageDialog.openInformation(new Shell(), "path", "fin plot");//TODO
				c.eval("dev.off()");
				c.eval("sink()");
				
				uiShape1.lblFunctImageDR.setImage(new Image(Display.getCurrent(), MainPageWizardPage.getstrMortalityPath() + File.separator + "Mort-Model"+ model+"."+strExt));
				uiShape1.lblFunctImageDR.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
			  	
			  	EPFAUtils.createHTMLfile(MainPageWizardPage.getstrMortalityPath(),"Mort-Model"+model);
	//		  	uiShape1.bwrResultDR.setUrl("file:///" + MainPageWizardPage.getstrMortalityPath() + "/Dev_Rate-Model"+(model)+".html");
			  	uiShape1.bwrResultDR.setUrl(MainPageWizardPage.getstrMortalityPath()+"Mort-Model"+model+".html");

				
				TableItem ti = new TableItem(SeveralModelsWizardPage.tableCriterias, SWT.None);
				ti.setText(new String[]{getModelNumber(MainPageWizardPage.lstSelectedModels.getItem(i))+"",MainPageWizardPage.lstSelectedModels.getItem(i),framesSel[0],framesSel[1],framesSel[2],framesSel[3],framesSel[4],framesSel[5]});
				
				c.close();
				arraysShell[i] = uiShape1.shell;
				uiShape1.shell.open();
				
			} catch (RserveException e) {
				Rserve.saveIlcymError(MainPageWizardPage.getstrMortalityPath(), title, saveToR);
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
				Rserve.saveIlcymError(MainPageWizardPage.getstrMortalityPath(), title, saveToR);
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
		//String path =MainPageWizardPage.getstrMortalityPath() + "/"+MainPageWizardPage.comboDevStage.getText()+"-Model"+ model+"."+strExt;
//		String stageSel = MainPageWizardPage.getStageSel();
		String algo = MainPageWizardPage.getSelectAlgo();
		String pathMotData = LoadPlotData.getFileMortalityData();
		Boolean charge = false;
		
		try {
			c = new RConnection();
			OneModelWizardPage1.lblImageTemp.setImage(null);
			
//			loadParamsFromDTNew(EPFAUtils.getLibPath());
			if (charge == false)
			{
				saveToR+="datm<-read.table('"+pathMotData+"',header = T)"+"\n";
				c.eval("datm<-read.table('"+pathMotData+"',header = T)");
				
				saveToR+="datm[,2]= datm[,2]+0.00001"+"\n";
				c.eval("datm[,2]= datm[,2]+0.00001");
				
				saveToR+="datashap <- data.frame(x =datm[,1] ,y =datm[,2],Lower =datm[,2], Upper=datm[,2])"+"\n";
				c.eval("datashap <- data.frame(x =datm[,1] ,y =datm[,2],Lower =datm[,2], Upper=datm[,2])");
				
				saveToR+="datao <- data.frame(x =c(datm[,1],datm[,1],datm[,1]) ,y =c(datm[,2],datm[,2],datm[,2]))"+"\n";
				c.eval("datao <- data.frame(x =c(datm[,1],datm[,1],datm[,1]) ,y =c(datm[,2],datm[,2],datm[,2]))");
						
				
				EPFAUtils.createTempScriptFile("/RScripts/mortalityDesigner_New.r");
				
		        saveToR += "source('"+MainPageWizardPage.getstrMortalityPath()+"/tempScripFile.r'"+")"+"\r\n";	
		        c.eval("source('"+MainPageWizardPage.getstrMortalityPath()+"/tempScripFile.r'"+")");
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
			
			c.eval(strExt+"(file=" + '"' +MainPageWizardPage.getstrMortalityPath() + "/"+"Mort-Model"+ modelo+"."+strExt+'"'+")");
			
//			saveToR += "corry <- c(0,1.5*max(datashap[,2]))" + "\r\n";
//			c.eval("corry <- c(0,1.5*max(datashap[,2]))");
	//		c.eval("corry <- c(0,2*max(datashap[,2]))");   ip.setCorrY1("0");
			
			
//			ip.setCorrY2(c.eval("1.5*max(datashap[,2])").asString());
			
	//		c.eval(strExt+"(file=" + '"' +MainPageWizardPage.getstrMortalityPath() + "/Dev_Rate-Model"+ (modelo)+"."+strExt+'"'+")");
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
//			OneModelWizardPage1.lblImageTemp.setImage(new Image(Display.getCurrent(), MainPageWizardPage.getstrMortalityPath() + "/Dev_Rate-Model"+ (modelo)+"."+strExt));
			OneModelWizardPage1.lblImageTemp.setImage(new Image(Display.getCurrent(), MainPageWizardPage.getstrMortalityPath() + "/"+"Mort-Model"+ modelo+"."+strExt));
																				
			
			OneModelWizardPage1.txtPar1.setText(df.format(dbpars[0]));
			OneModelWizardPage1.txtPar2.setText(df.format(dbpars[1]));
			OneModelWizardPage1.txtPar3.setText(df.format(dbpars[2]));
			OneModelWizardPage1.txtPar4.setText(df.format(dbpars[3]));
			OneModelWizardPage1.txtPar5.setText(df.format(dbpars[4]));
			OneModelWizardPage1.txtPar6.setText(df.format(dbpars[5]));
			OneModelWizardPage1.txtPar7.setText(df.format(dbpars[6]));
		  	
			c.close();
		
		}catch (RserveException e) {
			Rserve.saveIlcymError(MainPageWizardPage.getstrMortalityPath(), title, saveToR);
			c.close();
			e.printStackTrace();
			MessageDialog.openError(new Shell(), title, "Problems while trying to process one model");
		} catch (REXPMismatchException e) {
			Rserve.saveIlcymError(MainPageWizardPage.getstrMortalityPath(), title, saveToR);
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
			
			c.eval(strExt+"(file=" + '"' +MainPageWizardPage.getstrMortalityPath() + "/Dev_Rate-Model"+ (model)+"."+strExt+'"'+")");
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
			
//			OneModelWizardPage1.lblImageTemp.setImage(new Image(Display.getCurrent(), MainPageWizardPage.getstrMortalityPath() + "/Dev_Rate-Model"+ (model)+"."+strExt));
			OneModelWizardPage1.lblImageTemp.setImage(new Image(Display.getCurrent(), MainPageWizardPage.getstrMortalityPath() + "/"+"Mort-Model"+ model+"."+strExt));
			c.close();
		} catch (RserveException e) {
			Rserve.saveIlcymError(MainPageWizardPage.getstrMortalityPath(), title, saveToR);
			e.printStackTrace();
			MessageDialog.openError(new Shell(), title, "Problems while trying to estimate the parameters separately");
			c.close();
		} catch (REXPMismatchException e) {
			Rserve.saveIlcymError(MainPageWizardPage.getstrMortalityPath(), title, saveToR);
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
			
			c.eval("sink("+'"'+ MainPageWizardPage.getstrMortalityPath() + "/" + "EPFA_"+title + ".txt"+'"'+")");
			//c.eval("sink("+'"'+MainPageWizardPage.getstrMortalityPath() + "/"+MainPageWizardPage.comboDevStage.getText()+"-Model" +modelo+".txt"+'"'+")");
//			c.eval("sink("+'"'+ MainPageWizardPage.getstrMortalityPath() + "/" +"MaizeDevRate-"+ MainPageWizardPage.comboDevStage.getText() + ".txt"+'"'+")");
			
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
			//strSentSaveImage = "png(file=" + '"' +MainPageWizardPage.getstrMortalityPath() + "/" +MainPageWizardPage.comboDevStage.getText()+ imageName+'"'+")";
			c.eval(strExt+"(file=" + '"' +MainPageWizardPage.getstrMortalityPath() + "/"+"EPFA_"+title+"."+strExt+'"'+")");
			
			saveToR += "plot_shape<-grafshape(model,estshap,datashap,sdli,qtt,corrx,corry,mini,maxi,coefi,limit,1,labx,laby,titulo,grises=FALSE, scaleY, scaleX)"+"\r\n";
			c.eval("plot_shape<-grafshape(model,estshap,datashap,sdli,qtt,corrx,corry,mini,maxi,coefi,limit,1,labx,laby,titulo,grises=FALSE, scaleY, scaleX)");
		
			c.eval("dev.off()");
			
			dbpars= saveTempar("tempar", modelo);
			pars.setParameters(dbpars);
			pars.setStrModel(getModelName(modelo+""));
			pars.setStdParameters(dbStdpars);
			
			
			EPFAUtils.createHTMLfile(MainPageWizardPage.getstrMortalityPath(),"EPFA_"+title );
//		  	EPFAUtils.createHTMLfile(MainPageWizardPage.getstrMortalityPath(), MainPageWizardPage.comboDevStage.getText()+"-Model"+title);
//			EPFAUtils.createHTMLfile(MainPageWizardPage.getstrMortalityPath(), title);
//			File temp = new File(MainPageWizardPage.getstrMortalityPath() + File.separator + title + ".txt");
//		  	File temp = new File(MainPageWizardPage.getstrMortalityPath() + "/"+MainPageWizardPage.comboDevStage.getText()+"-Model" +modelo+ ".txt");
//			temp.delete();
			
/*			uiShape1.lblFunctImageDR.setImage(new Image(Display.getCurrent(), MainPageWizardPage.getstrMortalityPath() + File.separator + MainPageWizardPage.comboDevStage.getText()+"-Model"+ model+"."+strExt));
			uiShape1.lblFunctImageDR.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		  	
		  	EPFAUtils.createHTMLfile(MainPageWizardPage.getstrMortalityPath(), MainPageWizardPage.comboDevStage.getText()+"-Model"+model);
//		  	uiShape1.bwrResultDR.setUrl("file:///" + MainPageWizardPage.getstrMortalityPath() + "/Dev_Rate-Model"+(model)+".html");
		  	uiShape1.bwrResultDR.setUrl(MainPageWizardPage.getstrMortalityPath() + MainPageWizardPage.comboDevStage.getText()+"-Model"+model+".html");

*/			
			
//			OneModelWizardPage1.lblImageTemp.setImage(new Image(Display.getCurrent(), MainPageWizardPage.getstrMortalityPath() + "/"+MainPageWizardPage.comboDevStage.getText()+"-Model"+ model+"."+strExt));
			OneModelWizardPage2.lblImageFinal.setImage(new Image(Display.getCurrent(),MainPageWizardPage.getstrMortalityPath()+ File.separator +"EPFA_"+imageName));
//			OneModelWizardPage2.brwModelSel.setUrl("file:///" + MainPageWizardPage.getstrMortalityPath() + File.separator + title +".html");
			OneModelWizardPage2.brwModelSel.setUrl(MainPageWizardPage.getstrMortalityPath() + "EPFA_"+title+".html");
			OneModelWizardPage2.lblModelSelOne.setText(pars.getStrModel()); 
			
			//here we have to save R workSave in for the use for the mapping
			saveToR+="save(list = ls(all.names = TRUE), file = "+ '"' + MainPageWizardPage.getstrMortalityPath() + "/" + "EPFA_"+title + ".RData"+'"'+", envir = .GlobalEnv)"+"\n";
			c.eval("save(list = ls(all.names = TRUE), file = "+ '"' + MainPageWizardPage.getstrMortalityPath() + "/" + "EPFA_"+title + ".RData"+'"'+", envir = .GlobalEnv)");
			
			c.close();
		} catch (RserveException e) {
			Rserve.saveIlcymError(MainPageWizardPage.getstrMortalityPath(), title, saveToR);
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
			Rserve.saveIlcymError(MainPageWizardPage.getstrMortalityPath(), title, saveToR);
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
			c.eval("plot_shape<-grafshape(model,estshap,datashap,sdli,qtt,corrx=c(0,50),corry=c(0,100),mini=0,maxi=100,coefi,limit,1,labx="+'"'+"temperature (degree celsius)"+'"'+",laby="+'"'+"development rate (1/day)"+'"'+",titulo="+'"'+""+'"'+",grises=FALSE, scaleY=0.1, scaleX=5)");
			
			c.eval("dev.off()");
			lblImage.setImage(new Image(Display.getCurrent(), pathImage));
			c.close();
		} catch (RserveException e) {
			Rserve.saveIlcymError(MainPageWizardPage.getstrMortalityPath(), title, saveToR);
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
			
			
//			c.eval("sink("+'"'+ MainPageWizardPage.getstrMortalityPath() + "/" +"MaizeDevRate-"+ MainPageWizardPage.comboDevStage.getText() + ".txt"+'"'+")");
			c.eval("sink("+'"'+MainPageWizardPage.getstrMortalityPath() + "/" +"EPFA_"+title + ".txt"+'"'+")");
			
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
			c.eval(strExt+"(file=" + '"' +MainPageWizardPage.getstrMortalityPath() + "/" + "EPFA_"+imageName+'"'+")");
			
			saveToR += "plot_shape<-grafshape(model,estshap,datashap,sdli,qtt,corrx,corry,mini,maxi,coefi,limit,1,labx,laby,titulo,grises, scaleY, scaleX)"+"\r\n";
			c.eval("plot_shape<-grafshape(model,estshap,datashap,sdli,qtt,corrx,corry,mini,maxi,coefi,limit,1,labx,laby,titulo,grises, scaleY, scaleX)");
		
			c.eval("dev.off()");
			
			dbpars= saveTempar("tempar", modelo);
			pars.setParameters(dbpars);
			pars.setStdParameters(dbStdpars);
			
			EPFAUtils.createHTMLfile(MainPageWizardPage.getstrMortalityPath(), "EPFA_"+title);
//			File temp = new File(MainPageWizardPage.getstrMortalityPath()+ File.separator + title + ".txt");
//			temp.delete();
			
			//here we have to save R workSave in for the use for the mapping
			saveToR+="save(list = ls(all.names = TRUE), file = "+ '"' + MainPageWizardPage.getstrMortalityPath() + "/" +"EPFA_"+ title+ ".RData"+'"'+", envir = .GlobalEnv)"+"\n";
			c.eval("save(list = ls(all.names = TRUE), file = "+ '"' + MainPageWizardPage.getstrMortalityPath() + "/" + "EPFA_"+title+ ".RData"+'"'+", envir = .GlobalEnv)");
			
			c.close();
		} catch (RserveException e) {
			Rserve.saveIlcymError(MainPageWizardPage.getstrMortalityPath(), title, saveToR);
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
			Rserve.saveIlcymError(MainPageWizardPage.getstrMortalityPath(), title, saveToR);
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
		String[] arrayFiles = new File(MainPageWizardPage.getstrMortalityPath()).list();
		
		for(int i=0; i<arrayFiles.length; i++){
			if(new File(MainPageWizardPage.getstrMortalityPath().replace('/', '\\')+'\\'+arrayFiles[i]).isFile())
				if(!arrayFiles[i].startsWith("EPFA_"))
					new File(MainPageWizardPage.getstrMortalityPath().replace('/', '\\')+'\\'+arrayFiles[i]).delete();
		}
	}
	
	
	/** guarda el archivo resume con el modelo y parametros seleccionados **/
	private static void guardarResume(){
		File fResume = new File(MainPageWizardPage.getstrMortalityPath().replace('/', '\\')+"resume.EPFA");  //file.createNewFile()
		try {
			fResume.createNewFile();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
				//new File("\""+(MainActionMaizeDevRate.pathProj + File.separator +"resume.ilcym").replace("\\", "/")+"\"");//modified for testing
		System.out.println(fResume);
		//File fResume = new File(MainActionMaizeDevRate.pathProj + File.separator+title + File.separator +"resume.epfa");
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
				prop.store(new FileOutputStream(fResume), "EPFA Mortality Model Summary");
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
				Rserve.saveIlcymError(MainPageWizardPage.getstrMortalityPath(), title, saveToR);
				c.close();
				e.printStackTrace();
			} catch (REXPMismatchException e) {
				Rserve.saveIlcymError(MainPageWizardPage.getstrMortalityPath(), title, saveToR);
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
		File fResume = new File(MainActionRate.pathProj + File.separator + title + File.separator +"resume.ilcym");//verificar ruta
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
				Rserve.saveIlcymError(MainPageWizardPage.getstrMortalityPath(), title, saveToR);
				MessageDialog.openError(new Shell(), title, "Problems while saving the parameters in the resume sheet");
				c.close();
				e.printStackTrace();
			} catch (REXPMismatchException e) {
				Rserve.saveIlcymError(MainPageWizardPage.getstrMortalityPath(), title, saveToR);
				MessageDialog.openError(new Shell(), title, "Problems while saving the parameters in the resume sheet");
				c.close();
				e.printStackTrace();
			}
			
		}catch (RserveException e) {
			Rserve.saveIlcymError(MainPageWizardPage.getstrMortalityPath(), title, saveToR);
			e.printStackTrace();
			c.close();
			MessageDialog.openError(new Shell(), title, "Problems while saving the parameters in the resume sheet");
			return;
        }catch (Exception e) {
			Rserve.saveIlcymError(MainPageWizardPage.getstrMortalityPath(), title, saveToR);
        	c.close();
			e.printStackTrace();
			MessageDialog.openError(new Shell(), title, e.getMessage());
		}
	}

*/	
	
	/** guarda el proceso completado en el archivo progress **/
	private static void newSaveProgress(){
		File fileProg = new File(MainActionRate.pathProj + File.separator + "Progress.ilcym");
		try {
			
			BufferedReader in = new BufferedReader(new FileReader(fileProg));
	        String str,newfile="";
	        
	        
	        while ((str = in.readLine()) != null) {
	        	String[]lineT = ArrayConvertions.StringtoArray(str, "\t");
	        	if(lineT[0].trim().equalsIgnoreCase(MainPageWizardPage.getStageSel())){
	        		lineT[2] = "true";
	        	}
	       /* 	       	if(ViewProjectsUI.getRate().contains("Age") || ViewProjectsUI.getRate().contains("Temperature"))
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
			c.eval(strExt+"(file=" + '"' +MainPageWizardPage.getstrMortalityPath() + "/"+ imageName+'"'+")");
			
			c.eval("iniback <- ini1");
			c.eval("ini1=recalc(ini,niv=niv"+")");
			
			c.eval("shapprueba<-prueba(model,datashap,datao,ini1,corrx,corry,punt=" +strMinP+":" + strMaxP + ",labx, laby, titulo,grises=F)");
			c.voidEval("dev.off()");
			
//			OneModelWizardPage1.lblImageTemp.setImage(new Image(Display.getCurrent(), MainPageWizardPage.getstrMortalityPath() + "/"+ imageName));
			OneModelWizardPage1.lblImageTemp.setImage(new Image(Display.getCurrent(), MainPageWizardPage.getstrMortalityPath() + "/"+ imageName));
			
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
				Rserve.saveIlcymError(MainPageWizardPage.getstrMortalityPath(), title, saveToR);
				c.close();
				e.printStackTrace();
				MessageDialog.openError(new Shell(), title, "Problems while trying to random the parameters");
			}catch (Exception e) {
				Rserve.saveIlcymError(MainPageWizardPage.getstrMortalityPath(), title, saveToR);
				MessageDialog.openError(new Shell(), title, "Problems while trying to random the parameters");
				c.close();
				e.printStackTrace();
			}
			
			bolBackPars = false;
			c.close();
		} catch (RserveException e) {
			Rserve.saveIlcymError(MainPageWizardPage.getstrMortalityPath(), title, saveToR);
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
				Rserve.saveIlcymError(MainPageWizardPage.getstrMortalityPath(), title, saveToR);
				MessageDialog.openError(new Shell(), title, "Problems while trying to get the previous parameters");
				c.close();
				e.printStackTrace();
			}catch (Exception e) {
				Rserve.saveIlcymError(MainPageWizardPage.getstrMortalityPath(), title, saveToR);
				MessageDialog.openError(new Shell(), title, "Problems while trying to get the previous parameters");
				c.close();
				e.printStackTrace();
			}
			
//			c.eval(strSentSaveImage);
			c.eval(strExt+"(file=" + '"' +MainPageWizardPage.getstrMortalityPath() + "/"+ imageName+'"'+")");

			c.eval("shapprueba<-prueba(model,datashap,datao,iniback,corrx,corry,punt=" +strMinP+":" + strMaxP + ",labx, laby, titulo,grises=F)");
			c.voidEval("dev.off()");
			
			OneModelWizardPage1.lblImageTemp.setImage(new Image(Display.getCurrent(), MainPageWizardPage.getstrMortalityPath() + "/"+ imageName));

//			OneModelWizardPage1.lblImageTemp.setImage(new Image(Display.getCurrent(), MainPageWizardPage.getstrMortalityPath() + "/"+ imageName));
			bolBackPars = true;
			c.close();
		} catch (RserveException e) {
			Rserve.saveIlcymError(MainPageWizardPage.getstrMortalityPath(), title, saveToR);
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
			Rserve.saveIlcymError(MainPageWizardPage.getstrMortalityPath(), title, saveToR);
			c.close();
			MessageDialog.openError(new Shell(), title, "Problems while trying to set the parameters");
			e.printStackTrace();
	    } catch (REXPMismatchException e) {
			Rserve.saveIlcymError(MainPageWizardPage.getstrMortalityPath(), title, saveToR);
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
			case 1:
				if(bolAllModels){
					saveToR+="xha1<-log(datashap[,2]);xha=xha1[xha1 != -100]"+"\r\n";
					c.eval("xha1<-log(datashap[,2]);xha=xha1[xha1 != -100]");
					
					saveToR+="yha<-(1/(datashap[,1]+273.15))[xha1 != -100]"+"\r\n";
					c.eval("yha<-(1/(datashap[,1]+273.15))[xha1 != -100]");
					
					saveToR+="beta<-as.numeric(coef(lm(xha~yha))[2])"+"\r\n";
					c.eval("beta<-as.numeric(coef(lm(xha~yha))[2])");
					
					saveToR+="xli<-(datashap[,1]+273.15)"+"\r\n";
					c.eval("xli<-(datashap[,1]+273.15)");
					
					saveToR+="yli<-datashap[,2]"+"\r\n";
					c.eval("yli<-datashap[,2]");
					
					saveToR+="cof<-as.numeric(coef(lm(yli~xli)))"+"\r\n";
					c.eval("cof<-as.numeric(coef(lm(yli~xli)))");
					
					saveToR+="ini <- list(Ha = -beta*1.987,Tl = -cof[1]/cof[2]+2, Hl =-100000 ,Hh =200000, Th =datashap[(order(datashap[,2]))[nrow(datashap)],1]+273.15-2)"+"\r\n";
					c.eval("ini <- list(Ha = -beta*1.987,Tl = -cof[1]/cof[2]+2, Hl =-100000 ,Hh =200000, Th =datashap[(order(datashap[,2]))[nrow(datashap)],1]+273.15-2)");
				}else{
					saveToR+="ini <- list(Ha = " + str3 + ",Tl = " + str1 + ", Hl ="+ str4 + ",Hh ="+ str5 +", Th =" + str2 + ")"+"\r\n";
					c.eval("ini <- list(Ha = " + str3 + ",Tl = " + str1 + ", Hl ="+ str4 + ",Hh ="+ str5 +", Th =" + str2 + ")");
					
					if(modify)
						c.eval("ini <- list(Ha = " + str5 + ",Tl = " + str3 + ", Hl ="+ str6 + ",Hh ="+ str7 +", Th =" + str4 + ")");
				}
				break;
			case 2:
				if(bolAllModels){
					saveToR+="xha1<-log(datashap[,2]);xha=xha1[xha1 != -100]"+"\r\n";
					c.eval("xha1<-log(datashap[,2]);xha=xha1[xha1 != -100]");
					
					saveToR+="yha<-(1/(datashap[,1]+273.15))[xha1 != -100]"+"\r\n";
					c.eval("yha<-(1/(datashap[,1]+273.15))[xha1 != -100]");
					
					saveToR+= "beta<-as.numeric(coef(lm(xha~yha))[2])"+"\r\n";
					c.eval("beta<-as.numeric(coef(lm(xha~yha))[2])");
					
					saveToR+= "xli<-(datashap[,1]+273.15)"+"\r\n";
					c.eval("xli<-(datashap[,1]+273.15)");
					
					saveToR+= "yli<-datashap[,2]"+"\r\n";
					c.eval("yli<-datashap[,2]");
					
					saveToR+= "cof<-as.numeric(coef(lm(yli~xli)))"+"\r\n";
					c.eval("cof<-as.numeric(coef(lm(yli~xli)))");
					
					saveToR+= "ini <- list(To=median(datashap[,1])+273.15,Ha = -beta*1.987,Tl = -cof[1]/cof[2]+1, Hl =-10000 ,Hh =200000, Th =datashap[(order(datashap[,2]))[nrow(datashap)],1]+273.15+2)"+"\r\n";
					c.eval("ini <- list(To=median(datashap[,1])+273.15,Ha = -beta*1.987,Tl = -cof[1]/cof[2]+1, Hl =-10000 ,Hh =200000, Th =datashap[(order(datashap[,2]))[nrow(datashap)],1]+273.15+2)");
				}else{
					saveToR+="ini <- list(To=" + str1 + ",Ha =" + str4 + ",Tl =" + str2 + ", Hl =" + str5 + ",Hh =" + str6 + ", Th =" + str3 + ")"+"\r\n";
					c.eval("ini <- list(To=" + str1 + ",Ha =" + str4 + ",Tl =" + str2 + ", Hl =" + str5 + ",Hh =" + str6 + ", Th =" + str3 + ")");
					
					if(modify)
						c.eval("ini <- list(To=" + str2 + ",Ha =" + str5 + ",Tl =" + str3 + ", Hl =" + str6 + ",Hh =" + str7 + ", Th =" + str4 + ")");
				}
				
				break;
			case 3:
				if(bolAllModels){
					saveToR+="xha1<-log(datashap[,2]);xha=xha1[xha1 != -100]"+"\r\n";
					c.eval("xha1<-log(datashap[,2]);xha=xha1[xha1 != -100]");
					
					saveToR+="yha<-(1/(datashap[,1]+273.15))[xha1 != -100]"+"\r\n";
					c.eval("yha<-(1/(datashap[,1]+273.15))[xha1 != -100]");
					
					saveToR+= "beta<-as.numeric(coef(lm(xha~yha))[2])"+"\r\n";
					c.eval("beta<-as.numeric(coef(lm(xha~yha))[2])");
					
					saveToR+= "xli<-(datashap[,1]+273.15)"+"\r\n";
					c.eval("xli<-(datashap[,1]+273.15)");
					
					saveToR+= "yli<-datashap[,2]"+"\r\n";
					c.eval("yli<-datashap[,2]");
					
					saveToR+= "cof<-as.numeric(coef(lm(yli~xli)))"+"\r\n";
					c.eval("cof<-as.numeric(coef(lm(yli~xli)))");
					
					saveToR+= "ini <- list(p=median(datashap[,2]),To=median(datashap[,1])+273.15,Ha = -beta* 1.987,Tl =-cof[1]/cof[2] +2, Hl =-100000 ,Hh =200000, Th =datashap[(order(datashap[,2]))[nrow(datashap)],1]+273.15)"+"\n";
					c.eval("ini <- list(p=median(datashap[,2]),To=median(datashap[,1])+273.15,Ha = -beta* 1.987,Tl =-cof[1]/cof[2] +2, Hl =-100000 ,Hh =200000, Th =datashap[(order(datashap[,2]))[nrow(datashap)],1]+273.15)");
				}else{
					saveToR+="ini <- list(p=" + str1 + ",To=" + str2 + ",Ha =" + str5 + ",Tl =" + str3 + ", Hl =" + str6 + ",Hh =" + str7 + ", Th =" + str4 + ")"+"\r\n";
					c.eval("ini <- list(p=" + str1 + ",To=" + str2 + ",Ha =" + str5 + ",Tl =" + str3 + ", Hl =" + str6 + ",Hh =" + str7 + ", Th =" + str4 + ")");
				}
				break;
	
			case 4:
				if(bolAllModels){
					saveToR+="xha1<-log(datashap[,2]);xha=xha1[xha1 != -100]"+"\r\n";
					c.eval("xha1<-log(datashap[,2]);xha=xha1[xha1 != -100]");
					
					saveToR+="yha<-(1/(datashap[,1]+273.15))[xha1 != -100]"+"\r\n";
					c.eval("yha<-(1/(datashap[,1]+273.15))[xha1 != -100]");
					
					saveToR+="beta<-as.numeric(coef(lm(xha~yha))[2])"+"\r\n";
					c.eval("beta<-as.numeric(coef(lm(xha~yha))[2])");
					
					saveToR+="xli<-(datashap[,1]+273.15)"+"\r\n";
					c.eval("xli<-(datashap[,1]+273.15)");
					
					saveToR+="yli<-datashap[,2]"+"\r\n";
					c.eval("yli<-datashap[,2]");
					
					saveToR+="cof<-as.numeric(coef(lm(yli~xli)))"+"\r\n";
					c.eval("cof<-as.numeric(coef(lm(yli~xli)))");
					
					saveToR+="ini <- list(p=median(datashap[,2]),To=median(datashap[,1])+273.15,Ha = -beta* 1.987)"+"\r\n";
					c.eval("ini <- list(p=median(datashap[,2]),To=median(datashap[,1])+273.15,Ha = -beta* 1.987)");
				}else{
					saveToR+="ini <- list(p=" + str1 + ",To=" + str2 + ",Ha =" + str3 + ")"+"\r\n";
					c.eval("ini <- list(p=" + str1 + ",To=" + str2 + ",Ha =" + str3 + ")");
				}
				break;
			case 5:
				if(bolAllModels){
					saveToR+="xha1<-log(datashap[,2]);xha=xha1[xha1 != -100]"+"\r\n";
					c.eval("xha1<-log(datashap[,2]);xha=xha1[xha1 != -100]");
					
					saveToR+="yha<-(1/(datashap[,1]+273.15))[xha1 != -100]"+"\r\n";
					c.eval("yha<-(1/(datashap[,1]+273.15))[xha1 != -100]");
					
					saveToR+="beta<-as.numeric(coef(lm(xha~yha))[2])"+"\r\n";
					c.eval("beta<-as.numeric(coef(lm(xha~yha))[2])");
					
					saveToR+="xli<-(datashap[,1]+273.15)"+"\r\n";
					c.eval("xli<-(datashap[,1]+273.15)");
					
					saveToR+="yli<-datashap[,2]"+"\r\n";
					c.eval("yli<-datashap[,2]");
					
					saveToR+="cof<-as.numeric(coef(lm(yli~xli)))"+"\r\n";
					c.eval("cof<-as.numeric(coef(lm(yli~xli)))");
					
					saveToR+="ini <- list(p=median(datashap[,2]),To=median(datashap[,1])+273.15,Ha = -beta* 1.987,Tl =-cof[1]/cof[2] +2, Hl =-100000)"+"\r\n";
					c.eval("ini <- list(p=median(datashap[,2]),To=median(datashap[,1])+273.15,Ha = -beta* 1.987,Tl =-cof[1]/cof[2] +2, Hl =-100000)");
				}else{
					saveToR+="ini <- list(p=" + str1 + ",To=" + str2 + ",Ha =" + str4 + ",Tl =" + str3 + ", Hl =" + str5 + ")"+"\r\n";
					c.eval("ini <- list(p=" + str1 + ",To=" + str2 + ",Ha =" + str4 + ",Tl =" + str3 + ", Hl =" + str5 + ")");
				}
				break;
			case 6:
				if(bolAllModels){
					saveToR+="xha1<-log(datashap[,2]);xha=xha1[xha1 != -100]"+"\r\n";
					c.eval("xha1<-log(datashap[,2]);xha=xha1[xha1 != -100]");
					
					saveToR+="yha<-(1/(datashap[,1]+273.15))[xha1 != -100]"+"\r\n";
					c.eval("yha<-(1/(datashap[,1]+273.15))[xha1 != -100]");
					
					saveToR+="beta<-as.numeric(coef(lm(xha~yha))[2])"+"\r\n";
					c.eval("beta<-as.numeric(coef(lm(xha~yha))[2])");
					
					saveToR+="xli<-(datashap[,1]+273.15)"+"\r\n";
					c.eval("xli<-(datashap[,1]+273.15)");
					
					saveToR+="yli<-datashap[,2]"+"\r\n";
					c.eval("yli<-datashap[,2]");
					
					saveToR+="cof<-as.numeric(coef(lm(yli~xli)))"+"\r\n";
					c.eval("cof<-as.numeric(coef(lm(yli~xli)))");
					
					saveToR+="ini <- list(p=median(datashap[,2]),To=median(datashap[,1])+273.15,Ha = -beta* 1.987,Hh =200000, Th =datashap[(order(datashap[,2]))[nrow(datashap)],1]+273.15)"+"\r\n";
					c.eval("ini <- list(p=median(datashap[,2]),To=median(datashap[,1])+273.15,Ha = -beta* 1.987,Hh =200000, Th =datashap[(order(datashap[,2]))[nrow(datashap)],1]+273.15)");
				}else{
					saveToR+="ini <- list(p=" + str1 + ",To=" + str2 + ",Ha =" + str4  + ",Hh =" + str5 + ", Th =" + str3 + ")"+"\r\n";
					c.eval("ini <- list(p=" + str1 + ",To=" + str2 + ",Ha =" + str4  + ",Hh =" + str5 + ", Th =" + str3 + ")");
				}
				break;
			case 7:
				if(bolAllModels){
					saveToR+="xha1<-log(datashap[,2]);xha=xha1[xha1 != -100]"+"\r\n";
					c.eval("xha1<-log(datashap[,2]);xha=xha1[xha1 != -100]");
					
					saveToR+="yha<-(1/(datashap[,1]+273.15))[xha1 != -100]"+"\r\n";
					c.eval("yha<-(1/(datashap[,1]+273.15))[xha1 != -100]");
					
					saveToR+="beta<-as.numeric(coef(lm(xha~yha))[2])"+"\r\n";
					c.eval("beta<-as.numeric(coef(lm(xha~yha))[2])");
					
					saveToR+="xli<-(datashap[,1]+273.15)"+"\r\n";
					c.eval("xli<-(datashap[,1]+273.15)");
					
					saveToR+="yli<-datashap[,2]"+"\r\n";
					c.eval("yli<-datashap[,2]");
					
					saveToR+="cof<-as.numeric(coef(lm(yli~xli)))"+"\r\n";
					c.eval("cof<-as.numeric(coef(lm(yli~xli)))");
					
					saveToR+="ini <- list(To=median(datashap[,1])+273.15,Ha = -beta* 1.987)"+"\r\n";
					c.eval("ini <- list(To=median(datashap[,1])+273.15,Ha = -beta* 1.987)");
				}else{
					saveToR+="ini <- list(To=" + str1 + ",Ha =" + str2 + ")"+"\r\n";
					c.eval("ini <- list(To=" + str1 + ",Ha =" + str2 + ")");
					
					if(modify)
						c.eval("ini <- list(To=" + str2 + ",Ha =" + str3 + ")");
				}
				break;
			case 8:
				if(bolAllModels){
					saveToR+="xha1<-log(datashap[,2]);xha=xha1[xha1 != -100]"+"\r\n";
					c.eval("xha1<-log(datashap[,2]);xha=xha1[xha1 != -100]");
					
					saveToR+="yha<-(1/(datashap[,1]+273.15))[xha1 != -100]"+"\r\n";
					c.eval("yha<-(1/(datashap[,1]+273.15))[xha1 != -100]");
					
					saveToR+= "beta<-as.numeric(coef(lm(xha~yha))[2])"+"\r\n";
					c.eval("beta<-as.numeric(coef(lm(xha~yha))[2])");
					
					saveToR+= "xli<-(datashap[,1]+273.15)"+"\r\n";
					c.eval("xli<-(datashap[,1]+273.15)");
					
					saveToR+= "yli<-datashap[,2]"+"\r\n";
					c.eval("yli<-datashap[,2]");
					
					saveToR+= "cof<-as.numeric(coef(lm(yli~xli)))"+"\r\n";
					c.eval("cof<-as.numeric(coef(lm(yli~xli)))");
					
					saveToR+= "ini <- list(To=median(datashap[,1])+273.15,Ha = -beta* 1.987,Tl =-cof[1]/cof[2] +2, Hl =-100000 )"+"\r\n";
					c.eval("ini <- list(To=median(datashap[,1])+273.15,Ha = -beta* 1.987,Tl =-cof[1]/cof[2] +2, Hl =-100000 )");
				}else{
					saveToR+="ini <- list(To=" + str1 + ",Ha =" + str3 + ",Tl =" + str2 + ", Hl =" + str4 + ")"+"\r\n";
					c.eval("ini <- list(To=" + str1 + ",Ha =" + str3 + ",Tl =" + str2 + ", Hl =" + str4 + ")");
					
					if(modify)
						c.eval("ini <- list(To=" + str2 + ",Ha =" + str4 + ",Tl =" + str3 + ", Hl =" + str5 + ")");
				}
				break;
			case 9:
				if(bolAllModels){
					saveToR+="xha1<-log(datashap[,2]);xha=xha1[xha1 != -100]"+"\r\n";
					c.eval("xha1<-log(datashap[,2]);xha=xha1[xha1 != -100]");
					
					saveToR+="yha<-(1/(datashap[,1]+273.15))[xha1 != -100]"+"\r\n";
					c.eval("yha<-(1/(datashap[,1]+273.15))[xha1 != -100]");
					
					saveToR+= "beta<-as.numeric(coef(lm(xha~yha))[2])"+"\r\n";
					c.eval("beta<-as.numeric(coef(lm(xha~yha))[2])");
					
					saveToR+= "xli<-(datashap[,1]+273.15)"+"\r\n";
					c.eval("xli<-(datashap[,1]+273.15)");
					
					saveToR+= "yli<-datashap[,2]"+"\r\n";
					c.eval("yli<-datashap[,2]");
					
					saveToR+= "cof<-as.numeric(coef(lm(yli~xli)))"+"\r\n";
					c.eval("cof<-as.numeric(coef(lm(yli~xli)))");
					
					saveToR+= "ini <- list(To=median(datashap[,1])+273.15,Ha = -beta* 1.987,Hh =200000, Th =as.numeric(subset(datashap,max(datashap[,2])<=datashap[,2]))[1]+273.15)"+"\r\n";
					c.eval("ini <- list(To=median(datashap[,1])+273.15,Ha = -beta* 1.987,Hh =200000, Th =datashap[(order(datashap[,2]))[nrow(datashap)],1]+273.15)");
				}else{
					saveToR+="ini <- list(To=" + str1 + ",Ha =" + str3  + ",Hh =" + str4 + ", Th =" + str2 + ")"+"\r\n";
					c.eval("ini <- list(To=" + str1 + ",Ha =" + str3  + ",Hh =" + str4 + ", Th =" + str2 + ")");
					
					if(modify)
						c.eval("ini <- list(To=" + str2 + ",Ha =" + str4  + ",Hh =" + str5 + ", Th =" + str3 + ")");
				}
				break;
			case 10:
				if(bolAllModels){
					saveToR+="xha1<-log(datashap[,2]);xha=xha1[xha1 != -100]"+"\r\n";
					c.eval("xha1<-log(datashap[,2]);xha=xha1[xha1 != -100]");
					
					saveToR+="yha<-(1/(datashap[,1]+273.15))[xha1 != -100]"+"\r\n";
					c.eval("yha<-(1/(datashap[,1]+273.15))[xha1 != -100]");
					
					saveToR+= "beta<-as.numeric(coef(lm(xha~yha))[2])"+"\r\n";
					c.eval("beta<-as.numeric(coef(lm(xha~yha))[2])");
					
					saveToR+= "xli<-(datashap[,1]+273.15)"+"\r\n";
					c.eval("xli<-(datashap[,1]+273.15)");
					
					saveToR+= "yli<-datashap[,2]"+"\r\n";
					c.eval("yli<-datashap[,2]");
					
					saveToR+= "cof<-as.numeric(coef(lm(yli~xli)))"+"\r\n";
					c.eval("cof<-as.numeric(coef(lm(yli~xli)))");
					
					saveToR+= "ini <- list(p=median(datashap[,2]),Ha = -beta* 1.987,Tl =-cof[1]/cof[2] +2, Hl =-100000)"+"\r\n";
					c.eval("ini <- list(p=median(datashap[,2]),Ha = -beta* 1.987,Tl =-cof[1]/cof[2] +2, Hl =-100000)");
				}else{
					saveToR+= "ini <- list(p=" + str1 + ",Ha ="+ str3 + ",Tl ="+ str2 +", Hl ="+str4 +")"+"\r\n";
					c.eval("ini <- list(p=" + str1 + ",Ha ="+ str3 + ",Tl ="+ str2 +", Hl ="+str4 +")");
				}
				break;
			case 11:
				if(bolAllModels){
					saveToR+="yha<-1/(datashap[,1]+273.15)"+"\r\n";
					c.eval("yha<-1/(datashap[,1]+273.15)");
					
					saveToR+="xha<-log(datashap[,2])"+"\r\n";
					c.eval("xha<-log(datashap[,2])");
					
					saveToR+="beta<-as.numeric(coef(lm(xha~yha))[2])"+"\r\n";
					c.eval("beta<-as.numeric(coef(lm(xha~yha))[2])");
					
					saveToR+="xli<-(datashap[,1]+273.15)"+"\r\n";
					c.eval("xli<-(datashap[,1]+273.15)");
					
					saveToR+="yli<-datashap[,2]"+"\r\n";
					c.eval("yli<-datashap[,2]");
					
					saveToR+="cof<-as.numeric(coef(lm(yli~xli)))"+"\r\n";
					c.eval("cof<-as.numeric(coef(lm(yli~xli)))");
					
					saveToR+="ini <- list(p=median(datashap[,2]),Ha = -beta* 1.987,Tl =-cof[1]/cof[2] +2, Hl =-100000 ,Hh =200000, Th =as.numeric(subset(datashap,max(datashap[,2])<=datashap[,2]))[1]+273.15)"+"\r\n";
					c.eval("ini <- list(p=median(datashap[,2]),Ha = -beta* 1.987,Tl =-cof[1]/cof[2] +2, Hl =-100000 ,Hh =200000, Th =as.numeric(subset(datashap,max(datashap[,2])<=datashap[,2]))[1]+273.15)");
				}else{
					saveToR+="ini <- list(p=" + str1 + ",Ha =" + str4 + ",Tl =" + str2 + ", Hl =" + str5 + ",Hh =" + str6 + ", Th =" + str3 + ")"+"\r\n";
					c.eval("ini <- list(p=" + str1 + ",Ha =" + str4 + ",Tl =" + str2 + ", Hl =" + str5 + ",Hh =" + str6 + ", Th =" + str3 + ")");
				}
				break;
			case 12:
				if(bolAllModels){
					saveToR+="yha<-1/(datashap[,1]+273.15)"+"\r\n";
					c.eval("yha<-1/(datashap[,1]+273.15)");
					
					saveToR+="xha<-log(datashap[,2])"+"\r\n";
					c.eval("xha<-log(datashap[,2])");
					
					saveToR+="beta<-as.numeric(coef(lm(xha~yha))[2])"+"\r\n";
					c.eval("beta<-as.numeric(coef(lm(xha~yha))[2])");
					
					saveToR+="xli<-(datashap[,1]+273.15)"+"\r\n";
					c.eval("xli<-(datashap[,1]+273.15)");
					
					saveToR+="yli<-datashap[,2]"+"\r\n";
					c.eval("yli<-datashap[,2]");
					
					saveToR+="cof<-as.numeric(coef(lm(yli~xli)))"+"\r\n";
					c.eval("cof<-as.numeric(coef(lm(yli~xli)))");
					
					saveToR+="ini <- list(Ha = -beta* 1.987, Hl =-100000 , Tl =-cof[1]/cof[2] +2, Hh =200000, Th =300)"+"\r\n";
					c.eval("ini <- list(Ha = -beta* 1.987, Hl =-100000 , Tl =-cof[1]/cof[2] +2, Hh =200000, Th =300)");
				}else{
					saveToR+="ini <- list(Hh=" + str5 + ",Tl=" + str1 + ",Ha =" + str3  + ",Hl =" + str4 + ", Th =" + str2 + ")"+"\r\n";
					c.eval("ini <- list(Hh=" + str5 + ",Tl=" + str1 + ",Ha =" + str3  + ",Hl =" + str4 + ", Th =" + str2 + ")");
				}
				break;
			case 13:
				if(bolAllModels){
					saveToR+="yha<-1/(datashap[,1]+273.15)"+"\r\n";
					c.eval("yha<-1/(datashap[,1]+273.15)");
					
					saveToR+="xha<-log(datashap[,2])"+"\r\n";
					c.eval("xha<-log(datashap[,2])");
					
					saveToR+="beta<-as.numeric(coef(lm(xha~yha))[2])"+"\r\n";
					c.eval("beta<-as.numeric(coef(lm(xha~yha))[2])");
					
					saveToR+="xli<-(datashap[,1]+273.15)"+"\r\n";
					c.eval("xli<-(datashap[,1]+273.15)");
					
					saveToR+="yli<-datashap[,2]"+"\r\n";
					c.eval("yli<-datashap[,2]");
					
					saveToR+="cof<-as.numeric(coef(lm(yli~xli)))"+"\r\n";
					c.eval("cof<-as.numeric(coef(lm(yli~xli)))");
					
					saveToR+="ini <- list(p=median(datashap[,2]),Ha = -beta* 1.987, Hl =-100000 ,Tl =-cof[1]/cof[2] +2)"+"\r\n";
					c.eval("ini <- list(p=median(datashap[,2]),Ha = -beta* 1.987, Hl =-100000 ,Tl =-cof[1]/cof[2] +2)");
				}else{
					saveToR+= "ini <- list(p=" + str1 + ",Ha ="+ str3 + ",Tl ="+ str2 +", Hl ="+str4 +")"+"\r\n";
					c.eval("ini <- list(p=" + str1 + ",Ha ="+ str3 + ",Tl ="+ str2 +", Hl ="+str4 +")");
				}
				break;
			case 14:
				if(bolAllModels){
					saveToR+="yha<-1/(datashap[,1]+273.15)"+"\r\n";
					c.eval("yha<-1/(datashap[,1]+273.15)");
					
					saveToR+="xha<-log(datashap[,2])"+"\r\n";
					c.eval("xha<-log(datashap[,2])");
					
					saveToR+="beta<-as.numeric(coef(lm(xha~yha))[2])"+"\r\n";
					c.eval("beta<-as.numeric(coef(lm(xha~yha))[2])");
					
					saveToR+="xli<-(datashap[,1]+273.15)"+"\r\n";
					c.eval("xli<-(datashap[,1]+273.15)");
					
					saveToR+="yli<-datashap[,2]"+"\r\n";
					c.eval("yli<-datashap[,2]");
					
					saveToR+="cof<-as.numeric(coef(lm(yli~xli)))"+"\r\n";
					c.eval("cof<-as.numeric(coef(lm(yli~xli)))");
					
					saveToR+="ini <- list(p=median(datashap[,2]),Ha = -beta* 1.987, Hh =200000, Th =300)"+"\r\n";
					c.eval("ini <- list(p=median(datashap[,2]),Ha = -beta* 1.987, Hh =200000, Th =300)");
				}else{
					saveToR+= "ini <- list(p=" + str1 + ",Ha ="+ str3 + ",Th ="+ str2 +", Hh ="+str4 +")"+"\r\n";
					c.eval("ini <- list(p=" + str1 + ",Ha ="+ str3 + ",Th ="+ str2 +", Hh ="+str4 +")");
				}
				break;
			case 15:/**Deva 1**/
				if(bolAllModels){
					saveToR+= "xli<-datashap[,1][2:9]"+"\r\n";
					c.eval("xli<-datashap[,1][2:9]");
					
					saveToR+= "yli<-datashap[,2][2:9]"+"\r\n";
					c.eval("yli<-datashap[,2][2:9]");
					
					saveToR+= "beta<-as.numeric(coef(lm(yli~xli))[2])"+"\r\n";
					c.eval("beta<-as.numeric(coef(lm(yli~xli))[2])");
					
					saveToR+= "ini <- list(Tmin = min(datashap[,1]), b = beta)"+"\r\n";
					c.eval("ini <- list(Tmin = min(datashap[,1]), b = beta)");
				}else{
					saveToR+="ini <- list(Tmin=" + str1 + ",b=" + str2 + ")"+"\r\n";
					c.eval("ini <- list(Tmin=" + str1 + ",b=" + str2 + ")");
				}
				break;
			case 16:/**Deva 2**/
				if(bolAllModels){
					saveToR+= "ini <- list(b1=0.0001630595,b2=-74.79061,b3=-38.76691,b4=-3.6201e-05,b5=-0.3245052 )"+"\r\n";
					c.eval("ini <- list(b1=0.0001630595,b2=-74.79061,b3=-38.76691,b4=-3.6201e-05,b5=-0.3245052)");
				}else{
					saveToR+="ini <- list(b1=" + str1 + ",b2=" + str2 + ",b4 =" + str4  + ",b5 =" + str5 + ", b3 =" + str3 + ")"+"\r\n";
					c.eval("ini <- list(b1=" + str1 + ",b2=" + str2 + ",b4 =" + str4  + ",b5 =" + str5 + ", b3 =" + str3 + ")");
				}
				break;
			case 17:/**Logan 1**/
				if(bolAllModels){
					saveToR+= "ini <- list(Y=min(datashap[,2]),Tmax = max(datashap[,1]), p = median(datashap[,2]), v=3.5)"+"\r\n";
					c.eval("ini <- list(Y=min(datashap[,2]),Tmax = max(datashap[,1]), p = median(datashap[,2]), v=3.5)");
				}else{
					saveToR+="ini <- list(Y=" + str1 + ",p=" + str3 + ",v =" + str4  + ", Tmax =" + str2 + ")"+"\r\n";
					c.eval("ini <- list(Y=" + str1 + ",p=" + str3 + ",v =" + str4  + ", Tmax =" + str2 + ")");
				}
				break;
			case 18:/**Logan 2**/
				if(bolAllModels){
					saveToR+= "ini <- list(alfa=max(datashap[,2])+.2*max(datashap[,2]),k=100,Tmax = max(datashap[,1]), p = median(datashap[,2]), v=0.05)"+"\r\n";
					c.eval("ini <- list(alfa=max(datashap[,2])+.2*max(datashap[,2]),k=100,Tmax = max(datashap[,1]), p = median(datashap[,2]), v=0.05)");
				}else{
					saveToR+="ini <- list(alfa=" + str1 + ",k=" + str2 + ",Tmax =" + str3  + ",v =" + str5 + ", p =" + str4 + ")"+"\r\n";
					c.eval("ini <- list(alfa=" + str1 + ",k=" + str2 + ",Tmax =" + str3  + ",v =" + str5 + ", p =" + str4 + ")");
				}
				break;
			case 19:/**Briere 1**/
				if(bolAllModels){
					saveToR+= "ini <- list(aa=0.00003,To=min(datashap[,1])-4,Tmax =max(datashap[,1])+90)"+"\r\n";
					c.eval("ini <- list(aa=0.00003,To=min(datashap[,1])-4,Tmax =max(datashap[,1])+90)");
				}else{
					saveToR+="ini <- list(aa=" + str1 + ",To=" + str2 + ",Tmax =" + str3 + ")"+"\r\n";
					c.eval("ini <- list(aa=" + str1 + ",To=" + str2 + ",Tmax =" + str3 + ")");
				}
				break;
			case 20:/**Briere 2**/
				if(bolAllModels){
					saveToR+= "ini <- list(aa=0.00001,To=min(datashap[,1])-4,Tmax =max(datashap[,1])+90,d=0.5)"+"\r\n";
					c.eval("ini <- list(aa=0.00001,To=min(datashap[,1])-4,Tmax =max(datashap[,1])+90,d=0.5)");
				}else{
					saveToR+="ini <- list(aa=" + str1 + ",To=" + str2 + ",d =" + str4  + ", Tmax =" + str3 + ")"+"\r\n";
					c.eval("ini <- list(aa=" + str1 + ",To=" + str2 + ",d =" + str4  + ", Tmax =" + str3 + ")");
				}
				break;
			case 21:/**Stinner 1**/
				if(bolAllModels){
					saveToR+= "xstinner<-c(min(datashap[,1]),max(datashap[,1]))"+"\r\n";
					c.eval("xstinner<-c(min(datashap[,1]),max(datashap[,1]))");
					
					saveToR+= "ystinner<-c(log(as.numeric(subset(datashap,max(datashap[,2])<=datashap[,2]))[1]/min(datashap[,2])-1),log(as.numeric(subset(datashap,max(datashap[,2])<=datashap[,2]))[1]/max(datashap[,2])-1))"+"\r\n";
					c.eval("ystinner<-c(log(as.numeric(subset(datashap,max(datashap[,2])<=datashap[,2]))[1]/min(datashap[,2])-1),log(as.numeric(subset(datashap,max(datashap[,2])<=datashap[,2]))[1]/max(datashap[,2])-1))");
					
					saveToR+= "ks<-as.numeric(coef(lm(ystinner~xstinner)))"+"\r\n";
					c.eval("ks<-as.numeric(coef(lm(ystinner~xstinner)))");
					
					saveToR+= "ini <- list(Rmax = max(datashap[,2]),Topc=as.numeric(subset(datashap,max(datashap[,2])<=datashap[,2]))[1],k1 = ks[1], k2 = ks[2])"+"\r\n";
					c.eval("ini <- list(Rmax = max(datashap[,2]),Topc=as.numeric(subset(datashap,max(datashap[,2])<=datashap[,2]))[1],k1 = ks[1], k2 = ks[2])");
				}else{
					saveToR+="ini <- list(Rmax=" + str1 + ",Topc=" + str2 + ",k2 =" + str4  + ", k1 =" + str3 + ")"+"\r\n";
					c.eval("ini <- list(Rmax=" + str1 + ",Topc=" + str2 + ",k2 =" + str4  + ", k1 =" + str3 + ")");
				}
				break;
			case 22:/**Hilbert y Logan**/
				if(bolAllModels){
					saveToR+= "ini <- list(d=2*(max(datashap[,1])-min(datashap[,1])),Y=3*max(datashap[,2]),Tmax = max(datashap[,1]), v=0.01)"+"\r\n";
					c.eval("ini <- list(d=2*(max(datashap[,1])-min(datashap[,1])),Y=3*max(datashap[,2]),Tmax = max(datashap[,1]), v=0.01)");
				}else{
					saveToR+="ini <- list(Y=" + str2 + ",d=" + str1 + ",v =" + str4  + ", Tmax =" + str3 + ")"+"\r\n";
					c.eval("ini <- list(Y=" + str2 + ",d=" + str1 + ",v =" + str4  + ", Tmax =" + str3 + ")");
				}
				break;
			case 23:/**Lactin**/
				if(bolAllModels){
					saveToR+= "ini <- list(Tl = max(datashap[,1]), p = min(datashap[,2]), dt= 0.8,L=-1)"+"\n";
					c.eval("ini <- list(Tl = max(datashap[,1]), p = min(datashap[,2]), dt= 0.8,L=-1)");
				}else{
					saveToR+="ini <- list(Tl=" + str1 + ",p=" + str2 + ",L =" + str4  + ", dt =" + str3 + ")"+"\r\n";
					c.eval("ini <- list(Tl=" + str1 + ",p=" + str2 + ",L =" + str4  + ", dt =" + str3 + ")");
				}
				break;
			case 24:/** linear **/
				if(bolAllModels){
					saveToR+="xli1<-datashap[,1]"+"\r\n";
					c.eval("xli1<-datashap[,1]");
					
					saveToR+="yli1<-datashap[,2]"+"\r\n";
					c.eval("yli1<-datashap[,2]");
					
					saveToR+="cof<-as.numeric(coef(lm(yli1~xli1)))"+"\r\n";
					c.eval("cof<-as.numeric(coef(lm(yli1~xli1)))");
					
					saveToR+="ini <- list(Inter=cof[1],Slop=cof[2])"+"\r\n";
					c.eval("ini <- list(Inter=cof[1],Slop=cof[2])");
				}else{
					saveToR+="ini <- list(Inter=" + str1 + ",Slop =" + str2 + ")"+"\r\n";
					c.eval("ini <- list(Inter=" + str1 + ",Slop =" + str2 + ")");
				}
				break;
			case 25:/** exponential simple **/
				if(bolAllModels){
					saveToR+="ini=c(b1=0.019,b2=0.086)"+"\r\n";
					c.eval("ini=c(b1=0.019,b2=0.086)");
				}else{
					saveToR+="ini <- list(b1=" + str1 + ",b2 =" + str2 + ")"+"\r\n";
					c.eval("ini <- list(b1=" + str1 + ",b2 =" + str2 + ")");
				}
				break;
			case 26:/** Tb Model (Logan) **/
				if(bolAllModels){
					saveToR+="ini=c(sy=0.32,b=0.0924,Tb=max(datashap[,1]),DTb=0.084)"+"\r\n";
					c.eval("ini=c(sy=0.32,b=0.0924,Tb=max(datashap[,1]),DTb=0.084)");
				}else{
					saveToR+="ini <- list(sy=" + str1 + ",b=" + str2 + ",Tb =" + str3  + ", DTb =" + str4 + ")"+"\r\n";
					c.eval("ini <- list(sy=" + str1 + ",b=" + str2 + ",Tb =" + str3  + ", DTb =" + str5 + ")");
				}
				break;
			case 27:/** Exponential Model (Logan) **/
				if(bolAllModels){
					saveToR+="ini=c(sy=0.2567,b=0.086,Tb=min(datashap[,1]))"+"\r\n";
					c.eval("ini=c(sy=0.2567,b=0.086,Tb=min(datashap[,1]))");
				}else{
					saveToR+="ini <- list(sy=" + str1 + ",b=" + str2 + ",Tb =" + str3 + ")"+"\r\n";
					c.eval("ini <- list(sy=" + str1 + ",b=" + str2 + ",Tb =" + str3 + ")");
				}
				break;
			case 28:/** Exponential Tb (Logan) **/
				if(bolAllModels){
					saveToR+="ini=c(b=0.0087,Tmin=min(datashap[,1]))"+"\r\n";
					c.eval("ini=c(b=0.0087,Tmin=min(datashap[,1]))");
				}else{
					saveToR+="ini <- list(b=" + str1 + ",Tmin =" + str2 + ")"+"\r\n";
					c.eval("ini <- list(b=" + str1 + ",Tmin =" + str2 + ")");
				}
				break;
			case 29:/** Square root model of Ratkowsky **/
				if(bolAllModels){
					saveToR+="ini=c(b=0.000008487,Tb=-150.881)"+"\r\n";
					c.eval("ini=c(b=0.000008487,Tb=-150.881)");
				}else{
					saveToR+="ini <- list(b=" + str1 + ",Tb =" + str2 + ")"+"\r\n";
					c.eval("ini <- list(b=" + str1 + ",Tb =" + str2 + ")");
				}
				break;
			case 30:/** Davidson **/
				if(bolAllModels){
					saveToR+="ini=c(k=0.5,a=1,b=-0.05)"+"\r\n";
					c.eval("ini=c(k=0.5,a=1,b=-0.05)");
				}else{
					saveToR+="ini <- list(k=" + str1 + ",a=" + str2 + ",b =" + str3+ ")"+"\r\n";
					c.eval("ini <- list(k=" + str1 + ",a=" + str2 + ",b =" + str3 + ")");
				}
				break;
			case 31:/** Pradham **/
				if(bolAllModels){
					saveToR+="ini=c(R=0.3,Tm=mean(datashap[,1]),To=(-1)*min(datashap[,1]))"+"\r\n";
					c.eval("ini=c(R=0.3,Tm=mean(datashap[,1]),To=(-1)*min(datashap[,1]))");
				}else{
					saveToR+="ini <- list(R=" + str1 + ",Tm=" + str2 + ",To =" + str3+ ")"+"\r\n";
					c.eval("ini <- list(R=" + str1 + ",Tm=" + str2 + ",To =" + str3 + ")");
				}
				break;
			case 32:/** Angilletta Jr. **/
				if(bolAllModels){
					saveToR+="ini=c(a=0.3,b=mean(datashap[,1]),c=(-1)*min(datashap[,1]),d=1)"+"\r\n";
					c.eval("ini=c(a=0.3,b=mean(datashap[,1]),c=(-1)*min(datashap[,1]),d=1)");
				}else{
					saveToR+="ini <- list(a=" + str1 + ",b=" + str2 + ",c =" + str3+", d="+str4+ ")"+"\r\n";
					c.eval("ini <- list(a=" + str1 + ",b=" + str2 + ",c =" + str3+", d="+str4 + ")");
				}
				break;
			case 33:/** Stinner 2 **/
				if(bolAllModels){
					saveToR+="xstinner<-c(min(datashap[,1]),max(datashap[,1]))"+"\r\n";
					c.eval("xstinner<-c(min(datashap[,1]),max(datashap[,1]))");
					
					saveToR+="ystinner<-c(log(as.numeric(subset(datashap,max(datashap[,2])<=datashap[,2]))[1]/min(datashap[,2])-1),log(as.numeric(subset(datashap,max(datashap[,2])<=datashap[,2]))[1]/max(datashap[,2])-1))"+"\r\n";
					c.eval("ystinner<-c(log(as.numeric(subset(datashap,max(datashap[,2])<=datashap[,2]))[1]/min(datashap[,2])-1),log(as.numeric(subset(datashap,max(datashap[,2])<=datashap[,2]))[1]/max(datashap[,2])-1))");
					
					saveToR+="ks<-as.numeric(coef(lm(ystinner~xstinner)))"+"\r\n";
					c.eval("ks<-as.numeric(coef(lm(ystinner~xstinner)))");
					
					saveToR+="ini=c(Rmax=max(datashap[,2]),k1=ks[1],k2=ks[2],Topc=as.numeric(subset(datashap,max(datashap[,2])<=datashap[,2]))[1])"+"\r\n";
					c.eval("ini=c(Rmax=max(datashap[,2]),k1=ks[1],k2=ks[2],Topc=as.numeric(subset(datashap,max(datashap[,2])<=datashap[,2]))[1])");
				}else{
					saveToR+="ini <- list(Rmax=" + str1 + ",k1=" + str2 + ",k2 =" + str3+", Topc="+str4+ ")"+"\r\n";
					c.eval("ini <- list(Rmax=" + str1 + ",k1=" + str2 + ",k2 =" + str3+", Topc="+str4 + ")");
				}
				break;
			case 34:/** Hilbert **/
				if(bolAllModels){
					saveToR+="ini=c(Tb=0.01,Tmax=max(datashap[,1]),d=2*(max(datashap[,1])-min(datashap[,1])),Y=3*max(datashap[,2]),v=0.01)"+"\r\n";
					c.eval("ini=c(Tb=0.01,Tmax=max(datashap[,1]),d=2*(max(datashap[,1])-min(datashap[,1])),Y=3*max(datashap[,2]),v=0.01)");
				}else{
					saveToR+="ini <- list(Tb=" + str1 + ",Tmax=" + str2 + ",d =" + str3+", Y="+str4+", v="+str5+ ")"+"\r\n";
					c.eval("ini <- list(Tb=" + str1 + ",Tmax=" + str2 + ",d =" + str3+", Y="+str4+", v="+str5 + ")");
				}
				break;
			case 35:/** Lactin 2 **/
				if(bolAllModels){
					saveToR+="ini=c(Tl=max(datashap[,1])-23, p=min(datashap[,2]), dt=-40)"+"\r\n";
					c.eval("ini=c(Tl=max(datashap[,1])-23, p=min(datashap[,2]), dt=-40)");
				}else{
					saveToR+="ini <- list(Tl=" + str1 + ",p=" + str2 + ",dt =" + str3+ ")"+"\r\n";
					c.eval("ini <- list(Tl=" + str1 + ",p=" + str2 + ",dt =" + str3 + ")");
				}
				break;
			case 36:/** Anlytis-1 **/
				if(bolAllModels){
					saveToR+="ini=c(P=1.2,Tmax=max(datashap[,1]), Tmin=min(datashap[,1]),n=1,m=1)"+"\r\n";
					c.eval("ini=c(P=1.2,Tmax=max(datashap[,1]), Tmin=min(datashap[,1]),n=1,m=1)");
				}else{
					saveToR+="ini <- list(P=" + str1 + ",Tmax=" + str2 + ",Tmin =" + str3+", n="+str4+", m="+str5+ ")"+"\r\n";
					c.eval("ini <- list(P=" + str1 + ",Tmax=" + str2 + ",Tmin =" + str3+", n="+str4+", m="+str5 + ")");
				}
				break;
			case 37:/** Anlytis-2 **/
				if(bolAllModels){
					saveToR+="ini=c(P=1.2,Tmax=max(datashap[,1]), Tmin=min(datashap[,1]),n=1,m=1)"+"\r\n";
					c.eval("ini=c(P=1.2,Tmax=max(datashap[,1]), Tmin=min(datashap[,1]),n=1,m=1)");
				}else{
					saveToR+="ini <- list(P=" + str1 + ",Tmax=" + str2 + ",Tmin =" + str3+", n="+str4+", m="+str5+ ")"+"\r\n";
					c.eval("ini <- list(P=" + str1 + ",Tmax=" + str2 + ",Tmin =" + str3+", n="+str4+", m="+str5 + ")");
				}
				break;
			case 38:/** Anlytis-3 **/
				if(bolAllModels){
					saveToR+="ini=c(a=0.00124,Tmax=max(datashap[,1])+46, Tmin=min(datashap[,1])-9.16696,n=1.582,m=0.9658)"+"\r\n";
					c.eval("ini=c(a=0.00124,Tmax=max(datashap[,1])+46, Tmin=min(datashap[,1])-9.16696,n=1.582,m=0.9658)");
				}else{
					saveToR+="ini <- list(a=" + str1 + ",Tmax=" + str2 + ",Tmin =" + str3+", n="+str4+", m="+str5+ ")"+"\r\n";
					c.eval("ini <- list(a=" + str1 + ",Tmax=" + str2 + ",Tmin =" + str3+", n="+str4+", m="+str5 + ")");
				}
				break;
			case 39:/** Allahyari **/
				if(bolAllModels){
					saveToR+="ini=c(P=1.2,Tmax=max(datashap[,1]), Tmin=min(datashap[,1])-9,n=1,m=0.5)"+"\r\n";
					c.eval("ini=c(P=1.2,Tmax=max(datashap[,1]), Tmin=min(datashap[,1])-9,n=1,m=0.5)");
				}else{
					saveToR+="ini <- list(P=" + str1 + ",Tmax=" + str2 + ",Tmin =" + str3+", n="+str4+", m="+str5+ ")"+"\r\n";
					c.eval("ini <- list(P=" + str1 + ",Tmax=" + str2 + ",Tmin =" + str3+", n="+str4+", m="+str5 + ")");
				}
				break;
			case 40:/** Briere 3 **/
				if(bolAllModels){
					saveToR+="ini=c(aa=0.0078,To=min(datashap[,1])-2, Tmax=max(datashap[,1])+1)"+"\r\n";
					c.eval("ini=c(aa=0.0078,To=min(datashap[,1])-2, Tmax=max(datashap[,1])+1)");
				}else{
					saveToR+="ini <- list(aa=" + str1 + ",To=" + str2 + ",Tmax =" + str3+ ")"+"\r\n";
					c.eval("ini <- list(aa=" + str1 + ",To=" + str2 + ",Tmax =" + str3 + ")");
				}
				break;
			case 41:/** Briere 4 **/
				if(bolAllModels){
					saveToR+="ini=c(aa=0.0083,To=min(datashap[,1])-1, Tmax=max(datashap[,1])+1,n=1.9)"+"\r\n";
					c.eval("ini=c(aa=0.0083,To=min(datashap[,1])-1, Tmax=max(datashap[,1])+1,n=1.9)");
				}else{
					saveToR+="ini <- list(aa=" + str1 + ",To=" + str2 + ",Tmax =" + str3+", n="+str4+ ")"+"\r\n";
					c.eval("ini <- list(aa=" + str1 + ",To=" + str2 + ",Tmax =" + str3+", n="+str4 + ")");
				}
				break;
			case 42:/** Kontodimas-1 **/
				if(bolAllModels){
					saveToR+="ini=c(aa=0.00013,Tmin=min(datashap[,1])-4, Tmax=max(datashap[,1])+1)"+"\r\n";
					c.eval("ini=c(aa=0.00013,Tmin=min(datashap[,1])-4, Tmax=max(datashap[,1])+1)");
				}else{
					saveToR+="ini <- list(aa=" + str1 + ",Tmin=" + str2 + ",Tmax =" + str3+ ")"+"\r\n";
					c.eval("ini <- list(aa=" + str1 + ",Tmin=" + str2 + ",Tmax =" + str3 + ")");
				}
				break;
			case 43:/** Kontodimas-2 **/
				if(bolAllModels){
					saveToR+="ini=c(Dmin=4,Topt=mean(datashap[,1])+8,K=10,lmda=0.06)"+"\r\n";
					c.eval("ini=c(Dmin=4,Topt=mean(datashap[,1])+8,K=10,lmda=0.06)");
				}else{
					saveToR+="ini <- list(Dmin=" + str1 + ",Topt=" + str2 + ",K =" + str3+", lmda="+str4+ ")"+"\r\n";
					c.eval("ini <- list(Dmin=" + str1 + ",Topt=" + str2 + ",K =" + str3+", lmda="+str4 + ")");
				}
				break;
			case 44:/** Kontodimas-3 **/
				if(bolAllModels){
					saveToR+="ini=c(a1=0.2,b1=35,c1=1,d1=10,f1=1,g1=10)"+"\r\n";
					c.eval("ini=c(a1=0.2,b1=35,c1=1,d1=10,f1=1,g1=10)");
				}else{
					saveToR+="ini <- list(a1=" + str1 + ",b1 =" + str2 + ",c1 =" + str3 + ", d1 =" + str4 + ",f1 =" + str5 + ", g1 =" + str6 + ")"+"\r\n";
					c.eval("ini <- list(a1=" + str1 + ",b1 =" + str2 + ",c1 =" + str3 + ", d1 =" + str4 + ",f1 =" + str5 + ", g1 =" + str6 + ")");
				}
				break;
			case 45:/** Ratkowsky 2 **/
				if(bolAllModels){
					saveToR+="ini=c(aa=0.00065,Tmin=min(datashap[,1])-1,Tmax=max(datashap[,1])+38,b=0.09)"+"\r\n";
					c.eval("ini=c(aa=0.00065,Tmin=min(datashap[,1])-1,Tmax=max(datashap[,1])+38,b=0.09)");
				}else{
					saveToR+="ini <- list(aa=" + str1 + ",Tmin=" + str2 + ",Tmax =" + str3+", b="+str4+ ")"+"\r\n";
					c.eval("ini <- list(aa=" + str1 + ",Tmin=" + str2 + ",Tmax =" + str3+", b="+str4 + ")");
				}
				break;
			case 46:/** Janish-1 **/
				if(bolAllModels){
					saveToR+="ini=c(Dmin=2.8,Topt=mean(datashap[,1]),K=0.17)"+"\r\n";
					c.eval("ini=c(Dmin=2.8,Topt=mean(datashap[,1]),K=0.17)");
				}else{
					saveToR+="ini <- list(Dmin=" + str1 + ",Topt=" + str2 + ",K =" + str3+ ")"+"\r\n";
					c.eval("ini <- list(Dmin=" + str1 + ",Topt=" + str2 + ",K =" + str3 + ")");
				}
				break;
			case 47:/** Janish-2 **/
				if(bolAllModels){
					saveToR+="ini=c(c=0.2151377,a=4.97447,b=1.077405,Tm=max(datashap[,1]))"+"\r\n";
					c.eval("ini=c(c=0.2151377,a=4.97447,b=1.077405,Tm=max(datashap[,1]))");
				}else{
					saveToR+="ini <- list(c=" + str1 + ",a=" + str2 + ",b =" + str3+", Tm="+str4+ ")"+"\r\n";
					c.eval("ini <- list(c=" + str1 + ",a=" + str2 + ",b =" + str3+", Tm="+str4 + ")");
				}
				break;
			case 48:/** Tanigoshi **/
				if(bolAllModels){
					saveToR+="xTan<-datashap[,1]"+"\r\n";
					c.eval("xTan<-datashap[,1]");
					
					saveToR+="yTan<-datashap[,2]"+"\r\n";
					c.eval("yTan<-datashap[,2]");
					
					saveToR+="ks<-as.numeric(coef(lm(yTan~xTan+I(xTan^2)+I(xTan^3))))"+"\r\n";
					c.eval("ks<-as.numeric(coef(lm(yTan~xTan+I(xTan^2)+I(xTan^3))))");
					
					saveToR+="ini=c(a0=ks[1],a1=ks[2],a2=ks[3],a3=ks[4])"+"\r\n";
					c.eval("ini=c(a0=ks[1],a1=ks[2],a2=ks[3],a3=ks[4])");
				}else{
					saveToR+="ini <- list(a0=" + str1 + ",a1=" + str2 + ",a2 =" + str3+", a3="+str4+ ")"+"\r\n";
					c.eval("ini <- list(a0=" + str1 + ",a1=" + str2 + ",a2 =" + str3+", a3="+str4 + ")");
				}
				break;
			case 49:/** Wang-Lan-Ding **/
				if(bolAllModels){
					saveToR+="ini=c(k=0.4,a=0.46,b=1,c=1,Tmin=min(datashap[,1]),Tmax=max(datashap[,1]),r=0.53)"+"\r\n";
					c.eval("ini=c(k=0.4,a=0.46,b=1,c=1,Tmin=min(datashap[,1]),Tmax=max(datashap[,1]),r=0.53)");
				}else{
					saveToR+="ini <- list(k=" + str1 + ",a=" + str2 + ",b =" + str3 + ",c =" + str4 + ", Tmin =" + str5 + ",Tmax =" + str6 + ", r =" + str7 + ")"+"\r\n";
					c.eval("ini <- list(k=" + str1 + ",a=" + str2 + ",b =" + str3 + ",c =" + str5 + ", Tmin =" + str5 + ",Tmax =" + str6 + ", r =" + str7 + ")");
				}
				break;
			case 50:/** Stinner-3 **/
				if(bolAllModels){
					saveToR+="ini=c(c1=11.7,k1=5.783,k2=-0.0516)"+"\r\n";
					c.eval("ini=c(c1=11.7,k1=5.783,k2=-0.0516)");
				}else{
					saveToR+="ini <- list(c1=" + str1 + ",k1=" + str2 + ",k2 =" + str3+ ")"+"\r\n";
					c.eval("ini <- list(c1=" + str1 + ",k1=" + str2 + ",k2 =" + str3 + ")");
				}
				break;
			case 51:/** Stinner-4 **/
				if(bolAllModels){
					saveToR+="ini=c(c1=-0.962,c2=0.2545,k1=-3.91,k2=0.54,To=7.77)"+"\r\n";
					c.eval("ini=c(c1=-0.962,c2=0.2545,k1=-3.91,k2=0.54,To=7.77)");
				}else{
					saveToR+="ini <- list(c1=" + str1 + ",c2=" + str2 + ",k1 =" + str3+", k2="+str4+", To="+str5+ ")"+"\r\n";
					c.eval("ini <- list(c1=" + str1 + ",c2=" + str2 + ",k1 =" + str3+", k2="+str4+", To="+str5 + ")");
				}
				break;
			case 52:/** Logan-3 **/
				if(bolAllModels){
					saveToR+="ini=c(sy=0.08145,b=0.0627,Tmin=2.3183,Tmax=29.594,DTb=0.1448)"+"\r\n";
					c.eval("ini=c(sy=0.08145,b=0.0627,Tmin=2.3183,Tmax=29.594,DTb=0.1448)");
				}else{
					saveToR+="ini <- list(sy=" + str1 + ",b=" + str2 + ",Tmin =" + str3+", Tmax="+str4+", DTb="+str5+ ")"+"\r\n";
					c.eval("ini <- list(sy=" + str1 + ",b=" + str2 + ",Tmin =" + str3+", Tmax="+str4+", DTb="+str5 + ")");
				}
				break;
			case 53:/** Logan-4 **/
				if(bolAllModels){
					saveToR+="ini=c(alph=23.269,k=8778,b=0.1505,Tmin=-11.12,Tmax=52.85,Dt=3.66)"+"\r\n";
					c.eval("ini=c(alph=23.269,k=8778,b=0.1505,Tmin=-11.12,Tmax=52.85,Dt=3.66)");
				}else{
					saveToR+="ini <- list(alph=" + str1 + ",k =" + str2 + ",b =" + str3 + ", Tmin =" + str4 + ",Tmax =" + str5 + ", Dt =" + str6 + ")"+"\r\n";
					c.eval("ini <- list(alph=" + str1 + ",k =" + str2 + ",b =" + str3 + ", Tmin =" + str4 + ",Tmax =" + str5 + ", Dt =" + str6 + ")");
				}
				break;
			case 54:/** Logan-5 **/
				if(bolAllModels){
					saveToR+="ini=c(alph=0.14,k=30,b=0.1314,Tmax=35,Dt=0.489)"+"\r\n";
					c.eval("ini=c(alph=0.14,k=30,b=0.1314,Tmax=35,Dt=0.489)");
				}else{
					saveToR+="ini <- list(alph=" + str1 + ",k=" + str2 + ",b =" + str3+", Tmax="+str4+", Dt="+str5+ ")"+"\r\n";
					c.eval("ini <- list(alph=" + str1 + ",k=" + str2 + ",b =" + str3+", Tmax="+str4+", Dt="+str5 + ")");
				}
				break;
			case 55:/** Hilber y logan 2 **/
				if(bolAllModels){
					saveToR+="ini=c(trid=10.145,D=12457,Tmax=24.698,Dt=1.3818)"+"\r\n";
					c.eval("ini=c(trid=10.145,D=12457,Tmax=24.698,Dt=1.3818)");
				}else{
					saveToR+="ini <- list(trid=" + str1 + ",D=" + str2 + ",Tmax =" + str3+", Dt="+str4+ ")"+"\r\n";
					c.eval("ini <- list(trid=" + str1 + ",D=" + str2 + ",Tmax =" + str3+", Dt="+str4 + ")");
				}
				break;
			case 56:/** Hilber y logan 3 **/
				if(bolAllModels){
					saveToR+="ini=c(trid=32.237,Tmax=31.506,Tmin=3.04,D=78305,Dt=0.356,Smin=0.07577)"+"\r\n";
					c.eval("ini=c(trid=32.237,Tmax=31.506,Tmin=3.04,D=78305,Dt=0.356,Smin=0.07577)");
				}else{
					saveToR+="ini <- list(trid=" + str1 + ",Tmax =" + str2 + ",Tmin =" + str3 + ", D =" + str4 + ",Dt =" + str5 + ", Smin =" + str6 + ")"+"\r\n";
					c.eval("ini <- list(trid=" + str1 + ",Tmax =" + str2 + ",Tmin =" + str3 + ", D =" + str4 + ",Dt =" + str5 + ", Smin =" + str6 + ")");
				}
				break;
			case 57:/** Taylor **/
				if(bolAllModels){
					saveToR+="ini=c(rm=0.339,Topt=21.81,Troh=7.897,Smin=0.0657)"+"\r\n";
					c.eval("ini=c(rm=0.339,Topt=21.81,Troh=7.897,Smin=0.0657)");
				}else{
					saveToR+="ini <- list(rm=" + str1 + ",Topt=" + str2 + ",Troh =" + str3+", Smin="+str4+ ")"+"\r\n";
					c.eval("ini <- list(rm=" + str1 + ",Topt=" + str2 + ",Troh =" + str3+", Smin="+str4 + ")");
				}
				break;
			case 58:/** Lactin 3 **/
				if(bolAllModels){
					saveToR+="ini=c(p=-0.01689, Tl=0.001931, dt=-12.306,lamb=0.006475)"+"\r\n";
					c.eval("ini=c(p=-0.01689, Tl=0.001931, dt=-12.306,lamb=0.006475)");
				}else{
					saveToR+="ini <- list(p=" + str1 + ",Tl=" + str2 + ",dt =" + str3+", lamb="+str4+ ")"+"\r\n";
					c.eval("ini <- list(p=" + str1 + ",Tl=" + str2 + ",dt =" + str3+", lamb="+str4 + ")");
				}
				break;
			case 59:/** Sigmoid or logistic **/
				if(bolAllModels){
					saveToR+="ini=c(c1=0.4305,a=3.33407,b=-0.19878)"+"\r\n";
					c.eval("ini=c(c1=0.4305,a=3.33407,b=-0.19878)");
				}else{
					saveToR+="ini <- list(c1=" + str1 + ",a=" + str2 + ",b =" + str3+ ")"+"\r\n";
					c.eval("ini <- list(c1=" + str1 + ",a=" + str2 + ",b =" + str3 + ")");
				}
				break;
				
				case 60:/** MAIZSIM **/
					if(bolAllModels){
						saveToR+="ini=c(Rmax =0.53,Tceil=43.7,Topc =32.1)"+"\r\n";
						c.eval("ini=c(Rmax =0.53,Tceil=43.7,Topc =32.1)");
					}else{
						saveToR+="ini <- list(Rmax=" + str1 + ",Tceil=" + str2 + ",Topc =" + str3+ ")"+"\r\n";
						c.eval("ini <- list(Rmax=" + str1 + ",Tceil=" + str2 + ",Topc =" + str3 + ")");
					}
					break;
				case 61:/** Enzymatic Response **/
					if(bolAllModels){
						saveToR+="ini=c(alpha=3.5, To=306.4)"+"\r\n";
						c.eval("ini=c(alpha=3.5, To=306.4)");
					}else{
						saveToR+="ini <- list(alpha=" + str1 + ",To=" + str2 + ")"+"\r\n";
						c.eval("ini <- list(alpha=" + str1 + ",To=" + str2 + ")");
					}
					break;
					
				case 62:/** beta 1 **/ // ,,,, Tc   Tmin=  min(datashap[,1]) Topt=mean(datashap[,1])   ,Tmax=
					if(bolAllModels){
						saveToR+="ini=c(k=log(max(datashap[,2])), alpha=2.5,betas=0.469, Tb=min(min(datashap[,1]),5),Tc=max(max(datashap[,1]),35))"+"\r\n";
						c.eval("ini=c(k=log(max(datashap[,2])), alpha=2.5,betas=0.469, Tb=min(min(datashap[,1]),5),Tc=max(max(datashap[,1]),35))");
					}else{
						saveToR+="ini <- list(k=" + str1 + ",alpha =" + str2 + ",betas =" + str3 + ", Tb =" + str4 + ",Tc =" + str5 + ")"+"\r\n";
						c.eval("ini <- list(k=" + str1 + ",alpha =" + str2 + ",betas =" + str3 + ", Tb =" + str4 + ",Tc =" + str5 + ")");
					}
					break;
					
				case 63:/** Wang et Engel **/ // ,,,, Tc   Tmin= min(datashap[,1]), Topt=mean(datashap[,1]) Tmax=max(datashap[,1])
					if(bolAllModels){
						saveToR+="ini=c(Tmin= min(min(datashap[,1]),5), Tmax=max(max(datashap[,1]),35), Topt=mean(datashap[,1]))"+"\r\n";
						c.eval("ini=c(Tmin= min(min(datashap[,1]),5), Tmax=max(max(datashap[,1]),35), Topt=mean(datashap[,1]))");
					}else{
						saveToR+="ini <- list(Tmin=" + str1 + ",Tmax =" + str2 + ",Topt =" + str3 + ")"+"\r\n";
						c.eval("ini <- list(Tmin=" + str1 + ",Tmax =" + str2 + ",Topt =" + str3 + ")");
					}
					break;
					
				case 64:/** Richards**/ //  Tmin=  min(datashap[,1]) Topt=mean(datashap[,1])   ,Tmax=
					if(bolAllModels){
						saveToR+="ini=c(Yasym=max(datashap[,2]), k=5,Tm=mean(datashap[,1]),v=1.01)"+"\r\n";
						c.eval("ini=c(Yasym=max(datashap[,2]), k=5,Tm=mean(datashap[,1]),v=1.01)");
					}else{
						saveToR+="ini <- list(Yasym=" + str1 + ",k =" + str2 + ",Tm =" + str3 + ", v =" + str4 + ")"+"\r\n";
						c.eval("ini <- list(Yasym=" + str1 + ",k =" + str2 + ",Tm =" + str3 + ", v =" + str4 + ")");
					}
					break;
					

					
				
				case 65:/** Gompertz **/ // ,,,, Tc   Tmin=  min(datashap[,1]) Topt=mean(datashap[,1])   ,Tmax=
					if(bolAllModels){
						saveToR+="ini=c(Yasym=max(datashap[,2]), k=5,Tm=mean(datashap[,1]))"+"\r\n";
						c.eval("ini=c(Yasym=max(datashap[,2]), k=5,Tm=mean(datashap[,1]))");
					}else{
						saveToR+="ini <- list(Yasym=" + str1 + ",k =" + str2 + ",Tm =" + str3 + ")"+"\r\n";
						c.eval("ini <- list(Yasym=" + str1 + ",k =" + str2 + ",Tm =" + str3 + ")");
					}
					break;
					
				case 66:/** Beta 2 **/ // Rmax,Tmax,Topt   Tmin=  min(datashap[,1]) Topt=mean(datashap[,1])   ,Tmax=
					if(bolAllModels){
						saveToR+="ini=c(Rmax=max(datashap[,2]), Tmax=max(max(datashap[,1]),35),Topt=mean(datashap[,1]))"+"\r\n";
						c.eval("ini=c(Rmax=max(datashap[,2]), Tmax=max(max(datashap[,1]),35),Topt=mean(datashap[,1]))");
					}else{
						saveToR+="ini <- list(Rmax=" + str1 + ",Tmax =" + str2 + ",Topt =" + str3 + ")"+"\r\n";
						c.eval("ini <- list(Rmax=" + str1 + ",Tmax =" + str2 + ",Topt =" + str3 + ")");
					}
					break;
					

	///From here coorect init value of parameter!!	
					
				case 67:/** Q10 function **/ //Q10,Tref   initialize the value of Q10 with coef of the linear regression of the dataset
					if(bolAllModels){
						saveToR+="ini=c(Q10=max(datashap[,2]), Tref=mean(datashap[,1]))"+"\r\n";
						c.eval("ini=c(Q10=max(datashap[,2]), Tref=mean(datashap[,1]))");
					}else{
						saveToR+="ini <- list(Q10=" + str1 + ",Tref =" + str2 + ")"+"\r\n";
						c.eval("ini <- list(Q10=" + str1 + ",Tref =" + str2 + ")");
					}
					break;
				
				
				
				case 68:/** Ratkowsky 3 **/
					if(bolAllModels){
						saveToR+="ini=c(Tmin= min(min(datashap[,1]),5), Tref=mean(datashap[,1]))"+"\r\n";
						c.eval("ini=c(Tmin= min(min(datashap[,1]),5), Tref=mean(datashap[,1]))");
					}else{
						saveToR+="ini <- list(Tmin=" + str1 + ",Tref=" + str2 + ")"+"\r\n";
						c.eval("ini <- list(Tmin=" + str1 + ",Tref=" + str2 + ")");
					}
					break;
		
							
				case 69:/** Beta 3 **/ // Rmax,Topt,Tmax 
					if(bolAllModels){
						saveToR+="ini=c(Rmax=max(datashap[,2]),Topt=mean(datashap[,1]), Tmax=max(max(datashap[,1]),35))"+"\r\n";
						c.eval("ini=c(Rmax=max(datashap[,2]), Topt=mean(datashap[,1]), Tmax=max(max(datashap[,1]),35))");
					}else{
						saveToR+="ini <- list(Rmax=" + str1 + ",Topt =" + str2 + ",Tmax =" + str3 + ")"+"\r\n";
						c.eval("ini <- list(Rmax=" + str1 + ",Topt =" + str2 + ",Tmax =" + str3 + ")");
					}
					break;
					
					
				case 70:/** Bell curve**/ // Yasym,a,b,Topt  
					if(bolAllModels){
						saveToR+="ini=c(Yasym=max(datashap[,2]), a=5, b=1.01,Topt=mean(datashap[,1]))"+"\r\n";
						c.eval("ini=c(Yasym=max(datashap[,2]), a=5, b=1.01,Topt=mean(datashap[,1]))");
					}else{
						saveToR+="ini <- list(Yasym=" + str1 + ",a =" + str2 + ",b =" + str3 + ", Topt =" + str4 + ")"+"\r\n";
						c.eval("ini <- list(Yasym=" + str1 + ",a =" + str2 + ",b =" + str3 + ", Topt =" + str4 + ")");
					}
					break;
					
				case 71:/** Gaussian function**/ // Yasym,b,Topt   
					if(bolAllModels){
						saveToR+="ini=c(Yasym=max(datashap[,2]), b=5,Topt=mean(datashap[,1]))"+"\r\n";
						c.eval("ini=c(Yasym=max(datashap[,2]), b=5,Topt=mean(datashap[,1]))");
					}else{
						saveToR+="ini <- list(Yasym=" + str1 + ",b =" + str2 + ",Topt =" + str3 + ")"+"\r\n";
						c.eval("ini <- list(Yasym=" + str1 + ",b =" + str2 + ",Topt =" + str3 + ")");
					}
					break;
					
					
				case 72:/** Beta 4 **/ // Rmax,Tmin,Tmax,Topt  
					if(bolAllModels){
						saveToR+="ini=c(Rmax=max(datashap[,2]),Tmin=min(min(datashap[,1]),5), Tmax=max(max(datashap[,1]),35),Topt=mean(datashap[,1]))"+"\r\n";
						c.eval("ini=c(Rmax=max(datashap[,2]),Tmin=min(min(datashap[,1]),5), Tmax=max(max(datashap[,1]),35),Topt=mean(datashap[,1]))");
					}else{
						saveToR+="ini <- list(Rmax=" + str1 + ",Tmin =" + str2 + ",Tmax =" + str3 + ", Topt =" + str4 + ")"+"\r\n";
						c.eval("ini <- list(Rmax=" + str1 + ",Tmin =" + str2 + ",Tmax =" + str3 + ", Topt =" + str4 + ")");
					}
					break;
					
					
				case 73:/** Expo first order plus logistic**/ // Yo,k,b   
					if(bolAllModels){
						saveToR+="ini=c(Yo=max(datashap[,2]), k=1.01, b=0.55)"+"\r\n";
						c.eval("ini=c(Yo=max(datashap[,2]), k=1.01, b=0.55)");
					}else{
						saveToR+="ini <- list(Yo=" + str1 + ",k =" + str2 + ",b =" + str3 + ")"+"\r\n";
						c.eval("ini <- list(Yo=" + str1 + ",k =" + str2 + ",b =" + str3 + ")");
					}
					break;
					
					
				case 74:/** Beta 5 **/ // Yb,Rmax,Tmax,Topt,Tmin  
					if(bolAllModels){
						saveToR+="ini=c(Yb=min(datashap[,2]),Rmax=max(datashap[,2]),Tmax=max(max(datashap[,1]),35),Topt=mean(datashap[,1]),Tmin=min(min(datashap[,1]),5))"+"\r\n";
						c.eval("ini=c(Yb=min(datashap[,2]),Rmax=max(datashap[,2]),Tmax=max(max(datashap[,1]),35),Topt=mean(datashap[,1]),Tmin=min(min(datashap[,1]),5))");
					}else{
						saveToR+="ini <- list(Yb=" + str1 + ",Rmax =" + str2 + ",Tmax =" + str3 + ", Topt =" + str4 + ", Tmin =" + str5 + ")"+"\r\n";
						c.eval("ini <- list(Yb=" + str1 + ",Rmax =" + str2 + ",Tmax =" + str3 + ", Topt =" + str4 + ", Tmin =" + str5 + ")");
					}
					break;
					
					
				case 75:/** Beta 6 **/ // Rmax,Tmax  
					if(bolAllModels){
						saveToR+="ini=c(Rmax=max(datashap[,2]), Tmax=max(max(datashap[,1]),35))"+"\r\n";
						c.eval("ini=c(Rmax=max(datashap[,2]), Tmax=max(max(datashap[,1]),35))");
					}else{
						saveToR+="ini <- list(Rmax=" + str1 + ",Tmax =" + str2  + ")"+"\r\n";
						c.eval("ini <- list(Rmax=" + str1 + ",Tmax =" + str2 + ")");
					}
					break;
					
				case 76:/** Beta 7 **/ // Rmax,Tmax   
					if(bolAllModels){
						saveToR+="ini=c(Rmax=max(datashap[,2]), Tmax=max(max(datashap[,1]),35))"+"\r\n";
						c.eval("ini=c(Rmax=max(datashap[,2]), Tmax=max(max(datashap[,1]),35))");
					}else{
						saveToR+="ini <- list(Rmax=" + str1 + ",Tmax =" + str2  + ")"+"\r\n";
						c.eval("ini <- list(Rmax=" + str1 + ",Tmax =" + str2 + ")");
					}
					break;
					
				case 77:/** Beta 8 **/ // Rmax,Tmax,Topt  
					if(bolAllModels){
						saveToR+="ini=c(Rmax=max(datashap[,2]), Tmax=max(max(datashap[,1]),35),Topt=mean(datashap[,1]))"+"\r\n";
						c.eval("ini=c(Rmax=max(datashap[,2]), Tmax=max(max(datashap[,1]),35),Topt=mean(datashap[,1]))");
					}else{
						saveToR+="ini <- list(Rmax=" + str1 + ",Tmax =" + str2 + ",Topt =" + str3 + ")"+"\r\n";
						c.eval("ini <- list(Rmax=" + str1 + ",Tmax =" + str2 + ",Topt =" + str3  + ")");
					}
					break;
					
					
				case 78:/** Modified exponential **/ // a,b,Topt   
					if(bolAllModels){
						saveToR+="ini=c(a=max(datashap[,2]), b=5,Topt=mean(datashap[,1]))"+"\r\n";
						c.eval("ini=c(a=max(datashap[,2]), b=5,Topt=mean(datashap[,1]))");
					}else{
						saveToR+="ini <- list(a=" + str1 + ",b =" + str2 + ",Topt =" + str3 + ")"+"\r\n";
						c.eval("ini <- list(a=" + str1 + ",b =" + str2 + ",Topt =" + str3  + ")");
					}
					break;
					
					
				case 79:/** Lorentzian 3-parameter **/ // a,b,Topt   
					if(bolAllModels){
						saveToR+="ini=c(a=max(datashap[,2]), b=5,Topt=mean(datashap[,1]))"+"\r\n";
						c.eval("ini=c(a=max(datashap[,2]), b=5,Topt=mean(datashap[,1]))");
					}else{
						saveToR+="ini <- list(a=" + str1 + ",b =" + str2 + ",Topt =" + str3 + ")"+"\r\n";
						c.eval("ini <- list(a=" + str1 + ",b =" + str2 + ",Topt =" + str3  + ")");
					}
					break;

				
					
				case 80:/** Lorentzian 4-parameter**/ //Yopt,a,b,Topt   
					if(bolAllModels){
						saveToR+="ini=c(Yopt=0.0001,a=max(datashap[,2]), b=5,Topt=mean(datashap[,1]))"+"\r\n";
						c.eval("ini=c(Yopt=0.0001,a=max(datashap[,2]), b=5,Topt=mean(datashap[,1]))");
					}else{
						saveToR+="ini <- list(Yopt=" + str1 + ",a =" + str2 + ",b =" + str3 +",Topt =" + str4 + ")"+"\r\n";
						c.eval("ini <- list(Yopt=" + str1 + ",a =" + str2 + ",b =" + str3  +",Topt =" + str4 + ")");
					}
					break;

					
				case 81:/** Log normal 3-parameter **/ // a,b,Topt   
					if(bolAllModels){
						saveToR+="ini=c(a=max(datashap[,2]), b=5,Topt=mean(datashap[,1]))"+"\r\n";
						c.eval("ini=c(a=max(datashap[,2]), b=5,Topt=mean(datashap[,1]))");
					}else{
						saveToR+="ini <- list(a=" + str1 + ",b =" + str2 + ",Topt =" + str3 + ")"+"\r\n";
						c.eval("ini <- list(a=" + str1 + ",b =" + str2 + ",Topt =" + str3  + ")");
					}
					break;
					
					
				case 82:/** Pseudo-voigt 4 parameter**/ //a,b,k,Topt   
					if(bolAllModels){
						saveToR+="ini=c(a=max(datashap[,2]),b=5, k=0.025,Topt=mean(datashap[,1]))"+"\r\n";
						c.eval("ini=c(a=max(datashap[,2]),b=5, k=0.025,Topt=mean(datashap[,1]))");
					}else{
						saveToR+="ini <- list(a=" + str1 + ",b =" + str2 + ",k =" + str3 +",Topt =" + str4 + ")"+"\r\n";
						c.eval("ini <- list(a=" + str1 + ",b =" + str2 + ",k =" + str3  +",Topt =" + str4 + ")");
					}
					break;	
	
			default:
				break;
			}
		}catch (RserveException e) {
			Rserve.saveIlcymError(MainPageWizardPage.getstrMortalityPath(), title, saveToR);
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
			if(tempar.equalsIgnoreCase("ini")){
				dbpars[0] = c.eval(tempar + "["+'"'+"Tl"+'"'+"]").asList().at(0).asDouble();
				dbpars[1] = c.eval(tempar + "["+'"'+"Th"+'"'+"]").asList().at(0).asDouble();
				dbpars[2] = c.eval(tempar + "["+'"'+"Ha"+'"'+"]").asList().at(0).asDouble();
				dbpars[3] = c.eval(tempar + "["+'"'+"Hl"+'"'+"]").asList().at(0).asDouble();
				dbpars[4] = c.eval(tempar + "["+'"'+"Hh"+'"'+"]").asList().at(0).asDouble();
				break;
			}
			
			dbpars[0] = c.eval(tempar + "["+'"'+"p"+'"'+"]").asList().at(0).asDouble();
			dbpars[1] = c.eval(tempar + "["+'"'+"To"+'"'+"]").asList().at(0).asDouble();
			dbpars[2] = c.eval(tempar + "["+'"'+"Tl"+'"'+"]").asList().at(0).asDouble();
			dbpars[3] = c.eval(tempar + "["+'"'+"Th"+'"'+"]").asList().at(0).asDouble();
			dbpars[4] = c.eval(tempar + "["+'"'+"Ha"+'"'+"]").asList().at(0).asDouble();
			dbpars[5] = c.eval(tempar + "["+'"'+"Hl"+'"'+"]").asList().at(0).asDouble();
			dbpars[6] = c.eval(tempar + "["+'"'+"Hh"+'"'+"]").asList().at(0).asDouble();
			
			dbStdpars[0] = c.eval("Std.Error").asDoubles()[0];
			dbStdpars[1] = c.eval("Std.Error").asDoubles()[1];
			dbStdpars[2] = c.eval("Std.Error").asDoubles()[2];
			dbStdpars[3] = c.eval("Std.Error").asDoubles()[3];
			dbStdpars[4] = c.eval("Std.Error").asDoubles()[4];
			dbStdpars[5] = c.eval("Std.Error").asDoubles()[5];
			dbStdpars[6] = c.eval("Std.Error").asDoubles()[6];
			
			parsName[0] = "p";
			parsName[1] = "To";
			parsName[2] = "Tl";
			parsName[3] = "Th";
			parsName[4] = "Ha";
			parsName[5] = "Hl";
			parsName[6] = "Hh";
			
			break;
		case 2:
			if(tempar.equalsIgnoreCase("ini")){
				dbpars[0] = c.eval(tempar + "["+'"'+"To"+'"'+"]").asList().at(0).asDouble();
				dbpars[1] = c.eval(tempar + "["+'"'+"Tl"+'"'+"]").asList().at(0).asDouble();
				dbpars[2] = c.eval(tempar + "["+'"'+"Th"+'"'+"]").asList().at(0).asDouble();
				dbpars[3] = c.eval(tempar + "["+'"'+"Ha"+'"'+"]").asList().at(0).asDouble();
				dbpars[4] = c.eval(tempar + "["+'"'+"Hl"+'"'+"]").asList().at(0).asDouble();
				dbpars[5] = c.eval(tempar + "["+'"'+"Hh"+'"'+"]").asList().at(0).asDouble();
				break;
			}
			
			dbpars[0] = c.eval(tempar).asDoubles()[0];
			dbpars[1] = c.eval(tempar).asDoubles()[1];
			dbpars[2] = c.eval(tempar).asDoubles()[3];
			dbpars[3] = c.eval(tempar).asDoubles()[6];
			dbpars[4] = c.eval(tempar).asDoubles()[2];
			dbpars[5] = c.eval(tempar).asDoubles()[4];
			dbpars[6] = c.eval(tempar).asDoubles()[5];
			
			dbStdpars[0] = c.eval("Std.Error").asDoubles()[0];
			dbStdpars[1] = c.eval("Std.Error").asDoubles()[1];
			dbStdpars[2] = c.eval("Std.Error").asDoubles()[2];
			dbStdpars[3] = c.eval("Std.Error").asDoubles()[3];
			dbStdpars[4] = c.eval("Std.Error").asDoubles()[4];
			dbStdpars[5] = c.eval("Std.Error").asDoubles()[5];
			dbStdpars[6] = c.eval("Std.Error").asDoubles()[6];
			
			parsName[0] = "p";
			parsName[1] = "To";
			parsName[2] = "Tl";
			parsName[3] = "Th";
			parsName[4] = "Ha";
			parsName[5] = "Hl";
			parsName[6] = "Hh";
			break;
		case 3:
			dbpars[0] = c.eval(tempar + "["+'"'+"p"+'"'+"]").asList().at(0).asDouble();
			dbpars[1] = c.eval(tempar + "["+'"'+"To"+'"'+"]").asList().at(0).asDouble();
			dbpars[2] = c.eval(tempar + "["+'"'+"Tl"+'"'+"]").asList().at(0).asDouble();
			dbpars[3] = c.eval(tempar + "["+'"'+"Th"+'"'+"]").asList().at(0).asDouble();
			dbpars[4] = c.eval(tempar + "["+'"'+"Ha"+'"'+"]").asList().at(0).asDouble();
			dbpars[5] = c.eval(tempar + "["+'"'+"Hl"+'"'+ "]").asList().at(0).asDouble();
			dbpars[6] = c.eval(tempar + "["+'"'+"Hh"+'"'+"]").asList().at(0).asDouble();
			
			dbStdpars[0] = c.eval("Std.Error").asDoubles()[0];
			dbStdpars[1] = c.eval("Std.Error").asDoubles()[1];
			dbStdpars[2] = c.eval("Std.Error").asDoubles()[2];
			dbStdpars[3] = c.eval("Std.Error").asDoubles()[3];
			dbStdpars[4] = c.eval("Std.Error").asDoubles()[4];
			dbStdpars[5] = c.eval("Std.Error").asDoubles()[5];
			dbStdpars[6] = c.eval("Std.Error").asDoubles()[6];
			
			parsName[0] = "p";
			parsName[1] = "To";
			parsName[2] = "Tl";
			parsName[3] = "Th";
			parsName[4] = "Ha";
			parsName[5] = "Hl";
			parsName[6] = "Hh";
			break;
		case 4:
			dbpars[0] = c.eval(tempar + "["+'"'+"p"+'"'+"]").asList().at(0).asDouble();
			dbpars[1] = c.eval(tempar + "["+'"'+"To"+'"'+"]").asList().at(0).asDouble();
			dbpars[2] = c.eval(tempar + "["+'"'+"Ha"+'"'+"]").asList().at(0).asDouble();
			
			dbStdpars[0] = c.eval("Std.Error").asDoubles()[0];
			dbStdpars[1] = c.eval("Std.Error").asDoubles()[1];
			dbStdpars[2] = c.eval("Std.Error").asDoubles()[2];
			
			parsName[0] = "p";
			parsName[1] = "To";
			parsName[2] = "Ha";
			break;
		case 5:
			dbpars[0] = c.eval(tempar + "["+'"'+"p"+'"'+"]").asList().at(0).asDouble();
			dbpars[1] = c.eval(tempar + "["+'"'+"To"+'"'+"]").asList().at(0).asDouble();
			dbpars[2] = c.eval(tempar + "["+'"'+"Tl"+'"'+"]").asList().at(0).asDouble();
			dbpars[3] = c.eval(tempar + "["+'"'+"Ha"+'"'+"]").asList().at(0).asDouble();
			dbpars[4] = c.eval(tempar + "["+'"'+"Hl"+'"'+"]").asList().at(0).asDouble();
			
			dbStdpars[0] = c.eval("Std.Error").asDoubles()[0];
			dbStdpars[1] = c.eval("Std.Error").asDoubles()[1];
			dbStdpars[2] = c.eval("Std.Error").asDoubles()[2];
			dbStdpars[3] = c.eval("Std.Error").asDoubles()[3];
			dbStdpars[4] = c.eval("Std.Error").asDoubles()[4];
			
			parsName[0] = "p";
			parsName[1] = "To";
			parsName[2] = "Tl";
			parsName[3] = "Ha";
			parsName[4] = "Hl";
			break;
		case 6:
			dbpars[0] = c.eval(tempar + "["+'"'+"p"+'"'+"]").asList().at(0).asDouble();
			dbpars[1] = c.eval(tempar + "["+'"'+"To"+'"'+"]").asList().at(0).asDouble();
			dbpars[2] = c.eval(tempar + "["+'"'+"Th"+'"'+"]").asList().at(0).asDouble();
			dbpars[3] = c.eval(tempar + "["+'"'+"Ha"+'"'+"]").asList().at(0).asDouble();
			dbpars[4] = c.eval(tempar + "["+'"'+"Hh"+'"'+"]").asList().at(0).asDouble();
			
			dbStdpars[0] = c.eval("Std.Error").asDoubles()[0];
			dbStdpars[1] = c.eval("Std.Error").asDoubles()[1];
			dbStdpars[2] = c.eval("Std.Error").asDoubles()[2];
			dbStdpars[3] = c.eval("Std.Error").asDoubles()[3];
			dbStdpars[4] = c.eval("Std.Error").asDoubles()[4];
			
			parsName[0] = "p";
			parsName[1] = "To";
			parsName[2] = "Th";
			parsName[3] = "Ha";
			parsName[4] = "Hh";
			break;
		case 7:/***/
			if(tempar.equalsIgnoreCase("ini")){
				dbpars[0] = c.eval(tempar + "["+'"'+"To"+'"'+"]").asList().at(0).asDouble();
				dbpars[1] = c.eval(tempar + "["+'"'+"Ha"+'"'+"]").asList().at(0).asDouble();
				break;
			}
			
			dbpars[0] = c.eval(tempar).asDoubles()[0];
			dbpars[1] = c.eval(tempar).asDoubles()[1];
			dbpars[2] = c.eval(tempar).asDoubles()[2];
			
			dbStdpars[0] = c.eval("Std.Error").asDoubles()[0];
			dbStdpars[1] = c.eval("Std.Error").asDoubles()[1];
			dbStdpars[2] = c.eval("Std.Error").asDoubles()[2];
			
			parsName[0] = "p";
			parsName[1] = "To";
			parsName[2] = "Ha";
			break;
		case 8:/***/
			if(tempar.equalsIgnoreCase("ini")){
				dbpars[0] = c.eval(tempar + "["+'"'+"To"+'"'+"]").asList().at(0).asDouble();
				dbpars[1] = c.eval(tempar + "["+'"'+"Tl"+'"'+"]").asList().at(0).asDouble();
				dbpars[2] = c.eval(tempar + "["+'"'+"Ha"+'"'+"]").asList().at(0).asDouble();
				dbpars[3] = c.eval(tempar + "["+'"'+"Hl"+'"'+"]").asList().at(0).asDouble();
				break;
			}
			
			dbpars[0] = c.eval(tempar).asDoubles()[0];
			dbpars[1] = c.eval(tempar).asDoubles()[1];
			dbpars[3] = c.eval(tempar).asDoubles()[2];
			dbpars[2] = c.eval(tempar).asDoubles()[3];
			dbpars[4] = c.eval(tempar).asDoubles()[4];
			
			dbStdpars[0] = c.eval("Std.Error").asDoubles()[0];
			dbStdpars[1] = c.eval("Std.Error").asDoubles()[1];
			dbStdpars[2] = c.eval("Std.Error").asDoubles()[2];
			dbStdpars[3] = c.eval("Std.Error").asDoubles()[3];
			dbStdpars[4] = c.eval("Std.Error").asDoubles()[4];
			
			parsName[0] = "p";
			parsName[1] = "To";
			parsName[2] = "Tl";
			parsName[3] = "Ha";
			parsName[4] = "Hl";
			break;
		case 9:
			if(tempar.equalsIgnoreCase("ini")){
				dbpars[0] = c.eval(tempar + "["+'"'+"To"+'"'+"]").asList().at(0).asDouble();
				dbpars[1] = c.eval(tempar + "["+'"'+"Th"+'"'+"]").asList().at(0).asDouble();
				dbpars[2] = c.eval(tempar + "["+'"'+"Ha"+'"'+"]").asList().at(0).asDouble();
				dbpars[3] = c.eval(tempar + "["+'"'+"Hh"+'"'+"]").asList().at(0).asDouble();
				break;
			}
			
			dbpars[0] = c.eval(tempar).asDoubles()[0];
			dbpars[1] = c.eval(tempar).asDoubles()[1];
			dbpars[2] = c.eval(tempar).asDoubles()[4];
			dbpars[3] = c.eval(tempar).asDoubles()[2];
			dbpars[4] = c.eval(tempar).asDoubles()[3];
			
			dbStdpars[0] = c.eval("Std.Error").asDoubles()[0];
			dbStdpars[1] = c.eval("Std.Error").asDoubles()[1];
			dbStdpars[2] = c.eval("Std.Error").asDoubles()[2];
			dbStdpars[3] = c.eval("Std.Error").asDoubles()[3];
			dbStdpars[4] = c.eval("Std.Error").asDoubles()[4];
			
			parsName[0] = "p";
			parsName[1] = "To";
			parsName[2] = "Th";
			parsName[3] = "Ha";
			parsName[4] = "Hh";
			break;
		case 10:
			dbpars[0] = c.eval(tempar + "["+'"'+"p"+'"'+"]").asList().at(0).asDouble();
			dbpars[1] = c.eval(tempar + "["+'"'+"Tl"+'"'+"]").asList().at(0).asDouble();
			dbpars[2] = c.eval(tempar + "["+'"'+"Ha"+'"'+"]").asList().at(0).asDouble();
			dbpars[3] = c.eval(tempar + "["+'"'+"Hl"+'"'+"]").asList().at(0).asDouble();
			
			dbStdpars[0] = c.eval("Std.Error").asDoubles()[0];
			dbStdpars[1] = c.eval("Std.Error").asDoubles()[1];
			dbStdpars[2] = c.eval("Std.Error").asDoubles()[2];
			dbStdpars[3] = c.eval("Std.Error").asDoubles()[3];
			
			parsName[0] = "p";
			parsName[1] = "Tl";
			parsName[2] = "Ha";
			parsName[3] = "Hl";
			break;
		case 11:
			dbpars[0] = c.eval(tempar + "["+'"'+"p"+'"'+"]").asList().at(0).asDouble();
			dbpars[1] = c.eval(tempar + "["+'"'+"Tl"+'"'+"]").asList().at(0).asDouble();
			dbpars[2] = c.eval(tempar + "["+'"'+"Th"+'"'+"]").asList().at(0).asDouble();
			dbpars[3] = c.eval(tempar + "["+'"'+"Ha"+'"'+"]").asList().at(0).asDouble();
			dbpars[4] = c.eval(tempar + "["+'"'+"Hl"+'"'+"]").asList().at(0).asDouble();
			dbpars[5] = c.eval(tempar + "["+'"'+"Hh"+'"'+"]").asList().at(0).asDouble();
			
			dbStdpars[0] = c.eval("Std.Error").asDoubles()[0];
			dbStdpars[1] = c.eval("Std.Error").asDoubles()[1];
			dbStdpars[2] = c.eval("Std.Error").asDoubles()[2];
			dbStdpars[3] = c.eval("Std.Error").asDoubles()[3];
			dbStdpars[4] = c.eval("Std.Error").asDoubles()[4];
			dbStdpars[5] = c.eval("Std.Error").asDoubles()[5];
			
			parsName[0] = "p";
			parsName[1] = "Tl";
			parsName[2] = "Th";
			parsName[3] = "Ha";
			parsName[4] = "Hl";
			parsName[5] = "Hh";
			break;
		case 12:
			if(tempar.equalsIgnoreCase("ini")){
				dbpars[0] = c.eval(tempar + "["+'"'+"Tl"+'"'+"]").asList().at(0).asDouble();
				dbpars[1] = c.eval(tempar + "["+'"'+"Th"+'"'+"]").asList().at(0).asDouble();
				dbpars[2] = c.eval(tempar + "["+'"'+"Ha"+'"'+"]").asList().at(0).asDouble();
				dbpars[3] = c.eval(tempar + "["+'"'+"Hl"+'"'+"]").asList().at(0).asDouble();
				dbpars[4] = c.eval(tempar + "["+'"'+"Hh"+'"'+"]").asList().at(0).asDouble();
				break;
			}
			dbpars[0] = c.eval(tempar).asDoubles()[0];
			dbpars[1] = c.eval(tempar).asDoubles()[2];
			dbpars[2] = c.eval(tempar).asDoubles()[5];
			dbpars[3] = c.eval(tempar).asDoubles()[3];
			dbpars[4] = c.eval(tempar).asDoubles()[4];
			dbpars[5] = c.eval(tempar).asDoubles()[1];
			
			dbStdpars[0] = c.eval("Std.Error").asDoubles()[0];
			dbStdpars[1] = c.eval("Std.Error").asDoubles()[1];
			dbStdpars[2] = c.eval("Std.Error").asDoubles()[2];
			dbStdpars[3] = c.eval("Std.Error").asDoubles()[3];
			dbStdpars[4] = c.eval("Std.Error").asDoubles()[4];
			dbStdpars[5] = c.eval("Std.Error").asDoubles()[5];
			
			parsName[0] = "p";
			parsName[1] = "Tl";
			parsName[2] = "Th";
			parsName[3] = "Ha";
			parsName[4] = "Hl";
			parsName[5] = "Hh";
			break;
		case 13:
			dbpars[0] = c.eval(tempar + "["+'"'+"p"+'"'+"]").asList().at(0).asDouble();
			dbpars[1] = c.eval(tempar + "["+'"'+"Tl"+'"'+"]").asList().at(0).asDouble();
			dbpars[2] = c.eval(tempar + "["+'"'+"Ha"+'"'+"]").asList().at(0).asDouble();
			dbpars[3] = c.eval(tempar + "["+'"'+"Hl"+'"'+"]").asList().at(0).asDouble();
			
			dbStdpars[0] = c.eval("Std.Error").asDoubles()[0];
			dbStdpars[1] = c.eval("Std.Error").asDoubles()[1];
			dbStdpars[2] = c.eval("Std.Error").asDoubles()[2];
			dbStdpars[3] = c.eval("Std.Error").asDoubles()[3];
			
			parsName[0] = "p";
			parsName[1] = "Tl";
			parsName[2] = "Ha";
			parsName[3] = "Hl";
			break;
		case 14:
			dbpars[0] = c.eval(tempar + "["+'"'+"p"+'"'+"]").asList().at(0).asDouble();
			dbpars[1] = c.eval(tempar + "["+'"'+"Th"+'"'+"]").asList().at(0).asDouble();
			dbpars[2] = c.eval(tempar + "["+'"'+"Ha"+'"'+"]").asList().at(0).asDouble();
			dbpars[3] = c.eval(tempar + "["+'"'+"Hh"+'"'+"]").asList().at(0).asDouble();
			
			dbStdpars[0] = c.eval("Std.Error").asDoubles()[0];
			dbStdpars[1] = c.eval("Std.Error").asDoubles()[1];
			dbStdpars[2] = c.eval("Std.Error").asDoubles()[2];
			dbStdpars[3] = c.eval("Std.Error").asDoubles()[3];
			
			parsName[0] = "p";
			parsName[1] = "Th";
			parsName[2] = "Ha";
			parsName[3] = "Hh";
			break;
		case 15:/**Deva 1**/
			dbpars[0] = c.eval(tempar + "["+'"'+"Tmin"+'"'+"]").asList().at(0).asDouble();
			dbpars[1] = c.eval(tempar + "["+'"'+"b"+'"'+"]").asList().at(0).asDouble();
			
			dbStdpars[0] = c.eval("Std.Error").asDoubles()[0];
			dbStdpars[1] = c.eval("Std.Error").asDoubles()[1];
			dbStdpars[2] = c.eval("Std.Error").asDoubles()[2];
			
			parsName[0] = "Tmin";
			parsName[1] = "b";
			break;
		case 16:/**Deva 2**/
			dbpars[0] = c.eval(tempar + "["+'"'+"b1"+'"'+"]").asList().at(0).asDouble();
			dbpars[1] = c.eval(tempar + "["+'"'+"b2"+'"'+"]").asList().at(0).asDouble();
			dbpars[2] = c.eval(tempar + "["+'"'+"b3"+'"'+"]").asList().at(0).asDouble();
			dbpars[3] = c.eval(tempar + "["+'"'+"b4"+'"'+"]").asList().at(0).asDouble();
			dbpars[4] = c.eval(tempar + "["+'"'+"b5"+'"'+"]").asList().at(0).asDouble();
			
			dbStdpars[0] = c.eval("Std.Error").asDoubles()[0];
			dbStdpars[1] = c.eval("Std.Error").asDoubles()[1];
			dbStdpars[2] = c.eval("Std.Error").asDoubles()[2];
			dbStdpars[3] = c.eval("Std.Error").asDoubles()[3];
			dbStdpars[4] = c.eval("Std.Error").asDoubles()[4];
			
			parsName[0] = "b1";
			parsName[1] = "b2";
			parsName[2] = "b3";
			parsName[3] = "b4";
			parsName[4] = "b5";
			break;
		case 17:/**Logan 1**/
			dbpars[0] = c.eval(tempar + "["+'"'+"Y"+'"'+"]").asList().at(0).asDouble();
			dbpars[1] = c.eval(tempar + "["+'"'+"Tmax"+'"'+"]").asList().at(0).asDouble();
			dbpars[2] = c.eval(tempar + "["+'"'+"p"+'"'+"]").asList().at(0).asDouble();
			dbpars[3] = c.eval(tempar + "["+'"'+"v"+'"'+"]").asList().at(0).asDouble();
			
			parsName[0] = "Y";
			parsName[1] = "Tmax";
			parsName[2] = "p";
			parsName[3] = "v";
			break;
		case 18:/**Logan 2**/
			dbpars[0] = c.eval(tempar + "["+'"'+"alfa"+'"'+"]").asList().at(0).asDouble();
			dbpars[1] = c.eval(tempar + "["+'"'+"k"+'"'+"]").asList().at(0).asDouble();
			dbpars[2] = c.eval(tempar + "["+'"'+"Tmax"+'"'+"]").asList().at(0).asDouble();
			dbpars[3] = c.eval(tempar + "["+'"'+"p"+'"'+"]").asList().at(0).asDouble();
			dbpars[4] = c.eval(tempar + "["+'"'+"v"+'"'+"]").asList().at(0).asDouble();
			
			dbStdpars[0] = c.eval("Std.Error").asDoubles()[0];
			dbStdpars[1] = c.eval("Std.Error").asDoubles()[1];
			dbStdpars[2] = c.eval("Std.Error").asDoubles()[2];
			dbStdpars[3] = c.eval("Std.Error").asDoubles()[3];
			dbStdpars[4] = c.eval("Std.Error").asDoubles()[4];
			
			parsName[0] = "alfa";
			parsName[1] = "k";
			parsName[2] = "Tmax";
			parsName[3] = "p";
			parsName[4] = "v";
			break;
		case 19:/**Briere 1**/
			dbpars[0] = c.eval(tempar + "["+'"'+"aa"+'"'+"]").asList().at(0).asDouble();
			dbpars[1] = c.eval(tempar + "["+'"'+"To"+'"'+"]").asList().at(0).asDouble();
			dbpars[2] = c.eval(tempar + "["+'"'+"Tmax"+'"'+"]").asList().at(0).asDouble();
			
			dbStdpars[0] = c.eval("Std.Error").asDoubles()[0];
			dbStdpars[1] = c.eval("Std.Error").asDoubles()[1];
			dbStdpars[2] = c.eval("Std.Error").asDoubles()[2];
			
			parsName[0] = "aa";
			parsName[1] = "To";
			parsName[2] = "Tmax";
			break;
		case 20:/**Briere 2**/
			dbpars[0] = c.eval(tempar + "["+'"'+"aa"+'"'+"]").asList().at(0).asDouble();
			dbpars[1] = c.eval(tempar + "["+'"'+"To"+'"'+"]").asList().at(0).asDouble();
			dbpars[2] = c.eval(tempar + "["+'"'+"Tmax"+'"'+"]").asList().at(0).asDouble();
			dbpars[3] = c.eval(tempar + "["+'"'+"d"+'"'+"]").asList().at(0).asDouble();
			
			dbStdpars[0] = c.eval("Std.Error").asDoubles()[0];
			dbStdpars[1] = c.eval("Std.Error").asDoubles()[1];
			dbStdpars[2] = c.eval("Std.Error").asDoubles()[2];
			dbStdpars[3] = c.eval("Std.Error").asDoubles()[3];
			
			parsName[0] = "aa";
			parsName[1] = "To";
			parsName[2] = "Tmax";
			parsName[3] = "d";
			break;
		case 21:/**Stinner 1**/
			dbpars[0] = c.eval(tempar + "["+'"'+"Rmax"+'"'+"]").asList().at(0).asDouble();
			dbpars[1] = c.eval(tempar + "["+'"'+"Topc"+'"'+"]").asList().at(0).asDouble();
			dbpars[2] = c.eval(tempar + "["+'"'+"k1"+'"'+"]").asList().at(0).asDouble();
			dbpars[3] = c.eval(tempar + "["+'"'+"k2"+'"'+"]").asList().at(0).asDouble();
			
			dbStdpars[0] = c.eval("Std.Error").asDoubles()[0];
			dbStdpars[1] = c.eval("Std.Error").asDoubles()[1];
			dbStdpars[2] = c.eval("Std.Error").asDoubles()[2];
			dbStdpars[3] = c.eval("Std.Error").asDoubles()[3];
			
			parsName[0] = "Rmax";
			parsName[1] = "Topc";
			parsName[2] = "k1";
			parsName[3] = "k2";
			break;
		case 22:/**Hilbert y Logan**/
			dbpars[0] = c.eval(tempar + "["+'"'+"d"+'"'+"]").asList().at(0).asDouble();
			dbpars[1] = c.eval(tempar + "["+'"'+"Y"+'"'+"]").asList().at(0).asDouble();
			dbpars[2] = c.eval(tempar + "["+'"'+"Tmax"+'"'+"]").asList().at(0).asDouble();
			dbpars[3] = c.eval(tempar + "["+'"'+"v"+'"'+"]").asList().at(0).asDouble();
			
			dbStdpars[0] = c.eval("Std.Error").asDoubles()[0];
			dbStdpars[1] = c.eval("Std.Error").asDoubles()[1];
			dbStdpars[2] = c.eval("Std.Error").asDoubles()[2];
			dbStdpars[3] = c.eval("Std.Error").asDoubles()[3];
			
			parsName[0] = "d";
			parsName[1] = "Y";
			parsName[2] = "Tmax";
			parsName[3] = "v";
			break;
		case 23:/**Lactin**/
			dbpars[0] = c.eval(tempar + "["+'"'+"Tl"+'"'+"]").asList().at(0).asDouble();
			dbpars[1] = c.eval(tempar + "["+'"'+"p"+'"'+"]").asList().at(0).asDouble();
			dbpars[2] = c.eval(tempar + "["+'"'+"dt"+'"'+"]").asList().at(0).asDouble();
			dbpars[3] = c.eval(tempar + "["+'"'+"L"+'"'+"]").asList().at(0).asDouble();
			
			dbStdpars[0] = c.eval("Std.Error").asDoubles()[0];
			dbStdpars[1] = c.eval("Std.Error").asDoubles()[1];
			dbStdpars[2] = c.eval("Std.Error").asDoubles()[2];
			dbStdpars[3] = c.eval("Std.Error").asDoubles()[3];
			
			parsName[0] = "Tl";
			parsName[1] = "p";
			parsName[2] = "dt";
			parsName[3] = "L";
			break;
		case 24:/** linear **/
			dbpars[0] = c.eval(tempar + "["+'"'+"Inter"+'"'+"]").asList().at(0).asDouble();
			dbpars[1] = c.eval(tempar + "["+'"'+"Slop"+'"'+"]").asList().at(0).asDouble();
			
			dbStdpars[0] = c.eval("Std.Error").asDoubles()[0];
			dbStdpars[1] = c.eval("Std.Error").asDoubles()[1];
			
			parsName[0] = "Inter";
			parsName[1] = "Slop";
			break;
		case 25:/** exponential simple **/
			dbpars[0] = c.eval(tempar + "["+'"'+"b1"+'"'+"]").asList().at(0).asDouble();
			dbpars[1] = c.eval(tempar + "["+'"'+"b2"+'"'+"]").asList().at(0).asDouble();

			dbStdpars[0] = c.eval("Std.Error").asDoubles()[0];
			dbStdpars[1] = c.eval("Std.Error").asDoubles()[1];
			
			parsName[0] = "b1";
			parsName[1] = "b2";
			break;
		case 26:/** Tb Model (Logan) **/
			dbpars[0] = c.eval(tempar + "["+'"'+"sy"+'"'+"]").asList().at(0).asDouble();
			dbpars[1] = c.eval(tempar + "["+'"'+"b"+'"'+"]").asList().at(0).asDouble();
			dbpars[2] = c.eval(tempar + "["+'"'+"Tb"+'"'+"]").asList().at(0).asDouble();
			dbpars[3] = c.eval(tempar + "["+'"'+"DTb"+'"'+"]").asList().at(0).asDouble();

			dbStdpars[0] = c.eval("Std.Error").asDoubles()[0];
			dbStdpars[1] = c.eval("Std.Error").asDoubles()[1];
			dbStdpars[2] = c.eval("Std.Error").asDoubles()[2];
			dbStdpars[3] = c.eval("Std.Error").asDoubles()[3];
			
			parsName[0] = "sy";
			parsName[1] = "b";
			parsName[2] = "Tb";
			parsName[3] = "DTb";
			break;
		case 27:/** Exponential Model (Logan) **/
			dbpars[0] = c.eval(tempar + "["+'"'+"sy"+'"'+"]").asList().at(0).asDouble();
			dbpars[1] = c.eval(tempar + "["+'"'+"b"+'"'+"]").asList().at(0).asDouble();
			dbpars[2] = c.eval(tempar + "["+'"'+"Tb"+'"'+"]").asList().at(0).asDouble();
			
			dbStdpars[0] = c.eval("Std.Error").asDoubles()[0];
			dbStdpars[1] = c.eval("Std.Error").asDoubles()[1];
			dbStdpars[2] = c.eval("Std.Error").asDoubles()[2];
			
			parsName[0] = "sy";
			parsName[1] = "b";
			parsName[2] = "Tb";
			break;
		case 28:/** Exponential Tb (Logan) **/
			dbpars[0] = c.eval(tempar + "["+'"'+"b"+'"'+"]").asList().at(0).asDouble();
			dbpars[1] = c.eval(tempar + "["+'"'+"Tmin"+'"'+"]").asList().at(0).asDouble();
			
			dbStdpars[0] = c.eval("Std.Error").asDoubles()[0];
			dbStdpars[1] = c.eval("Std.Error").asDoubles()[1];
			
			parsName[0] = "b";
			parsName[1] = "Tmin";
			break;
		case 29:/** Square root model of Ratkowsky **/
			dbpars[0] = c.eval(tempar + "["+'"'+"b"+'"'+"]").asList().at(0).asDouble();
			dbpars[1] = c.eval(tempar + "["+'"'+"Tb"+'"'+"]").asList().at(0).asDouble();
			
			dbStdpars[0] = c.eval("Std.Error").asDoubles()[0];
			dbStdpars[1] = c.eval("Std.Error").asDoubles()[1];
			
			parsName[0] = "b";
			parsName[1] = "Tb";
			break;
		case 30:/**Davidson **/
			dbpars[0] = c.eval(tempar + "["+'"'+"k"+'"'+"]").asList().at(0).asDouble();
			dbpars[1] = c.eval(tempar + "["+'"'+"a"+'"'+"]").asList().at(0).asDouble();
			dbpars[2] = c.eval(tempar + "["+'"'+"b"+'"'+"]").asList().at(0).asDouble();
			
			dbStdpars[0] = c.eval("Std.Error").asDoubles()[0];
			dbStdpars[1] = c.eval("Std.Error").asDoubles()[1];
			dbStdpars[2] = c.eval("Std.Error").asDoubles()[2];
			
			parsName[0] = "k";
			parsName[1] = "a";
			parsName[2] = "b";
			break;
		case 31:/** Pradham **/
			dbpars[0] = c.eval(tempar + "["+'"'+"R"+'"'+"]").asList().at(0).asDouble();
			dbpars[1] = c.eval(tempar + "["+'"'+"Tm"+'"'+"]").asList().at(0).asDouble();
			dbpars[2] = c.eval(tempar + "["+'"'+"To"+'"'+"]").asList().at(0).asDouble();

			dbStdpars[0] = c.eval("Std.Error").asDoubles()[0];
			dbStdpars[1] = c.eval("Std.Error").asDoubles()[1];
			dbStdpars[2] = c.eval("Std.Error").asDoubles()[2];
			
			parsName[0] = "R";
			parsName[1] = "Tm";
			parsName[2] = "To";
			break;
		case 32:/** Angilletta Jr. **/
			dbpars[0] = c.eval(tempar + "["+'"'+"a"+'"'+"]").asList().at(0).asDouble();
			dbpars[1] = c.eval(tempar + "["+'"'+"b"+'"'+"]").asList().at(0).asDouble();
			dbpars[2] = c.eval(tempar + "["+'"'+"c"+'"'+"]").asList().at(0).asDouble();
			dbpars[3] = c.eval(tempar + "["+'"'+"d"+'"'+"]").asList().at(0).asDouble();

			dbStdpars[0] = c.eval("Std.Error").asDoubles()[0];
			dbStdpars[1] = c.eval("Std.Error").asDoubles()[1];
			dbStdpars[2] = c.eval("Std.Error").asDoubles()[2];
			dbStdpars[3] = c.eval("Std.Error").asDoubles()[3];
			
			parsName[0] = "a";
			parsName[1] = "b";
			parsName[2] = "c";
			parsName[3] = "d";
			break;
		case 33:/** Stinner 2 **/
			dbpars[0] = c.eval(tempar + "["+'"'+"Rmax"+'"'+"]").asList().at(0).asDouble();
			dbpars[1] = c.eval(tempar + "["+'"'+"k1"+'"'+"]").asList().at(0).asDouble();
			dbpars[2] = c.eval(tempar + "["+'"'+"k2"+'"'+"]").asList().at(0).asDouble();
			dbpars[3] = c.eval(tempar + "["+'"'+"Topc"+'"'+"]").asList().at(0).asDouble();
			
			dbStdpars[0] = c.eval("Std.Error").asDoubles()[0];
			dbStdpars[1] = c.eval("Std.Error").asDoubles()[1];
			dbStdpars[2] = c.eval("Std.Error").asDoubles()[2];
			dbStdpars[3] = c.eval("Std.Error").asDoubles()[3];
			
			parsName[0] = "Rmax";
			parsName[1] = "k1";
			parsName[2] = "k2";
			parsName[3] = "Topc";
			break;
		case 34:/** Hilbert **/
			dbpars[0] = c.eval(tempar + "["+'"'+"Tb"+'"'+"]").asList().at(0).asDouble();
			dbpars[1] = c.eval(tempar + "["+'"'+"Tmax"+'"'+"]").asList().at(0).asDouble();
			dbpars[2] = c.eval(tempar + "["+'"'+"d"+'"'+"]").asList().at(0).asDouble();
			dbpars[3] = c.eval(tempar + "["+'"'+"Y"+'"'+"]").asList().at(0).asDouble();
			dbpars[4] = c.eval(tempar + "["+'"'+"v"+'"'+"]").asList().at(0).asDouble();
			
			dbStdpars[0] = c.eval("Std.Error").asDoubles()[0];
			dbStdpars[1] = c.eval("Std.Error").asDoubles()[1];
			dbStdpars[2] = c.eval("Std.Error").asDoubles()[2];
			dbStdpars[3] = c.eval("Std.Error").asDoubles()[3];
			dbStdpars[4] = c.eval("Std.Error").asDoubles()[4];
			
			parsName[0] = "Tb";
			parsName[1] = "Tmax";
			parsName[2] = "d";
			parsName[3] = "Y";
			parsName[4] = "v";
			break;
		case 35:/** Lactin 2 **/
			dbpars[0] = c.eval(tempar + "["+'"'+"Tl"+'"'+"]").asList().at(0).asDouble();
			dbpars[1] = c.eval(tempar + "["+'"'+"p"+'"'+"]").asList().at(0).asDouble();
			dbpars[2] = c.eval(tempar + "["+'"'+"dt"+'"'+"]").asList().at(0).asDouble();
			
			dbStdpars[0] = c.eval("Std.Error").asDoubles()[0];
			dbStdpars[1] = c.eval("Std.Error").asDoubles()[1];
			dbStdpars[2] = c.eval("Std.Error").asDoubles()[2];
			
			parsName[0] = "Tl";
			parsName[1] = "p";
			parsName[2] = "dt";
			break;
		case 36:/** Anlytis-1 **/
			dbpars[0] = c.eval(tempar + "["+'"'+"P"+'"'+"]").asList().at(0).asDouble();
			dbpars[1] = c.eval(tempar + "["+'"'+"Tmax"+'"'+"]").asList().at(0).asDouble();
			dbpars[2] = c.eval(tempar + "["+'"'+"Tmin"+'"'+"]").asList().at(0).asDouble();
			dbpars[3] = c.eval(tempar + "["+'"'+"n"+'"'+"]").asList().at(0).asDouble();
			dbpars[4] = c.eval(tempar + "["+'"'+"m"+'"'+"]").asList().at(0).asDouble();
			
			dbStdpars[0] = c.eval("Std.Error").asDoubles()[0];
			dbStdpars[1] = c.eval("Std.Error").asDoubles()[1];
			dbStdpars[2] = c.eval("Std.Error").asDoubles()[2];
			dbStdpars[3] = c.eval("Std.Error").asDoubles()[3];
			dbStdpars[4] = c.eval("Std.Error").asDoubles()[4];
			
			parsName[0] = "P";
			parsName[1] = "Tmax";
			parsName[2] = "Tmin";
			parsName[3] = "n";
			parsName[4] = "m";
			break;
		case 37:/** Anlytis-2 **/
			dbpars[0] = c.eval(tempar + "["+'"'+"P"+'"'+"]").asList().at(0).asDouble();
			dbpars[1] = c.eval(tempar + "["+'"'+"Tmax"+'"'+"]").asList().at(0).asDouble();
			dbpars[2] = c.eval(tempar + "["+'"'+"Tmin"+'"'+"]").asList().at(0).asDouble();
			dbpars[3] = c.eval(tempar + "["+'"'+"n"+'"'+"]").asList().at(0).asDouble();
			dbpars[4] = c.eval(tempar + "["+'"'+"m"+'"'+"]").asList().at(0).asDouble();
			
			dbStdpars[0] = c.eval("Std.Error").asDoubles()[0];
			dbStdpars[1] = c.eval("Std.Error").asDoubles()[1];
			dbStdpars[2] = c.eval("Std.Error").asDoubles()[2];
			dbStdpars[3] = c.eval("Std.Error").asDoubles()[3];
			dbStdpars[4] = c.eval("Std.Error").asDoubles()[4];
			
			parsName[0] = "P";
			parsName[1] = "Tmax";
			parsName[2] = "Tmin";
			parsName[3] = "n";
			parsName[4] = "m";
			break;
		case 38:/** Anlytis-3 **/
			dbpars[0] = c.eval(tempar + "["+'"'+"a"+'"'+"]").asList().at(0).asDouble();
			dbpars[1] = c.eval(tempar + "["+'"'+"Tmax"+'"'+"]").asList().at(0).asDouble();
			dbpars[2] = c.eval(tempar + "["+'"'+"Tmin"+'"'+"]").asList().at(0).asDouble();
			dbpars[3] = c.eval(tempar + "["+'"'+"n"+'"'+"]").asList().at(0).asDouble();
			dbpars[4] = c.eval(tempar + "["+'"'+"m"+'"'+"]").asList().at(0).asDouble();
			
			dbStdpars[0] = c.eval("Std.Error").asDoubles()[0];
			dbStdpars[1] = c.eval("Std.Error").asDoubles()[1];
			dbStdpars[2] = c.eval("Std.Error").asDoubles()[2];
			dbStdpars[3] = c.eval("Std.Error").asDoubles()[3];
			dbStdpars[4] = c.eval("Std.Error").asDoubles()[4];
			
			parsName[0] = "a";
			parsName[1] = "Tmax";
			parsName[2] = "Tmin";
			parsName[3] = "n";
			parsName[4] = "m";
			break;
		case 39:/** Allahyari **/
			dbpars[0] = c.eval(tempar + "["+'"'+"P"+'"'+"]").asList().at(0).asDouble();
			dbpars[1] = c.eval(tempar + "["+'"'+"Tmax"+'"'+"]").asList().at(0).asDouble();
			dbpars[2] = c.eval(tempar + "["+'"'+"Tmin"+'"'+"]").asList().at(0).asDouble();
			dbpars[3] = c.eval(tempar + "["+'"'+"n"+'"'+"]").asList().at(0).asDouble();
			dbpars[4] = c.eval(tempar + "["+'"'+"m"+'"'+"]").asList().at(0).asDouble();
			
			dbStdpars[0] = c.eval("Std.Error").asDoubles()[0];
			dbStdpars[1] = c.eval("Std.Error").asDoubles()[1];
			dbStdpars[2] = c.eval("Std.Error").asDoubles()[2];
			dbStdpars[3] = c.eval("Std.Error").asDoubles()[3];
			dbStdpars[4] = c.eval("Std.Error").asDoubles()[4];
			
			parsName[0] = "P";
			parsName[1] = "Tmax";
			parsName[2] = "Tmin";
			parsName[3] = "n";
			parsName[4] = "m";
			break;
		case 40:/** Briere 3 **/
			dbpars[0] = c.eval(tempar + "["+'"'+"aa"+'"'+"]").asList().at(0).asDouble();
			dbpars[1] = c.eval(tempar + "["+'"'+"To"+'"'+"]").asList().at(0).asDouble();
			dbpars[2] = c.eval(tempar + "["+'"'+"Tmax"+'"'+"]").asList().at(0).asDouble();

			dbStdpars[0] = c.eval("Std.Error").asDoubles()[0];
			dbStdpars[1] = c.eval("Std.Error").asDoubles()[1];
			dbStdpars[2] = c.eval("Std.Error").asDoubles()[2];
			
			parsName[0] = "aa";
			parsName[1] = "To";
			parsName[2] = "Tmax";
			break;
		case 41:/** Briere 4 **/
			dbpars[0] = c.eval(tempar + "["+'"'+"aa"+'"'+"]").asList().at(0).asDouble();
			dbpars[1] = c.eval(tempar + "["+'"'+"To"+'"'+"]").asList().at(0).asDouble();
			dbpars[2] = c.eval(tempar + "["+'"'+"Tmax"+'"'+"]").asList().at(0).asDouble();
			dbpars[3] = c.eval(tempar + "["+'"'+"n"+'"'+"]").asList().at(0).asDouble();
			
			dbStdpars[0] = c.eval("Std.Error").asDoubles()[0];
			dbStdpars[1] = c.eval("Std.Error").asDoubles()[1];
			dbStdpars[2] = c.eval("Std.Error").asDoubles()[2];
			dbStdpars[3] = c.eval("Std.Error").asDoubles()[3];
			
			parsName[0] = "aa";
			parsName[1] = "To";
			parsName[2] = "Tmax";
			parsName[3] = "n";
			break;
		case 42:/** Kontodimas-1 **/
			dbpars[0] = c.eval(tempar + "["+'"'+"aa"+'"'+"]").asList().at(0).asDouble();
			dbpars[1] = c.eval(tempar + "["+'"'+"Tmin"+'"'+"]").asList().at(0).asDouble();
			dbpars[2] = c.eval(tempar + "["+'"'+"Tmax"+'"'+"]").asList().at(0).asDouble();
			
			dbStdpars[0] = c.eval("Std.Error").asDoubles()[0];
			dbStdpars[1] = c.eval("Std.Error").asDoubles()[1];
			dbStdpars[2] = c.eval("Std.Error").asDoubles()[2];
			
			parsName[0] = "aa";
			parsName[1] = "Tmin";
			parsName[2] = "Tmax";
			break;
		case 43:/** Kontodimas-2 **/
			dbpars[0] = c.eval(tempar + "["+'"'+"Dmin"+'"'+"]").asList().at(0).asDouble();
			dbpars[1] = c.eval(tempar + "["+'"'+"Topt"+'"'+"]").asList().at(0).asDouble();
			dbpars[2] = c.eval(tempar + "["+'"'+"K"+'"'+"]").asList().at(0).asDouble();
			dbpars[3] = c.eval(tempar + "["+'"'+"lmda"+'"'+"]").asList().at(0).asDouble();
			
			dbStdpars[0] = c.eval("Std.Error").asDoubles()[0];
			dbStdpars[1] = c.eval("Std.Error").asDoubles()[1];
			dbStdpars[2] = c.eval("Std.Error").asDoubles()[2];
			dbStdpars[3] = c.eval("Std.Error").asDoubles()[3];
			
			parsName[0] = "Dmin";
			parsName[1] = "Topt";
			parsName[2] = "K";
			parsName[3] = "lmda";
			break;
		case 44:/** Kontodimas-3 **/
			dbpars[0] = c.eval(tempar + "["+'"'+"a1"+'"'+"]").asList().at(0).asDouble();
			dbpars[1] = c.eval(tempar + "["+'"'+"b1"+'"'+"]").asList().at(0).asDouble();
			dbpars[2] = c.eval(tempar + "["+'"'+"c1"+'"'+"]").asList().at(0).asDouble();
			dbpars[3] = c.eval(tempar + "["+'"'+"d1"+'"'+"]").asList().at(0).asDouble();
			dbpars[4] = c.eval(tempar + "["+'"'+"f1"+'"'+"]").asList().at(0).asDouble();
			dbpars[5] = c.eval(tempar + "["+'"'+"g1"+'"'+"]").asList().at(0).asDouble();
			
			dbStdpars[0] = c.eval("Std.Error").asDoubles()[0];
			dbStdpars[1] = c.eval("Std.Error").asDoubles()[1];
			dbStdpars[2] = c.eval("Std.Error").asDoubles()[2];
			dbStdpars[3] = c.eval("Std.Error").asDoubles()[3];
			dbStdpars[4] = c.eval("Std.Error").asDoubles()[4];
			dbStdpars[5] = c.eval("Std.Error").asDoubles()[5];
			
			parsName[0] = "a1";
			parsName[1] = "b1";
			parsName[2] = "c1";
			parsName[3] = "d1";
			parsName[4] = "f1";
			parsName[5] = "g1";
			break;
		case 45:/** Ratkowsky 2 **/
			dbpars[0] = c.eval(tempar + "["+'"'+"aa"+'"'+"]").asList().at(0).asDouble();
			dbpars[1] = c.eval(tempar + "["+'"'+"Tmin"+'"'+"]").asList().at(0).asDouble();
			dbpars[2] = c.eval(tempar + "["+'"'+"Tmax"+'"'+"]").asList().at(0).asDouble();
			dbpars[3] = c.eval(tempar + "["+'"'+"b"+'"'+"]").asList().at(0).asDouble();
			
			dbStdpars[0] = c.eval("Std.Error").asDoubles()[0];
			dbStdpars[1] = c.eval("Std.Error").asDoubles()[1];
			dbStdpars[2] = c.eval("Std.Error").asDoubles()[2];
			dbStdpars[3] = c.eval("Std.Error").asDoubles()[3];
			
			parsName[0] = "aa";
			parsName[1] = "Tmin";
			parsName[2] = "Tmax";
			parsName[3] = "b";
			break;
		case 46:/** Janish-1 **/
			dbpars[0] = c.eval(tempar + "["+'"'+"Dmin"+'"'+"]").asList().at(0).asDouble();
			dbpars[1] = c.eval(tempar + "["+'"'+"Topt"+'"'+"]").asList().at(0).asDouble();
			dbpars[2] = c.eval(tempar + "["+'"'+"K"+'"'+"]").asList().at(0).asDouble();
			
			dbStdpars[0] = c.eval("Std.Error").asDoubles()[0];
			dbStdpars[1] = c.eval("Std.Error").asDoubles()[1];
			dbStdpars[2] = c.eval("Std.Error").asDoubles()[2];
			
			parsName[0] = "Dmin";
			parsName[1] = "Topt";
			parsName[2] = "K";
			break;
		case 47:/** Janish-2 **/
			dbpars[0] = c.eval(tempar + "["+'"'+"c"+'"'+"]").asList().at(0).asDouble();
			dbpars[1] = c.eval(tempar + "["+'"'+"a"+'"'+"]").asList().at(0).asDouble();
			dbpars[2] = c.eval(tempar + "["+'"'+"b"+'"'+"]").asList().at(0).asDouble();
			dbpars[3] = c.eval(tempar + "["+'"'+"Tm"+'"'+"]").asList().at(0).asDouble();
			
			dbStdpars[0] = c.eval("Std.Error").asDoubles()[0];
			dbStdpars[1] = c.eval("Std.Error").asDoubles()[1];
			dbStdpars[2] = c.eval("Std.Error").asDoubles()[2];
			dbStdpars[3] = c.eval("Std.Error").asDoubles()[3];
			
			parsName[0] = "c";
			parsName[1] = "a";
			parsName[2] = "b";
			parsName[3] = "Tm";
			break;
		case 48:/** Tanigoshi **/
			dbpars[0] = c.eval(tempar + "["+'"'+"a0"+'"'+"]").asList().at(0).asDouble();
			dbpars[1] = c.eval(tempar + "["+'"'+"a1"+'"'+"]").asList().at(0).asDouble();
			dbpars[2] = c.eval(tempar + "["+'"'+"a2"+'"'+"]").asList().at(0).asDouble();
			dbpars[3] = c.eval(tempar + "["+'"'+"a3"+'"'+"]").asList().at(0).asDouble();
			
			dbStdpars[0] = c.eval("Std.Error").asDoubles()[0];
			dbStdpars[1] = c.eval("Std.Error").asDoubles()[1];
			dbStdpars[2] = c.eval("Std.Error").asDoubles()[2];
			dbStdpars[3] = c.eval("Std.Error").asDoubles()[3];
			
			parsName[0] = "a0";
			parsName[1] = "a1";
			parsName[2] = "a2";
			parsName[3] = "a3";
			break;
		case 49:/** Wang-Lan-Ding **/
			dbpars[0] = c.eval(tempar + "["+'"'+"k"+'"'+"]").asList().at(0).asDouble();
			dbpars[1] = c.eval(tempar + "["+'"'+"a"+'"'+"]").asList().at(0).asDouble();
			dbpars[2] = c.eval(tempar + "["+'"'+"b"+'"'+"]").asList().at(0).asDouble();
			dbpars[3] = c.eval(tempar + "["+'"'+"c"+'"'+"]").asList().at(0).asDouble();
			dbpars[4] = c.eval(tempar + "["+'"'+"Tmin"+'"'+"]").asList().at(0).asDouble();
			dbpars[5] = c.eval(tempar + "["+'"'+"Tmax"+'"'+"]").asList().at(0).asDouble();
			dbpars[6] = c.eval(tempar + "["+'"'+"r"+'"'+"]").asList().at(0).asDouble();
			
			dbStdpars[0] = c.eval("Std.Error").asDoubles()[0];
			dbStdpars[1] = c.eval("Std.Error").asDoubles()[1];
			dbStdpars[2] = c.eval("Std.Error").asDoubles()[2];
			dbStdpars[3] = c.eval("Std.Error").asDoubles()[3];
			dbStdpars[4] = c.eval("Std.Error").asDoubles()[4];
			dbStdpars[5] = c.eval("Std.Error").asDoubles()[5];
			dbStdpars[6] = c.eval("Std.Error").asDoubles()[6];
			
			parsName[0] = "k";
			parsName[1] = "a";
			parsName[2] = "b";
			parsName[3] = "c";
			parsName[4] = "Tmin";
			parsName[5] = "Tmax";
			parsName[6] = "r";
			break;
		case 50:
			dbpars[0] = c.eval(tempar + "["+'"'+"c1"+'"'+"]").asList().at(0).asDouble();
			dbpars[1] = c.eval(tempar + "["+'"'+"k1"+'"'+"]").asList().at(0).asDouble();
			dbpars[2] = c.eval(tempar + "["+'"'+"k2"+'"'+"]").asList().at(0).asDouble();
			
			dbStdpars[0] = c.eval("Std.Error").asDoubles()[0];
			dbStdpars[1] = c.eval("Std.Error").asDoubles()[1];
			dbStdpars[2] = c.eval("Std.Error").asDoubles()[2];
			
			parsName[0] = "c1";
			parsName[1] = "k1";
			parsName[2] = "k2";
			break;
		case 51:
			dbpars[0] = c.eval(tempar + "["+'"'+"c1"+'"'+"]").asList().at(0).asDouble();
			dbpars[1] = c.eval(tempar + "["+'"'+"c2"+'"'+"]").asList().at(0).asDouble();
			dbpars[2] = c.eval(tempar + "["+'"'+"k1"+'"'+"]").asList().at(0).asDouble();
			dbpars[3] = c.eval(tempar + "["+'"'+"k2"+'"'+"]").asList().at(0).asDouble();
			dbpars[4] = c.eval(tempar + "["+'"'+"To"+'"'+"]").asList().at(0).asDouble();
			
			dbStdpars[0] = c.eval("Std.Error").asDoubles()[0];
			dbStdpars[1] = c.eval("Std.Error").asDoubles()[1];
			dbStdpars[2] = c.eval("Std.Error").asDoubles()[2];
			dbStdpars[3] = c.eval("Std.Error").asDoubles()[3];
			dbStdpars[4] = c.eval("Std.Error").asDoubles()[4];
			
			parsName[0] = "c1";
			parsName[1] = "c2";
			parsName[2] = "k1";
			parsName[3] = "k2";
			parsName[4] = "To";
			break;
		case 52:
			dbpars[0] = c.eval(tempar + "["+'"'+"sy"+'"'+"]").asList().at(0).asDouble();
			dbpars[1] = c.eval(tempar + "["+'"'+"b"+'"'+"]").asList().at(0).asDouble();
			dbpars[2] = c.eval(tempar + "["+'"'+"Tmin"+'"'+"]").asList().at(0).asDouble();
			dbpars[3] = c.eval(tempar + "["+'"'+"Tmax"+'"'+"]").asList().at(0).asDouble();
			dbpars[4] = c.eval(tempar + "["+'"'+"DTb"+'"'+"]").asList().at(0).asDouble();
			
			dbStdpars[0] = c.eval("Std.Error").asDoubles()[0];
			dbStdpars[1] = c.eval("Std.Error").asDoubles()[1];
			dbStdpars[2] = c.eval("Std.Error").asDoubles()[2];
			dbStdpars[3] = c.eval("Std.Error").asDoubles()[3];
			dbStdpars[4] = c.eval("Std.Error").asDoubles()[4];
			
			parsName[0] = "sy";
			parsName[1] = "b";
			parsName[2] = "Tmin";
			parsName[3] = "Tmax";
			parsName[4] = "DTb";
			break;
		case 53:
			dbpars[0] = c.eval(tempar + "["+'"'+"alph"+'"'+"]").asList().at(0).asDouble();
			dbpars[1] = c.eval(tempar + "["+'"'+"k"+'"'+"]").asList().at(0).asDouble();
			dbpars[2] = c.eval(tempar + "["+'"'+"b"+'"'+"]").asList().at(0).asDouble();
			dbpars[3] = c.eval(tempar + "["+'"'+"Tmin"+'"'+"]").asList().at(0).asDouble();
			dbpars[4] = c.eval(tempar + "["+'"'+"Tmax"+'"'+"]").asList().at(0).asDouble();
			dbpars[5] = c.eval(tempar + "["+'"'+"Dt"+'"'+"]").asList().at(0).asDouble();
			
			dbStdpars[0] = c.eval("Std.Error").asDoubles()[0];
			dbStdpars[1] = c.eval("Std.Error").asDoubles()[1];
			dbStdpars[2] = c.eval("Std.Error").asDoubles()[2];
			dbStdpars[3] = c.eval("Std.Error").asDoubles()[3];
			dbStdpars[4] = c.eval("Std.Error").asDoubles()[4];
			dbStdpars[5] = c.eval("Std.Error").asDoubles()[5];
			
			parsName[0] = "alph";
			parsName[1] = "k";
			parsName[2] = "b";
			parsName[3] = "Tmin";
			parsName[4] = "Tmax";
			parsName[5] = "Dt";
			break;
		case 54:
			dbpars[0] = c.eval(tempar + "["+'"'+"alph"+'"'+"]").asList().at(0).asDouble();
			dbpars[1] = c.eval(tempar + "["+'"'+"k"+'"'+"]").asList().at(0).asDouble();
			dbpars[2] = c.eval(tempar + "["+'"'+"b"+'"'+"]").asList().at(0).asDouble();
			dbpars[3] = c.eval(tempar + "["+'"'+"Tmax"+'"'+"]").asList().at(0).asDouble();
			dbpars[4] = c.eval(tempar + "["+'"'+"Dt"+'"'+"]").asList().at(0).asDouble();
			
			dbStdpars[0] = c.eval("Std.Error").asDoubles()[0];
			dbStdpars[1] = c.eval("Std.Error").asDoubles()[1];
			dbStdpars[2] = c.eval("Std.Error").asDoubles()[2];
			dbStdpars[3] = c.eval("Std.Error").asDoubles()[3];
			dbStdpars[4] = c.eval("Std.Error").asDoubles()[4];
			
			parsName[0] = "alph";
			parsName[1] = "k";
			parsName[2] = "b";
			parsName[3] = "Tmax";
			parsName[4] = "Dt";
			break;
		case 55:
			dbpars[0] = c.eval(tempar + "["+'"'+"trid"+'"'+"]").asList().at(0).asDouble();
			dbpars[1] = c.eval(tempar + "["+'"'+"D"+'"'+"]").asList().at(0).asDouble();
			dbpars[2] = c.eval(tempar + "["+'"'+"Tmax"+'"'+"]").asList().at(0).asDouble();
			dbpars[3] = c.eval(tempar + "["+'"'+"Dt"+'"'+"]").asList().at(0).asDouble();
			
			dbStdpars[0] = c.eval("Std.Error").asDoubles()[0];
			dbStdpars[1] = c.eval("Std.Error").asDoubles()[1];
			dbStdpars[2] = c.eval("Std.Error").asDoubles()[2];
			dbStdpars[3] = c.eval("Std.Error").asDoubles()[3];
			
			parsName[0] = "trid";
			parsName[1] = "D";
			parsName[2] = "Tmax";
			parsName[3] = "Dt";
			break;
		case 56:
			dbpars[0] = c.eval(tempar+ "["+'"'+"trid"+'"'+"]").asList().at(0).asDouble();
			dbpars[1] = c.eval(tempar + "["+'"'+"Tmax"+'"'+"]").asList().at(0).asDouble();
			dbpars[2] = c.eval(tempar + "["+'"'+"Tmin"+'"'+"]").asList().at(0).asDouble();
			dbpars[3] = c.eval(tempar + "["+'"'+"D"+'"'+"]").asList().at(0).asDouble();
			dbpars[4] = c.eval(tempar + "["+'"'+"Dt"+'"'+"]").asList().at(0).asDouble();
			dbpars[5] = c.eval(tempar + "["+'"'+"Smin"+'"'+"]").asList().at(0).asDouble();
			
			dbStdpars[0] = c.eval("Std.Error").asDoubles()[0];
			dbStdpars[1] = c.eval("Std.Error").asDoubles()[1];
			dbStdpars[2] = c.eval("Std.Error").asDoubles()[2];
			dbStdpars[3] = c.eval("Std.Error").asDoubles()[3];
			dbStdpars[4] = c.eval("Std.Error").asDoubles()[4];
			dbStdpars[5] = c.eval("Std.Error").asDoubles()[5];
			
			parsName[0] = "trid";
			parsName[1] = "Tmax";
			parsName[2] = "Tmin";
			parsName[3] = "D";
			parsName[4] = "Dt";
			parsName[5] = "Smin";
			break;
		case 57:
			dbpars[0] = c.eval(tempar + "["+'"'+"rm"+'"'+"]").asList().at(0).asDouble();
			dbpars[1] = c.eval(tempar + "["+'"'+"Topt"+'"'+"]").asList().at(0).asDouble();
			dbpars[2] = c.eval(tempar + "["+'"'+"Troh"+'"'+"]").asList().at(0).asDouble();
			dbpars[3] = c.eval(tempar + "["+'"'+"Smin"+'"'+"]").asList().at(0).asDouble();
			
			dbStdpars[0] = c.eval("Std.Error").asDoubles()[0];
			dbStdpars[1] = c.eval("Std.Error").asDoubles()[1];
			dbStdpars[2] = c.eval("Std.Error").asDoubles()[2];
			dbStdpars[3] = c.eval("Std.Error").asDoubles()[3];
			
			parsName[0] = "rm";
			parsName[1] = "Topt";
			parsName[2] = "Troh";
			parsName[3] = "Smin";
			break;
		case 58:
			dbpars[0] = c.eval(tempar + "["+'"'+"p"+'"'+"]").asList().at(0).asDouble();
			dbpars[1] = c.eval(tempar + "["+'"'+"Tl"+'"'+"]").asList().at(0).asDouble();
			dbpars[2] = c.eval(tempar + "["+'"'+"dt"+'"'+"]").asList().at(0).asDouble();
			dbpars[3] = c.eval(tempar + "["+'"'+"lamb"+'"'+"]").asList().at(0).asDouble();
			
			dbStdpars[0] = c.eval("Std.Error").asDoubles()[0];
			dbStdpars[1] = c.eval("Std.Error").asDoubles()[1];
			dbStdpars[2] = c.eval("Std.Error").asDoubles()[2];
			dbStdpars[3] = c.eval("Std.Error").asDoubles()[3];
			
			parsName[0] = "p";
			parsName[1] = "Tl";
			parsName[2] = "dt";
			parsName[3] = "lamb";
			break;
		case 59:
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
			
		case 60:
			dbpars[0] = c.eval(tempar + "["+'"'+"Rmax"+'"'+"]").asList().at(0).asDouble();
			dbpars[1] = c.eval(tempar + "["+'"'+"Tceil"+'"'+"]").asList().at(0).asDouble();
			dbpars[2] = c.eval(tempar + "["+'"'+"Topc"+'"'+"]").asList().at(0).asDouble();
			
			dbStdpars[0] = c.eval("Std.Error").asDoubles()[0];
			dbStdpars[1] = c.eval("Std.Error").asDoubles()[1];
			dbStdpars[2] = c.eval("Std.Error").asDoubles()[2];
			
			parsName[0] = "Rmax";
			parsName[1] = "Tceil";
			parsName[2] = "Topc";
			break;
		case 61://alpha = 3.5; To=306.4
			dbpars[0] = c.eval(tempar + "["+'"'+"alpha"+'"'+"]").asList().at(0).asDouble();
			dbpars[1] = c.eval(tempar + "["+'"'+"To"+'"'+"]").asList().at(0).asDouble();
			
			
			dbStdpars[0] = c.eval("Std.Error").asDoubles()[0];
			dbStdpars[1] = c.eval("Std.Error").asDoubles()[1];
			
			
			parsName[0] = "alpha";
			parsName[1] = "To";
			break;
			
			
		case 62: //k,alpha,betas,Tb, Tc
			dbpars[0] = c.eval(tempar + "["+'"'+"k"+'"'+"]").asList().at(0).asDouble();
			dbpars[1] = c.eval(tempar + "["+'"'+"alpha"+'"'+"]").asList().at(0).asDouble();
			dbpars[2] = c.eval(tempar + "["+'"'+"betas"+'"'+"]").asList().at(0).asDouble();
			dbpars[3] = c.eval(tempar + "["+'"'+"Tb"+'"'+"]").asList().at(0).asDouble();
			dbpars[4] = c.eval(tempar + "["+'"'+"Tc"+'"'+"]").asList().at(0).asDouble();
			
			dbStdpars[0] = c.eval("Std.Error").asDoubles()[0];
			dbStdpars[1] = c.eval("Std.Error").asDoubles()[1];
			dbStdpars[2] = c.eval("Std.Error").asDoubles()[2];
			dbStdpars[3] = c.eval("Std.Error").asDoubles()[3];
			dbStdpars[4] = c.eval("Std.Error").asDoubles()[4];
			
			parsName[0] = "k";
			parsName[1] = "alpha";
			parsName[2] = "betas";
			parsName[3] = "Tb";
			parsName[4] = "Tc";
			break;
			
			
		case 63: //Tmin,Tmax,Topt
			dbpars[0] = c.eval(tempar + "["+'"'+"Tmin"+'"'+"]").asList().at(0).asDouble();
			dbpars[1] = c.eval(tempar + "["+'"'+"Tmax"+'"'+"]").asList().at(0).asDouble();
			dbpars[2] = c.eval(tempar + "["+'"'+"Topt"+'"'+"]").asList().at(0).asDouble();

			
			dbStdpars[0] = c.eval("Std.Error").asDoubles()[0];
			dbStdpars[1] = c.eval("Std.Error").asDoubles()[1];
			dbStdpars[2] = c.eval("Std.Error").asDoubles()[2];

			
			parsName[0] = "Tmin";
			parsName[1] = "Tmax";
			parsName[2] = "Topt";
			break;
			
		case 64: //Yasym,k,Tm,v
			dbpars[0] = c.eval(tempar + "["+'"'+"Yasym"+'"'+"]").asList().at(0).asDouble();
			dbpars[1] = c.eval(tempar + "["+'"'+"k"+'"'+"]").asList().at(0).asDouble();
			dbpars[2] = c.eval(tempar + "["+'"'+"Tm"+'"'+"]").asList().at(0).asDouble();
			dbpars[3] = c.eval(tempar + "["+'"'+"v"+'"'+"]").asList().at(0).asDouble();
					
			dbStdpars[0] = c.eval("Std.Error").asDoubles()[0];
			dbStdpars[1] = c.eval("Std.Error").asDoubles()[1];
			dbStdpars[2] = c.eval("Std.Error").asDoubles()[2];
			dbStdpars[3] = c.eval("Std.Error").asDoubles()[3];
			
			parsName[0] = "Yasym";
			parsName[1] = "k";
			parsName[2] = "Tm";
			parsName[3] = "v";
		
			break;
			
		case 65: //Yasym,k,Tm
			dbpars[0] = c.eval(tempar + "["+'"'+"Yasym"+'"'+"]").asList().at(0).asDouble();
			dbpars[1] = c.eval(tempar + "["+'"'+"k"+'"'+"]").asList().at(0).asDouble();
			dbpars[2] = c.eval(tempar + "["+'"'+"Tm"+'"'+"]").asList().at(0).asDouble();
			
			dbStdpars[0] = c.eval("Std.Error").asDoubles()[0];
			dbStdpars[1] = c.eval("Std.Error").asDoubles()[1];
			dbStdpars[2] = c.eval("Std.Error").asDoubles()[2];

			parsName[0] = "Yasym";
			parsName[1] = "k";
			parsName[2] = "Tm";

			break;
			
			
		case 66: //Rmax,Tmax,Topt
			dbpars[0] = c.eval(tempar + "["+'"'+"Rmax"+'"'+"]").asList().at(0).asDouble();
			dbpars[1] = c.eval(tempar + "["+'"'+"Tmax"+'"'+"]").asList().at(0).asDouble();
			dbpars[2] = c.eval(tempar + "["+'"'+"Topt"+'"'+"]").asList().at(0).asDouble();
		
			
			dbStdpars[0] = c.eval("Std.Error").asDoubles()[0];
			dbStdpars[1] = c.eval("Std.Error").asDoubles()[1];
			dbStdpars[2] = c.eval("Std.Error").asDoubles()[2];
			
			
			parsName[0] = "Rmax";
			parsName[1] = "Tmax";
			parsName[2] = "Topt";
		
			break;
			
			
		case 67: //Q10,Tref
			dbpars[0] = c.eval(tempar + "["+'"'+"Q10"+'"'+"]").asList().at(0).asDouble();
			dbpars[1] = c.eval(tempar + "["+'"'+"Tref"+'"'+"]").asList().at(0).asDouble();
	
			
			dbStdpars[0] = c.eval("Std.Error").asDoubles()[0];
			dbStdpars[1] = c.eval("Std.Error").asDoubles()[1];
	
			
			parsName[0] = "Q10";
			parsName[1] = "Tref";
			break;
			
			
		case 68: //Tmin,Tref
			dbpars[0] = c.eval(tempar + "["+'"'+"Tmin"+'"'+"]").asList().at(0).asDouble();
			dbpars[1] = c.eval(tempar + "["+'"'+"Tref"+'"'+"]").asList().at(0).asDouble();
	
			
			dbStdpars[0] = c.eval("Std.Error").asDoubles()[0];
			dbStdpars[1] = c.eval("Std.Error").asDoubles()[1];
	
			
			parsName[0] = "Tmin";
			parsName[1] = "Tref";
			break;
			
			
		case 69: //Rmax,Topt,Tmax
			dbpars[0] = c.eval(tempar + "["+'"'+"Rmax"+'"'+"]").asList().at(0).asDouble();
			dbpars[1] = c.eval(tempar + "["+'"'+"Topt"+'"'+"]").asList().at(0).asDouble();
			dbpars[2] = c.eval(tempar + "["+'"'+"Tmax"+'"'+"]").asList().at(0).asDouble();
			
			dbStdpars[0] = c.eval("Std.Error").asDoubles()[0];
			dbStdpars[1] = c.eval("Std.Error").asDoubles()[1];
			dbStdpars[2] = c.eval("Std.Error").asDoubles()[2];

			parsName[0] = "Rmax";
			parsName[1] = "Topt";
			parsName[2] = "Tmax";

			break;
			
		case 70: //Yasym,a,b,Topt
			dbpars[0] = c.eval(tempar + "["+'"'+"Yasym"+'"'+"]").asList().at(0).asDouble();
			dbpars[1] = c.eval(tempar + "["+'"'+"a"+'"'+"]").asList().at(0).asDouble();
			dbpars[2] = c.eval(tempar + "["+'"'+"b"+'"'+"]").asList().at(0).asDouble();
			dbpars[3] = c.eval(tempar + "["+'"'+"Topt"+'"'+"]").asList().at(0).asDouble();
					
			dbStdpars[0] = c.eval("Std.Error").asDoubles()[0];
			dbStdpars[1] = c.eval("Std.Error").asDoubles()[1];
			dbStdpars[2] = c.eval("Std.Error").asDoubles()[2];
			dbStdpars[3] = c.eval("Std.Error").asDoubles()[3];
			
			parsName[0] = "Yasym";
			parsName[1] = "a";
			parsName[2] = "b";
			parsName[3] = "Topt";		
			break;
			
			
		case 71: //,Yasym,b,Topt
			dbpars[0] = c.eval(tempar + "["+'"'+"Yasym"+'"'+"]").asList().at(0).asDouble();
			dbpars[1] = c.eval(tempar + "["+'"'+"b"+'"'+"]").asList().at(0).asDouble();
			dbpars[2] = c.eval(tempar + "["+'"'+"Topt"+'"'+"]").asList().at(0).asDouble();
			
			dbStdpars[0] = c.eval("Std.Error").asDoubles()[0];
			dbStdpars[1] = c.eval("Std.Error").asDoubles()[1];
			dbStdpars[2] = c.eval("Std.Error").asDoubles()[2];

			parsName[0] = "Yasym";
			parsName[1] = "b";
			parsName[2] = "Topt";
			break;
			
		case 72: //Rmax,Tmin,Tmax,Topt
			dbpars[0] = c.eval(tempar + "["+'"'+"Rmax"+'"'+"]").asList().at(0).asDouble();
			dbpars[1] = c.eval(tempar + "["+'"'+"Tmin"+'"'+"]").asList().at(0).asDouble();
			dbpars[2] = c.eval(tempar + "["+'"'+"Tmax"+'"'+"]").asList().at(0).asDouble();
			dbpars[3] = c.eval(tempar + "["+'"'+"Topt"+'"'+"]").asList().at(0).asDouble();
					
			dbStdpars[0] = c.eval("Std.Error").asDoubles()[0];
			dbStdpars[1] = c.eval("Std.Error").asDoubles()[1];
			dbStdpars[2] = c.eval("Std.Error").asDoubles()[2];
			dbStdpars[3] = c.eval("Std.Error").asDoubles()[3];
			
			parsName[0] = "Rmax";
			parsName[1] = "Tmin";
			parsName[2] = "Tmax";
			parsName[3] = "Topt";		
			break;
		
		case 73: //Yo,k,b
			dbpars[0] = c.eval(tempar + "["+'"'+"Yo"+'"'+"]").asList().at(0).asDouble();
			dbpars[1] = c.eval(tempar + "["+'"'+"k"+'"'+"]").asList().at(0).asDouble();
			dbpars[2] = c.eval(tempar + "["+'"'+"b"+'"'+"]").asList().at(0).asDouble();
			
			dbStdpars[0] = c.eval("Std.Error").asDoubles()[0];
			dbStdpars[1] = c.eval("Std.Error").asDoubles()[1];
			dbStdpars[2] = c.eval("Std.Error").asDoubles()[2];

			parsName[0] = "Yo";
			parsName[1] = "k";
			parsName[2] = "b";
			break;
			
		case 74: //Yb,Rmax,Tmax,Topt,Tmin
			dbpars[0] = c.eval(tempar + "["+'"'+"Yb"+'"'+"]").asList().at(0).asDouble();
			dbpars[1] = c.eval(tempar + "["+'"'+"Rmax"+'"'+"]").asList().at(0).asDouble();
			dbpars[2] = c.eval(tempar + "["+'"'+"Tmax"+'"'+"]").asList().at(0).asDouble();
			dbpars[3] = c.eval(tempar + "["+'"'+"Topt"+'"'+"]").asList().at(0).asDouble();
			dbpars[4] = c.eval(tempar + "["+'"'+"Tmin"+'"'+"]").asList().at(0).asDouble();
			
			dbStdpars[0] = c.eval("Std.Error").asDoubles()[0];
			dbStdpars[1] = c.eval("Std.Error").asDoubles()[1];
			dbStdpars[2] = c.eval("Std.Error").asDoubles()[2];
			dbStdpars[3] = c.eval("Std.Error").asDoubles()[3];
			dbStdpars[4] = c.eval("Std.Error").asDoubles()[4];
			
			parsName[0] = "Yb";
			parsName[1] = "Rmax";
			parsName[2] = "Tmax";
			parsName[3] = "Topt";
			parsName[4] = "Tmin";
			break;
			
		case 75: //Rmax,Tmax
			dbpars[0] = c.eval(tempar + "["+'"'+"Rmax"+'"'+"]").asList().at(0).asDouble();
			dbpars[1] = c.eval(tempar + "["+'"'+"Tmax"+'"'+"]").asList().at(0).asDouble();
	
			
			dbStdpars[0] = c.eval("Std.Error").asDoubles()[0];
			dbStdpars[1] = c.eval("Std.Error").asDoubles()[1];
	
			
			parsName[0] = "Rmax";
			parsName[1] = "Tmax";
			break;
			
		case 76: //Rmax,Tmax
			dbpars[0] = c.eval(tempar + "["+'"'+"Rmax"+'"'+"]").asList().at(0).asDouble();
			dbpars[1] = c.eval(tempar + "["+'"'+"Tmax"+'"'+"]").asList().at(0).asDouble();
	
			
			dbStdpars[0] = c.eval("Std.Error").asDoubles()[0];
			dbStdpars[1] = c.eval("Std.Error").asDoubles()[1];
	
			
			parsName[0] = "Rmax";
			parsName[1] = "Tmax";
			break;
			
					
		case 77: //Rmax,Tmax,Topt
			dbpars[0] = c.eval(tempar + "["+'"'+"Rmax"+'"'+"]").asList().at(0).asDouble();
			dbpars[1] = c.eval(tempar + "["+'"'+"Tmax"+'"'+"]").asList().at(0).asDouble();
			dbpars[2] = c.eval(tempar + "["+'"'+"Topt"+'"'+"]").asList().at(0).asDouble();
			
			dbStdpars[0] = c.eval("Std.Error").asDoubles()[0];
			dbStdpars[1] = c.eval("Std.Error").asDoubles()[1];
			dbStdpars[2] = c.eval("Std.Error").asDoubles()[2];

			parsName[0] = "Rmax";
			parsName[1] = "Tmax";
			parsName[2] = "Topt";
			break;
			
		case 78: //a,b,Topt
			dbpars[0] = c.eval(tempar + "["+'"'+"a"+'"'+"]").asList().at(0).asDouble();
			dbpars[1] = c.eval(tempar + "["+'"'+"b"+'"'+"]").asList().at(0).asDouble();
			dbpars[2] = c.eval(tempar + "["+'"'+"Topt"+'"'+"]").asList().at(0).asDouble();
			
			dbStdpars[0] = c.eval("Std.Error").asDoubles()[0];
			dbStdpars[1] = c.eval("Std.Error").asDoubles()[1];
			dbStdpars[2] = c.eval("Std.Error").asDoubles()[2];

			parsName[0] = "a";
			parsName[1] = "b";
			parsName[2] = "Topt";
			break;
			
		case 79: //a,b,Topt
			dbpars[0] = c.eval(tempar + "["+'"'+"a"+'"'+"]").asList().at(0).asDouble();
			dbpars[1] = c.eval(tempar + "["+'"'+"b"+'"'+"]").asList().at(0).asDouble();
			dbpars[2] = c.eval(tempar + "["+'"'+"Topt"+'"'+"]").asList().at(0).asDouble();
			
			dbStdpars[0] = c.eval("Std.Error").asDoubles()[0];
			dbStdpars[1] = c.eval("Std.Error").asDoubles()[1];
			dbStdpars[2] = c.eval("Std.Error").asDoubles()[2];

			parsName[0] = "a";
			parsName[1] = "b";
			parsName[2] = "Topt";
			break;
			
			
		case 80: //Yopt,a,b,Topt
			dbpars[0] = c.eval(tempar + "["+'"'+"Yopt"+'"'+"]").asList().at(0).asDouble();
			dbpars[1] = c.eval(tempar + "["+'"'+"a"+'"'+"]").asList().at(0).asDouble();
			dbpars[2] = c.eval(tempar + "["+'"'+"b"+'"'+"]").asList().at(0).asDouble();
			dbpars[3] = c.eval(tempar + "["+'"'+"Topt"+'"'+"]").asList().at(0).asDouble();
					
			dbStdpars[0] = c.eval("Std.Error").asDoubles()[0];
			dbStdpars[1] = c.eval("Std.Error").asDoubles()[1];
			dbStdpars[2] = c.eval("Std.Error").asDoubles()[2];
			dbStdpars[3] = c.eval("Std.Error").asDoubles()[3];
			
			parsName[0] = "Yopt";
			parsName[1] = "a";
			parsName[2] = "b";
			parsName[3] = "Topt";		
			break;
			
		case 81: //a,b,Topt
			dbpars[0] = c.eval(tempar + "["+'"'+"a"+'"'+"]").asList().at(0).asDouble();
			dbpars[1] = c.eval(tempar + "["+'"'+"b"+'"'+"]").asList().at(0).asDouble();
			dbpars[2] = c.eval(tempar + "["+'"'+"Topt"+'"'+"]").asList().at(0).asDouble();
			
			dbStdpars[0] = c.eval("Std.Error").asDoubles()[0];
			dbStdpars[1] = c.eval("Std.Error").asDoubles()[1];
			dbStdpars[2] = c.eval("Std.Error").asDoubles()[2];

			parsName[0] = "a";
			parsName[1] = "b";
			parsName[2] = "Topt";
			break;
			
			
		case 82: //a,b,k,Topt
			dbpars[0] = c.eval(tempar + "["+'"'+"a"+'"'+"]").asList().at(0).asDouble();
			dbpars[1] = c.eval(tempar + "["+'"'+"b"+'"'+"]").asList().at(0).asDouble();
			dbpars[2] = c.eval(tempar + "["+'"'+"k"+'"'+"]").asList().at(0).asDouble();
			dbpars[3] = c.eval(tempar + "["+'"'+"Topt"+'"'+"]").asList().at(0).asDouble();
					
			dbStdpars[0] = c.eval("Std.Error").asDoubles()[0];
			dbStdpars[1] = c.eval("Std.Error").asDoubles()[1];
			dbStdpars[2] = c.eval("Std.Error").asDoubles()[2];
			dbStdpars[3] = c.eval("Std.Error").asDoubles()[3];
			
			parsName[0] = "a";
			parsName[1] = "b";
			parsName[2] = "k";
			parsName[3] = "Topt";		
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
