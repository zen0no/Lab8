package utils;

import com.google.gson.internal.LinkedTreeMap;
import dataClasses.*;
import exceptions.*;
import login.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

/**
 * Class for building HumanBeing instances
 */
public class HumanBeingBuilder {
    HumanBeing currentHuman;
    private static int idCounter = 0;


    /**
     * Creates new instance of HumanBeing
     * @param primaryKey
     * @throws BuilderException if builder already building
     */
    public void create(String primaryKey, String userLogin) throws BuilderException {
        idCounter++;
        if (currentHuman == null) {
            currentHuman = new HumanBeing(primaryKey, idCounter);
            currentHuman.setUserLogin(userLogin);
        }
        else throw new BuilderIsBusyException();
    }


    public void create(String primaryKey, int id, Date creationDate, String userLogin) throws BuilderException {
        if (id > idCounter){
            idCounter = id;
        }
        idCounter++;
        if (currentHuman == null) {
            currentHuman = new HumanBeing(primaryKey, id, creationDate);
            currentHuman.setUserLogin(userLogin);
        }

        else throw new BuilderIsBusyException();


    }

    /**
     * Creates instance of HumanBeing without increasing counter
     */
    public void createTemp(String primaryKey, int id, Date creationDate){
        if (currentHuman == null) currentHuman = new HumanBeing(primaryKey, id, creationDate);
        else throw new BuilderIsBusyException();
    }

    public void createTemp(){
        createTemp("", 0, new Date());
    }



    /**
     * Updates entity
     * @param entity entity to update
     * @throws BuilderException if builder already building
     * @throws IllegalModelFieldException
     */
    public void update(HumanBeing entity) throws BuilderException, IllegalModelFieldException{
        if (currentHuman == null) currentHuman = entity.clone();
        else throw  new BuilderException("Builder is busy");
    }

    public void build(LinkedTreeMap<String, Object> o){
        for (Map.Entry v: o.entrySet()){
            try{
                build((String) v.getKey(), (Map<String, String>) v.getValue());
            }
            catch (ClassCastException e){
                build((String) v.getKey(), (String) v.getValue());
            }
        }
    }

    public void build(ResultSet set) throws SQLException {
        currentHuman.setCar(new Car(set.getBoolean("car_cool")));
        currentHuman.setName(set.getString("name_"));
        currentHuman.setCoordinates(new Coordinates(set.getDouble("coordinates_x"), set.getDouble("coordinates_y")));
        currentHuman.setHasToothpick(set.getBoolean("hastoothpeak"));
        currentHuman.setRealHero(set.getBoolean("realhero"));
        currentHuman.setMood(Mood.parseMood(set.getString("mood")));
        currentHuman.setWeaponType(WeaponType.parseWeaponType(set.getString("weaponType")));
        currentHuman.setImpactSpeed(set.getInt("impactspeed"));
    }

    /**
     * Builds fields
     * @param fieldName
     * @param fieldValue
     * @throws BuilderException
     */
    public void build(String fieldName, Map<String, String> fieldValue) throws BuilderException{
       try{
            if (!HumanBeing.getFields().contains(fieldName))
                throw new BuilderException("HumanBeing.%s: no such field".formatted(fieldName));
            if (HumanBeing.getAutoGeneratedFields().contains(fieldName))
                throw new BuilderException("HumanBeing.%s: field is auto-generated");
            if (fieldName.equals("name")) currentHuman.setName(fieldValue.get("value"));
            if (fieldName.equals("coordinates")) currentHuman.setCoordinates(Coordinates.parseCoordinates(fieldValue));
            if (fieldName.equals("realHero")) currentHuman.setRealHero(parseBoolean(fieldValue.get("value")));
            if (fieldName.equals("hasToothpick")) currentHuman.setHasToothpick(parseBoolean(fieldValue.get("value")));
            if (fieldName.equals("impactSpeed")) currentHuman.setImpactSpeed(Integer.parseInt(fieldValue.get("value")));
            if (fieldName.equals("mood")) currentHuman.setMood(Mood.parseMood(fieldValue.get("value")));
            if (fieldName.equals("weaponType")) currentHuman.setWeaponType(WeaponType.parseWeaponType(fieldValue.get("value")));
            if (fieldName.equals("car")) currentHuman.setCar(Car.parseCar(fieldValue));
       }
       catch (ModelFieldException e){
           throw new BuilderException("Build error: " + e.getMessage());
       }
       catch (NumberFormatException e){
           throw new BuilderException("can't parse number from value");
       }
    }

    public void build(String fieldName, String fieldValue) throws ModelFieldException, BuilderException{
        try {
            if (!HumanBeing.getFields().contains(fieldName))
                throw new BuilderException("HumanBeing.%s: no such field".formatted(fieldName));
            if (HumanBeing.getAutoGeneratedFields().contains(fieldName))
                throw new BuilderException("HumanBeing.%s: field is auto-generated");
            if (fieldName.equals("name")) currentHuman.setName(fieldValue);
            if (fieldName.equals("coordinates")) currentHuman.setCoordinates(Coordinates.parseCoordinates(fieldValue));
            if (fieldName.equals("realHero")) currentHuman.setRealHero(parseBoolean(fieldValue));
            if (fieldName.equals("hasToothpick")) currentHuman.setHasToothpick(parseBoolean(fieldValue));
            if (fieldName.equals("impactSpeed")) currentHuman.setImpactSpeed(Integer.parseInt(fieldValue));
            if (fieldName.equals("mood")) currentHuman.setMood(Mood.parseMood(fieldValue));
            if (fieldName.equals("weaponType")) currentHuman.setWeaponType(WeaponType.parseWeaponType(fieldValue));
            if (fieldName.equals("car")) currentHuman.setCar(Car.parseCar(fieldValue));
        }
        catch (NullFieldException e){
            throw new BuilderException("Build error: Invalid value for field \"" + e.getField() + "\"");
        }

        catch (ModelFieldException e){
            throw new BuilderException("Build error: " + e.getMessage());
        }
        catch (NumberFormatException e){
            throw new BuilderException("can't parse number from value");
        }
    }


    /**
     * Get completed instance of HumanBeing
     * @return
     * @throws BuilderException
     */
    public HumanBeing get() throws BuilderException{
        for (String field: HumanBeing.getFields()) if (!currentHuman.getCurrentFields().contains(field))
            throw new BuilderException("HumanBeing.%s: field must be specified".formatted(field));
        HumanBeing temp = currentHuman;
        currentHuman = null;
        return temp;
    }

    /**
     * Method to correct parsing of boolean from string
     * @param s
     * @return
     */
    private Boolean parseBoolean(String s){
        if ("true".equals(s.toLowerCase(Locale.ROOT))) return true;
        if ("false".equals(s.toLowerCase(Locale.ROOT))) return false;
        return null;
    }

    /**
     * Clears instance, which builder working on
     */
    public void clear(){
        this.currentHuman = null;
    }

}