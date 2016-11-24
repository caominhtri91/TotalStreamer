package vn.edu.poly.totalstreamer.customAdapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import java.util.Collections;
import java.util.List;

import vn.edu.poly.totalstreamer.R;
import vn.edu.poly.totalstreamer.activities.TwitchStreamActivity;
import vn.edu.poly.totalstreamer.entities.ChannelEntity;
import vn.edu.poly.totalstreamer.extras.ConstantValue;
import vn.edu.poly.totalstreamer.myLog.L;
import vn.edu.poly.totalstreamer.network.VolleySingleton;

/**
 * Created by nix on 11/22/16.
 */

public class ChannelListAdapter extends RecyclerView.Adapter<ChannelListAdapter.MyViewHolder> {

    private Context context;
    private LayoutInflater inflater;

    private List<ChannelEntity> channels = Collections.emptyList();

    private VolleySingleton volleySingleton;
    private ImageLoader imageLoader;

    public ChannelListAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void setChannelList(List<ChannelEntity> channels) {
        this.channels = channels;
        notifyItemRangeChanged(0, channels.size());
        volleySingleton = VolleySingleton.getsInstance();
        imageLoader = VolleySingleton.getImageLoader();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.custom_channel_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        ChannelEntity channel = channels.get(position);

        holder.channelName.setText(channel.getChannelTitle());

        String channelStatus = channel.getChannelStatus();
        if (channelStatus.length() >= 45) {
            channelStatus = channelStatus.substring(0, 46) + "...";
        }
        holder.channelTitle.setText(channelStatus);

        String gamePlaying = channel.getGamePlaying();
        if (gamePlaying.length() >= 22) {
            gamePlaying = gamePlaying.substring(0, 23) + "...";
        }
        holder.channelGame.setText("Playing " + gamePlaying);
        holder.channelViewers.setText(channel.getViewers() + "viewers");

        String previewUrl = channel.getChannelPreview();
        if (!previewUrl.equals(ConstantValue.NA)) {
            imageLoader.get(previewUrl, new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    holder.autoBackground.setImageBitmap(response.getBitmap());
                }

                @Override
                public void onErrorResponse(VolleyError error) {
                    L.m("Failed to get Background Bitmap");
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return channels.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView autoBackground;
        TextView channelName, channelTitle, channelGame, channelViewers;

        public MyViewHolder(View itemView) {
            super(itemView);
            autoBackground = (ImageView) itemView.findViewById(R.id.autoBackground);
            channelName = (TextView) itemView.findViewById(R.id.channelname);
            channelTitle = (TextView) itemView.findViewById(R.id.channelTitle);
            channelGame = (TextView) itemView.findViewById(R.id.channelGame);
            channelViewers = (TextView) itemView.findViewById(R.id.channelViewer);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ChannelEntity channelEntity = channels.get(getAdapterPosition());
                    Intent i = new Intent(context, TwitchStreamActivity.class);
                    i.putExtra("channelName", channelEntity.getChannelName());
                    context.startActivity(i);
                }
            });
        }
    }
}
