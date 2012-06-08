package nl.topicus.iridium.examenstatus.viewer;

import java.util.List;

import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;

import com.google.common.collect.Lists;

public class ViewerPage extends WebPage
{
	private static final long serialVersionUID = 1L;

	public ViewerPage()
	{
		this(DefaultOmgeving.LOCAL);
	}

	public ViewerPage(DefaultOmgeving omgeving)
	{

		List<List<EndpointDescriptor>> stagedList = createStagedList(omgeving.getDescriptors(), 2);

		add(new OmgevingMenu("menu", omgeving));

		add(new ListView<List<EndpointDescriptor>>("stages", stagedList)
		{

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<List<EndpointDescriptor>> item)
			{
				item.add(new ListView<EndpointDescriptor>("panels", item.getModelObject())
				{
					private static final long serialVersionUID = 1L;

					@Override
					protected void populateItem(ListItem<EndpointDescriptor> item)
					{
						item.add(new EndPointObserverPanel("panel", item.getModelObject()));
					}
				});

			}

		});
	}

	private List<List<EndpointDescriptor>> createStagedList(List<EndpointDescriptor> endpoints,
			int itemsPerStage)
	{
		List<List<EndpointDescriptor>> stagedList =
			Lists.newArrayListWithExpectedSize(endpoints.size() / itemsPerStage);

		List<EndpointDescriptor> curr = Lists.newArrayListWithExpectedSize(itemsPerStage);

		for (EndpointDescriptor d : endpoints)
		{
			curr.add(d);

			if (curr.size() == itemsPerStage)
			{
				stagedList.add(curr);
				curr = Lists.newArrayListWithExpectedSize(itemsPerStage);
			}
		}

		if (!curr.isEmpty())
			stagedList.add(curr);
		return stagedList;
	}

	@Override
	public void renderHead(IHeaderResponse response)
	{
		super.renderHead(response);

		response.renderCSSReference("css/style.css");

		if (getApplication().usesDevelopmentConfig())
		{
			response.renderCSSReference("css/bootstrap.css");
			response.renderCSSReference("css/bootstrap-responsive.css");
			response.renderJavaScriptReference("js/bootstrap.js");
		}
		else
		{
			response.renderCSSReference("css/bootstrap.min.css");
			response.renderCSSReference("css/bootstrap-responsive.min.css");
			response.renderJavaScriptReference("js/bootstrap.min.js");
		}
	}
}
