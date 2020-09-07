package io.github.proton.editor;

public interface Tree<This> {
    boolean isEmpty();

    Projection<This> project();
}
