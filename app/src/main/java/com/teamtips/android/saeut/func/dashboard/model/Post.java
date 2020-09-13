package com.teamtips.android.saeut.func.dashboard.model;

import java.io.Serializable;
import java.util.Date;

/*
 * Post Domain Class
 * */

public class Post implements Serializable {

    // Variable
    private int post_id; //sequence
    private String account_id; // (FK) account 클래스의 id
    private String title; //게시물 제목
    private Date post_date; //게시글 추가한 날짜
    private String contents; // 게시글 내용
    private Date start_date; // 돌봄 요청 시작 날짜 -> 모바일 캘린더
    private Date due_date; // 돌봄 요청 마지막 날짜  -> 모바일 캘린더
    private int status; // 현재 돌봄 진행상태

    // Constructor
    public Post() { }

    public Post(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }

    public Post(int post_id, String account_id, String title, Date post_date, Date due_date) {
        this.post_id = post_id;
        this.account_id = account_id;
        this.title = title;
        this.post_date = post_date;
        this.due_date = due_date;
        this.status = 0;            // status init 0
    }



    // Setter & Getter
    public int getPost_id() {
        return post_id;
    }
    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }
    public String getAccount_id() {
        return account_id;
    }
    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public Date getPost_date() {
        return post_date;
    }
    public void setPost_date(Date post_date) {
        this.post_date = post_date;
    }
    public String getContents() {
        return contents;
    }
    public void setContents(String contents) {
        this.contents = contents;
    }
    public Date getStart_date() {
        return start_date;
    }
    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }
    public Date getDue_date() { return due_date; }
    public void setDue_date(Date due_date) {
        this.due_date = due_date;
    }
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusForText(int status) {
        if(status == 0) {
            return "모집 중";
        } else if(status == 1) {
            return "모집 마감";
        } else {
            return "완료";
        }
    }
}