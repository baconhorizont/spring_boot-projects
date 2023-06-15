# ERP project

## Leírás

Az ERP gyártással foglalkozó vállalkozások számára nyújt segítséget az alkalmazottak, gyártóberendezések és megrendelések nyilvántartásában.
Az alkalmazottak a gépek és a megrendelések külön entitásként is kezelhetőek, de az alkalmazás kompetenciamátrix kialakítására és a megrendelt munkák nyomonkövetésére is alkalmas.

---

## Felépítés

### Employee

Az `Employee` (alkalmazott) entitás a következő attribútumokkal rendelkezik:

* `id`(Long) adatbázis által generált egyedi azonosító
* `name`(String) alkalmazott neve
* `qualification`(EmployeeQualification) alkalmazott képesítését jelző enum,
  értékei: MILLING,TURNING,GRINDING,TECHNOLOGIST,PROGRAMMER,DESIGNER
* `canWorkOn`(Machine Lista) n-n kapcsolati attribútum inverse oldal

Végpontok: 

| HTTP metódus|státusz kód | Végpont                 			| Leírás                                                                 |
| ------------| -----------|-----------------------------------------------| ---------------------------------------------------------------------- |
| POST        |201   	| `"/api/employees"`      			| lement egy új alkalmazottat az adatbázisba                             |
| GET         |200   	| `"/api/employees/allEmployees"`		| lekérdezi az összes alkalmazottat az URL-hez fűzott keresési feltételek alapján |
| GET         |200   	| `"/api/employees/{employeeId}"`  		| lekérdez egy alkalmazottat `id` alapján                                     |
| PUT         |200   	| `"/api/employees/update"`  			| frissíti egy alkalmazott adatait a lekérdezés törzsében megadott adatok alapján                                     |
| DELETE      |204   	| `"/api/employees/remove/{employeeId}"`  	| töröl egy alkalmazottat `id` alapján                                     |

A lekérdezések törzsében érkező adatok validálásra kerülnek:

* `id` nem lehet negatív
* `name` nem lehet üres, null, három karakternél rövidebb és nem tartalmazhat speciális karaktert
* `qualification` csak az entitásban megadott típussal lehet létrehozni

Az `"/api/employees/allEmployees"` végponton lévő GET kéréskor a következő szűrési feltételek választhatók:
* feltétel nélkül - minden alkalmazottat listáz,
* `nameFragment` - az alkalmazott neve tartalmazza a megadott szöveget,
* `qualification` - a megadott képesítésű alkalmazottakat listázza,
* `nameFragment&qualification` - az alkalmazott neve tartalmazza a megadott szöveget és rendelkezik a megadott képesítéssel.

Az `"/api/employees/{employeeId}"` GET kéréskor az alkalmazott adatai mellet a hozzárendelt gépek is visszatérnek,
ha nem létezik az alkalmazott 404-es hibakóddal tér vissza.

Az `"/api/employees/update"`PUT kéréskor, ha nem létezik az alkalmazott 404-es hibakóddal tér vissza.

---

### Machine

A `Machine` entitás a következő attribútumokkal rendelkezik:

* `id`(Long) adatbázis által generált egyedi azonosító
* `name`(String) gép neve
* `type`(MachineType) gép tipusát jelző enum, értékei: MILL,LATHE,GRINDER
* `runingJob`(Job Lista) n-n kapcsolati attribútum inverse oldal
* `canUse`(Employee Lista) n-n kapcsolati attribútum owner oldal

A `Machine` és az `Employee` entitások között kétirányú, n-n kapcsolat van.
Egy alkalmazott több különböző de azonos típusú gépen is képes lehet dolgozni és egy gépen több különböző, de megfelelő képesítéssel rendelkező alkalmazott is dolgozhat.

Végpontok:

| HTTP metódus | státusz kód     |Végpont                 | Leírás                                                                 |
| ------------ |------|----------------- | ---------------------------------------------------------------------- |
| POST         |201   | `"/api/machines"`      | lement egy új gépet az adatbázisba                             |
| GET          |200   | `"/api/machines/allMachines"`| lekérdezi az összes gépet az URL-hez fűzott keresési feltételek alapján |
| POST         |201   | `"/api/machines/addEmployee"`  | hozzárendel egy alkalmazottat egy géphez `id` alapján                                 |
| PUT          |200   | `"/api/machines/removeEmployee"`  | megszünteti a kapcsolatot a két entitás között                                    |
| GET          |200   | `"/api/machines/runningJobs/{machineId}"`      | listázza a gépen futó munkákat `id` alapján                             |
| DELETE       |204   | `"/api/machines/remove/{machineId}"`  | töröl egy gépet `id` alapján                                     |

A lekérdezések törzsében érkező adatok validálásra kerülnek:

* `id` nem lehet negatív
* `name` nem lehet üres, null
* `machineType` csak az entitásban megadott típussal lehet létrehozni

Az `"/api/machines/allMachines"` végponton lévő GET kéréskor a következő szűrési feltételek választhatók:
* feltétel nélkül - minden gépet listáz,
* `type` - a típusnak megfeleő gépeket listázza.

Az `"/api/machines/addEmployee"` végponton lévő POST kéréskor, ha az alkalmazott nem rendelkezik a géphez szükséges képesítéssel 400-as hibakóddal tér vissza,
 ha az alkalmazott vagy a gép nem létezik az adatbázisban 404-es hibakóddal. Megfeleő kérés esetén a gép adatai mellett a rajta dolgozó alkalmazottakat is visszaadja.

Az `"/api/machines/remove/{machineId}"` DELETE kéréskor törlés előtt az összes hozzárendelt munka listájából törlődik,
ha nem létezik az gép 404-es hibakóddal tér vissza.

---

### Job

A `Job` entitás a következő attribútumokkal rendelkezik:

* `id`(Long) adatbázis által generált egyedi azonosító
* `customer`(String) megrendelő neve
* `orderDate`(LocalDate) megrendelés dátuma
* `deadline`(LocalDate) megrendelés határideje
* `orderType`(OrderType) munka tipusát jelző enum, értékei: STANDARD,PRIORITY,DISCOUNTED
* `estimatedMachiningHours`(int) kalkulált megmunkálási idő
* `cost`(double) munka összköltsége
* `spentMachiningHours`(int) összes megmunkálási idő
* `status`(JobStatus) munka állapotát jelző enum, értékei: RUNNING,NEW,ON_HOLD,FINISHED,INVOICED
* `spentMachiningHoursByType`(MachineType,Integer Map) megmunkálási idők géptipusonként gyűjtve
* `machinedOn`(Machine Lista) n-n kapcsolati attribútum owner oldal

A `Job` és a `Machine` entitások között kétirányú, n-n kapcsolat van.
Egy munka elvégzéséhez több különböző gépre is szükség lehet és egy gépre több különböző munkát is be lehet sorolni.

Végpontok:

| HTTP metódus |státusz kód| Végpont                 | Leírás                                                                 |
| ------------ |-----------| ----------------------- | ---------------------------------------------------------------------- |
| POST         |201   | `"/api/jobs"`      | lement egy új munkát az adatbázisba                             |
| GET          |200   | `"/api/jobs/allJobs"`| lekérdezi az összes munkát az URL-hez fűzott keresési feltételek alapján |
| POST         |201   | `"/api/jobs/addMachine"`      | hozzáad egy gépet egy munkához                             |
| POST         |201   | `"/api/jobs/addMachiningHours"`      | megmunkálási időt ad egy munkához, a megadott gép alapján |
| PUT          |200   | `"/api/jobs/updateStatus"`      | frissíti a munka állapotát                             |
| GET          |200   | `"/api/jobs/{jobId}/madeByEmployee"`      | listázza a munkán dolgozó alkalmazottakat            |
| GET          |200   | `"/api/jobs/allCost"`      | lekérdezi az összes költséget az URL-hez fűzott keresési feltételek alapján            |
| GET          |200   | `"/api/jobs/id/{jobId}"`      | lekérdez egy munkát `id` alapján           |

A lekérdezések törzsében érkező adatok validálásra kerülnek:

* `id` nem lehet negatív
* `customer` nem lehet üres, null, min. 3 karakter
* `orderDate` múltbéli vagy jelen dátum
* `deadline` jövőbeli dátum
* `orderType` csak az entitásban megadott típussal lehet létrehozni
* `estimatedMachiningHours` nem lehet negatív
* `spentMachiningHours` nem lehet negatív
* `status` csak az entitásban megadott típussal lehet létrehozni

Új munka létrehozásakor a következő attribútumokat kötelező megadni:
* `customer`
* `orderDate`
* `deadline`
* `orderType`
* `estimatedMachiningHours`
A státusz alapértelmezetten `JobStatus.NEW` értékre lesz beállítva, később át lehet állítani.
A többi attribútummot külön HTTP kérésekkel lehet inicializálni.

`Job` entitáshoz tartozó üzleti logikák:
* Egy új munka létrehozása után gépeket lehet hozzárendelni amely gépeken majd gyártva lesz.
* Ha a munka státusza `Jobstatus.RUNNING` akkor lehet
  a hozzárendelt gépeknek megfelelően megmunkálási időket hozzáadni.
* A `cost` attribútum értéke minden hozzáadott megmunkálási órával nő a géptípus,munkatípus és órák számának függvényében.
* Ha a munka státusza `JobStatus.FINISHED`-re lesz állítva akkor a hozzárendelt gépek listája törlődik és a munka lezártnak tekinthető. (A hozzárendelt megmunkálási idők megmaradnak.)

Az `"/api/jobs/allJobs"` végponton lévő GET kéréskor a következő szűrési feltételek választhatók:
* feltétel nélkül - minden munkát listáz,
* `customer` - a munka megrendelője tartalmazza a megadott szöveget,
* `jobStatus` - a megadott státuszú munkákat listázza,
* `customer&jobStatus` - a munka megrendelője tartalmazza a megadott szöveget és megadott státuszú a munka.

Az `"/api/jobs/addMachiningHours"` végponton lévő POST kéréskor, ha a gép nincs a munkához adva vagy a munka státusza nem futó akkor 400-as hibakóddal tér vissza,
ha munka vagy az adatbázis nem található 404-es hibakóddal tér vissza, sikeres kérés esetén a munka adataival és a hozzáadott megmunkálási órák kollekciójával tér vissza.

Az `"/api/jobs/allCost"` végponton lévő GET kéréskor a következő szűrési feltételek választhatók:
* feltétel nélkül - az összes költséget visszaadja,
* `startDate` a lekérés kezdő dátumát lehet megadni
* `endDate` a lekérés végső dátumát lehet megadni
* `startDate&endDate` a két dátum közötti időszak költségét adja vissza

Az `"/api/jobs/id/{jobId}"` vágponton lévő GET kérés a munka összes adatával, a hozzárendelt gépek listájával és a hozzáadott megmunkálási órák kollekciójával tér vissza.

---

## Technológiai részletek

Az alkalmazás 17-es JAVA verzióval készült.

Az ERP project háromrétegű alkalmazás, az alábbi rétegekkel:
* Controller - három osztály (JobController, MachineController, EmployeeController)
* Service - három osztály (JobService, MachineService, EmployeeService)
* Repository - három osztály (JobRepository, MachineRepository, EmployeeRepository)

Az integrációs tesztek WebTestClient segítségével futnak.
A Junit tesztek Mockitoval futnak.

Az alkalmazás MariaDb adatbázist használ.
Az adatbázis migrációt Liquibase végzi.

A mellékelt Dockerfile segítségével Docker image generálható, így az alkalmazás Dockerből is futtatható.

A rest szolgáltatások SwaggerUI segítségével is tesztelhetőek.

---