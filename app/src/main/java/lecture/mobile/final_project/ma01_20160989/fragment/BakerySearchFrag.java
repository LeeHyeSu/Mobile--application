package lecture.mobile.final_project.ma01_20160989.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import lecture.mobile.final_project.ma01_20160989.ViewDetailsActivity;
import lecture.mobile.final_project.ma01_20160989.model.BakeryDto;
import lecture.mobile.final_project.ma01_20160989.parser.BakeryXmlParser;
import lecture.mobile.final_project.ma01_20160989.adapter.MyBakeryAdapter;
import lecture.mobile.final_project.ma01_20160989.R;

public class BakerySearchFrag extends Fragment {

    public static final String TAG = "BakerySearchFrag";

    EditText etTarget;
    ListView lvBakeries;
    Button button;
    String apiAddress;
    String query;

    MyBakeryAdapter adapter;
    ArrayList<BakeryDto> resultList;
    BakeryXmlParser parser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, null);
        etTarget = (EditText)view.findViewById(R.id.etTarget);
        lvBakeries = (ListView)view.findViewById(R.id.lvBakeries);
        button = (Button)view.findViewById(R.id.btnSearch);

        resultList = new ArrayList<BakeryDto>();
        adapter = new MyBakeryAdapter(this, R.layout.listview_bakery_layout, resultList, inflater);
        lvBakeries.setAdapter(adapter);

        apiAddress = getResources().getString(R.string.api_url);
        parser = new BakeryXmlParser();

        lvBakeries.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BakeryDto item = resultList.get(position);

                Intent intent = new Intent(getActivity(), ViewDetailsActivity.class);
                intent.putExtra("bakeryDto", (Serializable) item);

                startActivity(intent);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(v.getId()) {
                    case R.id.btnSearch:
                        query = etTarget.getText().toString() + " 베이커리";
                        new BakeryAsyncTask().execute();
                        break;
                }
            }
        });
        return view;
    }

    class BakeryAsyncTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            StringBuffer response = new StringBuffer();

            // 클라이언트 아이디 및 시크릿 그리고 요청 URL 선언
            String clientId = getResources().getString(R.string.client_id);
            String clientSecret = getResources().getString(R.string.client_secret);

            try {
                String apiURL = apiAddress + URLEncoder.encode(query, "UTF-8");
                URL url = new URL(apiURL);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("X-Naver-Client-Id", clientId);
                con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
                // response 수신
                int responseCode = con.getResponseCode();
                if (responseCode == 200) {
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(con.getInputStream()));
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                } else {
                    Log.e(TAG, "API 호출 에러 발생 : 에러코드=" + responseCode);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response.toString();
        }


        @Override
        protected void onPostExecute(String result) {
            Log.i(TAG, result);

            resultList = parser.parse(result);
            adapter.setList(resultList);
            adapter.notifyDataSetChanged();
        }
    }

}
