package com.wx.java.share.entity.menu;

/**
 * 父按钮
 * Created by clear on 2017/2/19.
 */
public class ComplexButton extends Button{
    private Button [] sub_button;

    public Button[] getSub_button() {
        return sub_button;
    }

    public void setSub_button(Button[] sub_button) {
        this.sub_button = sub_button;
    }
}
