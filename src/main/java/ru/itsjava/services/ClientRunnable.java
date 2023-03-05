package ru.itsjava.services;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import ru.itsjava.dao.UserDao;
import ru.itsjava.domain.User;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

@RequiredArgsConstructor
public class ClientRunnable implements Runnable, Observer {
    private final Socket socket;
    private final ServerService serverService;
    private User user;
    private final UserDao userDao;

    @SneakyThrows
    @Override
    public void run() {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String messageFromClient = bufferedReader.readLine();
        if (authorization(messageFromClient) || registration(messageFromClient)) {
            System.out.println("Client connected");
            serverService.addObserver(this);
            serverService.notifyObserverMe(this, "Вы поключенны к чату");

            while ((messageFromClient = bufferedReader.readLine()) != null) {

                System.out.println(user.getName() + ":" + messageFromClient);
                serverService.notifyObserverExceptMe(this, user.getName() + ":" + messageFromClient);
            }
        }
    }

    @SneakyThrows
    private boolean authorization(String authorizationMessage) {

        if (authorizationMessage.startsWith("!autho!")) {
            String login = authorizationMessage.substring(7).split(":")[0];
            String password = authorizationMessage.substring(7).split(":")[1];

            user = userDao.findByNameAndPassword(login, password);
            return true;

        }
        return false;

    }

    @SneakyThrows
    private boolean registration(String registrationMessage) {
        if (registrationMessage.startsWith("!regis!")) {
            String login = registrationMessage.substring(7).split(":")[0];
            String password = registrationMessage.substring(7).split(":")[1];
            user = userDao.createNewUser(login, password);
            return true;
        }
        return false;

    }

    @SneakyThrows
    @Override
    public void notifyMe(String message) {
        PrintWriter clientWriter = new PrintWriter(socket.getOutputStream());
        clientWriter.println(message);
        clientWriter.flush();
    }
}

