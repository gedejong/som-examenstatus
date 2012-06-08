package com.jeroensteenbeeke.iridium.examenstatus.core;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

public final class ChallengeUtil
{
	private ChallengeUtil()
	{

	}

	public static String determineSolution(String userId, String challenge, String secretWord)
			throws ExamenStatusException
	{
		try
		{
			return sha1Sum(String.format("%s-%s-%s", challenge, secretWord, userId).getBytes());
		}
		catch (NoSuchAlgorithmException e)
		{
			throw new ExamenStatusException("Unable to compute solution");
		}
	}

	public static String sha1Sum(byte[] convertme) throws NoSuchAlgorithmException
	{
		MessageDigest md = MessageDigest.getInstance("SHA-1");
		return byteArray2Hex(md.digest(convertme));
	}

	private static String byteArray2Hex(final byte[] hash)
	{
		Formatter formatter = new Formatter();
		for (byte b : hash)
		{
			formatter.format("%02x", b);
		}
		return formatter.toString();
	}
}
