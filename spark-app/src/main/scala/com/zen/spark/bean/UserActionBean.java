package com.zen.spark.bean;

import java.io.Serializable;
import java.util.List;

public class UserActionBean implements Serializable {

    String user_id;
    List<String> hist_game_list;
    double total_consumption;
    double last_15_consumption;
    int num_of_game;
    double visit_times_average;


    int last_visit_date;
    int skip_ad_times;
    List<String> clicked_ad_list;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public List<String> getHist_game_list() {
        return hist_game_list;
    }

    public void setHist_game_list(List<String> hist_game_list) {
        this.hist_game_list = hist_game_list;
    }

    public double getTotal_consumption() {
        return total_consumption;
    }

    public void setTotal_consumption(double total_consumption) {
        this.total_consumption = total_consumption;
    }

    public double getLast_15_consumption() {
        return last_15_consumption;
    }

    public void setLast_15_consumption(double last_15_consumption) {
        this.last_15_consumption = last_15_consumption;
    }

    public int getNum_of_game() {
        return num_of_game;
    }

    public void setNum_of_game(int num_of_game) {
        this.num_of_game = num_of_game;
    }

    public double getVisit_times_average() {
        return visit_times_average;
    }

    public void setVisit_times_average(double visit_times_average) {
        this.visit_times_average = visit_times_average;
    }

    public int getLast_visit_date() {
        return last_visit_date;
    }

    public void setLast_visit_date(int last_visit_date) {
        this.last_visit_date = last_visit_date;
    }

    public int getSkip_ad_times() {
        return skip_ad_times;
    }

    public void setSkip_ad_times(int skip_ad_times) {
        this.skip_ad_times = skip_ad_times;
    }

    public List<String> getClicked_ad_list() {
        return clicked_ad_list;
    }

    public void setClicked_ad_list(List<String> clicked_ad_list) {
        this.clicked_ad_list = clicked_ad_list;
    }

    @Override
    public String toString() {
        return "UserActionInfo{" +
                "user_id='" + user_id + '\'' +
                ", hist_game_list=" + hist_game_list +
                ", total_consumption=" + total_consumption +
                ", last_15_consumption=" + last_15_consumption +
                ", num_of_game=" + num_of_game +
                ", visit_times_average=" + visit_times_average +
                ", last_visit_date=" + last_visit_date +
                ", skip_ad_times=" + skip_ad_times +
                ", clicked_ad_list=" + clicked_ad_list +
                '}';
    }


}