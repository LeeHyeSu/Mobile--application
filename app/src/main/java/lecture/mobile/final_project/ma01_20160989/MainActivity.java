// 프로젝트
// 작성일: 2018. 12. 26
// 작성자: 01분반 20160989 이혜수

package lecture.mobile.final_project.ma01_20160989;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import lecture.mobile.final_project.ma01_20160989.fragment.BakerySearchFrag;
import lecture.mobile.final_project.ma01_20160989.fragment.BookmarkFrag;
import lecture.mobile.final_project.ma01_20160989.fragment.MemoFrag;

public class MainActivity extends AppCompatActivity {

    FragmentManager fm;
    FragmentTransaction tran;
    BakerySearchFrag BSFrag;
    BookmarkFrag BFrag;
    MemoFrag MFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BSFrag = new BakerySearchFrag();
        BFrag = new BookmarkFrag();
        MFrag = new MemoFrag();

        setTitle("베이커리 검색");
        setFrag(0);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.action_search:
                                setTitle("베이커리 검색");
                                setFrag(0);
                                return true;
                            case R.id.action_bookmark:
                                setTitle("즐겨찾기");
                                setFrag(1);
                                return true;
                            case R.id.action_memo:
                                setTitle("메모");
                                setFrag(2);
                                return true;
                        }
                        return false;
                    }
                }
        );
    }

    public void setFrag(int n) {
        fm = getSupportFragmentManager();
        tran = fm.beginTransaction();
        switch (n) {
            case 0:
                tran.replace(R.id.main_frame, BSFrag);
                tran.commit();
                break;
            case 1:
                tran.replace(R.id.main_frame, BFrag);
                tran.commit();
                break;
            case 2:
                tran.replace(R.id.main_frame, MFrag);
                tran.commit();
                break;
        }
    }

}
