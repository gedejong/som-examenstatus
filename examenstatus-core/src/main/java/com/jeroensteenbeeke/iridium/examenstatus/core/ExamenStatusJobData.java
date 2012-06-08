package com.jeroensteenbeeke.iridium.examenstatus.core;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "job")
public class ExamenStatusJobData implements Serializable
{
	private static final long serialVersionUID = 1L;

	@XmlElement(name = "omschrijving", required = true)
	public String omschrijving;

	@XmlElement(name = "status", required = true)
	public String status;

	@XmlElement(name = "type", required = true)
	public String type;

	@XmlElement(name = "wachtrijstatus", required = true)
	public String wachtrijstatus;

	@XmlElement(name = "progressie", required = true)
	public int progressie;
}