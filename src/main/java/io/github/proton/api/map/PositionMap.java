package io.github.proton.api.map;

import io.github.proton.api.Position;

import java.util.List;
import java.util.stream.Collectors;

public final class PositionMap {
    private final Position[] positions;
    private final int[] indices;

    public PositionMap(CharSequence chars) {
        positions = new Position[chars.length() + 1];

        if (chars.length() == 0) {
            positions[0] = new Position(0, 0, 0);
            indices = new int[]{0};
            return;
        }

        List<String> lines = chars.toString().lines().collect(Collectors.toList());
        indices = new int[lines.size()];

        int i = 0, l = 0;
        for (String line : lines) {
            indices[l] = i;
            for (int c = 0; c <= line.length(); c++) {
                positions[i] = new Position(i, l, c);
                i++;
            }
            l++;
        }
    }

    public Position get(int index) {
        return positions[index];
    }

    public Position get(org.eclipse.lsp4j.Position position) {
        return get(indices[position.getLine()] + position.getCharacter());
    }
}
