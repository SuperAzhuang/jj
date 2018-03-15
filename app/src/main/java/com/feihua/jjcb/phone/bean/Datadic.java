package com.feihua.jjcb.phone.bean;

/**
 * Created by wcj on 2016-04-19.
 */
public class Datadic
{
    public String datadic_name;
    public String datadic_value;

    public Datadic(String datadic_name, String datadic_value)
    {
        this.datadic_name = datadic_name;
        this.datadic_value = datadic_value;
    }

    public String getDatadic_name()
    {
        return datadic_name == null ? "" : datadic_name.trim();
    }

    public String getDatadic_value()
    {
        return datadic_value == null ? "" : datadic_value.trim();
    }
}
