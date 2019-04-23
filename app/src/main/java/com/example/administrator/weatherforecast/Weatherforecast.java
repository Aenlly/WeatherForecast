package com.example.administrator.weatherforecast;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.administrator.weatherforecast.bean.Province;
import com.example.administrator.weatherforecast.dao.ProvinceDao;
import com.example.administrator.weatherforecast.util.JsonParse;

import java.util.List;

public class Weatherforecast extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weatherforecast);
        final ProvinceDao provinceDao=new ProvinceDao(this);
       List<Province> provinces= JsonParse.parseProvince(this);
        for(int i=0;i<provinces.size();i++){
            provinceDao.saveDb(provinces.get(i));
            Log.i("myInfo",provinces.get(i).getName());
        }
        ListView listView=(ListView) findViewById(R.id.Lv);

        final List<String> provinceNames=provinceDao.getAllProvinceName();

        final ArrayAdapter adapter=new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,provinceNames);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //这个方法中有position是表示你单击的行号
                //单击这一行的行控件对象
                Toast.makeText(Weatherforecast.this,
                        position+"",Toast.LENGTH_LONG).show();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String name=provinceNames.get(position);//将集合中的该项返回
                provinceNames.remove(position);//将集合中该项删除
                adapter.notifyDataSetChanged();//将数据源的改变通过适配器通知界面进行显示
                provinceDao.del(name);//在数据库中删除
                return false;
            }
        });
    }
}
public class CustomAdapter extends BaseAdapter{
    private List<Province> provinces;
    private LayoutInflater inflater;
    private Context context;

    public CustomAdapter(List<Province>provinces,Context context){
        this.provinces=provinces;
        inflater=LayoutInflater.from(context);
        this.context=context;
    }

    public View getView(final int position, View convertView, ViewGroup parent){

    }

}
