package cdl.android.ui.map;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import cdl.android.ui.user.OtherProfile;

public class MemberAdapter extends BaseAdapter{
	ArrayList<MemberItem> mItems;
	Context mContext;
	final Intent otherProfile;
	
	public MemberAdapter(Context context, ArrayList<MemberItem> items) {
		mItems = new ArrayList<MemberItem>();
		mContext = context;
		mItems = items;
		otherProfile = new Intent(mContext, OtherProfile.class);
	}
	
	public int getCount() {
		return mItems.size();
	}

	public Object getItem(int index) {
		return mItems.get(index);
	}

	public long getItemId(int index) {
		return index;
	}
	
	public View getView(int index, View convertView, ViewGroup parent) {
		MemberItemView item;

		final String id = mItems.get(index).getId();

		item = new MemberItemView(mContext, mItems.get(index));
		item.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Bundle data = new Bundle();
				data.putString("id", id);
				otherProfile.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				otherProfile.putExtras(data);
				mContext.startActivity(otherProfile);
			}
		});
		item.setClickable(true);
		
		return item;
	}

}
