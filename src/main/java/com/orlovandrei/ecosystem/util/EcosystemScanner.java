package com.orlovandrei.ecosystem.util;

import com.orlovandrei.ecosystem.annotation.Utility;

import java.util.Scanner;

@Utility
public class EcosystemScanner {
    private static EcosystemScanner instance;
    private Scanner scanner;

    private EcosystemScanner() {
        scanner = new Scanner(System.in);
    }

    public static synchronized EcosystemScanner getInstance() {
        if (instance == null) {
            instance = new EcosystemScanner();
        }
        return instance;
    }

    public Scanner getScanner() {
        return scanner;
    }
}

