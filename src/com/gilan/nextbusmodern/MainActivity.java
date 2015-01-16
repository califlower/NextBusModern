package com.gilan.nextbusmodern;


import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import android.os.AsyncTask;
import android.os.Bundle;
import com.gilan.nextbusmodern.MyBusPickerCard;
import com.fima.cardsui.objects.Card;
import com.fima.cardsui.views.CardUI;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import com.fima.cardsui.views.*;



public class MainActivity extends Activity 
{
	String[] busList;
	String[] stopNameList;
	String[] stopClickedList;
	String[] timeList;
	String busThatWasClicked="";
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//Sets the Card View
		CardUI mCardView = (CardUI) findViewById(R.id.busPicker);
		
		//Creates the Pick Bus Card
		mCardView.addCard(new MyBusPickerCard("#ffffff","", "|| Please Pick a Bus Route", false));
		
		//Shows the card
		mCardView.refresh();
		
		//Executes AsyncTask
		new GetBusList().execute();
		
	

    }
	
	
	//Creates a Background Network Task. Having Do in background return integer and on pre execute take the integer is 
	//because otherwise onPreExecute does not run
	
	private class GetBusList extends AsyncTask<Void, Void, Integer>  
    {
    	protected Integer doInBackground(Void... params) 
    	{
    		try
    			{
    				//Pulls Xml File From Internet
    				URL url = new URL("http://webservices.nextbus.com/service/publicXMLFeed?a=rutgers&command=routeConfig");
    				InputStream stream = url.openStream();
    				
    				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
    				
    				Document doc = dBuilder.parse(stream);
    				doc.getDocumentElement().normalize();
    				
    				//Gets all items under "route", and stopId(which allows me to find what bus was clicked and what bus to iterate too
    				//Which is all the bus Routes
    				NodeList nList = doc.getElementsByTagName("route");
    				NodeList nListId=doc.getElementsByTagName("stop");
    				
    				//Sets the busList array to the amount of bus routes
    				busList=new String[(nList.getLength())-4];
    				stopNameList=new String[(nListId.getLength())];

    				//Populates the busList with things under tag. Which is the bus names
    				for(int i = 0; i<nList.getLength();i++)
    				{
    					Node nNode = nList.item(i);
    					Node nNodeId=nListId.item(i);
    					Element eElement = (Element) nNode;
    					Element eElementId = (Element) nNodeId;
    					busList[i]=(eElement.getAttribute("tag").toUpperCase());
        				stopNameList[i]=(eElementId.getAttribute("tag"));
    					
    				}
    				
    				return 1;
    			}
    		catch (Exception e)
    			{
    				
    			}
			return 1;
		
    		
    	}


        protected void onPostExecute(Integer result)
        {

        	CardUI mCardView = (CardUI) findViewById(R.id.busPicker);
        	String[] colorArray={"#314046","#af897c","#ac9cdb","#719750","#4b2839","#383838" //an Array of colors for coloring stuff
        						,"#567880","#48836e","#a50b00","#000000","#ffa34c","#302e2f"};
        	String[] descArray={"College Ave - Busch","Livingston - Busch","B Commuter Shuttle",  //An array of descriptions for all the buses
        						"Col. Ave - Doug/Cook","Cook/Douglass - Col. Ave","Col. Ave - Busch",
        						"Col. Ave - Livi","Doug. - Livi","Doug. - Busch","All Campuses"};
        	
        	
			for(int i = 0; i<busList.length;i++) //Adds all the cards to mCardView
			{	
				busThatWasClicked=busList[i];//for identifying the bus that was clicked
				
				MyBusPickerCard toAddClick=new MyBusPickerCard(colorArray[i],busList[i], descArray[i], true);
				mCardView.addCard(toAddClick);
				
				toAddClick.setOnClickListener(new OnClickListener() //sets what to do on each click
				{
					@Override
					public void onClick(View v) 
					{
						//implement execute
						String busThatWasClickeduse=busThatWasClicked;
						//Allows me to have a useable variable to indentify the bus that was clicked

						if(busThatWasClickeduse.equalsIgnoreCase("a"))//Manual if else thingy to get which stopId List
						{
							String[] busIdSegment=Arrays.copyOfRange(stopNameList, 0, 13);
							new GetTimeList().execute(busThatWasClickeduse,busIdSegment);
							CardUI mCardView = (CardUI) findViewById(R.id.busPicker);
							mCardView.clearCards();	
							mCardView.refresh();
							
							
						}
						else if(busThatWasClickeduse.equalsIgnoreCase("b"))
						{
							String[] busIdSegment=Arrays.copyOfRange(stopNameList, 27, 35);
							new GetTimeList().execute(busThatWasClickeduse,busIdSegment);
							CardUI mCardView = (CardUI) findViewById(R.id.busPicker);
							mCardView.clearCards();	
							mCardView.refresh();
							
						}
						else if(busThatWasClickeduse.equalsIgnoreCase("c"))
						{
							String[] busIdSegment=Arrays.copyOfRange(stopNameList, 0, 13);
							new GetTimeList().execute(busThatWasClickeduse,busIdSegment);
							CardUI mCardView = (CardUI) findViewById(R.id.busPicker);
							mCardView.clearCards();	
							mCardView.refresh();
							
						}
						
						


					}
				});
				
				mCardView.refresh();
			}
			
        	return;
        }
    }
	
	
	
	
	
	
	
	//Get list of times and show it after click
	//Pass in object because I have to pass in two different items
	//one item is a String and one is a String Array
	private class GetTimeList extends AsyncTask<Object, Void, Integer>  
    {

		protected Integer doInBackground(Object... params) 
    	{
		    try 
		    {
		    	
		    	String myBusClicked = (String) params[0];
		    	String[] myBusStopName=(String[]) params[1];
		    	timeList=new String[myBusStopName.length];
		    	stopClickedList=myBusStopName.clone();
		    	
		    	
		    	for (int i=0; i<myBusStopName.length; i++)
		    	{
		    		URL url = new URL("http://webservices.nextbus.com/service/publicXMLFeed?a=rutgers&command=predictions&r="+myBusClicked+"&s="+myBusStopName[i]);
		    		InputStream stream = url.openStream();
		    		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		    		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		    		Document doc = dBuilder.parse(stream);
			    	doc.getDocumentElement().normalize();
			    	
			    	NodeList nList = doc.getElementsByTagName("prediction");

			    	
		        	Node nNode = nList.item(0);

		        	
		        	Element eElement = (Element) nNode;

		        	
		        	timeList[i]=(eElement.getAttribute("minutes"));
	
			    	
		 		
		    	}



		    	/*for(int i = 0; i<nList.getLength();i++)
		    		{
		        	Node nNode = nList.item(i);
		        	Element eElement = (Element) nNode;
		        	routeArray.add(eElement.getAttribute("tag"));
		    		}*/

		    	
		    	
		 


		    	

		    } 
		    catch (Exception e) 
		    	{
		    	e.printStackTrace();
		    	}
		    

			return 1;
    	}


        protected void onPostExecute(Integer result)
        {
        	CardUI mCardView2 = (CardUI) findViewById(R.id.busPicker);
        	
        	for(int i = 0; i<stopClickedList.length;i++) //Adds all the cards to mCardView
			{	
				
				MyBusPickerCard time=new MyBusPickerCard("red" ,timeList[i], stopClickedList[i], true);
				mCardView2.addCard(time);

					}
        	mCardView2.refresh();
			
        	
        }
    }
}

