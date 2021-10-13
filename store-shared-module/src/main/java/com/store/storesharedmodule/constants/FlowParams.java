package com.store.storesharedmodule.constants;

import java.util.Date;

import com.store.storesharedmodule.multiobjectpayload.FlowParamField;

public class FlowParams {
    
    private FlowParams() {

    }

    public static final FlowParamField<String> USERNAME = new FlowParamField<>("username");
    public static final FlowParamField<String> SUBJECT_ID = new FlowParamField<>("subject_id");
    public static final FlowParamField<Date> LOGIN_DATE = new FlowParamField<>("login_date");
}
