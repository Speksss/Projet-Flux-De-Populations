package testNTR.model;

import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service("userService")
public class UserServiceImpl implements UserService {

    private static ArrayList<Account> accounts = new ArrayList<>();

    /**
     * Get the account with the id in parameters
     * @param id
     * @return
     */
    @Override
    public Account findById(int id) {
        for (Account c : this.accounts) {
            if (c.getId() == id) {
                return c;
            }
        }
        return null;
    }

    /**
     * Get the account that belongs to the name in parameters
     * @param name
     * @return
     */
    @Override
    public Account findByName(String name) {
        for (Account c : this.accounts) {
            if (c.getOwnerName() == name) {
                return c;
            }
        }
        return null;
    }

    /**
     * Get all the accounts registered in the service
     * @return
     */
    @Override
    public Accounts findAllAccounts() {
        Accounts myAccounts = new Accounts();
        myAccounts.setAccounts(accounts);
        return myAccounts;
    }

    /**
     * Add the account in parameters to the service
     * @param a
     */
    @Override
    public void createAccount(Account a) {
        accounts.add(a);
    }

    /**
     * Replace the account with the specified id by the account in parameters
     * @param a
     * @param id
     */
    @Override
    public void updateAccount(Account a, int id) {
        for (Account c : this.accounts) {
            if (c.getId() == id) {
                this.accounts.remove(c);
            }
        }
        this.accounts.add(a);
    }

    /**
     * Delete the account in parameters
     * @param a
     */
    @Override
    public void deleteAccount(Account a) {
        for (Account c : this.accounts) {
            if (c.equals(a)) {
                this.accounts.remove(c);
            }
        }
    }

    /**
     * Returns TRUE if the account in parameters is in the service, FALSE if not
     * @param a
     * @return
     */
    @Override
    public boolean isAccountExist(Account a) {
        for (Account c : this.accounts) {
            if (c.equals(a)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Add the specified amount to the account in parameters
     * @param a
     * @param amount
     */
    @Override
    public void creditAccount(Account a, double amount) {
        for (Account c : this.accounts) {
            if (c.equals(a)) {
                c.setBalance(c.getBalance() + amount);
                c.addEntryHistory(amount);
            }
        }
    }

    /**
     * Take off the specified amount of the account in parameters
     * @param a
     * @param amount
     */
    @Override
    public void debitAccount(Account a, double amount) {
        for (Account c : this.accounts) {
            if (c.equals(a)) {
                c.setBalance(c.getBalance() - amount);
                c.addEntryHistory(-amount);
            }
        }
    }
}
