
package map.generated;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;element name="cmd" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="instack" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="opt" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="processor" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="result" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "cmd"
})
@XmlRootElement(name = "commands")
public class Commands {

    protected List<Commands.Cmd> cmd;

    /**
     * Gets the value of the cmd property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the cmd property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCmd().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Commands.Cmd }
     * 
     * 
     */
    public List<Commands.Cmd> getCmd() {
        if (cmd == null) {
            cmd = new ArrayList<Commands.Cmd>();
        }
        return this.cmd;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;attribute name="instack" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="opt" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="processor" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="result" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Cmd {

        @XmlAttribute(required = true)
        protected String instack;
        @XmlAttribute(required = true)
        protected String name;
        @XmlAttribute(required = true)
        protected String opt;
        @XmlAttribute(required = true)
        protected String processor;
        @XmlAttribute(required = true)
        @XmlSchemaType(name = "anySimpleType")
        protected String result;

        /**
         * Gets the value of the instack property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getInstack() {
            return instack;
        }

        /**
         * Sets the value of the instack property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setInstack(String value) {
            this.instack = value;
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

        /**
         * Gets the value of the opt property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getOpt() {
            return opt;
        }

        /**
         * Sets the value of the opt property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setOpt(String value) {
            this.opt = value;
        }

        /**
         * Gets the value of the processor property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getProcessor() {
            return processor;
        }

        /**
         * Sets the value of the processor property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setProcessor(String value) {
            this.processor = value;
        }

        /**
         * Gets the value of the result property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getResult() {
            return result;
        }

        /**
         * Sets the value of the result property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setResult(String value) {
            this.result = value;
        }

    }

}
