package testNTR.controller;

import testNTR.model.Account;
import testNTR.model.Accounts;
import testNTR.model.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;


@RestController
public class Controller {

    @Autowired
    UserService userService;

    /**
     * Get a balance account by its id (USING JSON)
     * @param id
     * @return double
     */
    @RequestMapping(value="/account/balance/{id}", method = RequestMethod.GET)
    @ResponseBody
    public double getBalance(@PathVariable("id") int id) {
        try {
            Account a = userService.findById(id);
            System.out.println("[GET] : " + a);
            return a.getBalance();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return Double.MIN_VALUE;
    }

    /**
     * Get an history account by its id (USING JSON)
     * @param id
     * @return ArrayList<Double>
     */
    @RequestMapping(value="/account/history/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ArrayList<Double> getHistory(@PathVariable("id") int id) {
        try {
            Account a = userService.findById(id);
            System.out.println("[GET] : " + a);
            return a.getHistory();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Get an account by its id (USING XML)
     * @param id
     * @return
     */
    @GetMapping(value = "/account/{id}", produces=MediaType.APPLICATION_XML_VALUE)
    public Account getAccountById(@PathVariable("id") int id) {
        try {
            Account a = userService.findById(id);
            System.out.println("[GET] : " + a);
            return a;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Retrieve all accounts (USING XML)
     * @return
     */
    @GetMapping(value = "/account", produces=MediaType.APPLICATION_XML_VALUE)
    public Accounts getAllAccount() {
        try {
            Accounts listA = userService.findAllAccounts();
            System.out.println("[GET] : " + listA);
            return listA;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Create an account (USING XML)
     * @param id
     * @param ownerName
     * @param ownerSurname
     * @param balance
     * @return
     */
    @PostMapping(path = "/account", produces=MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity createAccount(
            @RequestParam(name="id") int id,
            @RequestParam(name="ownerName") String ownerName,
            @RequestParam(name="ownerSurname") String ownerSurname,
            @RequestParam(name="balance") double balance
    ) {
        try {
            Account a = new Account(id, ownerName, ownerSurname, balance);

            if((!userService.isAccountExist(a)) && (userService.findById(id) == null)){
                userService.createAccount(a);
                System.out.println("[POST] : " + a + " CREATED");
                return new ResponseEntity(a.toString()+ "Account successfully created", HttpStatus.CREATED);
            } else {
                System.out.println("[ERROR] : Account already exist or id is already used");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new ResponseEntity("Unable to create account. Already exist or id already used.",HttpStatus.CONFLICT);
    }


    /**
     * Update an account (USING XML)
     * @param id
     * @param ownerName
     * @param ownerSurname
     * @param balance
     */
    @PutMapping(path = "/account", produces=MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity updateAccount(
            @RequestParam(name="id") int id,
            @RequestParam(name="ownerName") String ownerName,
            @RequestParam(name="ownerSurname") String ownerSurname,
            @RequestParam(name="balance") double balance
    ) {
        try {
            Account a = userService.findById(id);
            Account newA = new Account(id,ownerName,ownerSurname,balance);
            if(a != null) {
                System.out.println("[UPDATE] Updating account " + id + " | " + a + " ->" + newA);
                userService.updateAccount(newA,id);
                return new ResponseEntity("Account " + id + " successfully updated", HttpStatus.OK);

            } else {
                System.out.println("[ERROR] Account not found");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new ResponseEntity("Account not found", HttpStatus.NOT_FOUND);
    }

    /**
     * Delete an account by its id (USING XML)
     * @param id
     */
    @DeleteMapping(path = "/account", produces=MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity deleteAccount (
            @RequestParam(name="id") int id
    ) {
        try {
           Account a = userService.findById(id);
           if(a != null) {
               System.out.println("[DELETE] Deleting account " + id + " | " + a);
               userService.deleteAccount(a);
               return new ResponseEntity("Account successfully deleted", HttpStatus.OK);
           } else {
               System.out.println("[ERROR] Account not found");
           }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new ResponseEntity("Account not found", HttpStatus.NOT_FOUND);
    }

    /**
     * Credit the account with the specified id of the amount in parameters
     * @param id
     * @param amount
     * @return
     */
    @PutMapping(path = "/account/credit", produces=MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity creditAccount (
            @RequestParam(name="id") int id,
            @RequestParam(name="amount") double amount
    ) {
        try {
            Account a = userService.findById(id);
            if(a != null) {
                System.out.println("[UPDATE] Crediting account " + id + " of " + amount);
                userService.creditAccount(a, amount);
                return new ResponseEntity("Credit successfully done (" + amount + ")", HttpStatus.OK);
            } else {
                System.out.println("[ERROR] Account not found");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new ResponseEntity("Account not found", HttpStatus.NOT_FOUND);
    }

    /**
     * Debit the account with the specified id of the amount in parameters
     * @param id
     * @param amount
     * @return
     */
    @PutMapping(path = "/account/debit", produces=MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity debitAccount (
            @RequestParam(name="id") int id,
            @RequestParam(name="amount") double amount
    ) {
        try {
            Account a = userService.findById(id);
            if(a != null) {
                System.out.println("[UPDATE] Debiting account " + id + " of " + amount);
                userService.debitAccount(a, amount);
                return new ResponseEntity("Debit successfully done (" + amount + ")", HttpStatus.OK);
            } else {
                System.out.println("[ERROR] Account not found");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new ResponseEntity("Account not found", HttpStatus.NOT_FOUND);
    }
}
