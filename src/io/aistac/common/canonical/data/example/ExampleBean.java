/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/*
 * @(#)ExampleBean.java
 *
 * Copyright:	Copyright (c) 2016
 * Company:		Oathouse.com Ltd
 */
package io.aistac.common.canonical.data.example;

import io.aistac.common.canonical.data.ObjectBean;
import io.aistac.common.canonical.data.ObjectEnum;
import java.util.LinkedList;
import java.util.List;
import org.jdom2.Element;

/**
 * The {@code ExampleBean} Class
 *
 * @author Darryl Oatridge
 * @version 1.00 20-Mar-2016
 */
public class ExampleBean extends ObjectBean {

    private static final long serialVersionUID = 100L;

    private volatile int value;
    private volatile String name;

    public ExampleBean(int identifier, int value, String name, String owner) {
        super(identifier, owner);
        this.value = value;
        this.name = name;
    }

    public ExampleBean() {
        super();
        this.value = ObjectEnum.INITIALISATION.value();
        this.name = "";
    }

    public int getId() {
        return super.getIdentifier();
    }

    public int getKey() {
        return super.getGroupKey();
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) {
            return false;
        }
        if(getClass() != obj.getClass()) {
            return false;
        }
        final ExampleBean other = (ExampleBean) obj;
        if(this.value != other.getValue()) {
            return false;
        }
        if((this.name == null) ? (other.getName() != null) : !this.name.equals(other.getName())) {
            return false;
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + this.value;
        hash = 89 * hash + (this.name != null ? this.name.hashCode() : 0);
        return hash + super.hashCode();
    }

    /**
     * crates all the elements that represent this bean at this level.
     * @return List of elements in order
     */
    @Override
    public List<Element> getXMLElement() {
        List<Element> rtnList = new LinkedList<>();
        // create and add the content Element
        super.getXMLElement().stream().forEach((e) -> {
            rtnList.add(e);
        });
        Element bean = new Element("ExampleBean");
        rtnList.add(bean);
        // set the data
        bean.setAttribute("value", Integer.toString(value));
        bean.setAttribute("name", name);

        bean.setAttribute("serialVersionUID", Long.toString(serialVersionUID));
        return(rtnList);
    }

    /**
     * sets all the values in the bean from the XML. Remember to
     * put default values in getAttribute() and check the content
     * of getText() if you are parsing to a value.
     *
     * @param root element of the DOM
     */
    @Override
    public void setXMLDOM(Element root) {
        // extract the super meta data
        super.setXMLDOM(root);
        // extract the bean data
        Element bean = root.getChild("ExampleBean");
        // set up the data
        value = Integer.parseInt(bean.getAttributeValue("value","-1"));
        name = bean.getAttributeValue("name","");
    }

}
