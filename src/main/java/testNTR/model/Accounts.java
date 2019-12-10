package testNTR.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is used as wrapper of the Account class for XML purpose
 */
@JacksonXmlRootElement
public class Accounts implements Serializable {

    @JacksonXmlProperty(localName = "Account")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<Account> accounts = new ArrayList<>();

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }
}