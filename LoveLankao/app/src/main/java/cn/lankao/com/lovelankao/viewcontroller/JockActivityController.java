package cn.lankao.com.lovelankao.viewcontroller;

import android.app.ProgressDialog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.JsonElement;

import java.util.List;

import cn.lankao.com.lovelankao.R;
import cn.lankao.com.lovelankao.activity.JockActivity;
import cn.lankao.com.lovelankao.adapter.JockAdapter;
import cn.lankao.com.lovelankao.entity.Jock;
import cn.lankao.com.lovelankao.entity.JuheApiResult;
import cn.lankao.com.lovelankao.utils.CommonCode;
import cn.lankao.com.lovelankao.utils.GsonUtil;
import cn.lankao.com.lovelankao.utils.OkHttpUtil;
import cn.lankao.com.lovelankao.widget.OnRvScrollListener;
import cn.lankao.com.lovelankao.widget.ProDialog;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by BuZhiheng on 2016/4/18.
 */
public class JockActivityController implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    private final String url = "http://japi.juhe.cn/joke/content/list.from?key=da46a2a9e5d5a3bfefb5694bfa0e04c1&sort=asc&time=0000000000&pagesize=";
    private JockActivity context;
    private RecyclerView rv;
    private SwipeRefreshLayout refresh;
    private JockAdapter adapter;
    private ProgressDialog dialog;
    private int page = 1;
    private boolean isRefresh = true;
    private boolean canLoadMore = true;
    public JockActivityController(JockActivity context){
        this.context = context;
        initView();
        initData();
    }

    private void initData() {
        String finalUrl;
        if (isRefresh){
            finalUrl = url+ CommonCode.RV_ITEMS_COUT+"&page="+1;
        }else{
            finalUrl = url+CommonCode.RV_ITEMS_COUT+"&page="+page;
        }
        OkHttpUtil.getApi(finalUrl)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                refresh.setRefreshing(false);
            }

            @Override
            public void onError(Throwable e) {
                refresh.setRefreshing(false);
            }

            @Override
            public void onNext(String s) {
                JuheApiResult res = GsonUtil.jsonToObject(s, JuheApiResult.class);
                if (res.getError_code() == 0){
                    try{
                        JsonElement list = res.getResult().getAsJsonObject().getAsJsonArray("data");
                        List<Jock> data = GsonUtil.jsonToList(list,Jock.class);
                        if (isRefresh){
                            adapter.setData(data);
                        }else{
                            adapter.addData(data);
                        }
                        canLoadMore = true;
                        adapter.notifyDataSetChanged();
                        refresh.setRefreshing(false);
                        dialog.dismiss();
                    }catch (Exception e){
                        refresh.setRefreshing(false);
                    }
                }
            }
        });
    }

    private void initView() {
        dialog = ProDialog.getProDialog(context);
        dialog.show();
        adapter = new JockAdapter(context);
        context.setContentView(R.layout.activity_jock);
        context.findViewById(R.id.iv_jock_back).setOnClickListener(this);
        refresh = (SwipeRefreshLayout)context.findViewById(R.id.srl_jock_activity);
        refresh.setOnRefreshListener(this);
        refresh.setRefreshing(true);
        rv = (RecyclerView) context.findViewById(R.id.rv_activity_jock);
        rv.setLayoutManager(new LinearLayoutManager(context));
        rv.setAdapter(adapter);
        rv.addOnScrollListener(new OnRvScrollListener() {
            @Override
            public void toBottom() {
                if (canLoadMore) {
                    isRefresh = false;
                    canLoadMore = false;
                    page++;
                    initData();
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        isRefresh = true;
        page = 1;
        initData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_jock_back:
                context.finish();
                break;
        }
    }
}
