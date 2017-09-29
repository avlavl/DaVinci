package com.aioros.investor;

/**
 * Created by aizhang on 2017/6/25.
 */

import java.util.ArrayList;

import static com.aioros.investor.FormulaLib.MA;

public class MAL {
    public ArrayList<Double> priceList = new ArrayList<>();

    public MAL(ArrayList<Double> list) {
        priceList = list;
    }

    public ArrayList<Double> getMAList(int num) {
        ArrayList<Double> maList = new ArrayList<>();
        int size = priceList.size();
        for (int i = 0; i < size; i++) {
            maList.add(MA(priceList, i, num));
        }
        return maList;
    }

    public double getMAKey(int s, int l) {
        double lm = MA(priceList, priceList.size() - 1, l - 1);
        if (s > 1) {
            double sm = MA(priceList, priceList.size() - 1, s - 1);
            double key = (lm * (l - 1) * s - sm * (s - 1) * l) / (l - s);
            return key;
        }
        return lm;
    }

}
