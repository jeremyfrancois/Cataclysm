package fr.meijin.cataclysm.webapp.context;

import java.net.URL;
import java.security.ProtectionDomain;

import org.eclipse.jetty.webapp.WebAppContext;

public class AppContextBuilder {

	private WebAppContext webAppContext;
	
	public WebAppContext buildWebAppContext(){
		webAppContext = new WebAppContext();
		ProtectionDomain protectionDomain = AppContextBuilder.class.getProtectionDomain();
	    URL location = protectionDomain.getCodeSource().getLocation();

		System.out.println(location.toExternalForm());
		webAppContext.setDescriptor(location.toExternalForm() + "/WEB-INF/web.xml");
		
		webAppContext.setWar(location.toExternalForm());
		webAppContext.setContextPath("/Cataclysm");
		webAppContext.setParentLoaderPriority(true);
		return webAppContext;
	}
}
