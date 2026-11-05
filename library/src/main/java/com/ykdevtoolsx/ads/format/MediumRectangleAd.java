package com.ykdevtoolsx.ads.format;

import static com.ykdevtoolsx.ads.util.Constant.ADMOB;
import static com.ykdevtoolsx.ads.util.Constant.AD_STATUS_ON;
import static com.ykdevtoolsx.ads.util.Constant.FAN_BIDDING_ADMOB;
import static com.ykdevtoolsx.ads.util.Constant.FAN_BIDDING_AD_MANAGER;
import static com.ykdevtoolsx.ads.util.Constant.GOOGLE_AD_MANAGER;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.admanager.AdManagerAdView;
import com.ykdevtoolsx.ads.R;
import com.ykdevtoolsx.ads.util.Tools;

public class MediumRectangleAd {

    public static class Builder {

        private static final String TAG = "AdNetwork";
        private final Activity activity;
        private AdView adView;
        private AdManagerAdView adManagerAdView;
        FrameLayout ironSourceBannerView;

        private String adStatus = "";
        private String adNetwork = "";
        private String backupAdNetwork = "";
        private String adMobBannerId = "";
        private String googleAdManagerBannerId = "";
        private String fanBannerId = "";
        private String unityBannerId = "";
        private String appLovinBannerId = "";
        private String appLovinBannerZoneId = "";
        private String mopubBannerId = "";
        private String ironSourceBannerId = "";
        private int placementStatus = 1;
        private boolean darkTheme = false;
        private boolean legacyGDPR = false;

        public Builder(Activity activity) {
            this.activity = activity;
        }

        public Builder build() {
            loadBannerAd();
            return this;
        }

        public Builder setAdStatus(String adStatus) {
            this.adStatus = adStatus;
            return this;
        }

        public Builder setAdNetwork(String adNetwork) {
            this.adNetwork = adNetwork;
            return this;
        }

        public Builder setBackupAdNetwork(String backupAdNetwork) {
            this.backupAdNetwork = backupAdNetwork;
            return this;
        }

        public Builder setAdMobBannerId(String adMobBannerId) {
            this.adMobBannerId = adMobBannerId;
            return this;
        }

        public Builder setGoogleAdManagerBannerId(String googleAdManagerBannerId) {
            this.googleAdManagerBannerId = googleAdManagerBannerId;
            return this;
        }

        public Builder setFanBannerId(String fanBannerId) {
            this.fanBannerId = fanBannerId;
            return this;
        }

        public Builder setUnityBannerId(String unityBannerId) {
            this.unityBannerId = unityBannerId;
            return this;
        }

        public Builder setAppLovinBannerId(String appLovinBannerId) {
            this.appLovinBannerId = appLovinBannerId;
            return this;
        }

        public Builder setAppLovinBannerZoneId(String appLovinBannerZoneId) {
            this.appLovinBannerZoneId = appLovinBannerZoneId;
            return this;
        }

        public Builder setMopubBannerId(String mopubBannerId) {
            this.mopubBannerId = mopubBannerId;
            return this;
        }

        public Builder setIronSourceBannerId(String ironSourceBannerId) {
            this.ironSourceBannerId = ironSourceBannerId;
            return this;
        }

        public Builder setPlacementStatus(int placementStatus) {
            this.placementStatus = placementStatus;
            return this;
        }

        public Builder setDarkTheme(boolean darkTheme) {
            this.darkTheme = darkTheme;
            return this;
        }

        public Builder setLegacyGDPR(boolean legacyGDPR) {
            this.legacyGDPR = legacyGDPR;
            return this;
        }

        public void loadBannerAd() {
            if (adStatus.equals(AD_STATUS_ON) && placementStatus != 0) {
                switch (adNetwork) {
                    case ADMOB:
                    case FAN_BIDDING_ADMOB:
                        FrameLayout adContainerView = activity.findViewById(R.id.admob_banner_view_container);
                        adContainerView.post(() -> {
                            adView = new AdView(activity);
                            adView.setAdUnitId(adMobBannerId);
                            adContainerView.removeAllViews();
                            adContainerView.addView(adView);
                            adView.setAdSize(Tools.getAdSizeMREC());
                            adView.loadAd(Tools.getAdRequest(activity, legacyGDPR));
                            adView.setAdListener(new AdListener() {
                                @Override
                                public void onAdLoaded() {
                                    // Code to be executed when an ad finishes loading.
                                    adContainerView.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                                    // Code to be executed when an ad request fails.
                                    adContainerView.setVisibility(View.GONE);
                                    loadBackupBannerAd();
                                }

                                @Override
                                public void onAdOpened() {
                                    // Code to be executed when an ad opens an overlay that
                                    // covers the screen.
                                }

                                @Override
                                public void onAdClicked() {
                                    // Code to be executed when the user clicks on an ad.
                                }

                                @Override
                                public void onAdClosed() {
                                    // Code to be executed when the user is about to return
                                    // to the app after tapping on an ad.
                                }
                            });
                        });
                        Log.d(TAG, adNetwork + " Banner Ad unit Id : " + adMobBannerId);
                        break;

                    case GOOGLE_AD_MANAGER:
                    case FAN_BIDDING_AD_MANAGER:
                        FrameLayout googleAdContainerView = activity.findViewById(R.id.google_ad_banner_view_container);
                        googleAdContainerView.post(() -> {
                            adManagerAdView = new AdManagerAdView(activity);
                            adManagerAdView.setAdUnitId(googleAdManagerBannerId);
                            googleAdContainerView.removeAllViews();
                            googleAdContainerView.addView(adManagerAdView);
                            adManagerAdView.setAdSize(Tools.getAdSizeMREC());
                            adManagerAdView.loadAd(Tools.getGoogleAdManagerRequest());
                            adManagerAdView.setAdListener(new AdListener() {
                                @Override
                                public void onAdClicked() {
                                    super.onAdClicked();
                                }

                                @Override
                                public void onAdClosed() {
                                    super.onAdClosed();
                                }

                                @Override
                                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                    super.onAdFailedToLoad(loadAdError);
                                    googleAdContainerView.setVisibility(View.GONE);
                                    loadBackupBannerAd();
                                }

                                @Override
                                public void onAdImpression() {
                                    super.onAdImpression();
                                }

                                @Override
                                public void onAdLoaded() {
                                    super.onAdLoaded();
                                    googleAdContainerView.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onAdOpened() {
                                    super.onAdOpened();
                                }
                            });
                        });
                        break;

                    default:
                        break;
                }
                Log.d(TAG, "Banner Ad is enabled");
            } else {
                Log.d(TAG, "Banner Ad is disabled");
            }
        }

        public void loadBackupBannerAd() {
            if (adStatus.equals(AD_STATUS_ON) && placementStatus != 0) {
                switch (backupAdNetwork) {
                    case ADMOB:
                    case FAN_BIDDING_ADMOB:
                        FrameLayout adContainerView = activity.findViewById(R.id.admob_banner_view_container);
                        adContainerView.post(() -> {
                            adView = new AdView(activity);
                            adView.setAdUnitId(adMobBannerId);
                            adContainerView.removeAllViews();
                            adContainerView.addView(adView);
                            adView.setAdSize(Tools.getAdSizeMREC());
                            adView.loadAd(Tools.getAdRequest(activity, legacyGDPR));
                            adView.setAdListener(new AdListener() {
                                @Override
                                public void onAdLoaded() {
                                    // Code to be executed when an ad finishes loading.
                                    adContainerView.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                                    // Code to be executed when an ad request fails.
                                    adContainerView.setVisibility(View.GONE);
                                }

                                @Override
                                public void onAdOpened() {
                                    // Code to be executed when an ad opens an overlay that
                                    // covers the screen.
                                }

                                @Override
                                public void onAdClicked() {
                                    // Code to be executed when the user clicks on an ad.
                                }

                                @Override
                                public void onAdClosed() {
                                    // Code to be executed when the user is about to return
                                    // to the app after tapping on an ad.
                                }
                            });
                        });
                        Log.d(TAG, adNetwork + " Banner Ad unit Id : " + adMobBannerId);
                        break;

                    case GOOGLE_AD_MANAGER:
                    case FAN_BIDDING_AD_MANAGER:
                        FrameLayout googleAdContainerView = activity.findViewById(R.id.google_ad_banner_view_container);
                        googleAdContainerView.post(() -> {
                            adManagerAdView = new AdManagerAdView(activity);
                            adManagerAdView.setAdUnitId(googleAdManagerBannerId);
                            googleAdContainerView.removeAllViews();
                            googleAdContainerView.addView(adManagerAdView);
                            adManagerAdView.setAdSize(Tools.getAdSizeMREC());
                            adManagerAdView.loadAd(Tools.getGoogleAdManagerRequest());
                            adManagerAdView.setAdListener(new AdListener() {
                                @Override
                                public void onAdClicked() {
                                    super.onAdClicked();
                                }

                                @Override
                                public void onAdClosed() {
                                    super.onAdClosed();
                                }

                                @Override
                                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                    super.onAdFailedToLoad(loadAdError);
                                    googleAdContainerView.setVisibility(View.GONE);
                                }

                                @Override
                                public void onAdImpression() {
                                    super.onAdImpression();
                                }

                                @Override
                                public void onAdLoaded() {
                                    super.onAdLoaded();
                                    googleAdContainerView.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onAdOpened() {
                                    super.onAdOpened();
                                }
                            });
                        });
                        break;

                    default:
                        break;
                }
                Log.d(TAG, "Banner Ad is enabled");
            } else {
                Log.d(TAG, "Banner Ad is disabled");
            }
        }

        public void destroyAndDetachBanner() {

        }

    }

}
