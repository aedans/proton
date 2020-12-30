package io.github.proton.api.util;

import io.github.proton.api.Change;
import io.github.proton.api.map.PositionMap;

import java.util.*;

public final class Diff {
    public static List<Change> diff(PositionMap positions, String oldString, String newString) {
        int oldIndex = 0, newIndex = 0;
        List<Change> changes = new ArrayList<>();

        while (true) {
            if (oldIndex == oldString.length() && newIndex == newString.length()) {
                return changes;
            } else if (oldIndex == oldString.length()) {
                changes.add(0, new Change(positions.get(oldIndex), positions.get(oldIndex), newString.substring(newIndex)));
                return changes;
            } else if (newIndex == newString.length()) {
                changes.add(0, new Change(positions.get(oldIndex), positions.get(oldString.length()), ""));
                return changes;
            } else if (oldString.charAt(oldIndex) == newString.charAt(newIndex)) {
                oldIndex++;
                newIndex++;
            } else {
                int index = newIndex;
                while (oldString.charAt(oldIndex) != newString.charAt(newIndex)) {
                    newIndex++;
                    if (newIndex == newString.length()) {
                        break;
                    }
                }
                changes.add(0, new Change(positions.get(oldIndex), positions.get(oldIndex), newString.substring(index, newIndex)));
            }
        }
    }
}
