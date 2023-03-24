package ru.itsjava.dao;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import ru.itsjava.utils.Props;

import java.sql.*;

@AllArgsConstructor
public class MessageDaoImpl implements MessageDao {
    private final Props props;

    @SneakyThrows
    @Override
    public void writeMessageToDataBase(String user, String toText) {
        try (Connection connection = DriverManager.getConnection(
                props.getValue("db.url"),
                props.getValue("db.login"),
                props.getValue("db.password"))
        ) {
            PreparedStatement preparedStatement = connection.prepareStatement("Insert into schema_for_server.message(user,text) values (?,?);");
            preparedStatement.setString(1, user);
            preparedStatement.setString(2, toText);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Кажется такой пользователь уже есть");
        }
    }
}


