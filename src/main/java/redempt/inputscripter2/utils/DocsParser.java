package redempt.inputscripter2.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DocsParser {
	
	private static final int SECTION_NAME = 1;
	private static final int ARTICLE_NAME = 2;
	private static final int ARTICLE_INFO = 3;
	private static final int NONE = 0;
	
	public static Map<String, List<DocData>> parseDoc(String doc) {
		String sectionName = "";
		String articleName = "";
		String info = "";
		int type = 0;
		Map<String, List<DocData>> docs = new HashMap<>();
		for (char c : doc.toCharArray()) {
			switch (c) {
				case '[':
					sectionName = "";
					type = SECTION_NAME;
					continue;
				case ']':
					type = NONE;
					continue;
				case '#':
					articleName = "";
					type = ARTICLE_NAME;
					continue;
				case '{':
					info = "";
					type = ARTICLE_INFO;
					continue;
				case '}':
					List<DocData> list = docs.get(sectionName);
					if (list == null) {
						list = new ArrayList<>();
					}
					list.add(new DocData(articleName, info));
					docs.put(sectionName, list);
					type = NONE;
					continue;
			}
			switch (type) {
				case SECTION_NAME:
					sectionName += c;
					break;
				case ARTICLE_NAME:
					articleName += c;
					break;
				case ARTICLE_INFO:
					info += c;
					break;
			}
		}
		return docs;
	}
	
}
