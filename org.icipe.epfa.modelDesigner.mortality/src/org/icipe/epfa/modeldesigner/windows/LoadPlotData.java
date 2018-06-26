package org.icipe.epfa.modeldesigner.windows;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.icipe.epfa.classUtils.EPFAUtils;
import org.icipe.epfa.modeldesigner.ImageProperties;
import org.icipe.epfa.modeldesigner.ModifyImageUI;
import org.icipe.epfa.modeldesigner.wizards.MainPageWizardPage;
import org.icipe.epfa.project.windows.ViewProjectsUI;

import org.icipe.epfa.connectiontor.Rserve;
import org.osgi.framework.Bundle;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;

public class LoadPlotData extends Dialog {

	
	static RConnection c;
	protected Shell shlLoadAndPlat;
	private Text txtSelectADirfile;
	private static Label lblNewLabelimg;
	private Table table;
	protected Object result;
	protected Shell shellPlotData;
	protected String filePath;
	static String saveToR="";
	private static String fileMortalityData = "EPFA_mortalityData.txt";
	private static String imageMortalityData = "EPFA_mortalityData.png";
	
	private Button btnPlotData;
	public Composite composite_2,composite,composite_1,composite_3;
	private Text textAvTemp;
	private Text textMortality;
	
	private Group grpAddValue;
	private FormData fd_composite;
	private FormData fd_table;

	public LoadPlotData(Shell parent) {
		super(parent);
		setText("EPFA Load-Plot Data");
	}
	
	public LoadPlotData(Shell parent, int style) {
		
		super(parent, style);
		setText("EPFA Load Mortality Data");		
	}
	
	public Object open() 
	{
		
		createContents();
		
		
//		shellPlotData.pack();
		shellPlotData.open();
		
		
		
		Display display = getParent().getDisplay();
		while (!shellPlotData.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}
		
		
	private void createContents() 
	{
		 
		shellPlotData = new Shell(getParent(), SWT.SHELL_TRIM);
		shellPlotData.setSize(862, 652);
		shellPlotData.setText(getText());
		shellPlotData.setLayout(new FormLayout());
		
		
		if(!Platform.getOS().equalsIgnoreCase("macosx")){
			c = Rserve.launchRserve("",c);
			System.out.println("Launched");
		}
		
		composite_3 = new Composite(shellPlotData, SWT.NONE);
//		fd_table.top = new FormAttachment(composite_3, 12);
//		fd_composite.top = new FormAttachment(0, 74);
		FormData fd_composite_3 = new FormData();
		fd_composite_3.top = new FormAttachment(0, 10);
		fd_composite_3.left = new FormAttachment(0, 10);
		fd_composite_3.right = new FormAttachment(100, -10);
		composite_3.setLayoutData(fd_composite_3);
		
		
		Label lblChooseFile = new Label(composite_3, SWT.NONE);
		lblChooseFile.setBounds(10, 18, 90, 27);
		lblChooseFile.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		FormData fd_lblChooseFile = new FormData();
		fd_lblChooseFile.left = new FormAttachment(0, 10);
		lblChooseFile.setLayoutData(fd_lblChooseFile);
		lblChooseFile.setText("Choose File");
		
		txtSelectADirfile = new Text(composite_3, SWT.BORDER);
		txtSelectADirfile.setBounds(106, 15, 594, 30);
		fd_lblChooseFile.bottom = new FormAttachment(txtSelectADirfile, 0, SWT.BOTTOM);
		fd_lblChooseFile.top = new FormAttachment(txtSelectADirfile, 3, SWT.TOP);
		fd_lblChooseFile.right = new FormAttachment(txtSelectADirfile, -6);
		txtSelectADirfile.setText("Select mortality data file");
		FormData fd_txtSelectADirfile = new FormData();
		fd_txtSelectADirfile.bottom = new FormAttachment(100, -12);
		fd_txtSelectADirfile.left = new FormAttachment(0, 94);
		txtSelectADirfile.setLayoutData(fd_txtSelectADirfile);
		
		Button btnSelectFile = new Button(composite_3, SWT.NONE);
		btnSelectFile.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		btnSelectFile.setBounds(729, 15, 85, 27);
		fd_txtSelectADirfile.right = new FormAttachment(btnSelectFile, -6);
		btnSelectFile.addSelectionListener(new SelectionAdapter() {									
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				
				FileDialog dialog = new FileDialog(shellPlotData, SWT.OPEN|SWT.SINGLE);
				dialog.setFilterExtensions(new String[]{"*.txt"});
				//dialog.setFilterExtensions(new String[]{"*.txt","*.png"});
				//dialog.setFilterNames(new String[]{ "Text files (*.txt)", "PNG files (*.png)" });
				dialog.setFilterNames(new String[]{ "Text files (*.txt)"});
				dialog.open();
				
				String filePath = dialog.getFilterPath()+ File.separator+dialog.getFileName() ;
				txtSelectADirfile.setText(filePath);   // setText(dialog.getFileNames());
				
				//dialog.getFilterPath()+ File.separator+
			}
			
		});
		FormData fd_btnSelectFile = new FormData();
		fd_btnSelectFile.bottom = new FormAttachment(100, -10);
		fd_btnSelectFile.left = new FormAttachment(0, 720);
		btnSelectFile.setLayoutData(fd_btnSelectFile);
		btnSelectFile.setText("Load...");
		
		
		composite_1 = new Composite(shellPlotData, SWT.NONE);
		/*		fd_composite.left = new FormAttachment(composite_1, 0, SWT.LEFT);
				fd_composite.left = new FormAttachment(composite_1, 0, SWT.LEFT);
				fd_composite.left = new FormAttachment(composite_1, -22, SWT.LEFT);*/
				FormData fd_composite_1 = new FormData();
				fd_composite_1.left = new FormAttachment(0, 10);
				fd_composite_1.right = new FormAttachment(100, -10);
				fd_composite_1.bottom = new FormAttachment(100, 0);
				composite_1.setLayoutData(fd_composite_1);
				

	
		
		btnPlotData = new Button(composite_1, SWT.NONE);
		btnPlotData.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		btnPlotData.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				String dir1 = "";
				/*System.out.println(MainPageWizardPage.getstrMortalityPath());
				if(MainPageWizardPage.getstrMortalityPath().equalsIgnoreCase("/"))
				{
	    			MessageDialog.openError(shellPlotData, "Error", "You must first created a new project before start simulation");
	    			shellPlotData.dispose();
	    		}
				else{*/
				try {
					System.out.println("about to connect");
					 String pathMotData = getFileMortalityData();
					 
					
					//c = EPF.getConnection();
					//c = new RConnection();
					 if (c == null || !c.isConnected())
							c= new RConnection();
				
					 
					String parentFolder = new File("./").getCanonicalPath();
					//BufferedImage imge;

					REXP x1 = c.eval("R.version.string");

					System.out.println(x1.asString());  
					
				if((btnPlotData.getText()).equals("Load"))
				{
				 //Display display = Display.getDefault();
				 filePath = (txtSelectADirfile.getText()).replace('\\','/');
				//shellPlotData1.open();
				
//TESTTTTT						
					
		            String  scrptFile = ("/RScripts/mortalityDesigner_New.r").replace('\\','/');
		            
		            Bundle bundle = Platform.getBundle("org.icipe.epfa.project");

		            URL url = FileLocator.find(bundle, new Path("/RScripts/mortalityDesigner_New.r"), null);		            
		            URL fileURL = FileLocator.toFileURL(url);

		            /*		            
		            
//Test		            MainPageWizardPage.getstrMortalityPath() 
		            File tempScripFile = new File( "E:/NewFolder"+ File.separator + "tempScripFile.r");
		            InputStream in = FileLocator.openStream(bundle, new Path("/RScripts/mortalityDesigner_New.r"), false); 
		            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			        BufferedWriter writer = new BufferedWriter(new FileWriter(tempScripFile));
			           
			           //String ligne=null;
			           String line;
			           while ((line = reader.readLine()) != null) {
			               System.out.println(line);
			               writer.write(line);
			               writer.newLine();
			               //fw.write(line);
			               //c.eval(reader.readLine());
			           }
			           writer.close();
			           System.out.println(reader.readLine());
			           
			           
			           
			           
		            
		            
		            // String scrptFolder = new File(scrptFile); //.getCanonicalPath().replace("\\", "/");
		            System.out.println(parentFolder);
		            System.out.println(scrptFile);
		            System.out.println(fileURL);
		            System.out.println(fileURL.toString());
		            System.out.println(fileURL.toExternalForm());
		            
		            //scrptFolder = 

//END TESTTT
		            
		           String workingDir = System.getProperty("user.dir");
		           System.out.println(workingDir);
		           File fileDefaultPath = new File(scrptFile);
		           String scrptFolder= new String(fileDefaultPath.getAbsolutePath().replace('\\', '/'));
		           System.out.println(scrptFolder);
		           scrptFolder = scrptFolder+File.separator;
		           
		          // System.out.println(Paths.get(ClassLoader.getSystemResource("/RScripts/mortalityDesigner.r").toURI()));
		           
		           /*File tempScripFile = new File(MainPageWizardPage.getstrMortalityPath() + File.separator + "tempScripFile.r");
		           //FileWriter fw = new FileWriter(tempScripFile) ;
		           
		           InputStream in = getClass().getResourceAsStream("/RScripts/mortalityDesigner.r"); 
		          // OutputStream out = 
		           
		           
		           BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		           BufferedWriter writer = new BufferedWriter(new FileWriter(tempScripFile));
		           
		           //String ligne=null;
		           String line;
		           while ((line = reader.readLine()) != null) {
		               System.out.println(line);
		               writer.write(line);
		               writer.newLine();
		               //fw.write(line);
		               //c.eval(reader.readLine());
		           }
		           writer.close();
		           System.out.println(reader.readLine());
	           
		           
		           
		           System.out.println(fileURL.toString().substring(6));
		           String scriptPath = EPFAUtils.getLibPath();
		           EPFAUtils.createTempScriptFile(fileURL.toString().substring(6));
		           saveToR += "source('"+fileURL.toString().substring(6)+"'"+")"+"\r\n";	
		           c.eval("source('"+fileURL.toString().substring(6)+"'"+")");
			           
		           System.out.println("path script");
		           System.out.println(scriptPath);
*/		           
//   Original bloc code for reading the script
		            
		           EPFAUtils.createTempScriptFile("/RScripts/mortalityDesigner_New.r");
	//	           c.eval("install.packages(pkgs=" + '"' + "I:/Eclipsssss Test/Requirements/R/" +  "minpack.lm_1.2-1.zip" + '"' + ",repos=NULL, contriburl=NULL)");
		           
	//	           saveToR += "source('"+MainPageWizardPage.getstrMortalityPath()+"/tempScripFile.r'"+")"+"\r\n";	
	//	           saveToR += "source('"+ViewProjectsUI.getPathProject().replace('\\','/') +"/tempScripFile.r'"+")"+"\r\n";
		           saveToR += "source('"+(ViewProjectsUI.getPathProject()+ File.separator).replace('\\','/') +"tempScripFile.r'"+")"+"\r\n";
		           
		           c.eval("library(minpack.lm)");	
		           c.eval("library(MASS)");
		           
		           c.eval("source('"+(ViewProjectsUI.getPathProject()+ File.separator).replace('\\','/')+"tempScripFile.r'"+")");
	//	           c.eval("install.packages(pkgs=" + '"' + "I:/Eclipsssss Test/Requirements/R/" +  "MASS_7.3-49.zip" + '"' + ",repos=NULL, contriburl=NULL)");
	//	           c.eval("install.packages(pkgs=" + '"' + "I:/Eclipsssss Test/Requirements/R/" +  "minpack.lm_1.2-1.zip" + '"' + ",repos=NULL, contriburl=NULL)");

		           
		          
	//	           c.eval("source('"+"I:/other/".replace('\\','/')+"tempScripFile.r'"+")");
		           
		           		           
		           
//*/
		           
		           saveToR+="datLd<-read.table('"+filePath+"',header = T)"+"\n";
					c.eval("datLd<-read.table('"+filePath+"',header = T)");
		           
					System.out.println("charger");
					
							           
		           if(c.eval("ncol(datLd)").asInteger() == 3)
		           {
		        	   saveToR += "datm <-formatMortalityData2('"+filePath+"')" + "\r\n";
			           c.eval("datm <-formatMortalityData2('"+filePath+"')");
		           }else
		           {
		        	   saveToR += "datm <-formatMortalityData('"+filePath+"')" + "\r\n";
			           c.eval("datm <-formatMortalityData('"+filePath+"')");
		           }
					
		           
		           
		           System.out.println("charger__2");
		           System.out.println("'"+ pathMotData +"'");
					
		           
		           saveToR += "write.table(datm, file = '"+ getFileMortalityData() +"',row.names = FALSE)" + "\r\n";		
				   c.eval("write.table(datm, file = '"+ getFileMortalityData() +"',row.names = FALSE)");
				
				   
				   ////////////////////////////////////////////////////////////////////////
				 
		         
		          // write.table(dat, file = "donne.txt",row.names = FALSE)
		           
		           
				 	/*saveToR += "source('/RScripts/mortalityDesigner.r')" + "\r\n";		
					c.eval("source('/RScripts/mortalityDesigner.r')");
					
					//RList lst = con.eval("dtm <- data.frame(x1,y1)").asList();
					*/
				   
				   saveToR += "nrow(datm)" + "\r\n";	
					double nelt = c.eval("nrow(datm)").asDouble();
					System.out.println(nelt);
					
					//lst.			String[] keys = lst.keys();
					
					//lst.lastElement(); lst.firstElement();
					
					for(int i = 1;i<=nelt; i++)
					{
						TableItem ti = new TableItem(table, SWT.None);
						ti.setText(new String[]{i+"", c.eval("datm["+i+",1]").asString(), c.eval("datm["+i+",2]").asString()});
						
					}				
					
				/*Image image = new Image(display,filePath);
				lblNewLabelimg.setImage(image);*/
					
				txtSelectADirfile.setText(" ");
				btnPlotData.setText("Plot Data");
				
				c.close();
				}
				else if ((btnPlotData.getText()).equals("Plot Data"))
				{
					String strOutput = (parentFolder.concat("/Image/")).replace('\\','/');
					String dirImage = new File(strOutput).getParent().replace("\\", "/");
					
/*					saveToR += "mini <- min(datm[,1])" + "\r\n";
					c.eval("mini <- min(datm[,1])");
					
					saveToR += "maxi <- max(datm[,1])" + "\r\n";
					c.eval("maxi <- max(datm[,1])");

*/					
					saveToR += "mini <- 0" + "\r\n";
					c.eval("mini <- 0");
					
					saveToR += "maxi <- 45" + "\r\n";
					c.eval("maxi <- 45");
					
					saveToR += "corrx <- c(mini,maxi)" + "\r\n";
					c.eval("corrx <- c(mini,maxi)");
					
					saveToR += "corry <- c(0,100)" + "\r\n";
					c.eval("corry <- c(0,100)");
					
					saveToR += "labx <-'Temperature(Degree Celsius)'" + "\r\n";
					c.eval("labx <-'Temperature(Degree Celsius)'");
					
					saveToR += "laby <-'Mortality(%)'" + "\r\n";
					c.eval("laby <-'Mortality(%)'");
					
					saveToR += "titulo <-'Temperature Dependent Mortality'" + "\r\n";
					c.eval("titulo <-'Temperature Dependent Mortality'");
					
					c.eval("legx<-5");
					c.eval("legy<-20");
					
					c.eval("corrx2=seq(min(corrx),max(corrx),legx)");
					c.eval("corry2=seq(min(corry),max(corry),legy)");
										
					
					
					
					/*
					plot(datm[,1],datm[,2]*100,frame=F,pch=19,col=4,cex=1.3,xlim=corrx,ylim=corry,axes=F,xaxt = "n",xlab=labx,ylab=laby,main=graphTitle);axis(1, xaxp=c(corrx,5));axis(2,las=2);
					c.eval("png(file=" + '"' +MainPageWizardPage.getstrMortalityPath() + "/Mort-Model"+ modelm+".png"+'"'+")");
					saveToR+="plot_mort<-grafmort(" + '"' + "mortal" + '"' + ",modelm,estimor,g,datm,corrx,corry,mini,maxi,limit,1,labx,laby,titulo, grises)"+"\n";*/
					
					
					String strImgOutput = LoadPlotData.getImageMortalityData();//MainPageWizardPage.getstrMortalityPath()+ "EPFA_MortalityData.png";
					
					c.eval("png(file=" + '"' +strImgOutput +'"'+", width = 480, height = 480)");
					//c.eval("plot(datm[,1],datm[,2]*100,frame=F,pch=19,col=4,cex=1.3,xlim=corrx,ylim=corry,axes=F,xaxt = 'n',xlab=labx,ylab=laby,main=graphTitle);axis(1, xaxp=c(corrx,5));axis(2,las=2);");
					c.eval("plot(datm[,1],datm[,2]*100,frame=T,pch=19,cex=1.3,col=4,xlab=labx,ylab=laby,main=titulo,axes=F,xaxt = 'n',ylim=corry,xlim=corrx);axis(1, corrx2);axis(2, corry2,las=2)");
					c.eval("dev.off()");
					
					
					String strPath = new File(strImgOutput).getParent().replace("\\", "/");
//					String strName = EPFAUtils.ExtractPath(new File(strOutput).getName());
					//Image imge = new Image(Display.getDefault(),("\"" +dirImage + "/EPFA_MortalityData.png\"").replace('\\','/'));
					
//					Image imge = new Image(Display.getDefault(),strPath+"/"+"EPFA_MortalityData"+".png");
					Image imge = new Image(Display.getDefault(), LoadPlotData.getImageMortalityData());
					
			/*		dir1 = ("\"" +dirImage + "/EPFA_MortalityData.png\"");//.replace('\\','/');
					System.out.println(dir1);
					System.out.println(dir1.replace('\\','/'));
					//Image imge = new Image(Display.getDefault(),"C:/Users/Ritter/Documents/EclipseWorkspace/EPFAppli/Image/EPFA_MortalityData.png");//"C:/Users/Ritter/Documents/EclipseWorkspace/EPFAppli/Image/EPFA_MortalityData.png");
				*/	
					
					//File tempScripFile = new File(MainPageWizardPage.getstrMortalityPath() + File.separator + "tempScripFile.r");
					
					
					lblNewLabelimg.setImage(imge);
					btnPlotData.setText("Modify");
					
					c.close();
				}else if ((btnPlotData.getText()).equals("Modify"))
				{
					String corrx1 = c.eval("corrx[1]").asString();
					String corrx2 = c.eval("corrx[2]").asString();
					String corry1 = c.eval("corry[1]").asString();
					String corry2 = c.eval("corry[2]").asString();
					String labx = c.eval("labx").asString();
					String laby = c.eval("laby").asString();
					String titulo = c.eval("titulo").asString();
					String scaleX = c.eval("legx").asString();
					String scaleY = c.eval("legy").asString();
					
					//shellPlotData.dispose();
					c.close();
					ModifyLoadPlotImageUI modifImg = new ModifyLoadPlotImageUI(shellPlotData,SWT.SHELL_TRIM);
//					Shell modifImg = new Shell(shellPlotData,SWT.SHELL_TRIM);
					ImageProperties ip = ModifyImageUI.ip;
//					modifImg.ModifyImageUIVar(path, lblFunctImageDR,ip.getCorrX1(),ip.getCorrX2(),ip.getCorrY1(),ip.getCorrY2(),ip.getMini(),ip.getMaxi(), ip.getLabX(),ip.getLabY(),ip.getTitle(),ip.getLegX(),ip.getLegY(), ip.getScaleY(), ip.getScaleX());
					modifImg.ModifyImageUIVar(LoadPlotData.getImageMortalityData(), lblNewLabelimg,corrx1,corrx2,corry1,corry2,"0","100",labx,laby,titulo,scaleX,scaleY);
					modifImg.open();
				}
				
				} catch (RserveException e) {
					// TODO Auto-generated catch block
					MessageDialog.openError(new Shell(), "title", "Problems while trying to get data of the project");
					c.close();
					e.printStackTrace();
				} catch (REXPMismatchException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					MessageDialog.openError(new Shell(), "title", "Problems while trying to get data of the project");
					c.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		});
		btnPlotData.setBounds(575, 10, 75, 25);
		btnPlotData.setText("Load");
		
		Button btnCancel = new Button(composite_1, SWT.NONE);
		btnCancel.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				shellPlotData.dispose();
				//shellPlotData.close();
				
			}
		});
		btnCancel.setBounds(671, 10, 75, 25);
		btnCancel.setText("Close");
		fd_composite_1.top = new FormAttachment(0, 559);
		//lblNewLabelimg.pack();
		
		
		table = new Table(shellPlotData, SWT.BORDER | SWT.FULL_SELECTION);
		fd_composite_3.bottom = new FormAttachment(table, -6);
		fd_table = new FormData();
		fd_table.top = new FormAttachment(0, 70);
		fd_table.left = new FormAttachment(0, 10);
		
		table.setLayoutData(fd_table);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		//table.pack();
		//table.setBounds(arg0, arg1, arg2, arg3);
		TableColumn tblclmnId = new TableColumn(table, SWT.NONE);
		tblclmnId.setResizable(false);
		tblclmnId.setWidth(43);
		tblclmnId.setText("ID");
		
		TableColumn tblclmnTemperature = new TableColumn(table, SWT.NONE);
		tblclmnTemperature.setWidth(96);
		tblclmnTemperature.setText("Temperature");
		tblclmnTemperature.setResizable(false);
		
		TableColumn tblclmnMortality = new TableColumn(table, SWT.NONE);
		tblclmnMortality.setWidth(109);
		tblclmnMortality.setText("Mortality");
		tblclmnMortality.setResizable(false);
//		fd_table.bottom = new FormAttachment(grpAddValue, -15);
		
		
		
		
		
		grpAddValue = new Group(shellPlotData, SWT.BORDER);
		fd_table.bottom = new FormAttachment(grpAddValue, -10);
		grpAddValue.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		grpAddValue.setText("Add Value");
		grpAddValue.setLayout(new GridLayout(3, false));
		FormData fd_grpAddValue = new FormData();
		fd_grpAddValue.left = new FormAttachment(table,0,SWT.LEFT);
		fd_grpAddValue.bottom = new FormAttachment(composite_1, -94);
		fd_grpAddValue.top = new FormAttachment(0, 320);
		fd_grpAddValue.right = new FormAttachment(table,0,SWT.RIGHT);
		
		grpAddValue.setLayoutData(fd_grpAddValue);
		
		Label lblTemperature = new Label(grpAddValue, SWT.CENTER);
		lblTemperature.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		lblTemperature.setBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		GridData gd_lblTemperature = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_lblTemperature.widthHint = 77;
		gd_lblTemperature.heightHint = 23;
		lblTemperature.setLayoutData(gd_lblTemperature);
		lblTemperature.setText("Temp :");
		
		textAvTemp = new Text(grpAddValue, SWT.BORDER);
		GridData gd_textAvTemp = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_textAvTemp.widthHint = 56;
		textAvTemp.setLayoutData(gd_textAvTemp);
		new Label(grpAddValue, SWT.NONE);
		
		Label lblMortality = new Label(grpAddValue, SWT.CENTER);
		lblMortality.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		lblMortality.setBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		GridData gd_lblMortality = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_lblMortality.heightHint = 23;
		lblMortality.setLayoutData(gd_lblMortality);
		lblMortality.setText("     Mortality :");
		
		textMortality = new Text(grpAddValue, SWT.BORDER);
		textMortality.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(grpAddValue, SWT.NONE);
		new Label(grpAddValue, SWT.NONE);
		new Label(grpAddValue, SWT.NONE);
		
		Button btnAdd = new Button(grpAddValue, SWT.NONE);
		btnAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				
				String AvTemp = textAvTemp.getText();
				if( AvTemp.equalsIgnoreCase(""))
				{
	    			MessageDialog.openError(shellPlotData, "Error", "Please enter the value of Average Temperature");
	    			return;
	    		}
				
				
				String AvgMort = textMortality.getText();
				if( AvgMort.equalsIgnoreCase(""))
				{
	    			MessageDialog.openError(shellPlotData, "Error", "Please enter the value of the Mortality");
	    			return;
	    		}
				else if(!(Double.parseDouble(AvgMort)>=0 && Double.parseDouble(AvgMort)<=1))
	    		{
					MessageDialog.openError(shellPlotData, "Warning!", "Please! Mortality value must be between 0 to 1");
	    			return;	    			
	    		}
				
				
				try {
					c = new RConnection();
					System.out.println("adding connect");
					
					saveToR += "ncol(datm)" + "\r\n";	
					double nelt = c.eval("ncol(datm)").asDouble();
		/*			
					for(int j = 1;j<=nelt; j++)
					{
						saveToR += "datm[,"+j+"] = as.character(datm[,"+j+"])" + "\r\n";
						c.eval("datm[,"+j+"] = as.character(datm[,"+j+"])");
					}
					*/
					saveToR += "datm <-rbind(datm,c(x ="+Double.parseDouble(AvTemp) +", y="+Double.parseDouble(AvgMort)+"))"+ "\r\n";
					c.eval("datm <-rbind(datm,c(x ="+Double.parseDouble(AvTemp)+", y="+Double.parseDouble(AvgMort)+"))");
					
					double nel = c.eval("nrow(datm)").asDouble();
					System.out.println(nel);
					
				//	String tabelt[] = c.eval("datm").asStrings();
				//	reecriture du fichier
					 saveToR += "write.table(datm, file = '"+ getFileMortalityData() +"',row.names = FALSE)" + "\r\n";		
					 c.eval("write.table(datm, file = '"+ getFileMortalityData() +"',row.names = FALSE)");
					
//					saveToR += "write.table(datm,paste('"+CreatedMaizeProject.getStrPathProject()+"',"+comboStageDev.getText()+","+"\".txt\""+",sep=\""+"\"),row.names=FALSE)" + "\r\n";		
//					c.eval("write.table(datm,paste('"+CreatedMaizeProject.getStrPathProject()+"','"+comboStageDev.getText()+"',"+"\".txt\""+",sep=\""+"\"),row.names=FALSE)");
					
					
					int i= table.getItemCount()+1;
					TableItem ti = new TableItem(table, SWT.None);
					ti.setText(new String[]{i+"",AvTemp , AvgMort});
					
//					String strImgOutput = LoadPlotData.getImageMortalityData();//MainPageWizardPage.getstrMortalityPath()+ "EPFA_MortalityData.png";
					
					
 					saveToR += "png(file=" + '"' +LoadPlotData.getImageMortalityData() +'"'+", width = 480, height = 480)" + "\r\n";	
					c.eval("png(file=" + '"' +LoadPlotData.getImageMortalityData() +'"'+", width = 480, height = 480)");
					
					//c.eval("plot(datm[,1],datm[,2]*100,frame=T,pch=19,col=4,cex=1.3,xlim=corrx,ylim=corry,axes=F,xaxt = 'n',xlab=labx,ylab=laby,main=titulo);axis(1, xaxp=c(corrx,5));axis(2,las=2);"); 
					 
					saveToR += "plot(datm[,1],datm[,2]*100,frame=T,pch=19,cex=1.3,col=4,xlab=labx,ylab=laby,main=titulo,axes=F,xaxt = 'n',ylim=corry,xlim=corrx);axis(1, corrx2);axis(2, corry2,las=2)" + "\r\n";
					c.eval("plot(datm[,1],datm[,2]*100,frame=T,pch=19,cex=1.3,col=4,xlab=labx,ylab=laby,main=titulo,axes=F,xaxt = 'n',ylim=corry,xlim=corrx);axis(1, corrx2);axis(2, corry2,las=2)");
					
					saveToR += "dev.off()" + "\r\n";
					c.eval("dev.off()");
					
					
					Image imge = new Image(Display.getDefault(), LoadPlotData.getImageMortalityData());
																	
					lblNewLabelimg.setImage(imge);
					
//					btnPlotData.setText("Plot Data");
					
					c.close();
					
					
				} catch (RserveException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					MessageDialog.openError(shellPlotData, "Error", "Problem when trying to add new values! verify that rserve is running!");
				} catch (REXPMismatchException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				textAvTemp.setText(""); textMortality.setText("");
				
			}
		});
		
	
		GridData gd_btnAdd = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnAdd.widthHint = 77;
		btnAdd.setLayoutData(gd_btnAdd);
		btnAdd.setText("Add");
		
		composite_2 = new Composite(shellPlotData, SWT.NONE);
		fd_table.right = new FormAttachment(composite_2, -5);
		fd_composite = new FormData();
		fd_composite.right = new FormAttachment(100, -10);
		fd_composite.left = new FormAttachment(0, 265);
		fd_composite.top = new FormAttachment(table,0,SWT.TOP);
		fd_composite.bottom = new FormAttachment(composite_1, -5);
		composite_2.setLayoutData(fd_composite);
		
		
		
		lblNewLabelimg = new Label(composite_2, SWT.FILL);
//		int rest = composite_2.getBorderWidth();
		lblNewLabelimg.setBounds(0,0, 569,481);
/*		FormData label_composite = new FormData();
		label_composite.top = new FormAttachment(composite_2,2,SWT.TOP);
		label_composite.right = new FormAttachment(composite_2,2,SWT.RIGHT);
		label_composite.left = new FormAttachment(composite_2,2,SWT.LEFT);
		label_composite.bottom = new FormAttachment(composite_2,2,SWT.BOTTOM);
		lblNewLabelimg.setLayoutData(label_composite);
		*/
//		lblNewLabelimg.setAlignment(SWT.CENTER);
//		lblNewLabelimg.setBounds(10, 0, 578, 470);
		lblNewLabelimg.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));		
		
		
		shellPlotData.open();
		
	}
		
		 
		
	public static String getFileMortalityData() {
		//return MainPageWizardPage.getstrMortalityPath() + File.separator + fileMortalityData;
//		return MainPageWizardPage.getstrMortalityPath() + fileMortalityData;
				
		return ViewProjectsUI.getPathMortality().replace('\\','/') + fileMortalityData;

	}

	public static void setFileMortalityData(String fileMortalityData) {
		LoadPlotData.fileMortalityData = fileMortalityData;
	}
	
	public static String getImageMortalityData() {
		return ViewProjectsUI.getPathMortality().replace('\\','/') + imageMortalityData;
	}

	public static void setImageMortalityData(String imageMortalityData) {
		LoadPlotData.imageMortalityData = imageMortalityData;
	}


}
