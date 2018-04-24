package org.icipe.epfa;

import net.refractions.udig.internal.ui.UDIGApplication;
import net.refractions.udig.internal.ui.UDIGWorkbenchAdvisor;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.ui.application.WorkbenchAdvisor;

public class EPFA extends UDIGApplication implements IApplication 
{
	
	@Override
	protected WorkbenchAdvisor createWorkbenchAdvisor() {
		return new UDIGWorkbenchAdvisor() {
				@Override
				public String getInitialWindowPerspectiveId() {
				//	return "org.icipe.epfa.MappingPerspective";
					return "org.icipe.epfa.modelBuilderPerspective";
					
					
				}
		};
	}

}
