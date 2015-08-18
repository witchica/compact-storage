package com.tattyseal.compactstorage.event;

import com.tattyseal.compactstorage.CompactStorage;

public class CSRegisterCompatEvent 
{
	public CompactStorage cs;
	
	public CSRegisterCompatEvent(CompactStorage cs)
	{
		this.cs = cs;
	}
}
