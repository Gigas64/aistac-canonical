/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/*
 * @(#)ObjectBeanTestList.java
 *
 * Copyright:	Copyright (c) 2010
 * Company:		Oathouse.com Ltd
 */
package io.aistac.common.canonical.data;


import io.aistac.common.canonical.data.example.ExampleBean;

/**
 * The {@code ObjectBeanTestList} Enumeration
 *
 * @author Darryl Oatridge
 * @version 1.00 22-Jan-2011
 */
public enum ObjectBeanTestList {
   // example
    ExampleBean(ExampleBean.class.getName(),false);

    private String cls;
    private boolean printXml;

    private ObjectBeanTestList(String cls, boolean printXml) {
        this.cls = cls;
        this.printXml = printXml;
    }

    public String getCls() {
        return cls;
    }

    public boolean isPrintXml() {
        return printXml;
    }
}
