package com.hosigus.askposts.item.Option;

import com.hosigus.askposts.App;
import com.hosigus.askposts.item.jsonBean.BaseBean;
import com.hosigus.tools.options.NetOption;

/**
 * Created by 某只机智 on 2018/5/25.
 */

public class BaseOption extends NetOption {
    public BaseOption(String url) {
        super(url);
        beanClass(BaseBean.class);
    }

    public NetOption addUserParam(){
        return this.param("stuNum", App.getMe().getStuNum()).param("idNum", App.getPwd(App.getMe().getStuNum()));
    };

}
