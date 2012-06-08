package nl.topicus.iridium.examenstatus.viewer;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.time.Duration;

import com.jeroensteenbeeke.iridium.examenstatus.core.ExamenStatusJobData;

public class EndPointObserverPanel extends Panel
{
	private static final long serialVersionUID = 1L;

	public EndPointObserverPanel(String id, EndpointDescriptor descriptor)
	{
		super(id, new CompoundPropertyModel<EndpointDescriptor>(descriptor));

		add(new Label("name"));

		final EndpointModel em = new EndpointModel(descriptor.getUrl());
		em.getObject(); // Init label

		final Label errorLabel = new Label("error", Model.of("Kon geen taak-informatie ophalen"));
		errorLabel.setOutputMarkupId(true);
		errorLabel.setOutputMarkupPlaceholderTag(true);
		errorLabel.setVisible(em.isError());
		add(errorLabel);

		WebMarkupContainer updatingContainer = new WebMarkupContainer("container");
		updatingContainer.setOutputMarkupId(true);
		updatingContainer.add(new AjaxSelfUpdatingTimerBehavior(Duration.seconds(10))
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onPostProcessTarget(AjaxRequestTarget target)
			{
				super.onPostProcessTarget(target);
				if (em.isError())
				{
					errorLabel.setVisible(true);
				}
				else
				{
					errorLabel.setVisible(false);
				}
				target.add(errorLabel);
			}
		});

		add(updatingContainer);

		updatingContainer.add(new ListView<ExamenStatusJobData>("jobs", em)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<ExamenStatusJobData> item)
			{
				ExamenStatusJobData jobData = item.getModelObject();
				item.add(new Label("num", Model.of(item.getIndex() + 1)));
				item.add(new Label("type", jobData.type));
				item.add(new Label("status", jobData.status));
				item.add(new Label("omschrijving", jobData.omschrijving));
				item.add(new Label("wachtrijStatus", jobData.wachtrijstatus));
				item.add(new Label("progressie", String.format("%d%%", jobData.progressie)));

			}
		});
	}
}
