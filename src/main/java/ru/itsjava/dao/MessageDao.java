package ru.itsjava.dao;

public interface MessageDao {
   void writeMessageToDataBase(String user, String toText);
}
