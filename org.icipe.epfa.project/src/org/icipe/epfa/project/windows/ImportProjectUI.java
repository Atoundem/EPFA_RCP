package org.icipe.epfa.project.windows;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

public class ImportProjectUI extends Dialog {
	private Text text;
	protected Object result;

	protected Shell shell;
	String strfont = "Comic Sans MS";

	/**
	 * Create the dialog
	 * @param parent
	 * @param style
	 */
	public ImportProjectUI(Shell parent, int style) {
		super(parent, style);
	}

	/**
	 * Create the dialog
	 * @param parent
	 */
	public ImportProjectUI(Shell parent) {
		this(parent, SWT.NONE);
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
	
	protected void createContents() {
		shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shell.setSize(500, 172);
		shell.setText("Import existing project into workspace");

		final Button browseButton = new Button(shell, SWT.NONE);
		browseButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				text.setText("");
				DirectoryDialog dialog= new DirectoryDialog(new Shell(), SWT.OPEN | SWT.MULTI);
				dialog.open();
		        
		        text.setText(dialog.getFilterPath());
				
			}
		});
		browseButton.setFont(SWTResourceManager.getFont(strfont, 10, SWT.NONE));
		browseButton.setText("&Browse :");
		browseButton.setBounds(10, 30, 85, 25);

		text = new Text(shell, SWT.BORDER);
		text.setBounds(101, 30, 383, 25);

		final Button cancelButton = new Button(shell, SWT.NONE);
		cancelButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				shell.dispose();
			}
		});
		cancelButton.setFont(SWTResourceManager.getFont(strfont, 10, SWT.NONE));
		cancelButton.setText("&Cancel");
		cancelButton.setBounds(410, 110, 75, 25);

		final Button importButton = new Button(shell, SWT.NONE);
		importButton.setFont(SWTResourceManager.getFont(strfont, 10, SWT.NONE));
		importButton.setBounds(325, 110, 75, 25);
		importButton.setText("&Import");

		final Button copyIntoWorkspaceButton = new Button(shell, SWT.CHECK);
		copyIntoWorkspaceButton.setSelection(true);
		copyIntoWorkspaceButton.setEnabled(false);
		copyIntoWorkspaceButton.setFont(SWTResourceManager.getFont(strfont, 10, SWT.NONE));
		copyIntoWorkspaceButton.setText("Copy into workspace");
		copyIntoWorkspaceButton.setBounds(10, 61, 145, 16);

		final ProgressBar progressBar = new ProgressBar(shell, SWT.NONE);
		progressBar.setBounds(10, 84, 474, 10);
		
		importButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				File srcDir = new File(text.getText());
				File dstDir = new File(Platform.getLocation().toFile().getAbsolutePath()+File.separator + srcDir.getName());
				try {
					copyDirectory(srcDir, dstDir, progressBar);
					changeParametersInProject(srcDir, dstDir);
					
					MessageDialog.openInformation(shell, "Importing projects", "the project was imported successfully");
					shell.dispose();
					ViewProjectsUI.refreshTree();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
			}
		});
		
	}
	
	public void copyDirectory(File srcDir, File dstDir, ProgressBar pbar) throws IOException {
		if (srcDir.isDirectory()) {
			if (!dstDir.exists()) {
				dstDir.mkdir();
			} 
			String[] children = srcDir.list();
			for (int i=0; i<children.length; i++) {
				copyDirectory(new File(srcDir, children[i]), new File(dstDir, children[i]), pbar);
				pbar.setSelection(i * 100/children.length);
			}
		} else { // This method is implemented in Copying a File  
			copyFile(srcDir, dstDir); 
		}
	}
	
	void copyFile(File src, File dst) throws IOException {
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
	
	void changeParametersInProject(File srcDir, File dstDir) throws FileNotFoundException, IOException{
		File fileParaIn = new File(srcDir + File.separator + "Data" + File.separator + "Parameters.r");
		File fileParaOut = new File(dstDir + File.separator + "Data" + File.separator + "Parameters.r");
		
		BufferedReader br = new BufferedReader(new FileReader(fileParaIn));
        String str, strOut="", src="", dst="";
        
        dst = dstDir.toString().replace("\\", "/");
        while ((str = br.readLine()) != null) {
        	if(str.contains("/Data/")){
        		src = str.substring(str.lastIndexOf(":/")-1, str.lastIndexOf("/Data/"));
        		str = str.replace(src, dst);
        	}
        	strOut += str+"\r\n";
        }
        br.close();
        
        BufferedWriter bw = new BufferedWriter(new FileWriter(fileParaOut));
        bw.write(strOut);
        bw.close();
       
		
	}

}
