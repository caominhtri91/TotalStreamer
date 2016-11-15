package vn.edu.poly.totalstreamer.Entities;

/**
 * Created by nix on 11/13/16.
 */

public class User {
    private String fullname;
    private int gender;
    private int subscription;
    private byte[] avatar;

    public User() {
        this.fullname = "";
        this.gender = -1;
        this.subscription = -1;
        this.avatar = null;
    }

    public User(String fullname, int gender, int subscription) {
        this.fullname = fullname;
        this.gender = gender;
        this.subscription = subscription;
    }

    public User(String fullname, int gender, int subscription, byte[] avatar) {
        this.fullname = fullname;
        this.gender = gender;
        this.subscription = subscription;
        this.avatar = avatar;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getSubscription() {
        return subscription;
    }

    public void setSubscription(int subscription) {
        this.subscription = subscription;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }
}
