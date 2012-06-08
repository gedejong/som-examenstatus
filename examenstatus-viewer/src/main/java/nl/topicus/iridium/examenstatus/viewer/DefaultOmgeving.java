package nl.topicus.iridium.examenstatus.viewer;

import java.util.List;

import com.google.common.collect.Lists;

public enum DefaultOmgeving
{
	LOCAL(0, "Lokaal")
	{
		@Override
		public List<EndpointDescriptor> getDescriptors()
		{
			EndpointDescriptor lokaal = new EndpointDescriptor();
			lokaal.setName("Lokale Server");
			lokaal.setUrl("http://localhost:8080/som/services/examenStatus?wsdl");

			return Lists.newArrayList(lokaal);
		}
	},
	ACCEPTATIE(1, "Acceptatie")
	{
		@Override
		public List<EndpointDescriptor> getDescriptors()
		{
			EndpointDescriptor acceptatie = new EndpointDescriptor();
			acceptatie.setName("Acceptatie");
			acceptatie.setUrl("https://acceptatie.mijnsom.nl/services/examenStatus?wsdl");

			return Lists.newArrayList(acceptatie);
		}
	},

	TEST(7, "Test", "https://test%d.mijnsom.nl/services/examenStatus?wsdl"),
	PRODUCTIE(7, "Productie", "https://start%d.mijnsom.nl/services/examenStatus?wsdl")

	;

	private final int omgevingen;

	private final String prefix;

	private final String urlPattern;

	private DefaultOmgeving(int omgevingen, String prefix)
	{
		this(omgevingen, prefix, "");
	}

	private DefaultOmgeving(int omgevingen, String prefix, String urlPattern)
	{
		this.omgevingen = omgevingen;
		this.prefix = prefix;
		this.urlPattern = urlPattern;
	}

	public String getPrefix()
	{
		return prefix;
	}

	public List<EndpointDescriptor> getDescriptors()
	{
		final List<EndpointDescriptor> descriptors = Lists.newArrayListWithExpectedSize(omgevingen);

		for (int i = 1; i <= omgevingen; i++)
		{
			EndpointDescriptor desc = new EndpointDescriptor();
			desc.setName(String.format("%s straat %d", prefix, i));
			desc.setUrl(String.format(urlPattern, i));
			descriptors.add(desc);
		}

		return descriptors;

	}
}
