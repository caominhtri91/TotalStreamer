package vn.edu.poly.totalstreamer.entities;

/**
 * Created by nix on 11/22/16.
 */

public class ChannelEntity {
    private String channelName;
    private String channelTitle;
    private String channelStatus;
    private String gamePlaying;
    private int viewers;
    private String channelPreview;

    public ChannelEntity() {
    }

    public ChannelEntity(String channelName, String channelTitle, String channelStatus, String gamePlaying, int viewers, String channelPreview) {
        this.channelName = channelName;
        this.channelTitle = channelTitle;
        this.channelStatus = channelStatus;
        this.gamePlaying = gamePlaying;
        this.viewers = viewers;
        this.channelPreview = channelPreview;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelTitle() {
        return channelTitle;
    }

    public void setChannelTitle(String channelTitle) {
        this.channelTitle = channelTitle;
    }

    public String getGamePlaying() {
        return gamePlaying;
    }

    public void setGamePlaying(String gamePlaying) {
        this.gamePlaying = gamePlaying;
    }

    public int getViewers() {
        return viewers;
    }

    public void setViewers(int viewers) {
        this.viewers = viewers;
    }

    public String getChannelPreview() {
        return channelPreview;
    }

    public void setChannelPreview(String channelPreview) {
        this.channelPreview = channelPreview;
    }

    public String getChannelStatus() {
        return channelStatus;
    }

    public void setChannelStatus(String channelStatus) {
        this.channelStatus = channelStatus;
    }
}
