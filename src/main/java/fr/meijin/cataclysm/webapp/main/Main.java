package fr.meijin.cataclysm.webapp.main;

import java.awt.Desktop;
import java.net.URI;

import fr.meijin.cataclysm.webapp.server.JettyServer;
import fr.meijin.cataclysm.webapp.server.ServerRunner;

public class Main {
	
	public static void main(String[] args) {
		
		final JettyServer jettyServer = new JettyServer();
		
		Launcher launcher = new Launcher(jettyServer);
		try {
			
			launcher.start();
			new ServerRunner(jettyServer);
			
			Desktop d = Desktop.getDesktop();
			d.browse(new URI("http://localhost:8180/Cataclysm"));
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
}