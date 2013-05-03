package nl.topicus.iridium.examenwarroom.viewer;

import java.util.List;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.CssResourceReference;

import com.google.common.collect.Lists;

public class SelectionPage extends WebPage
{
	private static final long serialVersionUID = 1L;

	public SelectionPage()
	{

		List<Omgeving> omgevingen = Lists.newArrayList(Omgeving.values());

		final DropDownChoice<Omgeving> omgevingSelect =
			new DropDownChoice<Omgeving>("omgeving", Model.of(Omgeving.LOCAL), omgevingen);
		omgevingSelect.setRequired(true);
		omgevingSelect.setNullValid(false);

		final PasswordTextField secretField = new PasswordTextField("secret", Model.of(""));
		secretField.setRequired(true);

		Form<Omgeving> form = new Form<Omgeving>("form")
		{

			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit()
			{
				super.onSubmit();

				Omgeving omgeving = omgevingSelect.getModelObject();
				String secret = secretField.getModelObject();

				setResponsePage(new WarRoomPage(omgeving, secret));
			}

		};

		form.add(omgevingSelect);
		form.add(secretField);

		add(form);
	}

	@Override
	public void renderHead(IHeaderResponse response)
	{
		super.renderHead(response);

		response.render(CssHeaderItem.forReference(new CssResourceReference(SelectionPage.class,
			"css/selection.css")));
	}
}
