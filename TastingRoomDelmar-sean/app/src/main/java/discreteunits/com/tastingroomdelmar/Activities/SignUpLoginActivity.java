package discreteunits.com.tastingroomdelmar.Activities;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import discreteunits.com.tastingroomdelmar.R;
import discreteunits.com.tastingroomdelmar.utils.Constants;
import discreteunits.com.tastingroomdelmar.utils.FontManager;

public class SignUpLoginActivity extends AppCompatActivity {
    private static final String TAG = SignUpLoginActivity.class.getSimpleName();

    //TODO remove drawer. it's only for demo purpose

    TextView mTVPreviousActivityName;
    TextView mTVCurrentActivityName;
    TextView mTVQuestion;
    TextView mTVEmail;
    TextView mTVPassword;

    Button mButtonSignupLogin;

    EditText mEditTextEmail;
    EditText mEditTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_login);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayShowTitleEnabled(false);

        final ImageView mIVUp = (ImageView) findViewById(R.id.up_button);
        mIVUp.setVisibility(View.VISIBLE);
        mIVUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        final ImageButton mImageButtonDrawer = (ImageButton) findViewById(R.id.nav_button);
        mImageButtonDrawer.setVisibility(View.GONE);

        final ImageButton mImageButtonTab = (ImageButton) findViewById(R.id.current_order);
        mImageButtonTab.setVisibility(View.GONE);


        if (FontManager.getSingleton() == null) new FontManager(getApplicationContext());

        mTVPreviousActivityName = (TextView) toolbar.findViewById(R.id.tv_prev_activity);
        mTVPreviousActivityName.setTypeface(FontManager.nexa);

        mTVCurrentActivityName = (TextView) toolbar.findViewById(R.id.tv_curr_activity);
        mTVCurrentActivityName.setTypeface(FontManager.nexa);

        mTVQuestion = (TextView) findViewById(R.id.tv_signup_login_question);
        mTVQuestion.setTypeface(FontManager.nexa);

        mButtonSignupLogin = (Button) findViewById(R.id.button_signup_login);
        mButtonSignupLogin.setTypeface(FontManager.nexa);

        mTVEmail = (TextView) findViewById(R.id.tv_email);
        mTVEmail.setTypeface(FontManager.bebasReg);
        mEditTextEmail = (EditText) findViewById(R.id.et_email);
        mEditTextEmail.setTypeface(FontManager.bebasReg);

        mTVPassword = (TextView) findViewById(R.id.tv_password);
        mTVPassword.setTypeface(FontManager.bebasReg);

        mEditTextPassword = (EditText) findViewById(R.id.et_password);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int flagSignupOrLogin = extras.getInt("LOGIN_OR_SIGNUP");

            if (flagSignupOrLogin == Constants.SIGNUP_FLAG) {
                mTVCurrentActivityName.setText(getResources().getString(R.string.signup));
                mTVQuestion.setText(getResources().getString(R.string.question_signup));
                mButtonSignupLogin.setText(getResources().getString(R.string.signup));
                mButtonSignupLogin.setBackgroundColor(ContextCompat.getColor(this, R.color.confirmGreen));
                mButtonSignupLogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        createNewUser();
                    }
                });
            }
            else {
                mTVCurrentActivityName.setText(getResources().getString(R.string.login));
                mTVQuestion.setText(getResources().getString(R.string.question_login));
                mButtonSignupLogin.setText(getResources().getString(R.string.login));
                mButtonSignupLogin.setBackgroundColor(ContextCompat.getColor(this, R.color.grayText));
                mButtonSignupLogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        loginUser();
                    }
                });
            }
        }

        mTVPreviousActivityName.setVisibility(View.GONE);
    }

    private void createNewUser() {
        final String email = mEditTextEmail.getText().toString();

        final String password = mEditTextPassword.getText().toString();

        final ParseUser user = new ParseUser();
        user.setUsername(email);
        user.setEmail(email);
        user.setPassword(password);
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(getApplicationContext(), "Thanks! Signing in now", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignUpLoginActivity.this, Tier1Activity.class));
                } else {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Please complete the sign up form", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loginUser() {
        final String email = mEditTextEmail.getText().toString();

        final String password = mEditTextPassword.getText().toString();

        ParseUser.logInInBackground(email, password, new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    Toast.makeText(getApplicationContext(), "Thanks! Logging in now", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignUpLoginActivity.this, Tier1Activity.class));
                } else {
                    Toast.makeText(getApplicationContext(), "Username or password is wrong", Toast.LENGTH_SHORT).show();
                }

                if (e != null) {
                    e.printStackTrace();
                }
            }
        });
    }
}
