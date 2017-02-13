package com.cats.android.data;

import com.cats.android.model.Cat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by andrey on 13.02.17.
 */

public class CatContent {

    private static List<Cat> ITEMS;

    private static Map<Integer, Cat> ITEM_MAP = new HashMap<Integer, Cat>();

    public static void putItems(List<Cat> cats) {
        ITEMS = cats;
        Cat cat;
        for (int i = 0; i < cats.size(); i++) {
            cat = cats.get(i);
            ITEM_MAP.put(cat.getId(), cat);
        }
    }

    public static List<Cat> getITEMS() {
        return ITEMS;
    }

    public static Map<Integer, Cat> getItemMap() {
        return ITEM_MAP;
    }
}
