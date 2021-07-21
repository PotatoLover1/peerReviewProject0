package com.example.services;

import java.sql.SQLException;
import java.util.List;

import com.example.dao.AccountDao;
import com.example.dao.BankAccountDao;
import com.example.exceptions.InvalidTransactionException;
import com.example.logging.Logging;
import com.example.models.Account;
import com.example.models.BankAccount;

public class CustomerS {
	private int userId;
	private AccountDao acDao;
	private BankAccountDao apDao;
	
	public CustomerS(int userId,AccountDao ac, BankAccountDao ap) {
		this.userId = userId;
		acDao = ac;
		apDao = ap;
	}
	
	public void apply(double start) {
		try {
		apDao.createBankAccount(userId, start);
		Logging.logger.info("Application Success");
		}catch(SQLException e) {
			Logging.logger.info("Application Failed");
			e.printStackTrace();
		}
	}

	public void deposit(int accId,double amount)throws InvalidTransactionException{
		if(amount < 0.00) {
			Logging.logger.info("Invalid amount");
			throw new InvalidTransactionException();
		}
		Account a = acDao.getAccountById(accId);
		Double current = a.getBalance();
		current = current + amount;
		a.setBalance(current);
		acDao.updateAccount(a);
		Logging.logger.info("Withdrawal Successful");
		
	}
	
	public void transfer(int accId, int accId2 ,double amount)throws InvalidTransactionException{
		if(amount < 0.00) {
			throw new InvalidTransactionException();
		}
		Account a = acDao.getAccountById(accId);
		Account b = acDao.getAccountById(accId2);
		Double current = a.getBalance();
		if(current-amount <0) {
			throw new InvalidTransactionException();
		}else {
			current = current - amount;
			a.setBalance(current);
			acDao.updateAccount(a);
		}	
		current = current + amount;
		a.setBalance(current);
		acDao.updateAccount(a);
		Double t = b.getBalance();
		t = t + amount;
		b.setBalance(t);
		Logging.logger.info("Account Transfer occured");
	}
	
	public void withdrawl(int accId, double amount)throws InvalidTransactionException{
		if(amount < 0.00) {
			throw new InvalidTransactionException();
		}
		Account a = acDao.getAccountById(accId);
		Double current = a.getBalance();
		if(current-amount <0) {
			Logging.logger.info("Invalid amount");
			throw new InvalidTransactionException();
		}else {
			current = current - amount;
			a.setBalance(current);
			acDao.updateAccount(a);
			Logging.logger.info("Withdrawal Successful");
		}
	}
	
	
	public void checkApplications() {
		BankAccount a = apDao.getBankAccountByUserId(userId);
		
		if(a.getStatus() == "approved") {
			System.out.println("Application is accepted with a default pin of (0000)");
			apDao.removeBankAccount(a);
		}
		else if (a.getStatus() == "denied") 
		{
			System.out.println("Application is denied");
			apDao.removeBankAccount(a);
		}
		else if (a.getStatus() == "pending"){
			System.out.println("Application is pending");
		}
		else{
			System.out.println("No application at the moment");
		}
		
	}
	public void changePin(int accId, int currentPin, int pin) {
		Account a = acDao.getAccountById(accId);
		if(a.getPin()==currentPin) {
			a.setPin(pin);
			acDao.updateAccount(a);
			System.out.println("PIN Updated");
		}else {
			System.out.println("Invalid PIN, try again");
		}
		
	}
	
	public List<Account> getUserAccounts() {
		List<Account> accountList = acDao.getUserAccounts(userId);
		return accountList;
	}
	 
}
