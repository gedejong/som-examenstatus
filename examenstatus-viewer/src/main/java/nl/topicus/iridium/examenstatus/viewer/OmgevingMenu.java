package nl.topicus.iridium.examenstatus.viewer;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.ListModel;

import com.google.common.collect.Lists;

public class OmgevingMenu extends Panel
{
	private static final long serialVersionUID = 1L;

	public OmgevingMenu(String id, DefaultOmgeving omgeving)
	{
		super(id, Model.of(omgeving));

		add(new ListView<DefaultOmgeving>("omgevingen", new ListModel<DefaultOmgeving>(
			Lists.newArrayList(DefaultOmgeving.values())))
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<DefaultOmgeving> item)
			{
				DefaultOmgeving omgeving = item.getModelObject();
				if (omgeving.equals(OmgevingMenu.this.getDefaultModelObject()))
				{
					item.add(AttributeModifier.append("class", "active"));
				}

				Link<DefaultOmgeving> link = new Link<DefaultOmgeving>("link", Model.of(omgeving))
				{
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick()
					{
						setResponsePage(new ViewerPage(getModelObject()));

					}
				};
				link.setBody(Model.of(omgeving.getPrefix()));

				item.add(link);

			}
		});
	}
}
