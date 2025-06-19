package com.study.coffee.domain;

public class Coffee {
    
    private String menuId;
    private String name;
    private int price;

    public String getMenuId(){
        return menuId;
    }

    public void setMenuId(String menuId){
        this.menuId = menuId;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public int getPrice(){
        return price;
    }

    public void setPrice(int price){
        this.price = price;
    }
}
