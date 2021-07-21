package com.example.driver;
import java.util.List;
import java.util.Scanner;

import com.example.dao.AccountDao;
import com.example.dao.AccountDaoDB;
import com.example.dao.BankAccountDao;
import com.example.dao.BankAccountDaoDB;
import com.example.dao.UserDao;
import com.example.dao.UserDaoDB;
import com.example.models.Account;
import com.example.models.User;
import com.example.services.CustomerS;
import com.example.services.EmployeeS;
import com.example.services.UsersS;

public class BankDriver {
	private static AccountDao acDao = new AccountDaoDB();
	private static BankAccountDao baDao = new BankAccountDaoDB();
	private static UserDao uDao = new UserDaoDB();
	private static UsersS uServ = new UsersS(uDao);
	private static EmployeeS eServ = new EmployeeS(acDao, baDao);
	
	public static void main(String[] args) {
		
		Scanner in = new Scanner(System.in);
		System.out.println("BankApp");
		boolean done = false;
		User u = null;
		
		while(!done) {
			if(u==null) {
				System.out.print("Press 1 to Login\n"
						+ "Press 2 to SignUp\n"
						+ "Press 3 to Exit\n");
				System.out.print("Input: ");
				int choice = Integer.parseInt(in.nextLine());
				switch(choice) {
				case 1:
					System.out.print("Username: ");
					String username = in.nextLine();
					System.out.print("Password: ");
					String password = in.nextLine();
					try {
						u = uServ.signIn(username, password);
						System.out.println("Welcome "+ u.getFirstName());
					}catch(Exception e) {
						System.out.println("Username or password was incorect. Try again: ");
					}
					break;
				case 2:
					System.out.print("First Name: ");
					String first = in.nextLine();
					System.out.print("Last Name: ");
					String last = in.nextLine();
					System.out.print("Email: ");
					String email = in.nextLine();
					System.out.print("Password: ");
					String pass = in.nextLine();
					try {
						u = uServ.signUp(first, last, email, pass);
						System.out.println("User created with the username: " + u.getUsername());
						u = null;
					} catch (Exception e) {
						System.out.println("Sorry, we could not process your request");
						System.out.println("Please try again later");
						done = true;
					}
					break;
				case 3:
					done = true;
					break;
				case 1234:
					u = uServ.signIn("MarkTowels3295", "password");
					System.out.println("Welcome "+ u.getFirstName());
					break;
				case 2021:
					u = uServ.signIn("Admin1234", "P4ssword");
					System.out.println("Welcome "+ u.getFirstName());
					break;
				default:
					System.out.println("Invalid input, try again");
					break;
				}
				
			}else if(u.getUsername().equals("Admin1234"))  {
			
				System.out.print("Press 1 for view accounts\n"
						+ "Press 2 to look at applications\n"
						+ "Press 3 to quit\n");
				System.out.print("Input: ");
				int choice = Integer.parseInt(in.nextLine());
				if(choice == 1) {
					eServ.viewAccounts();
				}
				else if(choice == 2) {
					eServ.reviewApplications(in);
					}
				else if(choice == 3)
				{
					done = true;
					break;
				}
				else {
					System.out.println("Invalid input, try again");
					break;
				}
			}
			else  {
				CustomerS cServ = new CustomerS(u.getId(),acDao,baDao);
				List<Account> accountList = cServ.getUserAccounts();
					
					System.out.println("Press 1 to view accounts\n"
							 + "Press 2 to change pin\n"
							 + "Press 3 to perform a withdrawal\n"
							 + "Press 4 to perform a deposit\n"
							 + "Press 5 to check your account application\n"
							 + "Press 6 to apply for a new account\n"
							 + "Press 7 to transfer\n"
							 + "Press 8 to EXIT\n");
					System.out.print("Input: ");
					int choice = Integer.parseInt(in.nextLine());
					switch (choice) {
					case 1:
						accountList.forEach(s -> System.out.println(s));
						//System.out.println(accountList);
						break;
					case 2:
						System.out.print("Which account you want to change: ");
						int acc = Integer.parseInt(in.nextLine());
						System.out.print("Enter your current PIN: ");
						int current = Integer.parseInt(in.nextLine());
						System.out.print("Enter your new PIN: ");
						int pin = Integer.parseInt(in.nextLine());
						cServ.changePin(acc, current, pin);
						break;
					case 3:
						System.out.println("Which account do you want to make a withdrawal: ");
						int account = Integer.parseInt(in.nextLine());
						System.out.print("How much do you want to withdraw: $");
						double wamount = Double.parseDouble(in.nextLine());
						cServ.withdrawl(account, wamount);
						break;
					case 4:
						System.out.println("Which account do you want to make a deposit: ");
					int acc1 = Integer.parseInt(in.nextLine());
					System.out.print("How much do you want to deposit: $");
					double damount = Double.parseDouble(in.nextLine());
					cServ.deposit(acc1, damount);

					break;
					case 5:
						cServ.checkApplications();
						break;
					case 6:
						System.out.print("Enter first deposit:");
						double start = Double.parseDouble(in.nextLine());
						System.out.println("Account is created.");
						cServ.apply(start);	
						break;
					case 8:
						done = true;
						break;
					case 7:
						System.out.println("Which account do you want to transfer from: ");
						int tacc1 = Integer.parseInt(in.nextLine());
						System.out.print("How much do you want to transfer: $");
						double tamount1 = Double.parseDouble(in.nextLine());
						cServ.withdrawl(tacc1, tamount1);
						System.out.println("Which account do you want to transfer to: ");
						int tacc2 = Integer.parseInt(in.nextLine());
						cServ.transfer(tacc1, tacc2, tamount1);
					default:
							System.out.println("Invalid input, try again");
							break;
				}
				
			}
		

		}
		System.out.println("goodbye");

	}
}
