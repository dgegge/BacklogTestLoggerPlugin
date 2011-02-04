//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.02.04 at 02:11:40 PM MEZ 
//


package com.coremedia.util;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.coremedia.util package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Stories_QNAME = new QName("", "stories");
    private final static QName _StorySingleTest_QNAME = new QName("", "singleTest");
    private final static QName _SingleTestErrorlog_QNAME = new QName("", "errorlog");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.coremedia.util
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Errorlog }
     * 
     */
    public Errorlog createErrorlog() {
        return new Errorlog();
    }

    /**
     * Create an instance of {@link Story }
     * 
     */
    public Story createStory() {
        return new Story();
    }

    /**
     * Create an instance of {@link Stories }
     * 
     */
    public Stories createStories() {
        return new Stories();
    }

    /**
     * Create an instance of {@link SingleTest }
     * 
     */
    public SingleTest createSingleTest() {
        return new SingleTest();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Stories }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "stories")
    public JAXBElement<Stories> createStories(Stories value) {
        return new JAXBElement<Stories>(_Stories_QNAME, Stories.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SingleTest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "singleTest", scope = Story.class)
    public JAXBElement<SingleTest> createStorySingleTest(SingleTest value) {
        return new JAXBElement<SingleTest>(_StorySingleTest_QNAME, SingleTest.class, Story.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Errorlog }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "errorlog", scope = SingleTest.class)
    public JAXBElement<Errorlog> createSingleTestErrorlog(Errorlog value) {
        return new JAXBElement<Errorlog>(_SingleTestErrorlog_QNAME, Errorlog.class, SingleTest.class, value);
    }

}
