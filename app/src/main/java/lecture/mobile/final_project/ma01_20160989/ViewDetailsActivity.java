package lecture.mobile.final_project.ma01_20160989;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kakao.kakaolink.KakaoLink;
import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import lecture.mobile.final_project.ma01_20160989.adapter.MyBlogAdapter;
import lecture.mobile.final_project.ma01_20160989.helper.BakeryDBHelper;
import lecture.mobile.final_project.ma01_20160989.model.BakeryDto;
import lecture.mobile.final_project.ma01_20160989.model.BlogDto;
import lecture.mobile.final_project.ma01_20160989.parser.BlogXmlParser;

public class ViewDetailsActivity extends AppCompatActivity {

    public static final String TAG = "ViewDetailsActivity";

    TextView tvTitle;
    TextView tvDesc;
    TextView tvTel;
    TextView tvAddr;
    TextView tvLink;
    FloatingActionButton fab;
    ListView lvBlogList;

    BakeryDBHelper dbHelper;
    BakeryDto bakeryDto;

    MyBlogAdapter adapter;
    ArrayList<BlogDto> resultList;
    BlogXmlParser parser;

    String apiAddress;
    String query;
    String addressName;

    private Geocoder geoCoder;
    private GoogleMap googleMap;
    private MarkerOptions markerOptions;

    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_details);
        setTitle("베이커리");

        dbHelper = new BakeryDBHelper(this);

        tvTitle = findViewById(R.id.tvTitle);
        tvDesc = findViewById(R.id.tvDesc);
        tvTel = findViewById(R.id.tvTel);
        tvAddr = findViewById(R.id.tvAddr);
        tvLink = findViewById(R.id.tvLink);

        Intent intent = getIntent();
        bakeryDto = (BakeryDto) intent.getSerializableExtra("bakeryDto");

        tvTitle.setText(bakeryDto.getTitle());
        tvDesc.setText(bakeryDto.getDescription());
        tvTel.setText(bakeryDto.getTelephone());
        tvAddr.setText(bakeryDto.getAddress());
        tvLink.setText(bakeryDto.getLink());

        lvBlogList = (ListView)findViewById(R.id.lvBlogList);
        resultList = new ArrayList<BlogDto>();
        adapter = new MyBlogAdapter(this, R.layout.listview_blog_layout, resultList);
        lvBlogList.setAdapter(adapter);

        apiAddress = getResources().getString(R.string.blog_api_url);
        parser = new BlogXmlParser();

        query = bakeryDto.getTitle();
        new BlogAsyncTask().execute();

        lvBlogList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BlogDto item = resultList.get(position);

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getLink()));
                startActivity(intent);
            }
        });

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                ContentValues row = new ContentValues();
                row.put(BakeryDBHelper.COL_TITLE, bakeryDto.getTitle());
                row.put(BakeryDBHelper.COL_LINK, bakeryDto.getLink());
                row.put(BakeryDBHelper.COL_DESCRIPTION, bakeryDto.getDescription());
                row.put(BakeryDBHelper.COL_TELEPHONE, bakeryDto.getTelephone());
                row.put(BakeryDBHelper.COL_ADDRESS, bakeryDto.getAddress());

                db.insert(BakeryDBHelper.TABLE_NAME, null, row);

                dbHelper.close();
                Toast.makeText(ViewDetailsActivity.this, "즐겨찾기에 추가되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addressName = bakeryDto.getAddress();
        geoCoder = new Geocoder(this);
        MapFragment mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.google_map);
        mapFragment.getMapAsync(mapReadyCallBack);
    }

    OnMapReadyCallback mapReadyCallBack = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap map) {
            List<Address> addressList = null;
            googleMap = map;

            try {
                addressList = geoCoder.getFromLocationName(addressName, 5);
            } catch (IOException e) {
                e.printStackTrace();
            }

            LatLng latLng = new LatLng(addressList.get(0).getLatitude(), addressList.get(0).getLongitude());

            markerOptions = new MarkerOptions()
                    .position(latLng);

            googleMap.addMarker(markerOptions);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        }
    };

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_actions, menu) ;

        return true ;
    }

    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_share:
                shareKakao();
                return true;
        }
        return super.onOptionsItemSelected(item);
    };

    public void shareKakao() {
        try{
            final KakaoLink kakaoLink = KakaoLink.getKakaoLink(this);
            final KakaoTalkLinkMessageBuilder kakaoBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();

            /*메시지 추가*/
            kakaoBuilder.addText(bakeryDto.getTitle());

            /*앱 실행버튼 추가*/
            kakaoBuilder.addAppButton("자세히 보기");

            /*메시지 발송*/
            kakaoLink.sendMessage(kakaoBuilder, this);

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    class BlogAsyncTask extends AsyncTask<String, Integer, String> {

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