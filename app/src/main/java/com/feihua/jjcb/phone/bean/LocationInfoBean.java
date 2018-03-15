package com.feihua.jjcb.phone.bean;

import java.util.List;

/**
 * Created by wcj on 2016-03-31.
 */
public class LocationInfoBean
{
    public String isUpdate;
    public List<TimeInfo> TIME;

    public class TimeInfo{
        public String STRAT_TIME;
        public String END_TIME;
    }

}
