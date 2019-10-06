package io.github.proton;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextCharacter;
import io.github.proton.display.TerminalDisplay;
import io.reactivex.rxjava3.core.Observable;

public final class Main {
    public static void main(String[] args) throws Throwable {
        try (TerminalDisplay display = new TerminalDisplay()) {
            display.start().blockingAwait();
            display.write(
                    new TerminalPosition(0, 0),
                    Observable.fromArray('H', 'e', 'l', 'l', 'o', ' ', 'w', 'o', 'r', 'l', 'd', '!').map(TextCharacter::new)
            ).blockingAwait();
            display.refresh().blockingAwait();
            Thread.sleep(Integer.MAX_VALUE);
        }
    }
}
