# Employee Feedback Management Application

Desktop aplikacija razvijena u JavaFX-u za upravljanje zaposlenicima, feedbackom i performance review procesima. Projekt je izrađen u sklopu fakultetskog zadatka s fokusom na objektno orijentirano programiranje, rad s bazom podataka i izradu desktop aplikacije.

## Opis

Aplikacija omogućuje rad s više entiteta unutar sustava, uključujući zaposlenike, korisnike, feedback i performance review zapise. Implementirane su različite razine korisnika, uključujući administratorsku i korisničku razinu, s odvojenim funkcionalnostima prema ulozi korisnika.

## Funkcionalnosti

* CRUD operacije nad zaposlenicima
* CRUD operacije nad feedback zapisima
* Upravljanje performance review zapisima
* Administratorska i korisnička razina pristupa
* Upravljanje korisničkim podacima
* Grafičko korisničko sučelje izrađeno u JavaFX-u
* Validacija korisničkog unosa
* Rad s lokalnom H2 bazom podataka

## Tehnologije

* Java
* JavaFX
* H2 Database
* JDBC
* IntelliJ IDEA

## Baza podataka

Aplikacija koristi lokalnu H2 bazu podataka.

### Konfiguracija baze

* URL: `jdbc:h2:tcp://localhost/~/projekt`
* Username: `projekt`
* Password: `projekt`

## Pokretanje aplikacije

### Preduvjeti

* Java JDK
* IntelliJ IDEA ili drugi Java IDE
* H2 Database Engine

### Koraci

1. Pokrenuti H2 server, npr. putem `h2.bat` ili H2 Console aplikacije.
2. Provjeriti da je H2 server dostupan lokalno.
3. Otvoriti projekt u IntelliJ IDEA.
4. Provjeriti konfiguraciju baze u `database.properties`.
5. Pokrenuti glavnu klasu aplikacije.

## Napomena

Projekt je razvijen kao fakultetski zadatak i nije namijenjen produkcijskom korištenju. H2 baza koristi se lokalno za potrebe razvoja i testiranja.

## Autor

Filip Franić
https://github.com/ffranic
