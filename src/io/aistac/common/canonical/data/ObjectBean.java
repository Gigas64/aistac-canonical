/**
 * @(#)ObjectBean.java
 *
 * Copyright:       Copyright (c) 2017
 * Organisation:    opengrass.io aistac.io oatridge.io
 * Schema:          Adaptive, Intelligent, Single Task Application Concern (AI-STAC)
 */
package io.aistac.common.canonical.data;

import io.aistac.common.canonical.exceptions.ObjectBeanException;
import io.aistac.common.canonical.properties.TaskPropertiesService;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import static java.util.Arrays.asList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.input.sax.XMLReaders;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

/**
 * The {@code ObjectBean} Class is a generalised class for Beans
 * using the ObjectManager. This class allows a identifier to be
 * stored and referenced within the ObjectManager. This class is
 * an Abstract class as you must implement setXMLDOM() and getXMLDOM()
 * to add the content of class to a constructed or extracted XML Document
 * the child class creates. as an example the following code would be included
 * if you had a bean with one attribute called myInt
 *
 * <blockquote>
 * <pre>
 *   public Document getXMLDOM() {
 *       <font style="color:grey;">// set up the root elements </font>
 *       Element root = new Element("MyBeanName");
 *       Document doc = new Document(root);
 *       <font style="color:grey;">// set the class name for persisence</font>
 *       root.setAttribute("class", this.getClass().getName());
 *       <font style="color:grey;">// add the root elements</font>
 *       root.addContent(super.getXMLDOM().detachRootElement());
 *       <font style="color:grey;">// add the bean elements</font>
 *       Element bean = new Element("bean");
 *       root.addContent(bean);
 *       bean.setAttribute("myInt", Integer.toString(myInt));
 *       <font style="color:grey;">// now return the Document</font>
 *       return (doc);
 *   }
 *
 *   public void setXMLDOM(Element root) {
 *       <font style="color:grey;">// extract the super meta data</font>
 *       super.setXMLDOM(root.getChild("meta"));
 *       <font style="color:grey;">// extract the bean data</font>
 *       Element bean = root.getChild("bean");
 *       myInt = Integer.parseInt(bean.getAttributeValue("myInt"));
 *   }
 * </pre>
 * </blockquote>
 *
 * @author 	Darryl Oatridge
 * @version 1.03 13-July-2010
 */
public abstract class ObjectBean implements Serializable, Comparable<ObjectBean> {

    public static final String ROOTNAME = TaskPropertiesService.getProp("base.objectbean.xml.root", "Oathouse");
    private static final long serialVersionUID = 20100713103L;
    private volatile int identifier;
    private volatile int groupKey;
    private volatile int index;
    private volatile long created;
    private volatile long modified;
    private volatile String owner;

    @SuppressWarnings("PublicInnerClass")
    public enum XmlFormat {
        PRETTY, TRIMMED, PRINTED;

        public boolean isIn(XmlFormat[] array) {
            return (asList(array).contains(this));
        }
    }

    /**
     * Used when needing to create a blank bean for loading from persistence
     */
    protected ObjectBean() {
        this.identifier = ObjectEnum.INITIALISATION.value();
        this.groupKey = ObjectEnum.INITIALISATION.value();
        this.index = ObjectEnum.INITIALISATION.value();
        this.created = ObjectEnum.INITIALISATION.value();
        this.modified = ObjectEnum.INITIALISATION.value();
        this.owner = "";
    }

    /**
     * Constructor for a bean providing the bean identifier, groupKey, and owner.
     * The create and modify times are set automatically.
     *
     * @param identifier a numeric identifier for this bean
     * @param groupKey a numeric group key for this bean
     * @param owner the owner or creator of the bean
     */
    protected ObjectBean(int identifier, int groupKey, String owner) {
        this.identifier = identifier;
        this.groupKey = groupKey;
        this.index = ObjectEnum.DEFAULT_VALUE.value();
        this.created = System.currentTimeMillis();
        this.modified = this.created;
        this.setOwner(owner);
    }

    /**
     * Constructor for a bean providing the bean identifier and owner.
     * The create and modify times are set automatically. The groupKey
     * is set to ObjectEnum.DEFAULT_KEY.value()
     *
     * @param identifier a numeric identifier for this bean
     * @param owner the owner or creator of the bean
     */
    public ObjectBean(int identifier, String owner) {
        this.identifier = identifier;
        this.groupKey = ObjectEnum.DEFAULT_KEY.value();
        this.index = ObjectEnum.DEFAULT_VALUE.value();
        this.created = System.currentTimeMillis();
        this.modified = this.created;
        this.setOwner(owner);
    }

    /**
     * Internal constructor for constructing beans information from XML
     */
    private ObjectBean(int identifier, int groupKey, int index, long created, long modified, String owner) {
        this.identifier = identifier;
        this.groupKey = groupKey;
        this.index = index;
        this.created = created;
        this.modified = modified;
        this.owner = owner;
    }

    /*
     * The object identifier
     */
    public int getIdentifier() {
        return identifier;
    }

    /*
     * The object groupKey
     */
    public int getGroupKey() {
        return groupKey;
    }

    /*
     * The object index
     */
    public int getIndex() {
        return index;
    }

    /*
     * The object creation time in milliseconds
     */
    public long getCreated() {
        return created;
    }

    /*
     * The object modification time in milliseconds when the object was last changed
     */
    public long getModified() {
        return modified;
    }

    /*
     * The object owner who last changed the object
     */
    public String getOwner() {
        return owner;
    }

    @SuppressWarnings("FinalMethod")
    protected final void setIdentifier(int identifier) {
        this.identifier = identifier;
    }

    @SuppressWarnings("FinalMethod")
    protected final void setIndex(int index) {
        this.index = index;
    }

    @SuppressWarnings("FinalMethod")
    protected final void setGroupKey(int groupKey) {
        this.groupKey = groupKey;
    }

    @SuppressWarnings("FinalMethod")
    protected final void setModified() {
        this.modified = System.currentTimeMillis();
    }

    @SuppressWarnings("FinalMethod")
    protected final void setCreated(long created) {
        this.created = created;
    }

    @SuppressWarnings("FinalMethod")
    public final void setOwner(String owner) {
        if(owner == null || owner.isEmpty()) {
            throw new IllegalArgumentException("The 'owner' parameter of objects extending ObjectBean must have a value");
        }
        this.owner = owner;
    }

    /**
     * @return true if the identifier of this ObjectBean is the DEFAULT_ID
     * @see ObjectEnum
     */
    public boolean isDefaultId() {
        return this.getIdentifier() == ObjectEnum.DEFAULT_ID.value();
    }

    /**
     * Provides a safe way of fully cloning an ObjectBean T and allows the new {@code ObjectBean} to be allocated a new identifier
     * key and owner.
     *
     * @param <T> the objectBean type
     * @param identifier the new identifier for the clone
     * @param key the new key for the clone
     * @param owner the new owner of the clone
     * @return the cloned {@code ObjectBean} T
     */
    public <T extends ObjectBean> T clone(int identifier, int key, String owner) {
        T rtnOb;
        try {
            rtnOb = T.buildObjectBean(this.toXML());
        } catch(ObjectBeanException ex) {
            return null;
        }
        rtnOb.setIdentifier(identifier);
        rtnOb.setGroupKey(key);
        rtnOb.setOwner(owner);
        return (rtnOb);
    }

    /**
     * Provides a safe way of fully cloning an ObjectBean T and allows the new {@code ObjectBean} to be allocated a new identifier.
     *
     * @param <T> the objectBean type
     * @param identifier the new identifier for the clone
     * @return the cloned {@code ObjectBean} T
     */
    public <T extends ObjectBean> T clone(int identifier) {
        T rtnOb;
        try {
            rtnOb = T.buildObjectBean(this.toXML());
        } catch(ObjectBeanException ex) {
            return null;
        }
        rtnOb.setIdentifier(identifier);
        return (rtnOb);
    }

    /**
     * Compares both the groupKey and the identifier. If the groupKey is different then this is
     * compared. If the groupKey's are equal then the identifier is compared.
     *
     * @param other the ObjectBean to compare
     * @return -1 if less, 0 if the same, 1 if greater
     */
    @Override
    public int compareTo(ObjectBean other) {
        if(this.groupKey != other.getGroupKey()) {
            return (this.groupKey < other.getGroupKey() ? -1 : 1);
        }
        return (this.identifier < other.getIdentifier() ? -1 : (this.identifier == other.getIdentifier() ? 0 : 1));
    }

    /**
     * Compares the index of this bean with the other bean
     *
     * @param other the ObjectBean to compare
     * @return -1 if less, 0 if the same, 1 if greater
     */
    public int indexTo(ObjectBean other) {
        return (this.index < other.getIndex()? -1 : (this.index == other.getIndex() ? 0 : 1));
    }

    /**
     * A comparator which imposes a total ordering on some collection of ObjectBean objects by
     * the modify date of the ObjectBean.
     */
    public static final Comparator<ObjectBean> MODIFIED_ORDER = (ObjectBean c1, ObjectBean c2) -> {
        if(c1 == null && c2 == null) {
            return 0;
        }
        // just in case there are null object values show them last
        if(c1 != null && c2 == null) {
            return -1;
        }
        if(c1 == null && c2 != null) {
            return 1;
        }
        // compare
        @SuppressWarnings("null")
        int modComp = c1.getModified() < c2.getModified() ? -1 : (c1.getModified() == c2.getModified() ? 0 : 1);
        if(modComp != 0) {
            return modComp;
        }
        // Modify not unique so violates the equals comparability. Can cause disappearing objects in Sets
        return (c1.getIdentifier() < c2.getIdentifier() ? -1 : (c1.getIdentifier() == c2.getIdentifier() ? 0 : 1));
    };

    /**
     * A comparator which imposes a total ordering on some collection of ObjectBean objects by
     * the modify date of the ObjectBean in reverse order.
     */
    public static final Comparator<ObjectBean> REVERSE_MODIFIED_ORDER = (ObjectBean c1, ObjectBean c2) -> {
        if(c1 == null && c2 == null) {
            return 0;
        }
        // just in case there are null object values show them last
        if(c1 != null && c2 == null) {
            return 1;
        }
        if(c1 == null && c2 != null) {
            return -1;
        }
        // compare
        @SuppressWarnings("null")
        int modComp = c2.getModified() < c1.getModified() ? -1 : (c2.getModified() == c1.getModified() ? 0 : 1);
        if(modComp != 0) {
            return modComp;
        }
        // Modify not unique so violates the equals comparability. Can cause disappearing objects in Sets
        return (c2.getIdentifier() < c1.getIdentifier() ? -1 : (c2.getIdentifier() == c1.getIdentifier() ? 0 : 1));
    };

    /**
     * A comparator which imposes a total ordering on some collection of ObjectBean objects by
     * the index of the ObjectBean.
     */
    public static final Comparator<ObjectBean> INDEX_ORDER = (ObjectBean a1, ObjectBean a2) -> {
        if(a1 == null && a2 == null) {
            return 0;
        }
        // just in case there are null object values show them last
        if(a1 != null && a2 == null) {
            return 1;
        }
        if(a1 == null && a2 != null) {
            return -1;
        }
        // compare the ywd
        @SuppressWarnings("null")
        int ywdComp = a2.getIndex() < a1.getIndex() ? -1 : (a2.getIndex() == a1.getIndex() ? 0 : 1);
        if(ywdComp != 0) {
            return ywdComp;
        }
        // order and create not unique so violates the equals comparability. Can cause disappearing objects in Sets
        return (a2.getIdentifier() < a1.getIdentifier() ? -1 : (a2.getIdentifier() == a1.getIdentifier() ? 0 : 1));
    };

    @Override
    public boolean equals(Object obj) {
        if(obj == null) {
            return false;
        }
        if(this.getClass() != obj.getClass()) {
            return false;
        }
        final ObjectBean other = (ObjectBean) obj;
        return this.identifier == other.getIdentifier();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.identifier;
        return hash;
    }

    /**
     * Method constructing a JDOM Document.
     * @return JDOM Document.
     */
    public Document getXMLDOM() {
        // set up the root elements
        Element root = new Element(ROOTNAME);
        Document doc = new Document(root);
        // set the class name for persisence
        root.setAttribute("class", this.getClass().getName());
        // add the root elements
        this.getXMLElement().stream().forEach((e) -> {
            root.addContent(e);
        });
        return (doc);
    }

    /**
     * crates all the elements that represent this bean at this level.
     * @return List of elements in order
     */
    public List<Element> getXMLElement() {
        List<Element> rtnList = new LinkedList<>();
        // create and add the content Element
        Element bean = new Element("ObjectBean");
        rtnList.add(bean);
        // set the data
        bean.setAttribute("identifier", Integer.toString(identifier));
        bean.setAttribute("groupKey", Integer.toString(groupKey));
        bean.setAttribute("index", Integer.toString(index));
        bean.setAttribute("created", Long.toString(created));
        bean.setAttribute("modified", Long.toString(modified));
        bean.setAttribute("owner", owner);
        bean.setAttribute("serialVersionUID", Long.toString(serialVersionUID));
        return (rtnList);
    }

    /**
     * Method attempts to fill in the bean from a JDOM Element and sub elements
     * @param root element of the DOM
     */
    public void setXMLDOM(Element root) {
        // extract the bean data
        Element bean = root.getChild("ObjectBean");
        // set up the data
        identifier = Integer.parseInt(bean.getAttributeValue("identifier", Integer.toString(ObjectEnum.INITIALISATION.value())));
        groupKey = Integer.parseInt(bean.getAttributeValue("groupKey", Integer.toString(ObjectEnum.INITIALISATION.value())));
        index = Integer.parseInt(bean.getAttributeValue("index", Integer.toString(ObjectEnum.INITIALISATION.value())));
        created = Long.parseLong(bean.getAttributeValue("created", Long.toString(ObjectEnum.INITIALISATION.value())));
        modified = Long.parseLong(bean.getAttributeValue("modified", Long.toString(ObjectEnum.INITIALISATION.value())));
        owner = bean.getAttributeValue("owner", "NO_XML_OWNER");
    }

    /**
     * returns a String representation of bean as XML. By default the XML will be compacted.
     *
     * @param formatArgs the XmlFormat enum array of arguments
     * @return XML as string
     */
    public synchronized String toXML(XmlFormat... formatArgs) {
        XMLOutputter serializer;

        if(XmlFormat.PRETTY.isIn(formatArgs)) {
            serializer = new XMLOutputter(Format.getPrettyFormat().setEncoding("utf-8").setIndent("  "));
        } else {
            serializer = new XMLOutputter(Format.getCompactFormat().setEncoding("utf-8"));
        }
        StringBuilder xmlString = new StringBuilder(serializer.outputString(getXMLDOM()));
        if(!XmlFormat.PRETTY.isIn(formatArgs)) {
            int start = xmlString.indexOf("?>") + 2;
            int end = xmlString.indexOf("<" + ROOTNAME);
            xmlString.delete(start, end);
        }
        if(XmlFormat.TRIMMED.isIn(formatArgs)) {
            int i = xmlString.indexOf("<" + ROOTNAME);
            xmlString.delete(0, i);
        }
        if(XmlFormat.PRINTED.isIn(formatArgs)) {
            int i = xmlString.indexOf("\">") + 2;
            xmlString.delete(0, i);
            String name = this.getClass().getSimpleName();
            xmlString.insert(0, "<" + ROOTNAME + " class=\"" + name + "\">");
        }
        return (xmlString.toString().trim());
    }

    /**
     * builds an {@code ObjectBean} T from a given XML string
     *
     * @param <T> the ObjectBean type
     * @param xmlString the XML string to convert
     * @return returns an {@code ObjectBean} of type T
     * @throws ObjectBeanException when the XML can't be converted
     */
    public static synchronized <T extends ObjectBean> T buildObjectBean(String xmlString) throws ObjectBeanException {
        try {
            if(xmlString == null || xmlString.isEmpty()) {
                throw new NullPointerException("Unable to build the ObjectBean beacuse the XML string is NULL or empty");
            }
            // initialise the xml builder
            final SAXBuilder builder = new SAXBuilder();
            builder.setXMLReaderFactory(XMLReaders.NONVALIDATING);
            Document doc = null;
            Object newInstance = null;
            try {
                doc = builder.build(new StringReader(xmlString));
                Class<?> myClass = Class.forName(doc.getRootElement().getAttributeValue("class"));
                newInstance = myClass.newInstance();
            } catch(JDOMException jde) {
                throw new ObjectBeanException("Unable to build ObjectBean from XML string: " + jde.getMessage());
            } catch(IOException ioe) {
                throw new ObjectBeanException("IO error when building ObjectBean from XML string: " + ioe.getMessage());
            } catch(ClassNotFoundException cnfe) {
                throw new ObjectBeanException("Class not found : " + cnfe.getMessage());
            } catch(InstantiationException ie) {
                throw new ObjectBeanException("Instanciation error : " + ie.getMessage());
            } catch(IllegalAccessException iae) {
                throw new ObjectBeanException("Illigal access error : " + iae.getMessage());
            } catch(IllegalStateException ise) {
                throw new ObjectBeanException("Illigal state error : " + ise.getMessage() + ": xml = " + xmlString);
            } catch(NullPointerException npe) {
                throw new ObjectBeanException("Reading the XML String produced a NullPointerException");
            }
            if(newInstance == null) {
                throw new ObjectBeanException("creating the new class instance returned a null value");
            }
            @SuppressWarnings("unchecked")
            final T objectBean = (T) newInstance;
            objectBean.setXMLDOM(doc.getRootElement());
            return (objectBean);
        } catch(NullPointerException npe) {
            throw new ObjectBeanException("There was a problem when Building the Object Bean: " + npe.toString());
        }
    }

    /**
     * override of the toString method
     *
     * @return string representation of the bean
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Bean->\n");
        Class<?> cls = this.getClass();
        while(cls != null) {
            sb.append(cls.getSimpleName());
            sb.append("\n");
            for(Field field : cls.getDeclaredFields()) {
                if(Modifier.isStatic(field.getModifiers())) {
                    continue;
                }
                if(!Modifier.isPublic(field.getModifiers())) {
                    field.setAccessible(true);
                }
                sb.append("  ");
                sb.append(field.getName());
                try {
                    Object fieldValue = field.get(this);
                    if(field.getType().isPrimitive() || field.getType().isEnum() || fieldValue instanceof String) {
                        sb.append(" = ");
                        sb.append(fieldValue.toString());
                    } else if(fieldValue instanceof Object[]) {
                        if(fieldValue instanceof String[]) {
                            String[] s = (String[]) fieldValue;
                            int length = Array.getLength(fieldValue);
                            sb.append("[");
                            sb.append(Integer.toString(length));
                            sb.append("] = { ");
                            for(int i = 0; i < length; i++) {
                                sb.append(s[i]);
                                if(i < length - 1) {
                                    sb.append(", ");
                                }
                            }
                            sb.append(" }");
                        } else {
                            Object[] o = (Object[]) fieldValue;
                            int length = Array.getLength(fieldValue);
                            sb.append("[");
                            sb.append(Integer.toString(length));
                            sb.append("] = { \n");
                            for(int i = 0; i < length; i++) {
                                sb.append("    index [");
                                sb.append(i);
                                sb.append("] ");

                                sb.append(o[i].toString());
                            }
                            sb.append("  }");
                        }
                    } else if(field.getType().isArray()) {
                        int length = Array.getLength(fieldValue);
                        sb.append("[");
                        sb.append(Integer.toString(length));
                        sb.append("] = { ");
                        for(int i = 0; i < length; i++) {
                            Object arrayObject = Array.get(fieldValue, i);
                            sb.append(arrayObject.toString());
                            if(i < length - 1) {
                                sb.append(", ");
                            }
                        }
                        sb.append(" }");
                    } else if(fieldValue instanceof Set<?>) {
                        sb.append(" = ");
                        Set<?> set = (Set<?>) fieldValue;
                        sb.append(" : ");
                        for(Object o : set) {
                            sb.append(o.toString());
                            sb.append(" : ");
                        }
                    } else if(fieldValue instanceof Map<?, ?>) {
                        sb.append(" = ");
                        Map<?, ?> map = (Map<?, ?>) fieldValue;
                        sb.append(" : ");
                        for(Object key : map.keySet()) {
                            sb.append("[");
                            sb.append(key.toString());
                            sb.append("]->");
                            sb.append(map.get(key).toString());
                            sb.append(" : ");
                        }
                    } else if(fieldValue instanceof Object) {
                        sb.append(" [");
                        sb.append(field.getType().getSimpleName());
                        sb.append("] = ");
                        sb.append(fieldValue.toString());
                    } else {
                        sb.append(" Unknown Type =>  ");
                        sb.append(field.getType());
                    }

                } catch(IllegalArgumentException ex) {
                    sb.append(" IllegalArgumentException ");
                } catch(IllegalAccessException ex) {
                    sb.append(" IllegalAccessException ");
                }

                field.setAccessible(false);
                sb.append("\n");
            }
            cls = cls.getSuperclass();
            if(cls.equals(Object.class)) {
                cls = null;
            }
        }
        sb.append("\n");

        return (sb.toString());
    }
}
