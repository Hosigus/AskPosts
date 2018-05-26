package com.hosigus.askposts.item.jsonBean;

import com.hosigus.tools.items.JSONBean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 某只机智 on 2018/5/26.
 */

public class Question extends JSONBean {
    /**
     * title : 又在写bug\\ue056
     * description : bug是真的多
     * kind : 其他
     * tags : PHP
     * reward : 2
     * answer_num : 0
     * disappear_at : 2019-02-27 01:11:20
     * created_at : 2018-05-19 17:35:14
     * is_anonymous : 0
     * id : 52
     * photo_thumbnail_src :
     * nickname : 。
     * gender : 女
     */

    private String title;
    private String description;
    private String kind;
    private String tags;
    private int reward;
    private int answer_num;
    private String disappear_at;
    private String created_at;
    private int is_anonymous;
    private int id;
    private String photo_thumbnail_src;
    private String nickname;
    private String gender;

    public Question() {

    }

    public Question(JSONObject question) {
        parser(question);
    }

    @Override
    public void parser(JSONObject object) {
        try {
            title = object.getString("title");
            description = object.getString("description");
            kind = object.getString("kind");
            tags = object.getString("tags");
            reward = object.getInt("reward");
            answer_num = object.getInt("answer_num");
            disappear_at = object.getString("disappear_at");
            created_at = object.getString("created_at");
            is_anonymous = object.getInt("is_anonymous");
            id = object.getInt("id");
            photo_thumbnail_src = object.getString("photo_thumbnail_src");
            nickname = object.getString("nickname");
            gender = object.getString("gender");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public int getReward() {
        return reward;
    }

    public void setReward(int reward) {
        this.reward = reward;
    }

    public int getAnswer_num() {
        return answer_num;
    }

    public void setAnswer_num(int answer_num) {
        this.answer_num = answer_num;
    }

    public String getDisappear_at() {
        return disappear_at;
    }

    public void setDisappear_at(String disappear_at) {
        this.disappear_at = disappear_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getIs_anonymous() {
        return is_anonymous;
    }

    public void setIs_anonymous(int is_anonymous) {
        this.is_anonymous = is_anonymous;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhoto_thumbnail_src() {
        return photo_thumbnail_src;
    }

    public void setPhoto_thumbnail_src(String photo_thumbnail_src) {
        this.photo_thumbnail_src = photo_thumbnail_src;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

}
