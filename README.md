# Festival Management System

O aplicație backend pentru gestionarea festivalurilor muzicale, dezvoltată cu Spring Boot și MySQL.

## Ce face aplicația?

Aplicația permite utilizatorilor să:

🎵 **Vizualizeze și gestioneze festivaluri** - Lista festivalurilor, adăugare imagini, informații despre prețuri și locații

👥 **Se înregistreze și autentifice** - Sistem de utilizatori cu roluri (USER/ADMIN) și protecție împotriva atacurilor

💳 **Cumpere bilete** - Integrare cu Stripe pentru plăți online și gestionarea participărilor

🧠 **Primească recomandări** - Quiz interactiv care determină genul muzical preferat și oferă festivaluri personalizate

💬 **Chat în grupuri** - Comunicare între participanții la festivaluri

🗺️ **Vizualizeze hărți** - Puncte GPS pentru fiecare festival cu informații detaliate

## Tehnologii

- **Java 17** + **Spring Boot**
- **MySQL** pentru baza de date
- **Stripe** pentru plăți
- **Spring Security** pentru autentificare
-  **Vue** pentru frontend
-  **Leafet.js** pentru harti

## API Principal

- `GET /festivals` - Lista festivaluri
- `POST /users/register` - Înregistrare
- `POST /users/login` - Autentificare  
- `POST /api/payment/checkout-session` - Cumpărare bilete
- `GET /api/quiz/questions` - Quiz recomandări

---

Dezvoltat ca parte a lucrării de licență.
