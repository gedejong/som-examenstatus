package nl.topicus.iridium.examenwarroom.viewer;

import org.apache.wicket.protocol.http.WebApplication;

public class ExamenWarRoomApplication extends WebApplication
{
	/**
	 * @see org.apache.wicket.Application#getHomePage()
	 */
	@Override
	public Class<SelectionPage> getHomePage()
	{
		return SelectionPage.class;
	}
}
