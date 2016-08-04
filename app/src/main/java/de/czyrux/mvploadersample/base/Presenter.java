package de.czyrux.mvploadersample.base;

/**
 * todo 8 interface for attaching view to presenter
 * @param <V> view
 */
public interface Presenter <V>{
    void onViewAttached(V view);
    void onViewDetached();
    void onDestroyed();
}
