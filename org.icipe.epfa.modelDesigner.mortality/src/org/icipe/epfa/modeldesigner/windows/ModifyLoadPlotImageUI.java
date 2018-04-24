package org.icipe.epfa.modeldesigner.windows;



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

public class ModifyLoadPlotImageUI extends Dialog {

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
	
	//private String mini, maxi;
	
//	public int model;

	protected Shell shell;

	/**
	 * Create the dialog
	 * @param parent
	 * @param style
	 */
	public ModifyLoadPlotImageUI(Shell parent, int style) {
		super(parent, style);
	}

	/**
	 * Create the dialog
	 * @param parent
	 */
	public ModifyLoadPlotImageUI(Shell parent) {
		this(parent, SWT.NONE);
	}

	RConnection c; 
	String strEval; String pathImage; Label lblImage;
	String minX; String maxX; String minY; String maxY; String charX;
	String charY; String title; String legX; String legY;
//	public static ImageProperties ip = new ImageProperties();
	public int sims;
	
	public void ModifyImageUIVar(String pathImage, Label lblImage,
								 String minX, String maxX, String minY, String maxY, String mini, String maxi,
								 String charX, String charY, String title, String legX, String legY) {
		
		this.pathImage = pathImage;
		this.lblImage = lblImage;
		
		this.minX = minX; this.maxX=maxX; this.minY=minY; this.maxY=maxY; this.charX=charX;
		this.charY=charY;this.title=title;this.legX=legX;this.legY=legY;
		
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
		legendGroup_1.setBounds(180, 131, 252, 98);
		legendGroup_1.setText("Scale");
		legendGroup_1.setFont(SWTResourceManager.getFont("Comic Sans MS", 10, SWT.NONE));

		final Label LegX_1 = new Label(legendGroup_1, SWT.NONE);
		LegX_1.setFont(SWTResourceManager.getFont("Comic Sans MS", 10, SWT.NONE));
		LegX_1.setBounds(10, 30, 40, 18);
		LegX_1.setText("MinX :");

		txtMinX = new Text(legendGroup_1, SWT.BORDER);
		txtMinX.setText(minX);
		txtMinX.setBounds(55, 30, 53, 20);

		final Label yLabel_1 = new Label(legendGroup_1, SWT.NONE);
		yLabel_1.setFont(SWTResourceManager.getFont("Comic Sans MS", 10, SWT.NONE));
		yLabel_1.setBounds(10, 64, 42, 18);
		yLabel_1.setText("MaxX :");

		txtMaxX = new Text(legendGroup_1, SWT.BORDER);
		txtMaxX.setText(maxX);
		txtMaxX.setBounds(55, 60, 53, 20);
		
		final Label minY_1 = new Label(legendGroup_1, SWT.NONE);
		minY_1.setFont(SWTResourceManager.getFont("Comic Sans MS", 10, SWT.NONE));
		minY_1.setBounds(113, 30, 42, 18);
		minY_1.setText("MinY :");

		txtMinY = new Text(legendGroup_1, SWT.BORDER);
		txtMinY.setText(minY);
		txtMinY.setBounds(158, 30, 53, 20);

		final Label maxY_1 = new Label(legendGroup_1, SWT.NONE);
		maxY_1.setFont(SWTResourceManager.getFont("Comic Sans MS", 10, SWT.NONE));
		maxY_1.setBounds(113, 64, 42, 18);
		maxY_1.setText("MaxY :");

		txtMaxY = new Text(legendGroup_1, SWT.BORDER);
		txtMaxY.setText(maxY);
		txtMaxY.setBounds(158, 60, 53, 20);
		
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
				changeImage();
/*				
				ip.setCorrX1(txtMinX.getText());
				ip.setCorrX2(txtMaxX.getText());
				ip.setCorrY1(txtMinY.getText());
				ip.setCorrY2(txtMaxY.getText());
				
				ip.setLabX(txtChartX.getText());
				ip.setLabY(txtChartY.getText());
				ip.setTitle(txtTitle.getText());
				
				ip.setLegX(txtLegX.getText());
				ip.setLegY(txtLegY.getText());
				
*/
				shell.dispose();
			}
		});
	
	}
	
	private void changeImage(){
		try {
			c=new RConnection();
			c.eval("corrx<-c("+txtMinX.getText()+','+txtMaxX.getText()+")");
			c.eval("corry<-c("+txtMinY.getText()+','+txtMaxY.getText()+")");
			
			c.eval("legx<-"+txtLegX.getText());
			c.eval("legy<-"+txtLegY.getText());
			
			c.eval("labx<-"+'"'+ txtChartX.getText() +'"');
			c.eval("laby<-"+'"'+ txtChartY.getText() +'"');
			
			c.eval("titulo<-"+'"'+ txtTitle.getText() +'"');
			
			c.eval("corrx2=seq(min(corrx),max(corrx),legx)");
			c.eval("corry2=seq(min(corry),max(corry),legy)");
			
			
		
			c.eval("png(file=" + '"' +pathImage.replace("\\", "/") +'"'+", width = 480, height = 480)");
			
			c.eval("plot(datm[,1],datm[,2]*100,frame=T,pch=19,cex=1.3,col=4,xlab=labx,ylab=laby,main=titulo,axes=F,xaxt = 'n',ylim=corry,xlim=corrx);axis(1, corrx2);axis(2, corry2,las=2)");
			
			/*	
			c.eval("legx<-"+legX);
			c.eval("legy<-"+legY);
			
			c.eval("model<-"+model);
			
			
			if(chkGrises.getSelection())
				c.eval("grises=TRUE");
			else
				c.eval("grises=FALSE");
			
			if(MainPageWizardPage.getSeveralModels())
				Mortality.estimateParameters(c,model, true, false, "", "", "", "", "","");
			else{
				String str1,str2,str3,str4,str5, str6;
				str1 = Mortality.pars.getParameters()[0];
				str2 = Mortality.pars.getParameters()[1];
				str3 = Mortality.pars.getParameters()[2];
				str4 = Mortality.pars.getParameters()[3];
				str5 = Mortality.pars.getParameters()[4];
				str6 = Mortality.pars.getParameters()[5];
				Mortality.estimateParameters(c,model, false, true, str1, str2, str3, str4, str5, str6);
			}
			
			c.eval("pbmortal<-pruebamortal(" + '"' + "mortal" + '"' + ",modelm,datm,inim,corrx,corry,mini,maxi,labx,laby,titulo)");
			c.eval("inim<-pbmortal$ini");
			//c.eval("weights");
			c.eval("fmort<-dead_func(" + '"' + "mortal" + '"' +",modelm, datm, alg, inim, pesos,weights)");
			c.eval("estimor<-fmort$estimados");
			c.eval("g<-fmort$ecua");
			c.eval("gg<-fmort$f");
			c.eval("stdmortg <-  fmort$stdmort");
			c.eval("modelo<- fmort$modelo");
			c.eval("modelim<-fmort$modelo");
			c.eval("sol_mort<-coef_mort(" + '"' + "mortal" + '"' +",modelm,estimor, stdmortg, modelo,modelim,datm,alg,pesos,weights)");
			c.eval("plot_mort<-grafmort(" + '"' + "mortal" + '"' + ",modelm,estimor,g,datm,corrx,corry,mini,maxi,limit,1,labx,laby,titulo,grises)");
			
	*/		
			c.eval("dev.off()");
			
			lblImage.setImage(new Image(Display.getCurrent(), pathImage));
			

			c.close();
		} catch (RserveException e) {
			c.close();
			e.printStackTrace();
		} catch (Exception e) {
			c.close();
			e.printStackTrace();
		}
		
		
	}

}
