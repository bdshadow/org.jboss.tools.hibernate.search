package org.jboss.tools.hibernate.search.console;

import java.lang.reflect.Field;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.osgi.util.NLS;
import org.hibernate.console.ConsoleConfigClassLoader;
import org.hibernate.console.ConsoleConfiguration;
import org.hibernate.eclipse.console.HibernateConsoleMessages;
import org.hibernate.eclipse.console.HibernateConsolePlugin;

public class ConsoleConfigurationUtils {
	
	public static ClassLoader getClassLoader(ConsoleConfiguration cc) {
		try {
			Field loaderField = cc.getClass().getDeclaredField("classLoader");
			loaderField.setAccessible(true);
			return (ConsoleConfigClassLoader)loaderField.get(cc);
			
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e1) {
			return null;
		}
	}
	
	public static void loadSessionFactorySafely(ConsoleConfiguration cc) {
		if (cc.getSessionFactory() == null) {
			if (!cc.hasConfiguration() && askUserForConfiguration(cc.getName())) {
				cc.build();
			}
			cc.buildSessionFactory();
		}
	}
	
	private static boolean askUserForConfiguration(String name) {
		String out = NLS.bind(HibernateConsoleMessages.AbstractQueryEditor_do_you_want_open_session_factory, name);
		return MessageDialog.openQuestion( HibernateConsolePlugin.getDefault()
				.getWorkbench().getActiveWorkbenchWindow().getShell(),
				HibernateConsoleMessages.AbstractQueryEditor_open_session_factory, out );
	}
}