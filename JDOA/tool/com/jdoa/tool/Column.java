package com.jdoa.tool;


import java.io.Serializable;

public class Column
  implements Serializable
{
  private String header;
  private int width;
  private String name;
  private String align;
  private String renderer;
  private String editor;
  
  public String getEditor() {
	return editor;
}

public void setEditor(String editor) {
	this.editor = editor;
}

public Column(String paramString1, int paramInt, String paramString2, String paramString3, String paramString4,String  paramString5)
  {
    this.header = paramString1;
    this.width = paramInt;
    this.name = paramString2;
    this.align = paramString3;
    this.renderer = paramString4;
    this.editor=paramString5;
  }

  public Column()
  {
  }

  public String getHeader()
  {
    return this.header;
  }

  public void setHeader(String paramString)
  {
    this.header = paramString;
  }

  public int getWidth()
  {
    return this.width;
  }

  public void setWidth(int paramInt)
  {
    this.width = paramInt;
  }

  public String getName()
  {
    return this.name;
  }

  public void setName(String paramString)
  {
    this.name = paramString;
  }

  public String getAlign()
  {
    return this.align;
  }

  public void setAlign(String paramString)
  {
    this.align = paramString;
  }

public String getRenderer() {
	return renderer;
}

public void setRenderer(String renderer) {
	this.renderer = renderer;
}


}