package database;

import humans.*;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
public class DatabaseHandler {
    private final Connection connection;
    private static final String REGISTER_USER = "INSERT INTO users (user_name, user_password) VALUES (?,?) RETURNING id";
    private static final String CHECK_USER_EXIST = "SELECT COUNT(*) FROM users WHERE user_name = ?";
    private static final String VALIDATE_USER = "SELECT COUNT(*) AS count FROM users WHERE user_name = ? AND user_password = ?";
    private static final String GET_USER_ID_BY_NAME = "SELECT id FROM users WHERE user_name = ?";
    private static final String ADD_HUMAN_REQUEST = "INSERT INTO humans (hum_name, coordinate_id, hum_time, hum_realhero, hum_hasthoothpick, hum_speed, hum_weapon, hum_mood, car_id, user_owner)" +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING hum_id";
    private static final String ADD_CAR_REQUEST = "INSERT INTO cars (car_name)" +
            "VALUES (?) RETURNING id";
    private static final String ADD_COORDINATES_REQUEST = "INSERT INTO coordinates (coor_x, coor_y) VALUES (?, ?) RETURNING id";
    private static final String GET_HUMANS_BY_USER_ID = "SELECT hum_id FROM humans WHERE user_owner = ?";
    private static final String GET_CAR_BY_HUMAN_ID = "SELECT owner_id FROM cars WHERE hum_id = ?";
    private static final String REMOVE_HUMAN_BY_ID = "DELETE FROM humans WHERE hum_id = ?";
    private static final String REMOVE_CAR_BY_ID = "DELETE FROM cars WHERE id = ?";
    private static final String GET_COORDINATES_BY_HUMANS_ID = "SELECT coordinate_id FROM humans WHERE hum_id = ?";
    private static final String REMOVE_COORDINATE_BY_ID = "DELETE FROM coordinates WHERE id = ?";
    private static final String CHECK_HUMAN_OWNER  = "SELECT COUNT(*) FROM humans WHERE hum_id = ? AND user_owner = ?";
    private static final String CHECK_HUMAN_BY_ID = "SELECT EXISTS (SELECT (1) FROM humans WHERE hum_id = ?)";
    private static final String UPDATE_COORDINATE = "UPDATE coordinates SET coor_x = ?, coor_y = ? WHERE id = ?";
    private static final String UPDATE_HUMAN = "UPDATE humans SET hum_name = ?, hum_time = ?," +
            " hum_realhero = ?, hum_hasthoothpick = ?, hum_speed = ?, hum_weapon = ?, hum_mood = ? WHERE prod_id = ?";
    private static final String UPDATE_CAR = "UPDATE persons SET car_name = ? WHERE id = ?";

    public DatabaseHandler(String url, String userName, String password) throws SQLException {
        this.connection = DriverManager.getConnection(url, userName, password);
    }

    public synchronized Optional<HumanBeing> addProduct(HumanBeing humanBeing, int owner) {
        String humanName = humanBeing.getName();
        Coordinates coordinates = humanBeing.getCoordinates();
        LocalDateTime creationDate = humanBeing.getCreationDate();
        boolean realhero = humanBeing.getRealHero();
        boolean tooth = humanBeing.getHasToothpick();
        long speed = humanBeing.getImpactSpeed();
        WeaponType weaponType = humanBeing.getWeaponType();
        Mood mood = humanBeing.getMood();
        Car car = humanBeing.getCar();
        try {
            connection.setAutoCommit(false);
            connection.setSavepoint();

            PreparedStatement addCoordinates = connection.prepareStatement(ADD_COORDINATES_REQUEST, Statement.RETURN_GENERATED_KEYS);
            addCoordinates.setLong(1, coordinates.getX());
            addCoordinates.setFloat(2, coordinates.getY());
            addCoordinates.executeUpdate();
            ResultSet coordinateSet = addCoordinates.getGeneratedKeys();
            coordinateSet.next();
            int coordinateId = coordinateSet.getInt(1);
            addCoordinates.close();

            PreparedStatement addCar = connection.prepareStatement(ADD_CAR_REQUEST, Statement.RETURN_GENERATED_KEYS);
            addCar.setString(1, car.getName());
            addCar.executeUpdate();
            ResultSet carSet = addCar.getGeneratedKeys();
            carSet.next();
            int carId = carSet.getInt(1);
            addCar.close();

            PreparedStatement addHuman = connection.prepareStatement(ADD_HUMAN_REQUEST, Statement.RETURN_GENERATED_KEYS);
            addHuman.setString(1, humanName);
            addHuman.setInt(2, coordinateId);
            addHuman.setDate(3, (java.sql.Date) Date.from(Instant.from(creationDate.toLocalDate())));
            addHuman.setBoolean(4, realhero);
            addHuman.setBoolean(5, tooth);
            addHuman.setLong(6, speed);
            addHuman.setObject(7, weaponType.getName());
            addHuman.setObject(8, mood.getName());
            addHuman.setInt(9, carId);
            addHuman.setInt(10, owner);
            addHuman.executeUpdate();
            ResultSet humanSet = addHuman.getGeneratedKeys();
            humanSet.next();
            int product_id = humanSet.getInt(1);
            addHuman.close();

            connection.commit();
            connection.setAutoCommit(true);
            humanBeing.setId(product_id);
        } catch (SQLException e) {
            log.error(e.getSQLState(), e);
            return Optional.empty();
        }
        return Optional.of(humanBeing);
    }

    public void updateHuman(HumanBeing human, int humId) throws SQLException {
        Car car = human.getCar();
        Coordinates coordinates = human.getCoordinates();

        PreparedStatement getCoordinateId = connection.prepareStatement(GET_COORDINATES_BY_HUMANS_ID);
        getCoordinateId.setInt(1, humId);
        ResultSet set = getCoordinateId.executeQuery();
        set.next();
        int coordinateId = set.getInt(1);
        getCoordinateId.close();

        PreparedStatement getCar = connection.prepareCall(GET_CAR_BY_HUMAN_ID);
        getCar.setInt(1, humId);
        ResultSet resultSet = getCar.executeQuery();
        resultSet.next();
        int CarId = resultSet.getInt(1);
        getCar.close();

        PreparedStatement updateCoordinates = connection.prepareStatement(UPDATE_COORDINATE);
        updateCoordinates.setLong(1, coordinates.getX());
        updateCoordinates.setFloat(2, coordinates.getY());
        updateCoordinates.setInt(3, coordinateId);
        updateCoordinates.executeUpdate();
        updateCoordinates.close();

        PreparedStatement updateCar = connection.prepareStatement(UPDATE_CAR);
        updateCar.setString(1, car.getName());
        updateCar.setInt(2, humId);
        updateCar.executeUpdate();
        updateCar.close();

        PreparedStatement updateHuman = connection.prepareStatement(UPDATE_HUMAN);
        updateHuman.setString(1, human.getName());
        updateHuman.setDate(2, (java.sql.Date) Date.from(Instant.from(human.getCreationDate().toLocalDate())));
        updateHuman.setBoolean(3, human.getRealHero());
        updateHuman.setBoolean(4, human.getHasToothpick());
        updateHuman.setLong(5, human.getImpactSpeed());
        updateHuman.setObject(6, human.getWeaponType().getName(), Types.OTHER);
        updateHuman.setObject(7, human.getMood().getName(), Types.OTHER);
        updateHuman.setInt(8, humId);
        updateHuman.executeUpdate();
        updateHuman.close();
    }

    public synchronized void removeHuman(int id) throws SQLException {
        PreparedStatement getCoordinateId = connection.prepareStatement(GET_COORDINATES_BY_HUMANS_ID);
        getCoordinateId.setInt(1, id);
        ResultSet set = getCoordinateId.executeQuery();
        set.next();
        int coordinateId = set.getInt(1);
        getCoordinateId.close();

        PreparedStatement getCar = connection.prepareCall(GET_CAR_BY_HUMAN_ID);
        getCar.setInt(1, id);
        ResultSet resultSet = getCar.executeQuery();
        resultSet.next();
        int carId = resultSet.getInt(1);
        getCar.close();

        connection.setAutoCommit(false);
        connection.setSavepoint();

        PreparedStatement remHuman = connection.prepareCall(REMOVE_HUMAN_BY_ID);
        remHuman.setInt(1, id);
        remHuman.executeUpdate();
        remHuman.close();

        PreparedStatement remCar = connection.prepareCall(REMOVE_CAR_BY_ID);
        remCar.setInt(1, carId);
        remCar.executeUpdate();
        remCar.close();

        PreparedStatement remCoordinate = connection.prepareStatement(REMOVE_COORDINATE_BY_ID);
        remCoordinate.setInt(1, coordinateId);
        remCoordinate.executeUpdate();
        remCoordinate.close();

        connection.commit();
        connection.setAutoCommit(true);
    }

    public boolean checkById(int hum_id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(CHECK_HUMAN_BY_ID);
        statement.setInt(1, hum_id);
        ResultSet set = statement.executeQuery();
        set.next();
        boolean isExist = set.getBoolean(1);
        statement.close();
        return isExist;
    }

    public int registerUser(String userName, String password) throws SQLException {
        if (checkRegisterUser(userName)) {
            return -1;
        }
        PreparedStatement statement = connection.prepareStatement(REGISTER_USER, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, userName);
        statement.setString(2, password);
        statement.executeUpdate();
        ResultSet set = statement.getGeneratedKeys();
        set.next();
        int id = set.getInt(1);
        statement.close();
        return id;
    }

    public int getUserIdByName(String userName) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(GET_USER_ID_BY_NAME);
        statement.setString(1, userName);
        ResultSet set = statement.executeQuery();
        if (set.next()) {
            int id = set.getInt(1);
            statement.close();
            return id;
        } else {
            statement.close();
            return -1;
        }
    }

    public int logIn(String userName, String password) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(VALIDATE_USER);
        statement.setString(1, userName);
        statement.setString(2, password);
        ResultSet set = statement.executeQuery();
        set.next();
        int count = set.getInt(1);
        statement.close();

        if (count == 1) {
            PreparedStatement getId = connection.prepareStatement(GET_USER_ID_BY_NAME);
            getId.setString(1, userName);
            ResultSet idSet = getId.executeQuery();
            idSet.next();
            int id = idSet.getInt(1);
            getId.close();
            return id;
        } else {
            return -1;
        }
    }

    public boolean checkRegisterUser(String userName) throws SQLException {
        PreparedStatement state = connection.prepareStatement(CHECK_USER_EXIST);
        state.setString(1, userName);
        ResultSet result = state.executeQuery();
        result.next();
        int count = result.getInt(1);
        state.close();
        return count == 1;
    }

    public List<Integer> getHumansByUserId(int userId) throws SQLException {
        List<Integer> ids = new ArrayList<>();
        PreparedStatement state = connection.prepareStatement(GET_HUMANS_BY_USER_ID);
        state.setInt(1, userId);
        ResultSet set = state.executeQuery();
        while (set.next()) {
            ids.add(set.getInt("hum_id"));
        }
        state.close();
        return ids;
    }


    public boolean isOwner(int humId, int userId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(CHECK_HUMAN_OWNER);
        statement.setInt(1, humId);
        statement.setInt(2, userId);
        ResultSet set = statement.executeQuery();
        set.next();
        int count = set.getInt(1);
        return count == 1;
    }

    public Connection getConnection() {
        return connection;
    }
}