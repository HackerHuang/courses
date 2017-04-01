/* BadAccountException.java */

/**
 *  Implements an exception that should be thrown for negative amount of money.
 **/
public class BadTransactionException extends Exception {

    public int accountNumber;  // The invalid account number.

    /**
     *  Creates an exception object for nonexistent account "badAcctNumber".
     **/
    public BadTransactionException(int badAcctNumber) {
        super("Invalid account number: " + badAcctNumber);

        accountNumber = badAcctNumber;
    }
}