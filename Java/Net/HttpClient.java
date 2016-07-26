import java.net.*;
import java.io.*;

import org.apache.http.*;
import org.apache.http.client.*;
import org.apache.http.client.methods.*;
import org.apache.http.client.methods.HttpGet;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;


public class HttpClient {
	public static void main(String[] args) throws java.lang.Exception {
		BufferedReader in = null;
		String data = "";

		try{
			HttpClient httpclient = new DefaultHttpClient();

			HttpGet request = new HttpGet();
			URI website = new URI("http://alanhardin.comyr.com/matt24/matt28.php");
			request.setURI(website);
			HttpResponse response = httpclient.execute(request);
			in = new BufferedReader(new InputStreamReader(
				response.getEntity().getContent())
			);

			String line = in.readLine();
			data += "line";

		}catch(Exception e){
			System.out.writeLine("Error in http connection "+e.toString());
		}
	}
}


