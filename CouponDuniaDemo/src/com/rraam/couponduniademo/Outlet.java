package com.rraam.couponduniademo;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.List;

import org.apache.http.util.ByteArrayBuffer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

public class Outlet {
	public int OutletId;
	public String OutletName;
	public String NeighbourhoodName;
	public double Latitude;
	public double Longitude;
	public URI LogoURL;
	public int NumCoupons;
	public double Distance;
	public List<String> Categories;
	public Bitmap logoImage;
	public ListAdapter listAdapter;

	public Outlet() {

	}

	public Outlet(int OutLetId, String OutletName, String NeighbourhoodName,
			double Latitude, double Longitude, URI LogoURL, int NumCoupons,
			double Distance, List<String> Categories) {
		this.OutletId = OutLetId;
		this.OutletName = OutletName;
		this.NeighbourhoodName = NeighbourhoodName;
		this.Latitude = Latitude;
		this.Longitude = Longitude;
		this.LogoURL = LogoURL;
		this.NumCoupons = NumCoupons;
		this.Distance = Distance;
		this.Categories = Categories;
	}

	public int getOutletId() {
		return OutletId;
	}

	public void setOutletId(int outletId) {
		OutletId = outletId;
	}

	public String getOutletName() {
		return OutletName;
	}

	public void setOutletName(String outletName) {
		OutletName = outletName;
	}

	public String getNeighbourhoodName() {
		return NeighbourhoodName;
	}

	public void setNeighbourhoodName(String neighbourhoodName) {
		NeighbourhoodName = neighbourhoodName;
	}

	public double getLatitude() {
		return Latitude;
	}

	public void setLatitude(double latitude) {
		Latitude = latitude;
	}

	public double getLongitude() {
		return Longitude;
	}

	public void setLongitude(double longitude) {
		Longitude = longitude;
	}

	public URI getLogoURL() {
		return LogoURL;
	}

	public void setLogoURL(URI logoURL) {
		LogoURL = logoURL;
	}

	public int getNumCoupons() {
		return NumCoupons;
	}

	public void setNumCoupons(int numCoupons) {
		NumCoupons = numCoupons;
	}

	public double getDistance() {
		return Distance;
	}

	public void setDistance(double distance) {
		Distance = distance;
	}

	public List<String> getCategories() {
		return Categories;
	}

	public void setCategories(List<String> categories) {
		Categories = categories;
	}

	public Bitmap getLogoImage() {
		return logoImage;
	}

	public void loadLogoImage(ListAdapter listAdapter) {
		this.listAdapter = listAdapter;
		if (LogoURL != null && !LogoURL.equals("")) {
			new ImageLoad().execute(LogoURL.toString());
		}
	}

	public class ImageLoad extends AsyncTask<String, String, byte[]> {
		@Override
		protected byte[] doInBackground(String... aurl) {
			try {
				URL yourl = new URL(aurl[0]);
				HttpURLConnection connection = (HttpURLConnection) yourl.openConnection();
				connection.setDoInput(true);
				connection.connect();
				InputStream input = connection.getInputStream();
				BufferedInputStream bis = new BufferedInputStream(input);
				ByteArrayBuffer baf = new ByteArrayBuffer(1024);
				int line;
				while ((line = bis.read()) != -1) {
					baf.append((byte) line);
				}
				bis.close();
				byte[] ret = baf.toByteArray();
				return ret;
			} catch (Exception e) {
				return null;
			}
		}

		@Override
		protected void onPostExecute(byte[] bitmap) {
			if (bitmap != null) {
				try {
					logoImage = BitmapFactory.decodeByteArray(bitmap, 0, bitmap.length);
					if (listAdapter != null) {
	                    listAdapter.notifyDataSetChanged();
	                }
				} catch (Exception er) {
				}
			}
		}
	}
}
