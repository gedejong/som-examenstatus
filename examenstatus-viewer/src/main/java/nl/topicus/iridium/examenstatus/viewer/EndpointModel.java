package nl.topicus.iridium.examenstatus.viewer;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import org.apache.wicket.model.AbstractReadOnlyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
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

	private static final Map<String, IExamenStatusService> services = Maps.newConcurrentMap();

	private List<ExamenStatusJobData> data = null;

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
	public synchronized List<ExamenStatusJobData> getObject()
	{
		if (data == null)
		{
			try
			{
				error = false;
				if (!services.containsKey(url))
				{
					URL endpointURL = new URL(url);
					QName serviceName =
						new QName("http://services.mijnsom.nl", "ExamenStatusService");

					Service service = Service.create(endpointURL, serviceName);

					services.put(url, service.getPort(IExamenStatusService.class));
				}

				IExamenStatusService port = services.get(url);

				final String username = System.getProperty("examenstatus.username");
				final String secretWord = System.getProperty("examenstatus.secretword");

				String challenge = port.issueChallenge(username);

				data =
					port.getRunningExamenJobs(challenge,
						ChallengeUtil.determineSolution(username, challenge, secretWord));
			}
			catch (MalformedURLException e)
			{
				log.error(e.getMessage(), e);
				error = true;
				data = Lists.newArrayList();
			}
			catch (ExamenStatusException e)
			{
				log.error(e.getMessage(), e);
				error = true;
				data = Lists.newArrayList();
			}
			catch (Exception sf)
			{
				log.error(sf.getMessage(), sf);
				error = true;
				data = Lists.newArrayList();
			}
		}

		return data;

	}

	@Override
	public void detach()
	{
		super.detach();
		data = null;
	}
}
