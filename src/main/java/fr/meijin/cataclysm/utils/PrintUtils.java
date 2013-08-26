package fr.meijin.cataclysm.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import fr.meijin.cataclysm.model.Round;

public class PrintUtils {

	public static File exportRound(Round round) throws Exception {

		File file = new File("Ronde"+round.roundNumber+".html");

		Properties properties = new Properties();
		properties.put("input.encoding", "utf-8");
		properties.setProperty("resource.loader", "class");
		properties.setProperty("class.resource.loader.class",
			"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("round", round);
		BufferedWriter out = new BufferedWriter(new FileWriter(file));

		VelocityEngine engine = new VelocityEngine();
		engine.init(properties);
		Template template = engine.getTemplate("/round.html");

		// Create a context and add parameters
		VelocityContext context = new VelocityContext(parameters);

		// Render the template for the context into a string
		StringWriter stringWriter = new StringWriter();
		template.merge(context, stringWriter);
		String content = stringWriter.toString();
			
		out.write(content);
		out.close();

		return file;
	}
}
