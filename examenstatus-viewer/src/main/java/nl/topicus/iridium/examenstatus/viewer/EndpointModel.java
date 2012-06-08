package nl.topicus.iridium.examenstatus.viewer;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import org.apache.wicket.model.AbstractReadOnlyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.jeroensteenbeeke.iridium.examenstatus.core.ChallengeUtil;
import com.jeroensteenbeeke.iridium.examenstatus.core.ExamenStatusException;
import com.jeroensteenbeeke.iridium.examenstatus.core.ExamenStatusJobData;
import com.jeroensteenbeeke.iridium.examenstatus.core.IExamenStatusService;

public class EndpointModel extends AbstractReadOnlyModel<List<ExamenStatusJobData>>
{
	private static final long serialVersionUID = 1L;

	private static final Logger log = LoggerFactory.getLogger(EndpointModel.class);

	private final String url;

	private boolean error = false;

	public EndpointModel(String url)
	{
		super();
		this.url = url;
	}

	public boolean isError()
	{
		return error;
	}

	@Override
	public List<ExamenStatusJobData> getObject()
	{
		try
		{
			error = false;
			URL endpointURL = new URL(url);
			QName serviceName = new QName("http://services.mijnsom.nl", "ExamenStatusService");

			Service service = Service.create(endpointURL, serviceName);
			IExamenStatusService port = service.getPort(IExamenStatusService.class);

			final String username = System.getProperty("examenstatus.username");
			final String secretWord = System.getProperty("examenstatus.secretwird");

			String challenge = port.issueChallenge(username);

			return port.getRunningExamenJobs(challenge,
				ChallengeUtil.determineSolution(username, challenge, secretWord));
		}
		catch (MalformedURLException e)
		{
			log.error(e.getMessage(), e);
			error = true;
			return Lists.newArrayList();
		}
		catch (ExamenStatusException e)
		{
			log.error(e.getMessage(), e);
			error = true;
			return Lists.newArrayList();
		}
		catch (Exception sf)
		{
			log.error(sf.getMessage(), sf);
			error = true;
			return Lists.newArrayList();
		}

	}
}
