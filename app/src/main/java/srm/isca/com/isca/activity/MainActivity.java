package srm.isca.com.isca.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import srm.isca.com.isca.R;
import srm.isca.com.isca.helper.PrefManager;


public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private PrefManager pref;
    private TextView name,password, email, mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
}
