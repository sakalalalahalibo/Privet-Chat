# دردشة الأصدقاء — خطوات التشغيل

1. افتح المجلد في Android Studio (File → Open) وانتظر مزامنة Gradle.
2. في https://console.firebase.google.com أنشئ مشروعاً، ثم أضف تطبيق Android
   باسم الحزمة: com.friends.chat
3. نزّل google-services.json وضعه داخل مجلد app/
4. من Build في Firebase فعّل:
   - Authentication → Email/Password
   - Firestore Database
5. في Firestore → Rules الصق محتوى ملف firestore.rules بعد تعديل الإيميلات، ثم Publish.
6. شغّل التطبيق على هاتفك أو المحاكي.

ملاحظة: Firestore قد يطلب إنشاء فهرس (index) لأول مرة — اضغط على الرابط الذي يظهر في Logcat.
