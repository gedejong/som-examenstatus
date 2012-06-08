package com.jeroensteenbeeke.iridium.examenstatus.core;

import javax.xml.ws.WebFault;

@WebFault
public class ExamenStatusException extends Exception
{
	private static final long serialVersionUID = 1L;

	public ExamenStatusException(String message)
	{
		super(message);
	}
}