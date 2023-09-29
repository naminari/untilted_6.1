package database;

import collection.SimpleHumanValidator;
import collection.Validator;
import exceptions.ValidException;
import humans.*;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class PostgresHumanReader extends PostgresReader<HumanBeing>{
    private static final String TAKE_ALL_PRODUCTS_REQUESTS = "select * from products\n" +
            "    join persons on products.owner_id = persons.id\n" +
            "    join locations on persons.location_id = locations.id\n" +
            "    join coordinates on products.coordinate_id = coordinates.id;";
    @Override
    public List<HumanBeing> readObjects(Connection connection) throws SQLException {
        List<HumanBeing> humans = new ArrayList<>();
        PreparedStatement jStatement = connection.prepareStatement(TAKE_ALL_PRODUCTS_REQUESTS);
        ResultSet result = jStatement.executeQuery();

        while (result.next()) {
            try {
                HumanBeing human = getHuman(result);
                humans.add(human);
            } catch (ValidException | IllegalArgumentException e) {
                log.error(e.getMessage(), e);
            }
        }

        jStatement.close();
        return humans;
    }
    private HumanBeing getHuman(ResultSet set) throws SQLException, ValidException {
        int id = set.getInt("hum_id");
        String prod_name = set.getString("hum_name");
        Long x = set.getLong("coor_x");
        float y = set.getFloat("coor_y");
        LocalDateTime time = LocalDateTime.parse(set.getString("hum_time"));
        boolean hero = set.getBoolean("hum_realhero");
        boolean tooth = set.getBoolean("hum_hastoothpick");
        long speed = set.getLong("hum_speed");
        WeaponType weaponType = WeaponType.getWeaponByName(set.getString("hum_weapon"));
        Mood mood = Mood.getMoodByName(set.getString("hum_mood"));
        String car_name = set.getString("car_name");
        Validator<HumanBeing> validator = new SimpleHumanValidator();
        Coordinates coordinates = new Coordinates(x,y);
        Car car = new Car(car_name);
        HumanBeing humanBeing = new HumanBeing(id, prod_name, coordinates, time, hero, tooth, speed, weaponType, mood, car);
        validator.checkElement(humanBeing);
        return humanBeing;
    }

}