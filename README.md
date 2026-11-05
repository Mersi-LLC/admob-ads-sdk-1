# YKDevTools Ads SDK

An Android Java library that provides one builder-style API for Google AdMob and Google Ad Manager ads.

The repository contains two modules:

- `library`: reusable ads library (`com.ykdevtoolsx.ads`)
- `demo`: working example application (`com.ykdevtools.ads.sdkdemo`)

## Features

- Adaptive and collapsible banner
- Medium rectangle banner
- Interstitial
- Rewarded
- Native
- App open
- GDPR consent through Google User Messaging Platform (UMP)

The current implementation uses the legacy Google Mobile Ads dependency `com.google.android.gms:play-services-ads:24.4.0`. It does not use the GMA Next-Gen SDK.

## Release notes

### 1.0.0

- Updated Google Play services Ads SDK to 24.4.0.
- Updated Google UMP SDK to 3.2.0 for GDPR consent handling.
- Updated the minimum Android SDK level to API 23.
- Updated the project for current Android Studio compatibility.

## Requirements

- Android minSdk 23+
- compileSdk 34
- Java 8
- AndroidX

## Run the demo

Open the repository in Android Studio, select the `demo` configuration, and run it on a device or emulator.

To build from PowerShell:

```powershell
.\gradlew.bat :demo:assembleDebug
```

Output:

```text
demo/build/outputs/apk/debug/demo-debug.apk
```

The demo uses Google test ads. Do not use live ad units while repeatedly testing or clicking ads.

## Install the library

### JitPack dependency

After this repository is published on GitHub and a release tag is available through JitPack, add the JitPack repository to `settings.gradle`:

```gradle
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}
```

Add the SDK to your app module's `build.gradle`:

```gradle
dependencies {
    implementation 'com.github.ykdevtools:admob-ads-sdk:1.0.0'
}
```

Use a fixed release version such as `1.0.0`. Dynamic versions such as `1.+` are not recommended because a future release could change the resolved dependency without changing your application code.

The JitPack dependency becomes available only after:

1. The project is pushed to the `ykdevtools/admob-ads-sdk` GitHub repository.
2. Maven publishing is configured for the Android library.
3. A Git tag such as `1.0.0` is created and pushed.
4. JitPack successfully builds that tag.

### Module dependency

Copy `library` into your Android project and add it to `settings.gradle`:

```gradle
include ':app'
include ':library'
```

In your app module:

```gradle
dependencies {
    implementation project(':library')
}
```

### AAR dependency

Build the AAR:

```powershell
.\gradlew.bat :library:assembleRelease
```

Copy `library/build/outputs/aar/library-release.aar` to `app/libs`, then add:

```gradle
dependencies {
    implementation files('libs/library-release.aar')
    implementation 'com.google.android.gms:play-services-ads:24.4.0'
    implementation 'com.google.android.ump:user-messaging-platform:3.2.0'
    implementation 'androidx.appcompat:appcompat:1.2.0'
    implementation 'com.google.android.material:material:1.3.0'
}
```

## Configure the application ID

Add your Google Mobile Ads application ID inside the manifest `<application>` element:

```xml
<meta-data
    android:name="com.google.android.gms.ads.APPLICATION_ID"
    android:value="ca-app-pub-XXXXXXXXXXXXXXXX~YYYYYYYYYY" />
```

This must be an application ID, not an ad-unit ID. The demo reads `@string/admob_app_id` from `demo/src/main/res/values/ads.xml`.

## Initialize ads

```java
import com.ykdevtoolsx.ads.format.AdNetwork;

AdNetwork.Initialize ads = new AdNetwork.Initialize(this)
        .setAdStatus("1")
        .setAdNetwork("admob")
        .setBackupAdNetwork("none")
        .setAdMobAppId(null) // Use the manifest APPLICATION_ID
        .setDebug(BuildConfig.DEBUG)
        .build();
```

Prefer the library constants:

```java
import static com.ykdevtoolsx.ads.util.Constant.ADMOB;
import static com.ykdevtoolsx.ads.util.Constant.GOOGLE_AD_MANAGER;
```

## Banner

Add the supplied banner view to your layout container:

```java
LinearLayout bannerContainer = findViewById(R.id.banner_ad_view);
bannerContainer.addView(
        View.inflate(this, com.ykdevtoolsx.ads.R.layout.view_banner_ad, null)
);
```

Load it:

```java
import com.ykdevtoolsx.ads.format.BannerAd;

BannerAd.Builder bannerAd = new BannerAd.Builder(this)
        .setAdStatus("1")
        .setAdNetwork(ADMOB)
        .setBackupAdNetwork("none")
        .setAdMobBannerId("ca-app-pub-3940256099942544/6300978111")
        .setGoogleAdManagerBannerId("/21775744923/example/adaptive-banner")
        .setDarkTheme(false)
        .build(false);
```

Use `build(true)` only for an AdMob collapsible banner.

## Interstitial

Create and preload the builder before enabling the show button:

```java
import com.ykdevtoolsx.ads.format.InterstitialAd;

InterstitialAd.Builder interstitialAd = new InterstitialAd.Builder(this)
        .setAdStatus("1")
        .setAdNetwork(ADMOB)
        .setBackupAdNetwork("none")
        .setAdMobInterstitialId("ca-app-pub-3940256099942544/1033173712")
        .setGoogleAdManagerInterstitialId("/21775744923/example/interstitial")
        .setInterval(1)
        .build(() -> {
            // Ad dismissed.
        });
```

Show it at a natural transition:

```java
interstitialAd.show(
        () -> { /* shown */ },
        () -> { /* dismissed */ }
);
```

Do not call `show()` before calling `build()`.

## Rewarded

Preload the rewarded ad:

```java
import com.ykdevtoolsx.ads.format.RewardedAd;

RewardedAd.Builder rewardedAd = new RewardedAd.Builder(this)
        .setAdStatus("1")
        .setMainAds(ADMOB)
        .setBackupAds("none")
        .setAdMobRewardedId("ca-app-pub-3940256099942544/5224354917")
        .setAdManagerRewardedId("/21775744923/example/rewarded")
        .build(
                () -> { /* reward callback */ },
                () -> { /* dismissed */ }
        );
```

Show it and grant the reward only from the completion callback:

```java
rewardedAd.show(
        () -> { /* grant reward */ },
        () -> { /* dismissed */ },
        () -> { /* unavailable or failed */ }
);
```

Rewarded video needs access to Google's video CDN. A timeout for `*.gvt1.com` or `*.googlevideo.com` means the device network, DNS, VPN, firewall, or ad blocker prevented video caching.

## Native

Inflate a supplied native layout:

```java
LinearLayout nativeContainer = findViewById(R.id.native_ad);
nativeContainer.addView(
        View.inflate(this, com.ykdevtoolsx.ads.R.layout.view_native_ad_medium, null)
);
```

Load the ad:

```java
import com.ykdevtoolsx.ads.format.NativeAd;

NativeAd.Builder nativeAd = new NativeAd.Builder(this)
        .setAdStatus("1")
        .setAdNetwork(ADMOB)
        .setBackupAdNetwork("none")
        .setAdMobNativeId("ca-app-pub-3940256099942544/2247696110")
        .setAdManagerNativeId("/21775744923/example/native")
        .setNativeAdStyle("default")
        .setDarkTheme(false)
        .setPadding(0, 0, 0, 0)
        .build();
```

Demo styles: `default`, `news`, `radio`, `video_small`, and `video_large`.

## Google Ad Manager

Select Ad Manager before creating every format builder:

```java
String selectedNetwork = GOOGLE_AD_MANAGER;
```

Google test units used by the demo:

```text
/21775744923/example/adaptive-banner
/21775744923/example/interstitial
/21775744923/example/rewarded
/21775744923/example/native
/21775744923/example/app-open
```

Replace them with your own slash-prefixed Ad Manager units before release.

When changing networks at runtime, recreate or rebuild every ad builder. Existing builders keep the network selected when they were created. The demo chooser changes `Constant.AD_NETWORK` and recreates `MainActivity` for this reason.

## GDPR consent

```java
import com.ykdevtoolsx.ads.gdpr.GDPR;

GDPR gdpr = new GDPR(this);
gdpr.updateGDPRConsentStatus(ADMOB, false, false);
```

Review and adapt this flow for your app, region, privacy policy, and production requirements.

## Before publishing

1. Replace the sample application ID.
2. Replace all test ad-unit IDs with production units.
3. Confirm GDPR/UMP behavior on a clean installation.
4. Test a release build on physical devices.
5. Never click production ads during testing.

## Troubleshooting

### `Ad failed to load: 3` or `No fill`

The request reached the server, but no eligible creative was returned. Verify the unit format and use the current test units during development.

### Rewarded ad reports an error

Wait for preload before calling `show()`. If logcat reports a `SocketTimeoutException` for `gvt1.com` or `googlevideo.com`, change networks or disable the blocking DNS, VPN, firewall, or ad blocker.

### Button causes `NullPointerException`

The builder was never initialized. Call the matching load method during `onCreate()` before allowing the user to press the show button.

### AdMob works but Google Ad Manager does not

Confirm `GOOGLE_AD_MANAGER` was selected before all builders were created and that units use `/network-code/path` format.

## Package names

- Library: `com.ykdevtoolsx.ads`
- Demo: `com.ykdevtools.ads.sdkdemo`

See `demo/src/main/java/com/ykdevtools/ads/sdkdemo/activity/MainActivity.java` for the complete implementation.
