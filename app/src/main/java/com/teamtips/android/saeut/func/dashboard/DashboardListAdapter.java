package com.teamtips.android.saeut.func.dashboard;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.teamtips.android.saeut.R;
import com.teamtips.android.saeut.data.Post;
import com.teamtips.android.saeut.data.Tag;
import com.teamtips.android.saeut.func.dashboard.service.PostNetwork;
import com.teamtips.android.saeut.func.dashboard.service.PostNetworkService;
import com.teamtips.android.saeut.func.login.data.model.LoggedInUser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DashboardListAdapter extends BaseAdapter {

    public static final String TAG = "DashboardChildAdapter";

    private Context context;                    // inflater 객체 생성 시 필요함
    private int layout;                         // AdapterView 항목에 대한 layout
    private ArrayList<Post> postArrayList;      // 원본 데이터 리스트
    private LayoutInflater layoutInflater;      // inflater 객체
    private static PostNetworkService postNetworkService = new PostNetworkService();
    private int tab_position;

    /* Retrofit */
    private Gson gson = new GsonBuilder().setLenient().create();
    private OkHttpClient.Builder builder = new OkHttpClient.Builder();
    private OkHttpClient client = builder.build();
    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://49.50.173.180:8080/saeut/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

    LoggedInUser loggedInUser = LoggedInUser.getLoggedInUser();

    public DashboardListAdapter() { }

    public DashboardListAdapter(Context context, int layout, ArrayList<Post> postArrayList, int tab_position) {
        this.context = context;
        this.layout = layout;
        this.postArrayList = postArrayList;
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.tab_position = tab_position;
    }

    @Override
    public int getCount() {
        return postArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return postArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return postArrayList.get(position).getPost_id();
    }

    @Override
    public View getView(int pos, View view, ViewGroup viewGroup) {
        // 리스트뷰의 position 위치를 저장, onClick에 사용하기 위해 상수로 선언

        final int position = pos;
        ViewHolder holder;

        Log.d(TAG, "dashboardChildAdapter 지롱");

        if(view == null) {
            view = layoutInflater.inflate(layout, viewGroup, false);
            holder = AllFindViewAdapter(view, tab_position);
            view.setTag(holder);    // ViewHolder 삽입
        } else {
            holder = (ViewHolder) view.getTag();
        }

        setDataInHolder(holder, position); // holder에 data 넣기

        // view 클릭 시 이벤트 처리
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 상세 페이지 뜨도록 정의해야 함.
                // 이 때 로그인 여부 체크
                // 기타 등등 ...

                Log.e(TAG, "onItemClick");
                Post post = postArrayList.get(pos);
                Log.e(TAG, post.toString());
                Intent intent = new Intent(context, DetailPostActivity.class);
                intent.putExtra("post", (Serializable) post);
                context.startActivity(intent);
            }
        });

        // Private Layout OnClickListener
        holder.private_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditPostActivity.class);
                intent.putExtra("post", postArrayList.get(position));
                context.startActivity(intent);
            }
        });

        holder.private_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postNetworkService.deletePost(postArrayList.get(position).getPost_id(), context);
                notifyDataSetChanged();
            }
        });

        return view;
    }

    // 새로운 게시글을 추가하는 메서드
    public void addItem(int post_id, String post_date, String id, String title, String post_age,
                        String post_schedule, String post_gender, String contents, String start_date, String due_date,
                        int status, int type, int wage, int limit_supply, int limit_demand, int payment){
        Post post = new Post(post_id, post_date, id, title, post_age, post_schedule,
                             post_gender, contents, start_date, due_date, status, type, wage, limit_supply, limit_demand, payment);
        postArrayList.add(post);
    }

    private ViewHolder AllFindViewAdapter(View view, int tab_position) {
        ViewHolder holder = new ViewHolder();

        holder.tv_type = (TextView) view.findViewById(R.id.tv_type);
        holder.btn_status = (Button) view.findViewById(R.id.btn_status);
        holder.tv_title = (TextView) view.findViewById(R.id.group_title_tv);
        holder.tv_date = (TextView) view.findViewById(R.id.tv_date);
        holder.tv_address = (TextView) view.findViewById(R.id.profile_tv_address);

        holder.tv_tag1 = (TextView) view.findViewById(R.id.tv_tag1);
        holder.tv_tag2 = (TextView) view.findViewById(R.id.tv_tag2);
        holder.tv_tag3 = (TextView) view.findViewById(R.id.tv_tag3);

        // Private Layout
        holder.private_layout = (ConstraintLayout) view.findViewById(R.id.private_layout);
        holder.private_update = (Button) view.findViewById(R.id.btn_private_update);
        holder.private_delete = (Button) view.findViewById(R.id.btn_private_delete);

        return holder;
    }

    private void setDataInHolder(ViewHolder holder, int position) {
        holder.tv_title.setText(
                postArrayList.get(position).getTitle());

        // Date 연결
        String date = postArrayList.get(position).getStart_date()
                + "~" + postArrayList.get(position).getDue_date();
        holder.tv_date.setText(date);

        int type = postArrayList.get(position).getType();
        holder.tv_type.setText(postArrayList.get(position).getTypeForText(type));
        int status = postArrayList.get(position).getRecruit_status();
        holder.btn_status.setText(postArrayList.get(position).getStatusForText(status));
        holder.tv_address.setText("성남시 분당구 백현동");

        // Tag 저장
        getTagByPostId(holder, postArrayList.get(position).getPost_id());

        if(tab_position == 3) {
            holder.private_layout.setVisibility(View.VISIBLE);
        } else {
            holder.private_layout.setVisibility(View.GONE);
        }
    }

    private void getTagByPostId(ViewHolder holder, int post_id) {
        retrofit.create(PostNetwork.class).getTagList(post_id).enqueue(new Callback<List<Tag>>() {
            @Override
            public void onResponse(Call<List<Tag>> call, Response<List<Tag>> response) {
                if(response.isSuccessful()) {
                    List<Tag> tagList = response.body();
                    if (tagList != null && !(tagList.isEmpty())) {
                        if(tagList.size() >= 3) {
                            holder.tv_tag1.setText(tagList.get(0).getTag_name());
                            holder.tv_tag2.setText(tagList.get(1).getTag_name());
                            holder.tv_tag3.setText(tagList.get(2).getTag_name());
                        }
                    }
                } else {
                    Log.d(TAG, "실패  : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Tag>> call, Throwable t) {
                Log.d(TAG, "연결 실패  : " + t.getMessage());
            }
        });
    }

    static class ViewHolder {
        TextView tv_type;
        Button btn_status;
        TextView tv_title;
        TextView tv_date;
        TextView tv_address;

        TextView applyCount;
        TextView tv_tag1;
        TextView tv_tag2;
        TextView tv_tag3;

        //Private Layout
        ConstraintLayout private_layout;
        Button private_update;
        Button private_delete;
    }
}
