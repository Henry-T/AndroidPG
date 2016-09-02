package metadataloadtest.android.lolofinil.com.metadataloadtest;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    public static String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        try {
            ApplicationInfo appInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            Bundle AppInfoBundle = appInfo.metaData;
            Log.i(TAG, "=== get() ===");
            Log.i(TAG, "get(\"BooleanT\"): " + AppInfoBundle.get("BooleanT"));
            Log.i(TAG, "get(\"Int\"): " + AppInfoBundle.get("Int"));
            Log.i(TAG, "get(\"Long\"): " + AppInfoBundle.get("Long"));
            Log.i(TAG, "get(\"ShortFloat\"): " + AppInfoBundle.get("ShortFloat"));
            Log.i(TAG, "get(\"LongFloat\"): " + AppInfoBundle.get("LongFloat"));
            Log.i(TAG, "get(\"NumberInString\"): " + AppInfoBundle.get("NumberInString"));
            Log.i(TAG, "=== getInt() ===");
            Log.i(TAG, "getInt(\"Int\"): " + AppInfoBundle.getInt("Int"));
            Log.i(TAG, "getInt(\"Long\"): " + AppInfoBundle.getInt("Long"));
            Log.i(TAG, "getInt(\"NumberInString\"): " + AppInfoBundle.getInt("NumberInString"));
            Log.i(TAG, "=== getLong() ===");
            Log.i(TAG, "getLong(\"Int\"): " + AppInfoBundle.getLong("Int"));
            Log.i(TAG, "getLong(\"Long\"): " + AppInfoBundle.getLong("Long"));
            Log.i(TAG, "getLong(\"NumberInString\"): " + AppInfoBundle.getLong("NumberInString"));
            Log.i(TAG, "=== getFloat() ===");
            Log.i(TAG, "get(\"ShortFloat\"): " + AppInfoBundle.getFloat("ShortFloat"));
            Log.i(TAG, "get(\"LongFloat\"): " + AppInfoBundle.getFloat("LongFloat"));
            Log.i(TAG, "get(\"NumberInString\"): " + AppInfoBundle.getFloat("NumberInString"));
            Log.i(TAG, "=== getDouble() ===");
            Log.i(TAG, "getDouble(\"ShortFloat\"): " + AppInfoBundle.getDouble("ShortFloat"));
            Log.i(TAG, "getDouble(\"LongFloat\"): " + AppInfoBundle.getDouble("LongFloat"));
            Log.i(TAG, "getDouble(\"LongLongFloat\"): " + AppInfoBundle.getDouble("LongLongFloat"));
            Log.i(TAG, "getDouble(\"NumberInString\"): " + AppInfoBundle.getDouble("NumberInString"));
            Log.i(TAG, "=== getString() ===");
            Log.i(TAG, "getString(\"BooleanT\"): " + AppInfoBundle.getString("BooleanT"));
            Log.i(TAG, "getString(\"Int\"): " + AppInfoBundle.getString("Int"));
            Log.i(TAG, "getString(\"Long\"): " + AppInfoBundle.getString("Long"));
            Log.i(TAG, "getString(\"ShortFloat\"): " + AppInfoBundle.getString("ShortFloat"));
            Log.i(TAG, "getString(\"LongFloat\"): " + AppInfoBundle.getString("LongFloat"));
            Log.i(TAG, "getString(\"NumberInString\"): " + AppInfoBundle.getString("NumberInString"));
            Log.i(TAG, "=== getBoolean() ===");
            Log.i(TAG, "getBoolean(\"BooleanT\"): " + AppInfoBundle.getBoolean("BooleanT"));
            

        }catch(PackageManager.NameNotFoundException ex) {
            Log.i(TAG, ex.toString());
            ex.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
