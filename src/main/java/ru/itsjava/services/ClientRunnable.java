package ru.itsjava.services;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;
import ru.itsjava.dao.MessageDao;
import ru.itsjava.dao.UserDao;
import ru.itsjava.domain.User;
import ru.itsjava.exception.UserNotFoundException;

import java.io.*;
import java.net.Socket;

@Log4j
@RequiredArgsConstructor
public class ClientRunnable implements Runnable, Observer {
    private final Socket socket;
    private final ServerService serverService;
    private User user;
    private final UserDao userDao;
    private final PrintWriter fileWriter;
    private final MessageDao messageDao;

    @SneakyThrows
    @Override
    public void run() {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        String messageFromClient = bufferedReader.readLine();
        while (!verification(messageFromClient)) {
            messageFromClient = bufferedReader.readLine();
            verification(messageFromClient);
        }
        System.out.println("Client connected");
        while ((messageFromClient = bufferedReader.readLine()) != null) {
            fileWriter.println(user.getName() + ":" + messageFromClient);
            fileWriter.flush();
            messageDao.writeMessageToDataBase(user.getName(), messageFromClient);
            System.out.println(user.getName() + ":" + messageFromClient);
            serverService.notifyObserverExceptMe(this, user.getName() + ":" + messageFromClient);
        }

    }

    private boolean verification(String messageFromClient) {

        String login = messageFromClient.substring(7).split(":")[0];
        String password = messageFromClient.substring(7).split(":")[1];

        if (messageFromClient.startsWith("!autho!")) {
            if (authorization(login, password)) {
                log.info("Client " + user.getName() + " connected");
                serverService.addObserver(this);
                notifyMe("Вы поключенны к чату");
                return true;
            } else notifyMe("Пользователь не найден");
            return false;
        } else if (messageFromClient.startsWith("!regis!")) {
            if (registration(login, password)) {
                log.info("Client " + user.getName() + " connected");
                serverService.addObserver(this);
                notifyMe("Вы зарегестрировались и подключенны к чату");
                return true;
            } else notifyMe("Кажется такой пользователь уже есть, попробуйте авторизироваться");
        }
        return false;
    }

    @SneakyThrows
    private boolean authorization(String login, String password) {
        try {

            user = userDao.findByNameAndPassword(login, password);
            if (user != null) {
                log.info(("Client " + user.getName() + " authorized"));
                return true;
            }

        } catch (UserNotFoundException e) {
        }
        log.info(("Client not authorized"));
        return false;

    }

    @SneakyThrows
    private boolean registration(String login, String password) {
        if (authorization(login, password)) {
            log.info(("Client not registered"));
            return false;
        }
        user = userDao.createNewUser(login, password);
        log.info("Client " + user.getName() + " registered");
        return true;
    }

    @SneakyThrows
    @Override
    public void notifyMe(String message) {
        PrintWriter clientWriter = new PrintWriter(socket.getOutputStream());
        clientWriter.println(message);
        clientWriter.flush();
    }
}

