package testNTR.model;

public interface UserService {
        //Get
        Account findById(int id);
        Account findByName(String name);
        Accounts findAllAccounts();

        //Set
        void createAccount(Account a);

        //Update
        void updateAccount(Account a, int id);

        //Delete
        void deleteAccount(Account a);

        //Other
        boolean isAccountExist(Account a);
        void creditAccount(Account a, double amount);
        void debitAccount(Account a, double amount);
}
