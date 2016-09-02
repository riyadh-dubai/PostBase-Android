package com.example.riyad.postbase;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.support.v7.widget.ActionBarOverlayLayout;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by riyad on 29/08/2016.
 */
public class SimpleAdapter extends BaseAdapter{
    private Context context;
    public SimpleAdapter(Context context){
        super();
        this.context = context; // O contexto é necessário para criar a view
    }

    public List<Post> posts() {
        PostDB postDB = new PostDB(this.context);
        List<Post> posts = postDB.findAll();
        return posts;

    }

    @Override
    public int getCount(){

        return this.posts().size();//Retorna a quantidade de items no adapter
    }
    @Override
    public Object getItem(int position){
        return (this.posts().get(position).autor);//Retorna o objeto para essa posicao
    }
    @Override
    public long getItemId(int position){
        return position; // Retorna o id do objeto para esta posicao
    }
    @Override
    //Retorna a view para essa posição
    public View getView(final int position, View convertView, ViewGroup parent){
        TextView t = new TextView(context);
        final CardView c = new CardView(context);

        //Inicia o banco
        final PostDB postDB = new PostDB(this.context);
        final List<Post> posts = postDB.findAll();
        Collections.reverse(posts);

        //Parte do TextView (Nome do autor do post)
        t.setText(posts.get(position).autor);
        t.setGravity(Gravity.CENTER_HORIZONTAL);
        t.setTextColor(Color.parseColor("#FAA4BD"));
        t.setTextSize(15f);

        //Parte do TextView (Post)
        TextView p = new TextView(context);
        p.setGravity(Gravity.CENTER_VERTICAL + Gravity.CENTER_HORIZONTAL);
        p.setText(posts.get(position).desc);
        p.setTextColor(Color.parseColor("#FAE3C6"));
        p.setTextSize(20f);

        //Parte do TextView (Curtidas)
        final TextView l = new TextView(context);
        l.setText(posts.get(position).curtidas);
        l.setTextColor(Color.parseColor("#FAE3C6"));
        l.setTextSize(20f);
        l.setGravity(Gravity.BOTTOM + Gravity.RIGHT);
        l.setX(l.getX() - 10);

        //Parte do TextView (Data)
        final TextView d = new TextView(context);
        d.setTextColor(Color.parseColor("#FAE3C6"));
        d.setTextSize(10f);
        d.setGravity(Gravity.BOTTOM + Gravity.LEFT);
        d.setText(posts.get(position).data);

        //Parte do Button (Deletar)
        final ImageButton bt_deletar = new ImageButton(context);
        bt_deletar.setX(0f);
        bt_deletar.setY(0f);
        bt_deletar.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams
                .WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        bt_deletar.setImageResource(R.drawable.ic_delete);
        bt_deletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean show = true; //Mostra ou não a view
                int angulo = 180;
                ScaleAnimation encolher = new ScaleAnimation(
                        1.0f, 0.0f,
                        1.0f, 0.0f,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f
                );
                ScaleAnimation aumentar = new ScaleAnimation(
                        0.0f, 1.0f,
                        0.0f, 1.0f,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f
                );
                Animation a = show ? encolher : aumentar;
                a.setDuration(1000);
                a.setFillAfter(true);
                //Deleta o elemento na posicao
                postDB.delete(posts.get(position));
                //
                toast("Post excluído!");
                c.startAnimation(a);
            }
        });

        //Parte do Button (Curtir)
        ImageButton bt_curtir = new ImageButton(context);
        bt_curtir.setX(212f);
        bt_curtir.setY(0f);
        bt_curtir.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams
                .WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        bt_curtir.setImageResource(R.drawable.ic_star);
        bt_curtir.setOnClickListener(new View.OnClickListener() {
            Post post = new Post();
            int  valor_atual;
            @Override
            public void onClick(View v) {
                //Pega o post atual
                post = posts().get(posts().size() - position - 1);
                //Incrementa o número de curtidas
                valor_atual = Integer.parseInt(post.curtidas) + 1;
                post.curtidas = String.valueOf(valor_atual);
                //Atualiza o elemento no banco
                postDB.save(post);
                toast("Você curtiu isso!");
                l.setText(post.curtidas);
            }
        });

        //Parte do CardView
        int count = 0;
        if(posts().get(posts().size() - position - 1).prioridade.equals("baixa")){
            c.setCardBackgroundColor(Color.parseColor("#EFEFEF"));
        }else if (posts().get(posts().size() - position - 1).prioridade.equals("normal")){
            c.setCardBackgroundColor(Color.parseColor("#CAEBF2"));
        } else if (posts().get(posts().size() - position - 1).prioridade.equals("alta")){
            c.setCardBackgroundColor(Color.parseColor("#FF3B3F"));
        }

        c.setRadius(5f);
        c.setUseCompatPadding(true);
        c.setMinimumHeight(100);
        c.setMinimumHeight(280);
        c.addView(t, count);
        c.addView(p, count);
        c.addView(bt_deletar, count);
        c.addView(bt_curtir, count);
        c.addView(d, count);
        c.addView(l, count);
        count++;

        return c;
    }

    private void toast(String msg){

        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

}
