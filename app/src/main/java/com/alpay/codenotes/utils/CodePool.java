package com.alpay.codenotes.utils;

import java.util.ArrayList;
import java.util.Random;

public class CodePool {
    private ArrayList<String> poolList;

    public CodePool() {
        poolList = new ArrayList();
        fillPoolList();
    }

    private void fillPoolList() {
        Random random = new Random();
        poolList.add("arkaplan r: " + random.nextInt(255) + " g: " + random.nextInt(255) + " b: " + random.nextInt(255));
        poolList.add("kenar r: " + random.nextInt(255) + " g: " + random.nextInt(255) + " b: " + random.nextInt(255));
        poolList.add("doldur r: " + random.nextInt(255) + " g: " + random.nextInt(255) + " b: " + random.nextInt(255));
        poolList.add("elips x: " + Integer.toString(random.nextInt(400) + 50) +
                " y: " + Integer.toString(random.nextInt(400) + 50) +
                " w: " + Integer.toString(random.nextInt(250) + 50) +
                " h: " + Integer.toString(random.nextInt(250) + 50));
        poolList.add("dörtgen x: " + Integer.toString(random.nextInt(400) + 50) +
                " y: " + Integer.toString(random.nextInt(400) + 50) +
                " w: " + Integer.toString(random.nextInt(250) + 50) +
                " h: " + Integer.toString(random.nextInt(250) + 50));
        poolList.add("üçgen x: " + Integer.toString(random.nextInt(400) + 50) +
                " y: " + Integer.toString(random.nextInt(400) + 50) +
                " w: " + Integer.toString(random.nextInt(250) + 50) +
                " h: " + Integer.toString(random.nextInt(250) + 50));
        poolList.add("çizgi sx: " + Integer.toString(random.nextInt(400) + 50) +
                " sy: " + Integer.toString(random.nextInt(400) + 50) +
                " ex: " + Integer.toString(random.nextInt(250) + 50) +
                " ey: " + Integer.toString(random.nextInt(250) + 50));
    }

    public String drawRandomCodeFromPool() {
        Random random = new Random();
        return poolList.get(random.nextInt(poolList.size()));
    }
}
