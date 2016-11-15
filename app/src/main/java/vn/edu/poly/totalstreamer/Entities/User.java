package vn.edu.poly.totalstreamer.Entities;

/**
 * Created by nix on 11/13/16.
 */

public class User {
    private String fullname;
    private int age;
    private int gender;
    private int subscription;
    private String avatar;


    public User() {
        this.fullname = "";
        this.age = -1;
        this.gender = -1;
        this.subscription = -1;
        this.avatar = null;
    }

    public User(String fullname, int age, int gender, int subscription) {
        this.fullname = fullname;
        this.gender = gender;
        this.subscription = subscription;
        this.age = age;
    }

    public User(String fullname, int age, int gender, int subscription, String avatar) {
        this.fullname = fullname;
        this.age = age;
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
