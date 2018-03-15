package com.feihua.jjcb.phone.navi;

/**
 * Created by wcj on 2016-09-05.
 */
public class Node
{
    private double lon;
    private double lat;
    private String address;

    public Node(double lon,double lat,String address){
        this.lon = lon;
        this.lat = lat;
        this.address = address;
    }

    public double getLon()
    {
        return lon;
    }

    public double getLat()
    {
        return lat;
    }

    public String getAddress()
    {
        return address;
    }
}
