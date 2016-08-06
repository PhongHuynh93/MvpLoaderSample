package de.czyrux.mvploadersample.base;

import android.content.Context;
import android.support.v4.content.Loader;
import android.util.Log;

import hugo.weaving.DebugLog;

/**
 * https://medium.com/@czyrux/presenter-surviving-orientation-changes-with-loaders-6da6d86ffbbf#.ywgzlo1p7
 * @param <T>
 *     A class that performs asynchronous loading of data.
 */
public final class PresenterLoader<T extends Presenter> extends Loader<T> {

    /**
     *  this interface will hide the details about how to create a Presenter when is required.
     */
    private final PresenterFactory<T> factory;
    private T presenter;
    private final String tag;

    @DebugLog
    public PresenterLoader(Context context, PresenterFactory<T> factory, String tag) {
        super(context);
        this.factory = factory;
        this.tag = tag;
    }

    /**
     *  Will be called by the Framework for a new or already created Loader once Activity onStart() is reached.
     *  In here we check whether we hold a Presenter instance (— in which situation it will be delivered immediately) or the Presenter needs to be created.
     * todo 1 onStartLoading
     */
    @DebugLog
    @Override
    protected void onStartLoading() {
        Log.i("loader", "onStartLoading-" + tag);

        // if we already own a presenter instance, simply deliver it.
        if (presenter != null) {
            deliverResult(presenter);
            return;
        }

        // Otherwise, force a load
        forceLoad();
    }

    /**
     * todo 2 onForceLoad
     * Called when forceLoad() is invoked
     * Here we are calling the Factory to create our Presenter and delivering the result.
     */
    @DebugLog
    @Override
    protected void onForceLoad() {
        Log.i("loader", "onForceLoad-" + tag);

        // Create the Presenter using the Factory
        presenter = factory.create();

        // Deliver the result
        deliverResult(presenter);
    }

    /**
     * todo 3 deliverResult
     * will deliver our Presenter to the Activity/Fragment.
     * @param data
     */
    @DebugLog
    @Override
    public void deliverResult(T data) {
        super.deliverResult(data);
        Log.i("loader", "deliverResult-" + tag);
    }

    /**
     * todo 1b onStopLoading
     * called when onStop()
     */
    @DebugLog
    @Override
    protected void onStopLoading() {
        Log.i("loader", "onStopLoading-" + tag);
    }

    /**
     * todo 4 onReset
     * called when onDestroy()
     * will be call before the Loader gets destroyed,
     * giving us the chance to communicate this to the Presenter in case some ongoing operation could be cancelled or additional clean ups would be required.
     */
    @DebugLog
    @Override
    protected void onReset() {
        Log.i("loader", "onReset-" + tag);
        if (presenter != null) {
            presenter.onDestroyed();
            presenter = null;
        }
    }
}
