package org.icipe.epfa.project.wizards;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.icipe.epfa.project.ProjectFileProperties;
import org.icipe.epfa.project.windows.ViewProjectsUI;

public class NewProjectWizard extends Wizard implements INewWizard {
	
	public static ProjectFileProperties prop= new ProjectFileProperties();
	String strPathProject;
	
	
	public void addPages()
	{
		addPage(new RegistrationPage());
	}

	public NewProjectWizard() {
		// TODO Auto-generated constructor stub
		setWindowTitle("EPFA Project Creation");
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean performFinish() {
		if(!validateRegistration())
			return false;
		
		prop.setNameProject(RegistrationPage.txtTextprojetname.getText());
		prop.setNameSpecie(RegistrationPage.textSpeciesName.getText());
		prop.setStrAuthor(RegistrationPage.textAuthor.getText());
		prop.setStrDate(RegistrationPage.textDate.getText());
		prop.setStrObservations(RegistrationPage.textObservation.getText());
		
	
				
		createProject();
		saveProgress();
		crateFolders();
		
		ViewProjectsUI.refreshTree();
		
		
		return true;
	}
	
	
	

	File fileP;

	private boolean createProject() 
	{
		try {
			fileP = new File(strPathProject + File.separator + prop.getNameProject().trim());
			if(!fileP.exists()){
				fileP.mkdir();
				BufferedWriter bw;
				
				bw = new BufferedWriter(new FileWriter(fileP + File.separator + ".project"));
				
				bw.write("<?xml version=" + '"'+"1.0"+'"'+ " encoding=" + '"'+"UTF-8"+'"'+ "?>" + "\r\n");
				bw.write("<projectDescription>" + "\r\n");
				bw.write("<name>" + prop.getNameProject() + "</name>" + "\r\n");
				bw.write("<comment></comment>" + "\r\n");
				bw.write("<projects>" + "\r\n");
				bw.write("</projects>" + "\r\n");
				bw.write("<buildSpec>" + "\r\n");
				bw.write("</buildSpec>" + "\r\n");
				bw.write("<natures>" + "\r\n");
				bw.write("</natures>" + "\r\n");
				bw.write("</projectDescription>" + "\r\n");
				bw.close();
			}else{
				MessageDialog.openInformation(new Shell(), "Creating Project", prop.getNameProject() + " exists, please select another project name");
				return false;
			}
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(fileP + File.separator + prop.getNameProject() + ".epfa"));
			String strReg = "";
			strReg =  "ProjectName     : " + prop.getNameProject()+ "\r\n";
			strReg += "SpeciesName     : " + prop.getNameSpecie() + "\r\n" +
					  "Author          : " + prop.getStrAuthor() + "\r\n" +
					  "Date            : " + prop.getStrDate() + "\r\n" +
					  "Observations    : " + prop.getStrObservations()  ;
			
			bw.write(strReg);
			bw.close();
			
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
			MessageDialog.openError(new Shell(), "EPFA error", e.getMessage());
			return false;
		}
		return true;	
		
		
	}
	
	
	
	
	private void crateFolders() 
	{

		File fileMortalityPath = new File(fileP + File.separator + "Mortality");
		fileMortalityPath.mkdir();
		
				
		File fileMappingPath = new File(fileP + File.separator + "Mapping");
		fileMappingPath.mkdir();

		
		try {
			String[] arrayStagesIn, arrayStagesMad;
			String strResume="";
			
//			arrayStagesIn = ArrayConvertions.StringtoArray(prop.getInmadureStages(), ",");
//			arrayStagesMad = ArrayConvertions.StringtoArray(prop.getAdultStages(), ",");
			
			//Mortality
			strResume="";
			BufferedWriter bw = new BufferedWriter(new FileWriter(fileP + File.separator + "Mortality" + File.separator + "resume.epfa"));
			
			strResume+="Stage ="+"\r\n";
			strResume+="Model="+"\r\n";
			strResume+="Parameters ="+"\r\n";
			strResume+="Formula ="+"\r\n" + "\r\n";
				
			
									
			bw.write(strResume);
			bw.close();
			
			//Mapping
			strResume="";
			bw = new BufferedWriter(new FileWriter(fileP + File.separator + "Mapping" + File.separator + "resume.epfa"));
			
					strResume+="Stage ="+"\r\n";
					strResume+="Model ="+"\r\n";
					strResume+="Parameters ="+"\r\n";
					strResume+="Formula ="+"\r\n" + "\r\n";
			
									
			bw.write(strResume);
			bw.close();
			

					
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

	private void saveProgress() 
	{
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(fileP + File.separator + "Progress.epfa"));
			String strProgres="";
//			String[] arrayStagesIn, arrayStagesMad;
/*			
//			arrayStagesIn = ArrayConvertions.StringtoArray(prop.getInmadureStages(), ",");
//			arrayStagesMad = ArrayConvertions.StringtoArray(prop.getAdultStages(), ",");
			boolean bolAdultStage=false;
			if(arrayStagesMad.length == 1)
				bolAdultStage = true;
			
			if(prop.getStrRatio().contains("Age") || prop.getStrRatio().contains("Temperature")){
				strProgres = "Stgs\\Eval" + "\t" + "Dev. Time" + "\t" + "Dev. Rate" + "\t" + "Senes." +"\t"+ "Mort." + "\t" + "Tot. Ovi." + "\t" + "Rel. Ovi." + "\t" + "Rate Ovi." + "\r\n";
				
				for(int i=0; i<arrayStagesIn.length; i++){
					strProgres += arrayStagesIn[i].trim() + "\t" + "false" +
		  			  "\t" + "false" + "\t" + "disable" +
		  			  "\t" + "false" + "\t" + "disable" + 
		  			  "\t" + "disable" + "\t" + "disable" +"\r\n";
				}
				
				if(!bolAdultStage){
					for(int i=0; i<arrayStagesMad.length; i++){
						if(i != arrayStagesMad.length-1){
							strProgres += arrayStagesMad[i].trim() + "\t" + "false" +
							  "\t" + "disable" + "\t" + "false" + 
				  			  "\t" + "disable" + "\t" + "false" +
				  			  "\t" + "false" + "\t" + "false" + "\r\n";
						}else{
							strProgres += arrayStagesMad[i].trim() + "\t" + "false" +
							  "\t" + "disable" + "\t" + "false" +
				  			  "\t" + "disable" + "\t" + "disable" +
				  			  "\t" + "disable" + "\t" + "disable" +"\r\n";
						}
					}
				}else{
					strProgres += arrayStagesMad[0].trim() + "\t" + "false" +
					  "\t" + "disable" + "\t" + "false" + 
		  			  "\t" + "disable" + "\t" + "false" +
		  			  "\t" + "false" + "\t" + "false" + "\r\n";
				}
			}else{
				strProgres = "Stgs\\Eval" + "\t" + "Dev. Time" + "\t" + "Dev. Rate" + "\t" + "Senes." +"\t"+ "Mort." + "\t" + "Tot. Ovi." + "\t" + "Rel. Ovi." + "\r\n";
				
				for(int i=0; i<arrayStagesIn.length; i++){
					strProgres += arrayStagesIn[i].trim() + "\t" + "false" +
		  			  "\t" + "false" + "\t" + "disable" +
		  			  "\t" + "false" + "\t" + "disable" +
		  			  "\t" + "disable" + "\r\n";
				}
				
				if(!bolAdultStage){
					for(int i=0; i<arrayStagesMad.length; i++){
						if(i != arrayStagesMad.length-1){
							strProgres += arrayStagesMad[i].trim() + "\t" + "false" +
							  "\t" + "disable" + "\t" + "false" + 
				  			  "\t" + "disable" + "\t" + "false" +
				  			  "\t" + "false" + "\r\n";
						}else{
							strProgres += arrayStagesMad[i].trim() + "\t" + "false" +
							  "\t" + "disable" + "\t" + "false" +
				  			  "\t" + "disable" + "\t" + "disable" +
				  			  "\t" + "disable" + "\r\n";
						}
					}
				}else{
					strProgres += arrayStagesMad[0].trim() + "\t" + "false" +
					  "\t" + "disable" + "\t" + "false" + 
		  			  "\t" + "disable" + "\t" + "false" +
		  			  "\t" + "false" + "\r\n";
				}
			}
	*/		
			bw.write(strProgres);
			bw.close();
			
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
			MessageDialog.openError(new Shell(), "EPFA error", e.getMessage());
		}
		
		
	}

	private boolean validateRegistration() {
		
		if(RegistrationPage.txtTextprojetname.getText().equalsIgnoreCase(""))
		{
			MessageDialog.openInformation(new Shell(), "Incomplete Input", "You must enter the name of the project");
			return false;
		}
		
		if(RegistrationPage.textSpeciesName.getText().equalsIgnoreCase(""))
		{
			MessageDialog.openInformation(new Shell(), "Incomplete Input", "You must enter the name of the fungi species studied!");
			return false;
		}
		
//		strPathProject = "C:/Users/Ritter/Documents/EclipseRCPWorkspace/";
//				RegistrationPage.textPathworkspace.getText();
		
	
		
		int p=1;
		strPathProject = Platform.getLocation().toString();
		
		
/*		if(RegistrationPage.txtProjName.getText().equalsIgnoreCase("")){
			RegistrationPage.txtProjName.setText("Project "+p);
		}
		while(new File(strPathProject + File.separator + RegistrationPage.txtProjName.getText()).exists()){
			p++;
			RegistrationPage.txtProjName.setText("Project "+p);
		}
		
		if(RegistrationPage.txtAdults.getText().equalsIgnoreCase("")){
			MessageDialog.openInformation(new Shell(), "Incomplete", "You must to enter adults life-stages");
			return false;
		}
		
		String[] im = ArrayConvertions.StringtoArray(RegistrationPage.txtInamdure.getText(), ",");
		String[] ma = ArrayConvertions.StringtoArray(RegistrationPage.txtAdults.getText(), ",");
		String[] lifeStages = new String[im.length + ma.length];
		int i=0;
		for(i=0; i<im.length; i++){
			lifeStages[i] = im[i];
		}
		for(int j=0; j<ma.length; j++){
			lifeStages[j+i] = ma[j];
		}
		String stagesRep="";
		for(int j=0; j<lifeStages.length; j++){
			for(int n=j; n<lifeStages.length-1; n++){
				if(lifeStages[j].equalsIgnoreCase(lifeStages[n+1])){
					stagesRep += lifeStages[j]+'-';
				}
			}
			
		}
		
		if(!stagesRep.equalsIgnoreCase("")){
			stagesRep = stagesRep.substring(0, (stagesRep.length()-1));
			MessageDialog.openError(new Shell(), "Incomplete", "There is a duplicated life-stage : " + stagesRep);
			return false;
		}
		
		if(FemaleRatePage.fixedRateButton.getSelection()){
			if(FemaleRatePage.txtRatio.getText().equalsIgnoreCase("")){
				MessageDialog.openInformation(new Shell(), "Incomplete", "You must to enter a fixed rate");
				return false;
			}
			return true;
		}else{
			if(!FemaleRatePage.ageButton.getSelection() && !FemaleRatePage.temperatureButton.getSelection()){
				MessageDialog.openInformation(new Shell(), "Incomplete", "You must to select a variable rate");
				return false;
			}
			//bol=false;
			return true;
		}*/
		return true;
	}

}
