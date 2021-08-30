# eggy example Android (Kotlin) application

This is a tiny Kotlin application to demonstrate how eggy's Android SDK works.

Build steps are provided below, but for comprehensive instructions, please visit [the eggy docs on push notification integration](https://docs.useeggy.com/push-integration/android).

## How to build this

1. Make sure you have Android Studio installed.
2. Open the `hello-eggy-android-kotlin` directory in Android Studio.
3. Run the application from Android Studio.

## To test this with Firebase and your real eggy account

4. Create a Firebase project, add a `google-services.json` to the root of the `hello-eggy-android-kotlin` directory, and set your Firebase sender id in `res/values/strings.xml`.
5. Make an app in the eggy console (from [Apps](https://useeggy.com/apps)), generate an API token, and copy the API token from your App detail page. Use this token to set `apiToken` in `MainActivity.kt`.
