package com.zipsecure.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetAddress;

public class Network {
    private static final Logger LOGGER = LogManager.getLogger(Network.class);
    private static final int TIMEOUT = 2000;
    public static boolean isConnected(String address){
        boolean isConnected = false;
        try {
            InetAddress byName = InetAddress.getByName(address);
            isConnected = byName.isReachable(TIMEOUT);
        } catch (IOException e) {
            LOGGER.error("Can not to connect to server: " + e.getMessage());
        }
        return isConnected;
    }
}
