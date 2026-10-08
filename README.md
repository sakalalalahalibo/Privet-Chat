# Privet-Chat

تطبيق دردشة Android مبني باستخدام Kotlin و Jetpack Compose و Firebase، ويهدف إلى تمكين الأصدقاء من التواصل بشكل سريع وسهل عبر واجهة حديثة ومريحة.

## نبذة عن المشروع

Privet-Chat هو تطبيق دردشة جماعية/فردية مصمم لتجربة مستخدم سلسة، مع دعم:

- تسجيل الدخول والتسجيل باستخدام البريد الإلكتروني
- إرسال واستقبال الرسائل في الوقت الفعلي
- إدارة المستخدمين والرسائل عبر Firebase
- واجهة حديثة باستخدام Material 3 و Compose

## التقنيات المستخدمة

- Kotlin
- Android Jetpack Compose
- Firebase Authentication
- Firebase Firestore
- Gradle with Kotlin DSL
- Material 3

## بنية المشروع

```text
Privet-Chat/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/friends/chat/
│   │   │   │       ├── ui/               # مكونات الواجهة والتعامل مع الشاشة
│   │   │   │       ├── viewmodel/        # منطق التطبيق والحالة
│   │   │   │       ├── model/            # نماذج البيانات
│   │   │   │       ├── repository/       # التعامل مع Firebase
│   │   │   │       └── utils/            # أدوات مساعدة
│   │   │   └── res/
│   │   │       ├── drawable/
│   │   │       ├── values/
│   │   │       ├── xml/
│   │   │       └── mipmap/
│   │   ├── androidTest/
│   │   └── test/
│   ├── build.gradle.kts
│   └── google-services.json
├── .gradle/
├── .settings/
├── .project
├── build/
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
├── firestore.rules
├── README.md
├── README_AR.md
└── .gitignore
```

## ملفات التكوين الرئيسية

### 1) settings.gradle.kts

يحدد اسم المشروع والوحدات المضمنة في التطبيق:

```kotlin
rootProject.name = "FriendsChat"
include(":app")
```

### 2) build.gradle.kts

يحتوي على الإضافات الأساسية الخاصة بـ Gradle:

```kotlin
plugins {
    id("com.android.application") version "8.5.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.24" apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
}
```

### 3) app/build.gradle.kts

يحتوي إعدادات التطبيق الأساسية، بما فيها:

- اسم الحزمة: `com.friends.chat`
- `compileSdk = 34`
- `minSdk = 24`
- `targetSdk = 34`
- استخدام Compose
- الاعتماد على Firebase Auth و Firestore

```kotlin
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.friends.chat"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.friends.chat"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures { compose = true }
    composeOptions { kotlinCompilerExtensionVersion = "1.5.14" }
}
```

## المتطلبات

قبل تشغيل المشروع تأكد من توفر:

- Android Studio
- JDK 17+
- Google Firebase account
- اتصال بالإنترنت أثناء أول تشغيل

## خطوات التشغيل

1. افتح المشروع في Android Studio.
2. انتظر حتى يكتمل Gradle sync.
3. أنشئ مشروع Firebase جديد في Firebase Console.
4. أضف تطبيق Android باسم الحزمة:
   `com.friends.chat`
5. حمل ملف `google-services.json` وضعه داخل مجلد `app/`.
6. فعّل الخدمات التالية في Firebase:
   - Authentication → Email/Password
   - Firestore Database
7. افتح ملف `firestore.rules` وقم بتعديله حسب بريدك الإلكتروني أو احتياجاتك، ثم Publish.
8. شغّل التطبيق على جهاز حقيقي أو محاكي.

## Firebase والأمان

المشروع يعتمد على Firebase بشكل أساسي في:

- تسجيل المستخدمين
- تخزين الرسائل
- مزامنة البيانات في الوقت الحقيقي

ويستخدم ملف `firestore.rules` لتحديد من يمكنه الوصول إلى البيانات.

## ملاحظات مهمة

- إذا ظهر خطأ يتطلب إنشاء فهرس في Firestore، اضغط على الرابط الذي يظهر في Logcat لإنشاء الفهرس.
- تأكد أن `google-services.json` موجود داخل مجلد `app` قبل التشغيل.
- قد تحتاج إلى تشغيل قاعدة البيانات في وضع الاختبار أولاً عند التجربة الأولى.

## الخلاصة

هذا المشروع يمثل تطبيق دردشة Android حديث باستخدام Firebase و Compose، ويعد مثالاً جيداً على بناء تطبيق ذكي ومتمازج مع خدمات السحابة. 

إذا رغبت، أستطيع أيضًا أن أضيف لك:

- وصفًا تفصيليًا لكل ملف داخل `app/src/main`
- ترتيب لتنظيم الكود داخل مجلدات مثل `ui`, `viewmodel`, `repository`
- README باللغة الإنجليزية + العربية مع تصميم احترافي
- قائمة بمقترحات التطوير المستقبلية للمشروع