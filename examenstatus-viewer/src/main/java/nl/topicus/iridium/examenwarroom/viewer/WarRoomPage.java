package nl.topicus.iridium.examenwarroom.viewer;

import java.util.List;

import nl.topicus.examenwarroom.util.TokenHasher;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.bootstrap.Bootstrap;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.resource.JQueryResourceReference;
import org.odlabs.wiquery.ui.themes.WiQueryCoreThemeResourceReference;

import com.google.common.collect.Lists;

public class WarRoomPage extends WebPage
{

	private static final long serialVersionUID = 1L;

	private static final String[] bodyScripts = {"js/tools.js", "js/config.js",
		"js/examenwarroom.js", "js/takenpanel.js"};

	private final Omgeving omgeving;

	private final String secret;

	public WarRoomPage(Omgeving omgeving, String secret)
	{
		this.omgeving = omgeving;

		this.secret = secret;

		add(new ListView<String>("bodyScripts", Lists.newArrayList(bodyScripts))
		{

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<String> item)
			{
				final String ref = item.getModelObject();

				final String url =
					new StringBuilder().append(
						urlFor(new JavaScriptResourceReference(WarRoomPage.class, ref),
							new PageParameters())).toString();

				item.add(new WebMarkupContainer("script").add(AttributeModifier.replace("src", url)));

			}

		});
	}

	@Override
	public void renderHead(IHeaderResponse response)
	{
		super.renderHead(response);

		Bootstrap.renderHeadResponsive(response);

		response.render(JavaScriptHeaderItem.forReference(JQueryResourceReference.get()));
		response.render(CssHeaderItem.forReference(WiQueryCoreThemeResourceReference.get()));

		response.render(CssHeaderItem.forReference(new CssResourceReference(WarRoomPage.class,
			"js/progressbar/bootstrap-progressbar.min.css")));
		response.render(CssHeaderItem.forReference(new CssResourceReference(WarRoomPage.class,
			"css/style.css")));

		response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(
			WarRoomPage.class, "js/progressbar/bootstrap-progressbar.min.js")));
		response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(
			WarRoomPage.class, "js/underscore1.4.2.min.js")));
		response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(
			WarRoomPage.class, "js/d3.v2.min.js")));

		StringBuilder builder = new StringBuilder();

		builder.append("var authToken = '").append(TokenHasher.createToken(secret)).append("';\n");

		builder.append("var warroom = {\n");
		builder.append("\ttakenArray : [],\n");
		builder.append("\tfirstTimeGrafiekDraw: true,\n");
		builder.append("\taantalTakenTeDraaien: 0,\n");
		builder.append("\taantalTakenVoltooid: 0,\n");
		builder.append("\tindexTaakIdArray: [],\n");
		builder.append("\tstraten : [\n");

		int index = 1;

		List<EndpointDescriptor> descriptors = omgeving.getDescriptors();

		for (EndpointDescriptor desc : descriptors)
		{

			builder.append("\t\t{\n");
			builder.append("\t\t\tid : ").append(index++).append(",\n");
			builder.append("\t\t\turl : '");
			builder.append(desc.getUrl());
			builder.append("'\n");
			builder.append("\t\t}");

			if (index <= descriptors.size())
			{
				builder.append(",");
			}
			builder.append("\n");
		}

		builder.append("\t],\n");
		builder.append("\tgecrashteTaken : [],\n");
		builder.append("\ttakenMetAlertBolletje: [],\n");
		builder.append("\tstraatCounterGecrashteTaken: []\n");
		builder.append("}");

		response.render(JavaScriptHeaderItem.forScript(builder, "warroom-config"));

		response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(
			WarRoomPage.class, "js/d3.v2.min.js")));
	}
}
