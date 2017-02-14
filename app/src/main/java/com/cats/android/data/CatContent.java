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

    //for @GET("/cats/{id}")
    private static Cat CAT;

    private static String response;

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

    public static String getResponse() {
        return response;
    }

    public static void setResponse(String response) {
        CatContent.response = response;
    }

    public static Cat getCAT() {
        return CAT;
    }

    public static void setCAT(Cat CAT) {
        CatContent.CAT = CAT;
    }
}
