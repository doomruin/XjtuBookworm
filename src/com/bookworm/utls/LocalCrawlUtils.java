package com.bookworm.utls;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.util.Log;

import com.bookworm.dbmodel.Book;
import com.bookworm.dbmodel.BookCategory;
import com.bookworm.dbmodel.util.UserConfigDao;
import com.bookworm.json.JSONArray;
import com.bookworm.json.JSONException;
import com.bookworm.json.JSONObject;
import com.bookworm.searchbook.BookGuancang;

public class LocalCrawlUtils {
	
	
	public static ArrayList<BookGuancang> crawbookFromLib(int page, String keyword){
		/*
		 * 只接受类似下面的url路径
		 */
		//System.out.println(url);
		
		String url = "http://innopac.lib.xjtu.edu.cn/search/X?SEARCH="+keyword+"&guancang=on";
		//int startIndex = (page - 1) * 10;
		ArrayList<BookGuancang> arr = new ArrayList<BookGuancang>();
		String result1 = "";
		// 请求数据
		HttpGet httpRequest = new HttpGet(url);

		try {
			// 对提交数据进行编码

			HttpResponse httpResponse = new DefaultHttpClient()
					.execute(httpRequest);
			// 获取响应服务器的数据
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				// 利用字节数组流和包装的绑定数据
				result1 = EntityUtils.toString(httpResponse.getEntity());
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Document doc = Jsoup.parse(result1);
		//WSystem.out.println(result1);
		Elements Bigtables = doc.getElementsByClass("browseScreen");
		if (!Bigtables.isEmpty()) {
			
			List<Element> tables = Bigtables.get(0).getElementsByClass("briefCitRow");// 每本书信息
//			List<Element> smalltables =null;
//			System.out.println(tables.size());
//			if(tables.size() > (startIndex + 10)){
//				smalltables = tables.subList(startIndex, startIndex + 10);
//			}else if(tables.size() > startIndex){
//				smalltables = tables.subList(startIndex,tables.size());
//			}else{
//				
//				return arr;//如果初始index比tables长度还大，那么属于越界，直接返回一个空的arr
//			}
//			
			for (Element element : tables) {
				BookGuancang book = new BookGuancang();
				String title = element.getElementsByClass("briefcitTitle")
						.get(0).text();
				if (title.contains("/")) {// 如果有/那么/之前的为title
					book.setName(title.split("/")[0]);
				} else {
					book.setName(title);
				}
				Element titleExtra = element.getElementsByClass(
						"briefcitDetail").get(0);// 取得书籍名称，作者，出版社
				String[] temp = titleExtra.ownText().split(" ");
				if (temp[1].contains("(") || temp[0].contains("(")
						|| temp[2].contains("(")) {
					book.setAuthor(temp[0] + temp[1] + temp[2]);
				} else {
					book.setAuthor(temp[0]);
				}
				Elements guancangTbs = element
						.getElementsByClass("bibItemsEntry");
				StringBuffer sb = new StringBuffer();

				for (Element element2 : guancangTbs) {
					sb.append(element2.text());
					sb.append("\n");
				}
				book.setGuancang(sb.toString());
				 System.out.println(titleExtra.ownText());
				arr.add(book);
			}

			for (BookGuancang book : arr) {
				System.out.println(book.getGuancang());
			}

		} else {// 如果直接进入具体页面
			
			BookGuancang book = new BookGuancang();
			Elements guancang1 = doc.getElementsByClass("bibItemsEntry");
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < guancang1.size()/2; i++) {
				sb.append(guancang1.get(i).text());
				sb.append("\n");
			}
			book.setGuancang(sb.toString());
			arr.add(book);
			//System.out.println(book.getGuancang());
		}
		
		return arr;
	}
	/**
	 * 利用推荐类别所对应的抓取链接获取书籍在豆瓣的ID，长度20
	 * @param crawUrl
	 */
	public static List<String> getDoubanId(String crawUrl){
		List<String> result = new ArrayList<String>();
		String doubanId_regEx = "/[0-9]+/";// 匹配doubanId的正则表达式
		Pattern pat = Pattern.compile(doubanId_regEx);		
		String html = HtmlDownloader.Download(crawUrl);		
		Parser parser;
		try {
			parser = new Parser(html);
			parser.setEncoding("utf-8");
			NodeList bookUrlNodeList = parser.extractAllNodesThatMatch(new AndFilter(new TagNameFilter("a"), new HasAttributeFilter("title")));
			String doubanId = null;
			for (int i = 0; i < bookUrlNodeList.size(); i++) {
				doubanId = bookUrlNodeList.elementAt(i).getText();
				//System.out.println(doubanId);
				Matcher matcher = pat.matcher(doubanId);
				while (matcher.find()) {
				doubanId = doubanId.substring(matcher.start()+1, matcher.end()-1);
				result.add(doubanId);
				}
			}
		} catch (ParserException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
/**
 * 利用豆瓣的书籍ID扒取书籍具体信息,使用时必须保证UserConfigDao开启
 * @param doubanId
 * @return
 */
	public static void storeBookByDoubanId(String doubanId){
		
		
		
		try {
			String bookDetail = HtmlDownloader.Download("https://api.douban.com/v2/book/"+doubanId);
			JSONObject mess=new JSONObject(bookDetail);
			String imgUrl = mess.getString("image");
			String fileName = FileDownloader.saveImage(imgUrl);
			UserConfigDao.storeRecommendBook(mess.getString("title"), mess.getString("isbn13"), fileName, parseJSONArraytoString(mess.getJSONArray("author")), 
										doubanId, mess.getString("price"), mess.getString("publisher"), mess.getString("pubdate"), 
										mess.getString("pages"), mess.getString("summary").replaceAll("'", "\'"));
		
//			book = new Book();			
//			book.setDoubanId(doubanId);
//			book.setName(mess.getString("title"));
//			Log.i("name", book.getName());
//			book.setSummary(mess.getString("summary").replaceAll("'", "\'"));
//			book.setAuthor(parseJSONArraytoString(mess.getJSONArray("author")));
//			book.setSurfacePath(fileName);						
//			book.setIsbn(mess.getString("isbn13"));
//			book.setPages(mess.getString("pages"));
//			book.setPrice(mess.getString("price"));
//			book.setPublisher(mess.getString("publisher"));
//			book.setPublishDate(mess.getString("pubdate"));
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	
	}
	
	private static String parseJSONArraytoString (JSONArray arr)
    {
        StringBuffer str =new StringBuffer();
        for(int i=0;i<arr.length();i++)
        {
            try{
                str=str.append(arr.getString(i)).append(" ");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return str.toString();
    }

}
