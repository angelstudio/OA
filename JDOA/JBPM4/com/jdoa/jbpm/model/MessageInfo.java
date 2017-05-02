package com.jdoa.jbpm.model;

public class MessageInfo
{
    private String id;
    private String type;
    private String sender;
    private String prority;
    private String status;
    private String title;
    private String body;
    private String sendTime;
    private String receiveTime;
    private String receiver;
    private String bizid;
    private String biztype;
    private String fromtype;
    private String ftaskId;
    private String fexecutionId;
    private String fmessageType;

    public String getFexecutionId()
    {
        return fexecutionId;
    }

    public void setFexecutionId(String fexecutionId)
    {
        this.fexecutionId = fexecutionId;
    }

    public String getId()
    {
        return id;
    }

    public String getBosType()
    {
        // 获取单据Type方法。以后添加。
        return "JBPM0003";
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getSender()
    {
        return sender;
    }

    public void setSender(String sender)
    {
        this.sender = sender;
    }

    public String getPrority()
    {
        return prority;
    }

    public void setPrority(String prority)
    {
        this.prority = prority;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getBody()
    {
        return body;
    }

    public void setBody(String body)
    {
        this.body = body;
    }

    public String getSendTime()
    {
        return sendTime;
    }

    public void setSendTime(String sendTime)
    {
        this.sendTime = sendTime;
    }

    public String getReceiveTime()
    {
        return receiveTime;
    }

    public void setReceiveTime(String receiveTime)
    {
        this.receiveTime = receiveTime;
    }

    public String getReceiver()
    {
        return receiver;
    }

    public void setReceiver(String receiver)
    {
        this.receiver = receiver;
    }

    public String getBizid()
    {
        return bizid;
    }

    public void setBizid(String bizid)
    {
        this.bizid = bizid;
    }

    public String getBiztype()
    {
        return biztype;
    }

    public void setBiztype(String biztype)
    {
        this.biztype = biztype;
    }

    public String getFromtype()
    {
        return fromtype;
    }

    public void setFromtype(String fromtype)
    {
        this.fromtype = fromtype;
    }

    public String getFtaskId()
    {
        return ftaskId;
    }

    public void setFtaskId(String ftaskId)
    {
        this.ftaskId = ftaskId;
    }

    public String getFmessageType()
    {
        return fmessageType;
    }

    public void setFmessageType(String fmessageType)
    {
        this.fmessageType = fmessageType;
    }

}
