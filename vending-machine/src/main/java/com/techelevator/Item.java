package com.techelevator;

import java.math.BigDecimal;

public class Item {
    private final String name;
    private final BigDecimal price;
    private final String type;


    public Item (String name, BigDecimal price, String type) throws Exception{
        if (name == null) {
            throw new Exception("Item name cannot be NULL");
        }
        if (price == null) {
            throw new Exception("Item price cannot be NULL");
        }
        if (type == null) {
            throw new Exception("Item type cannot be NULL");
        }
        this.name = name;
        this.price = price;
        this.type = type;
    }

    public String getName() {
        return name;
    }
    public BigDecimal getPrice() {
        return price;
    }
    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        String message = "";
        String itemType = getType();
        if (itemType.equalsIgnoreCase("chip")) {
            message = "Crunch Crunch, Yum!";
        }
        if (itemType.equalsIgnoreCase("candy")) {
            message = "Munch Munch, Yum!";
        }
        if (itemType.equalsIgnoreCase("drink")) {
            message = "Glug Glug, Yum!";
        }
        if (itemType.equalsIgnoreCase("gum")) {
            message = "Chew Chew, Yum!";
        }
        return message;
    }
}
