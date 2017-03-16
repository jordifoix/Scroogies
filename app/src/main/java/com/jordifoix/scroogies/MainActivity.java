package com.jordifoix.scroogies;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String[] CONTENT = new String[] { "History", "Add", "Settings" };
    private static final int[] ICONS = new int[] {
            R.drawable.ic_library_books_white_24px,
            R.drawable.ic_add_white_24px,
            R.drawable.ic_settings_white_24px
    };

    Cursor cursor1;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        //mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager)findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        View view = findViewById(android.R.id.content);
        hideKeyboard(view);


    }



    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    HistoryFragment tab1 = new HistoryFragment();
                    return tab1;
                case 1:
                    AddFragment tab2 = new AddFragment();
                    return tab2;
                case 2:
                    ConfigFragment tab3 = new ConfigFragment();
                    return tab3;
                default:
                    return null;
            }
        }


        @Override
        public CharSequence getPageTitle(int position) {
            Drawable image = ContextCompat.getDrawable(MainActivity.this, ICONS[position]);
            image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
            image.setTint(Color.WHITE); // API 21
            SpannableString sb = new SpannableString(" ");
            ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
            sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return sb;
        }

        public int getPageIconResId(int position) {
            return ICONS[position];
        }


        @Override
        public int getCount() {
            return CONTENT.length;
        }

    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /*public String getContactNamebyPhoneNumber(Integer number) {
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null);
        if (cur.getCount()>0) {
            while (cur.moveToNext()) {

                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            }
        }
    }*/

}
