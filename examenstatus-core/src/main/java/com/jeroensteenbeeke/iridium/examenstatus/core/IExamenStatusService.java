package com.jeroensteenbeeke.iridium.examenstatus.core;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;


import org.apache.cxf.feature.Features;

@WebService()
@Features(features = "org.apache.cxf.transport.http.gzip.GZIPFeature")
public interface IExamenStatusService
{

	@WebMethod
	String issueChallenge(@WebParam(name = "userId") String userId) throws ExamenStatusException;

	@WebMethod
	List<ExamenStatusJobData> getRunningExamenJobs(@WebParam(name = "challenge") String challenge, @WebParam(
			name = "solution") String solution) throws ExamenStatusException;

}