package demo.controller;

import org.apache.struts.action.ActionForward;
import org.slim3.struts.annotation.Arg;
import org.slim3.struts.annotation.ByteType;
import org.slim3.struts.annotation.CreditCardType;
import org.slim3.struts.annotation.DateType;
import org.slim3.struts.annotation.DoubleRange;
import org.slim3.struts.annotation.DoubleType;
import org.slim3.struts.annotation.EmailType;
import org.slim3.struts.annotation.Execute;
import org.slim3.struts.annotation.FloatRange;
import org.slim3.struts.annotation.FloatType;
import org.slim3.struts.annotation.IntRange;
import org.slim3.struts.annotation.IntegerType;
import org.slim3.struts.annotation.LongRange;
import org.slim3.struts.annotation.LongType;
import org.slim3.struts.annotation.Mask;
import org.slim3.struts.annotation.Maxlength;
import org.slim3.struts.annotation.Minlength;
import org.slim3.struts.annotation.Msg;
import org.slim3.struts.annotation.Required;
import org.slim3.struts.annotation.ShortType;
import org.slim3.struts.annotation.UrlType;
import org.slim3.struts.annotation.Validwhen;

public class ValidatorController {

    @Required
    @ByteType
    public String byteText;

    @Required
    @ShortType
    public String shortText;

    @Required
    @IntegerType
    public String integerText;

    @Required
    @LongType
    public String longText;

    @Required
    @FloatType
    public String floatText;

    @Required
    @DoubleType
    public String doubleText;

    @Required
    @DateType(datePatternStrict = "MM/dd/yyyy")
    public String dateText;

    @Required
    @CreditCardType
    public String creditcardText;

    @Required
    @EmailType
    public String emailText;

    @Required
    @UrlType
    public String urlText;

    @Required
    @IntRange(min = 3, max = 10)
    public String intRangeText;

    @Required
    @LongRange(min = 3, max = 10)
    public String longRangeText;

    @Required
    @FloatRange(min = "3.0", max = "10.0")
    public String floatRangeText;

    @Required
    @DoubleRange(min = "3.0", max = "10.0")
    public String doubleRangeText;

    @Required
    @Minlength(minlength = 3)
    public String minlengthText;

    @Required
    @Maxlength(maxlength = 10)
    public String maxlengthText;

    @Required
    @Mask(mask = "\\d\\d\\d", msg = @Msg(key = "errors.number.format"), args = @Arg(key = "999", resource = false, position = 1))
    public String numberText;

    public String validwhen1Text;

    @Validwhen(test = "((validwhen1Text == null) or (*this* != null))", msg = @Msg(key = "errors.required.other"), args = @Arg(key = "validwhen1Text", resource = false, position = 1))
    public String validwhen2Text;

    @Execute(validate = false)
    public ActionForward index() {
        byteText = "1";
        shortText = "1";
        integerText = "1";
        longText = "1";
        floatText = "1.0";
        doubleText = "1.0";
        dateText = "12/31/2008";
        emailText = "higayasuo@gmail.com";
        urlText = "http://www.slim3.org/";
        intRangeText = "7";
        longRangeText = "7";
        floatRangeText = "7.0";
        doubleRangeText = "7.0";
        minlengthText = "123";
        maxlengthText = "1234567890";
        numberText = "123";
        return new ActionForward("index.jsp");
    }

    @Execute(input = "index.jsp")
    public ActionForward submit() {
        return new ActionForward("index.jsp");
    }
}