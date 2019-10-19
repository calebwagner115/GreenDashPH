package com.greendashPH;

public enum Breakdowns {
    ELECTRICITY("ELECTRICITY"),
    WATER("WATER"),
    GAS("GAS"),
    WASTE("WASTE")
    ;


    final private String source;

    private Breakdowns(String s){
        this.source=s;
    }
    @Override
    public String toString(){
        return source;
    }
}
