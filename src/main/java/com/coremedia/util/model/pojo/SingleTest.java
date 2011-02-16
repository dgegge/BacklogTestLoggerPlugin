//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.01.25 at 02:52:18 PM MEZ 
//


package com.coremedia.util.model.pojo;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for singleTest complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="singleTest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="errorlog" type="{}errorlog" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="package" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="class" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="method" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="success" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="done" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="executiontime" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="comment" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "singleTest", propOrder = {
        "content"
})
public class SingleTest {

  @XmlElementRef(name = "errorlog", type = JAXBElement.class)
  @XmlMixed
  protected List<Serializable> content;
  @XmlAttribute(name = "class", required = true)
  protected String clazz;
  @XmlAttribute(required = true)
  protected String method;
  @XmlAttribute(required = true)
  protected Boolean success;
  @XmlAttribute(required = true)
  protected Boolean done;
  @XmlAttribute(required = true)
  protected String executiontime;
  @XmlAttribute
  protected String comment;

  @XmlTransient
  protected Errorlog error;

  @XmlTransient
  protected List<Story> affectedStories = new ArrayList<Story>();

  public Errorlog getError() {
    return error;
  }

  public void setError(Errorlog error) {
    this.error = error;
  }

  public String getShortenedError() {
    StringBuilder result = new StringBuilder();
    result.append(getSuccess());
    if (error != null) {
      result.append("<span>");
      result.append(error.getException());
      result.append("</span>");
      return result.toString();
    } else {
      return result.toString();
    }
  }

  /**
   * Gets the value of the content property.
   * <p/>
   * <p/>
   * This accessor method returns a reference to the live list,
   * not a snapshot. Therefore any modification you make to the
   * returned list will be present inside the JAXB object.
   * This is why there is not a <CODE>set</CODE> method for the content property.
   * <p/>
   * <p/>
   * For example, to add a new item, do as follows:
   * <pre>
   *    getContent().add(newItem);
   * </pre>
   * <p/>
   * <p/>
   * <p/>
   * Objects of the following type(s) are allowed in the list
   * {@link JAXBElement }{@code <}{@link Errorlog }{@code >}
   * {@link String }
   */
  public List<Serializable> getContent() {
    if (content == null) {
      content = new ArrayList<Serializable>();
    }
    return this.content;
  }
  
  /**
   * Gets the value of the clazz property.
   *
   * @return possible object is
   *         {@link String }
   */
  public String getClazz() {
    return clazz;
  }

  /**
   * Sets the value of the clazz property.
   *
   * @param value allowed object is
   *              {@link String }
   */
  public void setClazz(String value) {
    this.clazz = value;
  }

  /**
   * Gets the value of the method property.
   *
   * @return possible object is
   *         {@link String }
   */
  public String getMethod() {
    return method;
  }

  /**
   * Sets the value of the method property.
   *
   * @param value allowed object is
   *              {@link String }
   */
  public void setMethod(String value) {
    this.method = value;
  }

  /**
   * Gets the value of the success property.
   *
   * @return possible object is
   *         {@link String }
   */
  public Boolean getSuccess() {
    return success;
  }

  /**
   * Sets the value of the success property.
   *
   * @param value allowed object is
   *              {@link String }
   */
  public void setSuccess(Boolean value) {
    this.success = value;
  }

  /**
   * Gets the value of the done property.
   *
   * @return possible object is
   *         {@link String }
   */
  public Boolean getDone() {
    return done;
  }

  /**
   * Sets the value of the done property.
   *
   * @param value allowed object is
   *              {@link String }
   */
  public void setDone(Boolean value) {
    this.done = value;
  }

  /**
   * Gets the value of the executiontime property.
   *
   * @return possible object is
   *         {@link String }
   */
  public String getExecutiontime() {
    return executiontime;
  }

  /**
   * Sets the value of the executiontime property.
   *
   * @param value allowed object is
   *              {@link String }
   */
  public void setExecutiontime(String value) {
    this.executiontime = value;
  }

  /**
   * Gets the value of the comment property.
   *
   * @return possible object is
   *         {@link String }
   */
  public String getComment() {
    return comment;
  }

  /**
   * Sets the value of the comment property.
   *
   * @param value allowed object is
   *              {@link String }
   */
  public void setComment(String value) {
    this.comment = value;
  }

  public String getAdaptedName() {
    String name = clazz + "." + method;
    if (name == null || name.isEmpty()) {
      return name;
    }
    StringBuilder result = new StringBuilder();
    if (name.length() > 60) {
      result.append(name.substring(0, 10));
      result.append("[...]");
      result.append(name.substring(name.length() - 44, name.length()));
      result.append("<span>");
      result.append(name);
      result.append("</span>");
    } else {
      result.append(name);
    }
    return result.toString();
  }

  public String getTestname() {
    return this.getClazz() + "." + this.getMethod();
  }

  public String getNameForUrl() {
    String name = clazz + "." + method;
    String result = name.replace("/", "..");
    return result;
  }

  public static String resolveTestNameInUrl(String testName) {
    String result = testName.replace("/", "..");
    return result;
  }

  /*public void setAffectedStories(final List<Story> stories) {
    this.affectedStories = new ArrayList<Story>();
    for (Story story : stories) {
      if (story.getTestWithName(this.getTestname()) != null) {
        this.affectedStories.add(story);
      }
    }
  }*/

  public void addStoryToTest(Story story) {
    this.affectedStories.add(story);    
  }

  public List<Story> getAffectedStories() {
    return affectedStories;
  }
}
