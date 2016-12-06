package vn.edu.poly.totalstreamer.customAdapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import java.util.Collections;
import java.util.List;

import vn.edu.poly.totalstreamer.R;
import vn.edu.poly.totalstreamer.activities.TwitchChannelActivity;
import vn.edu.poly.totalstreamer.entities.GameEntity;
import vn.edu.poly.totalstreamer.extras.ConstantValue;
import vn.edu.poly.totalstreamer.myLog.L;
import vn.edu.poly.totalstreamer.network.VolleySingleton;


public class GameListAdapter extends RecyclerView.Adapter<GameListAdapter.MyViewHolder> {

    VolleySingleton volleySingleton;
    ImageLoader imageLoader;
    private LayoutInflater layoutInflater;
    private Context context;
    private List<GameEntity> games = Collections.emptyList();

    public GameListAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void setGamesList(List<GameEntity> games) {
        this.games = games;
        notifyItemRangeChanged(0, games.size());
        volleySingleton = VolleySingleton.getsInstance();
        imageLoader = VolleySingleton.getImageLoader();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.custom_game_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        GameEntity game = games.get(position);

        String url = game.getGameBitmap();
        if (!game.getGameBitmap().equals(ConstantValue.NA)) {
            imageLoader.get(url, new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    holder.imageView.setImageBitmap(response.getBitmap());
                }

                @Override
                public void onErrorResponse(VolleyError error) {
                    L.m("Error when get Image Bitmap");
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return games.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, TwitchChannelActivity.class);
                    GameEntity gameEntity = games.get(getAdapterPosition());
                    i.putExtra("gameName", gameEntity.getName());
                    context.startActivity(i);
                }
            });
        }
    }
}
