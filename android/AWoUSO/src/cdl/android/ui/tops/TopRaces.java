package cdl.android.ui.tops;

import java.util.ArrayList;

import org.json.JSONArray;

import cdl.android.R;
import cdl.android.general.ServerResponse;
import cdl.android.server.ApiHandler;
import android.support.v4.app.*;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

public class TopRaces extends Fragment{
	ArrayList<TopRacesItem> raceItems;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
			
		View view =  inflater.inflate(R.layout.top_races, container, false);
		
		ListView mListView = (ListView) view.findViewById(R.id.top_races_list);
		mListView.setEmptyView(view.findViewById(android.R.id.empty));
		
		
		
		raceItems = new ArrayList<TopRacesItem>();
		
		ServerResponse resp = ApiHandler.getArray(ApiHandler.topRaceURL, view.getContext());
		
		
		if(resp.getStatus() == false){
		
			Toast.makeText(view.getContext(), resp.getError() , Toast.LENGTH_SHORT).show();
		
		}else{
			try {
				JSONArray topRacesData = (JSONArray) resp.getArrayData();
				
				
				for(int i = 0; i < topRacesData.length(); i++){
					TopRacesItem raceItem = new TopRacesItem();
					
					try{
						raceItem.parseContent(topRacesData.getJSONObject(i));
						raceItem.setPlace(i + 1);
						raceItems.add(raceItem);
						
					}catch(Exception e){
						Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
					}
				}
			}catch(Exception  e){
				e.printStackTrace();
			}
		}
	
		
		mListView.setAdapter(new TopRacesAdapter(this.getActivity().getApplicationContext(), raceItems));
		
		
		return view;
	}
}