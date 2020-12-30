package io.github.proton.api;

import java.util.Collection;

public interface Highlighter {
    final class Highlight {
        public final String type;
        public final int start;
        public final int end;

        public Highlight(String type, int start, int end) {
            this.type = type;
            this.start = start;
            this.end = end;
        }

        @Override
        public String toString() {
            return type + "(" + start + "," + end + ")";
        }
    }

    Collection<Highlight> highlights(CharSequence input);
}
