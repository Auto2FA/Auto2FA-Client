import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Account {
    private String email;
    private String secret;

    // Constructors
    public Account() {
	this.email = null;
	this.secret = null;
    }

    public Account(String email) {
	this.email = email;
	this.secret = null;
    }

    // Key generation
    private void generateSecret() {
	this.secret = "secret";
    }

    // Getters and setters
    public void setEmail(String email) {
	this.email = email;
    }
    
    public String getEmail() {
	return this.email;
    }

    public String getSecret() {
	if (this.secret == null)
	    generateSecret();
	return this.secret;
    }

    public static void main(String[] args) {
	String fileName = "accounts.txt";
	String accountEmail;
	Account newAccount;
	Scanner scan;
	
	ArrayList<Account> accounts = new ArrayList<Account>();
	File accountsData = new File(fileName);
	try {
	    scan = new Scanner(accountsData);
	} catch(FileNotFoundException e) {
	    System.out.println("File not found> ");
	    return;
	}

	while(scan.hasNextLine()) {
	    accountEmail = scan.nextLine();
	    newAccount = new Account(accountEmail);
	    accounts.add(newAccount);
	}

	for (Account a : accounts) {
	    System.out.println(a.getEmail());
	}
    }
}
