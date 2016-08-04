package de.czyrux.mvploadersample.base;

/**
 * todo 5 PresenterFactory
 * Creates a Presenter object.
 * @param <T> presenter type
 */

public interface PresenterFactory<T extends Presenter> {
    T create();
}
