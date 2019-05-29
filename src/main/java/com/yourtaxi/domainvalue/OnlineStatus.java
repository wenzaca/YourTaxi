package com.yourtaxi.domainvalue;

public enum OnlineStatus
{
    ONLINE("ONLINE"), OFFLINE("OFFLINE");

    private String status;


    OnlineStatus(String status)
    {

        this.status = status;
    }


    public String getStage()
    {

        return status;
    }
}
