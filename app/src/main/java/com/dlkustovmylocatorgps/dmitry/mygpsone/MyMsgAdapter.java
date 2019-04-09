package com.dlkustovmylocatorgps.dmitry.mygpsone;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;


public class MyMsgAdapter extends BaseAdapter {
        Context ctx;
        LayoutInflater lInflater;
        ArrayList<CMessages> objects;

    MyMsgAdapter(Context context, ArrayList<CMessages> products) {
            ctx = context;
            objects = products;
            lInflater = (LayoutInflater) ctx
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        // кол-во элементов
        @Override
        public int getCount() {
            return objects.size();
        }

        // элемент по позиции
        @Override
        public Object getItem(int position) {
            return objects.get(position);
        }

        // id по позиции
        @Override
        public long getItemId(int position) {
            return position;
        }

        // пункт списка
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // используем созданные, но не используемые view
            View view = convertView;
            if (view == null) {
                view = lInflater.inflate(R.layout.activity_listview, parent, false);
            }

            CMessages p = getCMessage(position);
            ((TextView) view.findViewById(R.id.tvTimeMsgSending)).setText(p.msg_time);
            ((TextView) view.findViewById(R.id.tvBodyMsg)).setText(p.msg_body + "");
            ((Button) view.findViewById(R.id.btnLoadingFile)).setText(p.msg_body);
           /* if(p.msg_is_text.equals("false"))
            {
                ((TextView) view.findViewById(R.id.tvTimeMsgSending)).setText(p.msg_time);
                ((TextView) view.findViewById(R.id.tvBodyMsg)).setVisibility(View.INVISIBLE);
                ((Button) view.findViewById(R.id.btnLoadingFile)).setText(p.msg_body);
            }
            else
            {
                ((TextView) view.findViewById(R.id.tvTimeMsgSending)).setText(p.msg_time);
                ((TextView) view.findViewById(R.id.tvBodyMsg)).setText(p.msg_body + "");
                ((Button) view.findViewById(R.id.btnLoadingFile)).setVisibility(View.INVISIBLE);
            }*/
            // заполняем View в пункте списка данными из товаров: наименование, цена
            // и картинка
           /* ((TextView) view.findViewById(R.id.tvTimeMsgSending)).setText(p.msg_time);
            ((TextView) view.findViewById(R.id.tvBodyMsg)).setText(p.msg_body + "");
            ((TextView) view.findViewById(R.id.btnLoadingFile)).setText(p.msg_body);*/
           // ((ImageView) view.findViewById(R.id.ivImage)).setImageResource(p.image);

            //CheckBox cbBuy = (CheckBox) view.findViewById(R.id.cbBox);
            // присваиваем чекбоксу обработчик
            //cbBuy.setOnCheckedChangeListener(myCheckChangeList);
            // пишем позицию
            //cbBuy.setTag(position);
            // заполняем данными из товаров: в корзине или нет
            //cbBuy.setChecked(p.box);
            return view;
        }

        // товар по позиции
        CMessages getCMessage(int position) {
            return ((CMessages) getItem(position));
        }

        // содержимое корзины
       /* ArrayList<CMessages> getBox() {
            ArrayList<CMessages> box = new ArrayList<CMessages>();
            for (CMessages p : objects) {
                // если в корзине
                if (p.box)
                    box.add(p);
            }
            return box;
        }*/

        // обработчик для чекбоксов
        /*OnCheckedChangeListener myCheckChangeList = new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                // меняем данные товара (в корзине или нет)
                getProduct((Integer) buttonView.getTag()).box = isChecked;
            }
        };*/
    }
