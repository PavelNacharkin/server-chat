package ru.itsjava.services;

public interface Observable {
    void addObserver(Observer observer);
    void deleteObserver(Observer observer);
    void notifyObserver(String message);
    void notifyObserverExceptMe(Observer observer, String message);
    void notifyObserverMe(Observer observer,String message);
}
