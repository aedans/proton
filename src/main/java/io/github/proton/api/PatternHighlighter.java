package io.github.proton.api;

import java.util.*;
import java.util.regex.*;

public final class PatternHighlighter implements Highlighter {
    private final Set<String> set;
    private final Pattern pattern;

    public PatternHighlighter(Map<String, String> patterns) {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> entry : patterns.entrySet()) {
            builder.append("(?<").append(entry.getKey()).append(">").append(entry.getValue()).append(")").append("|");
        }
        pattern = Pattern.compile(builder.toString());
        set = patterns.keySet();
    }

    @Override
    public Collection<Highlight> highlights(CharSequence input) {
        Matcher matcher = pattern.matcher(input);
        Collection<Highlight> highlights = new ArrayList<>();
        while (matcher.find()) {
            for (String token : set) {
                if (matcher.group(token) != null) {
                    highlights.add(new Highlight(token, matcher.start(token), matcher.end(token)));
                }
            }
        }
        return highlights;
    }
}
