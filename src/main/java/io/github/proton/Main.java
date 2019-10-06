package io.github.proton;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import io.github.proton.display.TerminalDisplay;
import io.reactivex.rxjava3.core.Observable;

import java.util.ArrayList;
import java.util.List;

public final class Main {
    public static void main(String[] args) throws Throwable {
        try (TerminalDisplay display = new TerminalDisplay()) {
            display.start().blockingAwait();
            List<Character> characters = new ArrayList<>();
            while (true) {
                KeyStroke keyStroke = display.read().blockingGet();
                if (keyStroke.getKeyType() == KeyType.EOF) break;
                characters.add(keyStroke.getCharacter());
                display.write(
                        new TerminalPosition(0, 0),
                        Observable.fromIterable(characters).map(TextCharacter::new)
                ).blockingAwait();
                display.refresh().blockingAwait();
            }
        }
    }
}
