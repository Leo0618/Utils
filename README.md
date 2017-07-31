# Utils #

Utils for Android Developer.


## Usage

**Step1:**

	dependencies {
	    compile 'com.leo618.utils:utils:1.0.0'
	}



**Step3:**

    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.READ_PHONE_STATE" />



**Step2:**

install in your application or which extends application at method onCreate.

	@Override
    public void onCreate() {
        super.onCreate();
        AndroidUtilsCore.install(this);
    }

 
and call method `initAfterCheckPermissions` after check permissions in SplashActivity.

enjoy!!!


Utils with :


	└─Utils
		--BuildUtils
		--ClipboardUtil
		--CrashHandler
		--DateTimeUtil
		--DeviceInfo
		--FileStorageUtil
		--ImageUtil
		--IntentLauncher
		--KeyBoardUtil
		--LogUtil
		--NetworkUtil
		--NumberUtil
		--PackageManagerUtil
		--ReflectUtil
		--ScreenUtil
		--SignUtil
		--SPUtil
		--StringUtil
		--UIUtil
		--ViewHolderUtil

