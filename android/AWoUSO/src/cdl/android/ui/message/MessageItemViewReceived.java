package cdl.android.ui.message;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import cdl.android.R;

/**
 * 
 * Message List Item View
 */
public class MessageItemViewReceived extends LinearLayout {
	MessageItem mItem;
	Context mContext;

	public MessageItemViewReceived(Context context, MessageItem item) {
		super(context);
		mContext = context;
		mItem = item;

		LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layoutInflater.inflate(R.layout.simple_message_list, this, true);

		TextView subject = (TextView) findViewById(R.id.subject);
		subject.setText(item.getSubject());

		TextView from = (TextView) findViewById(R.id.from);
		from.setText(item.getAuthor());

		TextView text = (TextView) findViewById(R.id.text);
		text.setText(item.getContent());
		
		TextView new_message = (TextView) findViewById(R.id.newmessage);
		if (!item.getRead().contains("false")){
			new_message.setVisibility(4);
		}
	}
}