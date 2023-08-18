package com.courage.platform.sms.admin.dispatcher.processor.body;

import com.courage.platform.sms.admin.dispatcher.processor.ProcessorRequestBody;

public class ApplyTemplateRequestBody extends ProcessorRequestBody {

    private Long bindingId;

    public ApplyTemplateRequestBody(Long bindingId) {
        this.bindingId = bindingId;
    }

    public Long getBindingId() {
        return bindingId;
    }

}
