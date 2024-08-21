package io.github.qe7.utils.local;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.qe7.utils.UtilBase;
import org.apache.commons.io.IOUtils;

import java.net.URL;
import java.nio.charset.StandardCharsets;

public final class PlayerUtil extends UtilBase {

    public static String getCapeUrlFromUsername(String username) {
        try {
            final URL url = new URL("https://api.capes.dev/load/" + username + "/minecraft");
            JsonObject jsonObj = JsonParser.parseString(IOUtils.toString(url, StandardCharsets.UTF_8)).getAsJsonObject();
            return jsonObj.get("imageUrl").getAsString();
        } catch (Exception e) {
            System.err.println("Error loading cape for " + username + ": " + e.getMessage());
            return "";
        }
    }
}
