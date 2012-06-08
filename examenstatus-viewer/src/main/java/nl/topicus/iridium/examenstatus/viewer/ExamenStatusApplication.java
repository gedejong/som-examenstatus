package nl.topicus.iridium.examenstatus.viewer;


import org.apache.wicket.protocol.http.WebApplication;

/**
 * Application object for your web application. If you want to run this application without deploying, run the Start class.
 * 
 * @see nl.topicus.iridium.examenstatus.viewer.StartExamenStatusApplication#main(String[])
 */
public class ExamenStatusApplication extends WebApplication
{    	
	/**
	 * @see org.apache.wicket.Application#getHomePage()
	 */
	@Override
	public Class<ViewerPage> getHomePage()
	{
		return ViewerPage.class;
	}

	/**
	 * @see org.apache.wicket.Application#init()
	 */
	@Override
	public void init()
	{
		super.init();

		// add your configuration here
	}
}
