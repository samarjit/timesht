
package map.generated;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}htmltempalte"/>
 *         &lt;element ref="{}includedjsp"/>
 *         &lt;element ref="{}callbackclass"/>
 *         &lt;choice maxOccurs="unbounded">
 *           &lt;element ref="{}scripts"/>
 *           &lt;element ref="{}stylesheets"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "htmltempalte",
    "includedjsp",
    "callbackclass",
    "scriptsOrStylesheets"
})
@XmlRootElement(name = "screen")
public class Screen {

    @XmlElement(required = true)
    protected String htmltempalte;
    @XmlElement(required = true)
    protected String includedjsp;
    @XmlElement(required = true)
    protected String callbackclass;
    @XmlElements({
        @XmlElement(name = "stylesheets", type = Stylesheets.class),
        @XmlElement(name = "scripts", type = Scripts.class)
    })
    protected List<Object> scriptsOrStylesheets;
    @XmlAttribute(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String name;

    /**
     * Gets the value of the htmltempalte property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHtmltempalte() {
        return htmltempalte;
    }

    /**
     * Sets the value of the htmltempalte property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHtmltempalte(String value) {
        this.htmltempalte = value;
    }

    /**
     * Gets the value of the includedjsp property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIncludedjsp() {
        return includedjsp;
    }

    /**
     * Sets the value of the includedjsp property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIncludedjsp(String value) {
        this.includedjsp = value;
    }

    /**
     * Gets the value of the callbackclass property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCallbackclass() {
        return callbackclass;
    }

    /**
     * Sets the value of the callbackclass property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCallbackclass(String value) {
        this.callbackclass = value;
    }

    /**
     * Gets the value of the scriptsOrStylesheets property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the scriptsOrStylesheets property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getScriptsOrStylesheets().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Stylesheets }
     * {@link Scripts }
     * 
     * 
     */
    public List<Object> getScriptsOrStylesheets() {
        if (scriptsOrStylesheets == null) {
            scriptsOrStylesheets = new ArrayList<Object>();
        }
        return this.scriptsOrStylesheets;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

}
