
package hackathon.com.theseus.drawer.navigation;
import java.util.ArrayList;
import java.util.List;

import hackathon.com.theseus.drawer.fragment.MapFragment;
import hackathon.com.theseus.drawer.fragment.SettingsFragment;
import hackathon.com.theseus.drawer.fragment.ProfileFragment;

import com.google.android.gms.maps.GoogleMap;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.SparseIntArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import hackathon.com.theseus.R;
import hackathon.com.theseus.drawer.utils.Constant;

import hackathon.com.theseus.drawer.adapter.NavigationAdapter;
import hackathon.com.theseus.drawer.utils.Menus;
import hackathon.com.theseus.drawer.utils.Utils;

public class NavigationMain extends ActionBarActivity{

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    private int mLastPosition = 0;
	private ListView mListDrawer;    

	private DrawerLayout mLayoutDrawer;		
	private RelativeLayout mUserDrawer;
	private RelativeLayout mRelativeDrawer;	

	private FragmentManager mFragmentManager;
	private NavigationAdapter mNavigationAdapter;
	private ActionBarDrawerToggleCompat mDrawerToggle;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		getSupportActionBar().setIcon(R.drawable.ic_launcher);
		
		setContentView(R.layout.navigation_main);		
		
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);		
        
        mListDrawer = (ListView) findViewById(R.id.listDrawer);        
		mRelativeDrawer = (RelativeLayout) findViewById(R.id.relativeDrawer);		
		mLayoutDrawer = (DrawerLayout) findViewById(R.id.layoutDrawer);	
		
		mUserDrawer = (RelativeLayout) findViewById(R.id.userDrawer);
		mUserDrawer.setOnClickListener(userOnClick);
		
		if (mListDrawer != null) {

			// All header menus should be informed here
			// listHeader.add(MENU POSITION)			
			List<Integer> mListHeader = new ArrayList<Integer>();
			//mListHeader.add(0);
			//mListHeader.add(6);			
			//mListHeader.add(10);
						
			// All menus which will contain an accountant should be informed here
			// Counter.put ("POSITION MENU", "VALUE COUNTER");			
			SparseIntArray  mCounter = new SparseIntArray();			
			mCounter.put(Constant.MENU_FRIENDS,7);
			mNavigationAdapter = new NavigationAdapter(this, NavigationList.getNavigationAdapter(this, mListHeader, mCounter, null));
		}
		
		mListDrawer.setAdapter(mNavigationAdapter);
		mListDrawer.setOnItemClickListener(new DrawerItemClickListener());

		mDrawerToggle = new ActionBarDrawerToggleCompat(this, mLayoutDrawer);		
		mLayoutDrawer.setDrawerListener(mDrawerToggle);
		
		if (savedInstanceState != null) { 			
			setLastPosition(savedInstanceState.getInt(Constant.LAST_POSITION));
			
			setTitleFragments(mLastPosition);			
			mNavigationAdapter.resetarCheck();		
			mNavigationAdapter.setChecked(mLastPosition, true);							
	    }else{
	    	setLastPosition(mLastPosition); 
	    	setFragmentList(mLastPosition);
	    }

	}

	private void setFragmentList(int posicao){

		Fragment mFragment = null;		
		mFragmentManager = getSupportFragmentManager();
		
		switch (posicao) {
            case Constant.MENU_MAPS:
                mFragment = new MapFragment().newInstance(Utils.getTitleItem(NavigationMain.this, Constant.MENU_MAPS));
                break;
            case Constant.MENU_PROFILE:
                mFragment = new ProfileFragment().newInstance(Utils.getTitleItem(NavigationMain.this, Constant.MENU_PROFILE));
                break;
            case Constant.MENU_SETTINGS:
                mFragment = new SettingsFragment().newInstance(Utils.getTitleItem(NavigationMain.this, Constant.MENU_SETTINGS));
                break;
            case Constant.MENU_INVITE:
			invite();
			break;	
		}
		
		if (mFragment != null){
			setTitleFragments(mLastPosition);
			mNavigationAdapter.resetarCheck();		
			mNavigationAdapter.setChecked(posicao, true);			
			mFragmentManager.beginTransaction().replace(R.id.content_frame, mFragment).commit();
		}
	}

    private void hideMenus(Menu menu, int posicao) {
    	    	
        boolean drawerOpen = mLayoutDrawer.isDrawerOpen(mRelativeDrawer);    	
    	
        switch (posicao) {

		}  
    }	

	private void setTitleFragments(int position){	
		setIconActionBar(Utils.iconNavigation[position]);
		setSubtitleActionBar(Utils.getTitleItem(NavigationMain.this, position));				
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub		
		super.onSaveInstanceState(outState);		
		outState.putInt(Constant.LAST_POSITION, mLastPosition);					
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {  
        		
		switch (item.getItemId()) {		
		case Menus.HOME:
			if (mLayoutDrawer.isDrawerOpen(mRelativeDrawer)) {
				mLayoutDrawer.closeDrawer(mRelativeDrawer);
			} else {
				mLayoutDrawer.openDrawer(mRelativeDrawer);
			}
			return true;			
		default:
			
	        if (mDrawerToggle.onOptionsItemSelected(item)) {
	            return true;
	        }		
			
			return super.onOptionsItemSelected(item);			
		}		             
    }
		
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
    	hideMenus(menu, mLastPosition);
        return super.onPrepareOptionsMenu(menu);  
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);        		
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);	     
	    mDrawerToggle.syncState();	
	 }	
	
	public void setTitleActionBar(CharSequence informacao) {    	
    	getSupportActionBar().setTitle(informacao);
    }	
	
	public void setSubtitleActionBar(CharSequence informacao) {    	
    	getSupportActionBar().setSubtitle(informacao);
    }	

	public void setIconActionBar(int icon) {    	
    	getSupportActionBar().setIcon(icon);
    }	
	
	public void setLastPosition(int posicao){		
		this.mLastPosition = posicao;
	}	
		
	private class ActionBarDrawerToggleCompat extends ActionBarDrawerToggle {

		public ActionBarDrawerToggleCompat(Activity mActivity, DrawerLayout mDrawerLayout){
			super(
			    mActivity,
			    mDrawerLayout, 
  			    R.drawable.ic_action_navigation_drawer, 
				R.string.drawer_open,
				R.string.drawer_close);
		}
		
		@Override
		public void onDrawerClosed(View view) {			
			supportInvalidateOptionsMenu();				
		}

		@Override
		public void onDrawerOpened(View drawerView) {	
			mNavigationAdapter.notifyDataSetChanged();			
			supportInvalidateOptionsMenu();			
		}		
	}
		  
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);		
	}
	
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int posicao, long id) {          	        	
	    	setLastPosition(posicao);        	
	    	setFragmentList(mLastPosition);	    	
	    	mLayoutDrawer.closeDrawer(mRelativeDrawer);	 
        }
    }	
    
	private OnClickListener userOnClick = new OnClickListener() {		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mLayoutDrawer.closeDrawer(mRelativeDrawer);
		}
	};	
	
	  public void invite() {

			Resources resources = getResources();

			Intent emailIntent = new Intent();
			emailIntent.setAction(Intent.ACTION_SEND);

			emailIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(resources.getString(R.string.invite_message)));
			emailIntent.putExtra(Intent.EXTRA_SUBJECT,resources.getString(R.string.subject));
			emailIntent.setType("message/rfc822");

			PackageManager pm = getPackageManager();
			Intent sendIntent = new Intent(Intent.ACTION_SEND);
			sendIntent.setType("text/plain");

			Intent openInChooser = Intent.createChooser(emailIntent,resources.getString(R.string.title_activity_invite));

			List<ResolveInfo> resInfo = pm.queryIntentActivities(sendIntent, 0);
			List<LabeledIntent> intentList = new ArrayList<LabeledIntent>();
			for (int i = 0; i < resInfo.size(); i++) {
				// Extract the label, append it, and repackage it in a LabeledIntent
				ResolveInfo ri = resInfo.get(i);
				String packageName = ri.activityInfo.packageName;
				if (packageName.contains("android.email")) {
					emailIntent.setPackage(packageName);
				} else if (packageName.contains("twitter")
						|| packageName.contains("facebook")
						|| packageName.contains("mms")
						|| packageName.contains("android.gm")) {
					Intent intent = new Intent();
					intent.setComponent(new ComponentName(packageName,ri.activityInfo.name));
					intent.setAction(Intent.ACTION_SEND);
					intent.setType("text/plain");
					if (packageName.contains("twitter")) {
						intent.putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.invite_message));
					} else if (packageName.contains("facebook")) {
						intent.putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.url_invite_message));
					} else if (packageName.contains("mms")) {
						intent.putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.invite_message));
					} else if (packageName.contains("android.gm")) {
						intent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(resources.getString(R.string.invite_message)));
						intent.putExtra(Intent.EXTRA_SUBJECT,resources.getString(R.string.subject));
						intent.setType("message/rfc822");
					}

					intentList.add(new LabeledIntent(intent, packageName, ri.loadLabel(pm), ri.icon));
				}
			}

			// convert intentList to array
			LabeledIntent[] extraIntents = intentList
					.toArray(new LabeledIntent[intentList.size()]);

			openInChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, extraIntents);
			startActivity(openInChooser);
	    }

}
