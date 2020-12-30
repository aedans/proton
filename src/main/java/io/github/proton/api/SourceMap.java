package io.github.proton.api;

import java.util.*;

public class SourceMap {
    private final StringBuilder string = new StringBuilder();
    private final List<Integer> map = new ArrayList<>();
    private int position;

    private SourceMap append(char c) {
        string.append(c);
        map.add(position);
        return this;
    }

    public SourceMap append(String s) {
        for (int i = 0; i < s.length(); i++) {
            append(s.charAt(i));
        }
        return this;
    }

    public SourceMap append(String s, int start, int end) {
        if (start > position) {
            position = start;
        }
        float ratio = ((float) (end - start)) / s.length();
        for (int i = 0; i < s.length(); i++) {
            append(s.charAt(i));
            position = (int) (start + (i * ratio));
        }
        position = end;
        return this;
    }

    public int src(int index) {
        if (index == map.size()) {
            return map.get(map.size() - 1) + 1;
        }
        return map.get(index);
    }

    public int dest(int index) {
        return (int) map.stream().takeWhile(x -> x < index).count();
    }

    @Override
    public String toString() {
        return string.toString();
    }
}
