package com.techelevator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

public class VendingMachineTest {
    VendingMachine vendingMachine;
    String chip;
    String candy;
    String drink;
    String gum;

    @Before
    public void setup() {
        /*
        UserBalance = 81.85
        Dollars: 81
        Quarters: 3
        Dimes: 1
        Revenue = 18.15
         */

        try {
            vendingMachine = new VendingMachine();
            vendingMachine.feedMoney("100.00");
            chip = vendingMachine.selectProduce("A1");
            vendingMachine.selectProduce("A1");
            vendingMachine.selectProduce("d2");
            drink = vendingMachine.selectProduce("c4");
            candy = vendingMachine.selectProduce("B2");
            vendingMachine.selectProduce("D1");
            gum = vendingMachine.selectProduce("d1");
            vendingMachine.selectProduce("d1");
            vendingMachine.selectProduce("A4");
            vendingMachine.selectProduce("d2");
            vendingMachine.selectProduce("D2");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void test_sales_report() {
        String expected = "\n**SALES REPORT**\nPotato Crisps | 2\nCloud Popcorn | 1\nCowtales | 1\nHeavy | 1\nU-Chews | 3\nLittle League Chew | 3\n\n**TOTAL REVENUE** $18.15\n";
        Assert.assertEquals(expected,vendingMachine.salesReport());
    }

    @Test
    public void test_import_inventory_FYI_Will_break_if_you_change_file() {
        Assert.assertEquals("Potato Crisps", vendingMachine.getInventory().get("A1").poll().getName());
    }

    @Test
    public void test_user_balance_test_revenue() {
        Assert.assertEquals("getUserBalance is off",new BigDecimal("81.85"), vendingMachine.getUserBalance());
        Assert.assertEquals("getRevenue is off",new BigDecimal("18.15"), vendingMachine.getVendingMachineRevenue());
    }

    @Test
    public void test_finish_transaction_clear_balance() {

        String expected = "Change dispensed: \nDollars: 81\nQuarters: 3\nDimes: 1";
        try {
            Assert.assertEquals(expected, vendingMachine.finishTransaction());
            Assert.assertEquals(BigDecimal.ZERO, vendingMachine.getUserBalance());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }



    }

    @Test
    public void test_select_product() {

        Assert.assertEquals("Crunch Crunch, Yum!\nYour current balance is $96.95", chip);
        Assert.assertEquals("Munch Munch, Yum!\nYour current balance is $89.95", candy);
        Assert.assertEquals("Glug Glug, Yum!\nYour current balance is $91.45", drink);
        Assert.assertEquals("Chew Chew, Yum!\nYour current balance is $88.25", gum);

    }
}
