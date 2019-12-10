package testNTR.model;

import java.util.ArrayList;
import java.util.Objects;

public class Account {
    private int id;
    private String ownerName;
    private String ownerSurname;
    private double balance;
    private ArrayList<Double> history;

    public Account() {
        this.id = 0;
        this.ownerName = "";
        this.ownerSurname = "";
        this.balance = 0;
        this.history = new ArrayList<>();
    }

    public Account(int id, String ownerName, String ownerSurname, double balance) {
        this.id = id;
        this.ownerName = ownerName;
        this.ownerSurname = ownerSurname;
        this.balance = balance;
        this.history = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerSurname() {
        return ownerSurname;
    }

    public void setOwnerSurname(String ownerSurname) {
        this.ownerSurname = ownerSurname;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public ArrayList<Double> getHistory() { return history; }

    public void setHistory(ArrayList<Double> history) { this.history = history; }

    public void addEntryHistory(double amount) {
        this.history.add(amount);
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", ownerName='" + ownerName + '\'' +
                ", ownerSurname='" + ownerSurname + '\'' +
                ", balance=" + balance +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) return false;
        Account account = (Account) o;
        return id == account.id &&
                Double.compare(account.balance, balance) == 0 &&
                Objects.equals(ownerName, account.ownerName) &&
                Objects.equals(ownerSurname, account.ownerSurname);
    }

}
