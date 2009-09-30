package slim3.demo.model;

import java.io.Serializable;

import org.slim3.datastore.Model;
import org.slim3.datastore.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@Model
public class Foo implements Serializable {

    private static final long serialVersionUID = 1L;

    @PrimaryKey
    private Key key;

    private String string01;

    private String string02;

    private String string03;

    private String string04;

    private String string05;

    private String string06;

    private String string07;

    private String string08;

    private String string09;

    private String string10;

    /**
     * Returns the key.
     * 
     * @return the key
     */
    public Key getKey() {
        return key;
    }

    /**
     * Sets the key.
     * 
     * @param key
     *            the key
     */
    public void setKey(Key key) {
        this.key = key;
    }

    /**
     * @return the string01
     */
    public String getString01() {
        return string01;
    }

    /**
     * @param string01
     *            the string01 to set
     */
    public void setString01(String string01) {
        this.string01 = string01;
    }

    /**
     * @return the string02
     */
    public String getString02() {
        return string02;
    }

    /**
     * @param string02
     *            the string02 to set
     */
    public void setString02(String string02) {
        this.string02 = string02;
    }

    /**
     * @return the string03
     */
    public String getString03() {
        return string03;
    }

    /**
     * @param string03
     *            the string03 to set
     */
    public void setString03(String string03) {
        this.string03 = string03;
    }

    /**
     * @return the string04
     */
    public String getString04() {
        return string04;
    }

    /**
     * @param string04
     *            the string04 to set
     */
    public void setString04(String string04) {
        this.string04 = string04;
    }

    /**
     * @return the string05
     */
    public String getString05() {
        return string05;
    }

    /**
     * @param string05
     *            the string05 to set
     */
    public void setString05(String string05) {
        this.string05 = string05;
    }

    /**
     * @return the string06
     */
    public String getString06() {
        return string06;
    }

    /**
     * @param string06
     *            the string06 to set
     */
    public void setString06(String string06) {
        this.string06 = string06;
    }

    /**
     * @return the string07
     */
    public String getString07() {
        return string07;
    }

    /**
     * @param string07
     *            the string07 to set
     */
    public void setString07(String string07) {
        this.string07 = string07;
    }

    /**
     * @return the string08
     */
    public String getString08() {
        return string08;
    }

    /**
     * @param string08
     *            the string08 to set
     */
    public void setString08(String string08) {
        this.string08 = string08;
    }

    /**
     * @return the string09
     */
    public String getString09() {
        return string09;
    }

    /**
     * @param string09
     *            the string09 to set
     */
    public void setString09(String string09) {
        this.string09 = string09;
    }

    /**
     * @return the string10
     */
    public String getString10() {
        return string10;
    }

    /**
     * @param string10
     *            the string10 to set
     */
    public void setString10(String string10) {
        this.string10 = string10;
    }
}
