package com.feihua.jjcb.phone.bean;

/**
 * Created by wcj on 2016-04-19.
 */
public class QueryBean
{

    private int iconId;
    private String name;
    private String content;

    public QueryBean(int iconId, String name, String content)
    {
        this.iconId = iconId;
        this.name = name;
        this.content = content;
    }

    public int getIconId()
    {
        return iconId;
    }

    public String getName()
    {
        return name;
    }

    public String getContent()
    {
        return content;
    }
}
