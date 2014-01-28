package com.prince.readingcalllogs;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MainActivity extends ListActivity {

	private ArrayList<String> conNames;
	private ArrayList<String> conNumbers;
	private ArrayList<String> conTime;
	private ArrayList<String> conDate;
	private ArrayList<String> conType;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		conNames = new ArrayList<String>();
		conNumbers = new ArrayList<String>();
		conTime = new ArrayList<String>();
		conDate = new ArrayList<String>();
		conType = new ArrayList<String>();

		Cursor curLog = CallLogHelper.getAllCallLogs(getContentResolver());

		setCallLogs(curLog);

		setListAdapter(new MyAdapter(this, android.R.layout.simple_list_item_1,
				R.id.tvNameMain, conNames));
	}

	private class MyAdapter extends ArrayAdapter<String> {

		public MyAdapter(Context context, int resource, int textViewResourceId,
				ArrayList<String> conNames) {
			super(context, resource, textViewResourceId, conNames);

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			View row = setList(position, parent);
			return row;
		}

		private View setList(int position, ViewGroup parent) {
			LayoutInflater inf = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View row = inf.inflate(R.layout.liststyle, parent, false);

			TextView tvName = (TextView) row.findViewById(R.id.tvNameMain);
			TextView tvNumber = (TextView) row.findViewById(R.id.tvNumberMain);
			TextView tvTime = (TextView) row.findViewById(R.id.tvTime);
			TextView tvDate = (TextView) row.findViewById(R.id.tvDate);
			TextView tvType = (TextView) row.findViewById(R.id.tvType);

			tvName.setText(conNames.get(position));
			tvNumber.setText(conNumbers.get(position));
			tvTime.setText("( " + conTime.get(position) + "sec )");
			tvDate.setText(conDate.get(position));
			tvType.setText("( " + conType.get(position) + " )");

			return row;
		}
	}

	private void setCallLogs(Cursor curLog) {
		while (curLog.moveToNext()) {
			String callNumber = curLog.getString(curLog
					.getColumnIndex(android.provider.CallLog.Calls.NUMBER));
			conNumbers.add(callNumber);

			String callName = curLog
					.getString(curLog
							.getColumnIndex(android.provider.CallLog.Calls.CACHED_NAME));
			if (callName == null) {
				conNames.add("Unknown");
			} else
				conNames.add(callName);

			String callDate = curLog.getString(curLog
					.getColumnIndex(android.provider.CallLog.Calls.DATE));
			SimpleDateFormat formatter = new SimpleDateFormat(
					"dd-MMM-yyyy HH:mm");
			String dateString = formatter.format(new Date(Long
					.parseLong(callDate)));
			conDate.add(dateString);

			String callType = curLog.getString(curLog
					.getColumnIndex(android.provider.CallLog.Calls.TYPE));
			if (callType.equals("1")) {
				conType.add("Incoming");
			} else
				conType.add("Outgoing");

			String duration = curLog.getString(curLog
					.getColumnIndex(android.provider.CallLog.Calls.DURATION));
			conTime.add(duration);

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater imf = getMenuInflater();
		imf.inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.item1) {
			Intent intent = new Intent(MainActivity.this, InsertCallLog.class);
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}

}
