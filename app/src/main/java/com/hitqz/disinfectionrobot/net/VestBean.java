package com.hitqz.disinfectionrobot.net;

import java.io.Serializable;

public class VestBean implements Serializable {

    private String team;
    private Integer num;
    private String name;
    private String weapon1;
    private Integer ammo1;
    private String weapon2;
    private Integer ammo2;
    private Integer hp;
    private Double lat;
    private Double lng;
    private String token;
    private String userId;
    private Integer status;

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWeapon1() {
        return weapon1;
    }

    public void setWeapon1(String weapon1) {
        this.weapon1 = weapon1;
    }

    public Integer getAmmo1() {
        return ammo1;
    }

    public void setAmmo1(Integer ammo1) {
        this.ammo1 = ammo1;
    }

    public String getWeapon2() {
        return weapon2;
    }

    public void setWeapon2(String weapon2) {
        this.weapon2 = weapon2;
    }

    public Integer getAmmo2() {
        return ammo2;
    }

    public void setAmmo2(Integer ammo2) {
        this.ammo2 = ammo2;
    }

    public Integer getHp() {
        return hp;
    }

    public void setHp(Integer hp) {
        this.hp = hp;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
