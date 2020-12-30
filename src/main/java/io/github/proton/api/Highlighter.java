package io.github.proton.api;

import java.util.Collection;

public interface Highlighter {
    Collection<Highlight> highlights(CharSequence input);
}
