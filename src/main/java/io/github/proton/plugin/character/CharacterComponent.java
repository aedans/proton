package io.github.proton.plugin.character;

import io.github.proton.display.Component;

public interface CharacterComponent extends Component {
    char getCharacter();

    CharacterComponent setCharacter(char character);
}
