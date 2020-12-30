package io.github.proton.api.highlight;

import java.util.Collection;

public interface Highlighter {
    Collection<Highlight> highlights(CharSequence input);
}
