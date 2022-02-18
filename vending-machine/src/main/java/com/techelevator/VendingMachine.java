package com.techelevator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class VendingMachine {
    //Instance Variables

    private final Map<String, Queue<Item>> INVENTORY = new HashMap<>();
    private Map<String, Queue<Item>> STARTING_INVENTORY = new HashMap<>();
    private BigDecimal userBalance = BigDecimal.ZERO;
    private BigDecimal vendingMachineRevenue = BigDecimal.ZERO;
    private boolean isNotFirstLog = false;
    private final File FILE_LOG = new File("Audit.txt");
    private final LocalDateTime CURRENT_DATE_TIME = LocalDateTime.now();
    private final DateTimeFormatter FORMAT_DATE_TIME = DateTimeFormatter.ofPattern("MM.dd.yyyy HH:mm:ss a");
    private BigDecimal changeLeft;

    //Constructors
    public VendingMachine() throws Exception{
        try {
            importInventory();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    //Getters
    public Map<String, Queue<Item>> getInventory() {
        return INVENTORY;
    }
    public BigDecimal getUserBalance() {
        return userBalance;
    }
    public BigDecimal getVendingMachineRevenue() {
        return vendingMachineRevenue;
    }

    //Methods
    public String displayItems() {
        String displayItems = "\n";

        List<String> slotList = new ArrayList<>(INVENTORY.keySet());
        Collections.sort(slotList);

        for (String slot : slotList) {
            Queue<Item> itemsInSlot = INVENTORY.get(slot);
            if (itemsInSlot.isEmpty()) {
                displayItems += slot + " is EMPTY\n";
            } else {
                Item item = itemsInSlot.peek();
                displayItems += slot + " | " + item.getName() + " | " + String.format("%.2f", item.getPrice()) + "\n";
            }
        }
        return displayItems;
    }

    public String salesReport() {
        String secretMenu = "\n**SALES REPORT**\n";

        List<String> slotList = new ArrayList<>(INVENTORY.keySet());
        Collections.sort(slotList);

        for (String slot : slotList) {
            Item item = STARTING_INVENTORY.get(slot).peek();
            int amountSold = STARTING_INVENTORY.get(slot).size() - INVENTORY.get(slot).size();

            if(amountSold>0) {
                secretMenu += item.getName() + " | ";
                secretMenu += amountSold + "\n";
            }
        }
        secretMenu += "\n**TOTAL REVENUE** $" + String.format("%.2f", vendingMachineRevenue) + "\n";
        return secretMenu;
    }

    public void feedMoney(String userInput) throws Exception {
        BigDecimal deposit;
        try {
            deposit = new BigDecimal(userInput);
            if (deposit.scale()>2 || deposit.compareTo(BigDecimal.ZERO)<1) {
                throw new Exception();
            }
        } catch (Exception e) {
            throw new Exception("Must deposit money.");
        }

        if (userBalance.add(deposit).compareTo(new BigDecimal("100"))>0 ) {
            String message = "Your current balance is $" + String.format("%.2f", userBalance);
            message += "\nYour attempted deposit was $" + String.format("%.2f", deposit);
            message += "\nTotal balance can not exceed $100.";
            throw new Exception(message);
        }
        userBalance = userBalance.add(deposit);
        userLog("FEED MONEY:", deposit);
    }

    public String selectProduce(String userInput) throws Exception {

        if (getUserBalance().compareTo(BigDecimal.ZERO)<1) {
            String ourThrownMessage = "Your current balance is $" + String.format("%.2f", getUserBalance());
            ourThrownMessage += "\nYou have not deposited any money; please finish Tech Elevator, get employed, and try again.";
            throw new Exception(ourThrownMessage);
        }
        if (!INVENTORY.containsKey(userInput.toUpperCase())) {
            throw new Exception ("The slot doesn't exist; please choose an existing slot");
        }
        if (INVENTORY.get(userInput.toUpperCase()).isEmpty()) {
            throw new Exception("Item is out of stock, please choose a different item.");
        }
        if (INVENTORY.get(userInput.toUpperCase()).peek().getPrice().compareTo(getUserBalance()) > 0 ) {
            String ourThrownMessage = "Your current balance is $" + String.format("%.2f", getUserBalance());
            ourThrownMessage += "\nUnfortunately, your balance is not enough. Please deposit more funds and try again.";
            throw new Exception(ourThrownMessage);
        }

        Item item = INVENTORY.get(userInput.toUpperCase()).poll();
        subFromBalance(item.getPrice());
        userLog(item.getName() + " " + userInput.toUpperCase(), item.getPrice());

        return item + "\nYour current balance is $" + String.format("%.2f", getUserBalance());
    }

    public String finishTransaction() throws Exception {
        changeLeft = getUserBalance();
        BigDecimal userBalance = getUserBalance();
        BigDecimal dollars = new BigDecimal("1.00");
        BigDecimal quarters = new BigDecimal("0.25");
        BigDecimal dimes = new BigDecimal("0.10");
        BigDecimal nickles = new BigDecimal("0.05");
        BigDecimal pennies = new BigDecimal("0.01");


        dollars = changeRefactor(dollars);
        quarters = changeRefactor(quarters);
        dimes = changeRefactor(dimes);
        nickles = changeRefactor(nickles);
        pennies = changeRefactor(pennies);

        if (pennies.compareTo(BigDecimal.ZERO) != 0) {
            throw new Exception("There was an error returning change, please call customer service");
        }

        String returnString = "Change dispensed: " + (dollars.compareTo(BigDecimal.ZERO)>0  ? "\nDollars: " + dollars : "");
        returnString += (quarters.compareTo(BigDecimal.ZERO)>0  ? "\nQuarters: " + quarters : "");
        returnString += (dimes.compareTo(BigDecimal.ZERO)>0  ? "\nDimes: " + dimes : "");
        returnString += (nickles.compareTo(BigDecimal.ZERO)>0  ? "\nNickles: " + nickles : "");

        clearBalance();
        userLog("GIVE CHANGE:", userBalance);

        return returnString;
    }

    // Private Methods
    private void importInventory() throws Exception {
        File inventoryFile = new File("ExampleFiles/VendingMachine.txt");

        try (Scanner scanner = new Scanner(inventoryFile)){
            while (scanner.hasNextLine()){
                String nextLine = scanner.nextLine();
                String[] itemArray = nextLine.split("\\|");
                Queue<Item> slot = new LinkedList<>();

                //index 0 - slot, 1 - name, 2 - price, 3 - type
                for (int i=0; i<5; i++) {
                    String name = itemArray[1];
                    BigDecimal price = new BigDecimal(itemArray[2]);
                    String type = itemArray[3];
                    Item item = new Item(name, price, type);
                    slot.offer(item);
                }
                INVENTORY.put(itemArray[0], slot);
                STARTING_INVENTORY.put(itemArray[0], new LinkedList<>(slot));

            }

        } catch(FileNotFoundException e) {
            throw new Exception("File provided does not exist, inventory is empty.");
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
    private BigDecimal changeRefactor(BigDecimal change) {
        BigDecimal amount = changeLeft.subtract(changeLeft.remainder(change));
        amount = amount.divide(change, 0, RoundingMode.HALF_EVEN);
        changeLeft = changeLeft.remainder(change);
        return amount;
    }
    private void subFromBalance(BigDecimal deposit) {
        userBalance = userBalance.subtract(deposit);
        vendingMachineRevenue = vendingMachineRevenue.add(deposit);
    }
    private void userLog(String whatToPrint, BigDecimal amount ) {

        try(PrintWriter dataFile = new PrintWriter(new FileOutputStream(FILE_LOG, isNotFirstLog))) {
            String dateTime = CURRENT_DATE_TIME.format(FORMAT_DATE_TIME);

            String logInfo = dateTime + " " + whatToPrint + " $" + String.format("%.2f", amount) + " $" + String.format("%.2f", userBalance);

            dataFile.println(logInfo);

            isNotFirstLog = true;

        } catch (FileNotFoundException ignored) {}

    }
    private void clearBalance() {
        userBalance = BigDecimal.ZERO;
    }
}
