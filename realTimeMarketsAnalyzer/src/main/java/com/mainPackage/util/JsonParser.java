package com.mainPackage.util;

import java.util.ArrayList;

/**
 * Created by cozafiu on 21.04.2017.
 */

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.annotation.PostConstruct;

public class JsonParser {

    private static final String filePath = "/home/claudiu/workspace/Licenta/realTimeMarketsAnalyzer/Stock.json";
    private ArrayList<String> symbolys;

    @PostConstruct
    public void init() {
        symbolys = new ArrayList<String>();

        try {
            // read the json file
            FileReader reader = new FileReader(filePath);
            JSONParser jsonParser = new JSONParser();
            JSONArray jsonArray = (JSONArray) jsonParser.parse(reader);

            for (Object obj: jsonArray) {
                JSONObject jsonObject = (JSONObject) obj;
                symbolys.add(jsonObject.get("Ticker").toString());
            }

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ParseException ex) {
            ex.printStackTrace();
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    public ArrayList<String> getSymbolys() {
        return symbolys;
    }

}
