package de.accso.library.testutil;

import org.h2.tools.Server;
import org.junit.rules.ExternalResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

/**
 * JUnit {@link org.junit.rules.TestRule} zum Starten und Stoppen des Console Web Servers für die H2-Datenbank
 * unter http://localhost:{@value H2ConsoleServerRule#CONSOLE_WEB_PORT}
 *
 * @author delfs
 */
public class H2ConsoleServerRule extends ExternalResource {

	private static final Logger LOGGER = LoggerFactory.getLogger(H2ConsoleServerRule.class);

	private static final int CONSOLE_WEB_PORT = 8082;

	private Server consoleWebServer;

	public H2ConsoleServerRule() {
		try {
			this.consoleWebServer = Server.createWebServer("-webAllowOthers", "-webPort", String.valueOf(CONSOLE_WEB_PORT));
		} catch (SQLException ex) {
			LOGGER.warn("H2 Console Web Server kann nicht erzeugt werden: " + ex);
		}
	}

	@Override
	protected void before() {
		try {
			consoleWebServer.start();
		} catch (SQLException ex) {
			LOGGER.warn("H2 Console Web Server kann nicht gestartet werden: " + ex);
		}
	}

	@Override
	protected void after() {
		consoleWebServer.stop();
	}

}
