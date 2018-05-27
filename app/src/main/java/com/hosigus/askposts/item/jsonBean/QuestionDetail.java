package com.hosigus.askposts.item.jsonBean;

import com.hosigus.tools.items.JSONBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 某只机智 on 2018/5/26.
 */

public class QuestionDetail extends JSONBean {
    /**
     * is_self : 0
     * title : 这个代码太难写了\\ue056
     * description : 代码是真的难
     * reward : 2
     * disappear_at : 2018-04-27 02:22:22
     * tags : PHP
     * kind : 其他
     * photo_urls : []
     * questioner_nickname : 。
     * questioner_photo_thumbnail_src :
     * questioner_gender : 女
     * answers : [{"id":"10","nickname":"Jay","photo_thumbnail_src":"http://wx.idsbllp.cn/cyxbsMobile/Public/photo/thumbnail/1503374869_593154551.png","gender":"男","content":"菜","created_at":"2018-04-22 14:08:50","praise_num":"0","comment_num":"0","is_adopted":"0","is_praised":0,"photo_url":[]}]
     */

    private int is_self;
    private String title;
    private String description;
    private String reward;
    private String disappear_at;
    private String tags;
    private String kind;
    private String questioner_nickname;
    private String questioner_photo_thumbnail_src;
    private String questioner_gender;
    private List<String> photo_urls;
    private List<AnswersBean> answers;

    public QuestionDetail() {}

    public QuestionDetail(JSONObject data) {
        parser(data);
        setJson(data.toString());
    }

    @Override
    public void parser(JSONObject object) {
// TODO: 2018/5/26
        try {
            is_self = object.getInt("is_self");
            title = object.getString("title");
            description = object.getString("description");
            reward = object.getString("reward");
            disappear_at = object.getString("disappear_at");
            tags = object.getString("tags");
            kind = object.getString("kind");
            questioner_nickname = object.getString("questioner_nickname");
            questioner_photo_thumbnail_src = object.getString("questioner_photo_thumbnail_src");
            questioner_gender = object.getString("questioner_gender");
            JSONArray urlArray = object.getJSONArray("photo_urls");
            photo_urls = new ArrayList<>();
            for (int i = 0; i < urlArray.length(); i++) {
                photo_urls.add(urlArray.getString(i));
            }
            JSONArray answerArray = object.getJSONArray("answers");
            answers = new ArrayList<>();
            for (int i = 0; i < answerArray.length(); i++) {
                answers.add(new AnswersBean(answerArray.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean isSelf() {
        return is_self == 1;
    }

    public int getIs_self() {
        return is_self;
    }

    public void setIs_self(int is_self) {
        this.is_self = is_self;
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

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public String getDisappear_at() {
        return disappear_at;
    }

    public void setDisappear_at(String disappear_at) {
        this.disappear_at = disappear_at;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getQuestioner_nickname() {
        return questioner_nickname;
    }

    public void setQuestioner_nickname(String questioner_nickname) {
        this.questioner_nickname = questioner_nickname;
    }

    public String getQuestioner_photo_thumbnail_src() {
        return questioner_photo_thumbnail_src;
    }

    public void setQuestioner_photo_thumbnail_src(String questioner_photo_thumbnail_src) {
        this.questioner_photo_thumbnail_src = questioner_photo_thumbnail_src;
    }

    public String getQuestioner_gender() {
        return questioner_gender;
    }

    public void setQuestioner_gender(String questioner_gender) {
        this.questioner_gender = questioner_gender;
    }

    public List<String> getPhoto_urls() {
        return photo_urls;
    }

    public void setPhoto_urls(List<String> photo_urls) {
        this.photo_urls = photo_urls;
    }

    public List<AnswersBean> getAnswers() {
        return answers;
    }

    public void setAnswers(List<AnswersBean> answers) {
        this.answers = answers;
    }

    public static class AnswersBean extends JSONBean {
        /**
         * id : 10
         * nickname : Jay
         * photo_thumbnail_src : http://wx.idsbllp.cn/cyxbsMobile/Public/photo/thumbnail/1503374869_593154551.png
         * gender : 男
         * content : 菜
         * created_at : 2018-04-22 14:08:50
         * praise_num : 0
         * comment_num : 0
         * is_adopted : 0
         * is_praised : 0
         * photo_url : []
         */

        private String id;
        private String nickname;
        private String photo_thumbnail_src;
        private String gender;
        private String content;
        private String created_at;
        private int praise_num;
        private int comment_num;
        private int is_adopted;
        private int is_praised;
        private List<String> photo_url;

        public AnswersBean() {

        }

        public AnswersBean(JSONObject jsonObject) {
            parser(jsonObject);
            setJson(jsonObject.toString());
        }

        @Override
        public void parser(JSONObject object) {
            try {
                id=object.getString("id");
                nickname=object.getString("nickname");
                photo_thumbnail_src=object.getString("photo_thumbnail_src");
                gender=object.getString("gender");
                content=object.getString("content");
                created_at=object.getString("created_at");
                praise_num=object.getInt("praise_num");
                comment_num=object.getInt("comment_num");
                is_adopted=object.getInt("is_adopted");
                is_praised=object.getInt("is_praised");
                photo_url = new ArrayList<>();
                JSONArray photo_urlArray = object.getJSONArray("photo_url");
                for (int i = 0; i < photo_urlArray.length(); i++) {
                    photo_url.add(photo_urlArray.getString(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public boolean isPraised() {
            return is_praised == 1;
        }

        public boolean isAdopted() {
            return is_adopted == 1;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getPhoto_thumbnail_src() {
            return photo_thumbnail_src;
        }

        public void setPhoto_thumbnail_src(String photo_thumbnail_src) {
            this.photo_thumbnail_src = photo_thumbnail_src;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public int getPraise_num() {
            return praise_num;
        }

        public void setPraise_num(int praise_num) {
            this.praise_num = praise_num;
        }

        public int getComment_num() {
            return comment_num;
        }

        public void setComment_num(int comment_num) {
            this.comment_num = comment_num;
        }

        public int getIs_adopted() {
            return is_adopted;
        }

        public void setIs_adopted(int is_adopted) {
            this.is_adopted = is_adopted;
        }

        public int getIs_praised() {
            return is_praised;
        }

        public void setIs_praised(int is_praised) {
            this.is_praised = is_praised;
        }

        public List<String> getPhoto_url() {
            return photo_url;
        }

        public void setPhoto_url(List<String> photo_url) {
            this.photo_url = photo_url;
        }

        public static class Commend extends JSONBean {

            /**
             * content : 测试
             * created_at : 2018-02-24 01:45:56
             * nickname : 溟\\n濛
             * photo_thumbnail_src : http://wx.idsbllp.cn/cyxbsMobile/Public/photo/thumbnail/1503374918_2132490885.png
             * gender : 男
             */

            private String content;
            private String created_at;
            private String nickname;
            private String photo_thumbnail_src;
            private String gender;

            public Commend(){}

            public Commend(JSONObject object) {
                parser(object);
                setJson(object.toString());
            }

            @Override
            public void parser(JSONObject object) {
                try {
                    content=object.getString("content");
                    created_at=object.getString("created_at");
                    nickname=object.getString("nickname");
                    photo_thumbnail_src=object.getString("photo_thumbnail_src");
                    gender=object.getString("gender");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getCreated_at() {
                return created_at;
            }

            public void setCreated_at(String created_at) {
                this.created_at = created_at;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getPhoto_thumbnail_src() {
                return photo_thumbnail_src;
            }

            public void setPhoto_thumbnail_src(String photo_thumbnail_src) {
                this.photo_thumbnail_src = photo_thumbnail_src;
            }

            public String getGender() {
                return gender;
            }

            public void setGender(String gender) {
                this.gender = gender;
            }

        }
    }
}
