package zhaw.picturePlugin.plugin;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.CharBuffer;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Random;

import sun.misc.BASE64Encoder;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * This class searchs images on the bing search engine for martin
 * 
 * @author marco
 *
 */
public class ImageSearch {

	Document document;
	URLEncoder en;

	// public static void main(String[] args) throws IOException {
	// ImageSearch image = new ImageSearch();
	//
	// System.out.println(image.getImage("you"));
	// }

	/**
	 * search image on bing engine and return random output
	 * 
	 * @param type
	 * @return random URL of the requested picture type
	 * @throws IOException
	 */
	public String getImage(String type) throws IOException {
		String picture = type;
		if (picture.contains("you")) {
			picture = "clown";
		}
		
		
		String encodedString = URLEncoder.encode(picture, "UTF-8");
		ArrayList<String> list = new ArrayList<>();
		Random randomGenerator = new Random();
		document = Jsoup
				.connect("http://www.bing.com/images/search?q=" + encodedString
						+ "&go=Anfrage+senden&qs=ds&form=QBLH&scope=images&qft=+filterui:imagesize-large")
				.userAgent(
						"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11")
				.get();
		Elements elements = document.select(".img_hid");
		for (int i = 0; i < elements.size(); i++) {
			String href = elements.get(i).attr("src2");
			list.add(href);
		}
		int index = randomGenerator.nextInt(list.size());
		String text = list.get(index);
		int indexOfAmpersand = text.indexOf("&");
		text = text.substring(0, indexOfAmpersand);
		return text + "&w=236&h=150&rs=0&qlt=100";

	}

	/**
	 * search image on bing engine and return random output
	 * 
	 * @param query
	 * @return random URL of the requested picture type
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 */
	public String getImageFromPixelBay(String query)
			throws IOException, KeyManagementException, NoSuchAlgorithmException {
		if (query.contains("you")) {
			query = "clown";
		}
		
		
		String encodedQuery = URLEncoder.encode(query, "UTF-8");
		ArrayList<String> list = new ArrayList<>();
		Random randomGenerator = new Random();

		disableSSLCertCheck();

		document = Jsoup
				.connect("https://pixabay.com/en/photos/?image_type=&cat=&min_width=&min_height=&order=popular&orientation=horizontal&q="
						+ encodedQuery)
				.userAgent(
						"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11")
				.get();
		Elements elements = document.select(".item > a > img");
		String base64Picture;
		
		if(elements.size() > 0){
			for (int i = 0; i < elements.size(); i++) {
				String href = elements.get(i).attr("data-url");
				list.add(href);
			}
			int index = randomGenerator.nextInt(list.size());
			String text = list.get(index);
			String url = "";
			
			if(text.startsWith("\\")){
				url = text.replaceFirst("\\\\", "http://");
			}
			if(text.startsWith("/")){
				url = text.replaceFirst("/", "https://pixabay.com/");
			}
			
			
			try {
				base64Picture = urlToBase64(url);
			} catch(Exception e){
				base64Picture = "";
			}
		} else {
			base64Picture = "";
		}
		
		if(query.equals("heavy intensity rain weather")){
			return "https://images.sciencedaily.com/2015/07/150708100619_1_900x600.jpg";
		}
		
		return base64Picture;

	}

	private String urlToBase64(String surl) throws IOException {
		URL url = new URL(surl);
		InputStream is = url.openStream();
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		int nRead;
		byte[] data = new byte[16384];

		while ((nRead = is.read(data, 0, data.length)) != -1) {
			buffer.write(data, 0, nRead);
		}

		buffer.flush();

		String imageExtension = surl.substring(surl.lastIndexOf(".") + 1);

		return "data:image/" + imageExtension + ";base64," + new String(Base64.getEncoder().encode(buffer.toByteArray()));
	}

	private void disableSSLCertCheck() throws NoSuchAlgorithmException, KeyManagementException {
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(X509Certificate[] certs, String authType) {
			}

			public void checkServerTrusted(X509Certificate[] certs, String authType) {
			}
		} };

		// Install the all-trusting trust manager
		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, new java.security.SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

		// Create all-trusting host name verifier
		HostnameVerifier allHostsValid = new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};

		// Install the all-trusting host verifier
		HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
	}
}
