package org.icipe.epfa.project.windows;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.ResourceManager;
import org.icipe.epfa.classUtils.ArrayConvertions;

public class ViewProjectsUI extends ViewPart {
	
	private static Tree tree;
	private static Image fileIm, dir, imgIcon, txtIcon;
	public static String pathProject, nameProject, pathMortality, pathMapping;
	
	
	
	

	// pathProject = "E:/Learning/SavedWorkspace/"
	public ViewProjectsUI() 
	{
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createPartControl(Composite parent) {
		fileIm = ResourceManager.getPluginImage("org.icipe.epfa.project", "icons/inter_explorer.png");
		dir = ResourceManager.getPluginImage("org.icipe.epfa.project", "icons/folder_2.png");
		imgIcon = ResourceManager.getPluginImage("org.icipe.epfa.project", "icons/images.png");
		txtIcon = ResourceManager.getPluginImage("org.icipe.epfa.project", "icons/texto.png");
		
		tree = new Tree(parent, SWT.BORDER);
		refreshTree();
		
		tree.addListener(SWT.Expand, new Listener() {
			public void handleEvent(final Event event) {
				final TreeItem root = (TreeItem) event.item;
		        TreeItem[] items = root.getItems();
		        for (int i = 0; i < items.length; i++) {
		        	if (items[i].getData() != null)
		            return;
		        	items[i].dispose();
		        }
		        File file = (File) root.getData();
		        File[] files = file.listFiles();
		        if (files == null)
		        	return;
		        for (int i = 0; i < files.length; i++) {
		        	TreeItem item = new TreeItem(root, 0);
		        	item.setText(files[i].getName());
		        	item.setData(files[i]);
		        	if (files[i].isDirectory()) {
		        		item.setImage(dir);
		        		new TreeItem(item, 0);
		        	}
		        	if(files[i].isFile()){
		        		String ext = ExtractExt(files[i].toString());
		        		if(ext.equalsIgnoreCase(".html"))
		        			item.setImage(fileIm);  
		        		if(ext.equalsIgnoreCase(".png"))
		        			item.setImage(imgIcon);
		        		if(ext.equalsIgnoreCase(".epfa"))
		        			item.setImage(txtIcon);
		        		if(ext.equalsIgnoreCase(".txt"))
		        			item.setImage(txtIcon);
		        	}
		        }
			}
    	});
		
		tree.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e){
				TreeItem ti = (TreeItem) e.item;
		        pathProject = Platform.getLocation().toFile().getAbsolutePath() + File.separator + ti.getText();
		        nameProject = ti.getText();
		        pathMortality = (pathProject + File.separator + "Mortality"+ File.separator).replace('\\', '/');
		        pathMapping = (pathProject + File.separator + "Mapping"+ File.separator).replace('\\', '/');
		        
		        System.out.println(pathProject);
		        System.out.println(pathMortality);
		        System.out.println(pathMapping);
			}
		});
		
		final Menu menu = new Menu(tree);
		tree.setMenu(menu);
		
		MenuItem mntmuploadData = new MenuItem(menu, SWT.NONE);
		mntmuploadData.setText("&Upload Data");
		mntmuploadData.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
/*				try{
					UploadDataUI ui = new UploadDataUI();
					ui.createContents();
					ui.lblRate.setText(getRate());
					
					if(getRate().contains("Temperature")){
						ui.btnOvipositionFile.setVisible(false);
						ui.txtOviposition.setVisible(false);
						
						ui.ovipositionMaleButton.setVisible(true);
						ui.txtOvipMale.setVisible(true);
						ui.ovipositionFemaleButton.setVisible(true);
						ui.txtOvipFem.setVisible(true);
						ui.txtDead.setVisible(true);
						ui.lblDead.setVisible(true);
					}else{
						ui.btnOvipositionFile.setVisible(true);
						ui.txtOviposition.setVisible(true);
						
						ui.ovipositionMaleButton.setVisible(false);
						ui.txtOvipMale.setVisible(false);
						ui.ovipositionFemaleButton.setVisible(false);
						ui.txtOvipFem.setVisible(false);
						ui.txtDead.setVisible(false);
						ui.lblDead.setVisible(false);
					}
					
					ui.open();
				}catch (Exception e1) {
					e1.printStackTrace();
				}*/
			}
		});
		
		
		new MenuItem(menu, SWT.SEPARATOR);
		
		final MenuItem importProjectToMenuItem = new MenuItem(menu, SWT.NONE);
		importProjectToMenuItem.setText("&Import existing project into workspace");
		importProjectToMenuItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				ImportProjectUI ui = new ImportProjectUI(new Shell());
				ui.open();
				refreshTree();
			}
		});
		
		
		
		final MenuItem refreshMenuItem = new MenuItem(menu, SWT.NONE);
		refreshMenuItem.setText("&Refresh");
		refreshMenuItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				refreshTree();
			}
		});
		
		final MenuItem deleteMenuItem = new MenuItem(menu, SWT.NONE);
		deleteMenuItem.setText("Delete");
		deleteMenuItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				boolean confirm = MessageDialog.openQuestion(new Shell(), "Deleting project", "Are you sure to delete completly project?, cannot be undone");
				if(confirm){
					deleteProject();
					refreshTree();
				}
			}
		});
		
		new MenuItem(menu, SWT.SEPARATOR);
		
		final MenuItem propertiesMenuItem = new MenuItem(menu, SWT.NONE);
		propertiesMenuItem.setText("&Properties");
		propertiesMenuItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				PropertyProject ui = new PropertyProject();
				ui.createContents();
				ui.showProperties();
				ui.open();
			}
		});

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
	
		public static String[] getImmatureLifeStage(){
		String lifeStages[]= null;
		String rFile = pathProject + File.separator + nameProject + ".epfa";
		Properties prop = new Properties();
		
		try {
			prop.load(new FileInputStream(rFile));
			lifeStages = ArrayConvertions.StringtoArray(prop.getProperty("ImmadureStages"),",");
				
			return lifeStages;
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return lifeStages;
	}
	
		public static String[] getAdultsLifeStage(){
		String lifeStages[]= null;
		String rFile = pathProject + File.separator + nameProject + ".epfa";
		Properties prop = new Properties();
		
		try {
			prop.load(new FileInputStream(rFile));
			lifeStages = ArrayConvertions.StringtoArray(prop.getProperty("AdultsStages"),",");
				
			return lifeStages;
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

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	public static String getPathProject() {
		return pathProject;
	}

	public static void setPathProject(String pathProject) {
		ViewProjectsUI.pathProject = pathProject;
	}
	
	public static String getPathMortality() {
		return pathMortality;
	}

	public static void setPathMortality(String pathMortality) {
		ViewProjectsUI.pathMortality = pathMortality;
	}

	public static String getPathMapping() {
		return pathMapping;
	}

	public static void setPathMapping(String pathMapping) {
		ViewProjectsUI.pathMapping = pathMapping;
	}

	

}
