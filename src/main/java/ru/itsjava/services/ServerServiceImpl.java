package ru.itsjava.services;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;
import ru.itsjava.dao.MessageDao;
import ru.itsjava.dao.MessageDaoImpl;
import ru.itsjava.dao.UserDao;
import ru.itsjava.dao.UserDaoImpl;
import ru.itsjava.utils.Props;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

@Log4j
public class ServerServiceImpl implements ServerService {
    public final static int PORT = 8081;
    public final List<Observer> observers = new ArrayList<>();
    private final UserDao userDaoImpl = new UserDaoImpl(new Props());
    private final File file = new File("src/main/resources/file.txt");
    private final PrintWriter fileWriter = new PrintWriter(file);
    private final MessageDao messageDaoIml = new MessageDaoImpl(new Props());

    public ServerServiceImpl() throws FileNotFoundException {
    }

    @SneakyThrows
    @Override

    public void start() {

        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("== SERVER START ==");
        log.info("SERVER START");

        while (true) {
            Socket socket = serverSocket.accept();
            if (socket != null) {
                Thread thread = new Thread(new ClientRunnable(socket, this, userDaoImpl, fileWriter,messageDaoIml));
                thread.start();
            }

        }

    }

    @Override
    public void addObserver(Observer observer) {
     observers.add(observer);
    }

    @Override
    public void deleteObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObserver(String message) {
        for (Observer observer : observers) {
            observer.notifyMe(message);
        }
    }


    @Override
    public void notifyObserverExceptMe(Observer observer, String message) {
        for (Observer obs : observers) {
            if (!observer.equals(obs)) {
                obs.notifyMe(message);
            }
        }
    }

    public void notifyObserverMe(Observer observer, String message) {
        for (Observer obs : observers) {
            if (observer.equals(obs)) {
                obs.notifyMe(message);
            }
        }
    }
}
