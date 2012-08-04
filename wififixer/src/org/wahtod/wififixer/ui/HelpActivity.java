/*Copyright [2010-2012] [David Van de Ven]

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */

package org.wahtod.wififixer.ui;

import org.wahtod.wififixer.R;
import org.wahtod.wififixer.legacy.ActionBarDetector;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class HelpActivity extends AppFragmentActivity {

	private static final String ASSET_INDEX = "file:///android_asset/index.html";
	private static final String CURRENT_URL = "CURRENT_URL";
	WebView webview;

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putString(CURRENT_URL, webview.getUrl());
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		ActionBarDetector.setDisplayHomeAsUpEnabled(this, true);
		setContentView(R.layout.help);
		webview = (WebView) findViewById(R.id.helpwebview);
		WebSettings websettings = webview.getSettings();
		webview.setWebViewClient(new HelpWebViewClient());
		websettings.setSavePassword(false);
		websettings.setSaveFormData(false);
		websettings.setJavaScriptEnabled(false);
		websettings.setSupportZoom(false);
		websettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		if (savedInstanceState != null
				&& savedInstanceState.containsKey(CURRENT_URL))
			webview.loadUrl(savedInstanceState.getString(CURRENT_URL));
		else
			webview.loadUrl(ASSET_INDEX);

		super.onCreate(savedInstanceState);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		ActionBarDetector.handleHome(this, item);
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
			webview.goBack();
			return true;
		} else

			return super.onKeyDown(keyCode, event);
	}

	private class HelpWebViewClient extends WebViewClient {
		private static final String CYANOGENMOD = "http://cyanogenmod.com";
		private static final String HTTP_STOP = "stop";
		private static final String BLOG = "http://wififixer.wordpress.com";
		private static final String EMAIL = "Email:";
		private static final String MAILMIME = "text/plain";
		private static final String MAILTO = "mailto:zanshin.g1@gmail.com";
		private static final String GOOGLECODE = "http://code.google.com/p/android/issues/detail?id=21469";

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			if (url.contains(MAILTO)) {
				Intent sendIntent = new Intent(Intent.ACTION_SEND);
				sendIntent.setType(MAILMIME);
				sendIntent
						.putExtra(Intent.EXTRA_EMAIL, new String[] { MAILTO });
				startActivity(Intent.createChooser(sendIntent, EMAIL));
				return false;
			} else if (url.contains(BLOG)) {
				displayExternalURI(BLOG);
				return false;
			} else if (url.contains(HTTP_STOP)) {
				HelpActivity.this.finish();
				return false;
			} else if (url.contains(CYANOGENMOD)) {
				displayExternalURI(CYANOGENMOD);
				return false;
			} else if (url.contains(GOOGLECODE)) {
				displayExternalURI(GOOGLECODE);
				return false;
			}
			view.loadUrl(url);
			return false;
		}

		private void displayExternalURI(final String uri) {
			Intent i = new Intent(Intent.ACTION_VIEW);
			Uri u = Uri.parse(uri);
			i.setData(u);
			startActivity(i);
		}

	}
}
