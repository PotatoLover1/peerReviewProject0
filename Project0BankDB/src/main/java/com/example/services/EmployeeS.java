package com.example.services;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.example.dao.AccountDao;
import com.example.dao.BankAccountDao;
import com.example.logging.Logging;
import com.example.models.Account;
import com.example.models.BankAccount;


public class EmployeeS {
	private AccountDao acDao;
	private BankAccountDao apDao;
	
	public EmployeeS(AccountDao ac, BankAccountDao ap) {
		acDao = ac;
		apDao = ap;
	}
	public void viewAccounts() {
		ArrayList<Account> accountList = new ArrayList<Account>();
		accountList = (ArrayList<Account>) acDao.getAllAccounts();
		for(Account a : accountList) {
			System.out.println(a);
		}
	}
	
	
	public void reviewApplications(Scanner in) {
		int input;
		List<BankAccount> appList = new ArrayList<BankAccount>();
		appList = apDao.getAllBankAccounts();
		for(BankAccount a : appList) {
			System.out.println(a);
			System.out.print("Press 1 to Approve, Press 2 to Reject: ");
			input = Integer.parseInt(in.nextLine());

			if (input == 1) {
				a.setStatus("approved");
				apDao.updateBankAccount(a);
				Account acc = new Account(a.getStartBalance(),a.getUserId(),0000);
				try{
					acDao.createAccount(acc);
					Logging.logger.info("Application Approved");
				}catch(SQLException e) {
					Logging.logger.info("SQL Error");
					e.printStackTrace();
				}
				System.out.println("approved");
				break;
			}
			else if (input == 2) {
				a.setStatus("denied");
				Logging.logger.info("Application denied");
				apDao.updateBankAccount(a);
				System.out.println("denied");
				break;
			}
			else {
				System.out.println("Invalid input, application is set to pending");
				break;
			}
			
			}
		}
	}

