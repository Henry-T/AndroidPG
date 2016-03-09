package digest.android.lolofinil.com.digest;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

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

        String a = "92d0b2f7465e3a3f2006d3897c234729769bc59f54778b12c67069441e2983d377842fd0bfce3028deb3af8e8c8ace01c8660df62ece657f2dc01b8cfcd3e45a2476adeda0f11eb833c835f2a422d43df0d3fe7f6e143360cf2e032b975090e4db688927d0127de13d9c18af004d56cc4511b944ac8ff4a54d1c96cf67fc3792";
        byte[] b = hexStringToBytes(a);
        Log.i("TTT", hexStringToBytes(a).toString());
       //  String v = hexStringToBytes(a);
        printByteArray(hexStringToBytes(a));
        Log.i("TTTTT", byteArrayToHex(hexStringToBytes(a)));

        byte by = (byte)0xB0;
        Log.i("int<-byte", String.valueOf((int)by));  // -80
        Log.i("int<-char<-byte", String.valueOf((int)(by & 0xFF)));  // 176
    }
    public static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    public static void printByteArray(byte[] byteAry) {
        for(int i=0; i<byteAry.length; i++) {
            int c = (int) (byteAry[i] & 0xFF);
            Log.i("TTTT", String.valueOf(c));
        }
    }

//    public printByteArray2(byte[] byteAry) {
//        String str = new String(byteAry, "UTF-8");
//        Log.i("TTTT", str);
//    }

    public static void printByteArray3(byte[] byteAry) {
        for(int i=0; i<byteAry.length; i++) {
            int c = (int) (byteAry[i] & 0xFF);
            Log.i("TTTT", String.valueOf(c));
        }
    }

    public static String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder(a.length * 2);
        for(byte b: a)
            sb.append(String.format("%02x", b & 0xff));
        return sb.toString();
    }

    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
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
