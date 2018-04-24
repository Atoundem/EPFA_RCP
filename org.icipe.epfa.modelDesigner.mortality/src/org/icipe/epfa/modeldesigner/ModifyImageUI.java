package org.icipe.epfa.modeldesigner;

import org.icipe.epfa.modeldesigner.wizards.MainPageWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;

public class ModifyImageUI extends Dialog {

	private Text txtMaxX;
	private Text txtMinX;
	private Text txtMaxY;
	private Text txtMinY;
	private Text txtLegY;
	private Text txtLegX;
	private Text txtChartY;
	private Text txtChartX;
	private Text txtTitle;
	private Button chkGrises;
	protected Object result;
	
	private String mini, maxi;
	
	public int model;

	protected Shell shell;

	/**
	 * Create the dialog
	 * @param parent
	 * @param style
	 */
	public ModifyImageUI(Shell parent, int style) {
		super(parent, style);
	}

	/**
	 * Create the dialog
	 * @param parent
	 */
	public ModifyImageUI(Shell parent) {
		this(parent, SWT.NONE);
	}

	RConnection c; 
	String pathImage; Label lblImage;
	String minX; String maxX; String minY; String maxY; String charX;
	String charY; String title; String legX; String legY; String scaleY; String scaleX;
	public static ImageProperties ip = new ImageProperties();
	public int sims;
	private Text txtScaleY;
	private Text txtScaleX;
	
	public void ModifyImageUIVar(String pathImage, Label lblImage,
								 String minX, String maxX, String minY, String maxY, String mini, String maxi,
								 String charX, String charY, String title, String legX, String legY, String scaleY, String scaleX) {
		//this.c = c;
		//this.strEval = strEval;
		this.pathImage = pathImage;
		this.lblImage = lblImage;
		
		this.minX = minX; this.maxX=maxX; this.minY=minY; this.maxY=maxY; this.charX=charX;
		this.charY=charY;this.title=title;this.legX=legX;this.legY=legY;this.scaleY = scaleY;this.scaleX = scaleX;
		
		this.mini = mini; this.maxi = maxi;
	}
	
	/**
	 * Open the dialog
	 * @return the result
	 */
	public Object open() {
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		return result;
	}

	/**
	 * Create contents of the dialog
	 */
	protected void createContents() {
		shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shell.setSize(444, 331);
		shell.setText("Image Properties");

		final Group group = new Group(shell, SWT.NONE);
		group.setFont(SWTResourceManager.getFont("Comic Sans MS", 10, SWT.NONE));
		group.setText("Chart");
		group.setBounds(10, 10, 422, 115);

		final Label label = new Label(group, SWT.NONE);
		label.setFont(SWTResourceManager.getFont("Comic Sans MS", 10, SWT.NONE));
		label.setText("Title :");
		label.setBounds(10, 25, 65, 18);

		final Label label_1 = new Label(group, SWT.NONE);
		label_1.setFont(SWTResourceManager.getFont("Comic Sans MS", 10, SWT.NONE));
		label_1.setText("Chart X :");
		label_1.setBounds(10, 55, 65, 18);

		final Label label_2 = new Label(group, SWT.NONE);
		label_2.setFont(SWTResourceManager.getFont("Comic Sans MS", 10, SWT.NONE));
		label_2.setText("Chart Y :");
		label_2.setBounds(10, 85, 65, 18);

		txtTitle = new Text(group, SWT.BORDER);
		txtTitle.setText(title);
		txtTitle.setBounds(81, 25, 331, 20);

		txtChartX = new Text(group, SWT.BORDER);
		txtChartX.setText(charX);
		txtChartX.setBounds(81, 55, 331, 20);

		txtChartY = new Text(group, SWT.BORDER);
		txtChartY.setText(charY);
		txtChartY.setBounds(81, 85, 331, 20);

		final Group legendGroup = new Group(shell, SWT.NONE);
		legendGroup.setFont(SWTResourceManager.getFont("Comic Sans MS", 10, SWT.NONE));
		legendGroup.setText("Legend");
		legendGroup.setBounds(10, 131, 144, 98);

		final Label LegX = new Label(legendGroup, SWT.NONE);
		LegX.setFont(SWTResourceManager.getFont("Comic Sans MS", 10, SWT.NONE));
		LegX.setText("Leg X :");
		LegX.setBounds(10, 30, 40, 18);

		txtLegX = new Text(legendGroup, SWT.BORDER);
		txtLegX.setText(legX);
		txtLegX.setBounds(75, 30, 53, 20);

		final Label yLabel = new Label(legendGroup, SWT.NONE);
		yLabel.setFont(SWTResourceManager.getFont("Comic Sans MS", 10, SWT.NONE));
		yLabel.setText("Leg Y :");
		yLabel.setBounds(10, 64, 40, 18);

		txtLegY = new Text(legendGroup, SWT.BORDER);
		txtLegY.setText(legY);
		txtLegY.setBounds(75, 60, 53, 20);

		final Group legendGroup_1 = new Group(shell, SWT.NONE);
		legendGroup_1.setBounds(180, 131, 252, 115);
		legendGroup_1.setText("Scale");
		legendGroup_1.setFont(SWTResourceManager.getFont("Comic Sans MS", 10, SWT.NONE));

		final Label LegX_1 = new Label(legendGroup_1, SWT.NONE);
		LegX_1.setFont(SWTResourceManager.getFont("Comic Sans MS", 10, SWT.NONE));
		LegX_1.setBounds(10, 30, 45, 18);
		LegX_1.setText("MinX  :");

		txtMinX = new Text(legendGroup_1, SWT.BORDER);
		txtMinX.setText(minX);
		txtMinX.setBounds(60, 30, 53, 20);

		final Label yLabel_1 = new Label(legendGroup_1, SWT.NONE);
		yLabel_1.setFont(SWTResourceManager.getFont("Comic Sans MS", 10, SWT.NONE));
		yLabel_1.setBounds(10, 63, 45, 18);
		yLabel_1.setText("MaxX  :");

		txtMaxX = new Text(legendGroup_1, SWT.BORDER);
		txtMaxX.setText(maxX);
		txtMaxX.setBounds(60, 60, 53, 20);
		
		final Label minY_1 = new Label(legendGroup_1, SWT.NONE);
		minY_1.setFont(SWTResourceManager.getFont("Comic Sans MS", 10, SWT.NONE));
		minY_1.setBounds(120, 30, 45, 18);
		minY_1.setText("MinY  :");

		txtMinY = new Text(legendGroup_1, SWT.BORDER);
		txtMinY.setText(minY);
		txtMinY.setBounds(170, 30, 53, 20);

		final Label maxY_1 = new Label(legendGroup_1, SWT.NONE);
		maxY_1.setFont(SWTResourceManager.getFont("Comic Sans MS", 10, SWT.NONE));
		maxY_1.setBounds(120, 64, 45, 18);
		maxY_1.setText("MaxY  :");

		txtMaxY = new Text(legendGroup_1, SWT.BORDER);
		txtMaxY.setText(maxY);
		txtMaxY.setBounds(170, 60, 53, 20);
		
		Label lblXScale = new Label(legendGroup_1, SWT.NONE);
		lblXScale.setFont(SWTResourceManager.getFont("Comic Sans MS", 10, SWT.NORMAL));
		lblXScale.setBounds(10, 90, 45, 18);
		lblXScale.setText("X Unit :");
		
		txtScaleY = new Text(legendGroup_1, SWT.BORDER);
		txtScaleY.setText(scaleY);
		txtScaleY.setBounds(170, 90, 53, 20);
		
		Label lblYUnit = new Label(legendGroup_1, SWT.NONE);
		lblYUnit.setText("Y Unit :");
		lblYUnit.setFont(SWTResourceManager.getFont("Comic Sans MS", 10, SWT.NORMAL));
		lblYUnit.setBounds(120, 90, 45, 18);
		
		txtScaleX = new Text(legendGroup_1, SWT.BORDER);
		txtScaleX.setText(scaleX);
		txtScaleX.setBounds(60, 90, 53, 20);
		
		chkGrises = new Button(shell, SWT.CHECK);
		chkGrises.setFont(SWTResourceManager.getFont("Comic Sans MS", 10, SWT.NONE));
		chkGrises.setText("Gray Scale");
		chkGrises.setBounds(10, 235, 100, 20);

		final Label label_3 = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_3.setBounds(0, 255, 438, 13);

		final Button btnAcept = new Button(shell, SWT.NONE);
		btnAcept.setFont(SWTResourceManager.getFont("Comic Sans MS", 10, SWT.NONE));
		btnAcept.setText("Accept");
		btnAcept.setBounds(140, 270, 65, 25);

		final Button btnCancel = new Button(shell, SWT.NONE);
		btnCancel.setFont(SWTResourceManager.getFont("Comic Sans MS", 10, SWT.NONE));
		btnCancel.setBounds(230, 270, 65, 25);
		btnCancel.setText("Close");
		
		btnCancel.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				shell.dispose();
			}
		});
		
		btnAcept.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				//changeImage();
				modifyImage();
				
				ip.setCorrX1(txtMinX.getText());
				ip.setCorrX2(txtMaxX.getText());
				ip.setCorrY1(txtMinY.getText());
				ip.setCorrY2(txtMaxY.getText());
				
				ip.setLabX(txtChartX.getText());
				ip.setLabY(txtChartY.getText());
				ip.setTitle(txtTitle.getText());
				
				ip.setLegX(txtLegX.getText());
				ip.setLegY(txtLegY.getText());
				ip.setScaleY(txtScaleY.getText());
				ip.setScaleX(txtScaleX.getText());
				shell.dispose();
			}
		});
	
	}
	
	private void modifyImage(){
		try {
			c=new RConnection();
			
			c.eval("corrx<-c("+txtMinX.getText()+','+txtMaxX.getText()+")");
			c.eval("corry<-c("+txtMinY.getText()+','+txtMaxY.getText()+")");
			c.eval("legx<-"+txtLegX.getText());
			c.eval("legy<-"+txtLegY.getText());
			c.eval("labx<-"+'"'+ txtChartX.getText() +'"');
			c.eval("laby<-"+'"'+ txtChartY.getText() +'"');
			c.eval("titulo<-"+'"'+ txtTitle.getText() +'"');
			c.eval("png(file=" + '"' +pathImage.replace("\\", "/") +'"'+")");
			c.eval("mini<-"+mini);
			c.eval("maxi<-"+maxi);
			c.eval("scaleY<-"+txtScaleY.getText());
			c.eval("scaleX<-"+txtScaleX.getText());
			
			c.eval("model<-"+model);
						
			if(chkGrises.getSelection())
				c.eval("grises=TRUE");
			else
				c.eval("grises=FALSE");
			
			if(MainPageWizardPage.getSeveralModels())
				DevelopmentRate.estimateParameters(c,model, true, false, "", "", "", "", "", "", "");
			else{
				String str1,str2,str3,str4,str5,str6,str7;
				str1 = DevelopmentRate.pars.getParameters()[0];
				str2 = DevelopmentRate.pars.getParameters()[1];
				str3 = DevelopmentRate.pars.getParameters()[2];
				str4 = DevelopmentRate.pars.getParameters()[3];
				str5 = DevelopmentRate.pars.getParameters()[4];
				str6 = DevelopmentRate.pars.getParameters()[5];
				str7 = DevelopmentRate.pars.getParameters()[6];
				DevelopmentRate.estimateParameters(c,model, false, true, str1, str2, str3, str4, str5, str6, str7);
			}
						
			c.eval("shapprueba<-prueba(model,datashap,datao,ini,corrx,corry,punt="+DevelopmentRate.strMinP+":" + DevelopmentRate.strMaxP + ",labx, laby, titulo)");
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
			
//			c.eval("plot_shape<-grafshape(model,estshap,datashap,sdli,qtt,corrx,corry,mini=0,maxi=100,coefi,limit,1,labx,laby,titulo,grises, scaleY, scaleX)");
			c.eval("plot_shape<-grafshape(model,estshap,datashap,sdli,qtt,corrx,corry,mini,maxi,coefi,limit,1,labx,laby,titulo,grises, scaleY, scaleX)");
			c.eval("dev.off()");
			
			lblImage.setImage(new Image(Display.getCurrent(), pathImage/*.replace('/', '\\')*/));
			c.close();
			
		}catch (RserveException e) {
			c.close();
			e.printStackTrace();
		} catch (Exception e) {
			c.close();
			e.printStackTrace();
		}
	}
}
