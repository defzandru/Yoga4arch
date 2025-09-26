Yoga4archacademy - Android project (JWT-enabled)
---------------------------------------------

This project is a minimal Android app skeleton that demonstrates login to a WordPress site using JWT.
It includes:
  - LoginActivity (POST /wp-json/jwt-auth/v1/token)
  - MyAppPrefs to store token and expiry
  - MainActivity to fetch /wp/v2/users/me using Bearer token
  - SplashActivity to check token presence and expiry

To use:
1. Update LOGIN_URL and USER_URL in Java files if your WP domain differs.
2. Ensure your WordPress has "JWT Authentication for WP REST API" plugin configured and active.
3. Build with Android Studio (set Java compatibility to 11).

