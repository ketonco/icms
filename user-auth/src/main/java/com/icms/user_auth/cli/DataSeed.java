package com.icms.user_auth.cli;

public interface DataSeed {

    int getOrder();
    String getName();
    void run();
    
}