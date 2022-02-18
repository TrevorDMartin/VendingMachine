package com.techelevator;

import com.techelevator.view.Menu;

import java.util.Scanner;

import static java.lang.System.in;

public class VendingMachineCLI {
Scanner scanner = new Scanner(System.in);
	private static VendingMachine vendingMachine;
	private static final String MAIN_MENU_OPTION_DISPLAY_ITEMS = "Display Vending Machine Items";
	private static final String MAIN_MENU_OPTION_PURCHASE = "Purchase";
	private static final String MAIN_MENU_EXIT = "Exit";
	private static final String MAIN_MENU_SALES_REPORT = "Hidden Menu";
	private static final String SUB_MENU_OPTION_FEED_MONEY = "Deposit Money";
	private static final String SUB_MENU_OPTION_SELECT_PRODUCT = "Select Product";
	private static final String SUB_MENU_FINISH_TRANSACTION = "Finish Transaction";
	private static final String[] MAIN_MENU_OPTIONS = {MAIN_MENU_OPTION_DISPLAY_ITEMS, MAIN_MENU_OPTION_PURCHASE, MAIN_MENU_EXIT, MAIN_MENU_SALES_REPORT};
	private static final String[] SUB_MENU_OPTIONS = {SUB_MENU_OPTION_FEED_MONEY, SUB_MENU_OPTION_SELECT_PRODUCT, SUB_MENU_FINISH_TRANSACTION};
	private final Menu menu;


	public static void main(String[] args) {
		Menu menu = new Menu(in, System.out);
		VendingMachineCLI cli = new VendingMachineCLI(menu);
		cli.run();
	}
	public VendingMachineCLI(Menu menu) {
		this.menu = menu;
	}

	public void run() {
		try {
			vendingMachine  = new VendingMachine();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		while (true) {
			String choice = (String) menu.getChoiceFromOptions(MAIN_MENU_OPTIONS);

			if (choice.equals(MAIN_MENU_OPTION_DISPLAY_ITEMS)) {
				displayItems();
			} else if (choice.equals(MAIN_MENU_OPTION_PURCHASE)) {
				purchaseMenu();
			} else if (choice.equals(MAIN_MENU_EXIT)) {
				break;
			} else if (choice.equals(MAIN_MENU_SALES_REPORT)) {
				salesReport();
			}
		}
	}

	private void purchaseMenu() {
		while (true) {
			String choiceSub = (String) menu.getChoiceFromOptions(SUB_MENU_OPTIONS);
			if (choiceSub.equals(SUB_MENU_OPTION_FEED_MONEY)) {
				feedMoney();
			} else if (choiceSub.equals(SUB_MENU_OPTION_SELECT_PRODUCT)) {
				selectProduct();
			} else if (choiceSub.equals(SUB_MENU_FINISH_TRANSACTION)) {
			 	finishTransaction();
				break;
			}
		}
	}

	public static void displayItems() {
		System.out.println(vendingMachine.displayItems());
	}

	private void feedMoney() {
		try {
			System.out.println("\nEnter deposit amount in whole dollars.");
			String userInput = scanner.nextLine();
			vendingMachine.feedMoney(userInput);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		System.out.println("Your current balance is $" + String.format("%.2f", vendingMachine.getUserBalance()));
	}

	private void selectProduct() {
		try {
			System.out.println("Please input the slot Letter and Number you would like to purchase");
			String userInput = scanner.nextLine();
			System.out.println(vendingMachine.selectProduce(userInput));

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	private void finishTransaction() {
		try {
			System.out.println(vendingMachine.finishTransaction());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	private void salesReport() {
		System.out.println(vendingMachine.salesReport());
	}
}
