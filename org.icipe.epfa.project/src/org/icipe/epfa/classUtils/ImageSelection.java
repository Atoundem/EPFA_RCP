package org.icipe.epfa.classUtils;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;

import org.eclipse.swt.graphics.Image;

/** * A Transferable able to transfer an AWT Image. * Similar to the JDK StringSelection class. */
public class ImageSelection implements Transferable {    
	private Image image;       
	private BufferedImage bfImage;
	public static void copyImageToClipboard(Image image) {
		ImageSelection imageSelection = new ImageSelection(image);
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		toolkit.getSystemClipboard().setContents(imageSelection, null);
	}   
	public static void copyImageToClipboard(BufferedImage image) {
		ImageSelection imageSelection = new ImageSelection(image);
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		toolkit.getSystemClipboard().setContents(imageSelection, null);
	} 
	
	public ImageSelection(Image image) {
		this.image = image;    
		
	}
	public ImageSelection(BufferedImage bfImage) {
		this.bfImage = bfImage;    
		
	}
	
	public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
		if (flavor.equals(DataFlavor.imageFlavor) == false) {
			throw new UnsupportedFlavorException(flavor);
		}        
		return bfImage;    
	}
	
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return flavor.equals(DataFlavor.imageFlavor);
	}       
	
	public DataFlavor[] getTransferDataFlavors() {
		return new DataFlavor[] {
				DataFlavor.imageFlavor        
		};   
	}
}