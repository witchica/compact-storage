package com.workshop.compactstorage.exception;

import org.apache.commons.lang3.ArrayUtils;

public class InvalidSizeException extends Exception
{
	public InvalidSizeException(String message) 
	{
		super(message);
	}
}
