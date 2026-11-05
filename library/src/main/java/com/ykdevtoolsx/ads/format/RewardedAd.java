package com.ykdevtoolsx.ads.format;

import static com.ykdevtoolsx.ads.util.Constant.ADMOB;
import static com.ykdevtoolsx.ads.util.Constant.AD_STATUS_ON;
import static com.ykdevtoolsx.ads.util.Constant.FAN_BIDDING_ADMOB;
import static com.ykdevtoolsx.ads.util.Constant.FAN_BIDDING_AD_MANAGER;
import static com.ykdevtoolsx.ads.util.Constant.GOOGLE_AD_MANAGER;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.ykdevtoolsx.ads.util.OnRewardedAdCompleteListener;
import com.ykdevtoolsx.ads.util.OnRewardedAdDismissedListener;
import com.ykdevtoolsx.ads.util.OnRewardedAdErrorListener;
import com.ykdevtoolsx.ads.util.Tools;

public class RewardedAd {

    @SuppressWarnings("deprecation")
    public static class Builder {

        private static final String TAG = "SoloRewarded";
        private final Activity activity;
        private com.google.android.gms.ads.rewarded.RewardedAd adMobRewardedAd;
        private com.google.android.gms.ads.rewarded.RewardedAd adManagerRewardedAd;
        private String adStatus = "";
        private String mainAds = "";
        private String backupAds = "";
        private String adMobRewardedId = "";
        private String adManagerRewardedId = "";
        private String fanRewardedId = "";
        private String unityRewardedId = "";
        private String applovinMaxRewardedId = "";
        private String applovinDiscRewardedZoneId = "";
        private String ironSourceRewardedId = "";
        private String wortiseRewardedId = "";
        private String alienAdsRewardedId = "";
        private int placementStatus = 1;
        private boolean legacyGDPR = false;

        public Builder(Activity activity) {
            this.activity = activity;
        }

        public Builder build(OnRewardedAdCompleteListener onComplete, OnRewardedAdDismissedListener onDismiss) {
            loadRewardedAd(onComplete, onDismiss);
            return this;
        }

        public Builder show(OnRewardedAdCompleteListener onComplete, OnRewardedAdDismissedListener onDismiss, OnRewardedAdErrorListener onError) {
            showRewardedAd(onComplete, onDismiss, onError);
            return this;
        }

        public Builder setAdStatus(String adStatus) {
            this.adStatus = adStatus;
            return this;
        }

        public Builder setMainAds(String mainAds) {
            this.mainAds = mainAds;
            return this;
        }

        public Builder setBackupAds(String backupAds) {
            this.backupAds = backupAds;
            return this;
        }

        public Builder setAdMobRewardedId(String adMobRewardedId) {
            this.adMobRewardedId = adMobRewardedId;
            return this;
        }

        public Builder setAdManagerRewardedId(String adManagerRewardedId) {
            this.adManagerRewardedId = adManagerRewardedId;
            return this;
        }

        public Builder setFanRewardedId(String fanRewardedId) {
            this.fanRewardedId = fanRewardedId;
            return this;
        }

        public Builder setUnityRewardedId(String unityRewardedId) {
            this.unityRewardedId = unityRewardedId;
            return this;
        }

        public Builder setApplovinMaxRewardedId(String applovinMaxRewardedId) {
            this.applovinMaxRewardedId = applovinMaxRewardedId;
            return this;
        }

        public Builder setApplovinDiscRewardedZoneId(String applovinDiscRewardedZoneId) {
            this.applovinDiscRewardedZoneId = applovinDiscRewardedZoneId;
            return this;
        }

        public Builder setIronSourceRewardedId(String ironSourceRewardedId) {
            this.ironSourceRewardedId = ironSourceRewardedId;
            return this;
        }

        public Builder setWortiseRewardedId(String wortiseRewardedId) {
            this.wortiseRewardedId = wortiseRewardedId;
            return this;
        }

        public Builder setAlienAdsRewardedId(String alienAdsRewardedId) {
            this.alienAdsRewardedId = alienAdsRewardedId;
            return this;
        }

        public Builder setPlacementStatus(int placementStatus) {
            this.placementStatus = placementStatus;
            return this;
        }

        public Builder setLegacyGDPR(boolean legacyGDPR) {
            this.legacyGDPR = legacyGDPR;
            return this;
        }

        public void loadRewardedAd(OnRewardedAdCompleteListener onComplete, OnRewardedAdDismissedListener onDismiss) {
            if (adStatus.equals(AD_STATUS_ON) && placementStatus != 0) {
                switch (mainAds) {
                    case ADMOB:
                    case FAN_BIDDING_ADMOB:
                        com.google.android.gms.ads.rewarded.RewardedAd.load(activity, adMobRewardedId, Tools.getAdRequest(activity, legacyGDPR), new RewardedAdLoadCallback() {
                            @Override
                            public void onAdLoaded(@NonNull com.google.android.gms.ads.rewarded.RewardedAd ad) {
                                adMobRewardedAd = ad;
                                adMobRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        super.onAdDismissedFullScreenContent();
                                        adMobRewardedAd = null;
                                        loadRewardedAd(onComplete, onDismiss);
                                        onDismiss.onRewardedAdDismissed();
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(@NonNull com.google.android.gms.ads.AdError adError) {
                                        super.onAdFailedToShowFullScreenContent(adError);
                                        adMobRewardedAd = null;
                                    }
                                });
                                Log.d(TAG, "[" + mainAds + "] " + "rewarded ad loaded");
                            }

                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                Log.d(TAG, loadAdError.toString());
                                adMobRewardedAd = null;
                                loadRewardedBackupAd(onComplete, onDismiss);
                                Log.d(TAG, "[" + mainAds + "] " + "failed to load rewarded ad: " + loadAdError.getMessage() + ", try to load backup ad: " + backupAds);
                            }
                        });
                        break;

                    case GOOGLE_AD_MANAGER:
                    case FAN_BIDDING_AD_MANAGER:
                        com.google.android.gms.ads.rewarded.RewardedAd.load(activity, adManagerRewardedId, Tools.getGoogleAdManagerRequest(), new RewardedAdLoadCallback() {
                            @Override
                            public void onAdLoaded(@NonNull com.google.android.gms.ads.rewarded.RewardedAd ad) {
                                adManagerRewardedAd = ad;
                                adManagerRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        super.onAdDismissedFullScreenContent();
                                        adManagerRewardedAd = null;
                                        loadRewardedAd(onComplete, onDismiss);
                                        onDismiss.onRewardedAdDismissed();
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(@NonNull com.google.android.gms.ads.AdError adError) {
                                        super.onAdFailedToShowFullScreenContent(adError);
                                        adManagerRewardedAd = null;
                                    }
                                });
                                Log.d(TAG, "[" + mainAds + "] " + "rewarded ad loaded");
                            }

                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                Log.d(TAG, loadAdError.toString());
                                adManagerRewardedAd = null;
                                loadRewardedBackupAd(onComplete, onDismiss);
                                Log.d(TAG, "[" + mainAds + "] " + "failed to load rewarded ad: " + loadAdError.getMessage() + ", try to load backup ad: " + backupAds);
                            }
                        });
                        break;

                    default:
                        break;
                }
            }
        }

        public void loadRewardedBackupAd(OnRewardedAdCompleteListener onComplete, OnRewardedAdDismissedListener onDismiss) {
            if (adStatus.equals(AD_STATUS_ON) && placementStatus != 0) {
                switch (backupAds) {
                    case ADMOB:
                    case FAN_BIDDING_ADMOB:
                        com.google.android.gms.ads.rewarded.RewardedAd.load(activity, adMobRewardedId, Tools.getAdRequest(activity, legacyGDPR), new RewardedAdLoadCallback() {
                            @Override
                            public void onAdLoaded(@NonNull com.google.android.gms.ads.rewarded.RewardedAd ad) {
                                adMobRewardedAd = ad;
                                adMobRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        super.onAdDismissedFullScreenContent();
                                        adMobRewardedAd = null;
                                        loadRewardedAd(onComplete, onDismiss);
                                        onDismiss.onRewardedAdDismissed();
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(@NonNull com.google.android.gms.ads.AdError adError) {
                                        super.onAdFailedToShowFullScreenContent(adError);
                                        adMobRewardedAd = null;
                                    }
                                });
                                Log.d(TAG, "[" + backupAds + "] [backup] " + "rewarded ad loaded");
                            }

                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                Log.d(TAG, loadAdError.toString());
                                adMobRewardedAd = null;
                                Log.d(TAG, "[" + backupAds + "] [backup] " + "failed to load rewarded ad: " + loadAdError.getMessage() + ", try to load backup ad: " + backupAds);
                            }
                        });
                        break;

                    case GOOGLE_AD_MANAGER:
                    case FAN_BIDDING_AD_MANAGER:
                        com.google.android.gms.ads.rewarded.RewardedAd.load(activity, adManagerRewardedId, Tools.getGoogleAdManagerRequest(), new RewardedAdLoadCallback() {
                            @Override
                            public void onAdLoaded(@NonNull com.google.android.gms.ads.rewarded.RewardedAd ad) {
                                adManagerRewardedAd = ad;
                                adManagerRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        super.onAdDismissedFullScreenContent();
                                        adManagerRewardedAd = null;
                                        loadRewardedAd(onComplete, onDismiss);
                                        onDismiss.onRewardedAdDismissed();
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(@NonNull com.google.android.gms.ads.AdError adError) {
                                        super.onAdFailedToShowFullScreenContent(adError);
                                        adManagerRewardedAd = null;
                                    }
                                });
                                Log.d(TAG, "[" + backupAds + "] [backup] " + "rewarded ad loaded");
                            }

                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                Log.d(TAG, loadAdError.toString());
                                adManagerRewardedAd = null;
                                Log.d(TAG, "[" + mainAds + "] " + "failed to load rewarded ad: " + loadAdError.getMessage() + ", try to load backup ad: " + backupAds);
                            }
                        });
                        break;

                    default:
                        break;
                }
            }
        }

        public void showRewardedAd(OnRewardedAdCompleteListener onComplete, OnRewardedAdDismissedListener onDismiss, OnRewardedAdErrorListener onError) {
            if (adStatus.equals(AD_STATUS_ON) && placementStatus != 0) {
                switch (mainAds) {
                    case ADMOB:
                    case FAN_BIDDING_ADMOB:
                        if (adMobRewardedAd != null) {
                            adMobRewardedAd.show(activity, rewardItem -> {
                                onComplete.onRewardedAdComplete();
                                Log.d(TAG, "The user earned the reward.");
                            });
                        } else {
                            showRewardedBackupAd(onComplete, onDismiss, onError);
                        }
                        break;

                    case GOOGLE_AD_MANAGER:
                    case FAN_BIDDING_AD_MANAGER:
                        if (adManagerRewardedAd != null) {
                            adManagerRewardedAd.show(activity, rewardItem -> {
                                onComplete.onRewardedAdComplete();
                                Log.d(TAG, "The user earned the reward.");
                            });
                        } else {
                            showRewardedBackupAd(onComplete, onDismiss, onError);
                        }
                        break;

                    default:
                        onError.onRewardedAdError();
                        break;
                }
            }

        }

        public void showRewardedBackupAd(OnRewardedAdCompleteListener onComplete, OnRewardedAdDismissedListener onDismiss, OnRewardedAdErrorListener onError) {
            if (adStatus.equals(AD_STATUS_ON) && placementStatus != 0) {
                switch (backupAds) {
                    case ADMOB:
                    case FAN_BIDDING_ADMOB:
                        if (adMobRewardedAd != null) {
                            adMobRewardedAd.show(activity, rewardItem -> {
                                onComplete.onRewardedAdComplete();
                                Log.d(TAG, "The user earned the reward.");
                            });
                        } else {
                            onError.onRewardedAdError();
                        }
                        break;

                    case GOOGLE_AD_MANAGER:
                    case FAN_BIDDING_AD_MANAGER:
                        if (adManagerRewardedAd != null) {
                            adManagerRewardedAd.show(activity, rewardItem -> {
                                onComplete.onRewardedAdComplete();
                                Log.d(TAG, "The user earned the reward.");
                            });
                        } else {
                            onError.onRewardedAdError();
                        }
                        break;

                    default:
                        onError.onRewardedAdError();
                        break;
                }
            }

        }

        public void destroyRewardedAd() {
        }

    }

}
