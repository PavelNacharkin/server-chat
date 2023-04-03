package ru.itsjava.dao;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import ru.itsjava.domain.User;
import ru.itsjava.exception.UserNotFoundException;
import ru.itsjava.utils.Props;

import java.sql.*;

@AllArgsConstructor
public class UserDaoImpl implements UserDao {
    private final Props props;

    @SneakyThrows
    @Override
    public User findByNameAndPassword(String name, String password)  {
        try
                (Connection connection = DriverManager.getConnection(
                        props.getValue("db.url"),
                        props.getValue("db.login"),
                        props.getValue("db.password"))

                ) {
            PreparedStatement preparedStatement = connection.prepareStatement("select count(*) cnt from schema_for_server.users where name = ? and password = ?;");
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            int userCount = resultSet.getInt("cnt");
            if (userCount == 1) {
                return new User(name, password);
            }
        } catch (UserNotFoundException userNotFoundException) {
            System.out.println("Кажется такой пользователь уже есть");
        }
        throw new UserNotFoundException("Пользователь не найден");
    }

    @SneakyThrows
    public User createNewUser(String name, String password) {
        try (Connection connection = DriverManager.getConnection(
                props.getValue("db.url"),
                props.getValue("db.login"),
                props.getValue("db.password")
        )) {
            PreparedStatement preparedStatement = connection.prepareStatement("Insert into schema_for_server.users(name,password) VALUES (?,?)");
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, password);
            preparedStatement.executeUpdate();
            return new User(name, password);
        } catch (UserNotFoundException userNotFoundException) {
            System.out.println("Кажется такой пользователь уже есть");
        }
        throw new UserNotFoundException("Пользователь не найден");
    }
}
