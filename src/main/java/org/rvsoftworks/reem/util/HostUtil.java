package org.rvsoftworks.reem.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class HostUtil {
    public static String getHostIP(){
        try {
            InetAddress localHost = InetAddress.getLocalHost();

            return localHost.getHostAddress();
        } catch (UnknownHostException e) {
            return  "[SERVER]";
        }
    }
}
