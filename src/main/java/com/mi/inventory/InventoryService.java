package com.mi.inventory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {

    final private static Logger log = LoggerFactory.getLogger(InventoryService.class);

    @Value("${data.location}")
    private String dataLocation;

    @Value("${data.filename}")
    private String dataFilename;

    public Inventory getInvetory(String id) {
        Inventory result = new Inventory();

        List<Inventory> inventories = null;

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Type type = new TypeToken<List<Inventory>>() {
        }.getType();

        InputStream stream = null;
        try {
            if (Files.exists(Paths.get(dataLocation + File.separator + dataFilename))) {
                stream = new FileInputStream(dataLocation + File.separator + dataFilename);
            } else {
                stream = this.getClass().getClassLoader().getResourceAsStream(dataFilename);
            }

            inventories = gson.fromJson(new InputStreamReader(stream, StandardCharsets.UTF_8), type);
        } catch (FileNotFoundException e) {
            inventories = new ArrayList<>();
        }

        for (Inventory i : inventories) {
            if (i.getId().equals(id)) {
                result = i;
                break;
            }
        }

        log.info("loaded: {}", gson.toJson(result));
        return result;
    }

}
