package fr.meijin.cataclysm.webapp.context;

import java.net.URL;
import java.security.ProtectionDomain;
import java.util.Calendar;

import org.eclipse.jetty.webapp.WebAppContext;

public class AppContextBuilder {

	private WebAppContext webAppContext;
	
	public WebAppContext buildWebAppContext(){
		long t1 = Calendar.getInstance().getTimeInMillis();
		System.out.println("Start building WebAppContext");
		webAppContext = new WebAppContext();
		ProtectionDomain protectionDomain = AppContextBuilder.class.getProtectionDomain();
	    URL location = protectionDomain.getCodeSource().getLocation();

		System.out.println(location.toExternalForm());
		webAppContext.setDescriptor(location.toExternalForm() + "/WEB-INF/web.xml");
		
		webAppContext.setWar(location.toExternalForm());
		webAppContext.setContextPath("/Cataclysm");
		webAppContext.setParentLoaderPriority(true);
		long t2 = Calendar.getInstance().getTimeInMillis();
		System.out.println("Finish building WebAppContext in : "+(t2-t1)+" ms");
		return webAppContext;
	}
}
