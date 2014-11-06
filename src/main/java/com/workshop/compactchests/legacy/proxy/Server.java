package com.workshop.compactchests.legacy.proxy;

/**
 * Created by Toby on 25/08/2014.
 */
public class Server implements IProxy
{
    @Override
    public void registerRenderers()
    {

    }

    @Override
    public String side()
    {
        return "SERVER";
    }
}
