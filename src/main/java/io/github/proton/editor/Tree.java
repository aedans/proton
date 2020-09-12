package io.github.proton.editor;

public interface Tree<This extends Tree<This>> {
    boolean isEmpty();

    Projection<This> project();
}
