package com.workshop.compactstorage.event;

import com.workshop.compactstorage.essential.CompactStorage;

public class CSRegisterCompatEvent 
{
	public CompactStorage cs;
	
	public CSRegisterCompatEvent(CompactStorage cs)
	{
		this.cs = cs;
	}
}
