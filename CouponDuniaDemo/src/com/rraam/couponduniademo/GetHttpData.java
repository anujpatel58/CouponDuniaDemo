package com.rraam.couponduniademo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

public class GetHttpData {
	public String getData(URI web) throws Exception {
		BufferedReader in = null;
		String data = null;
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost req = new HttpPost();
			req.setURI(web);
			HttpResponse res = client.execute(req);
			in = new BufferedReader(new InputStreamReader(res.getEntity().getContent()));
			StringBuffer sb = new StringBuffer("");
			String l = "";
			while ((l = in.readLine()) != null) {
				sb.append(l);
			}
			in.close();
			data = sb.toString();
			return data;
		} finally {
			if (in != null) {
				try {
					in.close();
					return data;
				} catch (Exception er) {
				}
			}
		}
	}
}
