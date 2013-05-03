package nl.topicus.examenwarroom.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.joda.time.DateMidnight;

public class TokenHasher
{
	private TokenHasher()
	{

	}

	public static String createToken(String secret)
	{
		DateMidnight dm = DateMidnight.now();

		String preHash =
			String.format("key-%s-%d-%d-%d", secret, dm.getDayOfMonth(), dm.getMonthOfYear(),
				dm.getYear());

		return toSHA1(preHash);
	}

	private static String toSHA1(String input)
	{
		MessageDigest md = null;
		try
		{
			md = MessageDigest.getInstance("SHA-1");
			byte[] digest = md.digest(input.getBytes());

			return byteArrayToHexString(digest);
		}
		catch (NoSuchAlgorithmException e)
		{
			return null;
		}

	}

	private static String byteArrayToHexString(byte[] b)
	{
		String result = "";
		for (int i = 0; i < b.length; i++)
		{
			result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
		}
		return result;
	}
}
