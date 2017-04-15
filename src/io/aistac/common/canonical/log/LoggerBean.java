/*
 * @(#)LoggerBean.java
 *
 * Copyright:       Copyright (c) 2017
 * Organisation:    opengrass.io aistac.io oatridge.io
 * Schema:          Adaptive, Intelligent, Single Task Application Concern (AI-STAC)
 */
package io.aistac.common.canonical.log;

import io.aistac.common.canonical.data.ObjectBean;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import org.jdom2.Element;

/**
 * The {@code LoggerBean} Class
 *
 * @author Darryl Oatridge
 * @version 1.00 22-Mar-2016
 */
public class LoggerBean extends ObjectBean {

    private static final long serialVersionUID = 100L;
    private volatile String tag;
    private volatile String message;

    public LoggerBean() {
        super();
        this.tag = "";
        this.message = "";
    }

    public LoggerBean(int identifier, String tag, String msg, String owner) {
        super(identifier, owner);
        this.tag = tag == null? "" : tag;
        this.message = msg == null? "" : msg;
    }

    public int getId() {
        return super.getIdentifier();
    }

    public String getTag() {
        return tag;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 19 * hash + Objects.hashCode(this.tag);
        hash = 19 * hash + Objects.hashCode(this.message);
        return hash + super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        }
        if(obj == null) {
            return false;
        }
        if(getClass() != obj.getClass()) {
            return false;
        }
        final LoggerBean other = (LoggerBean) obj;
        if(!Objects.equals(this.tag, other.tag)) {
            return false;
        }
        if(!Objects.equals(this.message, other.message)) {
            return false;
        }
        return super.equals(obj);
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
        Element bean = new Element("LoggerBean");
        rtnList.add(bean);
        // set the data
        bean.setAttribute("tag", tag);
        bean.setAttribute("message", message);
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
        Element bean = root.getChild("LoggerBean");
        // set up the data
        tag = bean.getAttributeValue("tag", "");
        message = bean.getAttributeValue("message", "");

    }
}
