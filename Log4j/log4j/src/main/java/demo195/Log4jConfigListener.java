package demo195;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.logging.log4j.core.config.Configurator;

public class Log4jConfigListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		String log4jConfigFile = sce.getServletContext().getRealPath("/WEB-INF/log4j2.xml");
		Configurator.initialize(null, log4jConfigFile);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// Clean up resources if needed
	}
}