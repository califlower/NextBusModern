package com.gilan.nextbusmodern;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fima.cardsui.objects.Card;

public class MyBusPickerCard extends Card 
{
	public MyBusPickerCard(String busLetter,String stripeColor, String busDescription,Boolean isClickable) 
	{
		super(stripeColor, busLetter, busDescription,isClickable);
	}

	public View getCardContent(Context context) 
	{
		View v = LayoutInflater.from(context).inflate(R.layout.bus_listcard, null);
		
		((TextView) v.findViewById(R.id.busLetter)).setText(busLetter);
		((TextView) v.findViewById(R.id.busDescription)).setText(busDescription);
		((ImageView)v.findViewById(R.id.rectDivider)).setBackgroundColor(Color.parseColor(stripeColor));		
		

		if (isClickable == true)
			((LinearLayout) v.findViewById(R.id.busLayout))
					.setBackgroundResource(R.drawable.selectable_background_cardbank);

		 

		return v;
	}
}
