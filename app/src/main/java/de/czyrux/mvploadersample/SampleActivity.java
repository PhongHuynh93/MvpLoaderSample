package de.czyrux.mvploadersample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import de.czyrux.mvploadersample.base.BasePresenterActivity;
import de.czyrux.mvploadersample.base.PresenterFactory;
import de.czyrux.mvploadersample.presenter.SamplePresenter;
import de.czyrux.mvploadersample.presenter.SamplePresenterFactory;
import de.czyrux.mvploadersample.presenter.SampleView;
import hugo.weaving.DebugLog;

/**
 * we need to connects our activities and fragments.
 * The connection point will be the LoaderManager.
 * We will call FragmentActivity’s getSupportLoaderManager() or Fragment’s getLoaderManager() to get our instance and then call initLoader().
 * Google recommends calling this method in Activity#onCreate() or Fragment#onActivityCreated().
 */
public class SampleActivity extends BasePresenterActivity<SamplePresenter, SampleView> implements SampleView {
    private static final String TAG = "SampleActivity";
    private static final int NUMBER_OF_PAGES = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mvp);
        Log.e(TAG, "onCreate-" + tag());

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, SampleFragment.newInstance("fragment", R.color.yellow))
                    .commit();
        }
    }

    @DebugLog
    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "onStart-" + tag());
    }

    @Override
    protected String tag() {
        return "activity";
    }

    @Override
    protected PresenterFactory<SamplePresenter> getPresenterFactory() {
        return new SamplePresenterFactory(tag());
    }

    @Override
    protected void onPresenterPrepared(SamplePresenter presenter) {
        // Nothing right now
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.menu_single) {
            replaceFragment(SampleFragment.newInstance("fragment", R.color.green));
            return true;
        } else if (id == R.id.menu_pager) {
            replaceFragment(SampleViewPagerFragment.newInstance(NUMBER_OF_PAGES));
            return true;
        } else if (id == R.id.menu_activity) {
            startActivity(new Intent(this, SampleActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(null)
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    @Override
    public void showMessage(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
