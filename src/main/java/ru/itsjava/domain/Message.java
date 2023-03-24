package ru.itsjava.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Message {
    private final String from;
    private final String toText;
}
