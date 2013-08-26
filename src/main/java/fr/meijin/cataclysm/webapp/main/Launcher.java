package fr.meijin.cataclysm.webapp.main;

import java.util.Calendar;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;

import fr.meijin.cataclysm.webapp.context.AppContextBuilder;
import fr.meijin.cataclysm.webapp.server.JettyServer;

public class Launcher extends Thread {

	private JettyServer jettyServer;
	
	public Launcher(JettyServer jettyServer) {
		super();
		this.jettyServer = jettyServer;
	}

	@Override
	public void run() {
		System.out.println("Creating ContextHandler");
		ContextHandlerCollection contexts = new ContextHandlerCollection();
		System.out.println("Building Web Application Context");
		contexts.setHandlers(new Handler[] { new AppContextBuilder().buildWebAppContext()});
		System.out.println("Setting Handler");
		jettyServer.setHandler(contexts);
		try {
			System.out.println("Starting jetty");
			long t1 = Calendar.getInstance().getTimeInMillis();
			jettyServer.start();
			long t2 = Calendar.getInstance().getTimeInMillis();
			System.out.println("Server started in : "+(t2-t1)+" ms");
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
}
