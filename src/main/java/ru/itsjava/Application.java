package ru.itsjava;

import ru.itsjava.services.ServerService;
import ru.itsjava.services.ServerServiceImpl;

import java.io.FileNotFoundException;

public class Application {

    public static void main(String[] args) throws FileNotFoundException {
        ServerService serverService = new ServerServiceImpl();
        serverService.start();





    }
}
