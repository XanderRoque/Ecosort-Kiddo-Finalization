package com.ecosort.ecosortkiddo.utils;

import com.ecosort.ecosortkiddo.model.Settings;

import java.util.HashMap;
import java.util.Map;


/**
 * Magagamit lang kapag 2 lang ang language, English to Filipino
 */
public class TranslatorUtil {

    private static Map<String, String>languageMap = new HashMap<>();

    static {

        //Main Activity
        languageMap.put("Location", "Lokasyon");
        languageMap.put("Profile", "Profile");

        //Settings Activity
        languageMap.put("Music", "Musika");
        languageMap.put("Sound", "Tunog");
        languageMap.put("Language", "Wika");

        //Music And Sound
        languageMap.put("On", "Buksan");
        languageMap.put("Off", "Patayin");

        //Location Activity
        languageMap.put("Select A Location", "Pumili ng Lokasyon");
        languageMap.put("House", "Bahay");
        languageMap.put("Backyard", "Likod-Bahay");
        languageMap.put("Forest", "Gubat");
        languageMap.put("Beach", "Dalampasigan");

        //Profile
        languageMap.put("Achievements", "Mga Nakamit");
        languageMap.put("Save Changes?", "I-save ang mga Pagbabago?");
        languageMap.put("Save", "I-save");
        languageMap.put("Don't Save", "Huwag I-save");

        //General Location Select Level
        languageMap.put("Select A Level", "Pumili ng Level");

        //General Play
        languageMap.put("Play", "Laruin");

        //General Back
        languageMap.put("Back", "Bumalik");

        //General Retry
        languageMap.put("Retry", "Ulitin");

        //General Next
        languageMap.put("Next", "Susunod");

        //Generel Back Cardview
        languageMap.put("Would you like to return to the Levels?", "Gusto mo bang bumalik sa Mga Levels?");

        //General Yes
        languageMap.put("Yes", "Oo");

        //General No
        languageMap.put("No", "Hindi");

        //General Try Again
        languageMap.put("Try Again", "Subukan Muli");

        //General Time is Up
        languageMap.put("Time is Up!", "Tapos na ang oras!");

        //General Question Sa Backyard Location
        languageMap.put("What type of waste is this?","Anong uri ng basura ito?");

        //Garbages
        languageMap.put("Crumpled Paper","Lukot na Papel");
        languageMap.put("Pizza Box","Kahon ng Pizza");
        languageMap.put("Used Plastic Cup","Gamit na Plastik na Baso");
        languageMap.put("Fruit Peels","Balat ng Prutas");
        languageMap.put("Used Can","Gamit na Lata");
        languageMap.put("Crumpled Chips Bag","Lukot na Supot ng Chips");
        languageMap.put("Broken Car","Sirang Laruang Sasakyan");
        languageMap.put("Dirty Paper Roll","Maduming Papel na Rolyo");
        languageMap.put("Bitten Burger","May Kagat na Burger");
        languageMap.put("Bitten Pizza","May Kagat na Pizza");
        languageMap.put("Cracked Egg Shell","Sira na Balat ng Itlog");
        languageMap.put("Broken Tree Branch","Sirang Sanga ng Puno");
        languageMap.put("Broken Class","Sirang Baso");
        languageMap.put("Broken Light Bulb","Sirang Bumbilya");
        languageMap.put("Banana Peel","Balat ng Saging");
        languageMap.put("Squeeze Ketchup Bottle","Pinisil na Bote ng Ketchupo");
        languageMap.put("Bone","Buto");
        languageMap.put("Garbage Bag","Sako ng Basura");
        languageMap.put("Tea Bag","Sachet ng Tsaa");
        languageMap.put("Rotten Meat","Nabubulok na Karne");
        languageMap.put("Moldy Bread","Tinapay na May Amag");
        languageMap.put("Trash Bag","Sako ng Basura");
        languageMap.put("Candy Wrapper","Balat ng Kendi");

        //Information About Us
        languageMap.put("About Us", "Tunkol Saamin");
        languageMap.put("Our Team", "Aming Grupo");
        languageMap.put("Reference", "Reperensya");
        languageMap.put("Terms and Conditions", "Tuntunin at Kondisyon");
        languageMap.put("Back  >>", "Bumalik  >>");

        //Reference
        languageMap.put("Design Reference", "Gabay sa Disenyo");
        languageMap.put("Person", "Tao");
        languageMap.put("Links", "Mga Koneksyon");
        languageMap.put("Sound and Music Reference", "Gabay sa Tunog at Musika");

        //Terms and Condition
        languageMap.put("Effective Date: October 2024", "Petsa ng Bisa: Oktubre 2024");
    }

    public static String translate(String word, String languageCode){
        if(Settings.LANGUAGE_ENGLISH.equals(languageCode)){
            return word;
        }else if(Settings.LANGUAGE_FILIPINO.equals(languageCode)){
            return languageMap.getOrDefault(word, word);
        }
        return word;
    }
}
