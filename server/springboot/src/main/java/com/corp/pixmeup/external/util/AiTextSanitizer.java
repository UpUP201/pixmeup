package com.corp.pixmeup.external.util;

public class AiTextSanitizer {

	public static String sanitize(String rawText) {
		if (rawText == null) return null;

		return rawText
			.replace("\\n", "\n")     // \n → 개행
			.replace("\\t", "\t")     // \t → 탭
			.replace("\\\"", "\"")    // \" → "
			.replace("\\\\", "\\")    // \\ → \
			.replace("&quot;", "\"")  // HTML quote → "
			.replace("&amp;", "&")    // &amp; → &
			.replace("&lt;", "<")     // &lt; → <
			.replace("&gt;", ">")     // &gt; → >
			.replaceAll("[\\p{Cntrl}&&[^\n\t]]", "") // 나머지 컨트롤 문자 제거 (개행, 탭 제외)
			.trim(); // 앞뒤 공백 제거
	}
}
