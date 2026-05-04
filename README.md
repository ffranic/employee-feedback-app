# Employee Feedback Management Application

Desktop aplikacija razvijena u JavaFX-u za upravljanje zaposlenicima, feedbackom i performance review procesima.

## Opis

Aplikacija omogućuje rad s više entiteta unutar sustava, uključujući zaposlenike, korisnike, feedback i performance review zapise. Implementirane su različite razine korisnika (admin i user) s odvojenim funkcionalnostima.

Projekt je razvijen s ciljem učenja rada s desktop aplikacijama, organizacije aplikacijske logike i integracije s bazom podataka.

## Funkcionalnosti

* CRUD operacije nad zaposlenicima, feedbackom i performance review zapisima
* Upravljanje korisnicima (admin i user razina pristupa)
* Grafičko korisničko sučelje (JavaFX)
* Validacija korisničkog unosa
* Rad s bazom podataka putem JDBC-a

## Tehnologije

* Java
* JavaFX
* H2 Database
* JDBC

## Baza podataka

Aplikacija koristi lokalnu H2 bazu.

### Konfiguracija

* URL: `jdbc:h2:tcp://localhost/~/projekt`
* Username: `projekt`
* Password: `projekt`

## Pokretanje aplikacije

### Preduvjeti

* Java JDK
* IntelliJ IDEA ili drugi Java IDE
* H2 Database Engine

### Koraci

1. Pokrenuti H2 server (npr. putem `h2.bat` ili H2 Console aplikacije)
2. Provjeriti da je server dostupan na `localhost`
3. Otvoriti projekt u IntelliJ IDEA
4. Provjeriti konfiguraciju baze u `database.properties`
5. Pokrenuti glavnu klasu aplikacije

## Napomena

Projekt je razvijen kao fakultetski zadatak i koristi lokalnu bazu za potrebe razvoja i testiranja.

## Autor

Filip Franić
https://github.com/ffranic
