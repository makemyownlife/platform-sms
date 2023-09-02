package cn.emay.sdk.task;

import java.io.File;

import cn.emay.sdk.common.CommonConstants;
import cn.emay.sdk.util.HostUtil;

public class ContrastHostTask implements Runnable {

	public File file;

	public String appId;

	public String secretkey;

	public ContrastHostTask(File file, String appId, String secretkey) {
		this.file = file;
		this.appId = appId;
		this.secretkey = secretkey;
	}

	@Override
	public void run() {
		if (CommonConstants.interList != null) {
			CommonConstants.bestUrl = HostUtil.getUrl(appId, secretkey);
			HostUtil.contrastWrite(file, appId, CommonConstants.bestUrl);
			CommonConstants.isBest = true;
		}
	}

}
