package com.auto2fa.client.util;

import com.google.common.io.Resources;
import io.netty.util.CharsetUtil;
import sun.misc.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;

import static java.lang.System.in;

public class ResouceHelper {

	public static String getResource(String resource) throws IOException {
		return Resources.toString(Resources.getResource(resource), CharsetUtil.UTF_8);
	}

}
