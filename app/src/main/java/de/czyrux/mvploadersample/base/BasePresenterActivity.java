package de.czyrux.mvploadersample.base;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import hugo.weaving.DebugLog;

public abstract class BasePresenterActivity<P extends Presenter<V>, V> extends AppCompatActivity {

    private static final String TAG = "base-activity";
    private static final int LOADER_ID = 101;
    private Presenter<V> presenter;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * todo 6 onCreate#BasePresenterActivity initLoader()
         * When calling initLoader we pass a unique id (unique within that Activity/Fragment — not globally unique) to identify the Loader,
         * an optional Bundle — which in this case won’t be need,
         * and an instance of LoaderCallbacks.
         *
         * This call to initLoader will hook ourselves in the component lifecycle,
         * receiving a call onStartLoading() when onStart() method is call and onStopLoading() when onStop().
         *
         * todo 6b  destroyLoader()
         * nếu gọi hàm này thì bên loader onReset() dc gọi
         */
        // LoaderCallbacks as an object, so no hint regarding Loader will be leak to the subclasses.

        /**
         * todo 7 LoaderManager.LoaderCallbacks
         * But how do I get my Presenter? LoaderCallbacks
         *
         * LoaderCallbacks is the communication point between Activity/Fragment and the Loader. There are three callbacks:
         * todo 7b onCreateLoader() — where you construct the actual Loader instance.
         * todo 7c onLoadFinished() — where Loader will deliver its work, the Presenter in our case.
         * todo 7d onLoaderReset() — your chance to clean up any references to the data.
         */
        getSupportLoaderManager().initLoader(LOADER_ID, null, new LoaderManager.LoaderCallbacks<P>() {
            // khi băt đầu thì trong hàm này tạo presenter bằng constructor
            @Override
            @DebugLog
            public final Loader<P> onCreateLoader(int id, Bundle args) {
                Log.i(TAG, "onCreateLoader");
                return new PresenterLoader<>(BasePresenterActivity.this, getPresenterFactory(), tag());
            }

            // khi finish load presenter thì gắn presenter vào
            @Override
            @DebugLog
            public final void onLoadFinished(Loader<P> loader, P presenter) {
                Log.i(TAG, "onLoadFinished");
                BasePresenterActivity.this.presenter = presenter;
                onPresenterPrepared(presenter);
            }

            // khi loader hoàn thành nhiệm vụ thì gán presenter bằng null
            @Override
            @DebugLog
            public final void onLoaderReset(Loader<P> loader) {
                Log.i(TAG, "onLoaderReset");
                BasePresenterActivity.this.presenter = null;
                onPresenterDestroyed();
            }
        });
    }

    /**
     * todo 9a activity thì onStart gắn view vào
     *
     */
    @Override
    @DebugLog
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart-" + tag());
        presenter.onViewAttached(getPresenterView());
    }

    /**
     * todo 9b
     */
    @Override
    @DebugLog
    protected void onStop() {
        presenter.onViewDetached();
        super.onStop();
        Log.i(TAG, "onStop-" + tag());
    }

    protected abstract String tag();

    protected abstract PresenterFactory<P> getPresenterFactory();

    protected abstract void onPresenterPrepared(P presenter);

    protected void onPresenterDestroyed() {
        // hook for subclasses
    }

    // Override in case of Activity not implementing Presenter<View> interface
    protected V getPresenterView() {
        return (V) this;
    }
}
