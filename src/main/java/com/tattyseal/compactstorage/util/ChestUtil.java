package com.tattyseal.compactstorage.util;

public class ChestUtil 
{
	public static int clamp(int val, int min, int max) 
	{
	    return Math.max(min, Math.min(max, val));
	}
}
