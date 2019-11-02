package lecture.mobile.final_project.ma01_20160989.parser;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

import lecture.mobile.final_project.ma01_20160989.model.BlogDto;

public class BlogXmlParser {
    public enum TagType { NONE, TITLE, LINK, DESC, NAME, DATE};

    public BlogXmlParser() {
    }

    public ArrayList<BlogDto> parse(String xml) {

        ArrayList<BlogDto> resultList = new ArrayList();
        BlogDto dto = null;

        BlogXmlParser.TagType tagType = BlogXmlParser.TagType.NONE;

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(xml));

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.END_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("item")) {
                            dto = new BlogDto();
                        } else if (parser.getName().equals("title")) {
                            if (dto != null) tagType = BlogXmlParser.TagType.TITLE;
                        } else if (parser.getName().equals("link")) {
                            if (dto != null) tagType = BlogXmlParser.TagType.LINK;
                        } else if (parser.getName().equals("description")) {
                            if (dto != null) tagType = BlogXmlParser.TagType.DESC;
                        } else if (parser.getName().equals("bloggername")) {
                            if (dto != null) tagType = BlogXmlParser.TagType.NAME;
                        } else if (parser.getName().equals("postdate")) {
                            if (dto != null) tagType = BlogXmlParser.TagType.DATE;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("item")) {
                            resultList.add(dto);
                            dto = null;
                        }
                        break;
                    case XmlPullParser.TEXT:
                        switch(tagType) {
                            case TITLE:
                                //타이틀에 <b>나 </b>, &amp;가 있을 경우 replaceAll 메소드를 사용하여 없애주는 작업
                                String text = parser.getText().replaceAll("<b>", "").replaceAll("</b>", "").replaceAll("&amp;", " ");
                                dto.setTitle(text);
                                break;
                            case LINK:
                                dto.setLink(parser.getText());
                                break;
                            case DESC:
                                dto.setDescription(parser.getText());
                                break;
                            case NAME:
                                dto.setBloggername(parser.getText());
                                break;
                            case DATE:
                                dto.setPostdate(parser.getText());
                                break;
                        }
                        tagType = BlogXmlParser.TagType.NONE;
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultList;
    }
}
