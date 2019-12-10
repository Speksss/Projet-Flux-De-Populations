package testNTR.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is used as wrapper of the Account class for XML purpose
 */
public class Accounts implements Serializable {
    private List<Account> accounts = new ArrayList<>();

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }
}