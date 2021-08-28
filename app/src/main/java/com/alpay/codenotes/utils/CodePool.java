package com.alpay.codenotes.utils;

import java.util.ArrayList;
import java.util.Random;

public class CodePool {
    private ArrayList<String> poolList;

    public CodePool() {
        poolList = new ArrayList();
        if (Utils.turtleMode)
            fillTurtleList();
        else
            fillPoolList();
    }

    private void fillTurtleList() {
        Random random = new Random();
        poolList.add("ileri #" + random.nextInt(100));
        poolList.add("tekrarla #" + random.nextInt(20));
        poolList.add("genislik #" + random.nextInt(10));
        poolList.add("renk #" + random.nextInt(100));
        poolList.add("sola #" + random.nextInt(100));
        poolList.add("saga #" + random.nextInt(100));
        poolList.add("bitir");
    }


    private void fillPoolList() {
        Random random = new Random();
        poolList.add("arkaplan #" + random.nextInt(255));
        poolList.add("kenar #" + random.nextInt(255));
        poolList.add("doldur #" + random.nextInt(255));
        poolList.add("elips");
        poolList.add("konum #" + Integer.toString(random.nextInt(400) + 50) +
                "#" + Integer.toString(random.nextInt(400) + 50));
        poolList.add("boyut #" + Integer.toString(random.nextInt(200) + 50) +
                "#" + Integer.toString(random.nextInt(200) + 50));
        poolList.add("üçgen");
    }

    public String drawRandomCodeFromPool() {
        Random random = new Random();
        return poolList.get(random.nextInt(poolList.size()));
    }
}
