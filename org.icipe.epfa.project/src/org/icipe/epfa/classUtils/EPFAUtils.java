package org.icipe.epfa.classUtils;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DirectColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.List;
/*
import net.refractions.udig.catalog.IService;
import net.refractions.udig.catalog.internal.Messages;
import net.refractions.udig.catalog.internal.ServiceFactoryImpl;
import net.refractions.udig.project.ui.internal.MapFactory;
*/
//import org.cgiar.cip.ilcym.connectiontor.Rserve;
import org.icipe.epfa.connectiontor.Rserve;
import org.icipe.epfa.project.Activator;    //TODO
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.osgi.framework.Bundle;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;
import org.icipe.epfa.project.windows.ViewProjectsUI;

public class EPFAUtils {
	public static void createHTMLfile(String path, String filename){
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(path + File.separator + filename + ".html"));
			BufferedReader in = new BufferedReader(new FileReader(path + File.separator + filename + ".txt"));
			
			bw.write("<HTML>" + "\n");
			bw.write("<PRE>" + "\n");
			
			String strProg123="";
	  		while ((strProg123 = in.readLine()) != null){
	  			bw.write(strProg123);
	  			bw.write("\n");
	  		}
	  		in.close();
	  		
	  		bw.write("</PRE>");
	  		bw.write("</HTML>");
	  		bw.close();
			
		} catch (IOException e) {
			MessageDialog.openError(null, "Error", e.getMessage());
			return;
		}
		
	}
	
	public static BufferedImage convertToAWT(ImageData data) {
	    ColorModel colorModel = null;
	    PaletteData palette = data.palette;
	    if (palette.isDirect) {
	      colorModel = new DirectColorModel(data.depth, palette.redMask, palette.greenMask,
	          palette.blueMask);
	      BufferedImage bufferedImage = new BufferedImage(colorModel, colorModel
	          .createCompatibleWritableRaster(data.width, data.height), false, null);
	      WritableRaster raster = bufferedImage.getRaster();
	      int[] pixelArray = new int[3];
	      for (int y = 0; y < data.height; y++) {
	        for (int x = 0; x < data.width; x++) {
	          int pixel = data.getPixel(x, y);
	          RGB rgb = palette.getRGB(pixel);
	          pixelArray[0] = rgb.red;
	          pixelArray[1] = rgb.green;
	          pixelArray[2] = rgb.blue;
	          raster.setPixels(x, y, 1, 1, pixelArray);
	        }
	      }
	      return bufferedImage;
	    } else {
	      RGB[] rgbs = palette.getRGBs();
	      byte[] red = new byte[rgbs.length];
	      byte[] green = new byte[rgbs.length];
	      byte[] blue = new byte[rgbs.length];
	      for (int i = 0; i < rgbs.length; i++) {
	        RGB rgb = rgbs[i];
	        red[i] = (byte) rgb.red;
	        green[i] = (byte) rgb.green;
	        blue[i] = (byte) rgb.blue;
	      }
	      if (data.transparentPixel != -1) {
	        colorModel = new IndexColorModel(data.depth, rgbs.length, red, green, blue,
	            data.transparentPixel);
	      } else {
	        colorModel = new IndexColorModel(data.depth, rgbs.length, red, green, blue);
	      }
	      BufferedImage bufferedImage = new BufferedImage(colorModel, colorModel
	          .createCompatibleWritableRaster(data.width, data.height), false, null);
	      WritableRaster raster = bufferedImage.getRaster();
	      int[] pixelArray = new int[1];
	      for (int y = 0; y < data.height; y++) {
	        for (int x = 0; x < data.width; x++) {
	          int pixel = data.getPixel(x, y);
	          pixelArray[0] = pixel;
	          raster.setPixel(x, y, pixelArray);
	        }
	      }
	      return bufferedImage;
	    }
	  }
	
	
	
	
	
	//addded to created a temp script file
	
	public static void createTempScriptFile(String pathRScriptFile) {
	      
		String path = ViewProjectsUI.getPathProject();
		 Bundle bundle = Platform.getBundle("org.icipe.epfa.project");
		System.out.println(path);
		System.out.println(pathRScriptFile);
		File tempScripFile = new File(path + File.separator + "tempScripFile.r");
		
		try {
			
		InputStream in = FileLocator.openStream(bundle, new Path(pathRScriptFile), false); 
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
           BufferedWriter writer = new BufferedWriter(new FileWriter(tempScripFile));

			
//		BufferedReader in = new BufferedReader(new FileReader(rScriptFile));
//		BufferedWriter bw = new BufferedWriter(new FileWriter(tempScripFile));
		
//		InputStream in = EPFAUtils.class.getResourceAsStream(pathRScriptFile);         
//        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        
 /*   		
		String line="";
  		while ((line = in.readLine()) != null){
  			bw.write(line);
  			bw.write("\n");
  			System.out.println(line);
  		}
  		
  		in.close();
  		bw.close();
        
*/        
        
    		
        
        //String ligne=null;
        String line;
        while ((line = reader.readLine()) != null) {
            
            writer.write(line);
            writer.newLine();
            
        }
        writer.close();
     
   
  		
  	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			MessageDialog.openError(new Shell(), "EPFA Warning", "Problems while trying to load R Scripts");
		}
    }
	

	public static int MaxValueTable( List<Double> douArray ) {
	       Double max = douArray.get(0);
	       
	       int row=0;
	       for( int j = 0; j < douArray.size(); j++ ) {
	           if (douArray.get(j) > max) {
	               max = douArray.get(j);
	               row=j;
	           }
	       }
	       return row;
	}
	public static int MinValueTable( List<Double> douArray ) {
	       Double min = douArray.get(0);
	       int row=0;
	       for( int j = 0; j < douArray.size(); j++ ) {
	       	if (douArray.get(j) < min) {
		            min = douArray.get(j);
		            row=j;
	           }
	       }
	       return row;
	}

	/** Metodo que devuelve la ubicacion de la carpeta /lib donde se encuentras los archivos con las funciones**/
	public static String getLibPath() {
        String tempString = null;
        
        tempString = Platform.getInstallLocation().getURL().getPath();
        
//        File fileDefaultPath = new File(tempString + File.separator + "lib" + File.separator);
       
        File fileDefaultPath = new File(tempString + File.separator + "RScripts" + File.separator);
        return new String(fileDefaultPath.getAbsolutePath().replace('\\', '/'));
    }

	public static String ChangeFileExt(String cadena, String strExt){
        String NewString = "";        
        int ini = cadena.lastIndexOf("."); 
        NewString = cadena.substring(0, ini) + strExt;        
        return NewString;
    }
	public static String ExtractPath(String strCadena){
        String strExt = null;
        try{
	        int fin = strCadena.lastIndexOf(".");
	        strExt = strCadena.substring(0, fin);
        }catch (Exception e) {
			e.printStackTrace();
		}
        return strExt;
    }

	public static void copyFile(File src, File dst) throws IOException {
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
/*
	public static void addToMap(final String strOutput){
    	
	 	   Job job = new Job("Loading the layer..."){
	 	          @SuppressWarnings("deprecation")
				protected IStatus run( final IProgressMonitor monitor ) {
	 	        	  monitor.worked(1);
	 	        	  if (monitor.isCanceled()) {
	 	        		  return Status.CANCEL_STATUS;
	                  }
	 	        	  try{
	 	        		  File file = new File(strOutput);
	 	        		  List<IService> imagePng;			
	 	        		  try {
	 	        			  imagePng = new ServiceFactoryImpl().acquire(file.toURL());
	 	        			  for( IService service : imagePng ) {
	 	        				  if (service != null) {
	 	        					  if (service.members(null) != null) {
	 	        						  try {
	 	        							  MapFactory.instance().process(null, service.members(null),false);
	 	        							  break;
	                                      } catch (Exception e1) {
	                                          System.err.println(e1.getMessage());
	                                          e1.printStackTrace();
	                                      }
	                                  }
	                              }
	                          }
	              		} catch (MalformedURLException e2) {
	              			e2.printStackTrace();	
	              		} catch (IOException e3) {
	              			e3.printStackTrace();
	              		} 
	          		}catch(Exception e) {
	        			System.err.println(e.getMessage());
	    			}         
	 	            monitor.done(); 
	 	            return  new Status(IStatus.OK, Activator.PLUGIN_ID, IStatus.OK, Messages.bind("ProjectUIPlugin.success",null), null);              
	 	          }       
	 	      };
	 	     job.schedule();
	 } */

	public static void removeAllColumns(Table table) {        
        while (table.getColumnCount() > 0) {
        	int lastOne = table.getColumnCount() - 1;
        	table.getColumn(lastOne).dispose();
        }
    }

	public static void quitaComillasArchivo(String pathFile){
		String newValue = "";
		try {
	        BufferedReader in = new BufferedReader(new FileReader(pathFile));
	        String str;
	        while ((str = in.readLine()) != null) {
	        	newValue += str.replaceAll('"'+"", "")+"\r\n";
	        }
	        in.close();
	        
	        BufferedWriter bw = new BufferedWriter(new FileWriter(pathFile));
	        bw.write(newValue);
	        bw.close();
	    } catch (IOException e) {
	    	e.printStackTrace();
	    	MessageDialog.openError(new Shell(), "Error", e.getMessage());
	    }
	}
	public static void viewOutputTable(String strInput, Table table, String method){
    	try {
	        BufferedReader in = new BufferedReader(new FileReader(strInput));
	        BufferedReader inLine = new BufferedReader(new FileReader(strInput));
	        String str;
	        
	        String[] strArray = null;
	        int intColumnsCount = 0, intRowsCount = 0, intCurrentRow = 0; 
	        
	        while((str = in.readLine()) != null){
                strArray = str.split("\t");
                
                intRowsCount++;
                if(intRowsCount == 1)
                    intColumnsCount = strArray.length;
                
            }
            
            TableColumn[] columnArray = new TableColumn[intColumnsCount];
	        
	        while((str = inLine.readLine()) != null){
                
                strArray = str.split("\t");
                if(intCurrentRow == 0){
                	for(int i = 0; i < intColumnsCount; i++){
                        columnArray[i] = new TableColumn(table, SWT.NONE);
                        if(method.equalsIgnoreCase("deter"))
                        	columnArray[i].setText(strArray[i]);
                    }
                }else{
                    TableItem item = new TableItem(table, SWT.NONE);
                    item.setText(strArray);
                    
                }
                intCurrentRow++;
            }
            
            for(int i = 0; i < intColumnsCount; i++){
                columnArray[i].pack();
            }
	        
	        in.close();
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }
    }

	public class RException extends Exception { public RException(String 
			msg) { super("R error: \""+msg+"\""); } }
			 
	public static REXP safeEval(RConnection c, String s) throws RserveException, 
	RException, REXPMismatchException {
	 REXP r = c.eval("try({" + s + "}, silent=TRUE)");
	 //if (r.inherits("try-error")) throw new RException(r.asString());
	 System.out.println(r.asString());
	 return r;
	 }
	
	public static String[] getTemperatureColumns(String strFile){
		String[] strColumns=null;
		try {
			BufferedReader inLine = new BufferedReader(new FileReader(strFile));
	        String str;
	        int line=1;
	        
	        while((str = inLine.readLine()) != null){
	        	if(line == 7){
	        		strColumns = ArrayConvertions.StringtoArray(str, "\t");
	        		break;
	        	}
	            line++;
	        }
		} catch (Exception e1) {
	    	e1.printStackTrace();
	    }
		
		return strColumns;
	}
	
	public static void getTemperaturesFromFile(String strFile){
		RConnection c = null;
		try {
	        BufferedReader inLine = new BufferedReader(new FileReader(strFile));
	        BufferedWriter bw = new BufferedWriter(new FileWriter(EPFAUtils.ExtractPath(strFile) + "-1.txt"));
	        String str;
	        int line=1;
	        
	        while((str = inLine.readLine()) != null){
	        	if(line > 6)
	        		bw.write(str + "\r\n");
                line++;
            }
            
	        inLine.close();
	        bw.close();
	        
	        if(!Platform.getOS().equalsIgnoreCase("macosx"))
	        	//c=new RConnection();     //TODO: delete this line on remplace by the good one below
				c = Rserve.launchRserve("",c);
			else{
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
			}
	        
	        c.eval("Table <- read.table("+'"'+ EPFAUtils.ExtractPath(strFile).replace("\\", "/") + "-1.txt" +'"'+",header=TRUE)");
	        c.close();
	    } catch (Exception e1) {
	    	e1.printStackTrace();
	    	c.close();
	    }
	}
	
	/*public static void reloadTemps(String strFile){
		
			RConnection c = null;
			try {
	       
		        if(!Platform.getOS().equalsIgnoreCase("macosx"))
					c = Rserve.launchRserve("",c);
				else{
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
				}
		        
		        c.eval("Table <- read.table("+'"'+ IlcymUtils.ExtractPath(strFile).replace("\\", "/") + "-1.txt" +'"'+",header=TRUE)");
		        c.close();
		    } catch (Exception e1) {
		    	e1.printStackTrace();
		    	c.close();
		    }
		
	}*/
			
			
}
