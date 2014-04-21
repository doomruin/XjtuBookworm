package com.bookworm.utls;

import java.io.File;

public class FilePathTool {
	private static File getFile() {
		// 关键是这行...
		String path = FilePathTool.class.getProtectionDomain().getCodeSource()
				.getLocation().getFile();
		try {
			path = java.net.URLDecoder.decode(path, "UTF-8");// 转换处理中文及空格
		} catch (java.io.UnsupportedEncodingException e) {
			return null;
		}
		return new File(path);
	}

	public static String getWebInfDir() {
		File file = getFile();
		if (file == null)
			return null;
		return file.getParentFile().getParentFile().getParent();
	}
}
