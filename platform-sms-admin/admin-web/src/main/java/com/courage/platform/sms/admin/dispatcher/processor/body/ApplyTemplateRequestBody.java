package com.courage.platform.sms.admin.dispatcher.processor.body;

import com.courage.platform.sms.admin.dispatcher.processor.RequestBody;

public class ApplyTemplateRequestBody extends RequestBody {

    private Long bindingId;

    public ApplyTemplateRequestBody(Long bindingId) {
        this.bindingId = bindingId;
    }

    public Long getBindingId() {
        return bindingId;
    }

}
