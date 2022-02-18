package com.techelevator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

public class ItemTest {
    Item chip;
    Item candy;
    Item drink;
    Item gum;

    @Before
    public void setup() {
        try {
            chip = new Item("Potato Crisps", new BigDecimal("3.05"), "Chip");
            candy = new Item("Moonpie", new BigDecimal("1.80"), "Candy");
            drink = new Item("Cola", new BigDecimal("1.25"), "Drink");
            gum = new Item("Little League Chew", new BigDecimal("0.95"), "Gum");
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    @Test
    public void test_print_message() {
        Assert.assertEquals("Crunch Crunch, Yum!", chip.toString());
        Assert.assertEquals("Munch Munch, Yum!", candy.toString());
        Assert.assertEquals("Glug Glug, Yum!", drink.toString());
        Assert.assertEquals("Chew Chew, Yum!", gum.toString());
    }
}
