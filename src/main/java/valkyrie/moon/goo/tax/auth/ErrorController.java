package valkyrie.moon.goo.tax.auth;

import java.io.IOException;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public final class ErrorController {

	@ExceptionHandler(NoHandlerFoundException.class)
	public String handleNoHandlerFoundException(final NoHandlerFoundException ex) {
		return "notFoundPage";
	}

	@ExceptionHandler(IOException.class)
	public String handleAbortedConnection(final IOException ex) {
		// avoids compile/runtime dependency by using class name
		if (ex.getClass().getName().equals("org.apache.catalina.connector.ClientAbortException")) {
			return null;
		}

		return "errorPage";
	}

	@ExceptionHandler(Exception.class)
	public String handleException(final Exception ex) {

		return "errorPage";
	}
}
