package com.gilan.nextbusmodern;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fima.cardsui.objects.Card;

public class MyBusTimeCard extends Card 
{
	public MyBusTimeCard(String stopName,String stripeColor, String timeOne,Boolean isClickable) 
	{
		super(stripeColor, stopName, timeOne,isClickable);
	}

	public View getCardContent(Context context) 
	{
		View v = LayoutInflater.from(context).inflate(R.layout.bus_timecard, null);
		
		((TextView) v.findViewById(R.id.stopName)).setText(busLetter);
		((TextView) v.findViewById(R.id.time1)).setText(busDescription);
		((ImageView)v.findViewById(R.id.timeDivider)).setBackgroundColor(Color.parseColor(stripeColor));		
		

		if (isClickable == true)
			((LinearLayout) v.findViewById(R.id.busLayout))
					.setBackgroundResource(R.drawable.selectable_background_cardbank);

		 

		return v;
	}
}